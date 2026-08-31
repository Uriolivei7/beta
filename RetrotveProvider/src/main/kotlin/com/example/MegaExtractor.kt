package com.example

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
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
 */
object MegaExtractor {

    private const val TAG = "MegaExtractor"
    private const val MEGA_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36"

    // ===== URL Parsing =====

    data class MegaUrlInfo(
        val fileId: String,
        val key: String,  // base64url-encoded, 32 bytes after decode
    )

    fun parseMegaUrl(url: String): MegaUrlInfo? {
        // Handle: mega.nz/embed/HANDLE#KEY or mega.nz/file/HANDLE#KEY or mega.nz/#!HANDLE!KEY
        val cleanUrl = url.trim()

        // New format: /embed/HANDLE#KEY or /file/HANDLE#KEY
        val newFormat = Regex("""mega\.nz/(?:embed|file)/([A-Za-z0-9_-]+)#([A-Za-z0-9_-]+)""")
        newFormat.find(cleanUrl)?.let { match ->
            return MegaUrlInfo(match.groupValues[1], match.groupValues[2])
        }

        // Old format: /#!HANDLE!KEY
        val oldFormat = Regex("""mega\.nz/#!([A-Za-z0-9_-]+)!([A-Za-z0-9_-]+)""")
        oldFormat.find(cleanUrl)?.let { match ->
            return MegaUrlInfo(match.groupValues[1], match.groupValues[2])
        }

        Log.w(TAG, "Cannot parse MEGA URL: $url")
        return null
    }

    // ===== Key Derivation =====

    /**
     * Decode base64url (with -_ instead of +/, no padding) to ByteArray
     */
    private fun base64UrlDecode(input: String): ByteArray {
        var padded = input.replace('-', '+').replace('_', '/')
        while (padded.length % 4 != 0) padded += "="
        return Base64.decode(padded, Base64.DEFAULT)
    }

    /**
     * Derive AES-128 key and IV from MEGA file key.
     * Key format: 32 bytes where:
     *   aes_key[i] = key_bytes[i] XOR key_bytes[i+16]  for i in 0..15
     *   iv[0..7] = key_bytes[16..23], iv[8..15] = 0
     */
    private fun deriveKeyAndIv(keyBytes: ByteArray): Pair<ByteArray, ByteArray> {
        require(keyBytes.size >= 32) { "MEGA key must be at least 32 bytes, got ${keyBytes.size}" }

        val aesKey = ByteArray(16)
        for (i in 0 until 16) {
            aesKey[i] = (keyBytes[i].toInt() xor keyBytes[i + 16].toInt()).toByte()
        }

        val iv = ByteArray(16)
        System.arraycopy(keyBytes, 16, iv, 0, 8)
        // iv[8..15] stays 0

        return Pair(aesKey, iv)
    }

    // ===== MEGA API =====

    data class MegaFileInfo(
        val downloadUrl: String,
        val fileSize: Long,
        val fileName: String,
    )

    /**
     * Call MEGA API to get file download info.
     * 3-step flow discovered from browser DevTools:
     *   1. {"a":"g","ad":1,"p":"HANDLE"} → metadata (s, at, fa, fh) but NO download URL
     *   2. {"a":"ufa","fah":"HASH","r":1,"ssl":1} → unlock file access (anti-abuse)
     *   3. {"a":"g","v":2,"g":1,"ssl":1,"p":"HANDLE"} → actual download URLs (array of CDN URLs)
     */
    suspend fun getFileInfo(fileId: String, keyBytes: ByteArray): MegaFileInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val apiUrl = "https://g.api.mega.co.nz/cs?"

                // === STEP 1: Get metadata ===
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

                // === STEP 2: Unlock file access (ufa) ===
                // Extract hash from fa field: format is "type:format*hash/..."
                // We need the hash from the entry containing "111:" or the last one
                val faHash = extractFaHash(fa)
                if (faHash != null) {
                    Log.d(TAG, "MEGA Step 2: ufa with fah=$faHash")
                    val step2Body = """[{"a":"ufa","fah":"$faHash","r":1,"ssl":1}]"""
                    megaApiPost(apiUrl, step2Body, sessionId = 1)
                    Log.d(TAG, "MEGA Step 2 done (anti-abuse unlock)")
                } else {
                    Log.w(TAG, "MEGA Step 2: no fa hash found, skipping ufa")
                }

