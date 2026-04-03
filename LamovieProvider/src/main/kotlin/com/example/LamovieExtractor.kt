package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

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
        val embedUrl = getEmbedUrl(url)
        val response = app.get(embedUrl, referer = referer)
        val res = response.text

        extractSubs(res, embedUrl, subtitleCallback)

        val unpackedJs = unpackJs(response.document) ?: res
        Regex("""file:\s*"([^"]+\.m3u8[^"]*)"""").find(unpackedJs)?.groupValues?.get(1)?.let { link ->
            M3u8Helper.generateM3u8(this.name, fixUrl(link), "$mainUrl/").forEach(callback)
        }
    }

    private fun unpackJs(element: Element): String? {
        return element.select("script").find { it.data().contains("eval(function(p,a,c,k,e,d)") }
            ?.data()?.let { getAndUnpack(it) }
    }

    private fun getEmbedUrl(url: String): String {
        return if (!url.contains("/embed-")) "$mainUrl/embed-${url.substringAfterLast("/")}" else url
    }
}

class GoodstreamExtractor : ExtractorApi() {
    override var name = "Goodstream"
    override val mainUrl = "https://goodstream.one"
    override val requiresReferer = false

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val response = app.get(url)
        val res = response.text

        extractSubs(res, url, subtitleCallback)

        Regex("""file:\s*"([^"]+\.m3u8[^"]*)"""").find(res)?.groupValues?.get(1)?.let { link ->
            M3u8Helper.generateM3u8(this.name, fixUrl(link), "$mainUrl/").forEach(callback)
        }
    }
}

private suspend fun extractSubs(html: String, refererUrl: String, subtitleCallback: (SubtitleFile) -> Unit) {
    val tracksMatch = Regex("""tracks\s*:\s*\[([\s\S]*?)\]""").find(html)
    tracksMatch?.groupValues?.get(1)?.let { rawTracks ->
        val trackRegex = Regex("""\{[^}]*file\s*:\s*["']([^"']+)["'][^}]*label\s*:\s*["']([^"']+)["'][^}]*\}""")

        trackRegex.findAll(rawTracks).forEach { match ->
            val rawUrl = match.groupValues[1]
            val subUrl = if (rawUrl.startsWith("http")) rawUrl
            else if (rawUrl.startsWith("//")) "https:$rawUrl"
            else "https://$rawUrl"

            val isVimeos = subUrl.contains("vimeos")
            val sourceName = if (isVimeos) "Vimeos" else "Good"
            val subLabel = "${match.groupValues[2]} ($sourceName)"

            if (subUrl.contains(".vtt") || subUrl.contains(".srt")) {
                Log.d("LaMovie", "LOG: Registrando Sub de $sourceName -> $subLabel: $subUrl")

                subtitleCallback.invoke(
                    newSubtitleFile(subLabel, subUrl) {
                        val finalReferer = if (isVimeos) "https://vimeos.net/" else "https://goodstream.one/"
                        val finalOrigin = if (isVimeos) "https://vimeos.net" else "https://goodstream.one"

                        this.headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
                            "Referer" to finalReferer,
                            "Origin" to finalOrigin,
                            "sec-ch-ua" to "\"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Brave\";v=\"146\"",
                            "sec-ch-ua-mobile" to "?0",
                            "sec-ch-ua-platform" to "\"Windows\"",
                            "Accept" to "*/*",
                            "Sec-Fetch-Dest" to "empty",
                            "Sec-Fetch-Mode" to "cors",
                            "Sec-Fetch-Site" to "cross-site"
                        )
                    }
                )
            }
        }
    }
}