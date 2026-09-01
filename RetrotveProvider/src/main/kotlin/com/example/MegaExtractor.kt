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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

object MegaExtractor {

    private const val TAG = "MegaExtractor"
    private const val MEGA_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36"
    private const val CHUNK_SIZE = 4L * 1024 * 1024
    private const val MAX_CACHE_CHUNKS = 128
    private const val FIVE_ZERO_NINE_DELAY_MS = 15000L

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

    // ===== On-Demand Proxy =====

    class ChunkCache(val fileSize: Long) {
        val chunks = ConcurrentHashMap<Long, ByteArray>()
        val fetching = ConcurrentHashMap<Long, Boolean>()
        val downloadedBytes = AtomicLong(0L)
        @Volatile var cdnUrl: String? = null
        @Volatile var failed = false
        @Volatile var errorMsg: String? = null
        val totalChunks = ((fileSize + CHUNK_SIZE - 1) / CHUNK_SIZE).toInt()

        fun chunkIndex(pos: Long) = (pos / CHUNK_SIZE).toInt()
        fun chunkStart(ci: Int) = ci.toLong() * CHUNK_SIZE
        fun chunkEnd(ci: Int) = ((ci + 1).toLong() * CHUNK_SIZE - 1).coerceAtMost(fileSize - 1)
    }

    private fun fetchChunkOnDemand(cache: ChunkCache, aesKey: ByteArray, baseIv: ByteArray, ci: Int): Boolean {
        if (cache.chunks.containsKey(ci.toLong())) return true
        if (cache.fetching.putIfAbsent(ci.toLong(), true) != null) {
            while (cache.fetching.containsKey(ci.toLong()) && !cache.failed) Thread.sleep(100)
            return cache.chunks.containsKey(ci.toLong())
        }

        val url = cache.cdnUrl ?: return false
        val start = cache.chunkStart(ci)
        val end = cache.chunkEnd(ci)

        var retries = 0
        while (retries < 10 && !cache.failed) {
            try {
                val chunkUrl = "$url/$start-$end"
                val conn = (URL(chunkUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 15000; readTimeout = 60000
                    setRequestProperty("User-Agent", MEGA_UA)
                    setRequestProperty("Origin", "https://mega.nz")
                    setRequestProperty("Referer", "https://mega.nz/")
                    instanceFollowRedirects = true
                }
                val code = conn.responseCode
                if (code == 509) {
                    conn.disconnect()
                    retries++
                    Log.w(TAG, "509 chunk $ci ($retries/10), waiting ${FIVE_ZERO_NINE_DELAY_MS}ms...")
                    Thread.sleep(FIVE_ZERO_NINE_DELAY_MS)
                    continue
                }
                if (code == 416) { conn.disconnect(); cache.chunks[ci.toLong()] = ByteArray(0); return true }
                if (code !in listOf(200, 206)) {
                    conn.disconnect()
                    retries++; Thread.sleep(3000L * retries)
                    continue
                }

                val cipher = Cipher.getInstance("AES/CTR/NoPadding")
                val ivForPos = baseIv.copyOf()
                val blockNum = start / 16
                ivForPos[8] = ((blockNum shr 56) and 0xFF).toByte()
                ivForPos[9] = ((blockNum shr 48) and 0xFF).toByte()
                ivForPos[10] = ((blockNum shr 40) and 0xFF).toByte()
                ivForPos[11] = ((blockNum shr 32) and 0xFF).toByte()
                ivForPos[12] = ((blockNum shr 24) and 0xFF).toByte()
                ivForPos[13] = ((blockNum shr 16) and 0xFF).toByte()
                ivForPos[14] = ((blockNum shr 8) and 0xFF).toByte()
                ivForPos[15] = (blockNum and 0xFF).toByte()
                cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(ivForPos))

                val data = conn.inputStream.use { enc ->
                    val baos = java.io.ByteArrayOutputStream()
                    val buf = ByteArray(256 * 1024)
                    while (true) {
                        val n = enc.read(buf)
                        if (n == -1) break
                        val dec = cipher.update(buf, 0, n) ?: continue
                        baos.write(dec)
                    }
                    baos.toByteArray()
                }
                conn.disconnect()

                cache.chunks[ci.toLong()] = data
                cache.downloadedBytes.addAndGet(data.size.toLong())
                cache.fetching.remove(ci.toLong())
                Log.d(TAG, "Chunk $ci fetched: ${data.size}B (total=${cache.downloadedBytes.get()}/${cache.fileSize})")
                return true
            } catch (e: Exception) {
                retries++
                Log.w(TAG, "Chunk $ci error ($retries/10): ${e.message}")
                Thread.sleep(3000L * retries.coerceAtMost(5))
            }
        }
        cache.fetching.remove(ci.toLong())
        cache.failed = true
        cache.errorMsg = "Chunk $ci failed after 10 retries"
        return false
    }