                // === STEP 3: Get download URLs ===
                Log.d(TAG, "MEGA Step 3: get download URLs")
                val step3Body = """[{"a":"g","v":2,"g":1,"ssl":1,"p":"$fileId"}]"""
                val step3Response = megaApiPost(apiUrl, step3Body, sessionId = 2)
                if (step3Response == null) {
                    Log.e(TAG, "MEGA Step 3 failed")
                    return@withContext null
                }

                // g can be a string (single URL) or array (multiple CDN URLs)
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

                // Decrypt filename
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

    /**
     * Extract the file attribute hash from the fa field.
     * fa format: "394:8*zPQ_kgZ5wMo/881:0*hJ3X_8Xa1LY/111:1*t3Jqn-LQ6aE"
     * We want the hash from the "111:" entry (file attribute hash).
     */
    private fun extractFaHash(fa: String): String? {
        if (fa.isEmpty()) return null
        // Try to find 111:* entry first, fallback to last entry
        val entries = fa.split("/")
        for (entry in entries) {
            val parts = entry.split("*")
            if (parts.size == 2 && parts[0].startsWith("111:")) {
                return parts[1]
            }
        }
        // Fallback: use last entry's hash
        val lastEntry = entries.lastOrNull()
        val lastParts = lastEntry?.split("*")
        return if (lastParts?.size == 2) lastParts[1] else null
    }

