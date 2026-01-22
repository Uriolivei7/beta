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
        // 1. Limpiamos el ID: Si viene una URL completa, sacamos solo el código final
        val cleanId = url.split("/").lastOrNull { it.isNotBlank() } ?: url
        Log.d(TAG, "Cargando serie con ID: $cleanId")

        val apiHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
            "Referer" to "https://anime.uniquestream.net/series/$cleanId",
            "Accept" to "application/json"
        )

        // 2. Pedimos los detalles de la serie
        val seriesResponse = app.get("$apiUrl/series/$cleanId", headers = apiHeaders).text

        // Si la serie no existe como tal, es un episodio.
        // Por ahora, enfoquémonos en que las series carguen sus episodios:
        if (seriesResponse.contains("Not Found")) {
            Log.e(TAG, "La API no encontró la serie $cleanId. Verificando formato...")
            return newAnimeLoadResponse("Serie no encontrada", url, TvType.Anime) { }
        }

        val details = AppUtils.parseJson<DetailsResponse>(seriesResponse)
        val episodesList = mutableListOf<Episode>()

        // 3. CARGA DE TEMPORADAS (Aquí es donde estaba el fallo)
        // Usamos el ID de cada temporada para traer sus episodios
        details.seasons?.forEach { season ->
            try {
                // Esta es la ruta que confirmamos con tu CURL
                val seasonUrl = "$apiUrl/season/${season.content_id}/episodes?page=1&limit=100&order_by=asc"
                Log.d(TAG, "Cargando episodios de temporada: ${season.season_number} (ID: ${season.content_id})")

                val seasonEpsText = app.get(seasonUrl, headers = apiHeaders).text

                // Verificamos que sea una lista [...] y no un error {"detail"...}
                if (seasonEpsText.trim().startsWith("[")) {
                    val eps = AppUtils.parseJson<List<EpisodeItem>>(seasonEpsText)
                    eps.forEach { ep ->
                        // Agregamos solo si no es un clip/trailer
                        if (ep.is_clip != true) {
                            episodesList.add(newEpisode(ep.content_id) {
                                this.name = ep.title
                                this.episode = ep.episode_number?.toInt()
                                this.season = season.season_number
                                this.posterUrl = ep.image
                            })
                        }
                    }
                    Log.d(TAG, "Temporada ${season.season_number} cargada con ${eps.size} items")
                } else {
                    Log.e(TAG, "Error en temporada ${season.season_number}: Respuesta no válida")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Fallo al procesar temporada ${season.season_number}: ${e.message}")
            }
        }

        // 4. Armamos la respuesta final para CloudStream
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
        Log.d(TAG, "Iniciando loadLinks para: $data")
        return try {
            // Obtenemos los links de video
            val response = app.get("$apiUrl/video?content_id=$data").text
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