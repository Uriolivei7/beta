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
                    newAnimeSearchResponse(it.series_title ?: it.title, it.content_id) {
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
        Log.d(TAG, "Cargando detalles ID: $url")
        val detailsJson = app.get("$apiUrl/article?content_id=$url").text
        val details = AppUtils.parseJson<DetailsResponse>(detailsJson)
        val episodesList = mutableListOf<Episode>()

        details.seasons?.forEach { season ->
            try {
                val seasonEpsJson = app.get("$apiUrl/episodes?season_id=${season.content_id}").text
                val eps = AppUtils.parseJson<List<EpisodeItem>>(seasonEpsJson)

                eps.filter { !it.is_clip }.forEach { ep ->
                    episodesList.add(newEpisode(ep.content_id) {
                        this.name = ep.title
                        this.episode = ep.episode_number.toInt()
                        this.season = season.season_number
                        this.posterUrl = ep.image
                    })
                }
            } catch (e: Exception) {
                Log.e(TAG, "Fallo al cargar episodios temporada ${season.season_number}")
            }
        }

        return newAnimeLoadResponse(details.title, url, TvType.Anime) {
            this.posterUrl = details.images?.firstOrNull { it.type == "poster_tall" }?.url
            this.plot = details.description
            this.tags = details.audio_locales
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
            val response = app.get("$apiUrl/video?content_id=$data").text
            val videoData = AppUtils.parseJson<VideoResponse>(response)
            var linksFound = 0

            // --- CORRECCIÓN HLS CON newExtractorLink ---
            videoData.versions?.hls?.forEach { v ->
                Log.d(TAG, "HLS Localidad: ${v.locale}")
                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} HLS ${v.locale}",
                        url = v.playlist,
                        type = ExtractorLinkType.M3U8 // Cambiado a ExtractorLinkType
                    ) {
                        this.referer = mainUrl
                    }
                )
                linksFound++
            }

            // --- CORRECCIÓN DASH CON newExtractorLink ---
            videoData.versions?.dash?.forEach { v ->
                Log.d(TAG, "DASH Localidad: ${v.locale}")
                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} DASH ${v.locale}",
                        url = v.playlist,
                        type = ExtractorLinkType.DASH
                    ) {
                        this.referer = mainUrl
                    }
                )
                linksFound++
            }

            if (linksFound == 0) Log.e(TAG, "No se encontraron links de video válidos")
            linksFound > 0
        } catch (e: Exception) {
            Log.e(TAG, "Error crítico en loadLinks: ${e.message}")
            false
        }
    }

    // --- MODELOS JSON ---
    @Serializable data class SearchRoot(val series: List<SeriesItem>? = null, val episodes: List<EpisodeItem>? = null)
    @Serializable data class SeriesItem(val content_id: String, val title: String, val image: String? = null)

    @Serializable data class DetailsResponse(
        val title: String, val description: String? = null,
        val images: List<ImageItem>? = null, val seasons: List<SeasonItem>? = null,
        val audio_locales: List<String>? = null
    )
    @Serializable data class ImageItem(val url: String, val type: String)
    @Serializable data class SeasonItem(val content_id: String, val season_number: Int)
    @Serializable data class EpisodeItem(val content_id: String, val title: String, val episode_number: Double, val image: String? = null, val is_clip: Boolean, val series_title: String? = null)
    @Serializable data class VideoResponse(val versions: VideoVersions? = null)
    @Serializable data class VideoVersions(val hls: List<VideoV>? = null, val dash: List<VideoV>? = null)
    @Serializable data class VideoV(val locale: String, val playlist: String)
}