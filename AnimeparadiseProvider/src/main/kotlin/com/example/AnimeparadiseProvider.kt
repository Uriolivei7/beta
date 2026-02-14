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
        Log.d(TAG, "Logs: Iniciando load para: $slug")

        return try {
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val internalId = animeData.data?.id ?: throw Exception("ID no encontrado")

            val epResponse = app.get("$apiUrl/anime/$internalId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            val episodes = epData.data.map { ep ->
                // Si el ID viene como URL o nulo, intentamos capturar el UUID real
                val rawId = ep.id ?: ""
                val cleanEpId = if (rawId.contains("http")) {
                    rawId.substringAfterLast("/").substringBefore("?")
                } else {
                    rawId
                }

                val num = ep.number?.toIntOrNull() ?: 0

                newEpisode("$cleanEpId|$internalId") {
                    this.name = ep.title ?: "Episodio ${ep.number}"
                    this.episode = num
                    this.posterUrl = ep.image
                }
            }.filter { it.data.split("|")[0].isNotBlank() } // Evita episodios sin ID
                .sortedBy { it.episode }

            newAnimeLoadResponse(animeData.data?.title ?: "Sin título", url, TvType.Anime) {
                this.posterUrl = animeData.data?.posterImage?.large
                this.plot = animeData.data?.synopsis // Corregido: synopsis
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
        val episodeUuid = parts[0].substringAfterLast("/").substringBefore("?")
        val animeOriginId = parts.getOrNull(1) ?: ""

        // Esta es la URL que me pasaste tú y que funciona en el navegador
        val watchUrl = "$mainUrl/watch/$episodeUuid?origin=$animeOriginId"
        Log.d(TAG, "Logs: === EXTRACCIÓN DIRECTA === URL: $watchUrl")

        return try {
            val response = app.get(watchUrl, headers = apiHeaders).text

            // 1. Buscamos los links de video dentro del HTML (están en un JSON oculto)
            // Buscamos cualquier cosa que diga "https://...master.m3u8" o similares
            val videoRegex = Regex("""https?://[a-zA-Z0-9.-]+/[^"\\\s]+master\.m3u8[^"\\\s]*""")
            val videoMatches = videoRegex.findAll(response).map { it.value.replace("\\", "") }.distinct()

            var foundAny = false

            videoMatches.forEach { rawUrl ->
                foundAny = true
                Log.d(TAG, "Logs: Enlace encontrado en HTML: $rawUrl")

                // Aplicamos el proxy de tus curls
                val finalUrl = "https://stream.animeparadise.moe/m3u8?url=$rawUrl"

                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = "AnimeParadise Player",
                        url = finalUrl,
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

            // 2. Extraer Subtítulos (Regex que ya teníamos)
            val subRegex = Regex("""\{"src":"([^"]+)","label":"([^"]+)","type":"[^"]+"\}""")
            subRegex.findAll(response).forEach { match ->
                val src = match.groupValues[1].replace("\\/", "/")
                val subUrl = if (src.startsWith("http")) src else "https://api.animeparadise.moe/stream/file/$src"
                Log.d(TAG, "Logs: Subtítulo encontrado: ${match.groupValues[2]}")
                subtitleCallback.invoke(newSubtitleFile(match.groupValues[2], subUrl))
            }

            if (!foundAny) {
                Log.e(TAG, "Logs: No se encontró ningún m3u8 en el HTML de la página")
                // Si falla, como último recurso intentamos imprimir un trozo del HTML para ver qué hay
                Log.d(TAG, "Logs: Muestra del HTML: ${response.take(500)}")
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en LoadLinks: ${e.message}")
            false
        }
    }

}

// --- DATA CLASSES CORREGIDAS ---

data class AnimeListResponse(val data: List<AnimeObject>)

data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null,
    val link: String? = null,
    val status: String? = null,
    @JsonProperty("synopsys") val synopsis: String? = null, // Mapeo de nombre raro de la API
    val genres: List<String>? = null,
    val animeSeason: SeasonInfo? = null,
    val posterImage: ImageInfo? = null
)

data class SeasonInfo(val year: Int? = null)
data class ImageInfo(val original: String? = null, val large: String? = null)

data class AnimeDetailResponse(val data: AnimeObject? = null)

data class EpisodeListResponse(val data: List<Episode>)

data class Episode(
    @JsonProperty("_id") val id: String? = null, // Prueba con _id (común en bases de datos)
    @JsonProperty("uuid") val uuid: String? = null,
    val number: String? = null,
    val title: String? = null,
    val image: String? = null
)