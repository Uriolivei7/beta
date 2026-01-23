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
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
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
            val url = "$apiUrl/?sort={\"rate\": -1}"
            val response = app.get(url, headers = apiHeaders).text
            val resData = json.decodeFromString(AnimeListResponse.serializer(), response)

            val animeList = resData.data.map {
                newAnimeSearchResponse(it.title, "${it.id}|${it.link}", TvType.Anime) {
                    this.posterUrl = it.posterImage.large ?: it.posterImage.original
                }
            }
            newHomePageResponse(listOf(HomePageList("Popular Anime", animeList)), true)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando $query")
        return try {
            val response = app.get("$apiUrl/?title=$query", headers = apiHeaders).text
            val resData = json.decodeFromString(AnimeListResponse.serializer(), response)

            resData.data.map {
                newAnimeSearchResponse(it.title, "${it.id}|${it.link}", TvType.Anime) {
                    this.posterUrl = it.posterImage.large
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: Cargando serie: $url")
        return try {
            val parts = url.split("|")
            val id = parts[0]
            val slug = parts[1]

            val document = Jsoup.parse(app.get("$mainUrl/anime/$slug").text)
            val jsonData = document.selectFirst("script#__NEXT_DATA__")?.data()
                ?: throw Exception("No NEXT_DATA")

            val details = json.decodeFromString(AnimeDetails.serializer(), jsonData).props.pageProps.data

            val epResponse = app.get("$apiUrl/anime/$id/episode", headers = apiHeaders).text
            val epData = json.decodeFromString(EpisodeListResponse.serializer(), epResponse)

            val episodes = epData.data.map {
                newEpisode("/watch/${it.uid}?origin=${it.origin}") {
                    this.name = it.title ?: "Episode ${it.number}"
                    this.episode = it.number?.toIntOrNull()
                }
            }.reversed()

            newAnimeLoadResponse(slug, url, TvType.Anime) {
                this.plot = details.synopsys
                this.tags = details.genres
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
        return try {
            Log.d(TAG, "Logs: Cargando links para $data")
            val doc = Jsoup.parse(app.get(mainUrl + data).text)
            val nextData = doc.selectFirst("script#__NEXT_DATA__")?.data() ?: return false
            val videoData = json.decodeFromString(VideoData.serializer(), nextData).props.pageProps

            videoData.subtitles?.forEach {
                subtitleCallback(newSubtitleFile(it.label, it.src))
            }

            val storageUrl = "$apiUrl/storage/${videoData.animeData.title}/${videoData.episode.number}"
            val videoResponse = app.get(storageUrl, headers = apiHeaders).text
            val videoObjectList = json.decodeFromString(VideoList.serializer(), videoResponse)

            videoObjectList.directUrl?.forEach { video ->
                callback(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} ${video.label}",
                        url = video.src
                    ) {
                        this.referer = "$mainUrl/"
                        this.quality = getQualityFromName(video.label)
                    }
                )
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en loadLinks: ${e.message}")
            false
        }
    }

    private fun getQualityFromName(qualityName: String): Int {
        return when {
            qualityName.contains("1080") -> Qualities.P1080.value
            qualityName.contains("720") -> Qualities.P720.value
            qualityName.contains("480") -> Qualities.P480.value
            qualityName.contains("360") -> Qualities.P360.value
            else -> Qualities.Unknown.value
        }
    }
}

// ===========================================================================
// CLASES DE DATOS (FUERA DE LA CLASE PRINCIPAL)
// Esto soluciona el error "typeParametersSerializers"
// ===========================================================================

@Serializable
data class AnimeListResponse(val data: List<AnimeObject>)

@Serializable
data class AnimeObject(
    @SerialName("_id") val id: String,
    val title: String,
    val link: String,
    val posterImage: ImageObject
)

@Serializable
data class ImageObject(val original: String? = null, val large: String? = null)

@Serializable
data class AnimeDetails(val props: DetailsProps)

@Serializable
data class DetailsProps(val pageProps: DetailsPageProps)

@Serializable
data class DetailsPageProps(val data: DetailsData)

@Serializable
data class DetailsData(val synopsys: String? = null, val genres: List<String>? = null)

@Serializable
data class EpisodeListResponse(val data: List<EpisodeObject>)

@Serializable
data class EpisodeObject(
    val uid: String,
    val origin: String,
    val number: String? = null,
    val title: String? = null
)

@Serializable
data class VideoData(val props: VideoProps)

@Serializable
data class VideoProps(val pageProps: VideoPageProps)

@Serializable
data class VideoPageProps(
    val subtitles: List<SubtitleObject>? = null,
    val animeData: AnimeInfo,
    val episode: EpisodeInfo
)

@Serializable
data class SubtitleObject(val src: String, val label: String)

@Serializable
data class AnimeInfo(val title: String)

@Serializable
data class EpisodeInfo(val number: String)

@Serializable
data class VideoList(val directUrl: List<VideoObject>? = null, val message: String? = null)

@Serializable
data class VideoObject(val src: String, val label: String)