package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import kotlinx.coroutines.withTimeoutOrNull

private const val DANI_TAG = "Danimados"

private fun unpackPackedJS(html: String): String? {
    val evalHeaderRegex = Regex("""eval\s*\(\s*function\s*\(\s*p\s*,\s*a\s*,\s*c\s*,\s*k\s*,\s*e\s*,\s*d\s*\)\s*\{[\s\S]*?\}\s*\(\s*'""")
    val evalHeader = evalHeaderRegex.find(html) ?: return null
    val argStart = evalHeader.range.last + 1

    val argsRegex = Regex("""',(\d+),(\d+),'([^']+)'\.split\('\|'\)""")
    val argsMatch = argsRegex.find(html, argStart) ?: return null

    val packedP = html.substring(argStart, argsMatch.range.first)
    val base = argsMatch.groupValues[1].toIntOrNull() ?: return null
    val count = argsMatch.groupValues[2].toIntOrNull() ?: return null
    val kRaw = argsMatch.groupValues[3]

    val k = kRaw.split("|").toTypedArray()

    val result = StringBuilder(packedP)
    for (idx in count - 1 downTo 0) {
        val key = idx.toString(base)
        val value = k.getOrElse(idx) { "" }
        if (key.isNotEmpty() && value.isNotEmpty()) {
            val pattern = Regex("\\b${Regex.escape(key)}\\b")
            val replacement = Regex.escapeReplacement(value)
            val replaced = pattern.replace(result, replacement)
            result.clear()
            result.append(replaced)
        }
    }

    return result.toString().replace("\\'", "'")
}

private suspend fun scanDaniHtmlForSubs(html: String, baseUrl: String, subtitleCallback: (SubtitleFile) -> Unit, seen: MutableSet<String> = mutableSetOf()) {
    Regex("""["']([^"']*\.(?:vtt|srt)(?:\?[^"']*)?)["']""", RegexOption.IGNORE_CASE).findAll(html).forEach { match ->
        val subUrl = match.groupValues[1]
        val cleanSubUrl = if (subUrl.startsWith("http")) subUrl else "$baseUrl/$subUrl"
        if (seen.add(cleanSubUrl)) {
            Log.d(DANI_TAG, "[PageSubs] SubtÃ­tulo: $cleanSubUrl")
            subtitleCallback.invoke(newSubtitleFile("EspaÃ±ol", cleanSubUrl))
        }
    }
}

private suspend fun tryExtractDaniSubsFromM3u8(
    m3u8Url: String,
    referer: String?,
    subtitleCallback: (SubtitleFile) -> Unit,
) {
    try {
        val subHeaders = mutableMapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
        )
        if (referer != null) subHeaders["Referer"] = referer
        val manifest = app.get(m3u8Url, headers = subHeaders, timeout = 20000L).text
        val baseUrl = m3u8Url.substringBeforeLast("/")
        var count = 0
        Regex("""#EXT-X-MEDIA:TYPE=SUBTITLES[^#]*""", RegexOption.IGNORE_CASE).findAll(manifest).forEach { mediaBlock ->
            val lang = Regex("""LANGUAGE\s*=\s*"([^"]*)""", RegexOption.IGNORE_CASE).find(mediaBlock.value)?.groupValues?.get(1) ?: "EspaÃ±ol"
            val uri = Regex("""URI\s*=\s*"([^"]*)""", RegexOption.IGNORE_CASE).find(mediaBlock.value)?.groupValues?.get(1)
            if (uri != null) {
                val subUrl = if (uri.startsWith("http")) uri else "$baseUrl/$uri"
                Log.d(DANI_TAG, "[M3u8Subs] lang=$lang, url=${subUrl.take(120)}")
                subtitleCallback.invoke(newSubtitleFile(lang, subUrl))
                count++
            }
        }
        Log.d(DANI_TAG, "[M3u8Subs] $count subs")
    } catch (e: Exception) {
        Log.d(DANI_TAG, "[M3u8Subs] Error manifest: ${e.message}")
    }
}

