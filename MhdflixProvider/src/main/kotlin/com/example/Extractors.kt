package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.extractors.VidStack
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.M3u8Helper.Companion.generateM3u8
import okhttp3.FormBody
import java.net.URL
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class ByseExtractor : ExtractorApi() {
    override var mainUrl = "https://bysedikamoum.com"
    override var name = "Byse"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("Byse", "[Byse] URL: $url")

        val videoId = Regex("""/e/(\w+)""").find(url)?.groupValues?.get(1) ?: return
        val baseUrl = try {
            val parsed = java.net.URL(url)
            "${parsed.protocol}://${parsed.host}"
        } catch (e: Exception) { mainUrl }

        try {
            val apiRes = app.post(
                "$baseUrl/api/source",
                headers = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                    "Referer" to url,
                    "Origin" to baseUrl,
                ),
                referer = url,
                requestBody = FormBody.Builder()
                    .add("r", "")
                    .add("s", videoId)
                    .build()
            )
            Log.d("Byse", "[Byse] API response: ${apiRes.text.take(300)}")

            val fileUrl = Regex("""file["']\s*:\s*["']([^"']+)["']""")
                .find(apiRes.text)?.groupValues?.get(1)
                ?: Regex("""url["']\s*:\s*["']([^"']+)["']""")
                .find(apiRes.text)?.groupValues?.get(1)
                ?: return

            callback.invoke(
                newExtractorLink(name, name, fileUrl) {
                    this.referer = baseUrl
                    this.quality = Qualities.Unknown.value
                    this.type = if (fileUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                }
            )
        } catch (e: Exception) {
            Log.e("Byse", "[Byse] Error: ${e.message}")
        }
    }
}

class MhdflixVidHide : ExtractorApi() {
    override var name = "MhdflixVidHide"
    override var mainUrl = "https://minochinos.com"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        val html = app.get(url, headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
            "Referer" to "https://minochinos.com/",
            "Accept-Language" to "es"
        )).text

        Regex("""(https?://[^"']+\.m3u8[^"']*)""").findAll(html).forEach {
            callback.invoke(newExtractorLink(name, "VidHide", it.value, ExtractorLinkType.M3U8) {
                this.referer = mainUrl
            })
        }
    }
}

class MhdflixCubeembed : ExtractorApi() {
    override var name = "Cubeembed"
    override var mainUrl = "https://cubeembed.rpmvid.com"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val hash = url.substringAfterLast("#").substringAfter("/")
        val baseUrl = getBaseUrl(url)
        Log.d("MhdflixCubeembed", "hash=$hash baseUrl=$baseUrl")

        val headers = mapOf("User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0")
        val encrypted = app.get("$baseUrl/api/v1/video?id=$hash", headers = headers).text.trim()
        Log.d("MhdflixCubeembed", "API response length=${encrypted.length}")

        val key = "kiemtienmua911ca"
        val ivList = listOf("1234567890oiuytr", "0123456789abcdef")

        val decryptedText = ivList.firstNotNullOfOrNull { iv ->
            try {
                decryptAES(encrypted, key, iv)
            } catch (e: Exception) {
                Log.w("MhdflixCubeembed", "Decrypt failed with IV=$iv: ${e.message}")
                null
            }
        } ?: throw Exception("Failed to decrypt with all IVs")

        Log.d("MhdflixCubeembed", "Decrypted: $decryptedText")

        val source = Regex("\"source\":\"(.*?)\"").find(decryptedText)
            ?.groupValues?.get(1)
            ?.replace("\\/", "/") ?: ""
        val cfUrl = Regex("\"cf\":\"(.*?)\"").find(decryptedText)
            ?.groupValues?.get(1)
            ?.replace("\\/", "/") ?: ""

        val subtitleSection = Regex("\"subtitle\":\\{(.*?)\\}").find(decryptedText)?.groupValues?.get(1)
        subtitleSection?.let { section ->
            Regex("\"([^\"]+)\":\\s*\"([^\"]+)\"").findAll(section).forEach { match ->
                val lang = match.groupValues[1]
                val rawPath = match.groupValues[2].split("#")[0]
                if (rawPath.isNotEmpty()) {
                    val path = rawPath.replace("\\/", "/")
                    val subUrl = "$baseUrl$path"
                    subtitleCallback(newSubtitleFile(lang, fixUrl(subUrl)))
                }
            }
        }

        if (cfUrl.isNotEmpty()) {
            Log.d("MhdflixCubeembed", "CF M3U8: $cfUrl")
            callback.invoke(
                newExtractorLink(this.name, "$name (CF)", cfUrl, ExtractorLinkType.M3U8) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
        }
        if (source.isNotEmpty()) {
            val sourceHttp = source.replaceFirst("https://", "http://")
            Log.d("MhdflixCubeembed", "Source M3U8 (HTTP): $sourceHttp")
            callback.invoke(
                newExtractorLink(this.name, "$name (Source)", sourceHttp, ExtractorLinkType.M3U8) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
        }
        val hlsTiktok = Regex("\"hlsVideoTiktok\":\"(.*?)\"").find(decryptedText)
            ?.groupValues?.get(1)
            ?.replace("\\/", "/") ?: ""
        if (hlsTiktok.isNotEmpty()) {
            val tiktokUrl = "$baseUrl$hlsTiktok"
            Log.d("MhdflixCubeembed", "TikTok M3U8: $tiktokUrl")
            callback.invoke(
                newExtractorLink(this.name, "$name (TikTok)", tiktokUrl, ExtractorLinkType.M3U8) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
        }
    }

    private fun getBaseUrl(url: String): String {
        return try {
            val parsed = URL(url)
            "${parsed.protocol}://${parsed.host}"
        } catch (e: Exception) {
            Log.e("MhdflixCubeembed", "getBaseUrl fallback: ${e.message}")
            mainUrl
        }
    }

    private fun decryptAES(inputHex: String, key: String, iv: String): String {
        val keySpec = SecretKeySpec(key.encodeToByteArray(), "AES")
        val ivSpec = IvParameterSpec(iv.encodeToByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decrypted = cipher.doFinal(hexToBytes(inputHex))
        return String(decrypted, Charsets.UTF_8)
    }

    private fun hexToBytes(hex: String): ByteArray {
        check(hex.length % 2 == 0) { "Hex string must have an even length" }
        return hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }
}

class Sendvid : ExtractorApi() {
    override var name = "Sendvid"
    override val mainUrl = "https://sendvid.com"
    override val requiresReferer = false
    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val doc = app.get(url).document
        val urlString = doc.select("head meta[property=og:video:secure_url]").attr("content")
        if (urlString.contains("m3u8")) {
            generateM3u8(
                name,
                urlString,
                mainUrl,
            ).forEach(callback)
        } else {
            callback.invoke(
                newExtractorLink(
                    source = this.name,
                    name = this.name,
                    url = urlString,
                    type = INFER_TYPE
                ) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
        }
    }
}
