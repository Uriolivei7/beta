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
        Log.d(TAG, "Logs: Iniciando loadLinks para: $data")
        return try {
            val epId = data.substringAfter("/watch/").substringBefore("?")
            val streamApiUrl = "https://api.animeparadise.moe/watch/$epId"

            Log.d(TAG, "Logs: Pidiendo fuentes a: $streamApiUrl")
            val response = app.get(streamApiUrl, headers = apiHeaders).text
            val videoData: VideoDirectList = mapper.readValue(response)

            videoData.directUrl?.forEach { video ->
                Log.d(TAG, "Logs: Fuente encontrada: ${video.label} -> ${video.src}")

                val isM3u8 = video.src.contains("m3u8")

                callback.invoke(
                    newExtractorLink(
                        name = "AnimeParadise - ${video.label}",
                        source = "AnimeParadise",
                        url = video.src,
                    ) {
                        // AQUÍ ES DONDE SE PONEN LOS PARÁMETROS EN TU VERSIÓN
                        this.quality = getQualityFromName(video.label)
                        this.referer = "https://www.animeparadise.moe/"
                        this.type = if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    }
                )
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en loadLinks: ${e.message}")
            false
        }
    }

    private fun getQualityFromName(name: String): Int {
        return when {
            name.contains("1080") -> 1080
            name.contains("720") -> 720
            name.contains("480") -> 480
            name.contains("360") -> 360
            else -> 0 // Calidad desconocida
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