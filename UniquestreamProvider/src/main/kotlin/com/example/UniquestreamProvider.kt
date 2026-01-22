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

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Accept" to "application/json"
    )

    private fun SeriesItem.toSearchResponse(): SearchResponse {
        return newAnimeSearchResponse(this.title, this.content_id) {
            this.posterUrl = image
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        return try {
            Log.d(TAG, "Cargando MainPage...")

            val response = app.get(
                "$apiUrl/search?query=&limit=20&order_by=popular",
                headers = baseHeaders,
                timeout = 30L
            ).text

            val data = AppUtils.parseJson<SearchRoot>(response)
            val homeItems = mutableListOf<HomePageList>()

            val seriesList = data.series?.map {
                newAnimeSearchResponse(it.title, it.content_id) {
                    this.posterUrl = it.image
                }
            }

            if (!seriesList.isNullOrEmpty()) {
                homeItems.add(HomePageList("Series Destacadas", seriesList))
            }

            newHomePageResponse(homeItems, homeItems.isNotEmpty())
        } catch (e: Exception) {
            Log.e(TAG, "Error en getMainPage: ${e.message}")
            newHomePageResponse(emptyList(), false)
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            val response = app.get(
                "$apiUrl/search?query=$query",
                headers = baseHeaders,
                timeout = 30L
            ).text

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

        val seriesResponse = app.get(
            "$apiUrl/series/$cleanId",
            headers = baseHeaders,
            timeout = 30L
        ).text

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
                    val response = app.get(seasonUrl, headers = baseHeaders, timeout = 30L).text

                    if (response.trim().startsWith("[")) {
                        val eps = AppUtils.parseJson<List<EpisodeItem>>(response)

                        if (eps.isEmpty()) {
                            keepLoading = false
                        } else {
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

                            if (eps.size < 20) {
                                keepLoading = false
                            } else {
                                page++
                            }
                        }
                    } else {
                        keepLoading = false
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
        val episodeId = if (data.contains("/")) {
            data.substringAfterLast("/").trim()
        } else {
            data.trim()
        }

        Log.d(TAG, "========================================")
        Log.d(TAG, "Episode ID: $episodeId")
        Log.d(TAG, "========================================")

        return try {
            val watchUrl = "$mainUrl/watch/$episodeId"

            // PASO 1: Visitar la página watch para obtener cookies de Cloudflare
            Log.d(TAG, "Obteniendo cookies de Cloudflare...")
            val watchHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                "Accept-Language" to "en-US,en;q=0.5",
                "Referer" to mainUrl
            )

            val watchPage = app.get(watchUrl, headers = watchHeaders, timeout = 30L)
            Log.d(TAG, "Página watch cargada: ${watchPage.code}")

            // Extraer cookies importantes de la respuesta
            val cookieHeader = watchPage.headers["set-cookie"] ?: ""
            Log.d(TAG, "Cookies recibidas: ${if(cookieHeader.isNotEmpty()) "Sí" else "No"}")

            // PASO 2: Probar diferentes locales para obtener los links
            val locales = listOf("es-419", "en-US", "ja-JP", "es-ES", "pt-BR")
            var linksEnviados = 0

            for (locale in locales) {
                try {
                    val mediaUrl = "$apiUrl/episode/$episodeId/media/dash/$locale"

                    Log.d(TAG, "Intentando locale: $locale")

                    val apiHeaders = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                        "Accept" to "*/*",
                        "Accept-Language" to "en-US,en;q=0.9",
                        "Referer" to watchUrl,
                        "Origin" to mainUrl,
                        "Sec-Fetch-Dest" to "empty",
                        "Sec-Fetch-Mode" to "cors",
                        "Sec-Fetch-Site" to "same-origin"
                    )

                    val response = app.get(mediaUrl, headers = apiHeaders, timeout = 30L)

                    if (response.code == 200) {
                        Log.d(TAG, "✓ API respondió 200 para $locale")

                        val videoData = AppUtils.parseJson<VideoResponse>(response.text)

                        // Procesar versiones HLS
                        videoData.versions?.hls?.forEach { hlsVersion ->
                            val playlistUrl = hlsVersion.playlist

                            if (playlistUrl.isNotBlank()) {
                                Log.d(TAG, "✓ Link encontrado: ${hlsVersion.locale}")
                                Log.d(TAG, "  URL: ${playlistUrl.take(80)}...")

                                // CRÍTICO: URLs HLS con tokens firmados
                                // Dejar que el reproductor use sus headers por defecto
                                callback(
                                    newExtractorLink(
                                        source = this.name,
                                        name = "${this.name} - ${hlsVersion.locale.uppercase()}",
                                        url = playlistUrl,
                                        type = ExtractorLinkType.M3U8
                                    ) {
                                        this.quality = Qualities.Unknown.value
                                        // Solo Referer, sin sobrescribir otros headers
                                        this.referer = "$mainUrl/"
                                    }
                                )
                                linksEnviados++

                                // Procesar subtítulos
                                hlsVersion.subtitles?.forEach { sub ->
                                    if (sub.url.isNotBlank()) {
                                        subtitleCallback(
                                            newSubtitleFile(
                                                lang = sub.locale,
                                                url = sub.url
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Si encontramos links, no necesitamos probar más locales
                        if (linksEnviados > 0) break
                    } else {
                        Log.w(TAG, "API devolvió ${response.code} para $locale")
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Error con locale $locale: ${e.message}")
                    continue
                }
            }

            Log.d(TAG, "========================================")
            Log.d(TAG, "TOTAL LINKS: $linksEnviados")
            Log.d(TAG, "========================================")

            linksEnviados > 0

        } catch (e: Exception) {
            Log.e(TAG, "ERROR CRÍTICO: ${e.message}")
            e.printStackTrace()
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