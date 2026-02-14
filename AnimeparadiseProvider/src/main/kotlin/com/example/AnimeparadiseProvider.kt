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
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
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
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
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
                // Guardamos el ID y el origen del anime
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
        val parts = data.split("|")
        val episodeUuid = parts.getOrNull(0)?.substringAfterLast("/")?.substringBefore("?") ?: ""
        val animeOriginId = parts.getOrNull(1) ?: ""

        Log.d(TAG, "Logs: === INICIO LOADLINKS (PLAYER API) ===")

        return try {
            // Este es el endpoint que alimenta al reproductor que viste en el curl
            val playerUrl = "$apiUrl/anime/episode/$episodeUuid/player?origin=$animeOriginId"
            Log.d(TAG, "Logs: Consultando Player API: $playerUrl")

            val response = app.get(playerUrl, headers = apiHeaders).text

            // Log para ver qué nos devuelve realmente la API de player
            Log.d(TAG, "Logs: Respuesta Player: ${response.take(200)}")

            // Buscamos el link de windflash o cualquier m3u8 dentro del JSON
            val videoRegex = Regex("""https?[:\\/]+[^"\\\s]+master\.m3u8[^"\\\s]*|https?[:\\/]+[^"\\\s]+index[^\s"\\\\]*\.m3u8[^"\\\s]*""")
            val videoMatches = videoRegex.findAll(response).map {
                it.value.replace("\\u002F", "/").replace("\\/", "/").replace("\\", "")
            }.distinct()

            var foundLinks = false
            videoMatches.forEach { rawUrl ->
                foundLinks = true

                // Si el link no viene ya con el proxy de stream.animeparadise, se lo ponemos
                val finalUrl = if (rawUrl.contains("stream.animeparadise.moe")) {
                    rawUrl
                } else {
                    "https://stream.animeparadise.moe/m3u8?url=${rawUrl.replace("/", "%2F").replace(":", "%3A")}"
                }

                Log.d(TAG, "Logs: ¡Enlace extraído!: $finalUrl")

                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = "AnimeParadise Mirror",
                        url = finalUrl,
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.quality = Qualities.P1080.value
                        // Usamos los headers exactos de tu curl
                        this.headers = mapOf(
                            "Origin" to "https://www.animeparadise.moe",
                            "Referer" to "https://www.animeparadise.moe/",
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36"
                        )
                    }
                )
            }

            // Extraer subtítulos si existen en el player
            Regex("""\{"src":"([^"]+)","label":"([^"]+)"""").findAll(response).forEach { match ->
                val src = match.groupValues[1].replace("\\/", "/")
                val label = match.groupValues[2]
                val subUrl = if (src.startsWith("http")) src else "https://api.animeparadise.moe/stream/file/$src"
                subtitleCallback.invoke(newSubtitleFile(label, subUrl))
            }

            if (!foundLinks) Log.e(TAG, "Logs: No se encontraron enlaces en la Player API")

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en loadLinks: ${e.message}")
            false
        }
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