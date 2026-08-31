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

    data class MegaFileInfo(val downloadUrl: String, val fileSize: Long, val fileName: String, val faHash: String?)

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

                val faHash = extractFaHash(fa)
                faHash?.let { hash ->
                    Log.d(TAG, "MEGA Step 2: ufa fah=$hash")
                    megaApiPost(apiUrl, """[{"a":"ufa","fah":"$hash","r":1,"ssl":1}]""", 1)
                }

                val fileName = if (encryptedAttrs.isNotEmpty()) decryptFileName(encryptedAttrs, keyBytes) ?: "mega_file" else "mega_file"
                MegaFileInfo("", fileSize, fileName, faHash)
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

    private class DownloadState(val fileSize: Long, val fileId: String, val faHash: String?) {
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

    fun startMegaProxy(fileSize: Long, aesKey: ByteArray, iv: ByteArray, fileId: String, faHash: String?): Pair<String, Int>? {
        return try {
            val serverSocket = ServerSocket(0)
            serverSocket.soTimeout = 300000
            val port = serverSocket.localPort
            Log.d(TAG, "MegaProxy port=$port")
            val tempFile = File.createTempFile("mega_", ".tmp")
            tempFile.deleteOnExit()
            val state = DownloadState(fileSize, fileId, faHash)
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
        val apiUrl = "https://g.api.mega.co.nz/cs?"
        var currentUrl: String? = null
        while (state.downloadedBytes < state.fileSize && retries < 20) {
            try {
                val offset = state.downloadedBytes
                Log.d(TAG, "MEGA DL: offset=$offset/${state.fileSize} (try ${retries + 1})")

                if (retries > 0 && state.faHash != null) {
                    Log.d(TAG, "MEGA Step 2 retry: ufa fah=${state.faHash}")
                    megaApiPost(apiUrl, """[{"a":"ufa","fah":"${state.faHash}","r":1,"ssl":1}]""", retries + 10)
                }

                if (currentUrl == null || retries > 0) {
                    val urls = getDownloadUrls(state.fileId)
                    if (urls.isEmpty()) { Log.e(TAG, "No download URLs"); retries++; Thread.sleep(10000); continue }
                    currentUrl = urls.first()
                }

                val conn = (URL(currentUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 15000; readTimeout = 60000
                    setRequestProperty("User-Agent", MEGA_UA)
                    setRequestProperty("Origin", "https://mega.nz")
                    setRequestProperty("Referer", "https://mega.nz/")
                    if (offset > 0) {
                        setRequestProperty("Range", "bytes=$offset-")
                        Log.d(TAG, "MEGA CDN Range: bytes=$offset-")
                    }
                    instanceFollowRedirects = true
                }

                val responseCode = conn.responseCode
                val contentLength = conn.contentLength.toLong()
                Log.d(TAG, "MEGA CDN: HTTP $responseCode Content-Length=$contentLength (${currentUrl!!.substringAfter("://").take(30)})")

                if (responseCode == 416) {
                    Log.d(TAG, "MEGA CDN 416 Range Not Satisfiable — file fully downloaded or range invalid")
                    conn.disconnect()
                    state.downloadComplete = true; state.notifyProgress()
                    Log.d(TAG, "MEGA COMPLETE (416): ${tempFile.length()} bytes")
                    return
                }
                if (responseCode !in listOf(200, 206)) { conn.disconnect(); retries++; currentUrl = null; Thread.sleep(10000); continue }

                val actualOffset = if (responseCode == 206) {
                    val cr = conn.getHeaderField("Content-Range")
                    Regex("""bytes (\d+)-""").find(cr ?: "")?.groupValues?.get(1)?.toLong() ?: offset
                } else offset

                Log.d(TAG, "MEGA CDN response: HTTP $responseCode, Content-Length=$contentLength, actualOffset=$actualOffset")
                val encStream = conn.inputStream

                val cipher = Cipher.getInstance("AES/CTR/NoPadding")
                cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(baseIv.copyOf()))

                val raf = RandomAccessFile(tempFile, "rw")
                raf.seek(actualOffset)

                val buf = ByteArray(256 * 1024)
                var totalDecrypted = 0L
                var writtenSinceOffset = 0L
                val bytesToSkip = actualOffset
                var lastLogPct = -1
                val lastReadTime = AtomicLong(System.currentTimeMillis())
                val streamClosed = AtomicBoolean(false)

                val watchdog = Thread {
                    while (!streamClosed.get()) {
                        Thread.sleep(5000)
                        if (streamClosed.get()) break
                        val idle = System.currentTimeMillis() - lastReadTime.get()
                        if (idle > 30000) {
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

                        state.downloadedBytes = actualOffset + writtenSinceOffset

                        val pct = (state.downloadedBytes * 100 / state.fileSize).toInt()
                        if (pct / 5 > lastLogPct / 5) {
                            Log.d(TAG, "MEGA: ${state.downloadedBytes}/${state.fileSize} ($pct%)")
                            lastLogPct = pct
                        }
                        state.notifyProgress()
                    }
                } finally {
                    streamClosed.set(true)
                    raf.close(); try { encStream.close() } catch (_: Exception) {}; conn.disconnect()
                }

                Log.d(TAG, "MEGA chunk done: wrote=$writtenSinceOffset from CDN=$contentLength total=${state.downloadedBytes}/${state.fileSize}")

                if (state.downloadedBytes >= state.fileSize) {
                    state.downloadComplete = true; state.notifyProgress()
                    Log.d(TAG, "MEGA COMPLETE: ${tempFile.length()} bytes")
                    return
                }

                if (writtenSinceOffset == 0L && contentLength > 0) {
                    Log.w(TAG, "MEGA wrote 0 bytes — CDN sent $contentLength but skip consumed all")
                    currentUrl = null
                }

                retries++
                val waitMs = 5000L
                Log.d(TAG, "MEGA: chunk ended, retry #${retries} in ${waitMs/1000}s...")
                Thread.sleep(waitMs)
            } catch (e: Exception) {
                Log.e(TAG, "MEGA DL error: ${e.message}")
                retries++; Thread.sleep(10000)
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

            if (isRange) {
                handleRangeRequest(socket, output, startByte, endByte, tempFile, state)
            } else {
                handleStreamRequest(socket, output, tempFile, state)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Proxy client err: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun handleRangeRequest(socket: Socket, output: java.io.OutputStream, startByte: Long, endByte: Long, tempFile: File, state: DownloadState) {
        try {
            if (startByte >= state.downloadedBytes && !state.downloadComplete) {
                Log.d(TAG, "Proxy Range: wait for byte $startByte (have ${state.downloadedBytes})")
                state.waitForData(startByte + 1, 60000)
            }

            val available = if (state.downloadComplete) state.fileSize else state.downloadedBytes
            if (startByte >= available) {
                val resp = "HTTP/1.1 416 Range Not Satisfiable\r\nContent-Range: bytes */${state.fileSize}\r\nConnection: close\r\n\r\n"
                output.write(resp.toByteArray()); output.flush(); socket.close()
                return
            }
            val actualEnd = endByte.coerceAtMost(available - 1)
            val contentLength = actualEnd - startByte + 1

            val resp = buildString {
                append("HTTP/1.1 206 Partial Content\r\n")
                append("Content-Type: video/mp4\r\n")
                append("Content-Length: $contentLength\r\n")
                append("Content-Range: bytes $startByte-$actualEnd/${state.fileSize}\r\n")
                append("Accept-Ranges: bytes\r\n")
                append("Connection: keep-alive\r\n\r\n")
            }
            output.write(resp.toByteArray()); output.flush()

            val raf = RandomAccessFile(tempFile, "r")
            raf.seek(startByte)
            val buf = ByteArray(64 * 1024)
            var rem = contentLength
            while (rem > 0) {
                val n = raf.read(buf, 0, minOf(buf.size.toLong(), rem).toInt())
                if (n == -1) break
                output.write(buf, 0, n)
                rem -= n
            }
            raf.close(); output.flush()
            Log.d(TAG, "Proxy Range served: $startByte-$actualEnd ($contentLength bytes)")
            socket.close()
        } catch (e: Exception) {
            Log.e(TAG, "Proxy Range err: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun handleStreamRequest(socket: Socket, output: java.io.OutputStream, tempFile: File, state: DownloadState) {
        try {
            Log.d(TAG, "Proxy Stream: waiting for data (have=${state.downloadedBytes})")

            val firstChunk = state.waitForData(65536, 30000)
            if (!firstChunk && state.downloadedBytes == 0L) {
                Log.w(TAG, "Proxy Stream: no data after 30s, aborting")
                sendError(socket, 503, "Download not started")
                return
            }

            val resp = buildString {
                append("HTTP/1.1 200 OK\r\n")
                append("Content-Type: video/mp4\r\n")
                append("Content-Length: ${state.fileSize}\r\n")
                append("Accept-Ranges: bytes\r\n")
                append("Connection: keep-alive\r\n\r\n")
            }
            output.write(resp.toByteArray()); output.flush()
            Log.d(TAG, "Proxy Stream: headers sent, streaming from disk")

            var served = 0L
            val buf = ByteArray(64 * 1024)
            while (!socket.isClosed) {
                if (served >= state.downloadedBytes) {
                    if (state.downloadComplete) break
                    state.waitForData(served + 1, 15000)
                    if (served >= state.downloadedBytes && !state.downloadComplete) {
                        if (state.downloadFailed) break
                        continue
                    }
                }

                val raf = RandomAccessFile(tempFile, "r")
                raf.seek(served)
                val toRead = minOf(buf.size.toLong(), state.downloadedBytes - served).toInt()
                val n = raf.read(buf, 0, toRead)
                raf.close()

                if (n > 0) {
                    output.write(buf, 0, n)
                    output.flush()
                    served += n
                    if (served % (5 * 1024 * 1024) < 65536) {
                        Log.d(TAG, "Proxy Stream: served ${served}/${state.downloadedBytes} bytes")
                    }
                } else {
                    Thread.sleep(100)
                }
            }
            Log.d(TAG, "Proxy Stream done: served $served bytes")
            socket.close()
        } catch (e: Exception) {
            Log.d(TAG, "Proxy Stream end: ${e.message}")
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

                val proxy = startMegaProxy(fileInfo.fileSize, aesKey, iv, urlInfo.fileId, fileInfo.faHash) ?: return@withContext null
                Log.d(TAG, "Proxy ready: ${proxy.first}")
                proxy
            } catch (e: Exception) { Log.e(TAG, "Extract fail: ${e.message}", e); null }
        }
    }
}
