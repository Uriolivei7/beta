package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.network.CloudflareKiller
import org.jsoup.nodes.Document
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Element

class AnimeLatinoHDProvider : MainAPI() {
    override var mainUrl = "https://www.animelatinohd.com"
    override var name = "AnimeLatinoHD"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasDownloadSupport = true
    override val hasQuickSearch = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val cloudflareKiller = CloudflareKiller()
    private var sessionCookies = mutableMapOf<String, String>()
    
    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "sec-ch-ua" to "\"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "document",
        "sec-fetch-mode" to "navigate",
        "sec-fetch-site" to "same-origin",
    )

    private suspend fun initCookies() {
        if (sessionCookies.isNotEmpty()) return
        try {
            val response = app.get(mainUrl, timeout = 15, headers = baseHeaders)
            if (response.isSuccessful && !response.text.contains("challenge-platform") && !response.text.contains("cf-turnstile")) {
                sessionCookies = response.cookies.toMutableMap()
                Log.d("AnimeLatinoHD", "Collected ${sessionCookies.size} cookies")
            } else {
                Log.w("AnimeLatinoHD", "Cloudflare challenge page detected, waiting for user to solve in WebView")
            }
        } catch (e: Exception) {
            Log.e("AnimeLatinoHD", "Failed to collect cookies: ${e.message}")
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/animes" to "Animes",
        "$mainUrl/animes/populares" to "Populares",
        "$mainUrl/animes/mas-vistos" to "Más Vistos",
        "$mainUrl/animes/latino" to "Latino",
        "$mainUrl/animes/castellano" to "Castellano",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        initCookies()
        val url = if (request.data.contains('?')) "${request.data}&page=$page" else "${request.data}?page=$page"
        val response = app.get(url, timeout = 15, headers = baseHeaders, cookies = sessionCookies)
        
        if (!response.isSuccessful || response.text.contains("challenge-platform") || response.text.contains("cf-turnstile")) {
            Log.w("AnimeLatinoHD", "Cloudflare challenge detected on main page")
            return newHomePageResponse(
                list = HomePageList(name = request.name, list = emptyList(), isHorizontalImages = false),
                hasNext = false
            )
        }

        val items = parseRscPayload(response.text)
        if (items.isNotEmpty()) {
            return newHomePageResponse(
                list = HomePageList(
                    name = request.name,
                    list = items,
                    isHorizontalImages = false
                ),
                hasNext = true
            )
        }

        val fallback = response.document.select("a.animeCard_animeCard__A3lxl, div.listAnime a").mapNotNull { it.toAnimeCardResult() }
        return newHomePageResponse(
            list = HomePageList(name = request.name, list = fallback, isHorizontalImages = false),
            hasNext = true
        )
    }

    private fun parseRscPayload(html: String): List<SearchResponse> {
        val results = mutableListOf<SearchResponse>()
        return try {
            val hrefMatches = Regex("\"href\":\"([^\"]*\\/anime\\/[^\"]+)\"").findAll(html)
            for (match in hrefMatches) {
                val href = match.groupValues[1]
                val titleMatches = Regex("\"children\":\"([^\"]+)\"").findAll(html)
                val title = titleMatches.map { it.groupValues[1] }.firstOrNull { it.isNotBlank() && !it.startsWith("/") && !it.startsWith("/") && !it.contains("http") } ?: ""
                val posterMatches = Regex("\"image\":\"([^\"]+)\"").findAll(html)
                val poster = posterMatches.map { it.groupValues[1] }.firstOrNull { it.isNotBlank() } ?: ""
                if (title.isNotBlank()) {
                    results.add(newTvSeriesSearchResponse(title, "$mainUrl$href", TvType.Anime) {
                        this.posterUrl = if (poster.startsWith("http")) poster else "https://media.themoviedb.org/t/p/w300$poster"
                    })
                }
            }
            results
        } catch (e: Exception) {
            Log.e("AnimeLatinoHD", "Error parsing RSC payload: ${e.message}")
            results
        }
    }

    private fun Element.toAnimeCardResult(): SearchResponse {
        val href = this.attr("abs:href").ifBlank { this.attr("href") }
        val img = this.selectFirst("img")
        val poster = img?.run { attr("src").ifBlank { attr("data-src") } }
        val title = this.selectFirst("h2")?.text()?.trim()
            ?: img?.attr("alt")?.replace("Ver ", "")?.replace(" Online", "")?.trim() ?: ""
        return newTvSeriesSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = poster
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        initCookies()
        val docUrl = "$mainUrl/animes?search=${query.replace(" ", "+")}"
        return try {
            val response = app.get(docUrl, timeout = 15, headers = baseHeaders, cookies = sessionCookies)
            val text = response.text
            
            if (!response.isSuccessful || text.contains("challenge-platform") || text.contains("cf-turnstile")) {
                Log.w("AnimeLatinoHD", "Cloudflare challenge detected on search")
                return emptyList()
            }
            
            val rscResults = parseRscPayload(text)
            if (rscResults.isNotEmpty()) {
                val q = query.lowercase()
                return rscResults.filter { it.name.lowercase().contains(q) }
            }
            
            response.document.select("a.animeCard_animeCard__A3lxl, div.listAnime a").mapNotNull { it.toAnimeCardResult() }
        } catch (e: Exception) {
            Log.e("AnimeLatinoHD", "Search error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        initCookies()
        val document = app.get(url, timeout = 15, headers = baseHeaders, cookies = sessionCookies).document

        val title = document.selectFirst("h1")?.ownText()?.trim()
            ?: document.selectFirst("h1")?.text()?.trim()
            ?: "Unknown"

        val poster = extractPoster(document)
        val description = document.selectFirst("p.page_overview__iOivM")?.text()
            ?: document.selectFirst("div.page_animeInfoRight__DwfVi > p")?.text()
            ?: ""
        val tags = document.select("div.page_genres__NsuIW a, div.page_genres__NsuIW span").map { it.text() }
        val statusText = document.select("div.page_animeDetailItem__wB8Ty").find { it.selectFirst("span")?.text()?.contains("Estado", ignoreCase = true) == true }?.selectFirst("small")?.text()
        val showStatus = when {
            statusText?.contains("Emisión", ignoreCase = true) == true || statusText?.contains("En emisión", ignoreCase = true) == true -> ShowStatus.Ongoing
            statusText?.contains("Finalizado", ignoreCase = true) == true -> ShowStatus.Completed
            else -> null
        }

        val yearText = document.select("div.page_animeDetailItem__wB8Ty").find { it.selectFirst("span")?.text()?.contains("Estreno", ignoreCase = true) == true }?.selectFirst("small")?.text()
        val year = Regex("(\\d{4})").find(yearText ?: "")?.groupValues?.get(1)?.toIntOrNull()
        val scoreText = document.select("div.page_animeDetailItem__wB8Ty").find { it.selectFirst("span")?.text()?.contains("Avg", ignoreCase = true) == true || it.selectFirst("span")?.text()?.contains("Score", ignoreCase = true) == true }?.selectFirst("small")?.text()
        val score = Regex("([0-9]+\\.[0-9]+)").find(scoreText ?: "")?.groupValues?.get(1)?.toDoubleOrNull()

        val episodes = extractEpisodes(document, url, poster)

        return if (episodes.isNotEmpty()) {
            newAnimeLoadResponse(title, url, TvType.Anime) {
                addEpisodes(DubStatus.Subbed, episodes)
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.showStatus = showStatus
                this.tags = tags
                this.score = score?.let { Score.from10(it) }
            }
        } else {
            newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = year
            }
        }
    }

    private suspend fun extractEpisodes(document: Document, animeUrl: String, posterUrl: String): List<Episode> {
        val episodes = mutableListOf<Episode>()
        val slug = Regex("/anime/([^/]+)").find(animeUrl)?.groupValues?.get(1)
            ?: return extractEpisodesFromHtml(document, animeUrl, posterUrl)

        val html = document.html()
        val epNumMatches = Regex("\"number\":(\\d+)").findAll(html)
        val episodeNumbers = epNumMatches.map { it.groupValues[1].toIntOrNull() }.filterNotNull().toSet()
        
        for (epNum in episodeNumbers) {
            if (epNum > 0) {
                val epUrl = "$mainUrl/ver/$slug/$epNum"
                episodes.add(newEpisode(epUrl) {
                    this.name = "Episodio $epNum"
                    this.episode = epNum
                    this.posterUrl = posterUrl.ifBlank { null }
                })
            }
        }
        
        if (episodes.isNotEmpty()) {
            return episodes.sortedBy { it.episode }
        }
        
        return extractEpisodesFromHtml(document, animeUrl, posterUrl)
    }

    private fun extractEpisodesFromHtml(document: Document, animeUrl: String, posterUrl: String): List<Episode> {
        val episodes = mutableListOf<Episode>()
        val slug = Regex("/anime/([^/]+)").find(animeUrl)?.groupValues?.get(1) ?: return episodes
        document.select("#episodes-grid a, div.episode-grid a, a[data-episode]").forEach { card ->
            val epHref = card.attr("abs:href")
            val epNum = card.attr("data-episode").toIntOrNull() ?: Regex("/ver/[^/]+/(\\d+)").find(epHref)?.groupValues?.get(1)?.toIntOrNull()
            if (epHref.isNotBlank() && epNum != null) {
                episodes.add(newEpisode(epHref) {
                    this.name = "Episodio $epNum"
                    this.episode = epNum
                    this.posterUrl = posterUrl.ifBlank { null }
                })
            }
        }
        return episodes.sortedBy { it.episode }
    }

    private fun extractPoster(document: Document): String {
        val ogImage = document.selectFirst("meta[property='og:image']")?.attr("content")
        if (!ogImage.isNullOrBlank()) return ogImage
        val coverImg = document.selectFirst("div.page_cover___eEeC img, img[src*='themoviedb']")
        if (coverImg != null) {
            val src = coverImg.attr("src")
            if (src.isNotBlank()) return src
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
        val langMap = mapOf("0" to "Subtitulado", "1" to "Latino", "2" to "Castellano")

        try {
            val dataObj = JSONObject(data)
            val players = dataObj.optJSONArray("players")
            if (players != null && players.length() > 0) {
                for (i in 0 until players.length()) {
                    val player = players.getJSONObject(i)
                    val embedUrl = player.optString("embed_url", "")
                    if (embedUrl.isNotBlank()) loadExtractor(embedUrl, mainUrl, subtitleCallback, callback)
                }
                return true
            }
        } catch (e: Exception) {}

        initCookies()
        val response = app.get(data, timeout = 15, headers = baseHeaders, cookies = sessionCookies)
        val html = response.text

        val videoUrlMatches = Regex("\"videoUrlEncrypted\":\"([^\"]+)\"").findAll(html)
        for (match in videoUrlMatches) {
            val videoUrl = match.groupValues[1]
            if (videoUrl.isNotBlank()) {
                callback(newExtractorLink(
                    source = "Video",
                    name = "Video",
                    url = videoUrl,
                ) {
                    this.referer = mainUrl
                    this.quality = Qualities.Unknown.value
                    this.type = ExtractorLinkType.M3U8
                })
            }
        }

        val serverUrlMatches = Regex("\"serverUrl\":\"([^\"]+)\"").findAll(html)
        for (match in serverUrlMatches) {
            val serverUrl = match.groupValues[1]
            if (serverUrl.isNotBlank()) {
                loadExtractor(serverUrl, mainUrl, subtitleCallback, callback)
            }
        }

        val document = response.document
        document.select("div.VidePlayer_options__yxprW select option").forEach { option ->
            val serverName = option.text()
            val iframe = document.selectFirst("div.VidePlayer_iframeContainer__qIOOy iframe")
            if (iframe != null) {
                val src = iframe.attr("src")
                if (src.isNotBlank()) loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
            }
        }

        document.select("button.server-btn").forEach { btn ->
            val serverUrl = btn.attr("data-url")
            if (serverUrl.isNotBlank()) loadExtractor(serverUrl, mainUrl, subtitleCallback, callback)
        }

        document.select("iframe#videoFrame").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotBlank()) loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
        }

        return true
    }
}
