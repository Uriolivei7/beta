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
        Log.i("LaMovie", "LOG: Cargando Vimeos con Headers de PC -> $embedUrl")

        // 1. Realizamos la petición inicial para obtener el JS y las Cookies necesarias
        val response = app.get(embedUrl, referer = "https://la.movie/")
        val res = response.text

        // 2. Extraemos las cookies de la respuesta para pasarlas al reproductor
        val cookieString = response.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

        // 3. Headers basados en tus curls exitosos
        val pcHeaders = mutableMapOf(
            "Accept" to "*/*",
            "Accept-Language" to "es-ES,es;q=0.7",
            "Connection" to "keep-alive",
            "Origin" to mainUrl,
            "Referer" to "$mainUrl/",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-GPC" to "1",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "sec-ch-ua" to "\"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Brave\";v=\"146\"",
            "sec-ch-ua-mobile" to "?0",
            "sec-ch-ua-platform" to "\"Windows\"",
            "Cookie" to cookieString // AGREGADO: Sin esto el servidor s14/zip da 403
        )

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""file:\s*["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        videoUrl?.let { m3u8 ->
            val finalUrl = fixUrl(m3u8).replace("http://", "https://")

            // Ajuste dinámico de Sec-Fetch-Site
            pcHeaders["Sec-Fetch-Site"] = if (finalUrl.contains("vimeos.net")) "same-site" else "cross-site"

            Log.d("LaMovie", "LOG: Enviando link compatible: $finalUrl")

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
        Log.i("LaMovie", "LOG: Cargando Goodstream con Headers de PC -> $url")

        val response = app.get(url, referer = "https://la.movie/")
        val res = response.text
        val cookieString = response.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

        val pcHeaders = mapOf(
            "Accept" to "*/*",
            "Origin" to mainUrl,
            "Referer" to "$mainUrl/",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "cross-site",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "sec-ch-ua-platform" to "\"Windows\"",
            "Cookie" to cookieString
        )

        val m3u8 = Regex("""file\s*:\s*["']([^"']+\.m3u8[^"']*)["']""").find(res)?.groupValues?.get(1)

        m3u8?.let { link ->
            val finalUrl = fixUrl(link).replace("http://", "https://")
            callback.invoke(
                newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = pcHeaders
                }
            )
        }
    }
}