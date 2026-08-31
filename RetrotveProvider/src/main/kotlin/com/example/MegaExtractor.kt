package com.example

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.Socket
import java.net.URL
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object MegaExtractor {

    private const val TAG = "MegaExtractor"
    private const val MEGA_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36"
    private const val MIN_BYTES_BEFORE_SERVE = 1024 * 1024 // 1MB minimum before serving

    data class MegaUrlInfo(val fileId: String, val key: String)

    fun parseMegaUrl(url: String): MegaUrlInfo? {
        val cleanUrl = url.trim()
        val newFormat = Regex("""mega\.nz/(?:embed|file)/([A-Za-z0-9_-]+)#([A-Za-z0-9_-]+)""")
        newFormat.find(cleanUrl)?.let { return MegaUrlInfo(it.groupValues[1], it.groupValues[2]) }
        val oldFormat = Regex("""mega\.nz/#!([A-Za-z0-9_-]+)!([A-Za-z0-9_-]+)""")
        oldFormat.find(cleanUrl)?.let { return MegaUrlInfo(it.groupValues[1], it.groupValues[2]) }
        Log.w(TAG, "Cannot parse MEGA URL: $url")
        return null
    }

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

    data class MegaFileInfo(val downloadUrl: String, val fileSize: Long, val fileName: String)

    suspend fun getFileInfo(fileId: String, keyBytes: ByteArray): MegaFileInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val apiUrl = "https://g.api.mega.co.nz/cs?"

                Log.d(TAG, "MEGA Step 1: get metadata for $fileId")
                val step1Body = """[{"a":"g","ad":1,"p":"$fileId"}]"""
                val step1Response = megaApiPost(apiUrl, step1Body, sessionId = 0) ?: return@withContext null

                val fileSize = step1Response.optLong("s", 0L)
                val encryptedAttrs = step1Response.optString("at", "")
                val fa = step1Response.optString("fa", "")
                Log.d(TAG, "MEGA metadata: size=$fileSize, fa=$fa")

                val faHash = extractFaHash(fa)
                if (faHash != null) {
                    Log.d(TAG, "MEGA Step 2: ufa with fah=$faHash")
                    megaApiPost(apiUrl, """[{"a":"ufa","fah":"$faHash","r":1,"ssl":1}]""", sessionId = 1)
                    Log.d(TAG, "MEGA Step 2 done")
                }

                val fileName = if (encryptedAttrs.isNotEmpty()) {
                    decryptFileName(encryptedAttrs, keyBytes) ?: "mega_file"
                } else "mega_file"

                MegaFileInfo("", fileSize, fileName)
            } catch (e: Exception) {
                Log.e(TAG, "MEGA API error: ${e.message}", e)
                null
            }
        }
    }

    /**
     * Get a FRESH download URL from Step 3.
     * MEGA URLs are single-use — call this each time we need to (re)download.
     */
    private fun getDownloadUrl(fileId: String): String? {
        val apiUrl = "https://g.api.mega.co.nz/cs?"
        val body = """[{"a":"g","v":2,"g":1,"ssl":1,"p":"$fileId"}]"""
        val response = megaApiPost(apiUrl, body, sessionId = 2) ?: return null

        val g = response.opt("g") ?: return null
        return when (g) {
            is org.json.JSONArray -> if (g.length() > 0) g.getString(0) else null
            is String -> g
            else -> null
        }
    }

    private fun extractFaHash(fa: String): String? {
        if (fa.isEmpty()) return null
        for (entry in fa.split("/")) {
            val parts = entry.split("*")
            if (parts.size == 2 && parts[0].startsWith("111:")) return parts[1]
        }
        val lastParts = fa.split("/").lastOrNull()?.split("*")
        return if (lastParts?.size == 2) lastParts[1] else null
    }

    private fun megaApiPost(apiUrl: String, body: String, sessionId: Int = 0): org.json.JSONObject? {
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
                setRequestProperty("Sec-Fetch-Dest", "empty")
                setRequestProperty("Sec-Fetch-Mode", "cors")
                setRequestProperty("Sec-Fetch-Site", "cross-site")
            }
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val responseCode = conn.responseCode
            val responseText = if (responseCode in 200..299) {
                conn.inputStream.bufferedReader().readText()
            } else conn.errorStream?.bufferedReader()?.readText() ?: ""
            conn.disconnect()
            Log.d(TAG, "MEGA API [$sessionId] ($responseCode): ${responseText.take(200)}")
            if (responseCode !in 200..299) return null
            val trimmed = responseText.trim()
            if (!trimmed.startsWith("[")) return null
            val arr = org.json.JSONArray(trimmed)
            if (arr.length() == 0) return null
            val first = arr.get(0)
            if (first is org.json.JSONObject) {
                if (first.has("e") && first.getInt("e") != 0) {
                    Log.e(TAG, "MEGA API error code: ${first.getInt("e")}")
                }
                return first
            }
            return null
        } catch (e: Exception) {
            Log.e(TAG, "MEGA API failed: ${e.message}", e)
            return null
        }
    }

    private fun decryptFileName(encryptedAttrs: String, keyBytes: ByteArray): String? {
        return try {
            val (aesKey, _) = deriveKeyAndIv(keyBytes)
            val attrBytes = base64UrlDecode(encryptedAttrs)
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(ByteArray(16)))
            val plain = cipher.doFinal(attrBytes)
            if (plain.size >= 4 && plain[0] == 'M'.code.toByte() && plain[1] == 'E'.code.toByte()
                && plain[2] == 'G'.code.toByte() && plain[3] == 'A'.code.toByte()
            ) {
                val json = org.json.JSONObject(String(plain, 4, plain.size - 4, Charsets.UTF_8))
                json.optString("n", null)
            } else null
        } catch (e: Exception) {
            Log.w(TAG, "Cannot decrypt MEGA attrs: ${e.message}")
            null
        }
    }

    // ===== Local HTTP Proxy (Progressive + Resume via fresh URL) =====

    private class DownloadState(val fileSize: Long, val fileId: String, val aesKey: ByteArray, val iv: ByteArray) {
        @Volatile var downloadedBytes: Long = 0L
        @Volatile var downloadComplete = false
        @Volatile var downloadFailed = false
        @Volatile var downloadError: String? = null
        val lock = Object()
        fun notifyProgress() { synchronized(lock) { lock.notifyAll() } }
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

    fun startMegaProxy(downloadUrl: String, fileSize: Long, aesKey: ByteArray, iv: ByteArray, fileId: String): Pair<String, Int>? {
        return try {
            val serverSocket = ServerSocket(0)
            serverSocket.soTimeout = 300000
            val port = serverSocket.localPort
            Log.d(TAG, "MegaProxy started on port $port")
            val tempFile = File.createTempFile("mega_", ".tmp")
            tempFile.deleteOnExit()
            val state = DownloadState(fileSize, fileId, aesKey, iv)
            Thread { downloadLoop(state, tempFile) }.start()
            Thread {
                try {
                    while (!serverSocket.isClosed) {
                        val cs = serverSocket.accept()
                        Thread { handleClient(cs, tempFile, state) }.start()
                    }
                } catch (e: Exception) {
                    if (!serverSocket.isClosed) Log.e(TAG, "MegaProxy server error: ${e.message}")
                }
            }.start()
            Pair("http://127.0.0.1:$port/video", port)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start MegaProxy: ${e.message}")
            null
        }
    }

    private fun downloadLoop(state: DownloadState, tempFile: File) {
        var retryCount = 0
        val MAX_RETRIES = 20

        while (state.downloadedBytes < state.fileSize && retryCount < MAX_RETRIES) {
            try {
                val currentOffset = state.downloadedBytes
                Log.d(TAG, "MEGA download: offset=$currentOffset/${state.fileSize} (attempt ${retryCount + 1})")

                // Get a FRESH download URL each time (MEGA URLs are single-use)
                val url = if (currentOffset == 0L) {
                    // First time: use the URL we already have
                    null // will be passed from startMegaProxy
                } else {
                    // Resume: get fresh URL from Step 3
                    Log.d(TAG, "MEGA: getting fresh download URL for resume...")
                    getDownloadUrl(state.fileId)
                }

                val downloadUrl = if (currentOffset == 0L && retryCount == 0) {
                    // Use the original URL
                    state.lock // just to avoid unused warning
                    // We need the original URL — it's stored somewhere accessible
                    // Actually let's just always get a fresh one for simplicity
                    getDownloadUrl(state.fileId) ?: run {
                        Log.e(TAG, "MEGA: cannot get download URL")
                        break
                    }
                } else {
                    url ?: run {
                        Log.e(TAG, "MEGA: cannot get fresh download URL")
                        retryCount++
                        Thread.sleep(2000)
                        continue
                    }
                }

                val conn = (URL(downloadUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 30000
                    readTimeout = 60000
                    setRequestProperty("User-Agent", MEGA_UA)
                    setRequestProperty("Accept", "*/*")
                    setRequestProperty("Origin", "https://mega.nz")
                    setRequestProperty("Referer", "https://mega.nz/")
                    instanceFollowRedirects = true
                    // NO Range header — MEGA doesn't support it
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
                // Compute IV for current offset (advance by offset/16 blocks)
                val ivForOffset = computeIvForOffset(state.iv, currentOffset)
                cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(state.aesKey, "AES"), IvParameterSpec(ivForOffset))

                // If resuming (offset > 0), we need to skip the first `offset` bytes of plaintext
                // MEGA sends the full file from the beginning, so we skip what we already have
                val bytesToSkip = currentOffset
                var skipped = 0L
                val skipBuffer = ByteArray(256 * 1024)

                val raf = RandomAccessFile(tempFile, "rw")
                raf.seek(currentOffset)

                val readBuffer = ByteArray(256 * 1024)
                var totalReadFromStart = 0L
                var writtenSinceOffset = 0L

                try {
                    while (true) {
                        val bytesRead = encryptedStream.read(readBuffer)
                        if (bytesRead == -1) break
                        totalReadFromStart += bytesRead

                        if (totalReadFromStart <= bytesToSkip) {
                            // Still in the "skip" zone — decrypt but discard
                            cipher.update(readBuffer, 0, bytesRead)
                            continue
                        }

                        // We're past the skip zone — write to file
                        val toProcess: Int
                        val bufferOffset: Int
                        if (skipped < bytesToSkip) {
                            // Partial buffer: some bytes to skip, some to write
                            val skipRemainder = (bytesToSkip - skipped).toInt()
                            val available = totalReadFromStart.toInt() - bytesToSkip.toInt()
                            val skipThisBuf = minOf(skipRemainder, bytesRead)
                            if (skipThisBuf > 0) {
                                cipher.update(readBuffer, 0, skipThisBuf)
                                skipped += skipThisBuf
                            }
                            val writeStart = skipThisBuf
                            val writeLen = bytesRead - skipThisBuf
                            if (writeLen > 0) {
                                val decrypted = cipher.update(readBuffer, writeStart, writeLen)
                                if (decrypted != null && decrypted.isNotEmpty()) {
                                    raf.write(decrypted)
                                    writtenSinceOffset += decrypted.size
                                }
                            }
                        } else {
                            // All bytes are new — write them
                            val decrypted = cipher.update(readBuffer, 0, bytesRead)
                            if (decrypted != null && decrypted.isNotEmpty()) {
                                raf.write(decrypted)
                                writtenSinceOffset += decrypted.size
                            }
                        }

                        state.downloadedBytes = currentOffset + writtenSinceOffset
                        state.notifyProgress()

                        if (state.downloadedBytes % (10 * 1024 * 1024) < 256 * 1024) {
                            Log.d(TAG, "MEGA progress: ${state.downloadedBytes}/${state.fileSize} (${state.downloadedBytes * 100 / state.fileSize}%)")
                        }
                    }
                } finally {
                    raf.close()
                    encryptedStream.close()
                    conn.disconnect()
                }

                Log.d(TAG, "MEGA CDN chunk done: wrote $writtenSinceOffset bytes, total=${state.downloadedBytes}/${state.fileSize}")

                if (state.downloadedBytes >= state.fileSize) {
                    state.downloadComplete = true
                    state.notifyProgress()
                    Log.d(TAG, "MEGA download complete: ${tempFile.length()} bytes")
                    return
                }

                // Connection dropped — retry with fresh URL
                retryCount++
                Log.d(TAG, "MEGA: connection ended at ${state.downloadedBytes}/${state.fileSize}, retrying with fresh URL...")
                Thread.sleep(1000)

            } catch (e: Exception) {
                Log.e(TAG, "MEGA download error: ${e.message}")
                retryCount++
                Thread.sleep(2000)
            }
        }

        if (state.downloadedBytes >= state.fileSize) {
            state.downloadComplete = true
        } else {
            state.downloadFailed = true
            state.downloadError = "Incomplete: ${state.downloadedBytes}/${state.fileSize}"
        }
        state.notifyProgress()
        Log.d(TAG, "MEGA download final: ${state.downloadedBytes}/${state.fileSize} complete=${state.downloadComplete}")
    }

    /**
     * Compute AES-CTR IV advanced by `offset` bytes.
     * CTR increments the 128-bit counter by 1 for each 16-byte block.
     */
    private fun computeIvForOffset(baseIv: ByteArray, offset: Long): ByteArray {
        val iv = baseIv.copyOf()
        var blocks = offset / 16
        // Big-endian increment
        var i = iv.size - 1
        while (blocks > 0 && i >= 0) {
            val sum = (iv[i].toInt() and 0xFF) + (blocks and 0xFF)
            iv[i] = sum.toByte()
            blocks = blocks shr 8
            i--
        }
        return iv
    }

    private fun handleClient(socket: Socket, tempFile: File, state: DownloadState) {
        try {
            val input = BufferedReader(java.io.InputStreamReader(socket.getInputStream()))
            val output = socket.getOutputStream()
            val requestLine = input.readLine() ?: return
            Log.d(TAG, "MegaProxy request: $requestLine")

            val headers = mutableMapOf<String, String>()
            while (true) {
                val line = input.readLine() ?: break
                if (line.isEmpty()) break
                val ci = line.indexOf(':')
                if (ci > 0) headers[line.substring(0, ci).trim().lowercase()] = line.substring(ci + 1).trim()
            }

            // Parse Range
            val rangeHeader = headers["range"]
            var startByte = 0L
            var endByte = state.fileSize - 1
            val isRangeRequest = rangeHeader != null
            if (isRangeRequest) {
                val m = Regex("""bytes=(\d+)-(\d*)""").find(rangeHeader!!)
                if (m != null) {
                    startByte = m.groupValues[1].toLong()
                    if (m.groupValues[2].isNotEmpty()) endByte = m.groupValues[2].toLong()
                }
            }

            // Wait for enough data
            if (startByte >= state.downloadedBytes && !state.downloadComplete) {
                Log.d(TAG, "MegaProxy: waiting for byte $startByte (have ${state.downloadedBytes})...")
                state.waitForData(startByte + 1, 60000)
            }

            // Clamp to available
            val available = if (state.downloadComplete) state.fileSize else state.downloadedBytes
            if (startByte >= available) {
                if (state.downloadComplete) {
                    sendError(socket, 404, "EOF")
                } else {
                    sendError(socket, 503, "Waiting for data")
                }
                return
            }
            endByte = endByte.coerceAtMost(available - 1)
            val contentLength = endByte - startByte + 1

            val status = if (isRangeRequest) "HTTP/1.1 206 Partial Content" else "HTTP/1.1 200 OK"
            val resp = buildString {
                append("$status\r\n")
                append("Content-Type: video/mp4\r\n")
                append("Content-Length: $contentLength\r\n")
                append("Accept-Ranges: bytes\r\n")
                append("Connection: close\r\n")
                if (isRangeRequest) append("Content-Range: bytes $startByte-$endByte/${state.fileSize}\r\n")
                append("\r\n")
            }
            output.write(resp.toByteArray())
            output.flush()

            val raf = RandomAccessFile(tempFile, "r")
            raf.seek(startByte)
            val buf = ByteArray(64 * 1024)
            var remaining = contentLength
            while (remaining > 0) {
                val toRead = minOf(buf.size.toLong(), remaining).toInt()
                val n = raf.read(buf, 0, toRead)
                if (n == -1) break
                output.write(buf, 0, n)
                remaining -= n
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
            val resp = "HTTP/1.1 $code $message\r\nContent-Type: application/json\r\nContent-Length: ${body.length}\r\nConnection: close\r\n\r\n$body"
            socket.getOutputStream().write(resp.toByteArray())
            socket.close()
        } catch (_: Exception) {}
    }

    // ===== Entry Point =====

    suspend fun extractMegaUrl(megaUrl: String): Pair<String, Int>? {
        return withContext(Dispatchers.IO) {
            try {
                val urlInfo = parseMegaUrl(megaUrl)
                if (urlInfo == null) { Log.e(TAG, "Failed to parse MEGA URL"); return@withContext null }
                Log.d(TAG, "Parsed MEGA: fileId=${urlInfo.fileId}, key=${urlInfo.key.take(20)}...")

                val keyBytes = base64UrlDecode(urlInfo.key)
                Log.d(TAG, "Key decoded: ${keyBytes.size} bytes")

                val fileInfo = getFileInfo(urlInfo.fileId, keyBytes)
                if (fileInfo == null) { Log.e(TAG, "Failed to get file info"); return@withContext null }

                val (aesKey, iv) = deriveKeyAndIv(keyBytes)
                Log.d(TAG, "AES key: ${aesKey.size}B, IV: ${iv.size}B")

                // Get first download URL
                val downloadUrl = getDownloadUrl(urlInfo.fileId)
                if (downloadUrl == null) { Log.e(TAG, "No download URL"); return@withContext null }
                Log.d(TAG, "MEGA download URL: ${downloadUrl.take(80)}...")

                val proxy = startMegaProxy(downloadUrl, fileInfo.fileSize, aesKey, iv, urlInfo.fileId)
                if (proxy == null) { Log.e(TAG, "Failed to start proxy"); return@withContext null }
                Log.d(TAG, "MEGA proxy ready: ${proxy.first}")
                proxy
            } catch (e: Exception) {
                Log.e(TAG, "MEGA extraction failed: ${e.message}", e)
                null
            }
        }
    }
}
