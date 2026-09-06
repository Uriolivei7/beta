package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

private const val MONOS_TAG = "MonosChinos"
private const val MONOS_SOURCE = "MonosChinos"

class MonosLuluvdo : ExtractorApi() {
    override val name = "MonosLuluvdo"
    override val mainUrl = "https://luluvdo.com"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d(MONOS_TAG, "[Lulu] URL: $url")
        try {
            val resp = app.get(url, headers = mapOf(
                "User-Agent" to USER_AGENT,
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Referer" to (referer ?: url),
            ), timeout = 20000L)
            Log.d(MONOS_TAG, "[Lulu] HTTP ${resp.code} len=${resp.text.length}")
            parseHtml(resp.text, MONOS_SOURCE, callback)
        } catch (e: Exception) {
            Log.e(MONOS_TAG, "[Lulu] Error: ${e.message}", e)
        }
    }

    suspend fun parseHtml(
        html: String,
        sourceName: String,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val packed = Regex("""\}\('(.*?)',(\d+),(\d+),'(.*?)'\.split\('\|'\)""", RegexOption.DOT_MATCHES_ALL)
            .find(html)
        if (packed == null) {
            Log.w(MONOS_TAG, "[Lulu] no packed JS found")
            return false
        }
        val decoded = unpackDeanEdwards(
            packed.groupValues[1],
            packed.groupValues[2].toIntOrNull() ?: 36,
            packed.groupValues[3].toIntOrNull() ?: 0,
            packed.groupValues[4]
        ) ?: return false
        Log.d(MONOS_TAG, "[Lulu] decoded len=${decoded.length}")
        val m3u8Regex = Regex("""(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""")
        var found = false
        for (m in m3u8Regex.findAll(decoded)) {
            Log.d(MONOS_TAG, "[Lulu] M3U8: ${m.value.take(120)}")
            callback.invoke(
                newExtractorLink(sourceName, "$sourceName - LuluVdo", m.value, ExtractorLinkType.M3U8) {
                    this.referer = mainUrl
                    this.headers = mapOf("Origin" to mainUrl)
                }
            )
            found = true
        }
        if (!found) {
            val fileRegex = Regex("""(?:file|src)\s*:\s*["']((?:https?:)?//[^"']+)["']""")
            for (m in fileRegex.findAll(decoded)) {
                val f = m.groupValues[1].replace("\\/", "/").trim()
                val full = if (f.startsWith("//")) "https:$f" else f
                if (full.contains(".m3u8")) {
                    Log.d(MONOS_TAG, "[Lulu] file: ${full.take(120)}")
                    callback.invoke(newExtractorLink(sourceName, "$sourceName - LuluVdo", full, ExtractorLinkType.M3U8) {
                        this.referer = mainUrl
                        this.headers = mapOf("Origin" to mainUrl)
                    })
                    found = true
                }
            }
        }
        if (!found) Log.w(MONOS_TAG, "[Lulu] no m3u8 after unpack, snippet=${decoded.take(300)}")
        return found
    }
}

class MonosMixdrop : ExtractorApi() {
    override val name = "MonosMixdrop"
    override val mainUrl = "https://mixdrop.top"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d(MONOS_TAG, "[Mix] URL: $url")
        try {
            val resp = app.get(url, headers = mapOf(
                "User-Agent" to USER_AGENT,
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Referer" to (referer ?: url),
            ), timeout = 20000L)
            Log.d(MONOS_TAG, "[Mix] HTTP ${resp.code} len=${resp.text.length}")
            parseHtml(resp.text, MONOS_SOURCE, callback)
        } catch (e: Exception) {
            Log.e(MONOS_TAG, "[Mix] Error: ${e.message}", e)
        }
    }

    suspend fun parseHtml(
        html: String,
        sourceName: String,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val packerRegex = Regex("""\}\('(.*?)',(\d+),(\d+),'(.*?)'\.split\('\|'\)""", RegexOption.DOT_MATCHES_ALL)
        var found = false
        for (pm in packerRegex.findAll(html)) {
            try {
                val decoded = unpackDeanEdwards(
                    pm.groupValues[1],
                    pm.groupValues[2].toIntOrNull() ?: 36,
                    pm.groupValues[3].toIntOrNull() ?: 0,
                    pm.groupValues[4]
                ) ?: continue
                Log.d(MONOS_TAG, "[Mix] decoded len=${decoded.length}")
                val wurl = Regex("""MDCore\.wurl\s*=\s*"([^"]*)""").find(decoded)?.groupValues?.get(1)
                if (!wurl.isNullOrBlank()) {
                    var f = wurl.trim()
                    if (f.startsWith("//")) f = "https:$f"
                    else if (!f.startsWith("http")) f = "https:$f"
                    Log.d(MONOS_TAG, "[Mix] wurl: ${f.take(120)}")
                    callback.invoke(
                        newExtractorLink(sourceName, "$sourceName - Mixdrop", f, INFER_TYPE) {
                            this.referer = mainUrl
                            this.headers = mapOf("Origin" to mainUrl)
                        }
                    )
                    found = true
                }
            } catch (_: Exception) {}
            if (found) break
        }
        if (!found) Log.w(MONOS_TAG, "[Mix] no wurl found, hasPacker=${html.contains("function(p,a,c,k")}")
        return found
    }
}

