package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

class LamovieProvider : MainAPI() {
    override var mainUrl = "https://la.movie"
    override var name = "LaMovie"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    private val apiBase = "$mainUrl/wp-api/v1"
    private val TAG = "LaMovie"

    override val mainPage = mainPageOf(
        "$apiBase/posts?postType=tvshows" to "Series",
        "$apiBase/posts?postType=movies" to "Películas",
        "$apiBase/posts?postType=any" to "Agregados Recientemente"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}&postsPerPage=20&page=$page"
        val res = app.get(url).text
        if (!res.trim().startsWith("{")) return newHomePageResponse(request.name, emptyList(), false)

        val json = parseJson<ApiResponse>(res)
        val home = json.data?.posts?.map { it.toSearchResult() } ?: emptyList()

        return newHomePageResponse(
            list = HomePageList(request.name, home, isHorizontalImages = false),
            hasNext = json.data?.pagination?.nextPageUrl != null
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$apiBase/search?postType=any&q=${query.replace(" ", "+")}&postsPerPage=26"
        val res = app.get(url).text
        if (!res.trim().startsWith("{")) return emptyList()

        val json = parseJson<ApiResponse>(res)
        return json.data?.posts?.map { it.toSearchResult() } ?: emptyList()
    }

    private fun Post.toSearchResult(): SearchResponse {
        val poster = if (images?.poster?.startsWith("/") == true) "$mainUrl${images.poster}" else images?.poster
        return if (type == "movies") {
            newMovieSearchResponse(title ?: "", "$mainUrl/movies/$slug", TvType.Movie) {
                this.posterUrl = poster
            }
        } else {
            newTvSeriesSearchResponse(title ?: "", "$mainUrl/series/$slug", TvType.TvSeries) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.split("/").last()
        val type = if (url.contains("/series/")) "tvshows" else "movies"

        val apiUrl = "$apiBase/single/$type?slug=$slug&postType=$type"
        println("$TAG: Cargando detalles desde -> $apiUrl")

        val res = app.get(apiUrl).text
        if (!res.trim().startsWith("{")) return null

        val postData = parseJson<SinglePostResponse>(res).data ?: return null

        val title = postData.title ?: ""
        val poster = if (postData.images?.poster?.startsWith("/") == true) "$mainUrl${postData.images.poster}" else postData.images?.poster
        val backdrop = if (postData.images?.backdrop?.startsWith("/") == true) "$mainUrl${postData.images.backdrop}" else postData.images?.backdrop

        return if (type == "movies") {
            newMovieLoadResponse(title, url, TvType.Movie, postData.id.toString()) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = postData.overview
                this.year = postData.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                addTrailer(postData.trailer)
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, emptyList()) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = postData.overview
                this.year = postData.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val masterUrl = "https://p3.vimeos.zip/hls2/03/00007/p8h98thufoby_h/master.m3u8?t=C2JCqeI0GLk1xuUOJc6zqlsldcPOPv07l5LriqYZNro&s=1769651287&e=43200"

        callback.invoke(
            newExtractorLink(
                source = this.name,
                name = "$name Server",
                url = masterUrl,
                type = ExtractorLinkType.M3U8
            ) {
                this.quality = Qualities.Unknown.value
                this.referer = "https://vimeos.net/"
            }
        )

        val subList = listOf(
            "Español" to "spa",
            "English" to "eng",
            "Português" to "por"
        )

        subList.forEach { (name, langCode) ->
            val subUrl = "https://s1.vimeos.net/vtt/03/00007/p8h98thufoby_$langCode.vtt"
            subtitleCallback.invoke(newSubtitleFile(name, subUrl))
        }

        return true
    }

    data class ApiResponse(val data: DataContainer?)
    data class SinglePostResponse(val data: Post?) 
    data class DataContainer(val posts: List<Post>?, val pagination: Pagination?)
    data class Pagination(@JsonProperty("next_page_url") val nextPageUrl: String?)
    data class Post(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val slug: String?,
        val type: String?,
        val overview: String?,
        val images: Images?,
        val trailer: String?,
        @JsonProperty("release_date") val releaseDate: String?,
        @JsonProperty("latest_episode") val latestEpisode: LatestEpisode?
    )
    data class Images(val poster: String?, val backdrop: String?)
    data class LatestEpisode(val season: Int?, val episode: Int?)
}