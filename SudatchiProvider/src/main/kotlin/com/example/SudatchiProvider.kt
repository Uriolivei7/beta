package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URLEncoder

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
    }

    private val apiHeaders = mapOf(
        "Accept" to "*/*",
        "Origin" to mainUrl,
        "Referer" to "$mainUrl/",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
        "Accept-Language" to "es-ES,es;q=0.9",
        "Cache-Control" to "no-cache",
        "Pragma" to "no-cache"
    )

    override val mainPage = mainPageOf(
        "$apiUrl/series?page=1&sort=POPULARITY_DESC" to "Más Populares",
        "$apiUrl/series?page=1&sort=TRENDING_DESC" to "En Tendencia"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        return try {
            val response = app.get(request.data, headers = apiHeaders).text
            val homeItems = mutableListOf<SearchResponse>()

            val data: SeriesDto = mapper.readValue(response)
            data.results?.forEach { anime ->
                val title = anime.title?.english ?: anime.title?.romaji ?: "Unknown"
                homeItems.add(newAnimeSearchResponse(title, "$mainUrl/anime/${anime.id}") {
                    this.posterUrl = anime.coverImage
                })
            }

            newHomePageResponse(listOf(HomePageList(request.name, homeItems)), true)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val id = url.substringAfterLast("/")
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

                val episodesList = data.episodes?.map { ep ->
                    val fullThumbUrl = ep.imgUrl?.let { path ->
                        if (path.startsWith("http")) path
                        else "$mainUrl${if (path.startsWith("/")) "" else "/"}$path"
                    }

                    Log.d("Sudatchi", "Logs: Generando episodio ${ep.number} - Imagen final: $fullThumbUrl")

                    newEpisode("$apiUrl/streams?episodeId=${ep.id ?: 0}") {
                        this.name = ep.title
                        this.episode = ep.number
                        this.posterUrl = fullThumbUrl
                    }
                }?.sortedBy { it.episode } ?: emptyList()

                addEpisodes(DubStatus.Subbed, episodesList)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en load: ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "Logs: === DEBUG SUBTITULOS === Data: $data")

        return try {
            val encodedSubs = data.substringAfter("&subs=", "")
            if (encodedSubs.isNotEmpty()) {
                val decodedSubs = java.net.URLDecoder.decode(encodedSubs, "UTF-8")
                Log.d(TAG, "Logs: JSON Decodificado: $decodedSubs")

                val subs: List<SubtitleDto> = mapper.readValue(decodedSubs)

                subs.forEach { sub ->
                    // Limpiamos bien la ruta
                    val cleanHash = sub.url.removePrefix("/ipfs/").removePrefix("/")

                    // Opción A: Usar el proxy del sitio
                    val subUrl = "$mainUrl/api/proxy/$cleanHash"

                    // Opción B (Respaldo): Gateway oficial de IPFS si el anterior falla
                    // val subUrl = "https://ipfs.io/ipfs/$cleanHash"

                    val label = sub.subtitlesName?.name ?: "Sub - ${sub.subtitlesName?.language}"

                    Log.d(TAG, "Logs: Intentando cargar sub: [$label]")
                    Log.d(TAG, "Logs: URL generada: $subUrl")

                    if (subUrl.isNotBlank()) {
                        subtitleCallback.invoke(
                            newSubtitleFile(label, subUrl)
                        )
                        Log.d(TAG, "Logs: subtitleCallback invocado exitosamente para $label")
                    }
                }
            }

            val cleanVideoUrl = data.substringBefore("&subs=")
            callback.invoke(
                newExtractorLink(this.name, "Sudatchi", cleanVideoUrl, ExtractorLinkType.M3U8) {
                    this.referer = "$mainUrl/"
                    this.headers = apiHeaders
                }
            )
            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: ERROR FATAL en subs: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            val response = app.get("$apiUrl/series?search=$query&page=1", headers = apiHeaders).text
            val data: SeriesDto = mapper.readValue(response)
            data.results?.map { anime ->
                newAnimeSearchResponse(anime.title?.english ?: anime.title?.romaji ?: "Unknown", "$mainUrl/anime/${anime.id}") {
                    this.posterUrl = anime.coverImage
                }
            } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

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
        val genres: List<String>? = null,
        val year: Int? = null,
        val episodes: List<EpisodeDto>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EpisodeDto(
        val id: Int? = null,
        val number: Int? = null,
        val title: String? = null,
        val imgUrl: String? = null,
        val subtitlesDto: List<SubtitleDto>? = null,
        val subtitles: List<SubtitleDto>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SubtitleDto(
        val url: String,
        @JsonProperty("SubtitlesName")
        val subtitlesName: SubtitlesNameDto? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SubtitlesNameDto(
        val name: String? = null,
        val language: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class LangDto(val name: String? = null)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CoverDto(val extraLarge: String? = null)
}