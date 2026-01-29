package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

class LamovieProvider : MainAPI() {
    override var mainUrl = "https://la.movie"
    override var name = "La.Movie"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime)

    private val apiBase = "$mainUrl/wp-api/v1"
    private val TAG = "LaMovie"

    private fun fixImg(url: String?): String? {
        if (url.isNullOrBlank()) return null
        if (url.startsWith("//")) return "https:$url"
        if (!url.startsWith("http")) return "$mainUrl$url"
        return url
    }

    override val mainPage = mainPageOf(
        "$apiBase/listing/movies?postType=movies" to "Películas",
        "$apiBase/listing/tvshows?postType=tvshows" to "Series",
        "$apiBase/listing/animes?postType=animes" to "Animes",
        "$apiBase/posts?postType=any" to "Recientes"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}&page=$page&postsPerPage=12&orderBy=latest&order=DESC"
        println("$TAG: Cargando MainPage -> $url")

        val res = app.get(url).text
        if (!res.trim().startsWith("{")) return newHomePageResponse(request.name, emptyList(), false)

        val json = parseJson<ApiResponse>(res)
        val home = json.data?.posts?.map { it.toSearchResult() } ?: emptyList()

        return newHomePageResponse(
            list = HomePageList(request.name, home, isHorizontalImages = false),
            hasNext = home.isNotEmpty()
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
        val poster = fixImg(images?.poster)
        val typeStr = type ?: "movies"

        val tvType = when (typeStr) {
            "movies" -> TvType.Movie
            "animes" -> TvType.Anime
            else -> TvType.TvSeries
        }

        val path = when (typeStr) {
            "movies" -> "movies"
            "animes" -> "animes"
            else -> "series"
        }

        return if (tvType == TvType.Movie) {
            newMovieSearchResponse(title ?: "", "$mainUrl/$path/$slug", tvType) {
                this.posterUrl = poster
            }
        } else {
            newTvSeriesSearchResponse(title ?: "", "$mainUrl/$path/$slug", tvType) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.trimEnd('/').split("/").last()
        val type = when {
            url.contains("/series/") -> "tvshows"
            url.contains("/animes/") -> "animes"
            else -> "movies"
        }

        val apiUrl = "$apiBase/single/$type?slug=$slug&postType=$type"
        val res = app.get(apiUrl).text
        if (!res.trim().startsWith("{")) return null

        val postData = parseJson<SinglePostResponse>(res).data ?: return null

        val title = postData.title ?: ""
        val poster = fixImg(postData.images?.poster ?: postData.poster)
        val backdrop = fixImg(postData.images?.backdrop ?: postData.backdrop)

        return if (type == "movies") {
            // El ID va como cuarto parámetro aquí
            newMovieLoadResponse(title, url, TvType.Movie, postData.id.toString()) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = postData.overview
                this.year = postData.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                addTrailer(postData.trailer)
            }
        } else {
            val tvType = if (type == "animes") TvType.Anime else TvType.TvSeries

            val episodes = mutableListOf<Episode>()
            postData.seasons?.forEach { season ->
                val sNum = season.number ?: 1
                season.episodes?.forEach { ep ->
                    episodes.add(newEpisode(ep.id.toString()) {
                        this.name = ep.title
                        this.episode = ep.number
                        this.season = sNum
                        this.posterUrl = fixImg(ep.image)
                    })
                }
            }

            // CORRECCIÓN: El ID (postData.id.toString()) se pasa como último parámetro
            // de la función, antes de las llaves { }
            newTvSeriesLoadResponse(title, url, tvType, episodes) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = postData.overview
                this.year = postData.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                // Borramos "this.data =" de aquí adentro para evitar el error
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val playerUrl = "$apiBase/player?postId=$data&demo=0"
        val res = app.get(playerUrl).text

        println("$TAG: Respuesta Player API -> $res")
        if (!res.trim().startsWith("{")) return false

        val response = parseJson<PlayerResponse>(res)

        response.data?.embeds?.forEach { embed ->
            val embedUrl = embed.url ?: return@forEach

            if (embedUrl.contains("vimeos.net")) {
                val embedHtml = app.get(
                    embedUrl,
                    referer = "$mainUrl/",
                    headers = mapOf("User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36")
                ).text

                // Regex mejorado para capturar el m3u8
                val masterUrl = Regex("""["'](https?://[^"']+\.m3u8[^"']*)["']""")
                    .find(embedHtml)?.groupValues?.get(1)

                if (masterUrl != null) {
                    callback.invoke(
                        newExtractorLink(
                            source = "Vimeos",
                            name = "Vimeos (${embed.lang ?: "Latino"})",
                            url = masterUrl,
                            type = ExtractorLinkType.M3U8
                        ) {
                            this.referer = "https://vimeos.net/"
                            this.quality = Qualities.P1080.value
                            this.headers = mapOf(
                                "Origin" to "https://vimeos.net",
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36"
                            )
                        }
                    )
                }
            } else {
                loadExtractor(embedUrl, "$mainUrl/", subtitleCallback, callback)
            }
        }
        return true
    }

    data class PlayerResponse(val data: PlayerData?)
    data class PlayerData(val embeds: List<EmbedItem>?)
    data class EmbedItem(
        val url: String?,
        val lang: String?,
        val quality: String?
    )

    data class SinglePostResponse(val data: Post?)

    data class Post(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val slug: String?,
        val type: String?,
        val overview: String?,
        val images: Images?,
        val poster: String?, // Backup por si images viene vacío
        val backdrop: String?, // Backup
        val trailer: String?,
        @JsonProperty("release_date") val releaseDate: String?,
        val seasons: List<Season>? // NECESARIO PARA SERIES
    )

    data class Images(
        val poster: String?,
        val backdrop: String?
    )

    data class Season(
        val number: Int?,
        val episodes: List<EpItem>?
    )

    data class EpItem(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val number: Int?,
        val image: String?
    )

    data class ApiResponse(val data: DataContainer?)
    data class DataContainer(val posts: List<Post>?, val pagination: Pagination?)
    data class Pagination(@JsonProperty("next_page_url") val nextPageUrl: String?)
}