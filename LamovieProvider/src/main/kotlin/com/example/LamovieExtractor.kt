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
        Log.i("LaMovie", "LOG: Cargando Vimeos -> $embedUrl")

        val response = app.get(embedUrl, referer = "https://la.movie/")
        val res = response.text
        val cookieString = response.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

        val standardHeaders = mapOf(
            "Referer" to "$mainUrl/",
            "User-Agent" to "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36",
            "Cookie" to cookieString,
            "Accept" to "*/*",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "cross-site",
            "Sec-Fetch-Dest" to "video"
        )

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        videoUrl?.let { m3u8 ->
            val finalUrl = fixUrl(m3u8).replace("http://", "https://")
            Log.i("LaMovie", "LOG: URL Vimeos Final: $finalUrl")

            callback.invoke(
                newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = standardHeaders
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
        Log.i("LaMovie", "LOG: Iniciando Goodstream -> $url")

        val response = app.get(url, referer = "https://la.movie/")
        val res = response.text
        val cookieString = response.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

        val m3u8 = Regex("""file\s*:\s*["']([^"']+\.m3u8[^"']*)["']""").find(res)?.groupValues?.get(1)

        val standardHeaders = mapOf(
            "Referer" to "$mainUrl/",
            "User-Agent" to "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Mobile Safari/537.36",
            "Cookie" to cookieString,
            "Accept" to "*/*",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "cross-site",
            "Sec-Fetch-Dest" to "video"
        )

        m3u8?.let { link ->
            val finalUrl = fixUrl(link).replace("http://", "https://")
            Log.w("LaMovie", "LOG: Enviando enlace Goodstream con headers de video.")
            callback.invoke(
                newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = standardHeaders
                }
            )
        }
    }
}