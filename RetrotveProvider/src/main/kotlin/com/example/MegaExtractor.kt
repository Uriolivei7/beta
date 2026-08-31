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
import java.util.concurrent.atomic.AtomicInteger
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
        val downloadedBytes = AtomicLong(0L)
        @Volatile var downloadComplete = false
        @Volatile var downloadFailed = false
        @Volatile var downloadError: String? = null
        @Volatile var tailReady = false
        val lock = Object()
        fun notifyProgress() { synchronized(lock) { lock.notifyAll() } }
        fun waitForData(minBytes: Long, timeoutMs: Long): Boolean {
            val deadline = System.currentTimeMillis() + timeoutMs
            synchronized(lock) {
                while (downloadedBytes.get() < minBytes && !downloadComplete && !downloadFailed) {
                    val r = deadline - System.currentTimeMillis()
                    if (r <= 0) return false
                    lock.wait(r.coerceAtLeast(100))
                }
            }
            return downloadedBytes.get() >= minBytes || downloadComplete
        }
        fun waitForTail(timeoutMs: Long): Boolean {
            val deadline = System.currentTimeMillis() + timeoutMs
            synchronized(lock) {
                while (!tailReady && !downloadComplete && !downloadFailed) {
                    val r = deadline - System.currentTimeMillis()
                    if (r <= 0) return false
                    lock.wait(r.coerceAtLeast(100))
                }
            }
            return tailReady || downloadComplete
        }
    }

    private data class MegaProxyResult(val url: String, val port: Int, val state: DownloadState)

    private fun startMegaProxy(fileSize: Long, aesKey: ByteArray, iv: ByteArray, fileId: String, faHash: String?): MegaProxyResult? {
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
            MegaProxyResult("http://127.0.0.1:$port/video", port, state)
        } catch (e: Exception) { Log.e(TAG, "Proxy start fail: ${e.message}"); null }
    }

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
        Log.d(TAG, "MEGA got ${urls.size} CDN URL(s): ${urls.map { it.removePrefix("https://").take(40) }}")
        return urls
    }

    private fun downloadLoop(state: DownloadState, tempFile: File, aesKey: ByteArray, baseIv: ByteArray) {
        if (state.faHash != null) performUfaUnlock(state.faHash, 0)

        var urls = getDownloadUrls(state.fileId)
        if (urls.isEmpty()) {
            state.downloadFailed = true
            state.downloadError = "No download URLs from MEGA"
            state.notifyProgress(); return
        }

        val chunkSize = 4L * 1024 * 1024
        val totalChunks = ((state.fileSize + chunkSize - 1) / chunkSize).toInt()
        val url = urls[0]

        val tailChunks = (totalChunks - 3).coerceAtLeast(0)
        val tailEnd = totalChunks - 1
        Log.d(TAG, "MEGA tail-first: downloading chunks $tailChunks..$tailEnd (last ${(tailEnd - tailChunks + 1) * 4}MB for moov)")
        for (ci in tailChunks..tailEnd) {
            if (state.downloadFailed) break
            val pos = ci.toLong() * chunkSize
            val end = (pos + chunkSize - 1).coerceAtMost(state.fileSize - 1)
            var ok = false
            var retries = 0
            while (!ok && retries < 8 && !state.downloadFailed) {
                try {
                    downloadSingleChunk(state, tempFile, aesKey, baseIv, url, pos, end, 0)
                    ok = true
                } catch (e: MegaBandwidthException) {
                    retries++; Thread.sleep(5000L * retries.coerceAtMost(6))
                    Log.w(TAG, "MEGA tail 509 ($retries/8)")
                } catch (e: Exception) {
                    retries++; Thread.sleep(3000L * retries.coerceAtMost(5))
                    Log.w(TAG, "MEGA tail error ($retries/8): ${e.message}")
                }
            }
            if (!ok) { Log.e(TAG, "MEGA tail chunk $ci FAILED"); break }
        }

        state.tailReady = true
        state.notifyProgress()
        Log.d(TAG, "MEGA tail ready (${state.downloadedBytes.get()}/${state.fileSize} bytes), starting head download")

        val nextChunk = AtomicInteger(0)
        val numWorkers = urls.size.coerceAtMost(4)
        Log.d(TAG, "MEGA parallel: $numWorkers workers for remaining chunks")

        val allWorkers = (0 until numWorkers).map { threadIdx ->
            Thread({
                var myUrlIdx = threadIdx % urls.size
                var myUrl = urls[myUrlIdx]
                var chunksDone = 0

                while (!state.downloadFailed) {
                    val ci = nextChunk.get()
                    if (ci >= totalChunks) break
                    if (!nextChunk.compareAndSet(ci, ci + 1)) continue

                    val pos = ci.toLong() * chunkSize
                    val end = (pos + chunkSize - 1).coerceAtMost(state.fileSize - 1)

                    var ok = false
                    var retries = 0
                    while (!ok && retries < 8 && !state.downloadFailed) {
                        try {
                            downloadSingleChunk(state, tempFile, aesKey, baseIv, myUrl, pos, end, threadIdx)
                            ok = true; chunksDone++
                        } catch (e: MegaBandwidthException) {
                            retries++
                            myUrlIdx = (myUrlIdx + 1) % urls.size
                            myUrl = urls[myUrlIdx]
                            Log.w(TAG, "MEGA T$threadIdx 509 ($retries/8), trying URL $myUrlIdx/${urls.size}")
                            Thread.sleep(5000L * retries.coerceAtMost(6))
                        } catch (e: Exception) {
                            retries++
                            Log.w(TAG, "MEGA T$threadIdx error ($retries/8): ${e.message}")
                            Thread.sleep(3000L * retries.coerceAtMost(5))
                        }
                    }
                    if (!ok) {
                        Log.e(TAG, "MEGA T$threadIdx chunk $ci FAILED after 8 retries")
                    }
                }
                Log.d(TAG, "MEGA T$threadIdx done: $chunksDone chunks")
            }, "MEGA-W$threadIdx").also { it.isDaemon = true; it.start() }
        }

        allWorkers.forEach { it.join() }

        if (state.downloadedBytes.get() >= state.fileSize) {
            state.downloadComplete = true
            Log.d(TAG, "MEGA COMPLETE: ${tempFile.length()} bytes, $totalChunks chunks")
        } else if (!state.downloadFailed) {
            state.downloadFailed = true
            state.downloadError = "Incomplete: ${state.downloadedBytes.get()}/${state.fileSize}"
            Log.e(TAG, "MEGA INCOMPLETE: ${state.downloadedBytes.get()}/${state.fileSize}")
        }
        state.notifyProgress()
    }

    private fun downloadSingleChunk(state: DownloadState, tempFile: File, aesKey: ByteArray, baseIv: ByteArray,
                                     url: String, pos: Long, end: Long, threadIdx: Int) {
        val chunkUrl = "$url/$pos-$end"
        val conn = (URL(chunkUrl).openConnection() as HttpURLConnection).apply {
            connectTimeout = 15000; readTimeout = 120000
            setRequestProperty("User-Agent", MEGA_UA)
            setRequestProperty("Origin", "https://mega.nz")
            setRequestProperty("Referer", "https://mega.nz/")
            instanceFollowRedirects = true
        }

        val code = conn.responseCode
        if (code == 509) {
            conn.disconnect()
            throw MegaBandwidthException("509 bandwidth limit")
        }
        if (code == 416) { conn.disconnect(); return }
        if (code !in listOf(200, 206)) {
            val err = try { conn.errorStream?.bufferedReader()?.readText()?.take(100) } catch (_: Exception) { "" }
            conn.disconnect()
            throw java.io.IOException("HTTP $code: $err")
        }

        val contentLength = conn.contentLength.toLong()
        Log.d(TAG, "MEGA T$threadIdx HTTP $code: $pos-$end ($contentLength bytes)")

        val cipher = Cipher.getInstance("AES/CTR/NoPadding")
        val ivForPos = baseIv.copyOf()
        val blockNum = pos / 16
        ivForPos[8] = ((blockNum shr 56) and 0xFF).toByte()
        ivForPos[9] = ((blockNum shr 48) and 0xFF).toByte()
        ivForPos[10] = ((blockNum shr 40) and 0xFF).toByte()
        ivForPos[11] = ((blockNum shr 32) and 0xFF).toByte()
        ivForPos[12] = ((blockNum shr 24) and 0xFF).toByte()
        ivForPos[13] = ((blockNum shr 16) and 0xFF).toByte()
        ivForPos[14] = ((blockNum shr 8) and 0xFF).toByte()
        ivForPos[15] = (blockNum and 0xFF).toByte()
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(ivForPos))

        conn.inputStream.use { enc ->
            RandomAccessFile(tempFile, "rw").use { raf ->
                raf.seek(pos)
                val buf = ByteArray(256 * 1024)
                var written = 0L
                var loggedFirstBytes = false
                while (true) {
                    val n = enc.read(buf)
                    if (n == -1) break
                    val dec = cipher.update(buf, 0, n) ?: continue
                    raf.write(dec)
                    written += dec.size
                    state.downloadedBytes.addAndGet(dec.size.toLong())
                    if (!loggedFirstBytes && dec.isNotEmpty() && pos == 0L) {
                        val hex = dec.take(32).joinToString("") { "%02x".format(it) }
                        val ascii = dec.take(32).map { b -> if (b in 32..126) b.toInt().toChar() else '.' }.joinToString("")
                        Log.d(TAG, "MEGA T$threadIdx first32: hex=$hex ascii=$ascii")
                        loggedFirstBytes = true
                    }
                    state.notifyProgress()
                }
                if (written > 0) {
                    Log.d(TAG, "MEGA T$threadIdx chunk done: $pos-$end ($written bytes, total=${state.downloadedBytes.get()}/${state.fileSize})")
                }
            }
        }
        conn.disconnect()
    }

    private fun handleClient(socket: Socket, tempFile: File, state: DownloadState) {
        try {
            if (state.downloadFailed) {
                Log.w(TAG, "Proxy: download failed, rejecting connection")
                sendError(socket, 503, state.downloadError ?: "Download failed")
                return
            }

            if (!state.downloadComplete) {
                Log.d(TAG, "Proxy: waiting for download to complete (${state.downloadedBytes.get()}/${state.fileSize})...")
                state.waitForData(state.fileSize, 300000)
                if (!state.downloadComplete) {
                    Log.w(TAG, "Proxy: download not complete after 300s, rejecting")
                    sendError(socket, 503, "Download not complete")
                    return
                }
                Log.d(TAG, "Proxy: download complete, serving ${state.fileSize} bytes")
            }

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
            if (startByte >= state.fileSize) {
                val resp = "HTTP/1.1 416 Range Not Satisfiable\r\nContent-Range: bytes */${state.fileSize}\r\nConnection: close\r\n\r\n"
                output.write(resp.toByteArray()); output.flush(); socket.close()
                return
            }

            val available = state.downloadedBytes.get()
            if (startByte >= available && !state.downloadComplete) {
                Log.d(TAG, "Proxy Range: need byte $startByte, have $available, waiting...")
                state.waitForData(startByte + 1, 60000)
            }

            val availableAfter = if (state.downloadComplete) state.fileSize else state.downloadedBytes.get()
            if (startByte >= availableAfter) {
                val resp = "HTTP/1.1 416 Range Not Satisfiable\r\nContent-Range: bytes */${state.fileSize}\r\nConnection: close\r\n\r\n"
                output.write(resp.toByteArray()); output.flush(); socket.close()
                return
            }
            val actualEnd = endByte.coerceAtMost(availableAfter - 1)
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
            socket.close()
        } catch (e: Exception) {
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun handleStreamRequest(socket: Socket, output: java.io.OutputStream, tempFile: File, state: DownloadState) {
        try {
            Log.d(TAG, "Proxy Stream: waiting for data (have=${state.downloadedBytes.get()})")

            val firstChunk = state.waitForData(65536, 30000)
            if (!firstChunk && state.downloadedBytes.get() == 0L) {
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
            val buf = ByteArray(256 * 1024)
            val raf = RandomAccessFile(tempFile, "r")
            try {
                while (!socket.isClosed) {
                    if (served >= state.downloadedBytes.get()) {
                        if (state.downloadComplete) break
                        state.waitForData(served + 1, 15000)
                        if (served >= state.downloadedBytes.get() && !state.downloadComplete) {
                            if (state.downloadFailed) break
                            continue
                        }
                    }

                    raf.seek(served)
                    val toRead = minOf(buf.size.toLong(), state.downloadedBytes.get() - served).toInt()
                    val n = raf.read(buf, 0, toRead)

                    if (n > 0) {
                        output.write(buf, 0, n)
                        output.flush()
                        served += n
                        if (served % (5 * 1024 * 1024) < 262144) {
                            Log.d(TAG, "Proxy Stream: served ${served}/${state.downloadedBytes.get()} bytes")
                        }
                    } else {
                        Thread.sleep(100)
                    }
                }
            } finally {
                raf.close()
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
                Log.d(TAG, "AES=${aesKey.size}B IV=${iv.size}B, size=${fileInfo.fileSize / 1024 / 1024}MB")

                val result = startMegaProxy(fileInfo.fileSize, aesKey, iv, urlInfo.fileId, fileInfo.faHash) ?: return@withContext null
                Log.d(TAG, "Proxy ready: ${result.url}")

                Log.d(TAG, "Waiting for download to complete before returning URL...")
                val startTime = System.currentTimeMillis()
                result.state.waitForData(result.state.fileSize, 300000)
                val elapsed = (System.currentTimeMillis() - startTime) / 1000
                if (result.state.downloadComplete) {
                    Log.d(TAG, "Download complete in ${elapsed}s, returning proxy URL")
                    Pair(result.url, result.port)
                } else {
                    Log.e(TAG, "Download incomplete after ${elapsed}s: ${result.state.downloadedBytes.get()}/${result.state.fileSize}")
                    null
                }
            } catch (e: Exception) { Log.e(TAG, "Extract fail: ${e.message}", e); null }
        }
    }
}
