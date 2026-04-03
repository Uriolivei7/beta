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

        val standardHeaders = mapOf(
            "Referer" to "$mainUrl/",
            "Origin" to mainUrl.removeSuffix("/"),
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
            "Accept" to "*/*",
            "Accept-Language" to "es-ES,es;q=0.9"
        )

        val res = app.get(embedUrl, referer = "https://la.movie/").text
        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        val subData = Regex("""["']([^"']+\.vtt[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)
        subData?.split(",")?.forEach { rawSub ->
            try {
                val langLabel = Regex("""\[([^\]]+)\]""").find(rawSub)?.groupValues?.get(1) ?: "Español"
                val cleanSubUrl = rawSub.substringAfter("]").trim()
                if (cleanSubUrl.startsWith("http")) {
                    subtitleCallback.invoke(newSubtitleFile(langLabel, cleanSubUrl) { this.headers = standardHeaders })
                }
            } catch (e: Exception) { }
        }

        videoUrl?.let { m3u8 ->
            val finalUrl = fixUrl(m3u8).replace("http://", "https://")
            val m3uLinks = try {
                M3u8Helper.generateM3u8(this.name, finalUrl, "$mainUrl/", headers = standardHeaders)
            } catch (e: Exception) { emptyList() }

            if (m3uLinks.isEmpty()) {
                callback.invoke(
                    newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                        this.quality = Qualities.P1080.value
                        this.headers = standardHeaders
                    }
                )
            } else {
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
        val standardHeaders = mapOf(
            "Referer" to "$mainUrl/",
            "Origin" to mainUrl.removeSuffix("/"),
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
            "Accept" to "*/*"
        )

        val res = app.get(url, referer = "https://la.movie/").text
        val m3u8 = Regex("""file\s*:\s*["']([^"']+\.m3u8[^"']*)["']""").find(res)?.groupValues?.get(1)

        m3u8?.let { link ->
            val finalUrl = fixUrl(link).replace("http://", "https://")
            val m3uLinks = try {
                M3u8Helper.generateM3u8(this.name, finalUrl, url, headers = standardHeaders)
            } catch (e: Exception) { emptyList() }

            if (m3uLinks.isEmpty()) {
                callback.invoke(
                    newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                        this.quality = Qualities.P1080.value
                        this.headers = standardHeaders 
                    }
                )
            } else {
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