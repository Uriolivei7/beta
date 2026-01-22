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
        val cleanId = url.split("/").filter { it.isNotEmpty() }.lastOrNull() ?: url

        val apiHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
            "Referer" to "$mainUrl/",
            "Accept" to "application/json"
        )

        // 1. Obtener detalles de la serie
        val response = app.get("$apiUrl/series/$cleanId", headers = apiHeaders).text
        val details = AppUtils.parseJson<DetailsResponse>(response)
        val episodesList = mutableListOf<Episode>()

        // 2. Cargar episodios usando la nueva ruta descubierta
        details.seasons?.forEach { season ->
            try {
                // USAMOS LA RUTA: /api/v1/season/{id}/episodes
                // Agregamos limit=100 para asegurar que traiga todos los episodios
                val seasonUrl = "$apiUrl/season/${season.content_id}/episodes?page=1&limit=100&order_by=asc"
                val seasonEpsResponse = app.get(seasonUrl, headers = apiHeaders).text

                if (seasonEpsResponse.startsWith("[")) {
                    val eps = AppUtils.parseJson<List<EpisodeItem>>(seasonEpsResponse)
                    eps.forEach { ep ->
                        if (ep.is_clip != true) {
                            episodesList.add(newEpisode(ep.content_id) {
                                this.name = ep.title
                                this.episode = ep.episode_number?.toInt()
                                this.season = season.season_number
                                this.posterUrl = ep.image
                            })
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en temporada ${season.season_number}: ${e.message}")
            }
        }

        return newAnimeLoadResponse(details.title ?: "Anime", url, TvType.Anime) {
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

    @Serializable data class ImageItem(val url: String, val type: String)

    @Serializable
    data class DetailsResponse(
        val content_id: String,
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
    data class SearchRoot(
        val series: List<SeriesItem>? = null,
        val episodes: List<EpisodeItem>? = null
    )

    @Serializable
    data class EpisodeItem(
        val series_title: String? = null,
        val content_id: String,
        val title: String? = null,
        val episode_number: Double? = null,
        val image: String? = null,
        val is_clip: Boolean? = false,
        val episode: String? = null
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
}