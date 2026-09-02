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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object MegaExtractor {

    private const val TAG = "MegaExtractor"
    private const val MEGA_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36"
    private const val CHUNK_SIZE = 4L * 1024 * 1024
    private const val FIVE_ZERO_NINE_DELAY_MS = 15000L
    private const val WAIT_INTERVAL_MS = 100L
    private const val MAX_WAIT_MS = 30000L

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

    private fun isMp4Signature(bytes: ByteArray): Boolean {
        if (bytes.size < 8) return false
        val sig = String(bytes, 4, minOf(4, bytes.size - 4), Charsets.US_ASCII)
        val validSignatures = listOf("ftyp", "moov", "mdat", "free", "wide", "skip", "junk", "uuid", "pdat", "stbl", "mvhd", "trak", "mdia")
        return validSignatures.any { sig.startsWith(it) }
    }

    data class MegaFileInfo(val downloadUrl: String, val fileSize: Long, val fileName: String, val faHash: String?, val ufaPUrl: String? = null)

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
                var ufaPUrl: String? = null
                faHash?.let { hash ->
                    Log.d(TAG, "MEGA Step 2: ufa fah=$hash")
                    val ufaResp = megaApiPost(apiUrl, """[{"a":"ufa","fah":"$hash","r":1,"ssl":1}]""", 1)
                    ufaPUrl = ufaResp?.optString("p", null)
                    Log.d(TAG, "MEGA ufa p URL: ${ufaPUrl?.take(80)}")
                }

                val fileName = if (encryptedAttrs.isNotEmpty()) decryptFileName(encryptedAttrs, keyBytes) ?: "mega_file" else "mega_file"
                MegaFileInfo("", fileSize, fileName, faHash, ufaPUrl)
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

    // ===== Disk-Based Streaming =====

    class DiskStream(val fileSize: Long, val tempFile: File, val rawKeyBytes: ByteArray? = null) {
        val writtenBytes = AtomicLong(0L)
        val downloadComplete = AtomicBoolean(false)
        val availableChunks = ConcurrentHashMap.newKeySet<Int>()
        @Volatile var cdnUrl: String? = null
        @Volatile var failed = false
        @Volatile var errorMsg: String? = null
        @Volatile var resolvedAesKey: ByteArray? = null
        @Volatile var resolvedBaseIv: ByteArray? = null
        @Volatile var firstEncryptedBytes: ByteArray? = null
        @Volatile var useUfaUrl: Boolean = false

        fun totalChunks() = ((fileSize + CHUNK_SIZE - 1) / CHUNK_SIZE).toInt()
        fun chunkStart(ci: Int) = ci.toLong() * CHUNK_SIZE
        fun chunkEnd(ci: Int) = ((ci + 1).toLong() * CHUNK_SIZE - 1).coerceAtMost(fileSize - 1)
        fun chunkIndexForByte(bytePos: Long) = (bytePos / CHUNK_SIZE).toInt()

        fun waitForChunk(ci: Int, timeoutMs: Long = MAX_WAIT_MS): Boolean {
            val deadline = System.currentTimeMillis() + timeoutMs
            while (!availableChunks.contains(ci) && !downloadComplete.get() && !failed) {
                if (System.currentTimeMillis() >= deadline) return false
                Thread.sleep(WAIT_INTERVAL_MS)
            }
            return availableChunks.contains(ci) || downloadComplete.get()
        }

        fun cleanup() {
            try { if (tempFile.exists()) tempFile.delete() } catch (_: Exception) {}
        }
    }

    private fun backgroundDownloader(stream: DiskStream, aesKey: ByteArray, baseIv: ByteArray, fileId: String, faHash: String?, ufaPUrl: String? = null) {
        Thread {
            try {
                if (faHash != null) performUfaUnlock(faHash, 0)

                // Use ufa p URL as primary CDN URL if available (works better with msd:1 files)
                if (ufaPUrl != null) {
                    stream.cdnUrl = ufaPUrl
                    stream.useUfaUrl = true
                    Log.d(TAG, "Using ufa p URL as CDN: ${ufaPUrl.take(80)}")
                }

                val raf = RandomAccessFile(stream.tempFile, "rw")
                try {
                    val totalChunks = stream.totalChunks()
                    Log.d(TAG, "Background download: $totalChunks chunks to disk")

                    val lastChunk = totalChunks - 1

                    Log.d(TAG, "Phase 1: downloading chunk 0 first (initial data)")
                    downloadChunkWithFreshUrl(stream, raf, 0, aesKey, baseIv, fileId, faHash)

                    if (stream.failed) {
                        Log.e(TAG, "Phase 1 failed, aborting download")
                        return@Thread
                    }

                    if (lastChunk > 0) {
                        Log.d(TAG, "Phase 2: downloading last chunk $lastChunk (moov atom)")
                        downloadChunkWithFreshUrl(stream, raf, lastChunk, aesKey, baseIv, fileId, faHash)
                    }

                    Log.d(TAG, "Phase 3: downloading chunks 1..${(lastChunk - 1).coerceAtLeast(0)} sequentially")
                    for (ci in 1 until lastChunk) {
                        if (stream.failed) break
                        if (stream.availableChunks.contains(ci)) continue
                        downloadChunkWithFreshUrl(stream, raf, ci, aesKey, baseIv, fileId, faHash)
                    }

                    if (!stream.failed) {
                        stream.downloadComplete.set(true)
                        Log.d(TAG, "Download complete: ${stream.writtenBytes.get()}/${stream.fileSize}")
                    } else {
                        Log.e(TAG, "Download failed: ${stream.errorMsg} (${stream.writtenBytes.get()}/${stream.fileSize} bytes)")
                    }
                } finally {
                    raf.close()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Background download error: ${e.message}")
                stream.failed = true
                stream.errorMsg = e.message
            }
        }.start()
    }

    private fun downloadChunkWithFreshUrl(stream: DiskStream, raf: RandomAccessFile, ci: Int, aesKey: ByteArray, baseIv: ByteArray, fileId: String, faHash: String?) {
        var retries = 0
        val maxRetries = 30
        while (retries < maxRetries && !stream.failed) {
            try {
                if (stream.cdnUrl == null || retries > 0) {
                    if (!stream.useUfaUrl && retries % 5 == 0) {
                        Log.d(TAG, "Getting fresh CDN URL (attempt $retries)")
                        val freshUrl = getDownloadUrls(fileId)
                        if (freshUrl.isNotEmpty()) {
                            stream.cdnUrl = freshUrl[0]
                            Log.d(TAG, "Fresh CDN URL: ${freshUrl[0].removePrefix("https://").take(40)}")
                        } else if (stream.cdnUrl == null) {
                            stream.failed = true
                            stream.errorMsg = "No CDN URLs available"
                            return
                        }
                    } else if (stream.useUfaUrl && retries > 0 && retries % 5 == 0) {
                        // Re-fetch ufa p URL on retries for ufa mode
                        Log.d(TAG, "Re-fetching ufa p URL (attempt $retries)")
                        val freshUrl = getDownloadUrls(fileId)
                        if (freshUrl.isNotEmpty()) {
                            stream.cdnUrl = freshUrl[0]
                            Log.d(TAG, "Fresh CDN URL (fallback): ${freshUrl[0].removePrefix("https://").take(40)}")
                            stream.useUfaUrl = false
                        }
                    }
                }

                val start = stream.chunkStart(ci)
                val end = stream.chunkEnd(ci)
                val chunkUrl = if (stream.useUfaUrl) stream.cdnUrl!! else "${stream.cdnUrl}/$start-$end"
                Log.d(TAG, "Chunk $ci: url=${if (stream.useUfaUrl) "ufa" else "path"}, range=$start-$end")
                val conn = (URL(chunkUrl).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 15000; readTimeout = 60000
                    setRequestProperty("User-Agent", MEGA_UA)
                    setRequestProperty("Origin", "https://mega.nz")
                    setRequestProperty("Referer", "https://mega.nz/")
                    instanceFollowRedirects = true
                    if (stream.useUfaUrl) {
                        setRequestProperty("Range", "bytes=$start-$end")
                    }
                }
                val code = conn.responseCode
                if (code == 509) {
                    conn.disconnect()
                    retries++
                    val delay = (FIVE_ZERO_NINE_DELAY_MS * (1 + retries / 3)).coerceAtMost(60000L)
                    Log.w(TAG, "509 chunk $ci ($retries/$maxRetries), waiting ${delay}ms...")
                    Thread.sleep(delay)
                    continue
                }
                if (code == 416) {
                    conn.disconnect()
                    stream.availableChunks.add(ci)
                    return
                }
                if (code !in listOf(200, 206)) {
                    conn.disconnect()
                    retries++
                    Thread.sleep(3000L * retries.coerceAtMost(5))
                    continue
                }

                val useAesKey = stream.resolvedAesKey ?: aesKey
                val useBaseIv = stream.resolvedBaseIv ?: baseIv
                val cipher = Cipher.getInstance("AES/CTR/NoPadding")
                val ivForPos = useBaseIv.copyOf()
                val blockNum = start / 16
                ivForPos[8] = ((blockNum shr 56) and 0xFF).toByte()
                ivForPos[9] = ((blockNum shr 48) and 0xFF).toByte()
                ivForPos[10] = ((blockNum shr 40) and 0xFF).toByte()
                ivForPos[11] = ((blockNum shr 32) and 0xFF).toByte()
                ivForPos[12] = ((blockNum shr 24) and 0xFF).toByte()
                ivForPos[13] = ((blockNum shr 16) and 0xFF).toByte()
                ivForPos[14] = ((blockNum shr 8) and 0xFF).toByte()
                ivForPos[15] = (blockNum and 0xFF).toByte()
                cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(useAesKey, "AES"), IvParameterSpec(ivForPos))

                var firstBytes: ByteArray? = null
                conn.inputStream.use { enc ->
                    raf.seek(start)
                    val buf = ByteArray(256 * 1024)
                    var totalWritten = 0L
                    var firstEncBuf: java.io.ByteArrayOutputStream? = null
                    if (ci == 0) firstEncBuf = java.io.ByteArrayOutputStream()
                    while (true) {
                        val n = enc.read(buf)
                        if (n == -1) break
                        firstEncBuf?.let { eb ->
                            if (eb.size() < 64 * 1024) eb.write(buf, 0, minOf(n, (64 * 1024 - eb.size())))
                        }
                        val dec = cipher.update(buf, 0, n) ?: continue
                        raf.write(dec)
                        if (firstBytes == null && dec.isNotEmpty()) {
                            firstBytes = dec.copyOf(minOf(32, dec.size))
                        }
                        totalWritten += dec.size
                    }
                    if (ci == 0) {
                        stream.firstEncryptedBytes = firstEncBuf?.toByteArray()
                        Log.d(TAG, "Saved ${stream.firstEncryptedBytes?.size ?: 0} encrypted bytes for fallback")
                    }
                    stream.writtenBytes.addAndGet(totalWritten)
                    if (ci == 0 && firstBytes != null) {
                        val hex = firstBytes.joinToString("") { "%02x".format(it) }
                        val ascii = firstBytes.map { b -> if (b in 32..126) b.toInt().toChar() else '.' }.joinToString("")
                        Log.d(TAG, "Chunk 0 first 32 bytes: $hex | $ascii")
                    }
                }
                conn.disconnect()

                // After chunk 0: validate MP4 and try fallback key derivations if standard fails
                if (ci == 0 && stream.rawKeyBytes != null && firstBytes != null && !isMp4Signature(firstBytes)) {
                    Log.w(TAG, "Standard derivation produced garbage MP4, trying fallback key derivations...")
                    val rawKey = stream.rawKeyBytes

                    // Log first encrypted bytes hex for debugging
                    stream.firstEncryptedBytes?.let { enc ->
                        val encHex = enc.take(64).joinToString("") { "%02x".format(it) }
                        Log.d(TAG, "First 64 encrypted bytes: $encHex")
                    }

                    // Derivation: name → (aesKey, baseIv, useAdditiveCounter)
                    // useAdditiveCounter=true means iv[8..15] has initial counter, block counter ADDED to it
                    // useAdditiveCounter=false means iv[8..15] is REPLACED by block counter
                    data class FallbackDeriv(val name: String, val aesKey: ByteArray, val baseIv: ByteArray, val additiveCounter: Boolean)

                    val fallbacks = listOf(
                        // SDK: key=first16, IV=last16 (full), counter ADDITIVE
                        FallbackDeriv("sdk-fulliv", rawKey.copyOf(16), rawKey.copyOfRange(16, 32), true),
                        // XOR key + full IV
                        FallbackDeriv("xor-fulliv", ByteArray(16).also { for (i in 0 until 16) it[i] = (rawKey[i].toInt() xor rawKey[i + 16].toInt()).toByte() }, rawKey.copyOfRange(16, 32), true),
                        // SDK: key=first16, IV=nonce(8)+zeros, counter REPLACES
                        FallbackDeriv("sdk-nonceonly", rawKey.copyOf(16), ByteArray(16).also { System.arraycopy(rawKey, 16, it, 0, 8) }, false),
                        // XOR key + nonce only
                        FallbackDeriv("xor-nonceonly", ByteArray(16).also { for (i in 0 until 16) it[i] = (rawKey[i].toInt() xor rawKey[i + 16].toInt()).toByte() }, ByteArray(16).also { System.arraycopy(rawKey, 16, it, 0, 8) }, false),
                        // key=first16, IV=zeros (full zeros)
                        FallbackDeriv("sdk-zerosIv", rawKey.copyOf(16), ByteArray(16), false),
                        // key=XOR, IV=nonce+zeros2 reversed (nonce from first half)
                        FallbackDeriv("xor-reversedNonce", ByteArray(16).also { for (i in 0 until 16) it[i] = (rawKey[i].toInt() xor rawKey[i + 16].toInt()).toByte() }, ByteArray(16).also { System.arraycopy(rawKey, 0, it, 0, 8) }, false),
                        // key=first16, IV=nonce from first half + counter from second half
                        FallbackDeriv("sdk-reversedNonceFull", rawKey.copyOf(16), ByteArray(16).also { System.arraycopy(rawKey, 0, it, 0, 8); System.arraycopy(rawKey, 24, it, 8, 8) }, true),
                        // key=XOR, IV=nonce from first half + counter from second half
                        FallbackDeriv("xor-reversedNonceFull", ByteArray(16).also { for (i in 0 until 16) it[i] = (rawKey[i].toInt() xor rawKey[i + 16].toInt()).toByte() }, ByteArray(16).also { System.arraycopy(rawKey, 0, it, 0, 8); System.arraycopy(rawKey, 24, it, 8, 8) }, true),
                        // key=full32 truncated to16, IV=last16
                        FallbackDeriv("full32to16-fulliv", rawKey.copyOf(16), rawKey.copyOfRange(16, 32), false)
                    )

                    for (fb in fallbacks) {
                        Log.d(TAG, "Trying: ${fb.name}, key=${fb.aesKey.joinToString("") { "%02x".format(it) }}, iv=${fb.baseIv.joinToString("") { "%02x".format(it) }}, additive=${fb.additiveCounter}")

                        val chunkUrl = "${stream.cdnUrl}/$start-$end"
                        val retryConn = (URL(chunkUrl).openConnection() as HttpURLConnection).apply {
                            connectTimeout = 15000; readTimeout = 60000
                            setRequestProperty("User-Agent", MEGA_UA)
                            setRequestProperty("Origin", "https://mega.nz")
                            setRequestProperty("Referer", "https://mega.nz/")
                            instanceFollowRedirects = true
                        }
                        val retryCode = retryConn.responseCode
                        if (retryCode !in listOf(200, 206)) {
                            retryConn.disconnect()
                            continue
                        }

                        val retryCipher = Cipher.getInstance("AES/CTR/NoPadding")
                        val retryIv = fb.baseIv.copyOf()
                        val retryBlockNum = start / 16
                        if (fb.additiveCounter) {
                            // ADD block counter to existing iv[8..15] (big-endian)
                            var carry = retryBlockNum
                            for (i in 15 downTo 8) {
                                val sum = (retryIv[i].toInt() and 0xFF) + (carry and 0xFF)
                                retryIv[i] = (sum and 0xFF).toByte()
                                carry = (carry shr 8) + (sum shr 8)
                            }
                        } else {
                            // REPLACE iv[8..15] with block counter
                            retryIv[8] = ((retryBlockNum shr 56) and 0xFF).toByte()
                            retryIv[9] = ((retryBlockNum shr 48) and 0xFF).toByte()
                            retryIv[10] = ((retryBlockNum shr 40) and 0xFF).toByte()
                            retryIv[11] = ((retryBlockNum shr 32) and 0xFF).toByte()
                            retryIv[12] = ((retryBlockNum shr 24) and 0xFF).toByte()
                            retryIv[13] = ((retryBlockNum shr 16) and 0xFF).toByte()
                            retryIv[14] = ((retryBlockNum shr 8) and 0xFF).toByte()
                            retryIv[15] = (retryBlockNum and 0xFF).toByte()
                        }
                        retryCipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(fb.aesKey, "AES"), IvParameterSpec(retryIv))

                        retryConn.inputStream.use { retryEnc ->
                            raf.seek(start)
                            val retryBuf = ByteArray(256 * 1024)
                            var retryTotalWritten = 0L
                            var retryFirstBytes: ByteArray? = null
                            while (true) {
                                val n = retryEnc.read(retryBuf)
                                if (n == -1) break
                                val dec = retryCipher.update(retryBuf, 0, n) ?: continue
                                raf.write(dec)
                                if (retryFirstBytes == null && dec.isNotEmpty()) {
                                    retryFirstBytes = dec.copyOf(minOf(32, dec.size))
                                }
                                retryTotalWritten += dec.size
                            }
                            if (retryFirstBytes != null) {
                                val hex = retryFirstBytes.joinToString("") { "%02x".format(it) }
                                Log.d(TAG, "${fb.name} first 32: $hex")
                                if (isMp4Signature(retryFirstBytes)) {
                                    Log.d(TAG, "FOUND WORKING DERIVATION: ${fb.name}")
                                    stream.resolvedAesKey = fb.aesKey
                                    stream.resolvedBaseIv = fb.baseIv
                                    stream.writtenBytes.addAndGet(retryTotalWritten)
                                    stream.availableChunks.add(ci)
                                    Log.d(TAG, "Chunk $ci -> disk via ${fb.name} (${stream.writtenBytes.get()}/${stream.fileSize})")
                                    retryConn.disconnect()
                                    return
                                }
                            }
                        }
                        retryConn.disconnect()
                    }
                    Log.e(TAG, "All fallback key derivations failed for chunk 0")
                }

                stream.availableChunks.add(ci)
                Log.d(TAG, "Chunk $ci -> disk (${stream.writtenBytes.get()}/${stream.fileSize})")
                return
            } catch (e: Exception) {
                retries++
                Log.w(TAG, "Chunk $ci error ($retries/$maxRetries): ${e.message}")
                Thread.sleep(3000L * retries.coerceAtMost(5))
            }
        }

        if (!stream.failed) {
            stream.failed = true
            stream.errorMsg = "Chunk $ci failed after $maxRetries retries"
        }
    }

    private class StreamState(val stream: DiskStream)

    private fun startStreamProxy(fileSize: Long, aesKey: ByteArray, iv: ByteArray, fileId: String, faHash: String?, rawKeyBytes: ByteArray?, ufaPUrl: String? = null): StreamProxyResult? {
        return try {
            val serverSocket = ServerSocket(0)
            serverSocket.soTimeout = 600000
            val port = serverSocket.localPort

            val tempFile = File.createTempFile("mega_stream_", ".mp4", File(System.getProperty("java.io.tmpdir") ?: "/tmp"))
            tempFile.deleteOnExit()
            Log.d(TAG, "Temp file: ${tempFile.absolutePath} (${fileSize / 1024 / 1024}MB)")

            val stream = DiskStream(fileSize, tempFile, rawKeyBytes)
            backgroundDownloader(stream, aesKey, iv, fileId, faHash, ufaPUrl)

            val state = StreamState(stream)

            Thread {
                try {
                    while (!serverSocket.isClosed) {
                        val cs = serverSocket.accept()
                        Thread { handleStreamClient(cs, state) }.start()
                    }
                } catch (e: Exception) {
                    if (!serverSocket.isClosed) Log.e(TAG, "Server err: ${e.message}")
                }
            }.start()

            Log.d(TAG, "Stream proxy started on port $port")
            StreamProxyResult("http://127.0.0.1:$port/video", port, stream, serverSocket)
        } catch (e: Exception) { Log.e(TAG, "Proxy start fail: ${e.message}"); null }
    }

    private data class StreamProxyResult(val url: String, val port: Int, val stream: DiskStream, val serverSocket: ServerSocket?)

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

    private fun handleStreamClient(socket: Socket, state: StreamState) {
        try {
            val input = BufferedReader(java.io.InputStreamReader(socket.getInputStream()))
            val output = socket.getOutputStream()
            val requestLine = input.readLine() ?: return

            val headers = mutableMapOf<String, String>()
            while (true) {
                val line = input.readLine() ?: break
                if (line.isEmpty()) break
                val colonIdx = line.indexOf(':')
                if (colonIdx > 0) {
                    headers[line.substring(0, colonIdx).trim().lowercase()] = line.substring(colonIdx + 1).trim()
                }
            }

            val method = requestLine.split(" ").firstOrNull() ?: ""
            val path = requestLine.split(" ").getOrElse(1) { "?" }
            Log.d(TAG, "$method $path range=${headers["range"] ?: "none"} chunks=${state.stream.availableChunks.size}/${state.stream.totalChunks()}")

            if (method == "HEAD") {
                val resp = "HTTP/1.1 200 OK\r\nContent-Type: video/mp4\r\nContent-Length: ${state.stream.fileSize}\r\nAccept-Ranges: bytes\r\nConnection: close\r\n\r\n"
                output.write(resp.toByteArray()); output.flush(); socket.close()
                return
            }

            val stream = state.stream
            if (stream.failed) {
                val msg = stream.errorMsg ?: "MEGA download failed"
                Log.e(TAG, "Stream failed, returning 503: $msg")
                sendError(socket, 503, msg)
                return
            }

            var startByte = 0L
            var endByte = stream.fileSize - 1
            var hasRangeHeader = false

            headers["range"]?.let { rangeValue ->
                Regex("""bytes=(\d+)-(\d*)""").find(rangeValue)?.let {
                    startByte = it.groupValues[1].toLong()
                    if (it.groupValues[2].isNotEmpty()) endByte = it.groupValues[2].toLong()
                    hasRangeHeader = true
                }
            }

            handleStreamRange(socket, output, startByte, endByte, hasRangeHeader, state)
        } catch (e: Exception) {
            Log.e(TAG, "Client err: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun handleStreamRange(socket: Socket, output: java.io.OutputStream, startByteIn: Long, endByteIn: Long, hasRangeHeader: Boolean, state: StreamState) {
        val stream = state.stream
        try {
            if (startByteIn >= stream.fileSize) {
                val resp = "HTTP/1.1 416 Range Not Satisfiable\r\nContent-Range: bytes */${stream.fileSize}\r\nContent-Length: 0\r\nConnection: close\r\n\r\n"
                output.write(resp.toByteArray()); output.flush()
                Log.d(TAG, "416: start=$startByteIn >= fileSize=${stream.fileSize}")
                socket.close()
                return
            }

            val startByte = startByteIn
            val endByte = endByteIn.coerceAtMost(stream.fileSize - 1)
            val actualEnd = if (hasRangeHeader) endByte else stream.fileSize - 1
            val contentLength = actualEnd - startByte + 1

            val resp = buildString {
                if (hasRangeHeader) {
                    append("HTTP/1.1 206 Partial Content\r\n")
                    append("Content-Range: bytes $startByte-$actualEnd/${stream.fileSize}\r\n")
                } else {
                    append("HTTP/1.1 200 OK\r\n")
                }
                append("Content-Type: video/mp4\r\n")
                append("Content-Length: $contentLength\r\n")
                append("Accept-Ranges: bytes\r\n")
                append("Connection: close\r\n\r\n")
            }
            output.write(resp.toByteArray()); output.flush()

            Log.d(TAG, "Serve: $startByte-$actualEnd ($contentLength bytes) range=$hasRangeHeader")

            val raf = RandomAccessFile(stream.tempFile, "r")
            try {
                var pos = startByte
                val buf = ByteArray(64 * 1024)

                while (pos <= actualEnd) {
                    if (stream.failed) {
                        Log.e(TAG, "Stream failed during serve")
                        break
                    }

                    val ci = stream.chunkIndexForByte(pos)
                    if (!stream.availableChunks.contains(ci) && !stream.downloadComplete.get()) {
                        if (!stream.waitForChunk(ci, MAX_WAIT_MS)) {
                            Log.w(TAG, "Timeout waiting for chunk $ci (pos=$pos)")
                            break
                        }
                    }

                    if (!stream.availableChunks.contains(ci)) break

                    raf.seek(pos)
                    val toRead = minOf(buf.size.toLong(), stream.chunkEnd(ci) - pos + 1, actualEnd - pos + 1).toInt()
                    val n = raf.read(buf, 0, toRead)
                    if (n <= 0) {
                        Thread.sleep(50)
                        continue
                    }
                    output.write(buf, 0, n)
                    output.flush()
                    pos += n
                }

                Log.d(TAG, "Serve done: $startByte-${pos - 1} (${pos - startByte} bytes)")
            } finally {
                raf.close()
                socket.close()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Serve end: ${e.message}")
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

    data class MegaProxyResult(val url: String, val port: Int, val stream: DiskStream, val serverSocket: ServerSocket?)

    private val activeProxies = ConcurrentHashMap<String, MegaProxyResult>()
    @Volatile private var currentFileId: String? = null

    fun cleanup(fileId: String) {
        activeProxies.remove(fileId)?.let { result ->
            Log.d(TAG, "Cleaning up proxy for $fileId (port ${result.port})")
            result.stream.failed = true
            try { result.serverSocket?.close() } catch (_: Exception) {}
            try { result.stream.tempFile.delete() } catch (_: Exception) {}
        }
    }

    fun cleanupAll() {
        activeProxies.forEach { (fileId, result) ->
            Log.d(TAG, "Cleaning up proxy for $fileId")
            result.stream.failed = true
            try { result.serverSocket?.close() } catch (_: Exception) {}
            try { result.stream.tempFile.delete() } catch (_: Exception) {}
        }
        activeProxies.clear()
        currentFileId = null
    }

    suspend fun extractMegaUrl(megaUrl: String): MegaProxyResult? {
        return withContext(Dispatchers.IO) {
            try {
                val urlInfo = parseMegaUrl(megaUrl) ?: return@withContext null
                Log.d(TAG, "Parsed: fileId=${urlInfo.fileId}, key=${urlInfo.key.take(20)}...")

                if (urlInfo.fileId != currentFileId) {
                    Log.d(TAG, "Episode changed: $currentFileId -> ${urlInfo.fileId}, cleaning old proxies")
                    val oldId = currentFileId
                    currentFileId = urlInfo.fileId
                    oldId?.let { cleanup(it) }
                }

                activeProxies[urlInfo.fileId]?.let { existing ->
                    if (!existing.stream.failed) {
                        Log.d(TAG, "Reusing existing proxy for ${urlInfo.fileId}: ${existing.url}")
                        return@withContext existing
                    }
                    Log.d(TAG, "Existing proxy for ${urlInfo.fileId} failed, creating new one")
                    cleanup(urlInfo.fileId)
                }

                val keyBytes = base64UrlDecode(urlInfo.key)
                val fileInfo = getFileInfo(urlInfo.fileId, keyBytes) ?: return@withContext null
                val (aesKey, iv) = deriveKeyAndIv(keyBytes)
                Log.d(TAG, "AES=${aesKey.size}B IV=${iv.size}B, size=${fileInfo.fileSize / 1024 / 1024}MB")
                Log.d(TAG, "keyHex=${aesKey.joinToString("") { "%02x".format(it) }}")
                Log.d(TAG, "ivHex=${iv.joinToString("") { "%02x".format(it) }}")
                Log.d(TAG, "keyBytesLen=${keyBytes.size}, keyBytesHex=${keyBytes.take(32).joinToString("") { "%02x".format(it) }}")

                val result = startStreamProxy(fileInfo.fileSize, aesKey, iv, urlInfo.fileId, fileInfo.faHash, keyBytes, fileInfo.ufaPUrl) ?: return@withContext null
                Log.d(TAG, "Stream proxy ready: ${result.url}")

                val proxyResult = MegaProxyResult(result.url, result.port, result.stream, result.serverSocket)
                activeProxies[urlInfo.fileId] = proxyResult
                proxyResult
            } catch (e: Exception) { Log.e(TAG, "Extract fail: ${e.message}", e); null }
        }
    }
}
