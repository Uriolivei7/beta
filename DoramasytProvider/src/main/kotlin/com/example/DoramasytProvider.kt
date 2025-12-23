package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import java.util.*
import kotlin.collections.ArrayList
import android.util.Log
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.*

class DoramasytProvider : MainAPI() {
    companion object {
        fun getType(t: String): TvType {
            return if (t.contains("OVA") || t.contains("Especial")) TvType.OVA
            else if (t.contains("Pelicula")) TvType.Movie
            else TvType.TvSeries
        }
        fun getDubStatus(title: String): DubStatus {
            return if (title.contains("Latino") || title.contains("Castellano"))
                DubStatus.Dubbed
            else DubStatus.Subbed
        }

        var latestCookie: Map<String, String> = emptyMap()
        var latestToken = ""
    }

    override var mainUrl = "https://www.doramasyt.com"
    override var name = "DoramasYT"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.AsianDrama)

    // Lógica importante de posters (Limpieza de parámetros y fixUrl)
    private fun cleanPoster(url: String?): String? {
        if (url.isNullOrEmpty()) return null
        val cleaned = url.substringBefore("?v=")
        return fixUrl(cleaned)
    }

    private suspend fun getToken(url: String): Map<String, String> {
        try {
            val maintas = app.get(url, headers = mapOf(
                "User-Agent" to USER_AGENT,
                "X-Requested-With" to "XMLHttpRequest"
            ))
            val token = maintas.document.selectFirst("html head meta[name=csrf-token]")?.attr("content") ?: ""
            latestToken = token
            latestCookie = maintas.cookies
            Log.d("Doramasyt", "Token obtenido correctamente")
        } catch (e: Exception) {
            Log.e("Doramasyt", "Error obteniendo Token: ${e.message}")
        }
        return latestCookie
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val items = ArrayList<HomePageList>()

        // 1. ÚLTIMOS CAPÍTULOS
        try {
            val response = app.get(mainUrl, timeout = 120)
            val latestChapters = response.document.select("h2:contains(últimos capítulos) + ul li.col article").mapNotNull { element ->
                val title = element.selectFirst("h3")?.text()?.trim() ?: return@mapNotNull null

                // Selector de posters corregido para el data-src
                val imgElement = element.selectFirst("img")
                val posterRaw = imgElement?.attr("data-src")?.ifEmpty { imgElement.attr("src") }

                val url = element.selectFirst("a")?.attr("href")
                    ?.replace("ver/", "dorama/")
                    ?.replace(Regex("-episodio-\\d+"), "-sub-espanol") ?: return@mapNotNull null

                newAnimeSearchResponse(title, fixUrl(url)) {
                    this.posterUrl = cleanPoster(posterRaw)
                    addDubStatus(getDubStatus(title))
                }
            }
            if (latestChapters.isNotEmpty()) {
                // Pasamos 'true' directamente en la tercera posición
                items.add(HomePageList("Últimos capítulos", latestChapters, true))
            }
        } catch (e: Exception) {
            Log.e("Doramasyt", "Error en Últimos Capítulos: ${e.message}")
        }

        // 2. OTRAS SECCIONES
        val urls = listOf(
            Pair("$mainUrl/emision", "En emisión"),
            Pair("$mainUrl/doramas", "Doramas"),
            Pair("$mainUrl/doramas?categoria=pelicula", "Películas")
        )

        coroutineScope {
            urls.map { (url, name) ->
                async {
                    try {
                        val response = app.get(url)
                        val homeList = response.document.select("li.col").mapNotNull { element ->
                            val title = element.selectFirst("h3")?.text()?.trim() ?: return@mapNotNull null
                            val imgElement = element.selectFirst("img")
                            val posterRaw = imgElement?.attr("data-src")?.ifEmpty { imgElement.attr("src") }

                            newAnimeSearchResponse(title, fixUrl(element.selectFirst("a")!!.attr("href"))) {
                                this.posterUrl = cleanPoster(posterRaw)
                            }
                        }
                        if (homeList.isNotEmpty()) {
                            // Pasamos 'true' directamente en la tercera posición
                            items.add(HomePageList(name, homeList, true))
                        }
                    } catch (e: Exception) {
                        Log.e("Doramasyt", "Error en sección $name: ${e.message}")
                    }
                }
            }.awaitAll()
        }

        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("Doramasyt", "Buscando: $query")
        return app.get("$mainUrl/buscar?q=$query").document.select("li.col").map {
            val title = it.selectFirst("h3")!!.text()
            val posterRaw = it.selectFirst("img")?.attr("data-src")
            newAnimeSearchResponse(title, it.selectFirst("a")!!.attr("href")) {
                this.posterUrl = cleanPoster(posterRaw)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("Doramasyt", "Cargando dorama: $url")
        getToken(url)
        val doc = app.get(url, timeout = 120).document

        val title = doc.selectFirst(".fs-2")?.text() ?: ""
        val posterRaw = doc.selectFirst("img.rounded-3")?.attr("data-src")
        val backRaw = doc.selectFirst("img.w-100")?.attr("data-src")

        val caplistUrl = doc.selectFirst(".caplist")?.attr("data-ajax") ?: throw ErrorLoadingException("No se encontró lista de episodios")

        val capJson = app.post(caplistUrl,
            headers = mapOf("Referer" to url, "X-Requested-With" to "XMLHttpRequest"),
            cookies = latestCookie,
            data = mapOf("_token" to latestToken)).parsed<CapList>()

        val epList = capJson.eps.map { ep ->
            val epUrl = "${url.replace("-sub-espanol","").replace("/dorama/","/ver/")}-episodio-${ep.num}"
            newEpisode(epUrl) { this.episode = ep.num }
        }

        return newAnimeLoadResponse(title, url, TvType.AsianDrama) {
            this.posterUrl = cleanPoster(posterRaw)
            this.backgroundPosterUrl = cleanPoster(backRaw)
            this.plot = doc.selectFirst("div.mb-3")?.text()?.replace("Ver menos", "")
            addEpisodes(DubStatus.Subbed, epList)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("Doramasyt", "Iniciando carga de links para: $data")
        try {
            // Es vital enviar el Referer y User-Agent para que la web no oculte los links
            val response = app.get(data, headers = mapOf(
                "Referer" to mainUrl,
                "User-Agent" to USER_AGENT
            ))

            // Selector ampliado para capturar botones de servidores y elementos de lista
            val buttons = response.document.select("button[data-player], li.play-video, [data-video]")
            Log.d("Doramasyt", "Elementos de video detectados: ${buttons.size}")

            buttons.forEach { button ->
                try {
                    val serverName = button.text().trim().ifEmpty { "Reproductor" }
                    // Intentamos obtener el link de diferentes atributos comunes
                    val encoded = button.attr("data-player").ifEmpty {
                        button.attr("data-video").ifEmpty {
                            button.attr("data-id")
                        }
                    }

                    if (encoded.isNullOrEmpty()) return@forEach

                    val isApi = button.attr("data-usa-api") == "1" || button.hasAttr("data-player")

                    if (isApi) {
                        // Caso 1: El link requiere pasar por el reproductor interno de la web
                        val iframeUrl = "$mainUrl/reproductor?video=$encoded"
                        Log.d("Doramasyt", "Consultando API interna: $iframeUrl")

                        val iframeDoc = app.get(iframeUrl, headers = mapOf(
                            "Referer" to data,
                            "X-Requested-With" to "XMLHttpRequest",
                            "User-Agent" to USER_AGENT
                        )).document

                        val iframeSrc = iframeDoc.selectFirst("iframe")?.attr("src")

                        if (!iframeSrc.isNullOrEmpty()) {
                            Log.d("Doramasyt", "Iframe encontrado: $iframeSrc")
                            customLoadExtractor(fixUrl(iframeSrc), iframeUrl, subtitleCallback, callback)
                        }
                    } else {
                        // Caso 2: El link está en Base64
                        val decoded = base64Decode(encoded)
                        if (decoded.startsWith("http")) {
                            Log.d("Doramasyt", "Link decodificado: $decoded")
                            customLoadExtractor(decoded, data, subtitleCallback, callback)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Doramasyt", "Error procesando botón: ${e.message}")
                }
            }
            return true
        } catch (e: Exception) {
            Log.e("Doramasyt", "Error general en loadLinks: ${e.message}")
            return false
        }
    }

    private suspend fun customLoadExtractor(
        url: String,
        ref: String?,
        sub: (SubtitleFile) -> Unit,
        cb: (ExtractorLink) -> Unit
    ) {
        // Corrección de dominios para que los extractores nativos funcionen (Streamwish, Vidhide, etc)
        val fixedUrl = url.replace("hglink.to", "streamwish.to")
            .replace("swdyu.com", "streamwish.to")
            .replace("mivalyo.com", "vidhidepro.com")
            .replace("filemoon.link", "filemoon.sx")

        Log.d("Doramasyt", "Llamando extractor para: $fixedUrl")
        try {
            loadExtractor(fixedUrl, ref, sub, cb)
        } catch (e: Exception) {
            Log.e("Doramasyt", "Extractor falló para $fixedUrl: ${e.message}")
        }
    }

    data class CapList(@JsonProperty("eps") val eps: List<Ep>)
    data class Ep(val num: Int?)
}