package com.example

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.Socket
import java.net.URL
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * MEGA.nz local proxy extractor.
 *
 * Flow:
 * 1. Parse file_id + key from mega.nz URL
 * 2. Call MEGA API to get temporary download URL
 * 3. Start local HTTP server on random port
 * 4. ExoPlayer connects to localhost → proxy fetches from MEGA → decrypts AES-CTR → serves plaintext
 *
 * Progressive download: serves data as it arrives, resumes if MEGA CDN drops connection.
 */
object MegaExtractor {

    private const val TAG = "MegaExtractor"
    private const val MEGA_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36"

    // ===== URL Parsing =====

    data class MegaUrlInfo(
        val fileId: String,
        val key: String,
    )

    fun parseMegaUrl(url: String): MegaUrlInfo? {
        val cleanUrl = url.trim()
        val newFormat = Regex("""mega\.nz/(?:embed|file)/([A-Za-z0-9_-]+)#([A-Za-z0-9_-]+)""")
        newFormat.find(cleanUrl)?.let { match ->
            return MegaUrlInfo(match.groupValues[1], match.groupValues[2])
        }
        val oldFormat = Regex("""mega\.nz/#!([A-Za-z0-9_-]+)!([A-Za-z0-9_-]+)""")
        oldFormat.find(cleanUrl)?.let { match ->
            return MegaUrlInfo(match.groupValues[1], match.groupValues[2])
        }
        Log.w(TAG, "Cannot parse MEGA URL: $url")
        return null
    }

    // ===== Key Derivation =====

    private fun base64UrlDecode(input: String): ByteArray {
        var padded = input.replace('-', '+').replace('_', '/')
        while (padded.length % 4 != 0) padded += "="
        return Base64.decode(padded, Base64.DEFAULT)
    }

    private fun deriveKeyAndIv(keyBytes: ByteArray): Pair<ByteArray, ByteArray> {
        require(keyBytes.size >= 32) { "MEGA key must be at least 32 bytes, got ${keyBytes.size}" }
        val aesKey = ByteArray(16)
        for (i in 0 until 16) {
            aesKey[i] = (keyBytes[i].toInt() xor keyBytes[i + 16].toInt()).toByte()
        }
        val iv = ByteArray(16)
        System.arraycopy(keyBytes, 16, iv, 0, 8)
        return Pair(aesKey, iv)
    }

    // ===== MEGA API =====

    data class MegaFileInfo(
        val downloadUrl: String,
        val fileSize: Long,
        val fileName: String,
    )

    suspend fun getFileInfo(fileId: String, keyBytes: ByteArray): MegaFileInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val apiUrl = "https://g.api.mega.co.nz/cs?"

                Log.d(TAG, "MEGA Step 1: get metadata for $fileId")
                val step1Body = """[{"a":"g","ad":1,"p":"$fileId"}]"""
                val step1Response = megaApiPost(apiUrl, step1Body, sessionId = 0)
                if (step1Response == null) {
                    Log.e(TAG, "MEGA Step 1 failed")
                    return@withContext null
                }

                val metadata = step1Response
                val fileSize = metadata.optLong("s", 0L)
                val encryptedAttrs = metadata.optString("at", "")
                val fileHandle = metadata.optString("fh", "")
                val fa = metadata.optString("fa", "")

                Log.d(TAG, "MEGA metadata: size=$fileSize, handle=$fileHandle, fa=$fa")

                val faHash = extractFaHash(fa)
                if (faHash != null) {
                    Log.d(TAG, "MEGA Step 2: ufa with fah=$faHash")
                    val step2Body = """[{"a":"ufa","fah":"$faHash","r":1,"ssl":1}]"""
                    megaApiPost(apiUrl, step2Body, sessionId = 1)
                    Log.d(TAG, "MEGA Step 2 done (anti-abuse unlock)")
                } else {
                    Log.w(TAG, "MEGA Step 2: no fa hash found, skipping ufa")
                }

                Log.d(TAG, "MEGA Step 3: get download URLs")
                val step3Body = """[{"a":"g","v":2,"g":1,"ssl":1,"p":"$fileId"}]"""
                val step3Response = megaApiPost(apiUrl, step3Body, sessionId = 2)
                if (step3Response == null) {
                    Log.e(TAG, "MEGA Step 3 failed")
                    return@withContext null
                }

