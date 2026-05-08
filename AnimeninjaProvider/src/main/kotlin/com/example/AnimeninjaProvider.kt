package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.network.CloudflareKiller
import org.jsoup.nodes.Document
import android.util.Log
import org.json.JSONObject
import org.jsoup.nodes.Element
import org.jsoup.Jsoup

class AnimeOnlineNinjaProvider : MainAPI() {
    override var mainUrl = "https://ww3.animeonline.ninja"
    override var name = "AnimeOnlineNinja"
    override val hasMainPage = true
    override var lang = "es"
    override val hasDownloadSupport = true
    override val hasQuickSearch = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val cloudflareKiller = CloudflareKiller()

    private val baseHeaders = mapOf(
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8"
    )

    private suspend fun getHtml(url: String): String? {
        Log.d("AnimeOnlineNinja", "Fetching URL: $url")
        return try {
            val response = app.get(url, headers = baseHeaders, timeout = 30, interceptor = cloudflareKiller)
            val html = response.text
            val len = html.length
            if (html.contains("challenge-platform") || html.contains("cf-turnstile")) {
                Log.w("AnimeOnlineNinja", "Cloudflare challenge detected at $url")
                return null
            }
            Log.d("AnimeOnlineNinja", "Fetched $len bytes from $url")
            html
        } catch (e: Exception) {
            Log.e("AnimeOnlineNinja", "Error fetching $url: ${e.message}")
            null
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/inicio/" to "Inicio",
        "$mainUrl/online/" to "Animes",
        "$mainUrl/pelicula/" to "Películas",
        "$mainUrl/genero/audio-latino/" to "Latino",
        "$mainUrl/genero/anime-castellano/" to "Castellano",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (page > 1) "${request.data.trimEnd('/')}/page/$page/" else request.data
        Log.d("AnimeOnlineNinja", "getMainPage: page=$page name=${request.name} url=$url")
        val html = getHtml(url) ?: run {
            Log.w("AnimeOnlineNinja", "getMainPage: html=null for $url")
            return newHomePageResponse(
                list = HomePageList(name = request.name, list = emptyList(), isHorizontalImages = false),
                hasNext = false
            )
        }
        val document = Jsoup.parse(html)

        val items = document.select("article.item").mapNotNull { it.toSearchResult() }
        val hasNext = document.selectFirst(".pagination .next, .pagination a.next.page-numbers, a.next.page-numbers") != null
        Log.d("AnimeOnlineNinja", "getMainPage: ${items.size} items, hasNext=$hasNext")

        return newHomePageResponse(
            list = HomePageList(name = request.name, list = items, isHorizontalImages = false),
            hasNext = hasNext
        )
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val linkEl = this.selectFirst(".data h3 a") ?: return null
        val href = linkEl.attr("abs:href").ifBlank { return null }
        val title = linkEl.text().trim()
        if (title.isBlank()) return null

        val img = this.selectFirst(".poster img")
        val poster = img?.attr("data-src")?.ifBlank { img.attr("src") }?.ifBlank { null }

        val type = when {
            href.contains("/pelicula/") || this.hasClass("movies") -> TvType.AnimeMovie
            else -> TvType.Anime
        }

        return when (type) {
            TvType.AnimeMovie -> newMovieSearchResponse(title, href, TvType.AnimeMovie) { this.posterUrl = poster }
            else -> newTvSeriesSearchResponse(title, href, TvType.Anime) { this.posterUrl = poster }
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/?s=${query.replace(" ", "+")}"
        Log.d("AnimeOnlineNinja", "search: query=$query url=$url")
        val html = getHtml(url) ?: run {
            Log.w("AnimeOnlineNinja", "search: html=null for $url")
            return emptyList()
        }
        val document = Jsoup.parse(html)
        val results = document.select("article.item").mapNotNull { it.toSearchResult() }
        Log.d("AnimeOnlineNinja", "search: ${results.size} results for '$query'")
        return results
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("AnimeOnlineNinja", "load: url=$url")
        val html = getHtml(url) ?: throw Exception("Failed to load page")
        val document = Jsoup.parse(html)

        val title = document.selectFirst(".sheader .data h1")?.text()?.trim()
            ?: document.selectFirst("h1")?.text()?.trim()
            ?: "Unknown"
        Log.d("AnimeOnlineNinja", "load: title='$title'")

        val poster = extractPoster(document)
        Log.d("AnimeOnlineNinja", "load: poster=$poster")

        val description = document.selectFirst("#info .wp-content p")?.text() ?: ""
        Log.d("AnimeOnlineNinja", "load: description=${description.take(100)}")

        val tags = document.select(".sgeneros a").map { it.text() }
        Log.d("AnimeOnlineNinja", "load: tags=$tags")

        val year = document.selectFirst("meta[property='article:published_time']")?.attr("content")?.let {
            Regex("(\\d{4})").find(it)?.groupValues?.get(1)?.toIntOrNull()
        }
        Log.d("AnimeOnlineNinja", "load: year=$year")

        val episodes = extractEpisodes(document, poster)
        Log.d("AnimeOnlineNinja", "load: ${episodes.size} episodes extracted")

        return if (episodes.isNotEmpty()) {
            Log.d("AnimeOnlineNinja", "load: returning Anime with ${episodes.size} episodes")
            newAnimeLoadResponse(title, url, TvType.Anime) {
                addEpisodes(DubStatus.Subbed, episodes)
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.tags = tags
            }
        } else {
            Log.d("AnimeOnlineNinja", "load: no episodes, returning AnimeMovie")
            newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = year
            }
        }
    }

    private fun extractEpisodes(document: Document, posterUrl: String): List<Episode> {
        val episodes = mutableListOf<Episode>()
        val seasonContainers = document.select("#seasons .se-c")
        Log.d("AnimeOnlineNinja", "extractEpisodes: ${seasonContainers.size} season containers found")

        seasonContainers.forEach { season ->
            val seasonNum = season.selectFirst(".se-q .se-t")?.text()?.toIntOrNull() ?: 1
            val epItems = season.select(".se-a ul.episodios li")
            Log.d("AnimeOnlineNinja", "extractEpisodes: season=$seasonNum, ${epItems.size} episode items")

            epItems.forEach { item ->
                val link = item.selectFirst(".episodiotitle a") ?: run {
                    Log.w("AnimeOnlineNinja", "extractEpisodes: no .episodiotitle a in item")
                    return@forEach
                }
                val href = link.attr("abs:href").ifBlank { return@forEach }
                val name = link.text().trim()

                val img = item.selectFirst(".imagen img")
                val epPoster = img?.attr("data-src")?.ifBlank { img.attr("src") }?.ifBlank { null }
                    ?: posterUrl.ifBlank { null }

                val numText = item.selectFirst(".numerando")?.text() ?: ""
                val parts = numText.split("-").map { it.trim() }
                val episodeNum = parts.getOrNull(1)?.toIntOrNull()

                if (episodeNum != null) {
                    episodes.add(newEpisode(href) {
                        this.name = name
                        this.episode = episodeNum
                        this.season = seasonNum
                        this.posterUrl = epPoster
                    })
                } else {
                    Log.w("AnimeOnlineNinja", "extractEpisodes: invalid numerando='$numText' for '$name'")
                }
            }
        }

        val sorted = episodes.sortedBy { it.episode }
        Log.d("AnimeOnlineNinja", "extractEpisodes: ${sorted.size} total episodes")
        return sorted
    }

    private fun extractPoster(document: Document): String {
        val ogImage = document.selectFirst("meta[property='og:image']")?.attr("content")
        if (!ogImage.isNullOrBlank() && !ogImage.contains("svg") && ogImage.startsWith("http")) return ogImage
        val posterImg = document.selectFirst(".sheader .poster img")
        if (posterImg != null) {
            val src = posterImg.attr("data-src").ifBlank { posterImg.attr("src") }
            if (src.isNotBlank() && !src.contains("svg")) return src
        }
        return ""
    }

    @Suppress("DEPRECATION", "DEPRECATION_ERROR")
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("AnimeOnlineNinja", "loadLinks: data=$data")
        val html = getHtml(data) ?: return false
        val document = Jsoup.parse(html)

        val apiBase = "$mainUrl/wp-json/dooplayer/v1/post/"
        var found = false

        val playerOptions = document.select("#playeroptionsul li.dooplay_player_option")
        Log.d("AnimeOnlineNinja", "loadLinks: ${playerOptions.size} player options found")

        playerOptions.forEach { option ->
            val postId = option.attr("data-post")
            val nume = option.attr("data-nume")
            val type = option.attr("data-type")
            val serverName = option.selectFirst(".server")?.text() ?: "Server"
            val titleName = option.selectFirst(".title")?.text() ?: serverName
            Log.d("AnimeOnlineNinja", "loadLinks: option type=$type post=$postId nume=$nume server=$serverName")

            if (postId.isNotBlank() && nume.isNotBlank()) {
                try {
                    val apiUrl = "${apiBase}${type}/${postId}/${nume}/"
                    Log.d("AnimeOnlineNinja", "loadLinks: calling API $apiUrl")
                    val response = app.get(apiUrl, headers = baseHeaders, interceptor = cloudflareKiller, timeout = 15)
                    val responseText = response.text
                    Log.d("AnimeOnlineNinja", "loadLinks: API response ${responseText.length} chars")
                    found = true

                    try {
                        val json = JSONObject(responseText)
                        val embedUrl = json.optString("embed_url", "")
                        val videoUrl = json.optString("url", "")
                        Log.d("AnimeOnlineNinja", "loadLinks: JSON parsed - embed_url='${embedUrl.take(100)}' url='${videoUrl.take(100)}'")
                        if (embedUrl.isNotBlank()) {
                            Log.d("AnimeOnlineNinja", "loadLinks: loading extractor from embed_url=$embedUrl")
                            loadExtractor(embedUrl, mainUrl, subtitleCallback, callback)
                        } else if (videoUrl.isNotBlank()) {
                            Log.d("AnimeOnlineNinja", "loadLinks: loading extractor from url=$videoUrl")
                            loadExtractor(videoUrl, mainUrl, subtitleCallback, callback)
                        } else {
                            Log.w("AnimeOnlineNinja", "loadLinks: JSON has no embed_url or url")
                        }
                    } catch (e: Exception) {
                        Log.d("AnimeOnlineNinja", "loadLinks: response is not JSON, parsing as HTML: ${e.message}")
                        val respDoc = Jsoup.parse(responseText)
                        val iframes = respDoc.select("iframe")
                        val sources = respDoc.select("video source[src]")
                        Log.d("AnimeOnlineNinja", "loadLinks: HTML fallback - ${iframes.size} iframes, ${sources.size} video sources")
                        iframes.forEach { iframe ->
                            val src = iframe.attr("src")
                            if (src.isNotBlank()) {
                                Log.d("AnimeOnlineNinja", "loadLinks: iframe src=$src")
                                loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                            }
                        }
                        sources.forEach { source ->
                            val src = source.attr("src")
                            if (src.isNotBlank()) {
                                Log.d("AnimeOnlineNinja", "loadLinks: video source src=$src")
                                callback(newExtractorLink(titleName, titleName, src) {
                                    this.referer = mainUrl
                                    this.quality = Qualities.Unknown.value
                                    this.type = ExtractorLinkType.M3U8
                                })
                            }
                        }
                        if (iframes.isEmpty() && sources.isEmpty()) {
                            Log.w("AnimeOnlineNinja", "loadLinks: HTML fallback found no iframes or video sources, raw response: ${responseText.take(500)}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AnimeOnlineNinja", "API error for $postId/$nume: ${e.message}")
                }
            }
        }

        if (!found) {
            Log.d("AnimeOnlineNinja", "loadLinks: no player options, trying direct iframes")
            document.select("iframe").forEach { iframe ->
                val src = iframe.attr("src")
                if (src.isNotBlank() && !src.contains("google") && !src.contains("facebook")) {
                    Log.d("AnimeOnlineNinja", "loadLinks: direct iframe src=$src")
                    loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                }
            }
        }

        return true
    }
}
