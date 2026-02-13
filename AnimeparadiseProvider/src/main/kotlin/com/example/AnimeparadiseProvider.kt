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
        Log.d(TAG, "Logs: Iniciando load dinámico para: $slug")

        return try {
            // 1. Obtener la página para extraer los IDs de Next.js (next-action)
            val mainPageHtml = app.get(url).text
            val detectedActions = Regex("""[a-f0-9]{40}""").findAll(mainPageHtml).map { it.value }.toList()

            // Fallbacks por si el Regex de detección falla
            val infoActionId = if (detectedActions.size >= 1) detectedActions[0] else "4079a5a3a1c98fdc637ee2ab5c0983b38c95ef4f0d"
            val epActionId = if (detectedActions.size >= 2) detectedActions[1] else "40f9328520c8b05c7b7870e35b1f2e3102d2b92ff7"

            Log.d(TAG, "Logs: Actions Detectadas -> Info: $infoActionId, EPs: $epActionId")

            // 2. PASO 1: Obtener ID interno (internalId)
            val infoResponse = app.post(
                url,
                headers = mapOf(
                    "next-action" to infoActionId,
                    "content-type" to "text/plain;charset=UTF-8",
                    "accept" to "text/x-component"
                ),
                requestBody = "[\"$slug\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            ).text

            val internalId = Regex("""\"_id\":\"([a-zA-Z0-9]+)\"""").find(infoResponse)?.groupValues?.get(1)

            if (internalId == null) {
                Log.e(TAG, "Logs: Error - No se encontró internalId en infoResponse")
                Log.d(TAG, "Logs: Preview infoResponse: ${infoResponse.take(300)}")
                return null
            }
            Log.d(TAG, "Logs: internalId detectado correctamente: $internalId")

            // 3. PASO 2: Obtener los episodios
            val epResponse = app.post(
                url,
                headers = mapOf(
                    "next-action" to epActionId,
                    "content-type" to "text/plain;charset=UTF-8",
                    "accept" to "text/x-component"
                ),
                requestBody = "[\"$internalId\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            ).text

            val episodes = mutableListOf<Episode>()

            // ACTUALIZACIÓN: Usamos "uid" (confirmado en logs) y manejamos comillas en "number"
            // Buscamos bloques que tengan uid y number
            val epRegex = Regex("""\{[^{]*?\"uid\":\"([^\"]+)\"[^{]*?\"number\":\"?(\d+)\"?[^{]*?\}""")

            epRegex.findAll(epResponse).forEach { match ->
                val blockText = match.value
                val epUid = match.groupValues[1]
                val epNum = match.groupValues[2].toIntOrNull() ?: 0

                // Extraemos título e imagen del bloque individual
                val epTitle = Regex("""\"title\":\"([^\"]+)\"""").find(blockText)?.groupValues?.get(1) ?: "Episodio $epNum"
                val epThumb = Regex("""\"image\":\"([^\"]+)\"""").find(blockText)?.groupValues?.get(1)?.replace("\\/", "/")

                episodes.add(newEpisode("/watch/$epUid?origin=$internalId") {
                    this.name = epTitle
                    this.episode = epNum
                    this.posterUrl = epThumb
                })
            }

            // Logs de seguridad si la lista está vacía
            if (episodes.isEmpty()) {
                Log.w(TAG, "Logs: No se encontraron episodios. Analizando epResponse...")
                Log.d(TAG, "Logs: epResponse preview: ${epResponse.take(500)}")
            } else {
                Log.d(TAG, "Logs: Carga exitosa. ${episodes.size} episodios encontrados para $slug")
            }

            // 4. Metadatos del Anime
            val title = Regex("""\"title\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1) ?: "Sin título"
            val plot = Regex("""\"synopsys\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1)
                ?.replace("\\n", "\n")
                ?.replace("\\r", "")
            val poster = Regex("""\"large\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1)?.replace("\\/", "/")

            newAnimeLoadResponse(title, url, TvType.Anime) {
                this.posterUrl = poster
                this.plot = plot
                addEpisodes(DubStatus.Subbed, episodes.sortedBy { it.episode })
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error crítico en load: ${e.message}")
            Log.e(TAG, "Logs: Stacktrace completo: ${e.stackTraceToString()}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val currentEpId = data.substringAfter("/watch/").substringBefore("?")
        Log.d(TAG, "Logs: === INICIO LOADLINKS === ID: $currentEpId")

        return try {
            val watchUrl = if (data.startsWith("http")) data else "$mainUrl$data"
            val actionHeaders = mapOf(
                "accept" to "*/*",
                "next-action" to "6002b0ce935408ccf19f5fa745fc47f1d3a4e98b24",
                "content-type" to "text/plain;charset=UTF-8",
                "origin" to "https://www.animeparadise.moe",
                "referer" to watchUrl,
                "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            )

            val currentOriginId = data.substringAfter("origin=").substringBefore("&")
            val requestBodyString = "[\"$currentEpId\",\"$currentOriginId\"]"
            val response = app.post(watchUrl, headers = actionHeaders, requestBody = requestBodyString.toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())).text

            val videoRegex = Regex("""https://[a-zA-Z0-9.-]+\.[a-z]{2,}/_v7/[^"\\\s]+master\.m3u8""")

            val index = response.indexOf(currentEpId)
            val searchArea = if (index != -1) {
                val start = (index - 5000).coerceAtLeast(0)
                val end = (index + 5000).coerceAtMost(response.length)
                response.substring(start, end)
            } else {
                response
            }

            val videoMatch = videoRegex.find(searchArea) ?: videoRegex.find(response)

            if (videoMatch != null) {
                val rawUrl = videoMatch.value.replace("\\", "").replace("u0026", "&")
                Log.d(TAG, "Logs: Video encontrado (Servidor: ${rawUrl.substringAfter("https://").substringBefore("/")}): $rawUrl")

                val proxyUrl = "https://stream.animeparadise.moe/m3u8?url=$rawUrl"

                callback.invoke(
                    newExtractorLink("AnimeParadise", "AnimeParadise", proxyUrl).apply {
                        this.quality = Qualities.P1080.value
                        this.type = ExtractorLinkType.M3U8
                        this.headers = mapOf(
                            "referer" to "https://www.animeparadise.moe/",
                            "origin" to "https://www.animeparadise.moe",
                            "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        )
                    }
                )
            } else {
                Log.e(TAG, "Logs: No se encontró ningún link de video (Ni lightning ni frosty)")
            }

            val subRegex = Regex("""\{"src":"([^"]+)","label":"([^"]+)","type":"([^"]+)"\}""")
            val addedSubs = mutableSetOf<String>()
            subRegex.findAll(searchArea).forEach { match ->
                val src = match.groupValues[1].replace("\\/", "/")
                if (addedSubs.add(src)) {
                    val subUrl = if (src.startsWith("http")) src else "https://api.animeparadise.moe/stream/file/$src"
                    subtitleCallback.invoke(newSubtitleFile(match.groupValues[2], subUrl))
                }
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en LoadLinks: ${e.message}")
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

data class VideoSource(
    val src: String,
    val label: String
)