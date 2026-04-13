package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor

class PandramaProvider:MainAPI() {
    companion object {
        private const val BASE_URL = "https://www.pandrama.tv"
        private const val TAG = "PandramaProvider"
    }

    override var mainUrl = BASE_URL
    override var name = "Pandrama"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.AsianDrama,
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BootstrapData(
        @JsonProperty("loaders") var loaders: LoadersData? = null,
        @JsonProperty("settings") var settings: Any? = null,
        @JsonProperty("i18n") var i18n: Any? = null,
        @JsonProperty("themes") var themes: Any? = null,
        @JsonProperty("language") var language: String? = null,
        @JsonProperty("user") var user: Any? = null,
        @JsonProperty("csrf_token") var csrfToken: String? = null
    )

    data class LoadersData(
        @JsonProperty("channelPage") var channelPage: ChannelPageData? = null,
        @JsonProperty("titlePage") var titlePage: TitlePageData? = null,
        @JsonProperty("searchPage") var searchPage: SearchPageData? = null,
        @JsonProperty("episodePage") var episodePage: EpisodePageData? = null
    )

    data class ChannelPageData(
        @JsonProperty("channel") var channel: ChannelData? = null
    )

    data class ChannelData(
        @JsonProperty("content") var content: ContentData? = null
    )

    data class ContentData(
        @JsonProperty("data") var data: List<ChannelItemData>? = null
    )

