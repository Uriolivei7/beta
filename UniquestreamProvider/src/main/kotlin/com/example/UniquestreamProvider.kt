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

            val apiHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
                "Accept" to "application/json, text/plain, */*",
                "Referer" to "$mainUrl/",
                "Origin" to mainUrl,
                "X-Requested-With" to "XMLHttpRequest" // Esto ayuda mucho con las APIs
            )

            // Cambiamos el endpoint a uno más probable de funcionar para la Home
            val response = app.get("$apiUrl/search?query=&limit=20&order_by=popular", headers = apiHeaders).text
            val data = AppUtils.parseJson<SearchRoot>(response)

            val homeItems = mutableListOf<HomePageList>()

            // Verificamos si hay series
            val seriesList = data.series?.map {
                newAnimeSearchResponse(it.title, it.content_id) {
                    this.posterUrl = it.image
                }
            }

            if (!seriesList.isNullOrEmpty()) {
                homeItems.add(HomePageList("Series Destacadas", seriesList))
            } else {
                Log.e(TAG, "La API no devolvió series para la Home")
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

        val apiHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
            "Referer" to "https://anime.uniquestream.net/series/$cleanId",
            "Accept" to "application/json",
            "X-Requested-With" to "XMLHttpRequest"
        )

        val seriesResponse = app.get("$apiUrl/series/$cleanId", headers = apiHeaders).text
        val details = AppUtils.parseJson<DetailsResponse>(seriesResponse)
        val episodesList = mutableListOf<Episode>()
        val processedSeasonIds = mutableSetOf<String>()

        details.seasons?.forEach { season ->
            if (processedSeasonIds.contains(season.content_id)) return@forEach
            processedSeasonIds.add(season.content_id)

            // BUCLE DE PAGINACIÓN: Pedimos de 20 en 20
            var page = 1
            var keepLoading = true

            while (keepLoading) {
                try {
                    val seasonUrl = "$apiUrl/season/${season.content_id}/episodes?page=$page&limit=20&order_by=asc"
                    val response = app.get(seasonUrl, headers = apiHeaders).text

                    if (response.trim().startsWith("[")) {
                        val eps = AppUtils.parseJson<List<EpisodeItem>>(response)

                        if (eps.isEmpty()) {
                            keepLoading = false
                        } else {
                            eps.forEach { ep ->
                                // DENTRO DE eps.forEach en la función load:
                                if (ep.is_clip != true) {
                                    // PASAMOS EL ID COMPLETO (sin hacer split)
                                    val idParaLink = ep.content_id

                                    episodesList.add(newEpisode(idParaLink) {
                                        this.name = ep.title
                                        this.episode = ep.episode_number?.toInt()
                                        this.season = season.season_number
                                        this.posterUrl = ep.image
                                    })
                                }
                            }
                            // Si recibimos menos de 20, es que ya no hay más páginas
                            if (eps.size < 20) {
                                keepLoading = false
                            } else {
                                page++ // Pedir la siguiente página
                            }
                        }
                    } else {
                        keepLoading = false
                        Log.e(TAG, "Respuesta no válida en Temp ${season.season_number} Pág $page")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error en paginación: ${e.message}")
                    keepLoading = false
                }
            }
        }

        Log.d(TAG, "Total episodios cargados: ${episodesList.size}")

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
        val cleanId = data.split("/").last { it.isNotBlank() }
        Log.d(TAG, "Obteniendo enlaces para el episodio: $cleanId")

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
            "Accept" to "application/json",
            "Referer" to "https://anime.uniquestream.net/watch/$cleanId",
            "X-Requested-With" to "XMLHttpRequest"
        )

        return try {
            // 1. Pedimos la info del episodio para sacar todas las versiones disponibles
            // Usamos la ruta de tu segundo curl que es la que trae todo el JSON de versiones
            val mediaUrl = "$apiUrl/episode/$cleanId/media/dash/ja-JP" // ja-JP es el trigger para que suelte el JSON
            val response = app.get(mediaUrl, headers = headers).text

            if (response.contains("versions")) {
                val videoData = AppUtils.parseJson<VideoResponse>(response)
                var linksFound = 0

                // 2. Recorremos las versiones HLS (Audio)
                videoData.versions?.hls?.forEach { v ->
                    // AUDIO ORIGINAL / DOBLADO
                    callback(
                        newExtractorLink(
                            this.name,
                            "${this.name} - Audio: ${v.locale.uppercase()}",
                            v.playlist,
                            "https://anime.uniquestream.net/",
                            ExtractorLinkType.M3U8,
                            headers = headers
                        )
                    )
                    linksFound++

                    // 3. HARDSUBS (Subtítulos pegados que el reproductor lee como una fuente más)
                    v.hard_subs?.forEach { sub ->
                        callback(
                            newExtractorLink(
                                this.name,
                                "${this.name} - ${v.locale.uppercase()} (Subs: ${sub.locale.uppercase()})",
                                sub.playlist,
                                "https://anime.uniquestream.net/",
                                ExtractorLinkType.M3U8,
                                headers = headers
                            )
                        )
                        linksFound++
                    }
                }
                linksFound > 0
            } else {
                Log.e(TAG, "No se encontraron versiones en la respuesta")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error en loadLinks: ${e.message}")
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

    data class VideoResponse(
        val versions: Versions? = null
    )

    data class Versions(
        val hls: List<HlsVersion>? = null
    )

    data class HlsVersion(
        val locale: String,
        val playlist: String,
        val hard_subs: List<HardSub>? = null // ¡AÑADE ESTA LÍNEA!
    )

    data class HardSub(
        val locale: String,
        val playlist: String
    )
}