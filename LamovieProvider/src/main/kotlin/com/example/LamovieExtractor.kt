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

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language" to "es-MX,es;q=0.9,en;q=0.8",
            "Referer" to "https://la.movie/",
            "Cache-Control" to "max-age=0",
            "Sec-Fetch-Dest" to "document",
            "Sec-Fetch-Mode" to "navigate",
            "Sec-Fetch-Site" to "cross-site"
        )

        val response = app.get(embedUrl, headers = headers)
        val res = response.text

        Regex("""["'](https?://[^"']+\.vtt[^"']*)["']""").findAll(res).forEach { match ->
            val subUrl = fixUrl(match.groupValues[1])
            val label = when {
                subUrl.contains("spa", true) || subUrl.contains("_es") -> "Español"
                subUrl.contains("eng", true) || subUrl.contains("_en") -> "English"
                else -> "Subtítulo"
            }
            Log.d("LaMovie", "LOG: SUB VIMEOS -> $subUrl")
            subtitleCallback.invoke(newSubtitleFile(label, subUrl) { this.headers = headers })
        }

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        videoUrl?.let { m3u8 ->
            callback.invoke(
                newExtractorLink(this.name, this.name, fixUrl(m3u8).replace("http://", "https://"), ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = headers + ("Referer" to "$mainUrl/")
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
        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language" to "es-MX,es;q=0.9,en;q=0.8",
            "Referer" to "https://la.movie/",
            "Sec-Fetch-Dest" to "document",
            "Sec-Fetch-Mode" to "navigate",
            "Sec-Fetch-Site" to "cross-site"
        )

        val res = app.get(url, headers = headers).text

        Regex("""["'](https?://[^"']+\.vtt[^"']*)["']""").findAll(res).forEach { match ->
            val subUrl = fixUrl(match.groupValues[1])

            val label = when {
                subUrl.contains("_spa", ignoreCase = true) -> "Español"
                subUrl.contains("_sli", ignoreCase = true) -> "Español (Latino)"
                subUrl.contains("_eng", ignoreCase = true) || subUrl.contains("_en", ignoreCase = true) -> "English"
                else -> "Subtítulo"
            }

            Log.d("LaMovie", "LOG: SUB GOODSTREAM -> $label: $subUrl")

            subtitleCallback.invoke(
                newSubtitleFile(lang = label, url = subUrl) {
                    this.headers = headers + ("Referer" to url)
                }
            )
        }

        val m3u8 = Regex("""file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""").find(res)?.groupValues?.get(1)
        m3u8?.let { link ->
            val videoUrl = fixUrl(link).replace("http://", "https://")

            callback.invoke(
                newExtractorLink(this.name, this.name, videoUrl, ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = headers + ("Referer" to "$url/")
                }
            )
        }
    }
}