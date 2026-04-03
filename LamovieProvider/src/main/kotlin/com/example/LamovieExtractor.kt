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
        val embedUrl =
            if (!url.contains("/embed-")) "$mainUrl/embed-${url.substringAfterLast("/")}" else url

        val response = app.get(embedUrl, referer = "https://la.movie/")
        val res = response.text

        val cookieString = response.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

        val pcHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Referer" to embedUrl,
            "Accept-Language" to "es-ES,es;q=0.7",
            "sec-ch-ua" to "\"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Brave\";v=\"146\"",
            "sec-ch-ua-mobile" to "?0",
            "sec-ch-ua-platform" to "\"Windows\"",
            "Cookie" to cookieString
        )

        val allSubs = Regex("""["'](https?://[^"']+\.(?:vtt|srt)[^"']*)["']""").findAll(res)

        var foundAny = false
        allSubs.forEach { match ->
            val subUrl = fixUrl(match.groupValues[1])

            val label = when {
                subUrl.contains("spa", ignoreCase = true) || subUrl.contains("_es") -> "Español"
                subUrl.contains("eng", ignoreCase = true) || subUrl.contains("_en") -> "English"
                else -> "Subtítulo"
            }

            Log.d("LaMovie", "LOG: Subtítulo detectado -> $label en $subUrl")
            foundAny = true

            subtitleCallback.invoke(
                newSubtitleFile(label, subUrl) {
                    this.headers = pcHeaders
                }
            )
        }

        if (!foundAny) {
            Regex("""file\s*:\s*["']([^"']+)["']\s*,\s*label\s*:\s*["']([^"']+)["']""").findAll(res).forEach { match ->
                val subUrl = fixUrl(match.groupValues[1])
                val subLabel = match.groupValues[2]

                if (subUrl.contains(".vtt") || subUrl.contains(".srt")) {
                    Log.d("LaMovie", "LOG: Subtítulo detectado (vía tracks) -> $subLabel")
                    subtitleCallback.invoke(
                        newSubtitleFile(subLabel, subUrl) {
                            this.headers = pcHeaders
                        }
                    )
                }
            }
        }

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""file\s*:\s*["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        videoUrl?.let { m3u8 ->
            val finalUrl = fixUrl(m3u8).replace("http://", "https://")
            callback.invoke(
                newExtractorLink(this.name, this.name, finalUrl, ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.headers = pcHeaders.toMutableMap().apply {
                        put("Referer", "$mainUrl/")
                    }
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

        val tracksRaw = Regex("""tracks:\s*(\[[\s\S]*?\])""").find(res)?.groupValues?.get(1)
        tracksRaw?.let { raw ->
            Regex("""file:\s*["']([^"']+)["']\s*,\s*label:\s*["']([^"']+)["']""").findAll(raw).forEach { match ->
                val subUrl = fixUrl(match.groupValues[1])
                val subLabel = match.groupValues[2]
                subtitleCallback.invoke(
                    newSubtitleFile(subLabel, subUrl) {
                        this.headers = pcHeaders
                    }
                )
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