internal suspend fun tryVidHideProExtractDanimados(
    url: String,
    referer: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    return try {
        Log.d(DANI_TAG, "[VH-Pro] trying $url")
        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Accept" to "*/*",
            "Referer" to referer,
        )
        val res = app.get(url, headers = headers, timeout = 20000L)
        if (!res.isSuccessful) {
            Log.w(DANI_TAG, "[VH-Pro] HTTP ${res.code}")
            return false
        }
        val html = res.text
        Log.d(DANI_TAG, "[VH-Pro] HTTP 200 len=${html.length}")

        val unpacked = unpackPackedJS(html)
        if (unpacked.isNullOrBlank()) {
            Log.w(DANI_TAG, "[VH-Pro] packer unpack failed")
            return false
        }

        val linksMap = mutableMapOf<String, String>()
        Regex("""links\s*[=:]\s*\{([^}]+)\}""", RegexOption.DOT_MATCHES_ALL).find(unpacked)?.let { block ->
            val inner = block.groupValues[1]
            Regex("""(?:["'])?([A-Za-z0-9]+)(?:["'])?\s*:\s*"([^"]+)""").findAll(inner).forEach { m ->
                if (m.groupValues[1].startsWith("hls")) linksMap[m.groupValues[1]] = m.groupValues[2]
            }
        }
        if (linksMap.isEmpty()) {
            Regex(""""(hls\d)"\s*:\s*"([^"]+)""").findAll(unpacked).forEach { m ->
                linksMap[m.groupValues[1]] = m.groupValues[2]
            }
        }

        if (linksMap.isEmpty()) {
            Log.w(DANI_TAG, "[VH-Pro] no hls links in unpacked JS")
            return false
        }

        val preferOrder = listOf("hls2", "hls3", "hls4")
        val orderedKeys = preferOrder.filter { linksMap.containsKey(it) } + linksMap.keys.filter { it !in preferOrder }

        val vidHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Referer" to url,
            "Origin" to url.substringBeforeLast("/"),
        )

        data class Variant(val key: String, val url: String)
        val probeHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Referer" to url,
        )
        val resolved = orderedKeys.mapNotNull { key ->
            var u = linksMap[key] ?: return@mapNotNull null
            if (u.startsWith("//")) u = "https:$u"
            if (u.startsWith("/")) {
                u = "https://vidhidepro.com$u"
                Log.d(DANI_TAG, "[VH-Pro] relative URL, prepended base: ${u.take(120)}")
            }
            Variant(key, u)
        }
        val reachable = java.util.Collections.synchronizedSet(mutableSetOf<Variant>())
        resolved.amap { v ->
            try {
                val code = withTimeoutOrNull(10000L) {
                    app.get(v.url, headers = probeHeaders, timeout = 8000L, allowRedirects = false).code
                } ?: -1
                Log.d(DANI_TAG, "[VH-Pro] probe ${v.key} -> $code")
                if (code in 200..399) reachable.add(v)
            } catch (_: Exception) {}
        }
        val toEmit = if (reachable.isNotEmpty()) {
            orderedKeys.mapNotNull { k -> reachable.firstOrNull { it.key == k } }
        } else {
            Log.w(DANI_TAG, "[VH-Pro] ningÃºn master responde, emitiendo todos igual")
            resolved
        }

        var firstM3u8: String? = null
        for (v in toEmit) {
            if (firstM3u8 == null) firstM3u8 = v.url
            Log.d(DANI_TAG, "[VH-Pro] emit ${v.key} url=${v.url.take(120)}")
            callback(newExtractorLink("Danimados", "Danimados - VidHidePro - ${v.key}", v.url, ExtractorLinkType.M3U8) {
                this.referer = url
                this.headers = vidHeaders
            })
        }
        Log.d(DANI_TAG, "[VH-Pro] emitted ${toEmit.size} variants")

        val seenSubs = mutableSetOf<String>()
        scanDaniHtmlForSubs(html, url.substringBeforeLast("/"), subtitleCallback, seenSubs)
        scanDaniHtmlForSubs(unpacked, url.substringBeforeLast("/"), subtitleCallback, seenSubs)
        firstM3u8?.let { tryExtractDaniSubsFromM3u8(it, url, subtitleCallback) }
        true
    } catch (e: Exception) {
        Log.e(DANI_TAG, "[VH-Pro] error: ${e.message}")
        false
    }
}

// ================================ VOE ================================

data class DamiVoeDecrypted(
    @JsonProperty("source") val source: String? = null,
    @JsonProperty("direct_access_url") val directAccessUrl: String? = null,
)

private fun decryptVoeF7(p8: String, quiet: Boolean = false): DamiVoeDecrypted? {
    return try {
        val vF = rot13(p8)
        val vF2 = replacePatterns(vF)
        val vF3 = removeUnderscores(vF2)
        val vF4 = base64Decode(vF3)
        val vF5 = charShift(vF4, 3)
        val vF6 = vF5.reversed()
        val vAtob = base64Decode(vF6)
        parseJson<DamiVoeDecrypted>(vAtob)
    } catch (e: Exception) {
        if (!quiet) Log.e(DANI_TAG, "[Voe] decrypt error: ${e.message}")
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

private fun base64Decode(input: String): String {
    return String(android.util.Base64.decode(input, android.util.Base64.DEFAULT))
}

private suspend fun parseVoeHtml(
    html: String,
    pageUrl: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit,
): Boolean {
    if (html.contains("altcha-widget") || html.contains("Confirm you&#039;re human")) {
        Log.w(DANI_TAG, "[Voe] CAPTCHA page, no parse: ${pageUrl.take(100)}")
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
        Log.w(DANI_TAG, "[Voe] encoded string not found: ${pageUrl.take(100)}")
        return false
    }

    val decryptedJson = decryptVoeF7(encodedString)
    val m3u8 = decryptedJson?.source
    val mp4 = decryptedJson?.directAccessUrl
    var emitted = false
    if (m3u8 != null) {
        Log.d(DANI_TAG, "[Voe] Found M3U8: ${m3u8.take(100)}")
        M3u8Helper.generateM3u8(
            "Danimados",
            m3u8,
            "$pageOrigin/",
            headers = mapOf("Origin" to "$pageOrigin/"),
        ).forEach(callback)
        emitted = true
    }
    if (mp4 != null) {
        Log.d(DANI_TAG, "[Voe] Found MP4: ${mp4.take(100)}")
        callback.invoke(
            newExtractorLink("Danimados MP4", "Danimados MP4", mp4, INFER_TYPE) {
                this.referer = pageUrl
                this.quality = Qualities.Unknown.value
            }
        )
        emitted = true
    }
    if (!emitted) Log.w(DANI_TAG, "[Voe] No source found after decryption")
    return emitted
}

internal suspend fun tryVoeExtractDanimados(
    url: String,
    referer: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    return try {
        Log.d(DANI_TAG, "[Voe] trying $url")
        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Referer" to referer,
        )
        suspend fun tryMirrors(): Boolean {
            val hashPath = try { java.net.URL(url).path } catch (_: Exception) { "" }
            if (!hashPath.startsWith("/e/")) return false

            val mirrors = listOf("yip.su", "tubelessceliolymph.com", "eugenemakedraw.com")
            for (mirror in mirrors) {
                try {
                    val mUrl = "https://$mirror$hashPath"
                    if (mUrl == url) continue
                    Log.d(DANI_TAG, "[Voe] probando mirror: $mUrl")
                    val mHtml = app.get(mUrl, headers = headers + ("Referer" to url), timeout = 10000L).text
                    if (parseVoeHtml(mHtml, mUrl, subtitleCallback, callback)) {
                        Log.d(DANI_TAG, "[Voe] mirror $mirror OK")
                        return true
                    }
                } catch (_: Exception) {}
            }
            return false
        }
        val res = app.get(url, headers = headers, timeout = 15000L, allowRedirects = false)
        val redirectUrl = res.headers["Location"] ?: res.url
        Log.d(DANI_TAG, "[Voe] status=${res.code} redirect=$redirectUrl")

        val finalUrl = if (res.code in 301..399) {
            val h2 = headers + ("Referer" to url)
            try { app.get(redirectUrl, headers = h2, timeout = 15000L).url } catch (_: Exception) { redirectUrl }
        } else {
            redirectUrl
        }
        Log.d(DANI_TAG, "[Voe] finalUrl=$finalUrl")

        val finalHtml = app.get(finalUrl, headers = headers, timeout = 15000L).text

        if (finalHtml.contains("captcha") || finalHtml.contains("CAPTCHA") || finalHtml.contains("cf-challenge")) {
            Log.w(DANI_TAG, "[Voe] CAPTCHA detected at $finalUrl")
            return tryMirrors()
        }

        if (parseVoeHtml(finalHtml, finalUrl, subtitleCallback, callback)) return true
        return tryMirrors()
    } catch (e: Exception) {
        Log.e(DANI_TAG, "[Voe] error: ${e.message}")
        false
    }
}

// ================================ Byse ================================

internal suspend fun tryByseHttpDanimados(
    embedUrl: String,
    parent: String,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit
): Boolean {
    return try {
        val site = runCatching { "https://${java.net.URI(parent).host}" }.getOrDefault("https://danimados.cc")
        Log.d(DANI_TAG, "[Byse] flujo HTTP (site=$site) $embedUrl")
        val sources = ByseHttpExtractor().extract(embedUrl, parent, site)
        var found = false
        for (s in sources) {
            Log.d(DANI_TAG, "[Byse] source: ${s.url.take(140)} label=${s.label}")
            for (sub in s.subtitles) subtitleCallback(sub)
            val type = if (s.url.contains(".m3u8")) ExtractorLinkType.M3U8 else INFER_TYPE
            callback(
                newExtractorLink("Danimados", "Danimados - Byse${s.label?.let { " - $it" } ?: ""}", s.url, type) {
                    this.referer = site
                    this.headers = mapOf("Origin" to site)
                }
            )
            found = true
        }
        if (found) Log.d(DANI_TAG, "[Byse] done vÃ­a HTTP")
        found
    } catch (e: Exception) {
        Log.w(DANI_TAG, "[Byse] HTTP error: ${e.message}")
        false
    }
}

