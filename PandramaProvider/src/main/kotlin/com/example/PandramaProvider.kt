package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink

class PandramaProvider : MainAPI() {
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
        @JsonProperty("loaders") var loaders: LoadersData? = null
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
        @JsonProperty("channel") var channel: ChannelItem? = null
    )

    data class ChannelItem(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("content") var content: ChannelContent? = null
    )

    data class ChannelContent(
        @JsonProperty("data") var data: List<TitleItem>? = null
    )

    data class TitleItem(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("slug") var slug: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("is_series") var isSeries: Boolean? = null,
        @JsonProperty("episodes_count") var episodesCount: Int? = null,
        @JsonProperty("rating") var rating: Double? = null,
        @JsonProperty("status") var status: String? = null
    )

    data class SearchPageData(
        @JsonProperty("results") var results: List<TitleItem>? = null,
        @JsonProperty("trendingTitles") var trendingTitles: List<TitleItem>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TitlePageData(
        @JsonProperty("title") var title: TitleDetail? = null,
        @JsonProperty("episodes") var episodes: EpisodesList? = null,
        @JsonProperty("available_seasons") var availableSeasons: List<Int>? = null
    )

    data class EpisodesList(
        @JsonProperty("data") var data: List<EpisodeItem>? = null
    )

    data class EpisodeItem(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("description") var description: String? = null,
        @JsonProperty("episode_number") var episodeNumber: Int? = null,
        @JsonProperty("season_number") var seasonNumber: Int? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("runtime") var runtime: Int? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TitleDetail(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("description") var description: String? = null,
        @JsonProperty("poster") var poster: String? = null,
        @JsonProperty("backdrop") var backdrop: String? = null,
        @JsonProperty("runtime") var runtime: Int? = null,
        @JsonProperty("year") var year: Int? = null,
        @JsonProperty("rating") var rating: Double? = null,
        @JsonProperty("genres") var genres: List<GenreItem>? = null,
        @JsonProperty("status") var status: String? = null,
        @JsonProperty("is_series") var isSeries: Boolean? = null,
        @JsonProperty("seasons_count") var seasonsCount: Int? = null,
        @JsonProperty("alternative_title") var alternativeTitle: String? = null
    )

    data class GenreItem(
        @JsonProperty("label") var label: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EpisodePageData(
        @JsonProperty("title") var title: TitleDetail? = null,
        @JsonProperty("current_video") var currentVideo: VideoData? = null,
        @JsonProperty("alternative_videos") var alternativeVideos: List<VideoData>? = null,
        @JsonProperty("loader") var loader: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class VideoData(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("src") var src: String? = null,
        @JsonProperty("type") var type: String? = null,
        @JsonProperty("quality") var quality: String? = null,
        @JsonProperty("language") var language: String? = null,
        @JsonProperty("captions") var captions: List<CaptionItem>? = null
    )

    data class CaptionItem(
        @JsonProperty("id") var id: Int? = null,
        @JsonProperty("url") var url: String? = null,
        @JsonProperty("name") var name: String? = null,
        @JsonProperty("language") var language: String? = null
    )

    private fun getImageUrl(link: String?): String? {
        if (link.isNullOrEmpty()) return null
        return when {
            link.startsWith("http://") || link.startsWith("https://") -> link
            link.startsWith("/storage") -> "$BASE_URL$link"
            link.startsWith("/") -> "https://image.tmdb.org/t/p/w500$link"
            else -> "https://image.tmdb.org/t/p/w500/$link"
        }
    }

    private fun getStatus(status: String?): ShowStatus? {
        return when (status?.lowercase()) {
            "ongoing" -> ShowStatus.Ongoing
            "ended" -> ShowStatus.Completed
            "released" -> ShowStatus.Completed
            else -> null
        }
    }

    private fun parseBootstrapData(html: String): BootstrapData? {
        try {
            val startIdx = html.indexOf("window.bootstrapData = ")
            if (startIdx == -1) {
                Log.d(TAG, "No bootstrapData found")
                return null
            }

            val braceStart = html.indexOf('{', startIdx)
            if (braceStart == -1) return null

            var depth = 0
            var endIdx = -1
            for (i in braceStart until html.length) {
                val c = html[i]
                if (c == '{') depth++
                else if (c == '}') {
                    depth--
                    if (depth == 0) { endIdx = i; break }
                }
            }
            if (endIdx == -1) return null

            val jsonStr = html.substring(braceStart, endIdx + 1)
            return parseJson<BootstrapData>(jsonStr)
        } catch (e: Exception) {
            Log.d(TAG, "parseBootstrapData error: ${e.message}")
            return null
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()

        try {
            val sections = listOf(
                "Recientes" to "/dramas",
                "Películas" to "/peliculas"
            )

            for ((displayName, endpoint) in sections) {
                try {
                    Log.d(TAG, "Fetching $endpoint")
                    val html = app.get("$mainUrl$endpoint").text
                    val bootstrap = parseBootstrapData(html) ?: continue

                    val channel = bootstrap.loaders?.channelPage?.channel ?: continue
                    val titles = channel.content?.data?.filter {
                        it.id != null && it.name != null
                    }?.take(20) ?: continue

                    val searchItems = titles.mapNotNull { info ->
                        val slug = info.slug ?: info.name?.lowercase()?.replace(" ", "-")?.replace(Regex("[^a-z0-9-]"), "") ?: return@mapNotNull null
                        val url = "$mainUrl/titulo/${info.id}/$slug"
                        val isMovie = info.isSeries == false
                        if (isMovie) {
                            newMovieSearchResponse(info.name ?: "Unknown", url, TvType.AsianDrama) {
                                this.posterUrl = getImageUrl(info.poster)
                            }
                        } else {
                            newAnimeSearchResponse(info.name ?: "Unknown", url, TvType.AsianDrama) {
                                this.posterUrl = getImageUrl(info.poster)
                            }
                        }
                    }

                        if (searchItems.isNotEmpty()) {
                        items.add(HomePageList(channel.name ?: displayName, searchItems))
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "Section error: ${e.message}")
                }
            }

            if (items.isEmpty()) throw ErrorLoadingException()
        } catch (e: Exception) {
            Log.d(TAG, "getMainPage error: ${e.message}")
            if (items.isEmpty()) throw ErrorLoadingException()
        }

        return newHomePageResponse(items, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val search = ArrayList<SearchResponse>()
        try {
            val searchUrl = "$mainUrl/search/${java.net.URLEncoder.encode(query, "utf-8")}"
            val html = app.get(searchUrl).text
            val bootstrap = parseBootstrapData(html) ?: return search

            val results = bootstrap.loaders?.searchPage?.results
            results?.forEach { title ->
                val id = title.id ?: return@forEach
                val slug = title.slug ?: title.name?.lowercase()?.replace(" ", "-")?.replace(Regex("[^a-z0-9-]"), "") ?: return@forEach
                val url = "$mainUrl/titulo/$id/$slug"
                val isMovie = title.isSeries == false
                search.add(if (isMovie)
                    newMovieSearchResponse(title.name ?: "Unknown", url, TvType.AsianDrama) { this.posterUrl = getImageUrl(title.poster) }
                else
                    newAnimeSearchResponse(title.name ?: "Unknown", url, TvType.AsianDrama) { this.posterUrl = getImageUrl(title.poster) }
                )
            }

            if (search.isEmpty()) {
                bootstrap.loaders?.searchPage?.trendingTitles?.forEach { title ->
                    val id = title.id ?: return@forEach
                    val slug = title.slug ?: title.name?.lowercase()?.replace(" ", "-")?.replace(Regex("[^a-z0-9-]"), "") ?: return@forEach
                    val url = "$mainUrl/titulo/$id/$slug"
                    if (search.none { it.url == url }) {
                        search.add(newAnimeSearchResponse(title.name ?: "Unknown", url, TvType.AsianDrama) {
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
            Log.d(TAG, "load: $url")

            val html = app.get(url).text
            val bootstrap = parseBootstrapData(html) ?: return null

            val titleInfo = bootstrap.loaders?.titlePage?.title
            val episodePageTitle = bootstrap.loaders?.episodePage?.title
            val detail = titleInfo ?: episodePageTitle ?: return null

            val title = detail.name ?: ""
            val description = detail.description
            val poster = getImageUrl(detail.poster)
            val backdrop = getImageUrl(detail.backdrop)
            val year = detail.year
            val rating = detail.rating
            val genres = detail.genres?.mapNotNull { it.label } ?: emptyList()
            val status = getStatus(detail.status)

            val titleId = detail.id ?: return null
            val slug = url.substringAfter("/titulo/").substringAfter("/").substringBefore("/")
            val baseUrl = "$mainUrl/titulo/$titleId/$slug"

            val episodes = bootstrap.loaders?.titlePage?.episodes?.data
            if (episodes != null && episodes.isNotEmpty()) {
                val episodeList = episodes.map { ep ->
                    val epNum = ep.episodeNumber ?: 1
                    val seasonNum = ep.seasonNumber ?: 1
                    newEpisode("$baseUrl/temporada/$seasonNum/episodio/$epNum") {
                        this.name = ep.name
                        this.episode = epNum
                        this.season = seasonNum
                        this.posterUrl = getImageUrl(ep.poster)
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
            }

            return newMovieLoadResponse(title, url, TvType.AsianDrama, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = description
                this.tags = genres
                this.year = year
                this.score = rating?.let { Score.from10(it) }
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
            Log.d(TAG, "loadLinks: $data")

            val html = app.get(data).text
            val bootstrap = parseBootstrapData(html) ?: return false

            val episodePage = bootstrap.loaders?.episodePage ?: return false

            val mainVideo = episodePage.currentVideo
            val altVideos = episodePage.alternativeVideos ?: emptyList()
            val allVideos = if (mainVideo != null) listOf(mainVideo) + altVideos else altVideos

            if (allVideos.isEmpty()) return false

                    var found = false
            for (video in allVideos) {
                val src = video.src ?: continue
                val cleanSrc = src.replace("\\/", "/")
                Log.d(TAG, "Video type=${video.type}, src=$cleanSrc")

                video.captions?.forEach { caption ->
                    val captionUrl = caption.url ?: return@forEach
                    val fullUrl = if (captionUrl.startsWith("/")) "$mainUrl$captionUrl" else captionUrl
                    subtitleCallback.invoke(newSubtitleFile(caption.name ?: caption.language ?: "Español", fullUrl))
                }

                try {
                    val langSuffix = when (video.language) {
                        "es", "es-419", "es-ES", "es-MX" -> " (Latino)"
                        "en", "en-US" -> " (ENG)"
                        "ko" -> " (KO)"
                        "ja" -> " (JP)"
                        "zh" -> " (CN)"
                        else -> if (video.language != null) " (${video.language})" else ""
                    }
                    val linkName = "${video.name ?: video.type ?: "Video"}$langSuffix"
                    val isDirectPlay = cleanSrc.endsWith(".m3u8") || cleanSrc.endsWith(".mpd")
                    when {
                        isDirectPlay -> {
                            callback.invoke(
                                newExtractorLink(
                                    linkName,
                                    linkName,
                                    cleanSrc
                                ) {
                                    this.referer = "$mainUrl/"
                                    this.quality = getQualityFromName(video.quality ?: "720p")
                                }
                            )
                            found = true
                        }
                        video.type == "embed" || video.type == "url" -> {
                            try {
                                val embedHtml = app.get(cleanSrc, headers = mapOf("User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36")).text
                                val videoUrlRegex = Regex("""https?://[^"'\s<>]+\.(?:m3u8|mp4|mpd)[^"'\s<>]*""")
                                val match = videoUrlRegex.find(embedHtml)
                                if (match != null) {
                                    callback.invoke(newExtractorLink(linkName, linkName, match.value) {
                                        this.referer = cleanSrc
                                        this.quality = getQualityFromName(video.quality ?: "720p")
                                    })
                                    found = true
                                } else {
                                    found = loadExtractor(cleanSrc, data, subtitleCallback, callback) || found
                                }
                            } catch (e: Exception) {
                                found = loadExtractor(cleanSrc, data, subtitleCallback, callback) || found
                            }
                        }
                        else -> {
                            found = loadExtractor(cleanSrc, data, subtitleCallback, callback) || found
                        }
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Video error for $cleanSrc: ${e.message}")
                }
            }

            found
        } catch (e: Exception) {
            Log.d(TAG, "loadLinks error: ${e.message}")
            false
        }
    }
}
