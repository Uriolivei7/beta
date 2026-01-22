package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.serialization.*

class UniqueStreamProvider : MainAPI() {
    override var mainUrl = "https://anime.uniquestream.net"
    override var name = "AnimeStream"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val apiUrl = "https://anime.uniquestream.net/api/v1"
    private val TAG = "UniqueStream"

    // Fix para el error de "Unresolved reference" en toSearchResponse
    private fun SeriesItem.toSearchResponse(): SearchResponse {
        return newAnimeSearchResponse(this.title, this.content_id) {
            this.posterUrl = image
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        return try {
            Log.d(TAG, "Cargando MainPage...")
            val response = app.get("$apiUrl/search?query=a&limit=20").text
            val data = AppUtils.parseJson<SearchRoot>(response)

            val homeItems = mutableListOf<HomePageList>()

            data.series?.let { series ->
                homeItems.add(HomePageList("Series Destacadas", series.map { it.toSearchResponse() }))
            }

            data.episodes?.let { episodes ->
                homeItems.add(HomePageList("Últimos Episodios", episodes.map {
                    newAnimeSearchResponse(it.title ?: "Anime", it.content_id) {
                        this.posterUrl = it.image
                    }
                }))
            }
            newHomePageResponse(homeItems, homeItems.isNotEmpty())
        } catch (e: Exception) {
            Log.e(TAG, "Error en getMainPage: ${e.message}")
            newHomePageResponse(emptyList(), false)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            val response = app.get("$apiUrl/search?query=$query").text
            val data = AppUtils.parseJson<SearchRoot>(response)
            data.series?.map { it.toSearchResponse() } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val cleanId = url.split("/").lastOrNull { it.isNotBlank() } ?: url
        Log.d(TAG, "Cargando serie con ID: $cleanId")

        // Headers completos para engañar a la protección del servidor
        val apiHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
            "Accept" to "application/json, text/plain, */*",
            "Accept-Language" to "es-ES,es;q=0.9",
            "Referer" to "https://anime.uniquestream.net/series/$cleanId",
            "Origin" to "https://anime.uniquestream.net",
            "Sec-Fetch-Dest" to "empty",
            "Sec-Fetch-Mode" to "cors",
            "Sec-Fetch-Site" to "same-origin"
        )

        val seriesResponse = app.get("$apiUrl/series/$cleanId", headers = apiHeaders).text

        if (seriesResponse.contains("Not Found")) {
            Log.d(TAG, "ID de episodio detectado en Home, buscando serie...")
            val search = app.get("$apiUrl/search?query=$cleanId", headers = apiHeaders).text
            val searchData = AppUtils.parseJson<SearchRoot>(search)
            val realSeriesId = searchData.series?.firstOrNull()?.content_id
            if (realSeriesId != null) {
                // Si encontramos la serie, volvemos a intentar cargar pero con el ID bueno
                return load("$mainUrl/series/$realSeriesId")
            }
        }

        val details = AppUtils.parseJson<DetailsResponse>(seriesResponse)
        val episodesList = mutableListOf<Episode>()

        // Reemplaza el bloque dentro de details.seasons?.forEach en tu función load:
        details.seasons?.forEach { season ->
            try {
                // AÑADIMOS limit=1000 para que no se corte en 5 episodios
                val seasonUrl = "$apiUrl/season/${season.content_id}/episodes?page=1&limit=1000&order_by=asc"
                Log.d(TAG, "Pidiendo episodios: $seasonUrl")

                val response = app.get(seasonUrl, headers = apiHeaders)
                val seasonEpsText = response.text

                if (seasonEpsText.trim().startsWith("[")) {
                    val eps = AppUtils.parseJson<List<EpisodeItem>>(seasonEpsText)
                    eps.forEach { ep ->
                        if (ep.is_clip != true) {
                            // LIMPIAMOS EL ID: Si ep.content_id es una URL, sacamos solo el ID
                            val cleanEpId = ep.content_id.split("/").lastOrNull { it.isNotBlank() } ?: ep.content_id

                            episodesList.add(newEpisode(cleanEpId) { // <--- Usamos el ID limpio
                                this.name = ep.title
                                this.episode = ep.episode_number?.toInt()
                                this.season = season.season_number
                                this.posterUrl = ep.image
                            })
                        }
                    }
                    Log.d(TAG, "Temporada ${season.season_number} ok: ${episodesList.size} caps acumulados")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en season ${season.season_number}: ${e.message}")
            }
        }

        return newAnimeLoadResponse(details.title ?: "Sin Título", url, TvType.Anime) {
            this.posterUrl = details.images?.find { it.type == "poster_tall" }?.url
            this.plot = details.description
            this.tags = (details.audio_locales ?: emptyList()) + (details.subtitle_locales ?: emptyList())
            addEpisodes(DubStatus.Subbed, episodesList)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        // LIMPIEZA DEL ID (Fundamental para que la API responda)
        val cleanId = data.split("/").lastOrNull { it.isNotBlank() } ?: data
        Log.d(TAG, "Iniciando loadLinks para ID limpio: $cleanId")

        return try {
            // Usamos el ID limpio aquí
            val response = app.get("$apiUrl/video?content_id=$cleanId").text
            val videoData = AppUtils.parseJson<VideoResponse>(response)
            var linksFound = 0

            // Definimos los headers que vimos en tu CURL
            val commonHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
                "Referer" to "$mainUrl/",
                "Accept" to "*/*"
            )

            // --- HLS ---
            videoData.versions?.hls?.forEach { v ->
                Log.d(TAG, "HLS encontrado: ${v.locale}")
                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} HLS ${v.locale}",
                        url = v.playlist,
                        type = ExtractorLinkType.M3U8
                    ) {
                        // Agregamos los headers aquí para que el reproductor los use
                        this.headers = commonHeaders
                    }
                )
                linksFound++

                // Si el JSON de video trae subtítulos, los cargamos aquí
                v.subtitles?.forEach { sub ->
                    Log.d(TAG, "Subtítulo detectado: ${sub.locale}")
                    subtitleCallback(SubtitleFile(sub.locale, sub.url))
                }
            }

            // --- DASH ---
            videoData.versions?.dash?.forEach { v ->
                Log.d(TAG, "DASH encontrado: ${v.locale}")
                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} DASH ${v.locale}",
                        url = v.playlist,
                        type = ExtractorLinkType.DASH
                    ) {
                        this.headers = commonHeaders
                    }
                )
                linksFound++
            }

            if (linksFound == 0) Log.e(TAG, "La API no devolvió enlaces de video")
            linksFound > 0
        } catch (e: Exception) {
            Log.e(TAG, "Error crítico en loadLinks: ${e.message}")
            false
        }
    }

    @Serializable data class SeriesItem(val content_id: String, val title: String, val image: String? = null)

    @Serializable
    data class SearchRoot(
        val series: List<SeriesItem>? = null,
        val episodes: List<EpisodeItem>? = null
    )

    @Serializable
    data class VideoResponse(
        val versions: VideoVersions? = null
    )

    @Serializable
    data class VideoVersions(
        val hls: List<VideoV>? = null,
        val dash: List<VideoV>? = null
    )

    @Serializable
    data class VideoV(
        val locale: String,
        val playlist: String,
        val subtitles: List<SubtitleItem>? = null // Para los subtítulos
    )

    @Serializable
    data class SubtitleItem(
        val locale: String,
        val url: String
    )

    @Serializable
    data class DetailsResponse(
        val content_id: String? = null, // El signo ? es vital aquí
        val title: String? = null,
        val description: String? = null,
        val images: List<ImageItem>? = null,
        val seasons: List<SeasonItem>? = null,
        val audio_locales: List<String>? = null,
        val subtitle_locales: List<String>? = null
    )

    @Serializable
    data class SeasonItem(
        val content_id: String,
        val season_number: Int,
        val title: String? = null
    )

    @Serializable
    data class EpisodeItem(
        val content_id: String,
        val series_id: String? = null, // <--- AÑADIR ESTO
        val title: String? = null,
        val episode_number: Double? = null,
        val image: String? = null,
        val is_clip: Boolean? = false
    )

    @Serializable
    data class ImageItem(
        val url: String,
        val type: String
    )
}