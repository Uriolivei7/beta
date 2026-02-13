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
            val mainPageHtml = app.get(url).text
            val detectedActions = Regex("""\"([a-f0-9]{40})\"""").findAll(mainPageHtml).map { it.groupValues[1] }.toList()

            val infoActionId = if (detectedActions.size >= 1) detectedActions[0] else "4079a5a3a1c98fdc637ee2ab5c0983b38c95ef4f0d"
            val epActionId = if (detectedActions.size >= 2) detectedActions[1] else "40f9328520c8b05c7b7870e35b1f2e3102d2b92ff7"

            Log.d(TAG, "Logs: Actions Detectadas -> Info: $infoActionId, EPs: $epActionId")

            val infoResponse = app.post(
                url,
                headers = mapOf(
                    "next-action" to infoActionId,
                    "content-type" to "text/plain;charset=UTF-8",
                    "accept" to "text/x-component",
                    "referer" to url
                ),
                requestBody = "[\"$slug\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            ).text

            val internalId = Regex("""\"_id\":\"([a-zA-Z0-9]+)\"""").find(infoResponse)?.groupValues?.get(1)

            if (internalId == null) {
                Log.e(TAG, "Logs: Error - No se encontró internalId. Preview: ${infoResponse.take(200)}")
                return null
            }

            val epResponse = app.post(
                url,
                headers = mapOf(
                    "next-action" to epActionId,
                    "content-type" to "text/plain;charset=UTF-8",
                    "accept" to "text/x-component",
                    "referer" to url
                ),
                requestBody = "[\"$internalId\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            ).text

            val episodes = mutableListOf<Episode>()
            val epRegex = Regex("""\{[^{]*?\"uid\":\"([^\"]+)\"[^{]*?\"number\":\"?(\d+)\"?[^{]*?\}""")

            epRegex.findAll(epResponse).forEach { match ->
                val blockText = match.value
                val epUid = match.groupValues[1]
                val epNum = match.groupValues[2].toIntOrNull() ?: 0
                val epTitle = Regex("""\"title\":\"([^\"]+)\"""").find(blockText)?.groupValues?.get(1) ?: "Episodio $epNum"
                val epThumb = Regex("""\"image\":\"([^\"]+)\"""").find(blockText)?.groupValues?.get(1)?.replace("\\/", "/")

                episodes.add(newEpisode("/watch/$epUid?origin=$internalId") {
                    this.name = epTitle
                    this.episode = epNum
                    this.posterUrl = epThumb
                })
            }

            // --- RECOMENDACIONES (FORMATO JSON DE NEXT.JS) ---
            val recommendations = mutableListOf<SearchResponse>()

            val recoRegex = Regex("""\"title\":\"([^\"]+)\",\"link\":\"([^\"]+)\"[^}]*?\"posterImage\":\{"large\":\"([^\"]+)\"""")

            recoRegex.findAll(infoResponse).forEach { match ->
                val recTitle = match.groupValues[1]
                val recLink = match.groupValues[2]
                val recPoster = match.groupValues[3].replace("\\/", "/")

                // Filtramos para no añadir el mismo anime que estamos viendo
                // y verificamos que el link no sea una URL completa
                if (recLink != slug && !recLink.contains("http")) {
                    recommendations.add(newAnimeSearchResponse(recTitle, recLink, TvType.Anime) {
                        this.posterUrl = recPoster
                    })
                }
            }

            Log.d(TAG, "Logs: Recomendaciones extraídas: ${recommendations.size}")

            // Metadatos finales
            val title = Regex("""\"title\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1) ?: "Sin título"
            val plot = Regex("""\"synopsys\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1)?.replace("\\n", "\n")
            val poster = Regex("""\"large\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1)?.replace("\\/", "/")

            Log.d(TAG, "Logs: Carga finalizada. ${episodes.size} EPs, ${recommendations.size} Recos.")

            newAnimeLoadResponse(title, url, TvType.Anime) {
                this.posterUrl = poster
                this.plot = plot
                this.recommendations = recommendations
                addEpisodes(DubStatus.Subbed, episodes.sortedBy { it.episode })
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
        val currentEpId = data.substringAfter("/watch/").substringBefore("?")
        val currentOriginId = data.substringAfter("origin=").substringBefore("&")
        val watchUrl = if (data.startsWith("http")) data else "$mainUrl$data"

        Log.d(TAG, "Logs: === INICIO LOADLINKS === EpUID: $currentEpId")

        return try {
            val watchPageHtml = app.get(watchUrl).text

            // Buscamos todos los hashes de 40 caracteres
            val detectedActions = Regex("""[a-f0-9]{40}""").findAll(watchPageHtml).map { it.value }.toList()

            // El ID de video en Next.js suele ser uno de los últimos, pero vamos a probar
            // con una lista de prioridad o el último detectado si no hay más.
            val linkActionId = if (detectedActions.size >= 3) detectedActions[2] else detectedActions.lastOrNull() ?: "6002b0ce935408ccf19f5fa745fc47f1d3a4e98b24"

            Log.d(TAG, "Logs: Probando Action ID: $linkActionId")
            
            val response = app.post(
                watchUrl,
                headers = mapOf(
                    "accept" to "text/x-component", // Igual que tu CURL
                    "next-action" to linkActionId,
                    "content-type" to "text/plain;charset=UTF-8",
                    "referer" to watchUrl,
                    "origin" to mainUrl,
                    "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                ),
                requestBody = "[\"$currentEpId\",\"$currentOriginId\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            ).text

            // Si la respuesta dice "Server action not found", el ID era incorrecto.
            if (response.contains("Server action not found")) {
                Log.e(TAG, "Logs: ID $linkActionId incorrecto. Intentando con el último de la lista...")
                // Re-intento con el último si el anterior falló
                val fallbackId = detectedActions.last()
                val retryResponse = app.post(watchUrl,
                    headers = mapOf("next-action" to fallbackId, "accept" to "text/x-component"),
                    requestBody = "[\"$currentEpId\",\"$currentOriginId\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
                ).text
                return parseVideoResponse(response, callback, subtitleCallback)
            }

            parseVideoResponse(response, callback, subtitleCallback)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en LoadLinks: ${e.message}")
            false
        }
    }

    private suspend fun parseVideoResponse(
        response: String,
        callback: (ExtractorLink) -> Unit,
        subtitleCallback: (SubtitleFile) -> Unit
    ): Boolean {
        // 1. Buscar el Video (m3u8)
        val videoRegex = Regex("""https?[:\\/]+[^"\\\s]+master\.m3u8[^"\\\s]*""")
        val videoMatch = videoRegex.find(response)

        if (videoMatch != null) {
            val rawUrl = videoMatch.value
                .replace("\\/", "/")
                .replace("\\u0026", "&")
                .replace("u0026", "&")

            val proxyUrl = if (rawUrl.contains("stream.animeparadise.moe")) rawUrl
            else "https://stream.animeparadise.moe/m3u8?url=$rawUrl"

            callback.invoke(
                newExtractorLink(name, name, proxyUrl, type = ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.referer = "$mainUrl/"
                }
            )

            // --- 2. LOGICA PARA SUBTITULOS ---
            // Buscamos el patrón JSON de subtítulos: {"src":"...","label":"..."}
            val subRegex = Regex("""\{"src":"([^"]+)","label":"([^"]+)"""")
            subRegex.findAll(response).forEach { match ->
                val src = match.groupValues[1].replace("\\/", "/")
                val label = match.groupValues[2]

                // Construimos la URL completa del subtítulo
                val subUrl = if (src.startsWith("http")) src
                else "https://api.animeparadise.moe/stream/file/$src"

                Log.d(TAG, "Logs: Subtítulo encontrado: $label -> $subUrl")

                subtitleCallback.invoke(
                    newSubtitleFile(label, subUrl)
                )
            }

            return true
        }

        Log.e(TAG, "Logs: No se encontró m3u8 en la respuesta.")
        return false
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