class MonosVoe : ExtractorApi() {
    override val name = "MonosVoe"
    override val mainUrl = "https://voe.sx"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        Log.d(MONOS_TAG, "[Voe] URL: $url")
        val voeHeaders = mapOf(
            "User-Agent" to USER_AGENT,
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
            Log.d(MONOS_TAG, "[Voe] Redirect to: $redirectUrl")
            currentReferer = currentUrl
            currentUrl = redirectUrl
            res = app.get(currentUrl, headers = voeHeaders, referer = currentReferer)
            maxRedirects--
            redirectUrl = redirectRegex.find(res.text)?.groupValues?.get(1)
        }
        if (maxRedirects == 0 && redirectUrl != null) {
            Log.e(MONOS_TAG, "[Voe] Too many redirects, giving up")
        }
        parseHtml(res.text, currentUrl, MONOS_SOURCE, subtitleCallback, callback)
    }

    suspend fun parseHtml(
        html: String,
        pageUrl: String,
        sourceName: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        if (html.contains("altcha-widget") || html.contains("Confirm you&#039;re human")) {
            Log.w(MONOS_TAG, "[Voe] CAPTCHA page, no parse: ${pageUrl.take(100)}")
            return false
        }
        val pageOrigin = try {
            val u = java.net.URL(pageUrl)
            "${u.protocol}://${u.host}"
        } catch (_: Exception) { pageUrl }

        var encodedString: String? = Regex(
            """<script[^>]*type=["']application/json["'][^>]*>(.*?)</script>""",
            RegexOption.DOT_MATCHES_ALL
        ).find(html)?.groupValues?.get(1)?.trim()
            ?.takeIf { it.contains("[\"") }
            ?.substringAfter("[\"")
            ?.substringBeforeLast("\"]")

        if (encodedString == null) {
            val scripts = Regex("""<script[^>]*>(.*?)</script>""", RegexOption.DOT_MATCHES_ALL)
                .findAll(html).map { it.groupValues[1] }
            outer@ for (body in scripts) {
                for (m in Regex("""["']([A-Za-z0-9+/=@\^~\*\?&#!%$]{100,})["']""").findAll(body)) {
                    val cand = m.groupValues[1]
                    val test = decryptVoeF7(cand, quiet = true)
                    if (test?.source != null || test?.directAccessUrl != null) {
                        encodedString = cand
                        break@outer
                    }
                }
            }
        }

        if (encodedString == null) {
            Log.w(MONOS_TAG, "[Voe] encoded string not found: ${pageUrl.take(100)}")
            return false
        }

        val decryptedJson = decryptVoeF7(encodedString)
        val m3u8 = decryptedJson?.source
        val mp4 = decryptedJson?.directAccessUrl
        var emitted = false
        if (m3u8 != null) {
            Log.d(MONOS_TAG, "[Voe] Found M3U8: ${m3u8.take(100)}")
            M3u8Helper.generateM3u8(
                sourceName,
                m3u8,
                "$pageOrigin/",
                headers = mapOf("Origin" to "$pageOrigin/"),
            ).forEach(callback)
            emitted = true
        }
        if (mp4 != null) {
            Log.d(MONOS_TAG, "[Voe] Found MP4: ${mp4.take(100)}")
            callback.invoke(
                newExtractorLink("$sourceName MP4", "$sourceName MP4", mp4, INFER_TYPE) {
                    this.referer = pageUrl
                    this.quality = Qualities.Unknown.value
                }
            )
            emitted = true
        }
        if (!emitted) Log.w(MONOS_TAG, "[Voe] No source found after decryption")
        return emitted
    }

    private fun decryptVoeF7(p8: String, quiet: Boolean = false): MonosVoeDecrypted? {
        return try {
            val vF = rot13(p8)
            val vF2 = replacePatterns(vF)
            val vF3 = removeUnderscores(vF2)
            val vF4 = base64Decode(vF3)
            val vF5 = charShift(vF4, 3)
            val vF6 = vF5.reversed()
            val vAtob = base64Decode(vF6)
            parseJson<MonosVoeDecrypted>(vAtob)
        } catch (e: Exception) {
            if (!quiet) Log.e(MONOS_TAG, "[Voe] decrypt error: ${e.message}")
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

data class MonosVoeDecrypted(
    @JsonProperty("source") val source: String? = null,
    @JsonProperty("direct_access_url") val directAccessUrl: String? = null,
)

fun unpackDeanEdwards(packed: String, base: Int, count: Int, dictRaw: String): String? {
    return try {
        val k = dictRaw.split("|").toTypedArray()
        val result = StringBuilder(packed)
        for (idx in count - 1 downTo 0) {
            val key = idx.toString(base)
            val value = k.getOrElse(idx) { "" }
            if (key.isNotEmpty() && value.isNotEmpty()) {
                val replaced = Regex("\\b${Regex.escape(key)}\\b").replace(result, Regex.escapeReplacement(value))
                result.clear()
                result.append(replaced)
            }
        }
        result.toString().replace("\\'", "'")
    } catch (_: Exception) { null }
}