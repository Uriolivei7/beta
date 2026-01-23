package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AnimeParadiseProvider : MainAPI() {
    override var mainUrl = "https://www.animeparadise.moe"
    private val apiUrl = "https://api.animeparadise.moe"
    override var name = "AnimeParadise"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val TAG = "AnimeParadise"

    // Código actualizado
    private val mapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
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
                // USAMOS EL SLUG (it.link) COMO IDENTIFICADOR ÚNICO
                val slug = it.link ?: ""
                newAnimeSearchResponse(it.title ?: "Sin título", slug, TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
            Log.d(TAG, "Logs: getMainPage exitoso, encontrados ${animeList.size} animes")
            newHomePageResponse(listOf(HomePageList("Popular Anime", animeList)), true)
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

            val results = resData.data.map {
                val slug = it.link ?: ""
                newAnimeSearchResponse(it.title ?: "Sin título", slug, TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
            Log.d(TAG, "Logs: Búsqueda exitosa para '$query', ${results.size} resultados")
            results
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: Iniciando load para URL/Slug: $url")
        return try {
            // La URL ahora es directamente el slug (ej: "kimetsu-no-yaiba")
            val slug = if (url.contains("/")) url.substringAfterLast("/") else url

            val apiPath = "$apiUrl/anime/$slug"
            Log.d(TAG, "Logs: Pidiendo datos a la API: $apiPath")

            val detailResponse = app.get(apiPath, headers = apiHeaders).text

            // Mapeamos al envoltorio "data"
            val wrapper: AnimeDetailResponse = mapper.readValue(detailResponse)

            val anime = wrapper.data
                ?: throw Exception("La API respondió pero el campo 'data' está vacío para: $slug")

            Log.d(TAG, "Logs: Datos obtenidos exitosamente para: ${anime.title}")

            val episodes = anime.ep?.mapIndexed { index, epId ->
                // Usamos el ID de episodio y mantenemos el origen para el extractor
                newEpisode("/watch/$epId?origin=${anime.id ?: slug}") {
                    this.name = "Episodio ${index + 1}"
                    this.episode = index + 1
                }
            } ?: emptyList()

            // Generamos la URL pública real para que no de 404 al compartir
            val publicUrl = "$mainUrl/anime/$slug"

            newAnimeLoadResponse(anime.title ?: "Sin título", publicUrl, TvType.Anime) {
                this.plot = anime.synopsys
                this.tags = anime.genres
                this.posterUrl = anime.posterImage?.large ?: anime.posterImage?.original
                this.year = anime.animeSeason?.year

                this.showStatus = when (anime.status) {
                    "finished" -> ShowStatus.Completed
                    "ongoing" -> ShowStatus.Ongoing
                    else -> null
                }

                addEpisodes(DubStatus.Subbed, episodes)
                if (!anime.trailer.isNullOrBlank()) {
                    addTrailer(anime.trailer)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error crítico en load: ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("AnimeParadise", "Logs: === INICIO LOADLINKS ===")
        return try {
            // Extraemos IDs necesarios
            val epId = data.substringAfter("/watch/").substringBefore("?")
            val originId = data.substringAfter("origin=").substringBefore("&")
            val watchUrl = if (data.startsWith("http")) data else "$mainUrl$data"

            // UNIFICAMOS EL USER AGENT: Esto es vital para evitar el Error 403
            val fixedUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

            // Headers para el POST (obtención del link)
            val actionHeaders = mapOf(
                "User-Agent" to fixedUserAgent,
                "Accept" to "text/x-component",
                "Next-Action" to "6002b0ce935408ccf19f5fa745fc47f1d3a4e98b24",
                "Content-Type" to "text/plain;charset=UTF-8",
                "Origin" to mainUrl,
                "Referer" to watchUrl
            )

            val body = "[\"$epId\",\"$originId\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())

            // Hacemos la petición POST
            val response = app.post(watchUrl, headers = actionHeaders, requestBody = body).text

            // --- ESCANEO DE SUBTÍTULOS ---
            Log.d("AnimeParadise", "Logs: === ESCANEANDO subData ===")
            val subRegex = Regex("""\{"src":"([^"]+)","label":"([^"]+)","type":"([^"]+)"\}""")
            subRegex.findAll(response).forEach { match ->
                val src = match.groupValues[1]
                val subUrl = if (src.startsWith("http")) src else "https://api.animeparadise.moe/stream/file/$src"

                subtitleCallback.invoke(
                    newSubtitleFile(lang = match.groupValues[2], url = subUrl) {
                        this.headers = mapOf("User-Agent" to fixedUserAgent, "Referer" to mainUrl)
                    }
                )
            }

            // --- RASTREO DE VIDEO ---
            val streamRegex = Regex(""""streamLink":"(https://[^"]+)"""")
            val streamMatch = streamRegex.find(response)

            if (streamMatch != null) {
                val finalUrl = streamMatch.groupValues[1]
                    .replace("\\u0026", "&")
                    .replace("\\/", "/")

                Log.d("AnimeParadise", "Logs: [V] Video enviado: $finalUrl")

                callback.invoke(
                    newExtractorLink(
                        name = "AnimeParadise",
                        source = "AnimeParadise",
                        url = finalUrl,
                    ) {
                        this.quality = 1080
                        this.type = ExtractorLinkType.M3U8
                        // CREDENCIALES PARA EL REPRODUCTOR: Deben coincidir con las del POST
                        this.headers = mapOf(
                            "User-Agent" to fixedUserAgent,
                            "Referer" to "https://www.animeparadise.moe/",
                            "Origin" to "https://www.animeparadise.moe",
                            "Accept" to "*/*"
                        )
                    }
                )
            } else {
                Log.d("AnimeParadise", "Logs: [!] Error: No se encontró streamLink")
            }

            Log.d("AnimeParadise", "Logs: === FIN LOADLINKS ===")
            true
        } catch (e: Exception) {
            Log.e("AnimeParadise", "Logs: ERROR CRÍTICO: ${e.message}")
            false
        }
    }

}

data class AnimeListResponse(val data: List<AnimeObject>)

data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null, // Cambiado a opcional para evitar el crash
    val link: String? = null,
    val status: String? = null,
    val synopsys: String? = null,
    val genres: List<String>? = null,
    val trailer: String? = null,
    val ep: List<String>? = null,
    val animeSeason: SeasonInfo? = null,
    val posterImage: ImageInfo? = null
)

data class SeasonInfo(val season: String? = null, val year: Int? = null)

data class ImageInfo(val original: String? = null, val large: String? = null)

data class NextDataResponse(val props: NextProps)
data class NextProps(val pageProps: NextPageProps)
data class NextPageProps(val data: DetailedAnimeData)
data class DetailedAnimeData(val synopsys: String? = null, val genres: List<String>? = null)

data class EpisodeListResponse(val data: List<EpisodeEntry>)
data class EpisodeEntry(val uid: String, val origin: String, val number: String? = null, val title: String? = null)

data class NextDataVideo(val props: VideoNextProps)
data class VideoNextProps(val pageProps: VideoPageData)
data class VideoPageData(val subtitles: List<SubtitleEntry>? = null, val animeData: SimpleAnime, val episode: SimpleEpisode)
data class SubtitleEntry(val src: String, val label: String)
data class SimpleAnime(val title: String)
data class SimpleEpisode(val number: String)

// Esta clase representa el sobre/envoltorio de la API
data class AnimeDetailResponse(
    val data: AnimeObject? = null,
    val error: Boolean? = null
)

data class VideoDirectList(
    val directUrl: List<VideoSource>? = null
)

data class VideoSource(
    val src: String,
    val label: String
)