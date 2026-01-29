package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

class LamovieProvider : MainAPI() {
    override var mainUrl = "https://la.movie"
    override var name = "La.Movie"
    override var lang = "es" // Cambiado a es (estándar)
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime)

    private val apiBase = "$mainUrl/wp-api/v1"
    private val TAG = "LaMovie"

    private fun fixImg(url: String?): String? {
        if (url.isNullOrBlank()) {
            Log.w(TAG, "LOG POSTER: Se recibió una URL nula o vacía")
            return null
        }

        Log.d(TAG, "LOG POSTER: Procesando entrada -> $url")

        // 1. Limpiar escapes de HTML como &quot; y comillas
        var cleanUrl = url.replace("&quot;", "")
            .replace("\"", "")
            .replace("'", "")
            .replace("&amp;", "&")
            .trim()

        // 2. Extraer si viene dentro de url(...) de CSS
        if (cleanUrl.contains("url(")) {
            cleanUrl = cleanUrl.substringAfter("url(").substringBefore(")").trim()
            Log.d(TAG, "LOG POSTER: Extraído de CSS -> $cleanUrl")
        }

        // 3. Corregir dominio y protocolos
        val finalUrl = when {
            cleanUrl.startsWith("http") -> cleanUrl
            cleanUrl.startsWith("//") -> "https:$cleanUrl"
            cleanUrl.startsWith("/") -> "$mainUrl$cleanUrl"
            else -> "$mainUrl/$cleanUrl"
        }

        Log.i(TAG, "LOG POSTER: Resultado Final -> $finalUrl")
        return finalUrl
    }

    override val mainPage = mainPageOf(
        "$apiBase/listing/movies?postType=movies" to "Películas",
        "$apiBase/listing/tvshows?postType=tvshows" to "Series",
        "$apiBase/listing/animes?postType=animes" to "Animes"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}&page=$page&postsPerPage=12&orderBy=latest&order=DESC"
        Log.d(TAG, "LOG MAIN: Solicitando $url")

        val res = app.get(url).text
        val json = try { parseJson<ApiResponse>(res) } catch (e: Exception) {
            Log.e(TAG, "LOG ERROR: Error parseando MainPage JSON: ${e.message}")
            null
        }

        val home = json?.data?.posts?.map { it.toSearchResult() } ?: emptyList()
        Log.d(TAG, "LOG MAIN: Se encontraron ${home.size} items")
        return newHomePageResponse(HomePageList(request.name, home), home.isNotEmpty())
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$apiBase/search?postType=any&q=${query.replace(" ", "+")}&postsPerPage=26"
        Log.d(TAG, "LOG SEARCH: Buscando $query")

        val res = app.get(url).text
        val json = try { parseJson<ApiResponse>(res) } catch (e: Exception) {
            Log.e(TAG, "LOG ERROR: Error en búsqueda: ${e.message}")
            null
        }
        return json?.data?.posts?.map { it.toSearchResult() } ?: emptyList()
    }

    private fun Post.toSearchResult(): SearchResponse {
        // Log para ver qué campos trae el objeto Post originalmente
        Log.v(TAG, "LOG POST: Procesando item [${this.title}] - PosterField: ${this.poster} - ImagesObj: ${this.images?.poster}")

        val posterUrl = fixImg(images?.poster ?: this.poster)
        val typeStr = type ?: "movies"
        val tvType = when (typeStr) {
            "movies" -> TvType.Movie
            "animes" -> TvType.Anime
            else -> TvType.TvSeries
        }

        val path = when (tvType) {
            TvType.Movie -> "peliculas"
            TvType.Anime -> "animes"
            else -> "series"
        }

        return if (tvType == TvType.Movie) {
            newMovieSearchResponse(title ?: "", "$mainUrl/$path/$slug", tvType) { this.posterUrl = posterUrl }
        } else {
            newTvSeriesSearchResponse(title ?: "", "$mainUrl/$path/$slug", tvType) { this.posterUrl = posterUrl }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "LOG LOAD: Cargando detalles de $url")
        val slug = url.trimEnd('/').split("/").last()
        val type = when {
            url.contains("/series/") -> "tvshows"
            url.contains("/animes/") -> "animes"
            else -> "movies"
        }

        val apiUrl = "$apiBase/single/$type?slug=$slug&postType=$type"
        val res = app.get(apiUrl).text
        val postData = try { parseJson<SinglePostResponse>(res).data } catch (e: Exception) {
            Log.e(TAG, "LOG ERROR: Error parseando SinglePost ($slug): ${e.message}")
            null
        } ?: return null

        val id = postData.id ?: return null
        val poster = fixImg(postData.images?.poster ?: postData.poster)

        return if (type == "movies") {
            newMovieLoadResponse(postData.title ?: "", url, TvType.Movie, id.toString()) {
                this.posterUrl = poster
                this.plot = postData.overview
            }
        } else {
            val episodesList = mutableListOf<Episode>()
            val seasons = if (postData.seasons_list.isNullOrEmpty()) listOf("1") else postData.seasons_list

            seasons.forEach { sNumStr ->
                val sNum = sNumStr.toIntOrNull() ?: return@forEach
                try {
                    val epUrl = "$apiBase/single/episodes/list?_id=$id&season=$sNum&page=1&postsPerPage=50"
                    val epRes = app.get(epUrl).text
                    val epData = parseJson<EpisodeListResponse>(epRes)
                    epData.data?.posts?.forEach { epItem ->
                        episodesList.add(newEpisode(epItem.id.toString()) {
                            this.name = epItem.title
                            this.season = sNum
                            this.episode = epItem.episode_number
                        })
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "LOG ERROR: Error en episodios S$sNum: ${e.message}")
                }
            }
            newTvSeriesLoadResponse(postData.title ?: "", url, if (type == "animes") TvType.Anime else TvType.TvSeries, episodesList) {
                this.posterUrl = poster
                this.plot = postData.overview
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
        Log.i(TAG, "LOG LINKS: Solicitando ID $data")

        val res = try { app.get(playerUrl).text } catch (e: Exception) {
            Log.e(TAG, "LOG ERROR: Error de red en Player API")
            return false
        }

        val response = try { parseJson<PlayerResponse>(res) } catch (e: Exception) {
            Log.e(TAG, "LOG ERROR: Error parseando Player JSON")
            null
        }

        response?.data?.embeds?.forEach { embed ->
            val embedUrl = embed.url ?: return@forEach
            Log.i(TAG, "LOG LINKS: Enviando a extractor -> $embedUrl")
            loadExtractor(embedUrl, "$mainUrl/", subtitleCallback, callback)
        }

        return true
    }

    // --- Modelos ---
    data class ApiResponse(val data: DataContainer?)
    data class DataContainer(val posts: List<Post>?)
    data class SinglePostResponse(val data: Post?)
    data class Post(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val slug: String?,
        val type: String?,
        val overview: String?,
        val images: Images?,
        val poster: String?,
        @JsonProperty("seasons") val seasons_list: List<String>?
    )
    data class Images(val poster: String?)
    data class EpisodeListResponse(val data: EpisodeListData?)
    data class EpisodeListData(val posts: List<EpisodePostItem>?)
    data class EpisodePostItem(@JsonProperty("_id") val id: Int?, val title: String?, val episode_number: Int?)
    data class PlayerResponse(val data: PlayerData?)
    data class PlayerData(val embeds: List<EmbedItem>?)
    data class EmbedItem(val url: String?)
}