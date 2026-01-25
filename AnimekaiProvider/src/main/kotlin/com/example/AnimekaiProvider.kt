package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.mvvm.safeApiCall
import com.fasterxml.jackson.annotation.*
import com.lagradost.cloudstream3.mvvm.Resource
import com.lagradost.cloudstream3.syncproviders.providers.SimklApi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import okhttp3.HttpUrl.Companion.toHttpUrl

class AnimeKaiProvider : MainAPI() {
    override var mainUrl = "https://animekai.to"
    override var name = "AnimeKAI"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)
    private val TAG = "AnimeKai"
    private val decryptionApi = "https://enc-dec.app/api"

    private val apiHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36",
        "Referer" to "$mainUrl/",
        "Accept" to "application/json, text/plain, */*"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = listOf(
            Pair("$mainUrl/trending?page=$page", "En Tendencia"),
            Pair("$mainUrl/updates?page=$page", "Actualizados")
        )

        val lists = items.map { (url, title) ->
            val document = app.get(url, headers = apiHeaders).document
            val animeList = document.select("div.aitem-wrapper div.aitem").map { element: Element ->
                val link = element.selectFirst("a.poster")?.attr("href") ?: ""
                val name = element.selectFirst("a.title")?.text() ?: ""
                val poster = element.select("a.poster img").attr("data-src")

                newAnimeSearchResponse(name, link, TvType.Anime) {
                    this.posterUrl = poster
                }
            }
            HomePageList(title, animeList)
        }
        return newHomePageResponse(lists, true)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/browser?keyword=$query"
        val document = app.get(url, headers = apiHeaders).document

        return document.select("div.aitem-wrapper div.aitem").map { element: Element ->
            val link = element.selectFirst("a.poster")?.attr("href") ?: ""
            val name = element.selectFirst("a.title")?.text() ?: ""
            val poster = element.select("a.poster img").attr("data-src")

            newAnimeSearchResponse(name, link, TvType.Anime) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: Iniciando load para $url")

        val response = app.get(url, headers = apiHeaders)
        val document = response.document

        val animeId = document.selectFirst("div[data-id]")?.attr("data-id") ?: return null

        // Metadatos
        val title = document.selectFirst("h1.title")?.text() ?: "Unknown"
        val poster = document.selectFirst(".poster img")?.attr("src") ?: document.selectFirst(".poster img")?.attr("data-src")
        val description = document.selectFirst(".desc, .synopsis, .description")?.text()
        val year = document.select(".detail").filter { it.text().contains("Released") }.firstOrNull()?.text()?.replace("Released:", "")?.trim()?.toIntOrNull()
        val genres = document.select(".detail a[href*='genre']").map { it.text() }

        // Lógica del Token
        val encResponse = app.get("$decryptionApi/enc-kai?text=$animeId").parsedSafe<ResultResponse>()
        val cleanToken = encResponse?.result

        if (cleanToken == null) {
            Log.e(TAG, "Logs: Error al obtener token para episodios")
            return null
        }

        // Petición de episodios
        val resJson = app.get("$mainUrl/ajax/episodes/list?ani_id=$animeId&_=$cleanToken", headers = apiHeaders).parsedSafe<ResultResponse>()

        if (resJson?.result == null) {
            Log.e(TAG, "Logs: El servidor devolvió episodios nulos (Posible 403)")
            return null
        }

        // --- ESTA ES LA PARTE QUE FALTABA: PROCESAR LOS EPISODIOS ---
        val epDocument = resJson.toDocument()
        val episodes = epDocument.select("div.eplist a").map { element ->
            val token = element.attr("token")
            val epNum = element.attr("num").toIntOrNull() ?: 0
            val epTitle = element.selectFirst("span")?.text() ?: "Episode $epNum"

            newEpisode(token) {
                this.name = epTitle
                this.episode = epNum
            }
        }.reversed()

        Log.d(TAG, "Logs: Se cargaron ${episodes.size} episodios correctamente")

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = description
            this.year = year
            this.tags = genres
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        // Si data es una URL completa, extraemos solo el ID final para la API
        val cleanId = if (data.contains("/")) data.split("/").last() else data
        Log.d(TAG, "Logs: Iniciando loadLinks para ID limpio: $cleanId")

        val resource = safeApiCall {
            // Usamos parsedSafe para obtener el token de desencriptación limpio
            val encResponse = app.get("$decryptionApi/enc-kai?text=$cleanId").parsedSafe<ResultResponse>()
            val cleanToken = encResponse?.result ?: return@safeApiCall false

            val serverRes = app.get("$mainUrl/ajax/links/list?token=$cleanId&_=$cleanToken", headers = apiHeaders).parsed<ResultResponse>()
            val serverDoc = serverRes.toDocument()

            for (serverElm in serverDoc.select("span.server[data-lid]")) {
                val lid = serverElm.attr("data-lid")
                val serverName = serverElm.text()

                val lidEncResponse = app.get("$decryptionApi/enc-kai?text=$lid").parsedSafe<ResultResponse>()
                val lidEnc = lidEncResponse?.result ?: continue

                val encodedLink = app.get("$mainUrl/ajax/links/view?id=$lid&_=$lidEnc", headers = apiHeaders).parsed<ResultResponse>().result ?: continue

                val iframeRes = app.post("$decryptionApi/dec-kai", json = mapOf("text" to encodedLink)).parsed<IframeResponse>()
                val iframeUrl = iframeRes.result.url

                if (iframeUrl.contains("megaup") || iframeUrl.contains("site")) {
                    loadMegaUpSource(iframeUrl, serverName, callback, subtitleCallback)
                }
            }
            true
        }
        return resource is Resource.Success
    }

    private suspend fun loadMegaUpSource(
        url: String,
        name: String,
        callback: (ExtractorLink) -> Unit,
        subtitleCallback: (SubtitleFile) -> Unit
    ) {
        try {
            val parsedUrl = url.toHttpUrl()
            val token = parsedUrl.pathSegments.lastOrNull() ?: return
            val megaUrl = "${parsedUrl.scheme}://${parsedUrl.host}/media/$token"

            // Obtenemos el token de la página del servidor
            val megaToken = app.get(megaUrl, headers = apiHeaders).parsedSafe<ResultResponse>()?.result ?: return

            // IMPORTANTE: El 'agent' debe ser EXACTAMENTE el mismo que está en apiHeaders
            val megaUpResult = app.post("$decryptionApi/dec-mega",
                json = mapOf(
                    "text" to megaToken,
                    "agent" to apiHeaders["User-Agent"]
                )
            ).parsedSafe<MegaUpResponse>()?.result ?: return

            megaUpResult.tracks.forEach { track: MegaUpTrack ->
                if (track.kind == "captions") {
                    subtitleCallback.invoke(newSubtitleFile(track.label ?: "English", track.file))
                }
            }

            megaUpResult.sources.forEach { source ->
                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = "$name - MegaUp",
                        url = source.file,
                        type = if (source.file.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    ) {
                        this.referer = "$mainUrl/"
                        this.quality = Qualities.P1080.value
                    }
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en loadMegaUpSource: ${e.message}")
        }
    }

}

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResultResponse(
    @JsonProperty("result") val result: String? = null,
    @JsonProperty("message") val message: String? = null,
    @JsonProperty("status") val status: Int? = null
) {
    fun toDocument(): Document = Jsoup.parseBodyFragment(result ?: "")
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class IframeResponse(val result: IframeUrl)
@JsonIgnoreProperties(ignoreUnknown = true)
data class IframeUrl(val url: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MegaUpResponse(
    @JsonProperty("result") val result: MegaUpResult? = null // Añadido ? y = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MegaUpResult(
    @JsonProperty("sources") val sources: List<MegaUpSource> = emptyList(), // Valor por defecto
    @JsonProperty("tracks") val tracks: List<MegaUpTrack> = emptyList()     // Valor por defecto
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class MegaUpSource(val file: String)
@JsonIgnoreProperties(ignoreUnknown = true)
data class MegaUpTrack(val file: String, val label: String?, val kind: String)