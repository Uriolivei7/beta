package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer

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
        "$apiBase/posts?postType=tvshows&postsPerPage=20" to "Series",
        "$apiBase/posts?postType=movies&postsPerPage=20" to "Películas",
        "$apiBase/posts?postType=any&postsPerPage=20" to "Agregados Recientemente"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}&page=$page"
        println("$TAG: Cargando MainPage -> $url")
        val json = app.get(url).parsed<ApiResponse>()

        val home = json.data?.posts?.map { it.toSearchResult() } ?: emptyList()
        println("$TAG: Se encontraron ${home.size} posts")

        return newHomePageResponse(
            list = HomePageList(request.name, home, isHorizontalImages = false),
            hasNext = json.data?.pagination?.nextPageUrl != null
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$apiBase/search?postType=any&q=$query&postsPerPage=26"
        println("$TAG: Buscando -> $url")
        val json = app.get(url).parsed<ApiResponse>()

        return json.data?.posts?.map { it.toSearchResult() } ?: emptyList()
    }

    private fun Post.toSearchResult(): SearchResponse {
        val poster = if (images?.poster?.startsWith("http") == true) images.poster
        else "https://image.tmdb.org/t/p/w500${images?.poster}"

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

    override suspend fun load(url: String): LoadResponse {
        println("$TAG: Iniciando carga de info para -> $url")
        val slug = url.split("/").last()
        val type = if (url.contains("/series/")) "tvshows" else "movies"

        val postData = app.get("$apiBase/posts/$slug").parsed<SinglePostResponse>().data
        val castUrl = "$apiBase/cast/$type/${postData?.id}"
        println("$TAG: Cargando cast desde -> $castUrl")

        val castData = try {
            app.get(castUrl).parsed<CastResponse>().data
        } catch(e: Exception) {
            println("$TAG: Error cargando cast: ${e.message}")
            null
        }

        val title = postData?.title ?: ""
        val poster = "https://image.tmdb.org/t/p/w500${postData?.images?.poster}"
        val backdrop = "https://image.tmdb.org/t/p/original${postData?.images?.backdrop}"

        val cast = castData?.map {
            ActorData(
                Actor(it.termName ?: "", "https://image.tmdb.org/t/p/w185${it.meta?.profilePath}"),
                role = ActorRole.Main
            )
        }

        return if (type == "movies") {
            newMovieLoadResponse(title, url, TvType.Movie, postData?.id.toString()) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = postData?.overview
                this.year = postData?.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                this.actors = cast
                addTrailer(postData?.trailer)
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, emptyList()) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = postData?.overview
                this.actors = cast
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        println("$TAG: Intentando cargar links para ID -> $data")

        val masterUrl = "https://p3.vimeos.zip/hls2/03/00007/p8h98thufoby_h/master.m3u8?t=C2JCqeI0GLk1xuUOJc6zqlsldcPOPv07l5LriqYZNro&s=1769651287&e=43200"

        callback.invoke(
            newExtractorLink(
                source = this.name,
                name = "$name Streaming",
                url = masterUrl,
                type = ExtractorLinkType.M3U8
            ) {
                this.quality = Qualities.Unknown.value
                this.referer = "https://vimeos.net/"
            }
        )

        subtitleCallback.invoke(
            newSubtitleFile("Español", "https://s1.vimeos.net/vtt/03/00007/p8h98thufoby_spa.vtt")
        )

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
        @JsonProperty("release_date") val releaseDate: String?
    )
    data class Images(val poster: String?, val backdrop: String?)
    data class CastResponse(val data: List<CastMember>?)
    data class CastMember(
        @JsonProperty("term_name") val termName: String?,
        val meta: CastMeta?
    )
    data class CastMeta(
        @JsonProperty("profile_path") val profilePath: String?,
        val character: String?
    )
}