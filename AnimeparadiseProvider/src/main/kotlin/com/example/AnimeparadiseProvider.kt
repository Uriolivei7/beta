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

        Log.d(TAG, "Logs: === EXTRACCIÓN DESDE NEXT.JS DATA ===")

        return try {
            // 1. Cargamos la página tal cual me la pasaste
            val watchUrl = "$mainUrl/watch/$epUuid?origin=$originId"
            val response = app.get(watchUrl).text

            // 2. Limpieza profunda: En Next.js las URLs vienen con muchas barras extra: \\\/
            val cleanData = response
                .replace("\\\\", "")
                .replace("\\u002F", "/")
                .replace("\\/", "/")

            // 3. Buscar Subtítulos (VTT)
            val subRegex = Regex("""https?://[^\s"\\,]+?\.vtt""")
            subRegex.findAll(cleanData).map { it.value }.distinct().forEach { subUrl ->
                if (!subUrl.contains("thumbnails")) {
                    val label = if (subUrl.contains("eng")) "English" else "Spanish"
                    subtitleCallback.invoke(SubtitleFile(label, subUrl))
                }
            }

            // 4. Buscar Enlaces de Video (M3U8)
            // Buscamos cualquier m3u8 que esté en los scripts de la página
            val videoRegex = Regex("""https?://[^\s"\\,]+?\.m3u8[^\s"\\,]*""")
            val links = videoRegex.findAll(cleanData).map { it.value }.distinct().toList()

            if (links.isEmpty()) {
                Log.e(TAG, "Logs: No se encontraron links en el HTML de Next.js. Longitud: ${response.length}")
                // Intentamos una última búsqueda por si están en el formato de "Paradise"
                return false
            }

            links.take(2).forEachIndexed { index, rawUrl ->
                // Si el link no es directo, lo pasamos por el proxy de la web
                val finalUrl = if (rawUrl.contains("stream.animeparadise.moe")) rawUrl
                else "https://stream.animeparadise.moe/m3u8?url=${rawUrl.replace("/", "%2F").replace(":", "%3A")}"

                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = "Paradise Player ${index + 1}",
                        url = finalUrl,
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.quality = Qualities.Unknown.value
                        this.referer = "$mainUrl/"
                    }
                )
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en extracción NextJS: ${e.message}")
            false
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