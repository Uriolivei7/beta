package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import kotlinx.serialization.*
import kotlinx.serialization.json.*

class AnimeParadiseProvider : MainAPI() {
    override var mainUrl = "https://www.animeparadise.moe"
    private val apiUrl = "https://api.animeparadise.moe"
    override var name = "AnimeParadise"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val TAG = "AnimeParadise"
    private val jsonParser = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
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
            val url = "$apiUrl/?sort=%7B%22rate%22:-1%7D" // URL Encode de {"rate":-1}
            val response = app.get(url, headers = apiHeaders).text
            Log.d(TAG, "Logs: API Responde (puros): ${response.take(100)}")

            val resData = jsonParser.decodeFromString(AnimeListResponse.serializer(), response)

            val animeList = resData.data.mapNotNull {
                try {
                    newAnimeSearchResponse(it.title, "${it.id}|${it.link}", TvType.Anime) {
                        this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                    }
                } catch(e: Exception) { null }
            }
            newHomePageResponse(listOf(HomePageList("Popular Anime", animeList)), true)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error Fatal en getMainPage: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando $query")
        return try {
            val response = app.get("$apiUrl/?title=$query", headers = apiHeaders).text
            val resData = jsonParser.decodeFromString(AnimeListResponse.serializer(), response)

            resData.data.mapNotNull {
                try {
                    newAnimeSearchResponse(it.title, "${it.id}|${it.link}", TvType.Anime) {
                        this.posterUrl = it.posterImage?.large
                    }
                } catch(e: Exception) { null }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error Fatal en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: Cargando serie: $url")
        return try {
            val parts = url.split("|")
            val id = parts[0]
            val slug = parts[1]

            val htmlResponse = app.get("$mainUrl/anime/$slug").text
            val document = Jsoup.parse(htmlResponse)
            val jsonData = document.selectFirst("script#__NEXT_DATA__")?.data()
                ?: throw Exception("No NEXT_DATA found")

            val details = jsonParser.decodeFromString(NextDataResponse.serializer(), jsonData).props.pageProps.data

            val epResponse = app.get("$apiUrl/anime/$id/episode", headers = apiHeaders).text
            val epData = jsonParser.decodeFromString(EpisodeListResponse.serializer(), epResponse)

            val episodes = epData.data.map {
                newEpisode("/watch/${it.uid}?origin=${it.origin}") {
                    this.name = it.title ?: "Episode ${it.number}"
                    this.episode = it.number?.toIntOrNull()
                }
            }.reversed()

            newAnimeLoadResponse(slug.replace("-", " "), url, TvType.Anime) {
                this.plot = details.synopsys
                this.tags = details.genres
                addEpisodes(DubStatus.Subbed, episodes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error Fatal en load: ${e.message}")
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
            val doc = Jsoup.parse(app.get(mainUrl + data).text)
            val nextData = doc.selectFirst("script#__NEXT_DATA__")?.data() ?: return false
            val videoProps = jsonParser.decodeFromString(NextDataVideo.serializer(), nextData).props.pageProps

            videoProps.subtitles?.forEach {
                subtitleCallback(SubtitleFile(it.label, it.src))
            }

            val storageUrl = "$apiUrl/storage/${videoProps.animeData.title}/${videoProps.episode.number}"
            val videoResponse = app.get(storageUrl, headers = apiHeaders).text
            val videoList = jsonParser.decodeFromString(VideoDirectList.serializer(), videoResponse)

            videoList.directUrl?.forEach { video ->
                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} ${video.label}",
                        url = video.src
                    ) {
                        this.referer = "$mainUrl/"
                        this.quality = when {
                            video.label.contains("1080") -> Qualities.P1080.value
                            video.label.contains("720") -> Qualities.P720.value
                            else -> Qualities.Unknown.value
                        }
                    }
                )
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error Fatal en loadLinks: ${e.message}")
            false
        }
    }
}

// ===========================================================================
// MODELOS EXTERNOS (PLANOS PARA MÁXIMA COMPATIBILIDAD)
// ===========================================================================

@Serializable data class AnimeListResponse(val data: List<AnimeObject>)
@Serializable data class AnimeObject(
    @SerialName("_id") val id: String,
    val title: String,
    val link: String,
    val posterImage: ImageInfo? = null
)
@Serializable data class ImageInfo(val original: String? = null, val large: String? = null)

@Serializable data class NextDataResponse(val props: NextProps)
@Serializable data class NextProps(val pageProps: NextPageProps)
@Serializable data class NextPageProps(val data: DetailedAnimeData)
@Serializable data class DetailedAnimeData(val synopsys: String? = null, val genres: List<String>? = null)

@Serializable data class EpisodeListResponse(val data: List<EpisodeEntry>)
@Serializable data class EpisodeEntry(val uid: String, val origin: String, val number: String? = null, val title: String? = null)

@Serializable data class NextDataVideo(val props: VideoNextProps)
@Serializable data class VideoNextProps(val pageProps: VideoPageData)
@Serializable data class VideoPageData(val subtitles: List<SubtitleEntry>? = null, val animeData: SimpleAnime, val episode: SimpleEpisode)
@Serializable data class SubtitleEntry(val src: String, val label: String)
@Serializable data class SimpleAnime(val title: String)
@Serializable data class SimpleEpisode(val number: String)

@Serializable data class VideoDirectList(val directUrl: List<VideoSource>? = null)
@Serializable data class VideoSource(val src: String, val label: String)