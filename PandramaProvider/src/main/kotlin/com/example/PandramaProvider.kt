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
        @JsonProperty("episodePage") var episodePage: EpisodePageData? = null,
        @JsonProperty("titlePage") var titlePage: TitlePageData? = null,
        @JsonProperty("searchPage") var searchPage: SearchPageData? = null,
        @JsonProperty("settings") var settings: Any? = null,
        @JsonProperty("i18n") var i18n: Any? = null,
        @JsonProperty("themes") var themes: Any? = null,
        @JsonProperty("language") var language: String? = null,
        @JsonProperty("user") var user: Any? = null,
        @JsonProperty("csrf_token") var csrfToken: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class LoadersData(
        @JsonProperty("channelPage") var channelPage: ChannelPageData? = null,
        @JsonProperty("titlePage") var titlePage: TitlePageData? = null,
        @JsonProperty("searchPage") var searchPage: SearchPageData? = null,
        @JsonProperty("episodePage") var episodePage: EpisodePageData? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ChannelPageData(
        @JsonProperty("channel") var channel: ChannelData? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ChannelData(
        @JsonProperty("content") var content: ContentData? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EpisodePageData(
        @JsonProperty("title") var title: TitleDetailData? = null,
        @JsonProperty("episode") var episode: EpisodeWithVideos? = null,
        @JsonProperty("current_video") var currentVideo: ApiVideoData? = null,
        @JsonProperty("alternative_videos") var alternativeVideos: List<ApiVideoData>? = null,
        @JsonProperty("loader") var loader: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EpisodeApiResponse(
        @JsonProperty("title") var title: TitleDetailData? = null,
        @JsonProperty("episode") var episode: ApiEpisodeData? = null,
        @JsonProperty("loader") var loader: String? = null,
        @JsonProperty("current_video") var currentVideo: ApiVideoData? = null,
        @JsonProperty("alternative_videos") var alternativeVideos: List<ApiVideoData>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ApiEpisodeData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("description") var description: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("episode_number") var episodeNumber: Int? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("videos") var videos: List<ApiVideoData>? = null,
        @JsonProperty("primary_video") var primaryVideo: PrimaryVideoApiData? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ApiVideoData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("src") var src: String? = null,
        @JsonProperty("category") var category: String? = null,
        @JsonProperty("type") var type: String? = null,
        @JsonProperty("clearkey") var clearKey: String? = null,
        @JsonProperty("quality") var quality: String? = null,
        @JsonProperty("captions") var captions: List<CaptionData>? = null
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
            
            val titleId: Int?
            val seasonNum: Int?
            val episodeId: Int?
            
            // Parse URL to get title/season/episode IDs
            when {
                data.contains("/titulo/") && data.contains("/temporada/") && data.contains("/episodio/") -> {
                    val parts = data.split("/")
                    titleId = parts.getOrNull(parts.indexOf("titulo") + 1)?.toIntOrNull()
                    seasonNum = parts.getOrNull(parts.indexOf("temporada") + 1)?.toIntOrNull()
                    episodeId = parts.getOrNull(parts.indexOf("episodio") + 1)?.toIntOrNull()
                }
                data.contains("/episodio/") -> {
                    episodeId = data.substringAfterLast("/episodio/").trim().toIntOrNull()
                    titleId = null
                    seasonNum = null
                }
                data.substringAfterLast("/").trim().all { it.isDigit() } -> {
                    episodeId = data.substringAfterLast("/").trim().toIntOrNull()
                    titleId = null
                    seasonNum = null
                }
                else -> {
                    episodeId = null
                    titleId = null
                    seasonNum = null
                }
            }
            
            Log.d(TAG, "loadLinks: titleId=$titleId, seasonNum=$seasonNum, episodeId=$episodeId")
            
            // Try different API formats
            val apiUrls = mutableListOf<String>()
            
            if (episodeId != null) {
                // Try various API endpoint formats
                if (titleId != null && seasonNum != null) {
                    apiUrls.add("$mainUrl/api/v1/titles/$titleId/seasons/$seasonNum/episodes/$episodeId")
                    apiUrls.add("$mainUrl/api/v1/titles/$titleId/seasons/$seasonNum/episodes/$episodeId?loader=episodePage")
                    apiUrls.add("$mainUrl/api/episodes/$episodeId")
                    apiUrls.add("$mainUrl/episodio/$episodeId")
                }
                apiUrls.add("$mainUrl/api/v1/episodes/$episodeId")
                apiUrls.add("$mainUrl/api/v1/episodes/$episodeId?loader=episodePage")
            }
            
            for (apiUrl in apiUrls) {
                Log.d(TAG, "loadLinks: trying API: $apiUrl")
                try {
                    val jsonResponse = app.get(apiUrl, headers = mapOf(
                        "Accept" to "application/json",
                        "X-Requested-With" to "XMLHttpRequest"
                    )).text
                    if (jsonResponse.startsWith("<")) {
                        Log.d(TAG, "loadLinks: got HTML response, not JSON")
                        continue
                    }
                    val episodeResponse = parseJson<EpisodeApiResponse>(jsonResponse)
                    Log.d(TAG, "loadLinks: got API response, episode=${episodeResponse.episode?.name}")
                    
                    val episode = episodeResponse.episode
                    if (episode != null) {
                        // Process subtitles from current_video
                        episodeResponse.currentVideo?.captions?.forEach { caption ->
                            val subUrl = caption.url
                            if (!subUrl.isNullOrEmpty()) {
                                val fullSubUrl = if (subUrl.startsWith("/")) "$mainUrl$subUrl" else subUrl
                                val lang = caption.language ?: "unknown"
                                val name = caption.name ?: lang
                                Log.d(TAG, "loadLinks: adding subtitle $name from $fullSubUrl")
                                subtitleCallback(SubtitleFile(name, fullSubUrl))
                            }
                        }
                        
                        // Get videos
                        val videos = episode.videos ?: emptyList()
                        Log.d(TAG, "loadLinks: videos count=${videos.size}")
                        
                        for (video in videos) {
                            val videoSrc = video.src
                            if (videoSrc.isNullOrEmpty()) continue
                            
                            Log.d(TAG, "loadLinks: video src=$videoSrc, type=${video.type}, quality=${video.quality}")
                            
                            when {
                                videoSrc.contains(".mpd") || video.type == "shaka" -> {
                                    val clearKey = video.clearKey
                                    if (clearKey != null) {
                                        // Try to extract key and pass to extractor
                                        loadExtractor(videoSrc, data, subtitleCallback, callback)
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
                        
                        // Try alternative videos
                        episodeResponse.alternativeVideos?.forEach { altVideo ->
                            val videoSrc = altVideo.src
                            if (videoSrc.isNullOrEmpty()) return@forEach
                            
                            Log.d(TAG, "loadLinks: alt video src=$videoSrc, type=${altVideo.type}")
                            
                            // Add subtitles from alternative videos
                            altVideo.captions?.forEach { caption ->
                                val subUrl = caption.url
                                if (!subUrl.isNullOrEmpty()) {
                                    val fullSubUrl = if (subUrl.startsWith("/")) "$mainUrl$subUrl" else subUrl
                                    val lang = caption.language ?: "unknown"
                                    val name = caption.name ?: lang
                                    subtitleCallback(SubtitleFile(name, fullSubUrl))
                                }
                            }
                            
                            when {
                                videoSrc.contains("youtube.com") || videoSrc.contains("youtu.be") -> {
                                    loadExtractor(videoSrc, data, subtitleCallback, callback)
                                    return true
                                }
                                videoSrc.startsWith("http") && !videoSrc.contains(".mpd") -> {
                                    loadExtractor(videoSrc, data, subtitleCallback, callback)
                                    return true
                                }
                            }
                        }
                        
                        // If we got a valid response (even with no videos), don't try more APIs
                        return true
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "loadLinks: API error: ${e.message}")
                }
            }
            
            // Fallback: try HTML parsing
            Log.d(TAG, "loadLinks: trying HTML parsing for: $data")
            val html = app.get(data).text
            
            // Debug: print first 500 chars of JSON
            val jsonStart = html.indexOf("window.bootstrapData")
            if (jsonStart != -1) {
                val scriptStart = html.lastIndexOf("<script>", jsonStart)
                val scriptEnd = html.indexOf("</script>", scriptStart)
                val jsonStr = html.substring(scriptStart + 8, scriptEnd).trim()
                val jsonContent = jsonStr.removePrefix("window.bootstrapData =").trim().trimEnd(';')
                Log.d(TAG, "loadLinks: JSON sample: ${jsonContent.take(500)}")
            }
            
            val bootstrap = parseBootstrapData(html)
            
            Log.d(TAG, "loadLinks: bootstrap null=${bootstrap == null}")
            Log.d(TAG, "loadLinks: loaders null=${bootstrap?.loaders == null}")
            Log.d(TAG, "loadLinks: episodePage (loaders) null=${bootstrap?.loaders?.episodePage == null}")
            Log.d(TAG, "loadLinks: episodePage (direct) null=${bootstrap?.episodePage == null}")
            Log.d(TAG, "loadLinks: titlePage null=${bootstrap?.loaders?.titlePage == null}")
            
            // Try to get video from episodePage -> currentVideo (checks both formats)
            val episodePage = bootstrap?.loaders?.episodePage ?: bootstrap?.episodePage
            
            if (episodePage?.currentVideo != null) {
                val video = episodePage.currentVideo
                val videoSrc = video?.src
                Log.d(TAG, "loadLinks: found currentVideo src=$videoSrc")
                
                if (!videoSrc.isNullOrEmpty()) {
                    video?.captions?.forEach { caption ->
                        val subUrl = caption.url
                        if (!subUrl.isNullOrEmpty()) {
                            val fullSubUrl = if (subUrl.startsWith("/")) "$mainUrl$subUrl" else subUrl
                            val name = caption.name ?: caption.language ?: "unknown"
                            Log.d(TAG, "loadLinks: adding subtitle $name")
                            subtitleCallback(newSubtitleFile(name, fullSubUrl))
                        }
                    }
                    
                    when {
                        videoSrc.contains(".mpd") || video?.type == "shaka" -> {
                            loadExtractor(videoSrc, data, subtitleCallback, callback)
                            return true
                        }
                        videoSrc.startsWith("http") -> {
                            loadExtractor(videoSrc, data, subtitleCallback, callback)
                            return true
                        }
                    }
                }
            }
            
            // Fallback to alternative videos
            episodePage?.alternativeVideos?.forEach { video ->
                val videoSrc = video.src
                if (videoSrc.isNullOrEmpty()) return@forEach
                Log.d(TAG, "loadLinks: trying alternative video src=$videoSrc")
                
                video.captions?.forEach { caption ->
                    val subUrl = caption.url
                    if (!subUrl.isNullOrEmpty()) {
                        val fullSubUrl = if (subUrl.startsWith("/")) "$mainUrl$subUrl" else subUrl
                        val name = caption.name ?: caption.language ?: "unknown"
                        subtitleCallback(newSubtitleFile(name, fullSubUrl))
                    }
                }
                
                if (videoSrc.startsWith("http") && !videoSrc.contains(".mpd")) {
                    loadExtractor(videoSrc, data, subtitleCallback, callback)
                    return true
                }
            }
            
            Log.d(TAG, "loadLinks: no video found in bootstrap data, returning false")
            return false
        } catch (e: Exception) {
            Log.d(TAG, "loadLinks error: ${e.message}")
            false
        }
    }
}