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

        Log.d("LaMovie", "LOG: Cookies obtenidas para Vimeos: ${cookieString.take(20)}...")

        val standardHeaders = mapOf(
            "Referer" to "$mainUrl/",
            "Origin" to mainUrl.removeSuffix("/"),
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
            "Accept" to "*/*",
            "Cookie" to cookieString
        )

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        val subData = Regex("""["']([^"']+\.vtt[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)
        subData?.split(",")?.forEach { rawSub ->
            try {
                val langLabel = Regex("""\[([^\]]+)\]""").find(rawSub)?.groupValues?.get(1) ?: "Español"
                val cleanSubUrl = rawSub.substringAfter("]").trim()
                if (cleanSubUrl.startsWith("http")) {
                    Log.d("LaMovie", "LOG: Subtítulo detectado: $langLabel")
                    subtitleCallback.invoke(newSubtitleFile(langLabel, cleanSubUrl) { this.headers = standardHeaders })
                }
            } catch (e: Exception) {
                Log.e("LaMovie", "LOG: Error en subtítulos Vimeos: ${e.message}")
            }
        }

        videoUrl?.let { m3u8 ->
            val finalUrl = fixUrl(m3u8).replace("http://", "https://")
            Log.i("LaMovie", "LOG: URL de video encontrada: $finalUrl")

            val m3uLinks = try {
                M3u8Helper.generateM3u8(this.name, finalUrl, "$mainUrl/", headers = standardHeaders)
            } catch (e: Exception) {
                Log.e("LaMovie", "LOG: Error en M3u8Helper Vimeos: ${e.message}")
                emptyList()
            }

            if (m3uLinks.isEmpty()) {
                Log.w("LaMovie", "LOG: M3u8Helper falló o vacío, enviando link directo para evitar error 2004.")
                callback.invoke(
                    newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                        this.quality = Qualities.P1080.value
                        this.headers = standardHeaders
                    }
                )
            } else {
                Log.i("LaMovie", "LOG: M3u8Helper exitoso, enviando ${m3uLinks.size} calidades.")
                m3uLinks.forEach { link ->
                    callback.invoke(
                        newExtractorLink(this.name, this.name, link.url, ExtractorLinkType.M3U8) {
                            this.quality = link.quality
                            this.headers = standardHeaders
                        }
                    )
                }
            }
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
            "Origin" to mainUrl.removeSuffix("/"),
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
            "Cookie" to cookieString
        )

        m3u8?.let { link ->
            val finalUrl = fixUrl(link).replace("http://", "https://")
            Log.i("LaMovie", "LOG: Goodstream m3u8 encontrado: $finalUrl")

            val m3uLinks = try {
                M3u8Helper.generateM3u8(this.name, finalUrl, url, headers = standardHeaders)
            } catch (e: Exception) {
                Log.e("LaMovie", "LOG: Error en M3u8Helper Goodstream: ${e.message}")
                emptyList()
            }

            if (m3uLinks.isEmpty()) {
                Log.w("LaMovie", "LOG: Goodstream Helper falló, enviando link directo con Cookies.")
                callback.invoke(
                    newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                        this.quality = Qualities.P1080.value
                        this.headers = standardHeaders
                    }
                )
            } else {
                Log.i("LaMovie", "LOG: Goodstream Helper exitoso.")
                m3uLinks.forEach { m3uLink ->
                    callback.invoke(
                        newExtractorLink(this.name, this.name, m3uLink.url, ExtractorLinkType.M3U8) {
                            this.quality = m3uLink.quality
                            this.headers = standardHeaders
                        }
                    )
                }
            }
        }
    }
}