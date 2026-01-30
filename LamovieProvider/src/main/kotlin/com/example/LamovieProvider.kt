package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import org.jsoup.Jsoup

class LamovieProvider : MainAPI() {
    override var mainUrl = "https://la.movie"
    override var name = "La.Movie"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.AsianDrama, TvType.Cartoon)

    private val apiBase = "$mainUrl/wp-api/v1"
    private val TAG = "LaMovie"
    private val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    private fun fixImg(url: String?): String? {
        if (url.isNullOrBlank()) return null
        // Limpiamos basura de estilos CSS o comillas del HTML
        var cleanUrl = url.replace("&quot;", "").replace("\"", "").replace("'", "").trim()
        if (cleanUrl.contains("url(")) {
            cleanUrl = cleanUrl.substringAfter("url(").substringBefore(")").trim()
        }

        // MEJORA DE NITIDEZ: Forzamos 'original' en lugar de 'w500' o 'w300'
        if (cleanUrl.contains("tmdb.org")) {
            cleanUrl = cleanUrl.replace(Regex("/t/p/w\\d+/"), "/t/p/original/")
        }

        if (cleanUrl.startsWith("/thumbs/") || cleanUrl.startsWith("thumbs/")) {
            cleanUrl = "/wp-content/uploads/${cleanUrl.removePrefix("/")}"
        }

        return when {
            cleanUrl.startsWith("http") -> cleanUrl
            cleanUrl.startsWith("//") -> "https:$cleanUrl"
            cleanUrl.startsWith("/") -> "$mainUrl$cleanUrl"
            else -> "$mainUrl/$cleanUrl"
        }
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
        val path = when (tvType) {
            TvType.Movie -> "peliculas"
            TvType.Anime -> "animes"
            else -> "series"
        }
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
        val responseObj = try { parseJson<SinglePostResponse>(apiRes) } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en parseo de load: ${e.message}")
            null
        }
        val postData = responseObj?.data ?: return null
        val id = postData.id ?: return null
        val title = postData.title ?: ""

        // CONFIGURACIÓN DE IMÁGENES
        // Usamos el 'backdrop' (imagen grande) como poster principal del LOAD
        val bigPoster = fixImg(postData.images?.backdrop ?: postData.backdrop)
        // Si por alguna razón no hay backdrop, usamos el poster normal como respaldo
        val fallbackPoster = fixImg(postData.images?.poster ?: postData.poster)

        val finalMainImg = bigPoster ?: fallbackPoster

        val response = if (type == "movies") {
            newMovieLoadResponse(title, url, TvType.Movie, id.toString()) {
                this.posterUrl = finalMainImg // <--- Aquí va la imagen grande
                this.backgroundPosterUrl = finalMainImg
                this.plot = postData.overview
            }
        } else {
            val episodesList = mutableListOf<Episode>()
            val firstSeasonUrl = "$apiBase/single/episodes/list?_id=$id&season=1&page=1&postsPerPage=50"
            val firstSeasonRes = app.get(firstSeasonUrl, headers = mapOf("User-Agent" to USER_AGENT)).text
            val firstSeasonData = try { parseJson<EpisodeListResponse>(firstSeasonRes) } catch (e: Exception) { null }

            val finalSeasons = firstSeasonData?.data?.seasons ?: listOf("1")

            finalSeasons.forEach { sNumStr ->
                try {
                    val sNum = sNumStr.toIntOrNull() ?: return@forEach
                    val epRes = if (sNum == 1) firstSeasonRes else {
                        app.get("$apiBase/single/episodes/list?_id=$id&season=$sNum&page=1&postsPerPage=50", headers = mapOf("User-Agent" to USER_AGENT)).text
                    }
                    val epData = parseJson<EpisodeListResponse>(epRes)
                    epData.data?.posts?.forEach { epItem ->
                        val cleanEpTitle = epItem.title?.replace(Regex("^.*?Temporada"), "Temporada")?.trim() ?: "T$sNum E${epItem.episode_number}"
                        episodesList.add(newEpisode(epItem.id.toString()) {
                            this.name = cleanEpTitle
                            this.season = sNum
                            this.episode = epItem.episode_number
                        })
                    }
                } catch (e: Exception) { }
            }

            newTvSeriesLoadResponse(title, url, if (type == "animes") TvType.Anime else TvType.TvSeries, episodesList) {
                this.posterUrl = finalMainImg // <--- Aquí también la imagen grande
                this.backgroundPosterUrl = finalMainImg
                this.plot = postData.overview
            }
        }

        // Cambia esto al final de tu función load:
        return response.apply {
            this.recommendations = postData.recommendations
                ?.filter { it.id != postData.id } // Evita que se recomiende a sí mismo
                ?.map { it.toSearchResult() }

            Log.d(TAG, "Logs: Load finalizado para $title. Imagen principal: $finalMainImg")
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val cleanId = data.substringAfterLast("/").trim()
        val playerUrl = "$apiBase/player?postId=$cleanId&demo=0"

        Log.d(TAG, "Logs: Cargando enlaces para ID $cleanId")

        val res = try {
            app.get(playerUrl, headers = mapOf("Referer" to "$mainUrl/")).text
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en API de player: ${e.message}")
            return false
        }

        val response = try { parseJson<PlayerResponse>(res) } catch (e: Exception) { null }

        response?.data?.embeds?.forEach { embed ->
            val embedUrl = embed.url ?: return@forEach

            if (embedUrl.contains("la.movie/embed.html")) {
                try {
                    val internalHtml = app.get(embedUrl, referer = "$mainUrl/").document
                    val realUrl = internalHtml.select("iframe").attr("src")
                    if (realUrl.isNotBlank()) {
                        loadExtractor(realUrl, embedUrl, subtitleCallback, callback)
                        return@forEach
                    }
                } catch (e: Exception) { }
            }
            loadExtractor(embedUrl, "$mainUrl/", subtitleCallback, callback)
        }

        response?.data?.downloads?.forEach { download ->
            download.url?.let { loadExtractor(it, "$mainUrl/", subtitleCallback, callback) }
        }

        return true
    }

    // --- DATA CLASSES ---

    data class PlayerResponse(val data: PlayerData?)
    data class PlayerData(val embeds: List<EmbedItem>?, val downloads: List<EmbedItem>?)
    data class EmbedItem(val url: String?, val server: String? = null, val lang: String? = null)

    data class ApiResponse(val data: DataContainer?)
    data class DataContainer(val posts: List<Post>?)
    data class SinglePostResponse(val data: Post?)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Post(
        @JsonProperty("_id") val id: Int?,
        val title: String?,
        val slug: String?,
        val type: String?,
        val overview: String?,
        val images: Images?,
        val poster: String?,
        val backdrop: String? = null,
        val recommendations: List<Post>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Images(
        val poster: String?,
        val backdrop: String? = null
    )

    data class EpisodeListResponse(val data: EpisodeListData?)
    data class EpisodeListData(val posts: List<EpisodePostItem>?, val seasons: List<String>?)
    data class EpisodePostItem(@JsonProperty("_id") val id: Int?, val title: String?, val episode_number: Int?)
}