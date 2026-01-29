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
        val res = app.get(url).text
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
        val json = parseJson<ApiResponse>(res)
        return json.data?.posts?.map { it.toSearchResult() } ?: emptyList()
    }

    private fun Post.toSearchResult(): SearchResponse {
        val poster = fixImg(images?.poster ?: this.poster)
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
        val postData = parseJson<SinglePostResponse>(res).data ?: return null

        val title = postData.title ?: ""
        val poster = fixImg(postData.images?.poster ?: postData.poster)
        val backdrop = fixImg(postData.images?.backdrop ?: postData.backdrop)
        val id = postData.id ?: return null

        if (type == "movies") {
            return newMovieLoadResponse(title, url, TvType.Movie, id.toString()) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = postData.overview
                this.year = postData.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                addTrailer(postData.trailer)
            }
        } else {
            val episodesList = mutableListOf<Episode>()

            // 1. Obtenemos las temporadas disponibles (la API devuelve ["2", "1"] según tu curl)
            // Usamos un fallback por si el campo se llama distinto
            val seasonNumbers = postData.seasons_list ?: listOf("1")

            // 2. Por cada temporada, hacemos la petición a la sub-API de episodios
            seasonNumbers.forEach { sNumStr ->
                try {
                    val sNum = sNumStr.toIntOrNull() ?: 1
                    val epRes = app.get("$apiBase/single/episodes/list?_id=$id&season=$sNum&page=1&postsPerPage=100").text
                    val epData = parseJson<EpisodeListResponse>(epRes)

                    epData.data?.posts?.forEach { epItem ->
                        episodesList.add(newEpisode(epItem.id.toString()) {
                            this.name = epItem.title
                            this.season = epItem.season_number ?: sNum
                            this.episode = epItem.episode_number
                            this.posterUrl = fixImg(epItem.still_path)
                        })
                    }
                } catch (e: Exception) {
                    println("$TAG: Error cargando temporada $sNumStr -> ${e.message}")
                }
            }

            return newTvSeriesLoadResponse(title, url, if (type == "animes") TvType.Anime else TvType.TvSeries, episodesList) {
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
        val playerUrl = "$apiBase/player?postId=$data&demo=0"
        println("$TAG: Solicitando links (loadLinks) para ID -> $data")
        println("$TAG: URL Player -> $playerUrl")

        val res = app.get(playerUrl).text
        println("$TAG: Respuesta API Player -> $res")

        if (!res.trim().startsWith("{")) return false
        val response = try { parseJson<PlayerResponse>(res) } catch(e: Exception) { null }

        response?.data?.embeds?.forEach { embed ->
            val embedUrl = embed.url ?: return@forEach
            println("$TAG: Procesando embed -> $embedUrl")

            if (embedUrl.contains("vimeos.net")) {
                val embedHtml = app.get(embedUrl, referer = "https://vimeos.net/").text

                val masterUrl = Regex("""file\s*:\s*["'](https?://[^"']+\.m3u8[^"']*)["']""")
                    .find(embedHtml)?.groupValues?.get(1)
                    ?: Regex("""source\s*:\s*["'](https?://[^"']+\.m3u8[^"']*)["']""")
                        .find(embedHtml)?.groupValues?.get(1)

                if (masterUrl != null) {
                    callback.invoke(
                        newExtractorLink(
                            source = "Vimeos",
                            name = "Vimeos ${embed.lang ?: ""}",
                            url = masterUrl,
                            type = ExtractorLinkType.M3U8
                        ) {
                            this.referer = "https://vimeos.net/"
                            this.quality = Qualities.P1080.value
                        }
                    )
                }
            } else {
                loadExtractor(embedUrl, "$mainUrl/", subtitleCallback, callback)
            }
        }
        return true
    }

    // --- MODELOS DE DATOS MEJORADOS ---
    data class ApiResponse(val data: DataContainer?)
    data class DataContainer(val posts: List<Post>?)

    data class SinglePostResponse(val data: Post?)

    data class EpisodeListResponse(val data: EpisodeListData?)
    data class EpisodeListData(val posts: List<EpisodePostItem>?, val seasons: List<String>?)

    data class EpisodePostItem(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val still_path: String?,
        val season_number: Int?,
        val episode_number: Int?
    )

    data class Post(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val slug: String?,
        val type: String?,
        val overview: String?,
        val images: Images?,
        val poster: String?,
        val backdrop: String?,
        val trailer: String?,
        @JsonProperty("release_date") val releaseDate: String?,
        @JsonProperty("seasons") val seasons_list: List<String>?
    )

    data class Images(val poster: String?, val backdrop: String?)

    data class Season(
        val number: Int?,
        val episodes: List<EpisodeItem>? // Quitamos la anotación para probar mapeo automático
    )

    data class EpisodeItem(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val number: Int?,
        val image: String?
    )

    data class PlayerResponse(val data: PlayerData?)
    data class PlayerData(val embeds: List<EmbedItem>?)
    data class EmbedItem(
        val url: String?,
        val lang: String?,
        val quality: String? // Añadido quality por si acaso
    )
}