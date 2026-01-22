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
                "X-Requested-With" to "XMLHttpRequest"
            )

            val response = app.get("$apiUrl/search?query=&limit=20&order_by=popular", headers = apiHeaders).text
            val data = AppUtils.parseJson<SearchRoot>(response)

            val homeItems = mutableListOf<HomePageList>()

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
                                if (ep.is_clip != true) {
                                    // CRÍTICO: Pasar SOLO el content_id, sin modificaciones
                                    episodesList.add(newEpisode(ep.content_id) {
                                        this.name = ep.title
                                        this.episode = ep.episode_number?.toInt()
                                        this.season = season.season_number
                                        this.posterUrl = ep.image
                                    })
                                }
                            }

                            if (eps.size < 20) {
                                keepLoading = false
                            } else {
                                page++
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
        // El 'data' ya viene como content_id limpio desde load()
        val episodeId = data.trim()

        val chromeUA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

        Log.d(TAG, "========================================")
        Log.d(TAG, "CARGANDO LINKS PARA EPISODIO: $episodeId")
        Log.d(TAG, "========================================")

        return try {
            // PASO 1: Visitar la página del watch para obtener cookies/tokens
            val watchUrl = "https://anime.uniquestream.net/watch/$episodeId"
            Log.d(TAG, "Visitando página: $watchUrl")

            val watchPage = app.get(watchUrl, headers = mapOf(
                "User-Agent" to chromeUA,
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
                "Accept-Language" to "en-US,en;q=0.5",
                "Connection" to "keep-alive",
                "Upgrade-Insecure-Requests" to "1"
            ))

            Log.d(TAG, "Página cargada - Status: ${watchPage.code}")

            // PASO 2: Ahora pedir los links de la API con las cookies obtenidas
            val mediaUrl = "$apiUrl/episode/$episodeId/media"

            Log.d(TAG, "URL API: $mediaUrl")

            val response = app.get(mediaUrl, headers = mapOf(
                "User-Agent" to chromeUA,
                "Referer" to "https://anime.uniquestream.net/",
                "Accept" to "application/json",
                "X-Requested-With" to "XMLHttpRequest"
            ))

            Log.d(TAG, "Status Code: ${response.code}")

            if (response.code != 200) {
                Log.e(TAG, "Error HTTP: ${response.code}")
                return false
            }

            val videoData = AppUtils.parseJson<VideoResponse>(response.text)
            var linksEnviados = 0

            // Procesar versiones HLS
            videoData.versions?.hls?.forEach { hlsVersion ->
                val playlistUrl = hlsVersion.playlist

                // Validar que la URL no esté vacía
                if (playlistUrl.isBlank()) {
                    Log.w(TAG, "URL vacía para locale: ${hlsVersion.locale}")
                    return@forEach
                }

                Log.d(TAG, "✓ Link ${hlsVersion.locale}: $playlistUrl")

                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} - ${hlsVersion.locale.uppercase()}",
                        url = playlistUrl,
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.quality = Qualities.Unknown.value
                        this.referer = "https://anime.uniquestream.net/"
                        this.headers = mapOf(
                            "User-Agent" to chromeUA,
                            "Referer" to "https://anime.uniquestream.net/",
                            "Origin" to "https://anime.uniquestream.net",
                            "Accept" to "*/*",
                            "Accept-Language" to "en-US,en;q=0.9",
                            "Connection" to "keep-alive"
                        )
                    }
                )
                linksEnviados++

                // Procesar subtítulos si existen
                hlsVersion.subtitles?.forEach { sub ->
                    if (sub.url.isNotBlank()) {
                        subtitleCallback(
                            newSubtitleFile(
                                lang = sub.locale,
                                url = sub.url
                            )
                        )
                        Log.d(TAG, "✓ Subtítulo ${sub.locale}: ${sub.url}")
                    }
                }
            }

            Log.d(TAG, "========================================")
            Log.d(TAG, "TOTAL LINKS ENVIADOS: $linksEnviados")
            Log.d(TAG, "========================================")

            linksEnviados > 0

        } catch (e: Exception) {
            Log.e(TAG, "========================================")
            Log.e(TAG, "ERROR CRÍTICO: ${e.message}")
            Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
            Log.e(TAG, "========================================")
            false
        }
    }

    @Serializable
    data class SeriesItem(
        val content_id: String,
        val title: String,
        val image: String? = null
    )

    @Serializable
    data class SearchRoot(
        val series: List<SeriesItem>? = null,
        val episodes: List<EpisodeItem>? = null
    )

    @Serializable
    data class DetailsResponse(
        val content_id: String? = null,
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
        val series_id: String? = null,
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

    @Serializable
    data class VideoResponse(
        val versions: Versions? = null
    )

    @Serializable
    data class Versions(
        val hls: List<HlsVersion>? = null,
        val dash: List<DashVersion>? = null
    )

    @Serializable
    data class HlsVersion(
        val locale: String,
        val playlist: String,
        val subtitles: List<SubtitleItem>? = null
    )

    @Serializable
    data class DashVersion(
        val locale: String,
        val playlist: String
    )

    @Serializable
    data class SubtitleItem(
        val locale: String,
        val url: String
    )
}