    private class OnDemandState(val fileSize: Long, val cache: ChunkCache, val aesKey: ByteArray, val baseIv: ByteArray)

    private fun startOnDemandProxy(fileSize: Long, aesKey: ByteArray, iv: ByteArray, fileId: String, faHash: String?): MegaProxyResult2? {
        return try {
            val serverSocket = ServerSocket(0)
            serverSocket.soTimeout = 600000
            val port = serverSocket.localPort

            val cache = ChunkCache(fileSize)

            Thread {
                if (faHash != null) performUfaUnlock(faHash, 0)
                val urls = getDownloadUrls(fileId)
                if (urls.isEmpty()) {
                    cache.failed = true
                    cache.errorMsg = "No CDN URLs"
                    return@Thread
                }
                cache.cdnUrl = urls[0]
                Log.d(TAG, "CDN URL ready: ${urls[0].removePrefix("https://").take(40)}")
            }.start()

            val state = OnDemandState(fileSize, cache, aesKey, iv)

            Thread {
                try {
                    while (!serverSocket.isClosed) {
                        val cs = serverSocket.accept()
                        Thread { handleOnDemandClient(cs, state) }.start()
                    }
                } catch (e: Exception) {
                    if (!serverSocket.isClosed) Log.e(TAG, "Server err: ${e.message}")
                }
            }.start()

            Log.d(TAG, "On-demand proxy started on port $port")
            MegaProxyResult2("http://127.0.0.1:$port/video", port, cache)
        } catch (e: Exception) { Log.e(TAG, "Proxy start fail: ${e.message}"); null }
    }

    private data class MegaProxyResult2(val url: String, val port: Int, val cache: ChunkCache)

    private class MegaBandwidthException(msg: String) : java.io.IOException(msg)

    private fun performUfaUnlock(faHash: String, sessionId: Int): Boolean {
        return try {
            Log.d(TAG, "MEGA UFA: fah=$faHash")
            val resp = megaApiPost("https://g.api.mega.co.nz/cs?", """[{"a":"ufa","fah":"$faHash","r":1,"ssl":1}]""", sessionId)
            Log.d(TAG, "MEGA UFA response: ${resp?.toString()?.take(100)}")
            resp != null
        } catch (e: Exception) {
            Log.w(TAG, "MEGA UFA error: ${e.message}")
            false
        }
    }

    private fun getDownloadUrls(fileId: String): List<String> {
        val resp = megaApiPost("https://g.api.mega.co.nz/cs?", """[{"a":"g","v":2,"g":1,"ssl":1,"p":"$fileId"}]""", 2) ?: return emptyList()
        val g = resp.opt("g") ?: return emptyList()
        val urls = when (g) {
            is org.json.JSONArray -> (0 until g.length()).mapNotNull { g.optString(it, null) }
            is String -> listOf(g)
            else -> emptyList()
        }
        Log.d(TAG, "MEGA got ${urls.size} CDN URL(s)")
        return urls
    }

    private fun handleOnDemandClient(socket: Socket, state: OnDemandState) {
        try {
            val input = BufferedReader(java.io.InputStreamReader(socket.getInputStream()))
            val output = socket.getOutputStream()
            val requestLine = input.readLine() ?: return
            Log.d(TAG, "Proxy req: $requestLine")

            while (true) {
                val line = input.readLine() ?: break
                if (line.isEmpty()) break
            }

            if (state.cache.failed) {
                sendError(socket, 503, state.cache.errorMsg ?: "MEGA download failed")
                return
            }

            val rangeHeader = null
            var startByte = 0L
            var endByte = state.fileSize - 1

            val rangeMatch = Regex("""bytes=(\d+)-(\d*)""").find(requestLine + socket.getInputStream().let {
                val sb = StringBuilder()
                val buf = CharArray(1024)
                while (input.ready()) { val n = input.read(buf); if (n > 0) sb.append(buf, 0, n) else break }
                sb.toString()
            })

            if (requestLine.contains("Range:")) {
                val allHeaders = StringBuilder(requestLine)
                while (true) {
                    val line = input.readLine() ?: break
                    if (line.isEmpty()) break
                    allHeaders.append("\r\n").append(line)
                }
                Regex("""Range:\s*bytes=(\d+)-(\d*)""").find(allHeaders.toString())?.let {
                    startByte = it.groupValues[1].toLong()
                    if (it.groupValues[2].isNotEmpty()) endByte = it.groupValues[2].toLong()
                }
            }

            handleOnDemandRange(socket, output, startByte, endByte, state)
        } catch (e: Exception) {
            Log.e(TAG, "Proxy client err: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun handleOnDemandRange(socket: Socket, output: java.io.OutputStream, startByte: Long, endByte: Long, state: OnDemandState) {
        try {
            if (startByte >= state.fileSize) {
                val resp = "HTTP/1.1 416 Range Not Satisfiable\r\nContent-Range: bytes */${state.fileSize}\r\nConnection: close\r\n\r\n"
                output.write(resp.toByteArray()); output.flush(); socket.close()
                return
            }

            val actualEnd = endByte.coerceAtMost(state.fileSize - 1)
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

            var pos = startByte
            val buf = ByteArray(64 * 1024)

            while (pos <= actualEnd) {
                val ci = state.cache.chunkIndex(pos)
                if (!state.cache.chunks.containsKey(ci.toLong())) {
                    Log.d(TAG, "On-demand: fetching chunk $ci for byte $pos")
                    val ok = fetchChunkOnDemand(state.cache, state.aesKey, state.baseIv, ci)
                    if (!ok) {
                        Log.e(TAG, "Failed to fetch chunk $ci, aborting")
                        break
                    }
                }

                val chunkData = state.cache.chunks[ci.toLong()] ?: break
                val chunkFileStart = state.cache.chunkStart(ci)
                val offsetInChunk = (pos - chunkFileStart).toInt()
                val available = chunkData.size - offsetInChunk
                val toSend = minOf(available.toLong(), actualEnd - pos + 1).toInt()

                if (toSend > 0) {
                    output.write(chunkData, offsetInChunk, toSend)
                    output.flush()
                    pos += toSend
                } else {
                    pos = state.cache.chunkStart(ci + 1)
                }

                if (state.cache.failed) {
                    Log.e(TAG, "Cache failed during serve")
                    break
                }
            }

            Log.d(TAG, "On-demand serve done: $startByte-$actualEnd")
            socket.close()
        } catch (e: Exception) {
            Log.d(TAG, "On-demand serve end: ${e.message}")
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

    data class MegaProxyResult(val url: String, val port: Int, val cache: ChunkCache)

    suspend fun extractMegaUrl(megaUrl: String): MegaProxyResult? {
        return withContext(Dispatchers.IO) {
            try {
                val urlInfo = parseMegaUrl(megaUrl) ?: return@withContext null
                Log.d(TAG, "Parsed: fileId=${urlInfo.fileId}, key=${urlInfo.key.take(20)}...")
                val keyBytes = base64UrlDecode(urlInfo.key)
                val fileInfo = getFileInfo(urlInfo.fileId, keyBytes) ?: return@withContext null
                val (aesKey, iv) = deriveKeyAndIv(keyBytes)
                Log.d(TAG, "AES=${aesKey.size}B IV=${iv.size}B, size=${fileInfo.fileSize / 1024 / 1024}MB")

                val result = startOnDemandProxy(fileInfo.fileSize, aesKey, iv, urlInfo.fileId, fileInfo.faHash) ?: return@withContext null
                Log.d(TAG, "On-demand proxy ready: ${result.url}")

                MegaProxyResult(result.url, result.port, result.cache)
            } catch (e: Exception) { Log.e(TAG, "Extract fail: ${e.message}", e); null }
        }
    }
}
