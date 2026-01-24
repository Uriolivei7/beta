package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class SudatchiProvider : MainAPI() {
    override var mainUrl = "https://sudatchi.com"
    private val apiUrl = "https://sudatchi.com/api"
    override var name = "Sudatchi"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val TAG = "Sudatchi"

    private val mapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
    }

    private val apiHeaders = mapOf(
        "Accept" to "application/json, text/plain, */*",
        "Origin" to mainUrl,
        "Referer" to "$mainUrl/",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
    )

    override val mainPage = mainPageOf(
        "$apiUrl/series?page=1&sort=POPULARITY_DESC" to "Más Popularesr",
        "$apiUrl/series?page=1&sort=TRENDING_DESC" to "En Tendencia"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: Iniciando getMainPage para ${request.name}")
        return try {
            val response = app.get(request.data, headers = apiHeaders).text
            val homeItems = mutableListOf<SearchResponse>()

            if (request.data.contains("/home")) {
                val data: HomePageDto = mapper.readValue(response)
                data.latestEpisodes?.forEach { ep ->
                    val title = ep.animeTitle?.english ?: ep.animeTitle?.romaji ?: ep.title ?: "Unknown"
                    homeItems.add(newAnimeSearchResponse(title, "$mainUrl/anime/${ep.animeId}") {
                        this.posterUrl = if (ep.coverImage?.startsWith("http") == true) ep.coverImage else "$mainUrl${ep.coverImage}"
                    })
                }
            } else {
                val data: SeriesDto = mapper.readValue(response)
                data.results?.forEach { anime ->
                    val title = anime.title?.english ?: anime.title?.romaji ?: "Unknown"
                    homeItems.add(newAnimeSearchResponse(title, "$mainUrl/anime/${anime.id}") {
                        this.posterUrl = anime.coverImage
                    })
                }
            }

            val list = HomePageList(request.name, homeItems)
            newHomePageResponse(listOf(list), true)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val id = url.substringAfterLast("/")
        Log.d(TAG, "Logs: Cargando detalles de ID: $id")

        return try {
            val response = app.get("$apiUrl/anime/$id", headers = apiHeaders).text
            val data: AnimeDetailDto = mapper.readValue(response)
            val title = data.title?.english ?: data.title?.romaji ?: "Unknown"

            newAnimeLoadResponse(title, url, TvType.Anime) {
                this.posterUrl = data.coverImage?.extraLarge ?: data.bannerImage
                this.backgroundPosterUrl = data.bannerImage
                this.year = data.year
                this.plot = data.description
                this.tags = data.genres

                val episodes = data.episodes?.map { ep ->
                    newEpisode("$apiUrl/streams?episodeId=${ep.id}") {
                        this.name = ep.title
                        this.episode = ep.number
                        this.posterUrl = if (ep.coverImage?.startsWith("http") == true) ep.coverImage else "$mainUrl${ep.coverImage}"
                    }
                }?.sortedBy { it.episode } ?: emptyList()

                addEpisodes(DubStatus.Subbed, episodes)

                data.trailer?.let {
                    if (it.site == "youtube") addTrailer("https://www.youtube.com/watch?v=${it.id}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en load: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando anime: $query")
        return try {
            val url = "$apiUrl/series?search=$query&page=1"
            val response = app.get(url, headers = apiHeaders).text
            val data: SeriesDto = mapper.readValue(response)

            data.results?.map { anime ->
                val title = anime.title?.english ?: anime.title?.romaji ?: "Unknown"
                newAnimeSearchResponse(title, "$mainUrl/anime/${anime.id}") {
                    this.posterUrl = anime.coverImage
                }
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en búsqueda: ${e.message}")
            emptyList()
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "Logs: === INICIO LOADLINKS SUDATCHI === URL: $data")

        return try {
            val response = app.get(data, headers = apiHeaders).text
            val streamData: StreamResponse = mapper.readValue(response)

            streamData.subtitles?.forEach { sub ->
                val subUrl = if (sub.src.startsWith("http")) sub.src else "$mainUrl${sub.src}"
                Log.d(TAG, "Logs: Subtítulo encontrado: ${sub.label} -> $subUrl")
                subtitleCallback.invoke(
                    newSubtitleFile(sub.label ?: "English", subUrl)
                )
            }

            val videoUrl = streamData.uri ?: ""

            if (videoUrl.isNotEmpty()) {
                Log.d(TAG, "Logs: URL de video detectada: $videoUrl")

                M3u8Helper.generateM3u8(
                    name,
                    videoUrl,
                    mainUrl,
                    headers = apiHeaders
                ).forEach { link ->
                    callback.invoke(link)
                }
            } else {
                Log.e(TAG, "Logs: No se encontró URI de video en la respuesta")
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en LoadLinks de Sudatchi: ${e.message}")
            false
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class StreamResponse(
        val uri: String? = null,
        val subtitles: List<SubtitleEntry>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SubtitleEntry(
        val src: String,
        val label: String? = null,
        val kind: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class HomePageDto(val latestEpisodes: List<EpisodeDto>? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SeriesDto(val results: List<AnimeDto>? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AnimeDto(
        val id: String? = null,
        val title: TitleDto? = null,
        val coverImage: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TitleDto(val romaji: String? = null, val english: String? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AnimeDetailDto(
        val title: TitleDto? = null,
        val description: String? = null,
        val coverImage: CoverDto? = null,
        val bannerImage: String? = null,
        @JsonProperty("genres") val genres: List<String>? = null,
        val year: Int? = null,
        val studio: String? = null,
        val episodes: List<EpisodeDto>? = null,
        val trailer: TrailerDto? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EpisodeDto(
        val id: Int? = null,
        val number: Int? = null,
        val title: String? = null,
        val animeId: Int? = null,
        val coverImage: String? = null,
        val animeTitle: TitleDto? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CoverDto(val extraLarge: String? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TrailerDto(val id: String? = null, val site: String? = null)
}