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
                val slug = it.link ?: ""
                newAnimeSearchResponse(it.title ?: "Sin título", slug, TvType.Anime) {
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
        val slug = url.substringAfterLast("/")
        Log.d(TAG, "Logs: Iniciando load para: $slug")

        return try {
            // 1. Obtener detalles del anime
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val internalId = animeData.data?.id ?: throw Exception("ID no encontrado")

            // 2. Obtener lista de episodios vía API (Más estable que Next Actions)
            val epResponse = app.get("$apiUrl/anime/$internalId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            val episodes = epData.data.map { ep ->
                val num = ep.number?.toIntOrNull() ?: 0
                // Guardamos el slug del anime y el numero para loadLinks
                // Formato: slug|numero_episodio
                newEpisode("${animeData.data.title}|${ep.number}") {
                    this.name = ep.title ?: "Episode ${ep.number}"
                    this.episode = num
                    this.posterUrl = ep.image
                }
            }.sortedBy { it.episode }

            Log.d(TAG, "Logs: ${episodes.size} episodios cargados correctamente")

            newAnimeLoadResponse(animeData.data.title ?: "Sin título", url, TvType.Anime) {
                this.plot = animeData.data.synopsys
                this.tags = animeData.data.genres
                this.posterUrl = animeData.data.posterImage?.large
                this.year = animeData.data.animeSeason?.year
                this.showStatus = if (animeData.data.status == "finished") ShowStatus.Completed else ShowStatus.Ongoing
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
        Log.d(TAG, "Logs: === INICIO LOADLINKS === Data: $data")

        return try {
            val parts = data.split("|")
            val animeTitle = parts[0]
            val epNumber = parts[1]

            // Construir la URL de la API de videos
            val videoListUrl = "$apiUrl/storage/$animeTitle/$epNumber"
            val response = app.get(videoListUrl, headers = apiHeaders).text

            // Usaremos una clase simple para parsear esto
            val videoData: VideoList = mapper.readValue(response)

            if (videoData.directUrl == null) {
                Log.e(TAG, "Logs: No se encontraron videos: ${videoData.message}")
                return false
            }

            videoData.directUrl.forEach { video ->
                val videoUrl = when {
                    video.src.startsWith("//") -> "https:${video.src}"
                    video.src.startsWith("/") -> apiUrl + video.src
                    else -> video.src
                }

                callback.invoke(
                    ExtractorLink(
                        name,
                        "AnimeParadise - ${video.label}",
                        videoUrl,
                        referer = "$mainUrl/",
                        quality = getQuality(video.label),
                        type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    )
                )
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en LoadLinks: ${e.message}")
            false
        }
    }

    private fun getQuality(label: String): Int {
        return Regex("""(\d+)p""").find(label)?.groupValues?.get(1)?.toIntOrNull() ?: Qualities.Unknown.value
    }

}

data class AnimeListResponse(val data: List<AnimeObject>)

data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null,
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
data class EpisodeEntry(
    val uid: String,
    val origin: String,
    val number: String? = null,
    val title: String? = null,
    val image: String? = null
)

data class NextDataVideo(val props: VideoNextProps)
data class VideoNextProps(val pageProps: VideoPageData)
data class VideoPageData(val subtitles: List<SubtitleEntry>? = null, val animeData: SimpleAnime, val episode: SimpleEpisode)
data class SubtitleEntry(val src: String, val label: String)
data class SimpleAnime(val title: String)
data class SimpleEpisode(val number: String)

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

data class VideoList(
    val directUrl: List<VideoSource>? = null,
    val message: String? = null
)