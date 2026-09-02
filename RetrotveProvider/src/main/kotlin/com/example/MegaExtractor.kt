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
                    performUfaUnlock(hash, 1)
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
        @Volatile var cdnUrls: List<String> = emptyList()
        @Volatile var cdnUrlIndex: Int = 0
        @Volatile var shardOffsets: LinkedHashMap<String, Long> = linkedMapOf()

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

    private fun backgroundDownloader(stream: DiskStream, aesKey: ByteArray, baseIv: ByteArray, fileId: String, faHash: String?) {
        Thread {
            try {
                if (faHash != null) performUfaUnlock(faHash, 0)

                val raf = RandomAccessFile(stream.tempFile, "rw")
                try {
                    val totalChunks = stream.totalChunks()
                    Log.d(TAG, "Background download: $totalChunks chunks (${stream.fileSize / 1024 / 1024}MB) to disk")

                    val lastChunk = totalChunks - 1

                    // STEP 1: Fetch CDN URLs and download chunk 0 IMMEDIATELY
                    // (before probe — ExoPlayer times out after ~30s)
                    Log.d(TAG, "Phase 1: fetching CDN URLs + downloading chunk 0 immediately")
                    downloadChunkWithFreshUrl(stream, raf, 0, aesKey, baseIv, fileId, faHash)

                    if (stream.failed) {
                        Log.e(TAG, "Phase 1 failed, aborting download")
                        return@Thread
                    }
                    Log.d(TAG, "Phase 1 OK: ${stream.writtenBytes.get()}/${stream.fileSize} bytes")

                    // STEP 2: Probe shard offsets (now in background, ExoPlayer already playing)
                    if (stream.cdnUrls.size > 1 && stream.shardOffsets.isEmpty()) {
                        Log.d(TAG, "Phase 2: probing shard offsets for ${stream.cdnUrls.size} CDN URLs...")
                        stream.shardOffsets = probeShardOffsets(stream.cdnUrls, aesKey, baseIv, stream.fileSize)
                    }

                    if (lastChunk > 0) {
                        Log.d(TAG, "Phase 3: downloading last chunk $lastChunk (moov atom)")
                        downloadChunkWithFreshUrl(stream, raf, lastChunk, aesKey, baseIv, fileId, faHash)
                    }

                    if (!stream.failed) {
                        Log.d(TAG, "Phase 3 OK: ${stream.writtenBytes.get()}/${stream.fileSize} bytes")
                    }

                    Log.d(TAG, "Phase 4: downloading chunks 1..${(lastChunk - 1).coerceAtLeast(0)} sequentially")
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
                // Fetch CDN URLs if we don't have any yet
                if (stream.cdnUrls.isEmpty()) {
                    Log.d(TAG, "Fetching CDN URLs for $fileId")
                    val urls = getDownloadUrls(fileId)
                    if (urls.isEmpty()) {
                        if (stream.cdnUrl == null) {
                            stream.failed = true
                            stream.errorMsg = "No CDN URLs available"
                            return
                        }
                    } else {
                        stream.cdnUrls = urls
                        // For multi-CDN: start at CDN #1 (known ftyp shard) to avoid garbage from CDN #0
                        stream.cdnUrlIndex = if (urls.size > 1) 1 else 0
                        stream.cdnUrl = urls[stream.cdnUrlIndex]
                        Log.d(TAG, "Got ${urls.size} CDN URL(s), starting at CDN #${stream.cdnUrlIndex}: ${stream.cdnUrl!!.removePrefix("https://").take(40)}")
                        // NOTE: shard probe is done in backgroundDownloader (not here) to avoid blocking chunk 0
                    }
                }

                // On retries, cycle through all CDN URLs
                if (retries > 0) {
                    var freshFetched = false
                    if (retries % 5 == 0) {
                        Log.d(TAG, "Getting fresh CDN URLs (attempt $retries)")
                        val freshUrls = getDownloadUrls(fileId)
                    if (freshUrls.isNotEmpty()) {
                        stream.cdnUrls = freshUrls
                        stream.cdnUrlIndex = 0
                        stream.cdnUrl = freshUrls[0]
                        freshFetched = true
                        Log.d(TAG, "Fresh URLs (${freshUrls.size}):")
                        freshUrls.forEachIndexed { i, u ->
                            val h = u.removePrefix("https://").takeWhile { it != '/' }
                            Log.d(TAG, "  Fresh #$i: $h")
                        }
                            // Re-probe shards with fresh URLs
                            if (freshUrls.size > 1) {
                                stream.shardOffsets = probeShardOffsets(freshUrls, aesKey, baseIv, stream.fileSize)
                            }
                        }
                    }
                    if (!freshFetched && stream.cdnUrls.isNotEmpty()) {
                        stream.cdnUrlIndex = (stream.cdnUrlIndex + 1) % stream.cdnUrls.size
                        stream.cdnUrl = stream.cdnUrls[stream.cdnUrlIndex]
                        Log.d(TAG, "Trying CDN URL #${stream.cdnUrlIndex}: ${stream.cdnUrl!!.removePrefix("https://").take(40)}")
                    }
                }

                val start = stream.chunkStart(ci)
                val end = stream.chunkEnd(ci)
                val shardOffsets = stream.shardOffsets

                // Build sub-chunks: split cross-shard chunks across multiple CDN URLs
                data class SubChunk(val filePos: Long, val fileSize: Long, val cdnUrl: String, val relStart: Long, val relEnd: Long)
                val subChunks = mutableListOf<SubChunk>()

                if (shardOffsets.size > 1) {
                    // Build sorted shard list with end offsets
                    val sortedShards = shardOffsets.entries.sortedBy { it.value }
                    var pos = start
                    for (i in sortedShards.indices) {
                        if (pos > end) break
                        val shardStart = sortedShards[i].value
                        val shardEnd = if (i + 1 < sortedShards.size) sortedShards[i + 1].value - 1 else Long.MAX_VALUE
                        // Bytes of this chunk that fall within this shard
                        val overlapStart = pos.coerceAtLeast(shardStart)
                        val overlapEnd = end.coerceAtMost(shardEnd)
                        if (overlapStart <= overlapEnd) {
                            subChunks.add(SubChunk(overlapStart, overlapEnd - overlapStart + 1, sortedShards[i].key, overlapStart - shardStart, overlapEnd - shardStart))
                            pos = overlapEnd + 1
                        }
                    }
                }

                if (subChunks.isEmpty()) {
                    subChunks.add(SubChunk(start, end - start + 1, stream.cdnUrl!!, start, end))
                }

                if (subChunks.size > 1) {
                    Log.d(TAG, "Chunk $ci: CROSS-SHARD split into ${subChunks.size} sub-chunks")
                    for (sc in subChunks) {
                        val h = sc.cdnUrl.removePrefix("https://").takeWhile { it != '/' }
                        Log.d(TAG, "  file=${sc.filePos}-${sc.filePos + sc.fileSize - 1} via ${h.take(30)}... rel=${sc.relStart}-${sc.relEnd}")
                    }
                }

                // Download each sub-chunk
                var totalWritten = 0L
                var firstBytes: ByteArray? = null
                var failedSubChunk = false
                val useAesKey = stream.resolvedAesKey ?: aesKey
                val useBaseIv = stream.resolvedBaseIv ?: baseIv

                for ((si, sc) in subChunks.withIndex()) {
                    val subChunkUrl = "${sc.cdnUrl}/${sc.relStart}-${sc.relEnd}"
                    val hostname = sc.cdnUrl.removePrefix("https://").takeWhile { it != '/' }
                    if (subChunks.size > 1) {
                        Log.d(TAG, "Sub[$si]: downloading from $hostname (${sc.relStart}-${sc.relEnd}, ${sc.fileSize}B)")
                    } else {
                        Log.d(TAG, "Chunk $ci: downloading from $hostname (${sc.relStart}-${sc.relEnd}, ${sc.fileSize}B) (attempt $retries)")
                    }

                    val conn = (URL(subChunkUrl).openConnection() as HttpURLConnection).apply {
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
                        val delay = (FIVE_ZERO_NINE_DELAY_MS * (1 + retries / 3)).coerceAtMost(60000L)
                        Log.w(TAG, "509 chunk $ci ($retries/$maxRetries), waiting ${delay}ms...")
                        Thread.sleep(delay)
                        failedSubChunk = true
                        break
                    }
                    if (code == 416) {
                        conn.disconnect()
                        if (subChunks.size == 1) {
                            stream.availableChunks.add(ci)
                            return
                        }
                        continue
                    }
                    if (code !in listOf(200, 206)) {
                        conn.disconnect()
                        retries++
                        Thread.sleep(3000L * retries.coerceAtMost(5))
                        failedSubChunk = true
                        break
                    }

                    val cipher = Cipher.getInstance("AES/CTR/NoPadding")
                    val ivForSub = useBaseIv.copyOf()
                    setIvBlockCounter(ivForSub, sc.filePos)
                    cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(useAesKey, "AES"), IvParameterSpec(ivForSub))

                    var subWritten = 0L
                    conn.inputStream.use { enc ->
                        raf.seek(sc.filePos)
                        val buf = ByteArray(256 * 1024)
                        var firstEncBuf: java.io.ByteArrayOutputStream? = null
                        if (ci == 0 && si == 0) firstEncBuf = java.io.ByteArrayOutputStream()
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
                            subWritten += dec.size
                        }
                        if (ci == 0 && si == 0) {
                            stream.firstEncryptedBytes = firstEncBuf?.toByteArray()
                        }
                        totalWritten += subWritten

                        // For cross-shard: verify sub-chunk size
                        if (subChunks.size > 1 && subWritten < sc.fileSize) {
                            Log.w(TAG, "Sub[$si] incomplete: got $subWritten/${sc.fileSize} bytes")
                            retries++
                            Thread.sleep(1000L)
                            failedSubChunk = true
                        }
                    }
                    conn.disconnect()
                    if (failedSubChunk) break
                }
                if (failedSubChunk) continue

                stream.writtenBytes.addAndGet(totalWritten)
                if (ci == 0 && firstBytes != null) {
                    val hex = firstBytes.joinToString("") { "%02x".format(it) }
                    val ascii = firstBytes.map { b -> if (b in 32..126) b.toInt().toChar() else '.' }.joinToString("")
                    Log.d(TAG, "Chunk 0 first 32 bytes: $hex | $ascii")
                }

                // For non-chunk0: detect incomplete or 0-byte CDN response (shard mismatch)
                if (ci > 0) {
                    val expectedSize = end - start + 1
                    if (totalWritten == 0L) {
                        Log.w(TAG, "Chunk $ci got 0 bytes (range=$start-$end, expected=$expectedSize)")
                        retries++
                        Thread.sleep(1000L)
                        continue
                    } else if (totalWritten < expectedSize) {
                        Log.w(TAG, "Chunk $ci incomplete: got $totalWritten/$expectedSize bytes, retrying")
                        stream.writtenBytes.addAndGet(-totalWritten)
                        raf.seek(start)
                        raf.setLength(start)
                        retries++
                        Thread.sleep(1000L)
                        continue
                    }
                }

                // After chunk 0: validate MP4 signature
                if (ci == 0 && firstBytes != null && !isMp4Signature(firstBytes)) {
                    stream.writtenBytes.addAndGet(-totalWritten)
                    if (stream.cdnUrls.size > 1 && stream.cdnUrlIndex < stream.cdnUrls.size - 1) {
                        Log.w(TAG, "Chunk 0 garbage from CDN #${stream.cdnUrlIndex}, trying next CDN URL immediately")
                        retries++
                        continue
                    }
                    Log.w(TAG, "All CDN URLs failed standard derivation, trying fallback key derivations...")
                    stream.writtenBytes.addAndGet(totalWritten)
                    val rawKey = stream.rawKeyBytes
                    if (rawKey == null || rawKey.size < 32) {
                        Log.e(TAG, "No raw key bytes available for fallback")
                        stream.availableChunks.add(ci)
                        return
                    }

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

                        // Use shard-relative range for fallback too
                        val fallbackShardOffset = stream.shardOffsets[stream.cdnUrl] ?: 0L
                        val fallbackRelStart = start - fallbackShardOffset
                        val fallbackRelEnd = end - fallbackShardOffset
                        val chunkUrl = "${stream.cdnUrl}/$fallbackRelStart-$fallbackRelEnd"
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
                    Log.e(TAG, "All fallback key derivations failed for chunk 0, trying next CDN URL")
                    retries++
                    Thread.sleep(2000L)
                    continue
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

    private fun startStreamProxy(fileSize: Long, aesKey: ByteArray, iv: ByteArray, fileId: String, faHash: String?, rawKeyBytes: ByteArray?): StreamProxyResult? {
        return try {
            val serverSocket = ServerSocket(0)
            serverSocket.soTimeout = 600000
            val port = serverSocket.localPort

            val tempFile = File.createTempFile("mega_stream_", ".mp4", File(System.getProperty("java.io.tmpdir") ?: "/tmp"))
            tempFile.deleteOnExit()
            Log.d(TAG, "Temp file: ${tempFile.absolutePath} (${fileSize / 1024 / 1024}MB)")

            val stream = DiskStream(fileSize, tempFile, rawKeyBytes)
            backgroundDownloader(stream, aesKey, iv, fileId, faHash)

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
        // Log full response structure for debugging
        val responseKeys = mutableListOf<String>()
        val iter = resp.keys()
        while (iter.hasNext()) responseKeys.add(iter.next())
        Log.d(TAG, "a=g response keys: $responseKeys")
        responseKeys.forEach { key ->
            val value = resp.opt(key)
            val preview = when (value) {
                is org.json.JSONArray -> "JSONArray(${value.length()} items)"
                is String -> if (value.length > 100) "String(${value.length}B): ${value.take(80)}..." else "String: $value"
                else -> value?.toString()?.take(80) ?: "null"
            }
            Log.d(TAG, "  [$key] = $preview")
        }

        val g = resp.opt("g") ?: return emptyList()
        val urls = when (g) {
            is org.json.JSONArray -> {
                Log.d(TAG, "g field is JSONArray with ${g.length()} items")
                (0 until g.length()).mapNotNull { g.optString(it, null) }
            }
            is String -> {
                Log.d(TAG, "g field is String: ${g.take(80)}")
                listOf(g)
            }
            else -> {
                Log.d(TAG, "g field is unknown type: ${g?.javaClass?.simpleName}")
                emptyList()
            }
        }

        // Log each URL's hostname for shard identification
        urls.forEachIndexed { i, url ->
            val hostname = url.removePrefix("https://").takeWhile { it != '/' }
            Log.d(TAG, "CDN #$i: $hostname")
        }
        Log.d(TAG, "MEGA got ${urls.size} CDN URL(s)")
        return urls
    }

    /**
     * Set IV block counter at position iv[8..15] for AES-CTR decryption.
     * Handles block-alignment: byte position / 16 = block number.
     */
    private fun setIvBlockCounter(iv: ByteArray, bytePosition: Long) {
        val blockNum = bytePosition / 16
        iv[8] = ((blockNum shr 56) and 0xFF).toByte()
        iv[9] = ((blockNum shr 48) and 0xFF).toByte()
        iv[10] = ((blockNum shr 40) and 0xFF).toByte()
        iv[11] = ((blockNum shr 32) and 0xFF).toByte()
        iv[12] = ((blockNum shr 24) and 0xFF).toByte()
        iv[13] = ((blockNum shr 16) and 0xFF).toByte()
        iv[14] = ((blockNum shr 8) and 0xFF).toByte()
        iv[15] = (blockNum and 0xFF).toByte()
    }

    /**
     * Try to decrypt `sample` assuming it's encrypted at file position `fileOffset`.
     * Returns decrypted bytes or null if decrypt fails.
     * Handles block-alignment: if fileOffset is not 16-byte aligned, advances CTR counter past the partial block.
     */
    private fun tryDecryptAtOffset(aesKey: ByteArray, baseIv: ByteArray, fileOffset: Long, sample: ByteArray): ByteArray? {
        return try {
            val cipher = Cipher.getInstance("AES/CTR/NoPadding")
            val iv = baseIv.copyOf()
            setIvBlockCounter(iv, fileOffset)
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(aesKey, "AES"), IvParameterSpec(iv))

            val partialBlockBytes = (fileOffset % 16).toInt()
            if (partialBlockBytes > 0) {
                // Advance CTR counter past the partial block by decrypting dummy bytes
                cipher.update(ByteArray(partialBlockBytes))
            }
            cipher.doFinal(sample)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * For msd:1 files with multiple CDN URLs, probe each URL to find which byte range (shard) it serves.
     * v57 strategy:
     * 1. Quick no-range test to detect mirrors vs shards
     * 2. For each URL: download ONE full shard (~66MB) without path-based range
     * 3. Decrypt at 6 candidate offsets (0, 66, 132, 198, 264, 330MB) to find valid MP4 signature
     * 4. The first shard is always at offset 0 (ftyp). Other shards at known offsets.
     * 5. No heuristic guessing — only confirmed MP4 signatures
     *
     * Returns LinkedHashMap<url, shardOffset> sorted by offset.
     */
    private fun probeShardOffsets(cdnUrls: List<String>, aesKey: ByteArray, baseIv: ByteArray, fileSize: Long): LinkedHashMap<String, Long> {
        val result = linkedMapOf<String, Long>()
        if (cdnUrls.size <= 1) return result

        val numUrls = cdnUrls.size
        // MEGA CDN hard-limits to ~66MB per connection (confirmed by no-range test)
        val SHARD_SIZE = 66L * 1024 * 1024  // 66MB
        val downloadSize = SHARD_SIZE.coerceAtMost(fileSize)
        Log.d(TAG, "Probing $numUrls CDN URLs for shard offsets (fileSize=${fileSize / 1024 / 1024}MB, download=${downloadSize / 1024 / 1024}MB per URL)")

        // Quick test: download without path-based ranges to check if these are mirrors or shards
        try {
            val testUrl = cdnUrls.first()
            val conn = (URL(testUrl).openConnection() as HttpURLConnection).apply {
                connectTimeout = 15000; readTimeout = 60000
                setRequestProperty("User-Agent", MEGA_UA)
                setRequestProperty("Origin", "https://mega.nz")
                setRequestProperty("Referer", "https://mega.nz/")
                instanceFollowRedirects = true
            }
            val code = conn.responseCode
            val contentLength = conn.getHeaderField("Content-Length")?.toLongOrNull() ?: 0L
            Log.d(TAG, "No-range test: HTTP $code, Content-Length=$contentLength (${contentLength / 1024 / 1024}MB)")
            conn.disconnect()

            if (code == 200 && contentLength >= fileSize - 1024) {
                Log.d(TAG, "URLs are MIRRORS (full file). Single URL mode.")
                result[cdnUrls.first()] = 0L
                val sorted = linkedMapOf<String, Long>()
                result.entries.sortedBy { it.value }.forEach { sorted[it.key] = it.value }
                return sorted
            }
            if (code in 200..299 && contentLength < fileSize) {
                Log.d(TAG, "No-range serves $contentLength bytes (~${contentLength / 1024 / 1024}MB) — shard confirmed")
            }
        } catch (e: Exception) {
            Log.d(TAG, "No-range test failed: ${e.message}")
        }

        // Candidate shard offsets: 0, 66MB, 132MB, 198MB, 264MB, 330MB
        // MEGA CDN limits to ~66MB per connection, so shards are ~66MB each
        val candidateOffsets = mutableListOf<Long>()
        var off = 0L
        while (off < fileSize) {
            candidateOffsets.add(off)
            off += SHARD_SIZE
        }
        Log.d(TAG, "Candidate shard offsets: ${candidateOffsets.map { "${it / 1024 / 1024}MB" }}")

        for ((index, url) in cdnUrls.withIndex()) {
            try {
                Log.d(TAG, "Shard #$index: downloading ${downloadSize / 1024 / 1024}MB from ${url.removePrefix("https://").take(40)}...")
                val conn = (URL(url).openConnection() as HttpURLConnection).apply {
                    connectTimeout = 30000; readTimeout = 120000
                    setRequestProperty("User-Agent", MEGA_UA)
                    setRequestProperty("Origin", "https://mega.nz")
                    setRequestProperty("Referer", "https://mega.nz/")
                    instanceFollowRedirects = true
                }
                val code = conn.responseCode
                if (code !in listOf(200, 206)) {
                    conn.disconnect()
                    Log.d(TAG, "Shard #$index: HTTP $code, skipping")
                    continue
                }
                val data = conn.inputStream.readBytes()
                conn.disconnect()
                Log.d(TAG, "Shard #$index: got ${data.size}B (${data.size / 1024 / 1024}MB, HTTP $code)")

                if (data.size < 32) {
                    Log.d(TAG, "Shard #$index: too small (${data.size}B), skipping")
                    continue
                }

                // Phase 1: try known candidate offsets with 512-byte sample
                var foundOffset = -1L
                for (candidate in candidateOffsets) {
                    val sampleSize = minOf(512, data.size)
                    val dec = tryDecryptAtOffset(aesKey, baseIv, candidate, data.copyOf(sampleSize))
                    if (dec != null && isMp4Signature(dec)) {
                        foundOffset = candidate
                        val boxName = String(dec, 4, minOf(4, dec.size - 4), Charsets.US_ASCII)
                        Log.d(TAG, "Shard #$index: MATCH at offset ${candidate / 1024 / 1024}MB ($candidate) — box=$boxName")
                        break
                    }
                }

                // Phase 2: fine scan — every 16 bytes within the downloaded data
                // For middle shards, the MP4 signature can be at ANY position within the 66MB
                if (foundOffset < 0) {
                    Log.d(TAG, "Shard #$index: no match at candidates, scanning every 16 bytes (max 5MB region)...")
                    val scanLimit = minOf(data.size.toLong(), 5L * 1024 * 1024)
                    var logCounter = 0
                    for (pos in 0L until scanLimit step 16) {
                        val sampleSize = minOf(512, data.size - pos.toInt())
                        if (sampleSize < 32) break
                        val dec = tryDecryptAtOffset(aesKey, baseIv, pos, data.copyOfRange(pos.toInt(), pos.toInt() + sampleSize))
                        if (dec != null && isMp4Signature(dec)) {
                            foundOffset = pos
                            val boxName = String(dec, 4, minOf(4, dec.size - 4), Charsets.US_ASCII)
                            Log.d(TAG, "Shard #$index: MATCH at offset ${pos / 1024 / 1024}MB ($pos, scan) — box=$boxName")
                            break
                        }
                        logCounter++
                        if (logCounter % 50000 == 0) {
                            Log.d(TAG, "Shard #$index: scanned ${pos / 1024}KB, no match yet...")
                        }
                    }
                }

                // Phase 3: coarse scan — every 1MB for the full 66MB (catches late signatures)
                if (foundOffset < 0) {
                    Log.d(TAG, "Shard #$index: no match in first 5MB, scanning every 1MB for full range...")
                    for (pos in 0L until data.size.toLong() step (1L * 1024 * 1024)) {
                        val sampleSize = minOf(512, data.size - pos.toInt())
                        if (sampleSize < 32) break
                        val dec = tryDecryptAtOffset(aesKey, baseIv, pos, data.copyOfRange(pos.toInt(), pos.toInt() + sampleSize))
                        if (dec != null && isMp4Signature(dec)) {
                            foundOffset = pos
                            val boxName = String(dec, 4, minOf(4, dec.size - 4), Charsets.US_ASCII)
                            Log.d(TAG, "Shard #$index: MATCH at offset ${pos / 1024 / 1024}MB ($pos, coarse) — box=$boxName")
                            break
                        }
                    }
                }

                if (foundOffset >= 0) {
                    result[url] = foundOffset
                } else {
                    Log.w(TAG, "Shard #$index: no MP4 signature found — skipping this URL")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Shard probe #$index error: ${e.message}")
            }
        }

        // Sort by offset and log
        val sorted = linkedMapOf<String, Long>()
        result.entries.sortedBy { it.value }.forEach { sorted[it.key] = it.value }

        Log.d(TAG, "Shard map: ${sorted.size}/${cdnUrls.size} URLs mapped")
        for ((url, offset) in sorted) {
            val hostname = url.removePrefix("https://").take(40)
            Log.d(TAG, "  ${hostname}... → offset=${offset / 1024 / 1024}MB ($offset)")
        }

        return sorted
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

                val result = startStreamProxy(fileInfo.fileSize, aesKey, iv, urlInfo.fileId, fileInfo.faHash, keyBytes) ?: return@withContext null
                Log.d(TAG, "Stream proxy ready: ${result.url}")

                val proxyResult = MegaProxyResult(result.url, result.port, result.stream, result.serverSocket)
                activeProxies[urlInfo.fileId] = proxyResult
                proxyResult
            } catch (e: Exception) { Log.e(TAG, "Extract fail: ${e.message}", e); null }
        }
    }
}
