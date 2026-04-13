package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor

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

    data class ApiResponse(
        @JsonProperty("titles") var titles: ArrayList<TitleApiData>? = null,
        @JsonProperty("status") var status: String? = null
    )

    data class TitleApiData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("type") var type: String? = null,
        @JsonProperty("release_date") var releaseDate: String? = null,
        @JsonProperty("description") var description: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("backdrop") var backdrop: String? = null,
        @JsonProperty("runtime") var runtime: Int? = null,
        @JsonProperty("year") var year: Int? = null,
        @JsonProperty("rating") var rating: Double? = null,
        @JsonProperty("genres") var genres: List<GenreData>? = null,
        @JsonProperty("status") var status: String? = null,
        @JsonProperty("is_series") var isSeries: Boolean? = null,
        @JsonProperty("primary_video") var primaryVideo: PrimaryVideoApiData? = null,
        @JsonProperty("videos") var videos: List<VideoApiData>? = null
    )

    data class GenreData(
        @JsonProperty("name") var name: String? = null
    )

    data class PrimaryVideoApiData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null
    )

    data class VideoApiData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("src") var src: String? = null,
        @JsonProperty("category") var category: String? = null,
        @JsonProperty("type") var type: String? = null
    )

    data class TitlePageResponse(
        @JsonProperty("title") var title: TitleDetailData? = null,
        @JsonProperty("episodes") var episodes: EpisodesApiData? = null,
        @JsonProperty("available_seasons") var availableSeasons: List<Int>? = null
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
        @JsonProperty("videos") var videos: List<VideoApiData>? = null
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

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()

        try {
            val sections = listOf(
                "Dramas" to "dramas",
                "Películas" to "peliculas"
            )

            for ((displayName, type) in sections) {
                try {
                    val response = app.get("$mainUrl/api/v1/titles?type=$type&perPage=15&page=1").text
                    val apiResponse = parseJson<ApiResponse>(response)

                    val titles = apiResponse.titles
                    if (titles != null && titles.isNotEmpty()) {
                        val titleItems = titles.map { info ->
                            val titleId = info.id
                            val titleSlug = info.name?.lowercase()?.replace(" ", "-") ?: "unknown"
                            if (titleId != null) {
                                newTvSeriesSearchResponse(info.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                                    this.posterUrl = getImageUrl(info.poster)
                                }
                            } else {
                                newTvSeriesSearchResponse(info.name ?: "Unknown", mainUrl) {
                                    this.posterUrl = getImageUrl(info.poster)
                                }
                            }
                        }
                        items.add(HomePageList(displayName, titleItems))
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
            val response = app.get("$mainUrl/api/v1/titles?search=$query&perPage=20").text
            val apiResponse = parseJson<ApiResponse>(response)

            apiResponse.titles?.forEach { title ->
                val titleId = title.id
                val titleSlug = title.name?.lowercase()?.replace(" ", "-") ?: "unknown"
                if (titleId != null) {
                    search.add(newTvSeriesSearchResponse(title.name ?: "Unknown", "$mainUrl/titulo/$titleId/$titleSlug") {
                        this.posterUrl = getImageUrl(title.poster)
                    })
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return search
    }

    override suspend fun load(url: String): LoadResponse? {
        try {
            val titleId = url.substringAfter("/titulo/").substringBefore("/").toIntOrNull() ?: return null

            val response = app.get("$mainUrl/api/v1/titles/$titleId?loader=titlePage").text
            val titlePage = parseJson<TitlePageResponse>(response)

            val titleInfo = titlePage.title ?: return null

            val title = titleInfo.name ?: ""
            val description = titleInfo.description
            val poster = getImageUrl(titleInfo.poster)
            val backdrop = getImageUrl(titleInfo.backdrop)
            val year = titleInfo.year
            val rating = titleInfo.rating
            val isSeries = titleInfo.isSeries == true || titlePage.availableSeasons?.isNotEmpty() == true

            val genres = titleInfo.genres?.mapNotNull { it.name } ?: emptyList()
            val status = getStatus(titleInfo.status)

            if (isSeries) {
                val episodes = titlePage.episodes?.data ?: emptyList()
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
            val titleId = data.substringAfter("/titulo/").substringBefore("/").toIntOrNull() ?: return false

            val response = app.get("$mainUrl/api/v1/titles/$titleId?loader=titlePage").text
            val titlePage = parseJson<TitlePageResponse>(response)

            val titleInfo = titlePage.title
            val episodes = titlePage.episodes?.data

            if (!episodes.isNullOrEmpty()) {
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