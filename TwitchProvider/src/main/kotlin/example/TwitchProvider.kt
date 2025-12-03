package example

import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LiveSearchResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.fixUrl
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newLiveSearchResponse
import com.lagradost.cloudstream3.newLiveStreamLoadResponse
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import android.util.Log
import org.jsoup.nodes.Element
import java.lang.RuntimeException

// **********************************************
// IMPORTACIONES FALTANTES DEL SISTEMA
// Si siguen saliendo errores, puede que necesites importar estas líneas
// manualmente en tu IDE si el compilador no las resuelve:
// import com.lagradost.cloudstream3.network.response
// import com.lagradost.cloudstream3.utils.AppUtils.parseJson
// **********************************************

class TwitchProvider : MainAPI() {
    override var mainUrl = "https://twitchtracker.com"
    override var name = "Twitch"
    override val supportedTypes = setOf(TvType.Live)
    override var lang = "es"
    override val hasMainPage = true
    private val gamesName = "games"
    private val TAG = "TwitchProvider"

    override val mainPage = mainPageOf(
        "$mainUrl/channels/live" to "Streams",
        "$mainUrl/channels/live/spanish" to "Streams en Español",
        "$mainUrl/channels/live/english" to "Streams en Inglés",
        "$mainUrl/channels/live/portuguese" to "Streams en Portugués",
        "$mainUrl/games" to gamesName
    )

    private val isHorizontal = true

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.e(TAG, "getMainPage - Solicitando datos para: ${request.name}")
        return when (request.name) {
            gamesName -> newHomePageResponse(parseGames(), hasNext = false)
            else -> {
                val url = request.data
                // Si la llamada falla, es posible que app.get() necesite una importación
                val doc = app.get(url, params = mapOf("page" to page.toString())).document

                Log.e(TAG, "getMainPage - Longitud del cuerpo: ${doc.body().html().length}. ¿Contiene Cloudflare? ${doc.body().html().contains("Cloudflare")}")

                val channels = doc.select("table#channels tr").mapNotNull { element -> // mapNotNull para filtrar filas de encabezado/malas
                    element.toLiveSearchResponse()
                }

                val finalChannels = channels

                Log.e(TAG, "getMainPage - Canales encontrados: ${finalChannels.size}")

                newHomePageResponse(
                    listOf(
                        HomePageList(
                            request.name,
                            finalChannels,
                            isHorizontalImages = isHorizontal
                        )
                    ),
                    hasNext = true
                )
            }
        }
    }

    // toLiveSearchResponse ahora devuelve LiveSearchResponse?
    private fun Element.toLiveSearchResponse(): LiveSearchResponse? {

        // Seleccionar el TD que contiene el nombre del canal (tercera columna: #1, imagen, nombre/juego)
        val nameCell = this.select("td:eq(2)").first()

        // Seleccionar el <a> dentro de esa celda que tiene el nombre del canal
        val nameAnchor = nameCell?.select("a[href^='/']")?.first()

        // Extraer el linkName (el handle del canal)
        val linkName = nameAnchor?.attr("href")?.substringAfterLast("/") ?: ""

        // Extraer el nombre del canal (el texto)
        val name = nameAnchor?.text()?.trim()

        // Extraer la URL de la imagen (la primera <img> en la fila)
        val image = this.select("img").first()?.attr("src")

        if (name.isNullOrBlank() || linkName.isBlank()) {
            Log.e(TAG, "toLiveSearchResponse - FALLO al extraer datos del canal.")
            return null // Devuelve null si no se puede parsear
        } else {
            Log.e(TAG, "toLiveSearchResponse - Extrayendo canal: $name, LinkName: $linkName")
        }

        return newLiveSearchResponse(
            name,
            linkName,
            TvType.Live,
            fix = false
        ) { posterUrl = image }
    }

    private suspend fun parseGames(): List<HomePageList> {
        val doc = app.get("$mainUrl/games").document
        return doc.select("div.ranked-item")
            .take(5)
            .mapNotNull { element ->
                val game = element.select("div.ri-name > a")
                val url = fixUrl(game.attr("href"))
                val name = game.text()
                // parseGame se llama aquí.
                val searchResponses = parseGame(url).ifEmpty { return@mapNotNull null }
                HomePageList(name, searchResponses, isHorizontalImages = isHorizontal)
            }
    }

    // CORREGIDO: SE ELIMINA LA PALABRA CLAVE 'override'
