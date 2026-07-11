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
    private val maxPagesGeneral = 2

    private val countrySections = listOf(
        "Perú" to "/canales/peru/",
        "Argentina" to "/canales/argentina/",
        "México" to "/canales/mexico/",
        "Colombia" to "/canales/colombia/",
        "Chile" to "/canales/chile/",
        "Estados Unidos" to "/canales/estados-unidos/",
        "Brasil" to "/canales/brasil/",
        "Venezuela" to "/canales/venezuela/",
        "España" to "/canales/espana/",
    )

    private fun extractPosterFromStyle(element: org.jsoup.nodes.Element): String? {
        val style = element.attr("style")
        if (style.isBlank()) return null
        return Regex("""url\s*\(?\s*['"]?\s*([^'")\s]+)""", RegexOption.IGNORE_CASE).find(style)?.groupValues?.get(1)
    }

    private data class ChannelInfo(val name: String, val url: String, val poster: String?)

    private fun extractChannels(html: String): List<ChannelInfo> {
        val channels = mutableListOf<ChannelInfo>()
        val doc = Jsoup.parse(html)
        doc.select("article.article-loop").forEach { article ->
            val link = article.selectFirst("a[rel=bookmark]")?.attr("href")
                ?: article.selectFirst("a")?.attr("href")
                ?: return@forEach
            val title = article.selectFirst(".entry-title")?.text()?.trim()
                ?: article.selectFirst("a p")?.text()?.trim()
                ?: return@forEach

            var poster: String? = null
            val imgEl = article.selectFirst(".article-image")
            if (imgEl != null) poster = extractPosterFromStyle(imgEl)
            if (poster == null) {
                poster = article.selectFirst("img")?.attr("src")?.let {
                    if (it.startsWith("http")) it else null
                }
            }

            channels.add(ChannelInfo(title, link, poster))
        }
        return channels
    }

    private suspend fun fetchSection(name: String, path: String, pages: Int = 1): List<ChannelInfo> {
        val channels = mutableListOf<ChannelInfo>()
        for (p in 1..pages) {
            val url = if (p == 1) "$mainUrl$path" else "$mainUrl${path}page/$p/"
            val html = safeGet(url) ?: continue
            channels.addAll(extractChannels(html))
        }
        return channels
    }

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

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val sections = mutableListOf<HomePageList>()

        // General channels
        val general = fetchSection("Canales", "/", maxPagesGeneral)
        if (general.isNotEmpty()) {
            sections.add(HomePageList("Canales", general.distinctBy { it.url }.map { it.toSearchResponse() }, isHorizontalImages = true))
        }

        // Country sections
        for ((name, path) in countrySections) {
            val channels = fetchSection(name, path)
            if (channels.isNotEmpty()) {
                sections.add(HomePageList(name, channels.distinctBy { it.url }.map { it.toSearchResponse() }, isHorizontalImages = true))
            }
        }

        if (sections.isEmpty()) return null
        return newHomePageResponse(sections, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val html = safeGet("$mainUrl/?s=$query") ?: return emptyList()
        return extractChannels(html).distinctBy { it.url }.map { it.toSearchResponse() }
    }

    override suspend fun load(url: String): LoadResponse? {
        try {
            val doc = try {
                app.get(url, interceptor = cfKiller).document
            } catch (_: Exception) {
                return null
            }
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
        } catch (e: Exception) {
            Log.e("Teleonline", "load error: ${e.message}")
            return null
        }
    }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            val mainHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
                "Accept-Language" to "es-ES,es;q=0.9"
            )

            val mainHtml = withTimeoutOrNull(20000L) {
                app.get(data, headers = mainHeaders, interceptor = cfKiller)
            }?.text ?: return false

            // Direct M3U8 on page
            val directM3u8 = extractM3u8FromHtml(mainHtml)
            if (directM3u8 != null) {
                callback(newExtractorLink(name, "$name - En Vivo", directM3u8, ExtractorLinkType.M3U8) {
                    this.headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Referer" to data
                    )
                })
                return true
            }

            // Look for iframes and player links
            val doc = Jsoup.parse(mainHtml)
            val playerUrls = doc.select("iframe[src], a[href*=/live], a[href*=.php], div[data-player] a, [data-src]")
                .mapNotNull { el ->
                    when {
                        el.tagName() == "iframe" -> el.attr("src")
                        el.hasAttr("data-src") -> el.attr("data-src")
                        else -> el.attr("href")
                    }
                }
                .filter { it.isNotBlank() && !it.contains("facebook") && !it.contains("twitter") }
                .distinct()
                .map { if (it.startsWith("http")) it else "$mainUrl$it" }

            Log.d("Teleonline", "playerUrls: $playerUrls")

            for ((idx, playerUrl) in playerUrls.withIndex()) {
                try {
                    val playerHtml = withTimeoutOrNull(15000L) {
                        app.get(playerUrl, headers = mainHeaders + mapOf("Referer" to data), interceptor = cfKiller)
                    }?.text ?: continue

                    val m3u8Url = extractM3u8FromHtml(playerHtml)
                    if (m3u8Url != null) {
                        callback(newExtractorLink(name, "$name - Opción ${idx + 1}", m3u8Url, ExtractorLinkType.M3U8) {
                            this.headers = mapOf(
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                "Referer" to playerUrl
                            )
                        })
                        return true
                    }
                } catch (_: Exception) {}
            }
            return false
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
        )
        for (pattern in patterns) {
            val match = Regex(pattern, RegexOption.IGNORE_CASE).find(html)
            if (match != null) {
                return match.groupValues[1].replace("\\/", "/")
            }
        }
        return null
    }

    private fun ChannelInfo.toSearchResponse() = newLiveSearchResponse(name, url, TvType.Live) {
        this.posterUrl = poster
    }
}
