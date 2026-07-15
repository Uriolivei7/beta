package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import okhttp3.FormBody

open class VoeExtractor : ExtractorApi() {
    override val name = "Voe"
    override val mainUrl = "https://voe.sx"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d("SoloLatino", "[Voe] URL: $url")
        val voeHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Accept-Language" to "en-US,en;q=0.9",
        )
        val redirectRegex = Regex("""(?:window\.)?location(?:\.href)?\s*=\s*'([^']+)'""")
        var currentUrl = url
        var currentReferer = referer ?: url
        var res = app.get(currentUrl, headers = voeHeaders, referer = currentReferer)
        var maxRedirects = 5
        var redirectUrl = redirectRegex.find(res.text)?.groupValues?.get(1)
        while (redirectUrl != null && maxRedirects > 0) {
            Log.d("SoloLatino", "[Voe] Redirect to: $redirectUrl")
            currentReferer = currentUrl
            currentUrl = redirectUrl
            res = app.get(currentUrl, headers = voeHeaders, referer = currentReferer)
            maxRedirects--
            redirectUrl = redirectRegex.find(res.text)?.groupValues?.get(1)
        }

        if (maxRedirects == 0 && redirectUrl != null) {
            Log.e("SoloLatino", "[Voe] Too many redirects, giving up")
        }

        // Debug: log HTML snippet if encoded string not found
        val pageText = res.text
        val htmlSnippet = pageText.take(2000)

        var encodedString: String? = null

        // Method 1: script[type=application/json]
        encodedString = res.document.selectFirst("script[type=application/json]")
            ?.data()?.trim()
            ?.substringAfter("[\"")
            ?.substringBeforeLast("\"]")

        // Method 2: search for the encoded blob in any script by decryptF7 pattern
        if (encodedString == null) {
            encodedString = res.document.select("script").mapNotNull { script ->
                val html = script.html()
                Regex("""["']([A-Za-z0-9+/=]{100,})["']""").find(html)?.groupValues?.get(1)
            }.firstOrNull()
        }

        if (encodedString == null) {
            Log.e("SoloLatino", "[Voe] encoded string not found after ${5 - maxRedirects} redirect(s)")
            Log.w("SoloLatino", "[Voe] HTML snippet (first 2000 chars): $htmlSnippet")
            return
        }

        val decryptedJson = decryptVoeF7(encodedString)
        val m3u8 = decryptedJson?.source
        val mp4 = decryptedJson?.directAccessUrl
        if (m3u8 != null) {
            Log.d("SoloLatino", "[Voe] Found M3U8: ${m3u8.take(100)}")
            M3u8Helper.generateM3u8(
                name,
                m3u8,
                "$mainUrl/",
                headers = mapOf("Origin" to "$mainUrl/"),
            ).forEach(callback)
        }
        if (mp4 != null) {
            Log.d("SoloLatino", "[Voe] Found MP4: ${mp4.take(100)}")
            callback.invoke(
                newExtractorLink("$name MP4", "$name MP4", mp4, INFER_TYPE) {
                    this.referer = url
                    this.quality = Qualities.Unknown.value
                }
            )
        }
        if (m3u8 == null && mp4 == null) {
            Log.e("SoloLatino", "[Voe] No source found after decryption")
        }
    }

    private fun decryptVoeF7(p8: String): VoeDecrypted? {
        return try {
            val vF = rot13(p8)
            val vF2 = replacePatterns(vF)
            val vF3 = removeUnderscores(vF2)
            val vF4 = base64Decode(vF3)
            val vF5 = charShift(vF4, 3)
            val vF6 = vF5.reversed()
            val vAtob = base64Decode(vF6)
            parseJson<VoeDecrypted>(vAtob)
        } catch (e: Exception) {
            Log.e("SoloLatino", "[Voe] decrypt error: ${e.message}")
            null
        }
    }

    private fun rot13(input: String): String {
        return input.map { c ->
            when (c) {
                in 'A'..'Z' -> ((c - 'A' + 13) % 26 + 'A'.code).toChar()
                in 'a'..'z' -> ((c - 'a' + 13) % 26 + 'a'.code).toChar()
                else -> c
            }
        }.joinToString("")
    }

    private fun replacePatterns(input: String): String {
        val patterns = listOf("@\$", "^^", "~@", "%?", "*~", "!!", "#&")
        return patterns.fold(input) { result, pattern ->
            result.replace(Regex(Regex.escape(pattern)), "_")
        }
    }

    private fun removeUnderscores(input: String): String = input.replace("_", "")

    private fun charShift(input: String, shift: Int): String {
        return input.map { (it.code - shift).toChar() }.joinToString("")
    }
}

class VoeYipsu : VoeExtractor() {
    override val mainUrl = "https://yip.su"
}

class VoeDonaldlineelse : VoeExtractor() {
    override val mainUrl = "https://donaldlineelse.com"
}

class VoeCharlestoughrace : VoeExtractor() {
    override val mainUrl = "https://charlestoughrace.com"
}

class VoeTubeless : VoeExtractor() {
    override val mainUrl = "https://tubelessceliolymph.com"
}

class VoeSimplum : VoeExtractor() {
    override val mainUrl = "https://simpulumlamerop.com"
}

class VoeUroch : VoeExtractor() {
    override val mainUrl = "https://urochsunloath.com"
}

class VoeNathan : VoeExtractor() {
    override val mainUrl = "https://nathanfromsubject.com"
}

class VoeMetagnath : VoeExtractor() {
    override val mainUrl = "https://metagnathtuggers.com"
}

class VoePamelachangemission : VoeExtractor() {
    override val mainUrl = "https://pamelachangemission.com"
}

data class VoeDecrypted(
    @JsonProperty("source") val source: String? = null,
    @JsonProperty("direct_access_url") val directAccessUrl: String? = null,
)

class ByseExtractor : ExtractorApi() {
    override var mainUrl = "https://bysedikamoum.com"
    override var name = "Byse"
    override val requiresReferer = false

    override suspend fun getUrl(url: String, referer: String?, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        Log.d("SoloLatino", "[Byse] URL: $url")

        val videoId = Regex("""/e/(\w+)""").find(url)?.groupValues?.get(1) ?: return

        try {
            val apiRes = app.post(
                "$mainUrl/api/source",
                headers = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                    "Referer" to url,
                    "Origin" to mainUrl,
                ),
                referer = url,
                requestBody = FormBody.Builder()
                    .add("r", "")
                    .add("s", videoId)
                    .build()
            )
            Log.d("SoloLatino", "[Byse] API response: ${apiRes.text}")

            val fileUrl = Regex("""file["']\s*:\s*["']([^"']+)["']""")
                .find(apiRes.text)?.groupValues?.get(1)
                ?: Regex("""url["']\s*:\s*["']([^"']+)["']""")
                .find(apiRes.text)?.groupValues?.get(1)
                ?: return

            callback.invoke(
                newExtractorLink(name, name, fileUrl) {
                    this.referer = mainUrl
                    this.quality = Qualities.Unknown.value
                    this.type = if (fileUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                }
            )
        } catch (e: Exception) {
            Log.e("SoloLatino", "[Byse] Error: ${e.message}")
        }
    }
}