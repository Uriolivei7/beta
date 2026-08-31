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
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

object MegaExtractor {

    private const val TAG = "MegaExtractor"
    private const val MEGA_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36"

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
        require(keyBytes.size >= 32)
        val aesKey = ByteArray(16)
        for (i in 0 until 16) aesKey[i] = (keyBytes[i].toInt() xor keyBytes[i + 16].toInt()).toByte()
        val iv = ByteArray(16)
        System.arraycopy(keyBytes, 16, iv, 0, 8)
        return Pair(aesKey, iv)
    }

    data class MegaFileInfo(val downloadUrl: String, val fileSize: Long, val fileName: String)

    suspend fun getFileInfo(fileId: String, keyBytes: ByteArray): MegaFileInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val apiUrl = "https://g.api.mega.co.nz/cs?"
                Log.d(TAG, "MEGA Step 1: get metadata for $fileId")
                val step1 = megaApiPost(apiUrl, """[{"a":"g","ad":1,"p":"$fileId"}]""", 0) ?: return@withContext null
                val fileSize = step1.optLong("s", 0L)
                val encryptedAttrs = step1.optString("at", "")
                val fa = step1.optString("fa", "")
                Log.d(TAG, "MEGA metadata: size=$fileSize, fa=$fa")

                extractFaHash(fa)?.let { hash ->
                    Log.d(TAG, "MEGA Step 2: ufa fah=$hash")
                    megaApiPost(apiUrl, """[{"a":"ufa","fah":"$hash","r":1,"ssl":1}]""", 1)
                }

                val fileName = if (encryptedAttrs.isNotEmpty()) decryptFileName(encryptedAttrs, keyBytes) ?: "mega_file" else "mega_file"
                MegaFileInfo("", fileSize, fileName)
            } catch (e: Exception) { Log.e(TAG, "MEGA API error: ${e.message}"); null }
        }
    }

    private fun getDownloadUrl(fileId: String): String? {
        val resp = megaApiPost("https://g.api.mega.co.nz/cs?", """[{"a":"g","v":2,"g":1,"ssl":1,"p":"$fileId"}]""", 2) ?: return null
        val g = resp.opt("g") ?: return null
        return when (g) {
            is org.json.JSONArray -> if (g.length() > 0) g.getString(0) else null
            is String -> g
            else -> null
        }
    }

    private fun extractFaHash(fa: String): String? {
        if (fa.isEmpty()) return null
        for (entry in fa.split("/")) {
            val p = entry.split("*")
            if (p.size == 2 && p[0].startsWith("111:")) return p[1]
        }
        val last = fa.split("/").lastOrNull()?.split("*")
        return if (last?.size == 2) last[1] else null
    }

    private fun megaApiPost(apiUrl: String, body: String, sessionId: Int = 0): org.json.JSONObject? {
        try {
            val conn = (URL(apiUrl).openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"; doOutput = true; connectTimeout = 15000; readTimeout = 30000
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
            val code = conn.responseCode
            val text = if (code in 200..299) conn.inputStream.bufferedReader().readText() else conn.errorStream?.bufferedReader()?.readText() ?: ""
            conn.disconnect()
            Log.d(TAG, "MEGA API[$sessionId] ($code): ${text.take(200)}")
            if (code !in 200..299) return null
            val trimmed = text.trim()
            if (!trimmed.startsWith("[")) return null
            val arr = org.json.JSONArray(trimmed)
            if (arr.length() == 0) return null
            val first = arr.get(0)
            return if (first is org.json.JSONObject) first else null
        } catch (e: Exception) { Log.e(TAG, "MEGA API fail: ${e.message}"); return null }
    }

    private fun decryptFileName(encryptedAttrs: String, keyBytes: ByteArray): String? {
        return try {
            val (aesKey, _) = deriveKeyAndIv(keyBytes)
            val cipher = Cipher.getInstance("AES/CBC/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(ByteArray(16)))
            val plain = cipher.doFinal(base64UrlDecode(encryptedAttrs))
            if (plain.size >= 4 && plain[0] == 'M'.code.toByte() && plain[1] == 'E'.code.toByte()
                && plain[2] == 'G'.code.toByte() && plain[3] == 'A'.code.toByte()
            ) org.json.JSONObject(String(plain, 4, plain.size - 4, Charsets.UTF_8)).optString("n", null) else null
        } catch (e: Exception) { null }
    }

    // ===== Proxy =====

    private class DownloadState(val fileSize: Long, val fileId: String) {
        @Volatile var downloadedBytes = 0L
        @Volatile var downloadComplete = false
        @Volatile var downloadFailed = false
        @Volatile var downloadError: String? = null
        val lock = Object()
        fun notifyProgress() { synchronized(lock) { lock.notifyAll() } }
        fun waitForData(minBytes: Long, timeoutMs: Long): Boolean {
            val deadline = System.currentTimeMillis() + timeoutMs
            synchronized(lock) {
                while (downloadedBytes < minBytes && !downloadComplete && !downloadFailed) {
                    val r = deadline - System.currentTimeMillis()
                    if (r <= 0) return false
                    lock.wait(r.coerceAtLeast(100))
                }
            }
            return downloadedBytes >= minBytes || downloadComplete
        }
    }

    fun startMegaProxy(fileSize: Long, aesKey: ByteArray, iv: ByteArray, fileId: String): Pair<String, Int>? {
        return try {
            val serverSocket = ServerSocket(0)
            serverSocket.soTimeout = 300000
            val port = serverSocket.localPort
            Log.d(TAG, "MegaProxy port=$port")
            val tempFile = File.createTempFile("mega_", ".tmp")
            tempFile.deleteOnExit()
            val state = DownloadState(fileSize, fileId)
            Thread { downloadLoop(state, tempFile, aesKey, iv) }.start()
            Thread {
                try { while (!serverSocket.isClosed) { val cs = serverSocket.accept(); Thread { handleClient(cs, tempFile, state) }.start() } }
                catch (e: Exception) { if (!serverSocket.isClosed) Log.e(TAG, "Server err: ${e.message}") }
            }.start()
            Pair("http://127.0.0.1:$port/video", port)
        } catch (e: Exception) { Log.e(TAG, "Proxy start fail: ${e.message}"); null }
    }

    private fun downloadLoop(state: DownloadState, tempFile: File, aesKey: ByteArray, baseIv: ByteArray) {
        var retries = 0
        while (state.downloadedBytes < state.fileSize && retries < 20) {
            try {
                val offset = state.downloadedBytes
                Log.d(TAG, "MEGA DL: offset=$offset/${state.fileSize} (try ${retries + 1})")

                val downloadUrls = getDownloadUrls(state.fileId)
                if (downloadUrls.isEmpty()) {
                    Log.e(TAG, "No download URLs"); retries++; Thread.sleep(3000); continue
                }

                var connected = false
                for (dlUrl in downloadUrls) {
                    try {
                        val conn = (URL(dlUrl).openConnection() as HttpURLConnection).apply {
                            connectTimeout = 15000; readTimeout = 30000
                            setRequestProperty("User-Agent", MEGA_UA)
                            setRequestProperty("Origin", "https://mega.nz")
                            setRequestProperty("Referer", "https://mega.nz/")
                            instanceFollowRedirects = true
                        }
                        val responseCode = conn.responseCode
                        Log.d(TAG, "MEGA CDN: HTTP $responseCode (${dlUrl.substringAfter("://").take(30)})")
                        if (responseCode !in listOf(200, 206)) { conn.disconnect(); continue }

                        val encStream = conn.inputStream
                        connected = true

                        val cipher = Cipher.getInstance("AES/CTR/NoPadding")
                        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(baseIv.copyOf()))

                        val raf = RandomAccessFile(tempFile, "rw")
                        raf.seek(offset)

                        val buf = ByteArray(256 * 1024)
                        var totalDecrypted = 0L
                        var writtenSinceOffset = 0L
                        val bytesToSkip = offset
                        var lastLogPct = -1
                        val lastReadTime = AtomicLong(System.currentTimeMillis())
                        val streamClosed = AtomicBoolean(false)

                        val watchdog = Thread {
                            while (!streamClosed.get()) {
                                Thread.sleep(5000)
                                if (streamClosed.get()) break
                                val idle = System.currentTimeMillis() - lastReadTime.get()
                                if (idle > 20000) {
                                    Log.w(TAG, "MEGA watchdog: no data for ${idle}ms, aborting")
                                    try { encStream.close(); conn.disconnect() } catch (_: Exception) {}
                                    streamClosed.set(true)
                                    break
                                }
                            }
                        }
                        watchdog.isDaemon = true
                        watchdog.start()

                        try {
                            while (true) {
                                val n = try { encStream.read(buf) } catch (e: Exception) { if (streamClosed.get()) -1 else throw e }
                                if (n == -1) break
                                lastReadTime.set(System.currentTimeMillis())

                                val decrypted = cipher.update(buf, 0, n) ?: continue
                                totalDecrypted += decrypted.size

                                if (totalDecrypted <= bytesToSkip) continue

                                val skipRemain = (bytesToSkip - (totalDecrypted - decrypted.size)).toInt().coerceAtLeast(0)
                                val writeFrom = skipRemain
                                val writeLen = decrypted.size - skipRemain

                                if (writeLen > 0) {
                                    raf.write(decrypted, writeFrom, writeLen)
                                    writtenSinceOffset += writeLen
                                }

                                state.downloadedBytes = offset + writtenSinceOffset

                                val pct = (state.downloadedBytes * 100 / state.fileSize).toInt()
                                if (pct / 5 > lastLogPct / 5) {
                                    Log.d(TAG, "MEGA: ${state.downloadedBytes}/${state.fileSize} ($pct%)${if (bytesToSkip > 0) " [skipped ${(totalDecrypted.coerceAtMost(bytesToSkip))}/$bytesToSkip]" else ""}")
                                    lastLogPct = pct
                                }
                                state.notifyProgress()
                            }
                        } finally {
                            streamClosed.set(true)
                            raf.close(); try { encStream.close() } catch (_: Exception) {}; conn.disconnect()
                        }

                        Log.d(TAG, "MEGA chunk done: wrote $writtenSinceOffset bytes (skipped $totalDecrypted total), total=${state.downloadedBytes}/${state.fileSize}")

                        if (state.downloadedBytes >= state.fileSize) {
                            state.downloadComplete = true; state.notifyProgress()
                            Log.d(TAG, "MEGA COMPLETE: ${tempFile.length()} bytes")
                            return
                        }
                        break
                    } catch (e: Exception) {
                        Log.w(TAG, "MEGA CDN fail: ${e.message}")
                        continue
                    }
                }

                if (!connected) { retries++; Thread.sleep(3000); continue }

                retries++
                Log.d(TAG, "MEGA: chunk ended, retrying in 3s...")
                Thread.sleep(3000)
            } catch (e: Exception) {
                Log.e(TAG, "MEGA DL error: ${e.message}")
                retries++; Thread.sleep(3000)
            }
        }

        if (state.downloadedBytes >= state.fileSize) state.downloadComplete = true
        else { state.downloadFailed = true; state.downloadError = "Incomplete: ${state.downloadedBytes}/${state.fileSize}" }
        state.notifyProgress()
    }

    private fun getDownloadUrls(fileId: String): List<String> {
        val resp = megaApiPost("https://g.api.mega.co.nz/cs?", """[{"a":"g","v":2,"g":1,"ssl":1,"p":"$fileId"}]""", 2) ?: return emptyList()
        val g = resp.opt("g") ?: return emptyList()
        return when (g) {
            is org.json.JSONArray -> (0 until g.length()).mapNotNull { g.optString(it, null) }
            is String -> listOf(g)
            else -> emptyList()
        }
    }

    private fun handleClient(socket: Socket, tempFile: File, state: DownloadState) {
        try {
            val input = BufferedReader(java.io.InputStreamReader(socket.getInputStream()))
            val output = socket.getOutputStream()
            val requestLine = input.readLine() ?: return
            Log.d(TAG, "Proxy req: $requestLine")

            val headers = mutableMapOf<String, String>()
            while (true) {
                val line = input.readLine() ?: break
                if (line.isEmpty()) break
                val ci = line.indexOf(':')
                if (ci > 0) headers[line.substring(0, ci).trim().lowercase()] = line.substring(ci + 1).trim()
            }

            val rangeHeader = headers["range"]
            var startByte = 0L
            var endByte = state.fileSize - 1
            val isRange = rangeHeader != null
            if (isRange) {
                Regex("""bytes=(\d+)-(\d*)""").find(rangeHeader!!)?.let {
                    startByte = it.groupValues[1].toLong()
                    if (it.groupValues[2].isNotEmpty()) endByte = it.groupValues[2].toLong()
                }
            }

            // Wait for data
            if (startByte > 0 && startByte >= state.downloadedBytes && !state.downloadComplete) {
                Log.d(TAG, "Proxy: wait for byte $startByte (have ${state.downloadedBytes})")
                state.waitForData(startByte + 1, 60000)
            }

            val available = if (state.downloadComplete) state.fileSize else state.downloadedBytes
            if (startByte >= available) {
                if (state.downloadComplete) sendError(socket, 404, "EOF") else sendError(socket, 503, "No data")
                return
            }
            endByte = endByte.coerceAtMost(available - 1)
            val contentLength = endByte - startByte + 1

            val status = if (isRange) "HTTP/1.1 206 Partial Content" else "HTTP/1.1 200 OK"
            val resp = buildString {
                append("$status\r\nContent-Type: video/mp4\r\nContent-Length: $contentLength\r\nAccept-Ranges: bytes\r\nConnection: close\r\n")
                if (isRange) append("Content-Range: bytes $startByte-$endByte/${state.fileSize}\r\n")
                append("\r\n")
            }
            output.write(resp.toByteArray()); output.flush()

            val raf = RandomAccessFile(tempFile, "r")
            raf.seek(startByte)
            val buf = ByteArray(64 * 1024)
            var rem = contentLength
            while (rem > 0) { val n = raf.read(buf, 0, minOf(buf.size.toLong(), rem).toInt()); if (n == -1) break; output.write(buf, 0, n); rem -= n }
            raf.close(); output.flush(); socket.close()
            Log.d(TAG, "Proxy served: $startByte-$endByte ($contentLength bytes)")
        } catch (e: Exception) {
            Log.e(TAG, "Proxy client err: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun sendError(socket: Socket, code: Int, message: String) {
        try {
            val body = "{\"error\":\"$message\"}"
            socket.getOutputStream().write("HTTP/1.1 $code $message\r\nContent-Type: application/json\r\nContent-Length: ${body.length}\r\nConnection: close\r\n\r\n$body".toByteArray())
            socket.close()
        } catch (_: Exception) {}
    }

    // ===== Entry Point =====

    suspend fun extractMegaUrl(megaUrl: String): Pair<String, Int>? {
        return withContext(Dispatchers.IO) {
            try {
                val urlInfo = parseMegaUrl(megaUrl) ?: return@withContext null
                Log.d(TAG, "Parsed: fileId=${urlInfo.fileId}, key=${urlInfo.key.take(20)}...")
                val keyBytes = base64UrlDecode(urlInfo.key)
                val fileInfo = getFileInfo(urlInfo.fileId, keyBytes) ?: return@withContext null
                val (aesKey, iv) = deriveKeyAndIv(keyBytes)
                Log.d(TAG, "AES=${aesKey.size}B IV=${iv.size}B")

                val proxy = startMegaProxy(fileInfo.fileSize, aesKey, iv, urlInfo.fileId) ?: return@withContext null
                Log.d(TAG, "Proxy ready: ${proxy.first}")
                proxy
            } catch (e: Exception) { Log.e(TAG, "Extract fail: ${e.message}", e); null }
        }
    }
}
