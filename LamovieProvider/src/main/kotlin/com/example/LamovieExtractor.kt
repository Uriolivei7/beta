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

        val basicHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Referer" to "https://la.movie/",
            "Origin" to mainUrl,
            "Accept-Language" to "es-MX,es;q=0.9"
        )

        val response = app.get(embedUrl, headers = basicHeaders)
        val res = response.text

        val vttRegex = Regex("""["'](https?://[^"']+\.vtt[^"']*)["']""")
        vttRegex.findAll(res).forEach { match ->
            val subUrl = fixUrl(match.groupValues[1])
            val label = when {
                subUrl.contains("spa", true) || subUrl.contains("_es") -> "Español"
                subUrl.contains("eng", true) || subUrl.contains("_en") -> "English"
                else -> "Subtítulo"
            }

            Log.d("LaMovie", "LOG: SUB ENCONTRADO -> $subUrl")

            subtitleCallback.invoke(
                newSubtitleFile(label, subUrl) {
                    this.headers = basicHeaders
                }
            )
        }

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        videoUrl?.let { m3u8 ->
            val finalUrl = fixUrl(m3u8).replace("http://", "https://")

            callback.invoke(
                newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = basicHeaders + mapOf("Referer" to "$mainUrl/")
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
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Referer" to "https://la.movie/"
        )

        val res = app.get(url, headers = headers).text

        Regex("""["'](https?://[^"']+\.vtt[^"']*)["']""").findAll(res).forEach {
            subtitleCallback.invoke(newSubtitleFile("Subtítulo", fixUrl(it.groupValues[1])) {
                this.headers = headers
            })
        }

        val m3u8 = Regex("""file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""").find(res)?.groupValues?.get(1)
        m3u8?.let { link ->
            callback.invoke(
                newExtractorLink(this.name, this.name, fixUrl(link).replace("http://", "https://"), ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = headers + mapOf("Referer" to "$mainUrl/")
                }
            )
        }
    }
}