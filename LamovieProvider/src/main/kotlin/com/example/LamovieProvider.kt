package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.utils.AppUtils.parseJson

class LamovieProvider : MainAPI() {
    override var mainUrl = "https://la.movie"
    override var name = "La.Movie"
    override var lang = "es"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime)

    private val apiBase = "$mainUrl/wp-api/v1"
    private val TAG = "LaMovie"

    private fun fixImg(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return if (url.startsWith("http")) url else if (url.startsWith("//")) "https:$url" else "$mainUrl$url"
    }

    override val mainPage = mainPageOf(
        "$apiBase/listing/movies?postType=movies" to "Películas",
        "$apiBase/listing/tvshows?postType=tvshows" to "Series",
        "$apiBase/listing/animes?postType=animes" to "Animes"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${request.data}&page=$page&postsPerPage=12&orderBy=latest&order=DESC"
        val res = app.get(url).text
        val json = parseJson<ApiResponse>(res)
        val home = json.data?.posts?.map { it.toSearchResult() } ?: emptyList()
        return newHomePageResponse(HomePageList(request.name, home), home.isNotEmpty())
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$apiBase/search?postType=any&q=${query.replace(" ", "+")}&postsPerPage=26"
        val res = app.get(url).text
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
        val path = if (tvType == TvType.Movie) "movies" else if (tvType == TvType.Anime) "animes" else "series"
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

        val res = app.get("$apiBase/single/$type?slug=$slug&postType=$type").text
        val postData = parseJson<SinglePostResponse>(res).data ?: return null
        val id = postData.id ?: return null

        if (type == "movies") {
            return newMovieLoadResponse(postData.title ?: "", url, TvType.Movie, id.toString()) {
                this.posterUrl = fixImg(postData.images?.poster ?: postData.poster)
                this.plot = postData.overview
            }
        } else {
            val episodesList = mutableListOf<Episode>()
            val seasons = if (postData.seasons_list.isNullOrEmpty()) listOf("1", "2", "3") else postData.seasons_list

            seasons.forEach { sNumStr ->
                val sNum = sNumStr.toIntOrNull() ?: return@forEach
                try {
                    val epRes = app.get("$apiBase/single/episodes/list?_id=$id&season=$sNum&page=1&postsPerPage=50").text
                    val epData = parseJson<EpisodeListResponse>(epRes)
                    epData.data?.posts?.forEach { epItem ->
                        episodesList.add(newEpisode(epItem.id.toString()) {
                            this.name = epItem.title
                            this.season = sNum
                            this.episode = epItem.episode_number
                        })
                    }
                } catch (e: Exception) { }
            }
            return newTvSeriesLoadResponse(postData.title ?: "", url, if (type == "animes") TvType.Anime else TvType.TvSeries, episodesList) {
                this.posterUrl = fixImg(postData.images?.poster ?: postData.poster)
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
        val cleanId = data.substringAfterLast("/")
        val playerUrl = "$apiBase/player?postId=$cleanId&demo=0"

        Log.i(TAG, "LOG: Iniciando carga de enlaces para ID: $cleanId")

        val res = try { app.get(playerUrl).text } catch (e: Exception) {
            Log.e(TAG, "LOG ERROR: Falló la petición API: ${e.message}")
            return false
        }

        val response = try { parseJson<PlayerResponse>(res) } catch (e: Exception) {
            Log.e(TAG, "LOG ERROR: Error parseando JSON")
            null
        }

        // 1. EMBEDS (Online)
        response?.data?.embeds?.forEach { embed ->
            val embedUrl = embed.url ?: return@forEach
            Log.i(TAG, "LOG: Procesando embed: $embedUrl")

            if (embedUrl.contains("vimeos.net")) {
                try {
                    val embedHtml = app.get(embedUrl, referer = "https://la.movie/").text
                    val masterUrl = Regex("""file\s*:\s*["']([^"']+\.m3u8[^"']*)["']""").find(embedHtml)?.groupValues?.get(1)
                    val subUrl = Regex("""file\s*:\s*["']([^"']+\.vtt[^"']*)["']""").find(embedHtml)?.groupValues?.get(1)

                    if (!subUrl.isNullOrBlank()) {
                        Log.i(TAG, "LOG: Subtítulo detectado: $subUrl")
                        subtitleCallback.invoke(SubtitleFile("Español", subUrl))
                    }

                    if (masterUrl != null) {
                        Log.i(TAG, "LOG: Video M3U8 detectado: $masterUrl")
                        // CORRECCIÓN AQUÍ: Usando el bloque {} para evitar el error de argumentos
                        callback.invoke(
                            newExtractorLink(
                                source = "Vimeos",
                                name = "Vimeos ${embed.lang ?: ""}",
                                url = masterUrl,
                                type = ExtractorLinkType.M3U8
                            ) {
                                this.quality = Qualities.P1080.value
                                this.referer = "https://vimeos.net/"
                            }
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "LOG ERROR: Vimeos falló: ${e.message}")
                }
            } else {
                loadExtractor(embedUrl, "https://la.movie/", subtitleCallback, callback)
            }
        }

        // 2. DOWNLOADS (Magnets)
        response?.data?.downloads?.forEach { dl ->
            val url = dl.url ?: return@forEach
            if (url.startsWith("magnet:")) {
                Log.i(TAG, "LOG: Magnet detectado: ${dl.quality}")
                callback.invoke(
                    newExtractorLink(
                        source = dl.server ?: "Torrent",
                        name = "Torrent ${dl.quality ?: ""} (${dl.lang ?: ""})",
                        url = url,
                        type = ExtractorLinkType.TORRENT
                    ) {
                        // En la nueva versión, la calidad se asigna aquí dentro
                        this.quality = Qualities.P1080.value
                    }
                )
            } else {
                loadExtractor(url, "https://la.movie/", subtitleCallback, callback)
            }
        }
        return true
    }

    // --- MODELOS ACTUALIZADOS ---
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
    data class PlayerData(
        val embeds: List<EmbedItem>?,
        val downloads: List<DownloadItem>?
    )
    data class EmbedItem(val url: String?, val lang: String?)
    data class DownloadItem(val url: String?, val server: String?, val quality: String?, val lang: String?)
}