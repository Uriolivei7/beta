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

        val headerMap = mapOf(
            "Referer" to "$mainUrl/",
            "Origin" to mainUrl,
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
            "Cookie" to cookieString
        )

        val tracksBlock = Regex("""tracks\s*:\s*\[([\s\S]*?)\]""").find(res)?.groupValues?.get(1)

        if (tracksBlock != null) {
            val subRegex = Regex("""file\s*:\s*["']([^"']+)["']\s*,\s*label\s*:\s*["']([^"']+)["']""")
            subRegex.findAll(tracksBlock).forEach { match ->
                val subUrl = fixUrl(match.groupValues[1])
                val label = match.groupValues[2]

                if (subUrl.contains(".vtt") || subUrl.contains(".srt")) {
                    Log.d("LaMovie", "LOG: Subtítulo detectado: $label -> $subUrl")
                    subtitleCallback.invoke(
                        newSubtitleFile(lang = label, url = subUrl) {
                            this.headers = headerMap
                        }
                    )
                }
            }
        } else {
            Regex("""["']([^"']+\.vtt[^"']*)["']""").findAll(res).forEach { match ->
                val subUrl = fixUrl(match.groupValues[1])
                val lang = when {
                    subUrl.contains("_spa") || subUrl.contains("_es") -> "Español"
                    subUrl.contains("_eng") || subUrl.contains("_en") -> "English"
                    else -> "Subtítulo"
                }

                subtitleCallback.invoke(
                    newSubtitleFile(lang = lang, url = subUrl) {
                        this.headers = headerMap
                    }
                )
            }
        }

        val unpackedJs = JsUnpacker(res).unpack() ?: res
        val videoUrl = Regex("""["']([^"']+\.m3u8[^"']*)["']""").find(unpackedJs)?.groupValues?.get(1)

        videoUrl?.let { m3u8 ->
            M3u8Helper.generateM3u8(this.name, fixUrl(m3u8), "$mainUrl/", headers = headerMap).forEach { link ->
                callback.invoke(
                    newExtractorLink(this.name, this.name, link.url, ExtractorLinkType.M3U8) {
                        this.quality = link.quality
                        this.headers = headerMap
                    }
                )
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