    data class ChannelItemData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("slug") var slug: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("content") var content: ChannelContentData? = null
    )

    data class ChannelContentData(
        @JsonProperty("data") var data: List<TitleApiData>? = null
    )

    data class TitleApiData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("slug") var slug: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("backdrop") var backdrop: String? = null,
        @JsonProperty("is_series") var isSeries: Boolean? = null,
        @JsonProperty("rating") var rating: Double? = null,
        @JsonProperty("primary_video") var primaryVideo: PrimaryVideoApiData? = null
    )

    data class PrimaryVideoApiData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("videos") var videos: List<VideoApiData>? = null
    )

    data class SearchPageData(
        @JsonProperty("results") var results: List<TitleApiData>? = null,
        @JsonProperty("trendingTitles") var trendingTitles: List<TitleApiData>? = null
    )

    data class TitlePageData(
        @JsonProperty("title") var title: TitleDetailData? = null,
        @JsonProperty("episodes") var episodes: EpisodesApiData? = null,
        @JsonProperty("available_seasons") var availableSeasons: List<Int>? = null
    )

    data class EpisodePageData(
        @JsonProperty("title") var title: TitleDetailData? = null,
        @JsonProperty("episode") var episode: EpisodeWithVideos? = null,
        @JsonProperty("loader") var loader: String? = null
    )

    data class EpisodeWithVideos(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("episode_number") var episodeNumber: Int? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("videos") var videos: List<VideoApiData>? = null,
        @JsonProperty("primary_video") var primaryVideo: PrimaryVideoApiData? = null
    )

    data class TitleDetailData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("description") var description: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("backdrop") var backdrop: String? = null,
        @JsonProperty("runtime") var runtime: Int? = null,
        @JsonProperty("year") var year: Int? = null,
        @JsonProperty("rating") var rating: Double? = null,
        @JsonProperty("genres") var genres: List<GenreData>? = null,
        @JsonProperty("status") var status: String? = null,
        @JsonProperty("is_series") var isSeries: Boolean? = null,
        @JsonProperty("videos") var videos: List<VideoApiData>? = null,
        @JsonProperty("available_seasons") var availableSeasons: List<Int>? = null
    )

    data class GenreData(
        @JsonProperty("name") var name: String? = null
    )

    data class VideoApiData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("src") var src: String? = null,
        @JsonProperty("category") var category: String? = null,
        @JsonProperty("type") var type: String? = null,
        @JsonProperty("clearkey") var clearKey: String? = null,
        @JsonProperty("quality") var quality: String? = null,
        @JsonProperty("captions") var captions: List<CaptionData>? = null
    )

    data class CaptionData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("language") var language: String? = null,
        @JsonProperty("url") var url: String? = null
    )

    data class EpisodesApiData(
        @JsonProperty("data") var data: List<EpisodeApiData>? = null
    )

    data class EpisodeApiData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("episode_number") var episodeNumber: Int? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("primary_video") var primaryVideo: PrimaryVideoApiData? = null
    )

    private fun getImageUrl(link: String?): String? {
        if (link.isNullOrEmpty()) return null
        return when {
            link.startsWith("http://") || link.startsWith("https://") -> link
            link.startsWith("/storage") -> "https://www.pandrama.tv$link"
            link.startsWith("/") -> "https://image.tmdb.org/t/p/w500$link"
            else -> "https://image.tmdb.org/t/p/w500/$link"
        }
    }

    private fun getStatus(status: String?): ShowStatus? {
        return when (status?.lowercase()) {
            "ongoing", "on_air", "airing" -> ShowStatus.Ongoing
            "ended", "completed" -> ShowStatus.Completed
            else -> null
        }
    }

    private fun parseBootstrapData(html: String): BootstrapData? {
        try {
            val startIdx = html.indexOf("window.bootstrapData")
            if (startIdx == -1) {
                Log.d(TAG, "No bootstrapData found in HTML")
                return null
            }
            
            val scriptStart = html.lastIndexOf("<script>", startIdx)
            val scriptEnd = html.indexOf("</script>", scriptStart)
            if (scriptStart == -1 || scriptEnd == -1) {
                Log.d(TAG, "No script tags found")
                return null
            }
            
            val jsonStr = html.substring(scriptStart + 8, scriptEnd).trim()
            val jsonContent = jsonStr.removePrefix("window.bootstrapData =").trim().trimEnd(';')
            
            Log.d(TAG, "parseBootstrapData: JSON length=${jsonContent.length}")
            
            val bootstrap = parseJson<BootstrapData>(jsonContent)
            Log.d(TAG, "parseBootstrapData: success, loaders=${bootstrap.loaders != null}")
            
            return bootstrap
        } catch (e: Exception) {
            Log.d(TAG, "parseBootstrapData error: ${e.message}")
            return null
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()

        try {
            Log.d(TAG, "getMainPage: Starting")
            
            val sections = listOf(
                "Dramas" to "/dramas",
                "Películas" to "/peliculas"
            )

            for ((displayName, endpoint) in sections) {
                try {
                    Log.d(TAG, "getMainPage: Fetching $mainUrl$endpoint")
                    val html = app.get("$mainUrl$endpoint").text
                    Log.d(TAG, "getMainPage: Got HTML, length=${html.length}")
                    
                    val bootstrap = parseBootstrapData(html) ?: continue
                    
                    val channelContent = bootstrap.loaders?.channelPage?.channel?.content?.data
                    Log.d(TAG, "getMainPage: channelContent=${channelContent?.size}")
                    
                    if (channelContent != null && channelContent.isNotEmpty()) {
                        val allTitles = ArrayList<TitleApiData>()
                        
                        for (item in channelContent) {
                            if (item.id != null && item.name != null) {
                                allTitles.add(
                                    TitleApiData(
                                        id = item.id,
                                        name = item.name,
                                        slug = item.slug,
                                        poster = item.poster,
                                        isSeries = null,
                                        primaryVideo = null
                                    )
                                )
                            }
                            item.content?.data?.let { nested ->
                                allTitles.addAll(nested.filter { it.id != null && it.name != null })
                            }
                        }
                        
                        Log.d(TAG, "getMainPage: allTitles=${allTitles.size}")
                        
                        val titleItems = allTitles.take(15).map { info ->
                            val titleId = info.id
                            val titleSlug = info.slug ?: info.name?.lowercase()?.replace(" ", "-")?.replace(Regex("[^a-z0-9-]"), "") ?: "unknown"
                            newTvSeriesSearchResponse(info.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                                this.posterUrl = getImageUrl(info.poster)
                            }
                        }
                        if (titleItems.isNotEmpty()) {
                            items.add(HomePageList(displayName, titleItems))
                        }
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "getMainPage error: ${e.message}")
                }
            }

            if (items.isEmpty()) {
                items.add(HomePageList("Contenido", listOf(
                    newTvSeriesSearchResponse("Cargando...", mainUrl) {
                        this.posterUrl = null
                    }
                )))
            }
        } catch (e: Exception) {
            Log.d(TAG, "getMainPage outer error: ${e.message}")
        }

        if (items.isEmpty()) throw ErrorLoadingException()
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val search = ArrayList<SearchResponse>()
        try {
            Log.d(TAG, "search: query=$query")
            
            val searchUrl = "$mainUrl/search/$query"
            val html = app.get(searchUrl).text
            Log.d(TAG, "search: Got HTML, length=${html.length}")
            
            val bootstrap = parseBootstrapData(html) ?: return search
            
            val results = bootstrap.loaders?.searchPage?.results
            Log.d(TAG, "search: results=${results?.size}")
            
            results?.forEach { title ->
                val titleId = title.id
                val titleSlug = title.slug ?: title.name?.lowercase()?.replace(" ", "-")?.replace(Regex("[^a-z0-9-]"), "") ?: "unknown"
                if (titleId != null) {
                    search.add(newTvSeriesSearchResponse(title.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                        this.posterUrl = getImageUrl(title.poster)
                    })
                }
            }
            
            if (search.isEmpty()) {
                val trending = bootstrap.loaders?.searchPage?.trendingTitles
                trending?.forEach { title ->
                    val titleId = title.id
                    val titleSlug = title.slug ?: title.name?.lowercase()?.replace(" ", "-")?.replace(Regex("[^a-z0-9-]"), "") ?: "unknown"
                    if (titleId != null && !search.any { it.url == "$mainUrl/titulo/$titleId/$titleSlug" }) {
                        search.add(newTvSeriesSearchResponse(title.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                            this.posterUrl = getImageUrl(title.poster)
                        })
                    }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "search error: ${e.message}")
        }
        return search
    }

    override suspend fun load(url: String): LoadResponse? {
        try {
            Log.d(TAG, "load: url=$url")
            
            val html = app.get(url).text
            Log.d(TAG, "load: Got HTML, length=${html.length}")
            
            val bootstrap = parseBootstrapData(html) ?: return null
            
            val titleInfo = bootstrap.loaders?.titlePage?.title
            Log.d(TAG, "load: titleInfo=${titleInfo?.name}")
            
            if (titleInfo == null) return null

            val title = titleInfo.name ?: ""
            val description = titleInfo.description
            val poster = getImageUrl(titleInfo.poster)
            val backdrop = getImageUrl(titleInfo.backdrop)
            val year = titleInfo.year
            val rating = titleInfo.rating
            val titlePage = bootstrap.loaders?.titlePage
            val isSeries = titleInfo.isSeries == true || titlePage?.availableSeasons?.isNotEmpty() == true

            val genres = titleInfo.genres?.mapNotNull { it.name } ?: emptyList()
            val status = getStatus(titleInfo.status)

            if (isSeries) {
                val episodes = titlePage?.episodes?.data ?: emptyList()
                Log.d(TAG, "load: episodes=${episodes.size}")
                
                val episodeList = episodes.map { episode ->
                    val seasonNum = episode.seasonNumber ?: 1
                    newEpisode("$mainUrl/titulo/${titleInfo.id}/temporada/$seasonNum/episodio/${episode.id}") {
                        this.name = episode.name
                        this.episode = episode.episodeNumber
                        this.season = seasonNum
                        this.posterUrl = getImageUrl(episode.poster)
                    }
                }

                return newTvSeriesLoadResponse(title, url, TvType.AsianDrama, episodeList) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backdrop
                    this.plot = description
                    this.tags = genres
                    this.year = year
                    this.score = rating?.let { Score.from10(it) }
                    this.showStatus = status
                }
            } else {
                return newMovieLoadResponse(title, url, TvType.Movie, url) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backdrop
                    this.plot = description
                    this.tags = genres
                    this.year = year
                    this.score = rating?.let { Score.from10(it) }
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "load error: ${e.message}")
        }
        return null
    }

override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        return try {
            Log.d(TAG, "loadLinks: data=$data")
            
            var html: String
            var bootstrap: BootstrapData?
            
            // Handle different URL formats
            when {
                data.contains("/episodio/") -> {
                    Log.d(TAG, "loadLinks: handling /episodio/ URL")
                    html = app.get(data).text
                    bootstrap = parseBootstrapData(html)
                    Log.d(TAG, "loadLinks: parsed bootstrap, loaders present=${bootstrap?.loaders != null}")
                }
                data.contains("/titulo/") || data.contains("/temporada/") -> {
                    html = app.get(data).text
                    bootstrap = parseBootstrapData(html)
                    
                    // Try to get episode from URL if it's an episode link
                    if (data.contains("/episodio/")) {
                        val episodeId = data.substringAfterLast("/episodio/").trim()
                        if (episodeId.all { it.isDigit() }) {
                            html = app.get("$mainUrl/episodio/$episodeId").text
                            bootstrap = parseBootstrapData(html)
                        }
                    }
                }
                data.substringAfterLast("/").trim().all { it.isDigit() } -> {
                    // Just a number, try as episode ID
                    val episodeId = data.substringAfterLast("/").trim()
                    Log.d(TAG, "loadLinks: trying as episode ID $episodeId")
                    html = app.get("$mainUrl/episodio/$episodeId").text
                    bootstrap = parseBootstrapData(html)
                }
                else -> {
                    html = app.get(data).text
                    bootstrap = parseBootstrapData(html)
                }
            }
            
            if (bootstrap == null) {
                Log.d(TAG, "loadLinks: bootstrap is null, returning false")
                return false
            }
            
            val episodeData = bootstrap.loaders?.episodePage?.episode
            
            Log.d(TAG, "loadLinks: episodePage=${bootstrap.loaders?.episodePage != null}, episodeData=$episodeData")
            
            Log.d(TAG, "loadLinks: episodeData=${episodeData?.name}, videos=${episodeData?.videos?.size}")
            
            if (episodeData != null) {
                val videos = episodeData.videos ?: emptyList()
                
                for (video in videos) {
                    val videoSrc = video.src
                    if (videoSrc.isNullOrEmpty()) continue
                    
                    Log.d(TAG, "loadLinks: trying video src=$videoSrc, type=${video.type}")
                    
                    video.captions?.forEach { caption ->
                        val subUrl = caption.url
                        if (!subUrl.isNullOrEmpty()) {
                            val fullSubUrl = if (subUrl.startsWith("/")) "$mainUrl$subUrl" else subUrl
                            val lang = caption.language ?: "unknown"
                            val name = caption.name ?: lang
                            Log.d(TAG, "loadLinks: adding subtitle $name from $fullSubUrl")
                            subtitleCallback(SubtitleFile(name, fullSubUrl))
                        }
                    }
                    
                    when {
                        videoSrc.contains(".mpd") || video.type == "shaka" -> {
                            val clearKey = video.clearKey
                            if (clearKey != null) {
                                val keyUrl = "$videoSrc?clearkey=$clearKey"
                                loadExtractor(keyUrl, data, subtitleCallback, callback)
                            } else {
                                loadExtractor(videoSrc, data, subtitleCallback, callback)
                            }
                            return true
                        }
                        videoSrc.contains("youtube.com") || videoSrc.contains("youtu.be") -> {
                            loadExtractor(videoSrc, data, subtitleCallback, callback)
                            return true
                        }
                        videoSrc.startsWith("http") -> {
                            loadExtractor(videoSrc, data, subtitleCallback, callback)
                            return true
                        }
                    }
                }
                
                val primaryVideoId = episodeData.primaryVideo?.id
                if (primaryVideoId != null) {
                    val videoApiUrl = "$mainUrl/api/videos/$primaryVideoId"
                    Log.d(TAG, "loadLinks: trying primary video API: $videoApiUrl")
                    loadExtractor(videoApiUrl, data, subtitleCallback, callback)
                    return true
                }
            }
            
            val titleInfo = bootstrap.loaders?.titlePage?.title
            val episodes = bootstrap.loaders?.titlePage?.episodes?.data
            
            Log.d(TAG, "loadLinks: title page episodes=${episodes?.size}")

            if (!episodes.isNullOrEmpty()) {
                val firstEpisode = episodes.firstOrNull { it.primaryVideo != null } ?: episodes.firstOrNull()
                val videoId = firstEpisode?.primaryVideo?.id
                
                Log.d(TAG, "loadLinks: videoId=$videoId")
                
                if (videoId != null) {
                    loadExtractor("$mainUrl/api/videos/$videoId", data, subtitleCallback, callback)
                    return true
                }
            }

            titleInfo?.videos?.forEach { video ->
                if (video.category == "full" || video.category == "trailer") {
                    if (video.src?.contains("youtube") == true || video.src?.contains("embed") == true) {
                        loadExtractor(video.src!!, data, subtitleCallback, callback)
                    }
                }
            }

            true
        } catch (e: Exception) {
            Log.d(TAG, "loadLinks error: ${e.message}")
            false
        }
    }
}