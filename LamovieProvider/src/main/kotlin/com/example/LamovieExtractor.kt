package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class Vimeos : ExtractorApi() {
    override val name = "Vimeos"
    override val mainUrl = "https://vimeos.net"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val embedUrl = if (!url.contains("/embed-")) "$mainUrl/embed-${url.substringAfterLast("/")}" else url
        val response = app.get(embedUrl, referer = "https://la.movie/")
        val res = response.text
        val cookieString = response.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

        val pcHeaders = mutableMapOf(
            "Accept" to "*/*",
            "Accept-Language" to "es-ES,es;q=0.7",
            "Connection" to "keep-alive",
            "Origin" to mainUrl,
            "Referer" to "$mainUrl/",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Cookie" to cookieString
        )

        // --- EXTRACCIÓN DE SUBTÍTULOS ---
        // Buscamos líneas tipo: tracks: [{file: "...", label: "Spanish", kind: "captions"}]
        val tracksRaw = Regex("""tracks:\s*(\[[\s\S]*?\])""").find(res)?.groupValues?.get(1)
        if (tracksRaw != null) {
            val subRegex = Regex("""file:\s*["']([^"']+)["']\s*,\s*label:\s*["']([^"']+)["']""")
            subRegex.findAll(tracksRaw).forEach { match ->
                val subUrl = fixUrl(match.groupValues[1])
                val subLabel = match.groupValues[2]
                Log.d("LaMovie", "Subtítulo encontrado: $subLabel -> $subUrl")
                subtitleCallback.invoke(SubtitleFile(subLabel, subUrl))
            }
        }

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""file:\s*["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        videoUrl?.let { m3u8 ->
            val finalUrl = fixUrl(m3u8).replace("http://", "https://")
            pcHeaders["Sec-Fetch-Site"] = if (finalUrl.contains("vimeos.net")) "same-site" else "cross-site"

            callback.invoke(
                newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = pcHeaders
                }
            )
        }
    }
}

class GoodstreamExtractor : ExtractorApi() {
    override var name = "Goodstream"
    override val mainUrl = "https://goodstream.one"
    override val requiresReferer = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url, referer = "https://la.movie/")
        val res = response.text
        val cookieString = response.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

        val pcHeaders = mapOf(
            "Accept" to "*/*",
            "Origin" to mainUrl,
            "Referer" to "$mainUrl/",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Cookie" to cookieString
        )

        // --- EXTRACCIÓN DE SUBTÍTULOS PARA GOODSTREAM ---
        val tracksRaw = Regex("""tracks:\s*(\[[\s\S]*?\])""").find(res)?.groupValues?.get(1)
        tracksRaw?.let { raw ->
            Regex("""file:\s*["']([^"']+)["']\s*,\s*label:\s*["']([^"']+)["']""").findAll(raw).forEach { match ->
                subtitleCallback.invoke(SubtitleFile(match.groupValues[2], fixUrl(match.groupValues[1])))
            }
        }

        val m3u8 = Regex("""file\s*:\s*["']([^"']+\.m3u8[^"']*)["']""").find(res)?.groupValues?.get(1)

        m3u8?.let { link ->
            callback.invoke(
                newExtractorLink(this.name, this.name, fixUrl(link).replace("http://", "https://"), ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = pcHeaders
                }
            )
        }
    }
}