// Ya no es 'override suspend fun parseGame(...)'
    suspend fun parseGame(url: String): List<LiveSearchResponse> {
        val doc = app.get(url).document
        // mapNotNull filtra los resultados nulos
        return doc.select("td.cell-slot.sm").mapNotNull { element ->
            element.toLiveSearchResponse()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.e(TAG, "load - URL recibida: $url")
        val realUrl = url.substringAfterLast("/")
        Log.e(TAG, "load - Nombre de canal extraído: $realUrl")

        val doc = app.get("$mainUrl/$realUrl", referer = mainUrl).document
        val name = doc.select("div#app-title").text().trim()

        if (name.isBlank()) {
            throw RuntimeException("Could not load page, please try again.\n")
        }

        // Nuevo selector para el Rank
        val rank = doc.select("div.rank-badge span.to-number").text()?.toIntOrNull()

        val image = doc.select("div#app-logo > img").attr("src")

        // Selector para la imagen de fondo (banner)
        val backgroundPoster = doc.select("div.header-background").attr("style")
            .substringAfter("url(")
            .substringBefore(")")
            .trim()
            .replace("'", "") // Limpiar la URL de la imagen de fondo
            .ifEmpty { image } // Usar la imagen de perfil como fallback

        // Selector de descripción, manteniéndolo si la ves en otros canales
        val description = doc.select("div[style='word-wrap:break-word;font-size:12px;']").text().ifEmpty { "" }

        // Corrección: Definir 'language' antes de usarla en tags.
        val language = doc.select("a.label.label-soft").text().ifEmpty { null }

        // Estado Live
        val isLive = doc.select("div.live-indicator-container").isNotEmpty()

        val tags = listOfNotNull(
            isLive.let { if (it) "Live" else "Offline" },
            language, // El error Unresolved reference 'language' está resuelto aquí
            rank?.let { "Rank: $it" },
        )

        val twitchUrl = "https://twitch.tv/$realUrl"
        Log.e(TAG, "load - URL de Twitch (Data para Extractor): $twitchUrl")

        return newLiveStreamLoadResponse(
            name, twitchUrl, twitchUrl
        ) {
            plot = description
            posterUrl = image
            backgroundPosterUrl = backgroundPoster // Usar la nueva variable
            this@newLiveStreamLoadResponse.tags = tags
        }
    }

    // Corrección: Se utiliza mapNotNull y se realiza un cast al tipo de retorno esperado.
    override suspend fun search(query: String): List<SearchResponse>? {
        Log.e(TAG, "search - Query: $query")
        val document =
            app.get("$mainUrl/search", params = mapOf("q" to query), referer = mainUrl).document

        Log.e(TAG, "search - Longitud del cuerpo: ${document.body().html().length}. ¿Contiene Cloudflare? ${document.body().html().contains("Cloudflare")}")

        // Se filtran los resultados nulos y se hace un cast a SearchResponse
        val results = document.select("table.tops tr").mapNotNull {
            it.toLiveSearchResponse()
        }.map { it as SearchResponse } // Cast LiveSearchResponse a SearchResponse

        Log.e(TAG, "search - Resultados encontrados: ${results.size}")

        return results
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.e(TAG, "loadLinks - DATA enviada al Extractor: $data")
        // Tu extractor original (pwn.sh) se mantiene, pero recuerda que probablemente fallará en tiempo de ejecución.
        TwitchExtractor().getUrl(data, null, subtitleCallback, callback)

        return true
    }

    class TwitchExtractor : ExtractorApi() {
        private val EXTRACTOR_TAG = "TwitchExtractor"

        override val mainUrl = "https://twitch.tv/"
        override val name = "Twitch"
        override val requiresReferer = false

        data class ApiResponse(
            val success: Boolean,
            val urls: Map<String, String>?
        )

        override suspend fun getUrl(
            url: String,
            referer: String?,
            subtitleCallback: (SubtitleFile) -> Unit,
            callback: (ExtractorLink) -> Unit
        ) {
            Log.e(EXTRACTOR_TAG, "getUrl - URL de Stream recibida: $url")
            val apiUrl = "https://pwn.sh/tools/streamapi.py?url=$url"
            Log.e(EXTRACTOR_TAG, "getUrl - API URL final: $apiUrl")

            // Si .parsed<ApiResponse>() da error, necesitas la importación:
            // import com.lagradost.cloudstream3.network.response
            // import com.lagradost.cloudstream3.utils.AppUtils.parseJson
            val response =
                app.get(apiUrl).parsed<ApiResponse>()

            Log.e(EXTRACTOR_TAG, "getUrl - API Response Success: ${response.success}")

            if (response.success != true) {
                Log.e(EXTRACTOR_TAG, "getUrl - FALLO CRÍTICO: StreamAPI devolvió éxito=false.")
                throw RuntimeException("StreamAPI failed to get links for $url")
            }

            response.urls?.forEach { (name, linkUrl) ->
                if (name.contains("audio_only", ignoreCase = true)) {
                    Log.e(EXTRACTOR_TAG, "getUrl - Omitiendo enlace de solo audio.")
                    return@forEach
                }

                val quality = getQualityFromName(name.substringBefore("p"))

                Log.e(EXTRACTOR_TAG, "getUrl - Encontrado enlace. Nombre: $name, Calidad: $quality")

                if (quality == 0 && !name.contains("p")) {
                    Log.e(EXTRACTOR_TAG, "getUrl - Omitiendo enlace con calidad 0 sin 'p'.")
                    return@forEach
                }

                callback.invoke(
                    newExtractorLink(
                        this.name,
                        "${this.name} ${name.replace("${quality}p", "").trim()}",
                        linkUrl
                    ) {
                        this.type = ExtractorLinkType.M3U8
                        this.quality = quality
                        this.referer = ""
                    }
                )
            }
            Log.e(EXTRACTOR_TAG, "getUrl - Finalizado el procesamiento de enlaces.")
        }
    }
}