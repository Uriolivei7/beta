package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class AnimeParadiseProvider : MainAPI() {
    override var mainUrl = "https://www.animeparadise.moe"
    private val apiUrl = "https://api.animeparadise.moe"
    override var name = "AnimeParadise"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val TAG = "AnimeParadise"

    private val mapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val apiHeaders = mapOf(
        "Accept" to "application/json, text/plain, */*",
        "Origin" to mainUrl,
        "Referer" to "$mainUrl/",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        return try {
            val url = "$apiUrl/?sort=%7B%22rate%22:-1%7D"
            val response = app.get(url, headers = apiHeaders).text
            val resData: AnimeListResponse = mapper.readValue(response)

            val animeList = resData.data.map {
                newAnimeSearchResponse(it.title ?: "Sin título", it.link ?: "", TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
            newHomePageResponse(listOf(HomePageList("Animes Populares", animeList)), true)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            val response = app.get("$apiUrl/?title=$query", headers = apiHeaders).text
            val resData: AnimeListResponse = mapper.readValue(response)

            resData.data.map {
                newAnimeSearchResponse(it.title ?: "Sin título", it.link ?: "", TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.substringAfterLast("/")
        return try {
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val data = animeData.data!!
            val internalId = data.id ?: throw Exception("ID no encontrado")

            val epResponse = app.get("$apiUrl/anime/$internalId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            val episodes = epData.data.map { ep ->
                val epUuid = ep.id ?: ""
                newEpisode("$epUuid|$internalId") {
                    this.name = ep.title ?: "Episodio ${ep.number}"
                    this.episode = ep.number?.toIntOrNull() ?: 0
                    this.posterUrl = ep.image
                }
            }.sortedBy { it.episode }

            newAnimeLoadResponse(data.title ?: "Sin título", url, TvType.Anime) {
                this.posterUrl = data.posterImage?.large
                this.plot = data.synopsis
                this.tags = data.genres
                this.year = data.animeSeason?.year
                this.showStatus = if (data.status == "finished") ShowStatus.Completed else ShowStatus.Ongoing
                addEpisodes(DubStatus.Subbed, episodes)
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val parts = data.split("|")
        val id = parts.getOrNull(0) ?: ""
        val origin = parts.getOrNull(1) ?: ""

        Log.d(TAG, "Logs: === INICIO MODO MULTI-RUTA ===")

        // Lista de rutas posibles a probar
        val potentialEndpoints = listOf(
            "$apiUrl/episode/$id/player?origin=$origin",
            "$apiUrl/anime/episode/$id/stream?origin=$origin",
            "$apiUrl/stream/$id?origin=$origin"
        )

        val videoRegex = Regex("""https?[:\\/]+[^"\\\s]+index[^\s"\\\\]*\.m3u8[^"\\\s]*|https?[:\\/]+[^"\\\s]+master\.m3u8[^"\\\s]*""")

        for (url in potentialEndpoints) {
            try {
                Log.d(TAG, "Logs: Probando ruta: $url")
                val response = app.get(url, headers = apiHeaders)

                if (response.code == 200) {
                    val text = response.text
                    val matches = videoRegex.findAll(text).map {
                        it.value.replace("\\u002F", "/").replace("\\/", "/").replace("\\", "")
                    }.distinct().toList()

                    if (matches.isNotEmpty()) {
                        Log.d(TAG, "Logs: ¡Bingo! Links encontrados en: $url")
                        matches.forEach { rawUrl ->
                            val finalUrl = if (rawUrl.contains("stream.animeparadise.moe")) rawUrl
                            else "https://stream.animeparadise.moe/m3u8?url=${rawUrl.replace("/", "%2F").replace(":", "%3A")}"

                            callback.invoke(
                                newExtractorLink(this.name, "AnimeParadise Mirror", finalUrl, ExtractorLinkType.M3U8) {
                                    this.quality = Qualities.P1080.value
                                    this.headers = apiHeaders
                                }
                            )
                        }
                        return true
                    }
                } else {
                    Log.d(TAG, "Logs: Ruta fallida (Código ${response.code})")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Logs: Error probando $url: ${e.message}")
            }
        }

        Log.e(TAG, "Logs: Ninguna ruta de API funcionó.")
        return false
    }
}

// --- DATA CLASSES ---
data class AnimeListResponse(val data: List<AnimeObject>)
data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null,
    val link: String? = null,
    val status: String? = null,
    @JsonProperty("synopsys") val synopsis: String? = null,
    val genres: List<String>? = null,
    val animeSeason: SeasonInfo? = null,
    val posterImage: ImageInfo? = null
)
data class SeasonInfo(val year: Int? = null)
data class ImageInfo(val original: String? = null, val large: String? = null)
data class AnimeDetailResponse(val data: AnimeObject? = null)
data class EpisodeListResponse(val data: List<Episode>)
data class Episode(
    @JsonProperty("_id") val id: String? = null,
    val number: String? = null,
    val title: String? = null,
    val image: String? = null
)