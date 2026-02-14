package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AnimeParadiseProvider : MainAPI() {
    override var mainUrl = "https://www.animeparadise.moe"
    private val apiUrl = "https://api.animeparadise.moe"
    override var name = "AnimeParadise"
    override val hasMainPage = true
    override var lang = "es"
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
        } catch (e: Exception) { null }
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
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.substringAfterLast("/")
        Log.d(TAG, "Logs: Iniciando load para slug: $slug")

        return try {
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val internalAnimeId = animeData.data?.id ?: throw Exception("ID no encontrado")

            val epResponse = app.get("$apiUrl/anime/$internalAnimeId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            val episodes = epData.data.map { ep ->
                newEpisode("${ep.id ?: ""}|$internalAnimeId") {
                    this.name = ep.title ?: "Episodio ${ep.number}"
                    this.episode = ep.number?.toIntOrNull() ?: 0
                    this.posterUrl = ep.image
                }
            }.sortedBy { it.episode }

            newAnimeLoadResponse(animeData.data?.title ?: "Anime", url, TvType.Anime) {
                this.posterUrl = animeData.data?.posterImage?.large
                this.plot = animeData.data?.synopsis
                this.tags = animeData.data?.genres
                addEpisodes(DubStatus.Subbed, episodes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en load: ${e.message}"); null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val parts = data.split("|")
        val epUuid = parts.getOrNull(0) ?: ""
        val originId = parts.getOrNull(1) ?: ""

        Log.d(TAG, "Logs: === LOADLINKS (LIMPIEZA DE SUBS Y SERVIDORES) ===")

        return try {
            val watchUrl = "$mainUrl/watch/$epUuid?origin=$originId"
            val actionHeaders = mapOf(
                "next-action" to "604a8a337238f40e9a47f69916d68967b49f8fc44b",
                "content-type" to "text/plain;charset=UTF-8",
                "referer" to watchUrl,
                "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36"
            )

            val response = app.post(watchUrl, headers = actionHeaders,
                requestBody = "[\"$epUuid\",\"$originId\"]".toRequestBody("text/plain".toMediaTypeOrNull())).text

            // 1. FILTRAR SUBTÍTULOS (Solo Español e Inglés, ignorar miniaturas)
            val subRegex = Regex("""https?[:\\/]+[^"\\\s]+?\.vtt""")
            subRegex.findAll(response).map { it.value.replace("\\/", "/") }.distinct().forEach { subUrl ->
                if (!subUrl.contains("thumbnails")) { // Ignorar archivos de miniatura
                    val label = if (subUrl.contains("eng")) "English" else "Spanish"
                    Log.d(TAG, "Logs: Subtítulo válido: $subUrl")
                    subtitleCallback.invoke(newSubtitleFile(label, subUrl))
                }
            }

            // 2. FILTRAR ENLACES (Solo 2 servidores distintos para no saturar)
            val videoRegex = Regex("""https?[:\\/]+[^"\\\s]+master\.m3u8[^"\\\s]*""")
            val links = videoRegex.findAll(response)
                .map { it.value.replace("\\u002F", "/").replace("\\/", "/").replace("\\", "") }
                .distinct()
                .toList()

            links.forEachIndexed { index, rawUrl ->
                // Solo tomamos un link por dominio para evitar repetidos pesados
                if (index > 2) return@forEachIndexed

                val finalUrl = if (rawUrl.contains("stream.animeparadise.moe")) rawUrl
                else "https://stream.animeparadise.moe/m3u8?url=${rawUrl.replace("/", "%2F").replace(":", "%3A")}"

                val serverName = when {
                    rawUrl.contains("windflash") -> "Paradise Wind (Recomendado)"
                    rawUrl.contains("lightning") -> "Paradise Light"
                    else -> "Paradise Mirror ${index + 1}"
                }

                callback.invoke(
                    newExtractorLink(this.name, serverName, finalUrl, ExtractorLinkType.M3U8) {
                        this.quality = Qualities.Unknown.value // M3U8 seleccionará la mejor calidad según el internet
                        this.headers = mapOf("Referer" to "$mainUrl/", "User-Agent" to actionHeaders["user-agent"]!!)
                    }
                )
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en loadLinks: ${e.message}"); false
        }
    }
}

// Clases de datos (Sin cambios para mantener estabilidad)
data class AnimeListResponse(val data: List<AnimeObject>)
data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null,
    val link: String? = null,
    @JsonProperty("synopsys") val synopsis: String? = null,
    val genres: List<String>? = null,
    val posterImage: ImageInfo? = null
)
data class ImageInfo(val original: String? = null, val large: String? = null)
data class AnimeDetailResponse(val data: AnimeObject? = null)
data class EpisodeListResponse(val data: List<Episode>)
data class Episode(
    @JsonProperty("_id") val id: String? = null,
    val number: String? = null,
    val title: String? = null,
    val image: String? = null
)