    /**
     * Low-level MEGA API POST request.
     * Returns the first element of the JSON array response, or null on error.
     */
    private fun megaApiPost(
        apiUrl: String,
        body: String,
        sessionId: Int = 0,
    ): org.json.JSONObject? {
        try {
            val conn = (java.net.URL(apiUrl).openConnection() as java.net.HttpURLConnection).apply {
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
                        // Don't return null for step 1 - ad:[-9] is expected
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

    /**
     * Decrypt filename from MEGA encrypted attributes.
     * Attributes are AES-CBC encrypted with the key, zero IV.
     * Plaintext starts with "MEGA" prefix then JSON: {"n":"filename.ext"}
     */
    private fun decryptFileName(encryptedAttrs: String, keyBytes: ByteArray): String? {
        return try {
            val (aesKey, _) = deriveKeyAndIv(keyBytes)

            val attrBytes = base64UrlDecode(encryptedAttrs)
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(aesKey, "AES"),
                IvParameterSpec(ByteArray(16))  // zero IV for attributes
            )
            val plainAttrs = cipher.doFinal(attrBytes)

            // Check for "MEGA" prefix
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

    // ===== Local HTTP Proxy Server =====

    /**
     * Start a local HTTP server that proxies MEGA content with on-the-fly AES-CTR decryption.
     * Returns the local URL for ExoPlayer to connect to.
     */
    fun startMegaProxy(
        downloadUrl: String,
        fileSize: Long,
        aesKey: ByteArray,
        iv: ByteArray,
    ): Pair<String, Int>? {
        return try {
            val serverSocket = ServerSocket(0) // random available port
            serverSocket.soTimeout = 60000 // 60s timeout
            val port = serverSocket.localPort
            Log.d(TAG, "MegaProxy started on port $port")

            // Start server thread
            Thread {
                try {
                    while (!serverSocket.isClosed) {
                        val clientSocket = serverSocket.accept()
                        Thread { handleClient(clientSocket, downloadUrl, fileSize, aesKey, iv) }.start()
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

    private fun handleClient(
        socket: Socket,
        downloadUrl: String,
        fileSize: Long,
        aesKey: ByteArray,
        iv: ByteArray,
    ) {
        try {
            val input = BufferedReader(InputStreamReader(socket.getInputStream()))
            val output = socket.getOutputStream()

            // Read HTTP request line
            val requestLine = input.readLine() ?: return
            Log.d(TAG, "MegaProxy request: $requestLine")

            // Read headers
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
            var endByte = fileSize - 1
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

            val contentLength = endByte - startByte + 1

            // Send HTTP response headers
            val statusLine = if (isRangeRequest) "HTTP/1.1 206 Partial Content" else "HTTP/1.1 200 OK"
            val responseHeaders = buildString {
                append("$statusLine\r\n")
                append("Content-Type: video/mp4\r\n")
                append("Content-Length: $contentLength\r\n")
                append("Accept-Ranges: bytes\r\n")
                append("Connection: close\r\n")
                if (isRangeRequest) {
                    append("Content-Range: bytes $startByte-$endByte/$fileSize\r\n")
                }
                append("\r\n")
            }

            output.write(responseHeaders.toByteArray())
            output.flush()

            // Fetch and decrypt the requested byte range from MEGA
            fetchAndDecryptRange(downloadUrl, startByte, endByte, aesKey, iv, output)

            output.flush()
            socket.close()
            Log.d(TAG, "MegaProxy response sent: $startByte-$endByte ($contentLength bytes)")
        } catch (e: Exception) {
            Log.e(TAG, "MegaProxy client error: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    /**
     * Fetch encrypted byte range from MEGA and decrypt AES-CTR on-the-fly.
     */
    private fun fetchAndDecryptRange(
        downloadUrl: String,
        startByte: Long,
        endByte: Long,
        aesKey: ByteArray,
        iv: ByteArray,
        output: OutputStream,
    ) {
        try {
            // MEGA download URLs support Range requests
            val rangeUrl = URL(downloadUrl)
            val conn = rangeUrl.openConnection() as HttpURLConnection
            conn.setRequestProperty("Range", "bytes=$startByte-$endByte")
            conn.setRequestProperty("User-Agent", "Mozilla/5.0")
            conn.connectTimeout = 15000
            conn.readTimeout = 30000

            val responseCode = conn.responseCode
            if (responseCode !in listOf(200, 206)) {
                Log.e(TAG, "MEGA download error: HTTP $responseCode")
                return
            }

            val encryptedStream: InputStream = conn.inputStream

            // Compute AES-CTR initial counter for this byte offset
            // Counter = iv + (startByte / 16) as big-endian increment
            val counter = ByteArray(16)
            System.arraycopy(iv, 0, counter, 0, 16)

            // Increment counter by startByte / 16
            var blockOffset = startByte / 16
            for (i in 15 downTo 0) {
                if (blockOffset == 0L) break
                val carry = (counter[i].toInt() and 0xFF) + (blockOffset and 0xFF)
                counter[i] = (carry and 0xFF).toByte()
                blockOffset = blockOffset shr 8
            }

            // If startByte is not block-aligned, we need to decrypt from the block start
            // and discard the prefix bytes
            val blockAlignOffset = (startByte % 16).toInt()

            val cipher = Cipher.getInstance("AES/CTR/NoPadding")
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(aesKey, "AES"),
                IvParameterSpec(counter)
            )

            // If not block-aligned, decrypt dummy bytes to advance the CTR counter
            if (blockAlignOffset > 0) {
                val dummy = ByteArray(blockAlignOffset)
                cipher.update(dummy) // advance counter, discard output
            }

            // Decrypt and stream the actual content
            val bufferSize = 64 * 1024  // 64KB chunks
            val buffer = ByteArray(bufferSize)
            var totalRead = 0L
            val totalToRead = endByte - startByte + 1

            while (totalRead < totalToRead) {
                val bytesToRead = minOf(bufferSize.toLong(), totalToRead - totalRead).toInt()
                val bytesRead = encryptedStream.read(buffer, 0, bytesToRead)
                if (bytesRead == -1) break

                val decrypted = cipher.update(buffer, 0, bytesRead)
                if (decrypted != null && decrypted.isNotEmpty()) {
                    output.write(decrypted)
                    totalRead += bytesRead
                }
            }

            encryptedStream.close()
            conn.disconnect()
        } catch (e: Exception) {
            Log.e(TAG, "MEGA fetch/decrypt error: ${e.message}")
        }
    }

    // ===== Main Entry Point =====

    /**
     * Extract a playable local proxy URL from a MEGA link.
     * Returns Pair(localUrl, port) or null on failure.
     */
    suspend fun extractMegaUrl(megaUrl: String): Pair<String, Int>? {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Parse URL
                val urlInfo = parseMegaUrl(megaUrl)
                if (urlInfo == null) {
                    Log.e(TAG, "Failed to parse MEGA URL: $megaUrl")
                    return@withContext null
                }
                Log.d(TAG, "Parsed MEGA: fileId=${urlInfo.fileId}, key=${urlInfo.key.take(20)}...")

                // 2. Decode key
                val keyBytes = base64UrlDecode(urlInfo.key)
                Log.d(TAG, "Key decoded: ${keyBytes.size} bytes")

                // 3. Get file info from API
                val fileInfo = getFileInfo(urlInfo.fileId, keyBytes)
                if (fileInfo == null) {
                    Log.e(TAG, "Failed to get MEGA file info")
                    return@withContext null
                }

                // 4. Derive AES key and IV
                val (aesKey, iv) = deriveKeyAndIv(keyBytes)
                Log.d(TAG, "AES key derived: ${aesKey.size} bytes, IV: ${iv.size} bytes")

                // 5. Start local proxy server
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
