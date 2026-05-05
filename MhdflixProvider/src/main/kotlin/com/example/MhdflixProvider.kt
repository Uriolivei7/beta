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
    override var lang = "mx"
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
        "peliculas" to "Películas Recientes",
        "series" to "Series Recientes",
        "anime" to "Animes Recientes",
        "dorama" to "Doramas Recientes",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d("Mhdflix-MainPage", "=== getMainPage START ===")
        Log.d("Mhdflix-MainPage", "request.name: ${request.name}, category: ${request.data}")

        val searchTerms = when (request.data) {
            "peliculas" -> listOf("movie", "pelicula", "action", "2024")
            "series" -> listOf("series", "tv", "temporada", "2024")
            "anime" -> listOf("anime", "naruto", "dragon ball", "one piece")
            "dorama" -> listOf("dorama", "korean", "asia", "kdrama")
            else -> listOf("2024", "movie", "series")
        }

        val tvType = when (request.data) {
            "peliculas" -> TvType.Movie
            "anime" -> TvType.Anime
            "dorama" -> TvType.AsianDrama
            else -> TvType.TvSeries
        }

        val allItems = mutableMapOf<Long, SearchResultItem>()

        for (term in searchTerms) {
            if (allItems.size >= 30) break
            try {
                val searchUrl = "$mainUrl/api/search?query=${term.replace(" ", "+")}&page=1&limit=10"
                Log.d("Mhdflix-MainPage", "Searching: $searchUrl")
                
                val response = app.get(
                    searchUrl,
                    headers = mapOf(
                        "User-Agent" to baseHeaders.getValue("User-Agent"),
                        "Referer" to "$mainUrl/",
                        "Accept" to "application/json, text/plain, */*"
                    )
                ).text

                val searchResponse = tryParseJson<SearchApiResponse>(response)
                searchResponse?.data?.forEach { item ->
                    item.id?.let { id ->
                        if (!allItems.containsKey(id)) {
                            allItems[id] = item
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Mhdflix-MainPage", "Search error for '$term': ${e.message}")
            }
        }

        Log.d("Mhdflix-MainPage", "Total unique items found: ${allItems.size}")

        val searchResponses = allItems.values.take(30).mapNotNull { item ->
            val title = item.title ?: return@mapNotNull null
            val id = item.id ?: return@mapNotNull null
            val type = item.type ?: "tv"
            val slug = item.slug ?: return@mapNotNull null
            val url = if (type == "movie") "$mainUrl/movies/$id/$slug" else "$mainUrl/tvs/$id/$slug"
            
            Log.d("Mhdflix-MainPage", "  Item: title='$title', poster='${item.poster?.take(50)}'")
            
            newMovieSearchResponse(title, url, if (type == "movie") TvType.Movie else tvType) {
                this.posterUrl = fixUrlPath(item.poster)
            }
        }

        Log.d("Mhdflix-MainPage", "=== getMainPage END - returning ${searchResponses.size} items ===")

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
        
        if (url.contains("/episode/")) {
            return loadEpisodePage(url)
        }

        val html = app.get(url, headers = baseHeaders).text
        Log.d("Mhdflix-Load", "HTML length: ${html.length}")

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
            val episodes = loadEpisodesFromApi(idFromUrl)
            
            if (episodes.isNotEmpty()) {
                return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                    this.posterUrl = poster
                    this.plot = description
                }
            }
            
            Log.d("Mhdflix-Load", "No episodes found via API, returning single episode placeholder")
            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(newEpisode(url) {
                this.name = title
            })) {
                this.posterUrl = poster
                this.plot = description
            }
        }
    }

    private suspend fun loadEpisodesFromApi(tvId: Long?): List<Episode> {
        if (tvId == null) return emptyList()
        
        val apiHeaders = mapOf(
            "User-Agent" to baseHeaders.getValue("User-Agent"),
            "Referer" to "$mainUrl/",
            "Accept" to "application/json, text/plain, */*"
        )
        
        Log.d("Mhdflix-Load", "Fetching seasons for TV ID: $tvId")
        
        val seasonsResponse = try {
            val res = app.get("$mainUrl/api/series/$tvId/seasons", headers = apiHeaders).text
            Log.d("Mhdflix-Load", "Seasons response: ${res.take(300)}")
            tryParseJson<SeasonsApiResponse>(res)
        } catch (e: Exception) {
            Log.e("Mhdflix-Load", "Failed to fetch seasons: ${e.message}")
            null
        }
        
        if (seasonsResponse?.data.isNullOrEmpty()) {
            Log.d("Mhdflix-Load", "No seasons found")
            return emptyList()
        }
        
        val episodes = mutableListOf<Episode>()
        
        for (season in seasonsResponse.data) {
            val seasonId = season.id ?: continue
            val seasonNum = season.seasonNumber ?: continue
            val seasonName = season.name ?: "Temporada $seasonNum"
            
            Log.d("Mhdflix-Load", "Fetching episodes for season $seasonNum (id=$seasonId)")
            
            var page = 1
            var hasMore = true
            
            while (hasMore) {
                try {
                    val epUrl = "$mainUrl/api/series/$seasonId/episodes?page=$page"
                    val res = app.get(epUrl, headers = apiHeaders).text
                    
                    val epResponse = tryParseJson<EpisodesApiResponse>(res)
                    val seasonEpisodes = epResponse?.data ?: emptyList()
                    
                    Log.d("Mhdflix-Load", "  Season $seasonNum page $page: ${seasonEpisodes.size} episodes")
                    
                    for (ep in seasonEpisodes) {
                        val epId = ep.idEpisodios ?: continue
                        val epNum = ep.numEpisode ?: continue
                        val epTitle = ep.title?.takeIf { it.isNotBlank() } ?: "Episodio $epNum"
                        val epPoster = ep.posterPath?.takeIf { it.isNotBlank() }?.let { fixUrlPath(it) } ?: ""
                        
                        episodes.add(newEpisode("$mainUrl/tvs/episode/$epId") {
                            this.name = epTitle
                            this.season = seasonNum
                            this.episode = epNum
                            this.posterUrl = epPoster
                            this.description = ep.overview?.takeIf { it.isNotBlank() }
                        })
                    }
                    
                    hasMore = seasonEpisodes.isNotEmpty() && seasonEpisodes.size >= 20
                    page++
                } catch (e: Exception) {
                    Log.e("Mhdflix-Load", "Failed to fetch episodes page $page: ${e.message}")
                    hasMore = false
                }
            }
        }
        
        Log.d("Mhdflix-Load", "Total episodes loaded: ${episodes.size}")
        return episodes
    }

    private suspend fun loadEpisodePage(url: String): LoadResponse? {
        Log.d("Mhdflix-EpPage", "=== loadEpisodePage - url: $url ===")
        
        val epId = Regex("""/episode/(\d+)""").find(url)?.groupValues?.get(1)
        val epUrl = "$mainUrl/api/links?id=$epId&type=episode"
        
        var episodeTitle: String
        var description: String
        var poster: String
        
        try {
            val html = app.get(url, headers = baseHeaders).text
            val doc = Jsoup.parse(html)
            episodeTitle = doc.selectFirst("meta[property='og:title']")?.attr("content")
                ?: doc.selectFirst("title")?.text()?.substringBefore("|")?.trim()
                ?: "Episodio $epId"
            description = doc.selectFirst("meta[property='og:description']")?.attr("content") ?: ""
            poster = doc.selectFirst("meta[property='og:image']")?.attr("content") ?: ""
        } catch (e: Exception) {
            episodeTitle = "Episodio $epId"
            description = ""
            poster = ""
        }
        
        Log.d("Mhdflix-EpPage", "episodeTitle: $episodeTitle, epId: $epId")

        val serieMatch = Regex("""/tvs/(\d+)/([^/]+)""").find(url)
        val serieUrl = if (serieMatch != null) "$mainUrl/tvs/${serieMatch.groupValues[1]}/${serieMatch.groupValues[2]}" else url

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

    data class SeasonsApiResponse(
        @JsonProperty("data") val data: List<SeasonItem> = emptyList()
    )

    data class SeasonItem(
        @JsonProperty("id") val id: Long? = null,
        @JsonProperty("name") val name: String? = null,
        @JsonProperty("overview") val overview: String? = null,
        @JsonProperty("posterPath") val posterPath: String? = null,
        @JsonProperty("seasonNumber") val seasonNumber: Int? = null,
        @JsonProperty("isSerie") val isSerie: Long? = null
    )

    data class EpisodesApiResponse(
        @JsonProperty("data") val data: List<EpisodeItem> = emptyList()
    )

    data class EpisodeItem(
        @JsonProperty("idEpisodios") val idEpisodios: Long? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("overview") val overview: String? = null,
        @JsonProperty("airDate") val airDate: String? = null,
        @JsonProperty("numEpisode") val numEpisode: Int? = null,
        @JsonProperty("numSeason") val numSeason: Int? = null,
        @JsonProperty("posterPath") val posterPath: String? = null,
        @JsonProperty("mediaId") val mediaId: Long? = null
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
