package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.Jsoup

class MhdflixProvider : MainAPI() {
    override var mainUrl = "https://ww1.mhdflix.com"
    override var name = "MHDFLIX"
    override val hasMainPage = true
    override var lang = "es"
    override val hasDownloadSupport = true
    override val hasChromecastSupport = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.Cartoon)

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "Referer" to "$mainUrl/"
    )

    private fun fixUrlPath(path: String?): String {
        if (path.isNullOrBlank() || path.contains("undefined")) return ""
        return if (path.startsWith("http")) path
        else "https://image.tmdb.org/t/p/w500$path"
    }

    override val mainPage = mainPageOf(
        "$mainUrl/movies" to "Películas Recientes",
        "$mainUrl/tvs" to "Series Recientes",
        "$mainUrl/tvs/category/anime" to "Animes Recientes",
        "$mainUrl/tvs/category/dorama" to "Doramas Recientes",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d("Mhdflix-MainPage", "=== getMainPage START ===")
        Log.d("Mhdflix-MainPage", "request.name: ${request.name}, request.data: ${request.data}")
        
        val html = app.get(request.data, headers = baseHeaders).text
        Log.d("Mhdflix-MainPage", "HTML length: ${html.length}")

        val doc = Jsoup.parse(html)
        val links = doc.select("a[href*='/movies/'], a[href*='/tvs/']")
        Log.d("Mhdflix-MainPage", "Total links: ${links.size}")

        val tvType = when {
            request.name.contains("Película") -> TvType.Movie
            request.name.contains("Anime") -> TvType.Anime
            request.name.contains("Dorama") -> TvType.AsianDrama
            else -> TvType.TvSeries
        }

        val items = links.mapNotNull { link ->
            val href = link.attr("href")
            if (href.contains("/episode/") || href.contains("/category/") || href.contains("/genres")) return@mapNotNull null
            
            val id = Regex("""/(?:movies|tvs)/(\d+)/""").find(href)?.groupValues?.get(1) ?: return@mapNotNull null
            val slug = Regex("""/(?:movies|tvs)/\d+/([^/]+)""").find(href)?.groupValues?.get(1) ?: return@mapNotNull null
            
            val img = link.selectFirst("img")
            var poster = img?.attr("data-src")?.takeIf { it.isNotBlank() }
                ?: img?.attr("src")?.takeIf { it.isNotBlank() }
                ?: img?.let { 
                    Regex("""url\(['"]?([^'")]+)['"]?\)""").find(it.attr("style"))?.groupValues?.get(1)
                }

            val linkHtml = link.html()
            if (poster.isNullOrBlank() && linkHtml.contains("tmdb.org")) {
                poster = Regex("""tmdb\.org/t/p/w500([^'")\s]+)""").find(linkHtml)?.groupValues?.get(1)
                    ?.let { "https://image.tmdb.org/t/p/w500$it" }
            }

            if (poster.isNullOrBlank()) {
                Log.d("Mhdflix-MainPage", "  DEBUG linkHtml for id=$id: ${linkHtml.take(500)}")
            }

            val title = img?.attr("alt")?.takeIf { it.isNotBlank() }
                ?: link.selectFirst("h3, h4, p, span")?.text()
                ?: slug.replace("-", " ").replaceFirstChar { it.uppercase() }
            
            if (title.isNotBlank() && title.length > 2) {
                val fullUrl = if (href.startsWith("http")) href else "$mainUrl$href"
                val type = if (href.contains("/movies/")) "movie" else "tv"
                Log.d("Mhdflix-MainPage", "  Item: title='$title', poster='${poster?.take(50)}', url='$fullUrl'")
                SearchItem(id.toLong(), title, poster ?: "", fullUrl, type)
            } else null
        }.distinctBy { it.id }.take(30)

        Log.d("Mhdflix-MainPage", "Total items: ${items.size}")

        val searchResponses = items.map { item ->
            newMovieSearchResponse(item.title, item.url, tvType) {
                this.posterUrl = fixUrlPath(item.poster)
            }
        }

        Log.d("Mhdflix-MainPage", "=== getMainPage END ===")

        return if (searchResponses.isNotEmpty()) {
            newHomePageResponse(
                list = HomePageList(request.name, searchResponses),
                hasNext = false
            )
        } else null
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("Mhdflix-Search", "=== search START - query: '$query' ===")
        
        val searchUrl = "$mainUrl/api/search?query=${query.replace(" ", "+")}&page=1&limit=25"
        Log.d("Mhdflix-Search", "API URL: $searchUrl")
        
        return try {
            val response = app.get(
                searchUrl,
                headers = mapOf(
                    "User-Agent" to baseHeaders.getValue("User-Agent"),
                    "Referer" to "$mainUrl/search?q=${query.replace(" ", "%20")}",
                    "Accept" to "application/json, text/plain, */*"
                )
            ).text
            
            Log.d("Mhdflix-Search", "Response: ${response.take(500)}")
            
            val searchResponse = parseJson<SearchApiResponse>(response)
            val results = searchResponse.data.mapNotNull { item ->
                val slug = item.slug ?: item.title?.lowercase()?.replace(Regex("[^a-z0-9]+"), "-")?.trim('-') ?: return@mapNotNull null
                val type = item.type ?: "tv"
                val url = if (type == "movie") "$mainUrl/movies/${item.id}/$slug" else "$mainUrl/tvs/${item.id}/$slug"
                val tvType = if (type == "movie") TvType.Movie else TvType.TvSeries
                
                Log.d("Mhdflix-Search", "  Result: title='${item.title}', poster='${item.poster}', url='$url'")
                
                newMovieSearchResponse(item.title ?: "", url, tvType) {
                    this.posterUrl = fixUrlPath(item.poster)
                }
            }
            
            Log.d("Mhdflix-Search", "=== search END - returning ${results.size} results ===")
            results
        } catch (e: Exception) {
            Log.e("Mhdflix-Search", "Error: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("Mhdflix-Load", "=== load START - url: $url ===")
        
        val html = app.get(url, headers = baseHeaders).text
        Log.d("Mhdflix-Load", "HTML length: ${html.length}")

        if (url.contains("/episode/")) {
            return loadEpisodePage(url, html)
        }

        val doc = Jsoup.parse(html)
        
        val title = doc.selectFirst("meta[property='og:title']")?.attr("content")
            ?: doc.selectFirst("title")?.text()?.substringBefore("|")?.trim()
            ?: "Sin título"
        
        val poster = doc.selectFirst("meta[property='og:image']")?.attr("content") ?: ""
        val description = doc.selectFirst("meta[property='og:description']")?.attr("content")
            ?: doc.selectFirst("meta[name='description']")?.attr("content")
            ?: ""
        
        val idFromUrl = Regex("""/(?:movies|tvs)/(\d+)/""").find(url)?.groupValues?.get(1)?.toLongOrNull()
        val typeFromUrl = if (url.contains("/movies/")) "movie" else "tv"
        
        Log.d("Mhdflix-Load", "title: $title, poster: $poster, idFromUrl: $idFromUrl, type: $typeFromUrl")

        val tvType = if (typeFromUrl == "movie") TvType.Movie else TvType.TvSeries

        if (tvType == TvType.Movie) {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
            }
        } else {
            Log.d("Mhdflix-Load", "DEBUG HTML snippet (searching for season/episode):")
            val seasonRelated = doc.select("[class*='season'], [class*='episode'], [class*='temporada'], select, option, button[data-season], .server")
            Log.d("Mhdflix-Load", "Found ${seasonRelated.size} season/episode related elements")
            seasonRelated.take(5).forEach { el ->
                Log.d("Mhdflix-Load", "  Element: tag=${el.tagName()}, class=${el.className()}, text=${el.text().take(50)}, outerHtml=${el.outerHtml().take(200)}")
            }
            
            val allSelects = doc.select("select")
            Log.d("Mhdflix-Load", "All selects on page: ${allSelects.size}")
            allSelects.forEach { sel ->
                Log.d("Mhdflix-Load", "  Select: name=${sel.attr("name")}, id=${sel.attr("id")}, class=${sel.className()}, options=${sel.select("option").size}")
                sel.select("option").take(3).forEach { opt ->
                    Log.d("Mhdflix-Load", "    Option: value=${opt.attr("value")}, text=${opt.text()}")
                }
            }
            val episodes = mutableListOf<Episode>()
            val slug = Regex("""/tvs/\d+/([^/?]+)""").find(url)?.groupValues?.get(1)
            
            val seasonSelect = doc.selectFirst("select[name*='season'], select[id*='season'], select[class*='season']")
            
            if (seasonSelect != null) {
                val options = seasonSelect.select("option")
                Log.d("Mhdflix-Load", "Season select found with ${options.size} options")
                
                for (option in options) {
                    val seasonValue = option.attr("value")
                    if (seasonValue.isBlank()) continue
                    
                    val seasonNum = seasonValue.toIntOrNull() ?: continue
                    val seasonUrl = "$mainUrl/tvs/${idFromUrl}/${slug}?seasson=$seasonValue"
                    Log.d("Mhdflix-Load", "Loading season $seasonNum from: $seasonUrl")
                    
                    val seasonHtml = app.get(seasonUrl, headers = baseHeaders).text
                    val seasonDoc = Jsoup.parse(seasonHtml)
                    val episodeLinks = seasonDoc.select("a[href*='/episode/']")
                    
                    Log.d("Mhdflix-Load", "Season $seasonNum: ${episodeLinks.size} episode links found")
                    
                    for (epLink in episodeLinks) {
                        val epHref = epLink.attr("href")
                        val epId = Regex("""/episode/(\d+)""").find(epHref)?.groupValues?.get(1)?.toLongOrNull() ?: continue
                        val epTitle = epLink.text().trim()
                        val epUrl = if (epHref.startsWith("http")) epHref else "$mainUrl$epHref"
                        
                        Log.d("Mhdflix-Load", "  Episode: id=$epId, title='$epTitle'")
                        
                        episodes.add(newEpisode(epUrl) {
                            this.name = epTitle.ifBlank { "Episodio $epId" }
                            this.season = seasonNum
                            this.episode = episodes.count { it.season == seasonNum } + 1
                        })
                    }
                }
            } else {
                Log.d("Mhdflix-Load", "No season select found, looking for episodes directly")
                val episodeLinks = doc.select("a[href*='/episode/']")
                Log.d("Mhdflix-Load", "Direct episode links: ${episodeLinks.size}")
                
                for (epLink in episodeLinks) {
                    val epHref = epLink.attr("href")
                    val epId = Regex("""/episode/(\d+)""").find(epHref)?.groupValues?.get(1)?.toLongOrNull() ?: continue
                    val epTitle = epLink.text().trim()
                    val epUrl = if (epHref.startsWith("http")) epHref else "$mainUrl$epHref"
                    
                    episodes.add(newEpisode(epUrl) {
                        this.name = epTitle.ifBlank { "Episodio $epId" }
                        this.season = 1
                        this.episode = episodes.size + 1
                    })
                }
            }

            Log.d("Mhdflix-Load", "Total episodes: ${episodes.size}")
            
            if (episodes.isNotEmpty()) {
                return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                    this.posterUrl = poster
                    this.plot = description
                }
            } else {
                Log.d("Mhdflix-Load", "No episodes found, returning single episode")
                return newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(newEpisode(url) {
                    this.name = title
                })) {
                    this.posterUrl = poster
                    this.plot = description
                }
            }
        }
    }

    private suspend fun loadEpisodePage(url: String, html: String): LoadResponse? {
        Log.d("Mhdflix-EpPage", "=== loadEpisodePage - url: $url ===")
        
        val doc = Jsoup.parse(html)
        val episodeTitle = doc.selectFirst("meta[property='og:title']")?.attr("content")
            ?: doc.selectFirst("h1")?.text()
            ?: "Episodio"
        
        val description = doc.selectFirst("meta[property='og:description']")?.attr("content") ?: ""
        val poster = doc.selectFirst("meta[property='og:image']")?.attr("content") ?: ""
        
        val epId = Regex("""/episode/(\d+)""").find(url)?.groupValues?.get(1)
        Log.d("Mhdflix-EpPage", "episodeTitle: $episodeTitle, epId: $epId")

        val serieSlug = Regex("""/tvs/\d+/([^/]+)""").find(url)?.groupValues?.get(1)
        val serieId = Regex("""/tvs/(\d+)/""").find(url)?.groupValues?.get(1)
        val serieUrl = if (serieId != null && serieSlug != null) "$mainUrl/tvs/$serieId/$serieSlug" else url

        return newTvSeriesLoadResponse(episodeTitle, serieUrl, TvType.TvSeries, listOf(newEpisode(url) {
            this.name = episodeTitle
            this.season = 1
            this.episode = 1
            this.description = description.takeIf { it.isNotBlank() }
            this.posterUrl = poster
        })) {
            this.posterUrl = poster
            this.plot = description.takeIf { it.isNotBlank() }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("Mhdflix-Links", "=== loadLinks START - data: $data ===")
        
        val contentInfo = extractContentInfo(data)
        if (contentInfo == null) {
            Log.e("Mhdflix-Links", "Could not extract ID from: $data")
            return false
        }
        
        val (id, type) = contentInfo
        val apiUrl = "$mainUrl/api/links?id=$id&type=$type"
        Log.d("Mhdflix-Links", "API URL: $apiUrl")
        
        return try {
            val response = app.get(
                apiUrl,
                headers = mapOf(
                    "User-Agent" to baseHeaders.getValue("User-Agent"),
                    "Referer" to "$mainUrl/",
                    "Accept" to "application/json, text/plain, */*"
                )
            ).text
            
            Log.d("Mhdflix-Links", "Response: $response")
            
            when {
                response.trim().startsWith("[") -> {
                    val links = parseJson<List<ApiLink>>(response)
                    processApiLinks(links, subtitleCallback, callback)
                }
                response.trim().startsWith("{") -> {
                    val singleLink = tryParseJson<ApiLink>(response)
                    if (singleLink != null) {
                        processApiLinks(listOf(singleLink), subtitleCallback, callback)
                    } else {
                        false
                    }
                }
                else -> {
                    val doc = Jsoup.parse(response)
                    val iframes = doc.select("iframe[src]")
                    var found = false
                    for (iframe in iframes) {
                        val src = iframe.attr("src")
                        if (src.isNotBlank() && !src.contains("undefined")) {
                            loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                            found = true
                        }
                    }
                    found
                }
            }
        } catch (e: Exception) {
            Log.e("Mhdflix-Links", "API error: ${e.message}", e)
            val html = app.get(data, headers = baseHeaders).text
            val doc = Jsoup.parse(html)
            var found = false
            for (iframe in doc.select("iframe[src]")) {
                val src = iframe.attr("src")
                if (src.isNotBlank() && !src.contains("undefined")) {
                    loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                    found = true
                }
            }
            found
        }
    }

    private suspend fun processApiLinks(
        links: List<ApiLink>,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var found = false
        Log.d("Mhdflix-Links", "Processing ${links.size} links")
        
        for ((index, link) in links.withIndex()) {
            val videoUrl = link.url ?: link.embedUrl ?: link.iframeUrl
            Log.d("Mhdflix-Links", "Link[$index]: url=$videoUrl")
            
            if (videoUrl.isNullOrBlank() || videoUrl.contains("undefined")) continue
            
            val serverName = link.server?.name ?: link.serverName ?: "Server"
            val languageName = link.language?.name ?: link.languageName ?: "Latino"
            val qualityName = link.quality?.name ?: ""
            val linkName = "$serverName - $languageName"
            
            if (videoUrl.startsWith("http")) {
                val qualityValue = when {
                    qualityName.contains("4k", ignoreCase = true) || qualityName.contains("2160") -> Qualities.P2160.value
                    qualityName.contains("1080") -> Qualities.P1080.value
                    qualityName.contains("720") -> Qualities.P720.value
                    qualityName.contains("480") -> Qualities.P480.value
                    else -> Qualities.Unknown.value
                }
                
                val isM3u8 = videoUrl.contains(".m3u8") || videoUrl.contains("m3u8")
                val linkType = if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                
                callback.invoke(
                    newExtractorLink(name, linkName, videoUrl) {
                        this.referer = mainUrl
                        this.quality = qualityValue
                        this.type = linkType
                    }
                )
                found = true
            } else {
                loadExtractor(fixUrl(videoUrl), mainUrl, subtitleCallback, callback)
                found = true
            }
            
            link.subtitles?.forEach { sub ->
                sub.url?.let { url ->
                    subtitleCallback.invoke(
                        newSubtitleFile(sub.name ?: languageName, fixUrl(url))
                    )
                }
            }
        }
        
        Log.d("Mhdflix-Links", "processApiLinks - found=$found")
        return found
    }

    private fun extractContentInfo(data: String): Pair<String, String>? {
        Regex("""/movies/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "movie")
        }
        Regex("""/tvs/episode/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "episode")
        }
        Regex("""/tvs/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "episode")
        }
        return null
    }

    data class SearchItem(
        val id: Long,
        val title: String,
        val poster: String,
        val url: String,
        val type: String
    )

    data class SearchApiResponse(
        @JsonProperty("data") val data: List<SearchResultItem> = emptyList(),
        @JsonProperty("totalPage") val totalPage: Int? = null,
        @JsonProperty("status") val status: Int? = null
    )

    data class SearchResultItem(
        @JsonProperty("id") val id: Long? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("poster") val poster: String? = null,
        @JsonProperty("backdrop") val backdrop: String? = null,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("slug") val slug: String? = null,
        @JsonProperty("description") val description: String? = null,
        @JsonProperty("genre") val genre: List<String?> = emptyList(),
        @JsonProperty("date") val date: String? = null,
        @JsonProperty("duration") val duration: String? = null,
        @JsonProperty("rating") val rating: Int? = null
    )

    data class ApiLink(
        @JsonProperty("url") val url: String? = null,
        @JsonProperty("embedUrl") val embedUrl: String? = null,
        @JsonProperty("iframeUrl") val iframeUrl: String? = null,
        @JsonProperty("language") val language: ApiLanguage? = null,
        @JsonProperty("languageName") val languageName: String? = null,
        @JsonProperty("quality") val quality: ApiQuality? = null,
        @JsonProperty("server") val server: ApiServer? = null,
        @JsonProperty("serverName") val serverName: String? = null,
        @JsonProperty("subtitles") val subtitles: List<ApiSubtitle>? = null
    )

    data class ApiLanguage(@JsonProperty("name") val name: String? = null)
    data class ApiQuality(@JsonProperty("name") val name: String? = null)
    data class ApiServer(@JsonProperty("name") val name: String? = null)
    data class ApiSubtitle(
        @JsonProperty("url") val url: String? = null,
        @JsonProperty("name") val name: String? = null
    )
}
