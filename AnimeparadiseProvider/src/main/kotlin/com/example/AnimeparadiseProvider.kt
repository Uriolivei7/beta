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
        Log.d(TAG, "Logs: Iniciando getMainPage")
        return try {
            val url = "$apiUrl/?sort=%7B%22rate%22:-1%7D"
            val response = app.get(url, headers = apiHeaders).text
            val resData: AnimeListResponse = mapper.readValue(response)

            val animeList = resData.data.map {
                newAnimeSearchResponse(it.title ?: "Sin título", it.link ?: "", TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
            Log.d(TAG, "Logs: getMainPage exitoso, encontrados ${animeList.size} animes")
            newHomePageResponse(listOf(HomePageList("Animes Populares", animeList)), true)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando anime: $query")
        return try {
            val response = app.get("$apiUrl/?title=$query", headers = apiHeaders).text
            val resData: AnimeListResponse = mapper.readValue(response)

            resData.data.map {
                newAnimeSearchResponse(it.title ?: "Sin título", it.link ?: "", TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.substringAfterLast("/")
        Log.d(TAG, "Logs: Iniciando load para: $slug")

        return try {
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val internalId = animeData.data?.id ?: throw Exception("ID no encontrado")

            val epResponse = app.get("$apiUrl/anime/$internalId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            val episodes = epData.data.map { ep ->
                // Guardamos el ID tal cual viene de la API
                val epUuid = ep.id ?: ""
                newEpisode("$epUuid|$internalId") {
                    this.name = ep.title ?: "Episodio ${ep.number}"
                    this.episode = ep.number?.toIntOrNull() ?: 0
                    this.posterUrl = ep.image
                }
            }.sortedBy { it.episode }

            Log.d(TAG, "Logs: Load exitoso: ${episodes.size} episodios encontrados")

            newAnimeLoadResponse(animeData.data?.title ?: "Sin título", url, TvType.Anime) {
                this.posterUrl = animeData.data?.posterImage?.large
                this.plot = animeData.data?.synopsis
                this.tags = animeData.data?.genres
                this.year = animeData.data?.animeSeason?.year
                this.showStatus = if (animeData.data?.status == "finished") ShowStatus.Completed else ShowStatus.Ongoing
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
        val rawEpId = parts.getOrNull(0) ?: ""
        val animeOriginId = parts.getOrNull(1) ?: ""

        val episodeUuid = if (rawEpId.contains("/")) {
            rawEpId.substringAfterLast("/").substringBefore("?")
        } else {
            rawEpId
        }

        val watchUrl = "$mainUrl/watch/$episodeUuid?origin=$animeOriginId"

        Log.d(TAG, "Logs: === INICIO LOADLINKS ===")
        Log.d(TAG, "Logs: URL Scraping: $watchUrl")

        if (episodeUuid.isBlank()) {
            Log.e(TAG, "Logs: Error - Episode UUID está vacío")
            return false
        }

        return try {
            val response = app.get(watchUrl, headers = apiHeaders).text

            // MEJORA: Regex que detecta m3u8 incluso con barras escapadas \/ o \u002F
            val videoRegex = Regex("""https?[:\\]+[^"\\\s]+master\.m3u8[^"\\\s]*""")
            val videoMatches = videoRegex.findAll(response).map {
                it.value.replace("\\u002F", "/").replace("\\/", "/").replace("\\", "")
            }.distinct()

            var foundLinks = false
            videoMatches.forEach { rawUrl ->
                foundLinks = true
                Log.d(TAG, "Logs: Enlace m3u8 encontrado: $rawUrl")
                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = "AnimeParadise Player",
                        url = "https://stream.animeparadise.moe/m3u8?url=$rawUrl",
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.quality = Qualities.P1080.value
                        this.headers = mapOf(
                            "Origin" to "https://www.animeparadise.moe",
                            "Referer" to "$mainUrl/",
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        )
                    }
                )
            }

            // Subtítulos
            Log.d(TAG, "Logs: Buscando subtítulos en el HTML...")
            Regex("""\{"src":"([^"]+)","label":"([^"]+)","type":"[^"]+"\}""").findAll(response).forEach { match ->
                val src = match.groupValues[1].replace("\\u002F", "/").replace("\\/", "/")
                val label = match.groupValues[2]
                val subUrl = if (src.startsWith("http")) src else "https://api.animeparadise.moe/stream/file/$src"
                Log.d(TAG, "Logs: Subtítulo añadido: $label")
                subtitleCallback.invoke(newSubtitleFile(label, subUrl))
            }

            if (!foundLinks) {
                Log.e(TAG, "Logs: No se encontraron enlaces de video en el HTML")
                // Log de emergencia para ver si el HTML cargó algo útil o un error
                Log.d(TAG, "Logs: Fragmento HTML: ${response.take(300)}")
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error crítico en loadLinks: ${e.message}")
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
    val id: String? = null,
    val number: String? = null,
    val title: String? = null,
    val image: String? = null
)