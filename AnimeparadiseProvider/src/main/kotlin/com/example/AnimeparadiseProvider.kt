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
import kotlin.text.Regex
import kotlin.text.RegexOption
import kotlin.text.MatchResult

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
        return try {
            val response = app.get("$apiUrl/?sort=%7B%22rate%22:-1%7D", headers = apiHeaders).text
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
        Log.d(TAG, "Logs: Iniciando load para: $slug")

        return try {
            val mainPageHtml = app.get(url).text
            val detectedActions = Regex("""\"([a-f0-9]{40})\"""").findAll(mainPageHtml).map { it.groupValues[1] }.toList()

            val infoActionId = detectedActions.getOrNull(0) ?: "4079a5a3a1c98fdc637ee2ab5c0983b38c95ef4f0d"
            val epActionId = detectedActions.getOrNull(1) ?: "40f9328520c8b05c7b7870e35b1f2e3102d2b92ff7"

            val infoResponse = app.post(url,
                headers = mapOf("next-action" to infoActionId, "accept" to "text/x-component"),
                requestBody = "[\"$slug\"]".toRequestBody("text/plain".toMediaTypeOrNull())
            ).text

            val internalId = Regex("""\"_id\":\"([a-zA-Z0-9]+)\"""").find(infoResponse)?.groupValues?.get(1) ?: return null

            val epResponse = app.post(url,
                headers = mapOf("next-action" to epActionId, "accept" to "text/x-component"),
                requestBody = "[\"$internalId\"]".toRequestBody("text/plain".toMediaTypeOrNull())
            ).text

            val episodes = mutableListOf<Episode>()
            // Especificamos el tipo MatchResult explícitamente
            Regex("""\{[^{]*?\"uid\":\"([^\"]+)\"[^{]*?\"number\":\"?(\d+)\"?[^{]*?\}""").findAll(epResponse).forEach { match: MatchResult ->
                val epUid = match.groupValues[1]
                val epNum = match.groupValues[2].toIntOrNull() ?: 0
                episodes.add(newEpisode("/watch/$epUid?origin=$internalId") {
                    this.episode = epNum
                    this.name = "Episodio $epNum"
                })
            }

            val recommendations = mutableListOf<SearchResponse>()
            // Corrección de RegexOption y tipo explícito
            val recoRegex = Regex("""(?s)\"title\":\"([^\"]+)\".*?\"link\":\"([^\"]+)\"""")
            recoRegex.findAll(infoResponse).forEach { match: MatchResult ->
                val rTitle = match.groupValues[1]
                val rLink = match.groupValues[2]
                if (rLink != slug && !rLink.contains("\\")) {
                    recommendations.add(newAnimeSearchResponse(rTitle, rLink, TvType.Anime))
                }
            }

            val title = Regex("""\"title\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1) ?: "Sin título"
            val plot = Regex("""\"synopsys\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1)?.replace("\\n", "\n")
            val poster = Regex("""\"large\":\"([^\"]+)\"""").find(infoResponse)?.groupValues?.get(1)?.replace("\\/", "/")

            newAnimeLoadResponse(title, url, TvType.Anime) {
                this.posterUrl = poster
                this.plot = plot
                this.recommendations = recommendations.distinctBy { it.url }
                addEpisodes(DubStatus.Subbed, episodes.sortedBy { it.episode })
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
        val watchUrl = if (data.startsWith("http")) data else "$mainUrl$data"
        Log.d(TAG, "Logs: === INICIO LOADLINKS WEB SCAN ===")

        return try {
            val response = app.get(watchUrl).text

            val videoMatch = Regex("""https?[:\\/]+[^"\\\s]+?master\.m3u8[^"\\\s]*""").find(response)
            if (videoMatch != null) {
                Log.d(TAG, "Logs: Enlace encontrado directamente en el HTML")
                return parseVideoResponse(response, callback, subtitleCallback)
            }

            // Next.js suele guardar el estado inicial de la página aquí
            val nextData = Regex("""<script id="__NEXT_DATA__"[^>]*>(.*?)</script>""").find(response)?.groupValues?.get(1)
            if (nextData != null) {
                Log.d(TAG, "Logs: __NEXT_DATA__ encontrado, analizando...")
                if (nextData.contains("master.m3u8")) {
                    return parseVideoResponse(nextData, callback, subtitleCallback)
                }
            }

            // Esto es muy común en las versiones nuevas de Next.js
            val pushData = Regex("""self\.__next_f\.push\(\[1,"(.*?)"\]\)""").findAll(response)
                .map { it.groupValues[1] }
                .joinToString("")

            if (pushData.contains("master.m3u8")) {
                Log.d(TAG, "Logs: master.m3u8 encontrado en bloques push")
                return parseVideoResponse(pushData.replace("\\\"", "\""), callback, subtitleCallback)
            }

            Log.d(TAG, "Logs: Falló extracción directa, intentando POST de rescate...")
            val actionId = Regex("""[a-f0-9]{40}""").find(response)?.value ?: "60d3cd85d1347bb1ef9c0fd8ace89f28de2c8e0d7e"

            val currentEpId = data.substringAfter("/watch/").substringBefore("?")
            val currentOriginId = data.substringAfter("origin=").substringBefore("&")

            val postRes = app.post(watchUrl,
                headers = mapOf("accept" to "text/x-component", "next-action" to actionId, "x-nextjs-postponed" to "1"),
                requestBody = "[\"$currentEpId\",\"$currentOriginId\"]".toRequestBody("text/plain".toMediaTypeOrNull())
            ).text

            return parseVideoResponse(postRes, callback, subtitleCallback)

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
        val videoRegex = Regex("""https?[:\\/]+[^"\\\s]+?master\.m3u8[^"\\\s]*""")
        val match = videoRegex.find(response)

        if (match != null) {
            val cleanUrl = match.value.replace("\\/", "/")
                .replace("\\u0026", "&")
                .replace("u0026", "&")

            Log.d(TAG, "Logs: URL de video limpia: $cleanUrl")

            // Usamos java.net.URLEncoder para codificar la URL del proxy
            val encodedUrl = java.net.URLEncoder.encode(cleanUrl, "UTF-8")
            val finalUrl = if (cleanUrl.contains("stream.animeparadise.moe")) cleanUrl
            else "https://stream.animeparadise.moe/m3u8?url=$encodedUrl"

            callback.invoke(
                newExtractorLink(name, name, finalUrl, type = ExtractorLinkType.M3U8) {
                    this.quality = Qualities.P1080.value
                    this.referer = "$mainUrl/"
                }
            )

            // Parseo de subtítulos
            Regex("""\{"src":"([^"]+)","label":"([^"]+)"""").findAll(response).forEach { sMatch ->
                val src = sMatch.groupValues[1].replace("\\/", "/")
                val subUrl = if (src.startsWith("http")) src else "https://api.animeparadise.moe/stream/file/$src"
                subtitleCallback.invoke(newSubtitleFile(sMatch.groupValues[2], subUrl))
            }
            return true
        }
        return false
    }
}

// Data Classes (Simplificadas)
data class AnimeListResponse(val data: List<AnimeObject>)
data class AnimeObject(
    val title: String? = null,
    val link: String? = null,
    val posterImage: ImageInfo? = null
)
data class ImageInfo(val original: String? = null, val large: String? = null)