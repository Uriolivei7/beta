package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import org.jsoup.Jsoup

class LamovieProvider : MainAPI() {
    override var mainUrl = "https://la.movie"
    override var name = "La.Movie"
    override var lang = "es"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime)

    private val apiBase = "$mainUrl/wp-api/v1"
    private val TAG = "LaMovie"
    private val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    private fun fixImg(url: String?): String? {
        if (url.isNullOrBlank()) return null
        var cleanUrl = url.replace("&quot;", "").replace("\"", "").replace("'", "").trim()

        if (cleanUrl.contains("url(")) {
            cleanUrl = cleanUrl.substringAfter("url(").substringBefore(")").trim()
        }

        // Si es de TMDB, forzar calidad Original
        if (cleanUrl.contains("tmdb.org")) {
            cleanUrl = cleanUrl.replace(Regex("/t/p/w\\d+/"), "/t/p/original/")
        }

        if (cleanUrl.startsWith("/thumbs/") || cleanUrl.startsWith("thumbs/")) {
            val path = if (cleanUrl.startsWith("/")) cleanUrl else "/$cleanUrl"
            cleanUrl = "/wp-content/uploads$path"
        }

        return when {
            cleanUrl.startsWith("http") -> cleanUrl
            cleanUrl.startsWith("//") -> "https:$cleanUrl"
            cleanUrl.startsWith("/") -> "$mainUrl$cleanUrl"
            else -> "$mainUrl/$cleanUrl"
        }
    }

    override val mainPage = mainPageOf(
        "$apiBase/listing/movies?postType=movies" to "Películas",
        "$apiBase/listing/tvshows?postType=tvshows" to "Series",
        "$apiBase/listing/animes?postType=animes" to "Animes"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}&page=$page&postsPerPage=12&orderBy=latest&order=DESC"
        val res = app.get(url, headers = mapOf("User-Agent" to USER_AGENT)).text
        val json = try { parseJson<ApiResponse>(res) } catch (e: Exception) {
            Log.e(TAG, "FALLO LOG: Error en MainPage JSON")
            null
        }
        val home = json?.data?.posts?.map { it.toSearchResult() } ?: emptyList()
        return newHomePageResponse(HomePageList(request.name, home), home.isNotEmpty())
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$apiBase/search?postType=any&q=${query.replace(" ", "+")}&postsPerPage=26"
        val res = app.get(url, headers = mapOf("User-Agent" to USER_AGENT)).text
        val json = try { parseJson<ApiResponse>(res) } catch (e: Exception) { null }
        return json?.data?.posts?.map { it.toSearchResult() } ?: emptyList()
    }

    private fun Post.toSearchResult(): SearchResponse {
        val poster = fixImg(images?.poster ?: this.poster)
        val typeStr = type ?: "movies"
        val tvType = when (typeStr) {
            "movies" -> TvType.Movie
            "animes" -> TvType.Anime
            else -> TvType.TvSeries
        }
        val path = if (tvType == TvType.Movie) "peliculas" else if (tvType == TvType.Anime) "animes" else "series"

        return if (tvType == TvType.Movie) {
            newMovieSearchResponse(title ?: "", "$mainUrl/$path/$slug", tvType) { this.posterUrl = poster }
        } else {
            newTvSeriesSearchResponse(title ?: "", "$mainUrl/$path/$slug", tvType) { this.posterUrl = poster }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.trimEnd('/').split("/").last()
        val type = when {
            url.contains("/series/") -> "tvshows"
            url.contains("/animes/") -> "animes"
            else -> "movies"
        }

        val apiRes = app.get("$apiBase/single/$type?slug=$slug&postType=$type", headers = mapOf("User-Agent" to USER_AGENT)).text
        val postData = try { parseJson<SinglePostResponse>(apiRes).data } catch (e: Exception) { null }
        val id = postData?.id ?: return null
        val title = postData.title ?: ""

        // Extraer Póster de ALTA CALIDAD del HTML (bg-image)
        var poster = fixImg(postData.images?.poster ?: postData.poster)
        try {
            val html = app.get(url, headers = mapOf("User-Agent" to USER_AGENT)).text
            val doc = Jsoup.parse(html)
            val bgStyle = doc.selectFirst("div.bg-image")?.attr("style")
            if (!bgStyle.isNullOrBlank()) {
                val highRes = fixImg(bgStyle)
                if (highRes?.contains("tmdb.org") == true) {
                    poster = highRes
                    Log.d(TAG, "LOG LOAD: Usando póster Original de TMDB -> $poster")
                }
            }
        } catch (e: Exception) { Log.e(TAG, "FALLO LOG: Error en Jsoup Poster") }

        return if (type == "movies") {
            newMovieLoadResponse(title, url, TvType.Movie, id.toString()) {
                this.posterUrl = poster
                this.plot = postData.overview
            }
        } else {
            val episodesList = mutableListOf<Episode>()
            // Iterar por todas las temporadas disponibles
            val seasons = postData.seasons_list ?: listOf("1")

            seasons.forEach { sNumStr ->
                val sNum = sNumStr.toIntOrNull() ?: return@forEach
                try {
                    val epRes = app.get("$apiBase/single/episodes/list?_id=$id&season=$sNum&page=1&postsPerPage=100", headers = mapOf("User-Agent" to USER_AGENT)).text
                    val epData = parseJson<EpisodeListResponse>(epRes)
                    epData.data?.posts?.forEach { epItem ->
                        // Limpiar el título para que solo diga "Temporada X Episodio Y"
                        val cleanEpTitle = epItem.title?.replace(Regex(".*(?=Temporada)"), "")?.trim()
                            ?: "Temporada $sNum Episodio ${epItem.episode_number}"

                        episodesList.add(newEpisode(epItem.id.toString()) {
                            this.name = cleanEpTitle
                            this.season = sNum
                            this.episode = epItem.episode_number
                        })
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "FALLO LOG: Error cargando temporada $sNum")
                }
            }

            newTvSeriesLoadResponse(title, url, if (type == "animes") TvType.Anime else TvType.TvSeries, episodesList) {
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
        val res = app.get(playerUrl, headers = mapOf("User-Agent" to USER_AGENT)).text
        val response = try { parseJson<PlayerResponse>(res) } catch (e: Exception) { null }
        response?.data?.embeds?.forEach { embed ->
            loadExtractor(embed.url ?: return@forEach, "$mainUrl/", subtitleCallback, callback)
        }
        return true
    }

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