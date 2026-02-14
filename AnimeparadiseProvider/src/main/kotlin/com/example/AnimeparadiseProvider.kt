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
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val internalId = animeData.data?.id ?: throw Exception("ID no encontrado")

            val epResponse = app.get("$apiUrl/anime/$internalId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            // DENTRO DE load
            val episodes = epData.data.map { ep ->
                val num = ep.number?.toIntOrNull() ?: 0
                // USAMOS EL ID INTERNO O SLUG, NO EL TÍTULO CON ESPACIOS
                // Formato: slug_del_anime|numero_episodio
                newEpisode("$slug|${ep.number}") {
                    this.name = ep.title ?: "Episode ${ep.number}"
                    this.episode = num
                }
            }.sortedBy { it.episode }

            // USAR ESTE CONSTRUCTOR QUE ES MÁS COMPATIBLE
            newAnimeLoadResponse(animeData.data?.title ?: "Sin título", url, TvType.Anime) {
                this.posterUrl = animeData.data?.posterImage?.large
                this.plot = animeData.data?.synopsys
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
        // Limpiamos los datos: "kimetsu-no-yaiba|1"
        val parts = data.split("|")
        val animeId = parts[0].substringAfterLast("/")
        val epNumber = parts[1]

        Log.d(TAG, "Logs: === INICIO LOADLINKS === Anime: $animeId, Ep: $epNumber")

        return try {
            // 1. Obtener los videos desde el storage de la API
            val videoApiUrl = "$apiUrl/storage/$animeId/$epNumber"
            val response = app.get(videoApiUrl, headers = apiHeaders).text

            Log.d(TAG, "Logs: Respuesta API Video obtenida")

            val videoData: VideoList = mapper.readValue(response)

            // 2. Extraer Enlaces de Video
            videoData.directUrl?.forEach { video ->
                val videoUrl = when {
                    video.src.startsWith("//") -> "https:${video.src}"
                    video.src.startsWith("/") -> apiUrl + video.src
                    else -> video.src
                }

                // Usamos newExtractorLink con el initializer
                val link = newExtractorLink(
                    source = this.name,
                    name = "AnimeParadise - ${video.label}",
                    url = videoUrl,
                    type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                ) {
                    // Aquí resolvemos el error de 'getQuality' extrayendo el número
                    this.quality = Regex("""(\d+)""").find(video.label)?.value?.toIntOrNull() ?: Qualities.Unknown.value
                    this.referer = "$mainUrl/"
                }
                callback.invoke(link)
            }

            // 3. Restaurar Lógica de Subtítulos (Desde la página de reproducción)
            // A veces los subs no están en la API de video, sino en la página del episodio
            val watchUrl = "$mainUrl/watch/$animeId/$epNumber" // Ajusta según la URL real de la web
            val watchResponse = app.get(watchUrl).text

            val subRegex = Regex("""\{"src":"([^"]+)","label":"([^"]+)","type":"([^"]+)"\}""")
            subRegex.findAll(watchResponse).forEach { match ->
                val src = match.groupValues[1].replace("\\/", "/")
                val subUrl = if (src.startsWith("http")) src else "https://api.animeparadise.moe/stream/file/$src"
                Log.d(TAG, "Logs: Subtítulo encontrado: ${match.groupValues[2]}")
                subtitleCallback.invoke(newSubtitleFile(match.groupValues[2], subUrl))
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error crítico en LoadLinks: ${e.message}")
            false
        }
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

data class VideoList(
    val directUrl: List<VideoSource>? = null,
    val message: String? = null
)

data class VideoSource(
    val src: String,
    val label: String
)