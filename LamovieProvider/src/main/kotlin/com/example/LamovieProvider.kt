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

        if (cleanUrl.contains("tmdb.org")) {
            cleanUrl = cleanUrl.replace(Regex("/t/p/w\\d+/"), "/t/p/original/")
        }

        if (cleanUrl.startsWith("/thumbs/") || cleanUrl.startsWith("thumbs/")) {
            cleanUrl = "/wp-content/uploads/${cleanUrl.removePrefix("/")}"
        }

        return if (cleanUrl.startsWith("http")) cleanUrl else if (cleanUrl.startsWith("//")) "https:$cleanUrl" else if (cleanUrl.startsWith("/")) "$mainUrl$cleanUrl" else "$mainUrl/$cleanUrl"
    }

    override val mainPage = mainPageOf(
        "$apiBase/listing/tvshows?postType=tvshows" to "Series",
        "$apiBase/listing/animes?postType=animes" to "Animes",
        "$apiBase/listing/movies?postType=movies" to "Películas"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}&page=$page&postsPerPage=12&orderBy=latest&order=DESC"
        val res = app.get(url, headers = mapOf("User-Agent" to USER_AGENT)).text
        val json = try { parseJson<ApiResponse>(res) } catch (e: Exception) { null }
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
        val tvType = if (typeStr == "movies") TvType.Movie else if (typeStr == "animes") TvType.Anime else TvType.TvSeries
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
        val responseObj = try { parseJson<SinglePostResponse>(apiRes) } catch (e: Exception) { null }
        val postData = responseObj?.data ?: return null
        val id = postData.id ?: return null
        val title = postData.title ?: ""

        // LOG PÓSTER: Buscamos el fondo grande en el HTML
        var finalPoster = fixImg(postData.images?.poster ?: postData.poster)
        try {
            val html = app.get(url, headers = mapOf("User-Agent" to USER_AGENT)).text
            val doc = Jsoup.parse(html)
            val bgStyle = doc.selectFirst("div.bg-image")?.attr("style")
            if (!bgStyle.isNullOrBlank()) {
                finalPoster = fixImg(bgStyle)
            }
        } catch (e: Exception) { Log.e(TAG, "FALLO LOG: No se pudo obtener bg-image") }
        Log.d(TAG, "LOG LOAD: URL del póster final -> $finalPoster")

        if (type == "movies") {
            return newMovieLoadResponse(title, url, TvType.Movie, id.toString()) {
                this.posterUrl = finalPoster
                this.plot = postData.overview
            }
        } else {
            val episodesList = mutableListOf<Episode>()

            // 1. Llamada inicial a la T1 para detectar el resto de temporadas
            val firstSeasonUrl = "$apiBase/single/episodes/list?_id=$id&season=1&page=1&postsPerPage=50"
            val firstSeasonRes = app.get(firstSeasonUrl, headers = mapOf("User-Agent" to USER_AGENT)).text
            val firstSeasonData = try { parseJson<EpisodeListResponse>(firstSeasonRes) } catch (e: Exception) { null }

            // Obtenemos temporadas de la API, si viene vacío probamos con [1, 2] como fallback
            val apiSeasons = firstSeasonData?.data?.seasons ?: emptyList()
            val finalSeasons = if (apiSeasons.isEmpty()) listOf("1", "2") else apiSeasons

            Log.d(TAG, "LOG LOAD: Temporadas a procesar -> $finalSeasons")

            finalSeasons.forEach { sNumStr ->
                try {
                    val sNum = sNumStr.toIntOrNull() ?: return@forEach
                    val epRes = if (sNum == 1) firstSeasonRes else {
                        app.get("$apiBase/single/episodes/list?_id=$id&season=$sNum&page=1&postsPerPage=50", headers = mapOf("User-Agent" to USER_AGENT)).text
                    }
                    val epData = parseJson<EpisodeListResponse>(epRes)

                    epData.data?.posts?.forEach { epItem ->
                        val cleanEpTitle = epItem.title?.replace(Regex("^.*?Temporada"), "Temporada")?.trim()
                            ?: "Temporada $sNum Episodio ${epItem.episode_number}"

                        episodesList.add(newEpisode(epItem.id.toString()) {
                            this.name = cleanEpTitle
                            this.season = sNum
                            this.episode = epItem.episode_number
                        })
                    }
                    Log.d(TAG, "LOG LOAD: Temporada $sNum cargada OK")
                } catch (e: Exception) {
                    Log.e(TAG, "FALLO LOG: Error en temporada $sNumStr")
                }
            }

            return newTvSeriesLoadResponse(title, url, if (type == "animes") TvType.Anime else TvType.TvSeries, episodesList) {
                this.posterUrl = finalPoster
                this.plot = postData.overview
            }
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
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
        val seasons: List<String>?,
        val seasons_list: List<String>?
    )
    data class Images(val poster: String?)
    data class EpisodeListResponse(val data: EpisodeListData?)
    data class EpisodeListData(
        val posts: List<EpisodePostItem>?,
        val seasons: List<String>? // ¡IMPORTANTE! Esto faltaba para detectar temporadas
    )
    data class EpisodePostItem(@JsonProperty("_id") val id: Int?, val title: String?, val episode_number: Int?)
    data class PlayerResponse(val data: PlayerData?)
    data class PlayerData(val embeds: List<EmbedItem>?)
    data class EmbedItem(val url: String?)
}