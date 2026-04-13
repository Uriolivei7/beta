package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class PandramaProvider:MainAPI() {
    companion object {
        private const val BASE_URL = "https://www.pandrama.tv"
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

    data class BootstrapData(
        @JsonProperty("loaders") var loaders: LoadersData? = null
    )

    data class LoadersData(
        @JsonProperty("channelPage") var channelPage: ChannelPageData? = null,
        @JsonProperty("titlePage") var titlePage: TitlePageData? = null,
        @JsonProperty("searchPage") var searchPage: SearchPageData? = null
    )

    data class ChannelPageData(
        @JsonProperty("channel") var channel: ChannelData? = null
    )

    data class ChannelData(
        @JsonProperty("content") var content: ContentData? = null
    )

    data class ContentData(
        @JsonProperty("data") var data: ArrayList<ChannelItemData>? = null
    )

    data class ChannelItemData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("slug") var slug: String? = null,
        @JsonProperty("content") var content: ChannelContentData? = null
    )

    data class ChannelContentData(
        @JsonProperty("data") var data: ArrayList<TitleData>? = null
    )

    data class TitleData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("slug") var slug: String? = null,
        @JsonProperty("release_date") var releaseDate: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("backdrop") var backdrop: String? = null,
        @JsonProperty("is_series") var isSeries: Boolean? = null,
        @JsonProperty("model_type") var modelType: String? = null,
        @JsonProperty("rating") var rating: Double? = null,
        @JsonProperty("primary_video") var primaryVideo: PrimaryVideoData? = null
    )

    data class PrimaryVideoData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null
    )

    data class TitlePageData(
        @JsonProperty("title") var title: TitleInfo? = null,
        @JsonProperty("episodes") var episodes: EpisodesData? = null
    )

    data class SearchPageData(
        @JsonProperty("results") var results: ArrayList<TitleData>? = null,
        @JsonProperty("trendingTitles") var trendingTitles: ArrayList<TitleData>? = null
    )

    data class TitleInfo(
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
        @JsonProperty("available_seasons") var availableSeasons: List<Int>? = null,
        @JsonProperty("videos") var videos: List<VideoData>? = null
    )

    data class GenreData(
        @JsonProperty("name") var name: String? = null
    )

    data class VideoData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("src") var src: String? = null,
        @JsonProperty("category") var category: String? = null,
        @JsonProperty("quality") var quality: String? = null,
        @JsonProperty("language") var language: String? = null
    )

    data class EpisodesData(
        @JsonProperty("data") var data: List<EpisodeInfo>? = null
    )

    data class EpisodeInfo(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("episode_number") var episodeNumber: Int? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("primary_video") var primaryVideo: PrimaryVideoData? = null
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

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()

        try {
            val sections = listOf(
                "Dramas" to "/dramas",
                "Películas" to "/peliculas"
            )

            for ((displayName, endpoint) in sections) {
                try {
                    val html = app.get("$mainUrl$endpoint").text

                    val scriptMatch = Regex(
                        """<script>\s*window\.bootstrapData\s*=\s*(.+?);\s*</script>""",
                        RegexOption.DOT_MATCHES_ALL
                    ).find(html)
                        ?: continue

                    val jsonStr = scriptMatch.groupValues[1]
                    val bootstrap = parseJson<BootstrapData>(jsonStr)

                    val channelContent = bootstrap.loaders?.channelPage?.channel?.content?.data

                    if (channelContent != null && channelContent.isNotEmpty()) {
                        val allTitles = channelContent.mapNotNull { it.content?.data }.flatten()
                        val titleItems = allTitles.take(15).map { info ->
                            val titleId = info.id
                            val titleSlug = info.slug
                            if (titleId != null && titleSlug != null) {
                                newTvSeriesSearchResponse(info.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                                    this.posterUrl = getImageUrl(info.poster)
                                }
                            } else {
                                newTvSeriesSearchResponse(info.name ?: "Unknown", mainUrl) {
                                    this.posterUrl = getImageUrl(info.poster)
                                }
                            }
                        }
                        if (titleItems.isNotEmpty()) {
                            items.add(HomePageList(displayName, titleItems))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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
            e.printStackTrace()
        }

        if (items.isEmpty()) throw ErrorLoadingException()
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val search = ArrayList<SearchResponse>()
        try {
            val searchUrl = "$mainUrl/search/$query"
            val html = app.get(searchUrl).text
            
            val scriptMatch = Regex("""<script>\s*window\.bootstrapData\s*=\s*(.+?);\s*</script>""", RegexOption.DOT_MATCHES_ALL).find(html)
            if (scriptMatch != null) {
                val jsonStr = scriptMatch.groupValues[1]
                val bootstrap = try {
                    parseJson<BootstrapData>(jsonStr)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return search
                }
                
                val results = bootstrap.loaders?.searchPage?.results
                results?.forEach { title ->
                    val titleId = title.id
                    val titleSlug = title.slug
                    if (titleId != null && titleSlug != null) {
                        search.add(newTvSeriesSearchResponse(title.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                            this.posterUrl = getImageUrl(title.poster)
                        })
                    } else {
                        search.add(newTvSeriesSearchResponse(title.name ?: "Unknown", mainUrl) {
                            this.posterUrl = getImageUrl(title.poster)
                        })
                    }
                }
                
                val trending = bootstrap.loaders?.searchPage?.trendingTitles
                trending?.forEach { title ->
                    val titleId = title.id
                    val titleSlug = title.slug
                    if (titleId != null && titleSlug != null && !search.any { it.url == "$mainUrl/titulo/$titleId/$titleSlug" }) {
                        search.add(newTvSeriesSearchResponse(title.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                            this.posterUrl = getImageUrl(title.poster)
                        })
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return search
    }

    override suspend fun load(url: String): LoadResponse? {
        try {
            val html = app.get(url).text
            
            val scriptMatch = Regex("""<script>\s*window\.bootstrapData\s*=\s*(.+?);\s*</script>""", RegexOption.DOT_MATCHES_ALL).find(html)
                ?: return null

            val jsonStr = scriptMatch.groupValues[1]
            val bootstrap = try {
                parseJson<BootstrapData>(jsonStr)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

            val titleInfo = bootstrap.loaders?.titlePage?.title
            if (titleInfo == null) return null
            
            val episodes = bootstrap.loaders?.titlePage?.episodes?.data ?: emptyList()

            val title = titleInfo.name ?: ""
            val description = titleInfo.description
            val poster = getImageUrl(titleInfo.poster)
            val backdrop = getImageUrl(titleInfo.backdrop)
            val year = titleInfo.year
            val rating = titleInfo.rating
            val isSeries = titleInfo.isSeries == true || titleInfo.availableSeasons?.isNotEmpty() == true

            val genres = titleInfo.genres?.mapNotNull { it.name } ?: emptyList()
            val status = getStatus(titleInfo.status)

            if (isSeries) {
                val episodeList = episodes.map { episode ->
                    newEpisode(episode.id.toString()) {
                        this.name = episode.name
                        this.episode = episode.episodeNumber
                        this.season = episode.seasonNumber ?: 1
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
            e.printStackTrace()
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
            val html = app.get(data).text
            
            val scriptMatch = Regex("""<script>\s*window\.bootstrapData\s*=\s*(.+?);\s*</script>""", RegexOption.DOT_MATCHES_ALL).find(html)
                ?: return false

            val jsonStr = scriptMatch.groupValues[1]
            val bootstrap = try {
                parseJson<BootstrapData>(jsonStr)
            } catch (e: Exception) {
                return false
            }

            val titleInfo = bootstrap.loaders?.titlePage?.title
            val episodes = bootstrap.loaders?.titlePage?.episodes?.data ?: return false

            if (episodes.isNotEmpty()) {
                val firstEpisode = episodes.firstOrNull { it.primaryVideo != null } ?: episodes.firstOrNull()
                val videoId = firstEpisode?.primaryVideo?.id
                
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
            e.printStackTrace()
            false
        }
    }
}