                val downloadUrl = when {
                    step3Response.has("g") -> {
                        val g = step3Response.get("g")
                        when (g) {
                            is org.json.JSONArray -> {
                                if (g.length() > 0) g.getString(0) else null
                            }
                            is String -> g
                            else -> null
                        }
                    }
                    else -> null
                }

                if (downloadUrl.isNullOrEmpty()) {
                    Log.e(TAG, "MEGA: no download URL in step 3 response")
                    return@withContext null
                }

                val fileName = if (encryptedAttrs.isNotEmpty()) {
                    decryptFileName(encryptedAttrs, keyBytes) ?: "mega_file"
                } else {
                    "mega_file"
                }

                Log.d(TAG, "MEGA file: name=$fileName, size=$fileSize")
                Log.d(TAG, "MEGA download URL: ${downloadUrl.take(100)}...")

                MegaFileInfo(downloadUrl, fileSize, fileName)
            } catch (e: Exception) {
                Log.e(TAG, "MEGA API error: ${e.message}", e)
                null
            }
        }
    }

    private fun extractFaHash(fa: String): String? {
        if (fa.isEmpty()) return null
        val entries = fa.split("/")
        for (entry in entries) {
            val parts = entry.split("*")
            if (parts.size == 2 && parts[0].startsWith("111:")) {
                return parts[1]
            }
        }
        val lastEntry = entries.lastOrNull()
        val lastParts = lastEntry?.split("*")
        return if (lastParts?.size == 2) lastParts[1] else null
    }

    private fun megaApiPost(
        apiUrl: String,
        body: String,
        sessionId: Int = 0,
    ): org.json.JSONObject? {
        try {
            val conn = (URL(apiUrl).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                doOutput = true
                connectTimeout = 15000
                readTimeout = 30000
                setRequestProperty("Content-Type", "text/plain;charset=UTF-8")
                setRequestProperty("User-Agent", MEGA_UA)
                setRequestProperty("Origin", "https://mega.nz")
                setRequestProperty("Referer", "https://mega.nz/")
                setRequestProperty("MEGA-Chrome-Antileak", "/cs?id=$sessionId&v=3&lang=es&wcv=7.1.2&domain=meganz")
                setRequestProperty("Accept", "*/*")
                setRequestProperty("Accept-Language", "es-ES,es;q=0.6")
                setRequestProperty("Connection", "keep-alive")
                setRequestProperty("Sec-Fetch-Dest", "empty")
                setRequestProperty("Sec-Fetch-Mode", "cors")
                setRequestProperty("Sec-Fetch-Site", "cross-site")
            }

            conn.outputStream.use { os ->
                os.write(body.toByteArray(Charsets.UTF_8))
            }

            val responseCode = conn.responseCode
            val responseText = if (responseCode in 200..299) {
                conn.inputStream.bufferedReader().readText()
            } else {
                conn.errorStream?.bufferedReader()?.readText() ?: ""
            }
            conn.disconnect()

            Log.d(TAG, "MEGA API [$sessionId] ($responseCode): ${responseText.take(300)}")

            if (responseCode !in 200..299) {
                Log.e(TAG, "MEGA API HTTP error: $responseCode")
                return null
            }

            val trimmed = responseText.trim()
            if (trimmed.startsWith("[")) {
                val jsonArray = org.json.JSONArray(trimmed)
                if (jsonArray.length() == 0) return null

                val firstElement = jsonArray.get(0)
                if (firstElement is org.json.JSONObject) {
                    if (firstElement.has("e") && firstElement.getInt("e") != 0) {
                        Log.e(TAG, "MEGA API error code: ${firstElement.getInt("e")}")
                    }
                    return firstElement
                } else {
                    Log.e(TAG, "MEGA API unexpected response: $firstElement")
                    return null
                }
            } else {
                Log.e(TAG, "MEGA API error: $trimmed")
                return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "MEGA API request failed: ${e.message}", e)
            return null
        }
    }

    private fun decryptFileName(encryptedAttrs: String, keyBytes: ByteArray): String? {
        return try {
            val (aesKey, _) = deriveKeyAndIv(keyBytes)
            val attrBytes = base64UrlDecode(encryptedAttrs)
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(aesKey, "AES"),
                IvParameterSpec(ByteArray(16))
            )
            val plainAttrs = cipher.doFinal(attrBytes)
            if (plainAttrs.size >= 4 &&
                plainAttrs[0] == 'M'.code.toByte() &&
                plainAttrs[1] == 'E'.code.toByte() &&
                plainAttrs[2] == 'G'.code.toByte() &&
                plainAttrs[3] == 'A'.code.toByte()
            ) {
                val jsonStr = String(plainAttrs, 4, plainAttrs.size - 4, Charsets.UTF_8)
                val json = org.json.JSONObject(jsonStr)
                json.optString("n", null)
            } else {
                Log.w(TAG, "MEGA attributes don't start with MEGA prefix")
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Cannot decrypt MEGA attributes: ${e.message}")
            null
        }
    }

    // ===== Local HTTP Proxy Server (Progressive Download) =====

    /**
     * Shared state for download progress.
     */
    private class DownloadState(val fileSize: Long) {
        @Volatile var downloadedBytes: Long = 0L
        @Volatile var downloadComplete = false
        @Volatile var downloadFailed = false
        @Volatile var downloadError: String? = null

        val lock = Object()

        fun notifyProgress() {
            synchronized(lock) { lock.notifyAll() }
        }

        fun waitForData(minBytes: Long, timeoutMs: Long): Boolean {
            val deadline = System.currentTimeMillis() + timeoutMs
            synchronized(lock) {
                while (downloadedBytes < minBytes && !downloadComplete && !downloadFailed) {
                    val remaining = deadline - System.currentTimeMillis()
                    if (remaining <= 0) return false
                    lock.wait(remaining.coerceAtLeast(100))
                }
            }
            return downloadedBytes >= minBytes || downloadComplete
        }
    }

    fun startMegaProxy(
        downloadUrl: String,
        fileSize: Long,
        aesKey: ByteArray,
        iv: ByteArray,
    ): Pair<String, Int>? {
        return try {
            val serverSocket = ServerSocket(0)
            serverSocket.soTimeout = 300000
            val port = serverSocket.localPort
            Log.d(TAG, "MegaProxy started on port $port")

            val tempFile = File.createTempFile("mega_", ".tmp")
            tempFile.deleteOnExit()
            val state = DownloadState(fileSize)

            // Download thread: progressive with resume
            Thread {
                downloadWithResume(downloadUrl, fileSize, aesKey, iv, tempFile, state)
            }.start()

            // Server thread
            Thread {
                try {
                    while (!serverSocket.isClosed) {
                        val clientSocket = serverSocket.accept()
                        Thread {
                            handleClientProgressive(clientSocket, tempFile, state)
                        }.start()
                    }
                } catch (e: Exception) {
                    if (!serverSocket.isClosed) {
                        Log.e(TAG, "MegaProxy server error: ${e.message}")
                    }
                }
            }.start()

            Pair("http://127.0.0.1:$port/video", port)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start MegaProxy: ${e.message}")
            null
        }
    }

    private fun downloadWithResume(
        downloadUrl: String,
        fileSize: Long,
        aesKey: ByteArray,
        iv: ByteArray,
        outputFile: File,
        state: DownloadState,
    ) {
        val MAX_RETRIES = 10
        var retryCount = 0

        while (state.downloadedBytes < fileSize && retryCount < MAX_RETRIES) {
            try {
                val offset = state.downloadedBytes
                Log.d(TAG, "MEGA CDN connect: offset=$offset/$fileSize (attempt ${retryCount + 1})")

                val conn = (URL(downloadUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 30000
                    readTimeout = 60000
                    setRequestProperty("User-Agent", MEGA_UA)
                    setRequestProperty("Accept", "*/*")
                    setRequestProperty("Origin", "https://mega.nz")
                    setRequestProperty("Referer", "https://mega.nz/")
                    instanceFollowRedirects = true
                    if (offset > 0) {
                        setRequestProperty("Range", "bytes=$offset-")
                    }
                }

                val responseCode = conn.responseCode
                Log.d(TAG, "MEGA CDN response: HTTP $responseCode")
                if (responseCode !in listOf(200, 206)) {
                    conn.disconnect()
                    retryCount++
                    Thread.sleep(2000)
                    continue
                }

                val encryptedStream = conn.inputStream
                val cipher = Cipher.getInstance("AES/CTR/NoPadding")
                // For resume: advance IV by offset/16 blocks
                val ivForResume = iv.copyOf()
                if (offset > 0) {
                    var blocks = offset / 16
                    var carry = (offset % 16).toInt()
                    var i = 15
                    while (blocks > 0 && i >= 0) {
                        val sum = (ivForResume[i].toInt() and 0xFF) + (blocks and 0xFF)
                        ivForResume[i] = sum.toByte()
                        blocks = blocks shr 8
                        i--
                    }
                    // Handle partial block carry
                    if (carry > 0) {
                        i = 15
                        while (carry > 0 && i >= 0) {
                            val sum = (ivForResume[i].toInt() and 0xFF) + carry
                            ivForResume[i] = sum.toByte()
                            carry = sum shr 8
                            i--
                        }
                    }
                }
                cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(ivForResume))

                // Skip partial block bytes if resume is not block-aligned
                val partialBlockOffset = (offset % 16).toInt()
                if (partialBlockOffset > 0) {
                    val skipBuf = ByteArray(partialBlockOffset)
                    var skipRemaining = partialBlockOffset
                    while (skipRemaining > 0) {
                        val skipped = encryptedStream.read(skipBuf, 0, skipRemaining)
                        if (skipped == -1) break
                        cipher.update(skipBuf, 0, skipped)
                        skipRemaining -= skipped
                    }
                }

                val bufferSize = 256 * 1024
                val buffer = ByteArray(bufferSize)
                var totalRead = offset
                val fos = FileOutputStream(outputFile, offset > 0)

                try {
                    while (totalRead < fileSize) {
                        val bytesToRead = minOf(bufferSize.toLong(), fileSize - totalRead).toInt()
                        val bytesRead = encryptedStream.read(buffer, 0, bytesToRead)
                        if (bytesRead == -1) {
                            Log.w(TAG, "MEGA CDN: connection dropped at $totalRead/$fileSize")
                            break
                        }

                        val decrypted = cipher.update(buffer, 0, bytesRead)
                        if (decrypted != null && decrypted.isNotEmpty()) {
                            fos.write(decrypted)
                        }
                        totalRead += bytesRead
                        state.downloadedBytes = totalRead

                        if (totalRead % (10 * 1024 * 1024) == 0L || totalRead == fileSize) {
                            Log.d(TAG, "MEGA progress: $totalRead/$fileSize (${totalRead * 100 / fileSize}%)")
                        }
                        state.notifyProgress()
                    }
                } finally {
                    fos.flush()
                    fos.close()
                    encryptedStream.close()
                    conn.disconnect()
                }

                if (totalRead >= fileSize) {
                    state.downloadComplete = true
                    state.notifyProgress()
                    Log.d(TAG, "MEGA download complete: ${outputFile.length()} bytes")
                    return
                }

                // Connection dropped, retry with resume
                retryCount++
                Log.d(TAG, "MEGA resume: retrying from $totalRead (attempt $retryCount)")
                Thread.sleep(1000)

            } catch (e: Exception) {
                Log.e(TAG, "MEGA download error: ${e.message}")
                retryCount++
                Thread.sleep(2000)
            }
        }

        if (state.downloadedBytes >= fileSize) {
            state.downloadComplete = true
        } else {
            state.downloadFailed = true
            state.downloadError = "Download incomplete: ${state.downloadedBytes}/$fileSize after $retryCount retries"
        }
        state.notifyProgress()
        Log.d(TAG, "MEGA download final: ${state.downloadedBytes}/$fileSize complete=${state.downloadComplete} failed=${state.downloadFailed}")
    }

    private fun handleClientProgressive(
        socket: Socket,
        tempFile: File,
        state: DownloadState,
    ) {
        try {
            val input = BufferedReader(InputStreamReader(socket.getInputStream()))
            val output = socket.getOutputStream()

            val requestLine = input.readLine() ?: return
            Log.d(TAG, "MegaProxy request: $requestLine")

            val headers = mutableMapOf<String, String>()
            while (true) {
                val line = input.readLine() ?: break
                if (line.isEmpty()) break
                val colonIdx = line.indexOf(':')
                if (colonIdx > 0) {
                    headers[line.substring(0, colonIdx).trim().lowercase()] =
                        line.substring(colonIdx + 1).trim()
                }
            }

            // Parse Range header
            val rangeHeader = headers["range"]
            var startByte = 0L
            var endByte = state.fileSize - 1
            val isRangeRequest = rangeHeader != null

            if (isRangeRequest) {
                val rangeMatch = Regex("""bytes=(\d+)-(\d*)""").find(rangeHeader!!)
                if (rangeMatch != null) {
                    startByte = rangeMatch.groupValues[1].toLong()
                    if (rangeMatch.groupValues[2].isNotEmpty()) {
                        endByte = rangeMatch.groupValues[2].toLong()
                    }
                }
            }

            // If request is beyond what's downloaded yet, wait for it
            if (startByte > state.downloadedBytes && !state.downloadComplete) {
                Log.d(TAG, "MegaProxy: client wants byte $startByte, have ${state.downloadedBytes} — waiting...")
                state.waitForData(startByte, 30000)
            }

            // Clamp endByte to what we have
            val availableEnd = if (state.downloadComplete) state.fileSize - 1 else (state.downloadedBytes - 1).coerceAtLeast(0)
            if (endByte > availableEnd && startByte <= availableEnd) {
                endByte = availableEnd
            }

            if (startByte > availableEnd && !state.downloadComplete) {
                sendError(socket, 503, "Data not yet available")
                return
            }

            val contentLength = endByte - startByte + 1
            val statusLine = if (isRangeRequest) "HTTP/1.1 206 Partial Content" else "HTTP/1.1 200 OK"
            val responseHeaders = buildString {
                append("$statusLine\r\n")
                append("Content-Type: video/mp4\r\n")
                append("Content-Length: $contentLength\r\n")
                append("Accept-Ranges: bytes\r\n")
                append("Connection: close\r\n")
                if (isRangeRequest) {
                    append("Content-Range: bytes $startByte-$endByte/${state.fileSize}\r\n")
                }
                append("\r\n")
            }

            output.write(responseHeaders.toByteArray())
            output.flush()

            // Serve from temp file
            val raf = RandomAccessFile(tempFile, "r")
            raf.seek(startByte)
            val buffer = ByteArray(64 * 1024)
            var remaining = contentLength

            while (remaining > 0) {
                val toRead = minOf(buffer.size.toLong(), remaining).toInt()
                val bytesRead = raf.read(buffer, 0, toRead)
                if (bytesRead == -1) break
                output.write(buffer, 0, bytesRead)
                remaining -= bytesRead
            }

            raf.close()
            output.flush()
            socket.close()
            Log.d(TAG, "MegaProxy served: $startByte-$endByte ($contentLength bytes)")
        } catch (e: Exception) {
            Log.e(TAG, "MegaProxy client error: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun sendError(socket: Socket, code: Int, message: String) {
        try {
            val body = "{\"error\":\"$message\"}"
            val response = "HTTP/1.1 $code $message\r\nContent-Type: application/json\r\nContent-Length: ${body.length}\r\nConnection: close\r\n\r\n$body"
            socket.getOutputStream().write(response.toByteArray())
            socket.close()
        } catch (_: Exception) {}
    }

    // ===== Main Entry Point =====

    suspend fun extractMegaUrl(megaUrl: String): Pair<String, Int>? {
        return withContext(Dispatchers.IO) {
            try {
                val urlInfo = parseMegaUrl(megaUrl)
                if (urlInfo == null) {
                    Log.e(TAG, "Failed to parse MEGA URL: $megaUrl")
                    return@withContext null
                }
                Log.d(TAG, "Parsed MEGA: fileId=${urlInfo.fileId}, key=${urlInfo.key.take(20)}...")

                val keyBytes = base64UrlDecode(urlInfo.key)
                Log.d(TAG, "Key decoded: ${keyBytes.size} bytes")

                val fileInfo = getFileInfo(urlInfo.fileId, keyBytes)
                if (fileInfo == null) {
                    Log.e(TAG, "Failed to get MEGA file info")
                    return@withContext null
                }

                val (aesKey, iv) = deriveKeyAndIv(keyBytes)
                Log.d(TAG, "AES key derived: ${aesKey.size} bytes, IV: ${iv.size} bytes")

                val proxy = startMegaProxy(fileInfo.downloadUrl, fileInfo.fileSize, aesKey, iv)
                if (proxy == null) {
                    Log.e(TAG, "Failed to start MEGA proxy")
                    return@withContext null
                }

                Log.d(TAG, "MEGA proxy ready: ${proxy.first}")
                proxy
            } catch (e: Exception) {
                Log.e(TAG, "MEGA extraction failed: ${e.message}", e)
                null
            }
        }
    }
}
