package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withTimeoutOrNull

class TeleonlineProvider : MainAPI() {
    override var mainUrl = "https://teleonline.org"
    override var name = "Teleonline"
    override val supportedTypes = setOf(TvType.Live)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val cfKiller = CloudflareKiller()
    private val maxPages = 3

    private suspend fun safeGet(url: String, timeoutMs: Long = 15000L): String? {
        for (attempt in 1..2) {
            try {
                val interceptor = if (attempt > 1) cfKiller else null
                val res = app.get(url, timeout = timeoutMs * attempt, interceptor = interceptor)
                if (res.isSuccessful) return res.text
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                Log.e("Teleonline", "safeGet error (intento $attempt): ${e.message}")
            }
        }
        return null
    }

    private data class ChannelInfo(val name: String, val url: String, val poster: String?)

    private fun extractChannels(html: String): List<ChannelInfo> {
        val channels = mutableListOf<ChannelInfo>()
        val doc = Jsoup.parse(html)
        doc.select("article.article-loop").forEach { article ->
            val link = article.selectFirst("a")?.attr("href") ?: return@forEach
            val title = article.selectFirst(".entry-title")?.text()?.trim()
                ?: article.selectFirst("a p")?.text()?.trim()
                ?: return@forEach
            val style = article.selectFirst(".article-image")?.attr("style") ?: ""
            val poster = Regex("""url\s*['"]?([^'")]+)""").find(style)?.groupValues?.get(1)
            channels.add(ChannelInfo(title, link, poster))
        }
        return channels
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val allChannels = mutableListOf<ChannelInfo>()
        for (p in 1..maxPages) {
            val url = if (p == 1) mainUrl else "$mainUrl/page/$p/"
            val html = safeGet(url) ?: continue
            allChannels.addAll(extractChannels(html))
        }
        val results = allChannels.distinctBy { it.url }.map { ch ->
            newLiveSearchResponse(ch.name, ch.url, TvType.Live) {
                this.posterUrl = ch.poster
            }
        }
        return newHomePageResponse(listOf(HomePageList("Canales", results)), hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val html = safeGet("$mainUrl/?s=$query") ?: return emptyList()
        val channels = extractChannels(html)
        return channels.map { ch ->
            newLiveSearchResponse(ch.name, ch.url, TvType.Live) {
                this.posterUrl = ch.poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document
        val title = doc.selectFirst("h1")?.text()
            ?: doc.selectFirst("title")?.text()
            ?: "Canal"
        val poster = doc.selectFirst("meta[property='og:image']")?.attr("content")
        val desc = doc.selectFirst("meta[property='og:description']")?.attr("content")
            ?: doc.selectFirst("meta[name=description]")?.attr("content")
            ?: ""

        val episodes = listOf(newEpisode(url) {
            this.name = "En Vivo"
            this.posterUrl = poster
        })
        return newTvSeriesLoadResponse(title, url, TvType.Live, episodes) {
            this.posterUrl = poster
            this.plot = desc
        }
    }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ): Boolean {
        val targetUrl = data
        try {
            val mainHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9"
            )
            val mainHtml = withTimeoutOrNull(20000L) {
                app.get(targetUrl, headers = mainHeaders, interceptor = cfKiller)
            }?.text ?: return false

            val m3u8 = extractM3u8FromHtml(mainHtml)
            if (m3u8 != null) {
                callback(newExtractorLink(name, "$name - En Vivo", m3u8, ExtractorLinkType.M3U8) {
                    this.headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Referer" to targetUrl
                    )
                })
                return true
            }

            val doc = Jsoup.parse(mainHtml)
            val optionLinks = doc.select("a[href*=/live], iframe[src], a[href*=.php]")
                .mapNotNull { if (it.tagName() == "iframe") it.attr("src") else it.attr("href") }
                .filter { it.isNotBlank() && !it.contains("facebook") }
                .distinct()

            return coroutineScope {
                for ((idx, rawUrl) in optionLinks.withIndex()) {
                    val playerUrl = if (rawUrl.startsWith("http")) rawUrl else "$mainUrl$rawUrl"
                    try {
                        val playerHtml = withTimeoutOrNull(15000L) {
                            app.get(playerUrl, headers = mainHeaders + mapOf("Referer" to targetUrl), interceptor = cfKiller)
                        }?.text ?: continue

                        val m3u8Url = extractM3u8FromHtml(playerHtml)
                        if (m3u8Url != null) {
                            callback(newExtractorLink(name, "$name - Opción ${idx + 1}", m3u8Url, ExtractorLinkType.M3U8) {
                                this.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                    "Referer" to playerUrl
                                )
                            })
                            return@coroutineScope true
                        }
                    } catch (_: Exception) {}
                }
                false
            }
        } catch (e: Exception) {
            Log.e("Teleonline", "loadLinks error: ${e.message}")
            return false
        }
    }

    private fun extractM3u8FromHtml(html: String): String? {
        val patterns = listOf(
            """["'](https?[:\/\/\\]+[^"']+\.m3u8[^"']*)["']""",
            """source\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
            """file\s*[:=]\s*["']([^"']+\.m3u8[^"']*)["']""",
            """var\s+src\s*=\s*["']([^"']+\.m3u8[^"']*)["']""",
            """(https?://[^"'\s<>]+\.m3u8[^"'\s<>]*)""",
            """['"]([^"']+\.m3u8[^"']*)['"]"""
        )
        for (pattern in patterns) {
            val match = Regex(pattern, RegexOption.IGNORE_CASE).find(html)
            if (match != null) {
                return match.groupValues[1].replace("\\/", "/")
            }
        }
        return null
    }
}
