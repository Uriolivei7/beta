package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.DubStatus
import com.lagradost.cloudstream3.Episode
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.LoadResponse.Companion.addAniListId
import com.lagradost.cloudstream3.LoadResponse.Companion.addMalId
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.Score
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SearchResponseList
import com.lagradost.cloudstream3.ShowStatus
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.addDate
import com.lagradost.cloudstream3.addDubStatus
import com.lagradost.cloudstream3.addEpisodes
import com.lagradost.cloudstream3.amap
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.fixUrl
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newAnimeLoadResponse
import com.lagradost.cloudstream3.newAnimeSearchResponse
import com.lagradost.cloudstream3.newEpisode
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.toNewSearchResponseList
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlin.random.Random


class AnimekaiProvider : MainAPI() {
    override var mainUrl = AnimeKaiPlugin.currentAnimeKaiServer
    override var name = "AnimeKai"
    override val hasQuickSearch = true
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val usesWebView = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie, TvType.OVA)

    private fun Element.toSearchResult(): SearchResponse {
        val href = fixUrl(this.select("a.poster").attr("href"))
        val title = this.select("a.title").text()
        val subCount = this.selectFirst("div.info span.sub")?.text()?.toIntOrNull()
        val dubCount = this.selectFirst("div.info span.dub")?.text()?.toIntOrNull()
        val posterUrl = fixUrl(this.select("a.poster img").attr("data-src"))
        val type = getType(this.selectFirst("div.fd-infor > span.fdi-item")?.text() ?: "")

        return newAnimeSearchResponse(title, href, type) {
            this.posterUrl = posterUrl
            addDubStatus(dubCount != null, subCount != null, dubCount, subCount)
        }
    }

    private fun Element.toRecommendResult(): SearchResponse {
        val href = fixUrl(this.attr("href"))
        val title = this.select("div.title").text()
        val posterUrl = fixUrl(this.attr("style").substringAfter("('").substringBefore("')"))
        return newAnimeSearchResponse(title, href) {
            this.posterUrl = posterUrl
        }
    }


    companion object {

        suspend fun decode(text: String?): String {
            return try {
                val res = app.get("${BuildConfig.KAIENC}?text=$text").text
                JSONObject(res).getString("result")
            } catch (_: Exception) {
                app.get("${BuildConfig.KAISVA}/?f=e&d=$text").text
            }
        }

        private val JSON = "application/json; charset=utf-8".toMediaType()

        suspend fun decodeReverse(text: String): String {
            val jsonBody = """{"text":"$text"}""".toRequestBody(JSON)

            return try {
                val res = app.post(
                    BuildConfig.KAIDEC,
                    requestBody = jsonBody
                ).text
                JSONObject(res).getString("result")
            } catch (_: Exception) {
                app.get("${BuildConfig.KAISVA}/?f=d&d=$text").text
            }
        }



        fun getType(t: String): TvType {
            val lower = t.lowercase()
            return when {
                "ova" in lower || "special" in lower -> TvType.OVA
                "movie" in lower -> TvType.AnimeMovie
                else -> TvType.Anime
            }
        }

        fun getStatus(t: String): ShowStatus {
            return when (t) {
                "Finished Airing" -> ShowStatus.Completed
                "Releasing" -> ShowStatus.Ongoing
                else -> ShowStatus.Completed // optionally log unexpected status
            }
        }
    }


    override val mainPage = mainPageOf(
            "$mainUrl/browser?keyword=&status[]=releasing&sort=trending" to "Trending",
            "$mainUrl/browser?keyword=&status[]=releasing&sort=updated_date" to "Latest Episode",
            "$mainUrl/browser?keyword=&type[]=tv&status[]=releasing&sort=added_date&language[]=sub&language[]=softsub" to "Recently SUB",
            "$mainUrl/browser?keyword=&type[]=tv&status[]=releasing&sort=added_date&language[]=dub" to "Recently DUB",
    )

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query,1).items


    override suspend fun search(query: String,page: Int): SearchResponseList {
        val link = "$mainUrl/browser?keyword=$query&page=$page"
        val res = app.get(link).documentLarge
        return res.select("div.aitem-wrapper div.aitem").map { it.toSearchResult() }.toNewSearchResponseList()
    }


    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        delay(Random.nextLong(1000, 2000))

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
            "Accept-Language" to "en-US,en;q=0.9",
            "Referer" to "$mainUrl/",
            "Connection" to "keep-alive",
            "Upgrade-Insecure-Requests" to "1",
            "Cookie" to "usertype=guest; session=Mv2Y6x1b2I8SEw3fj0eNDfQYJM3CTpH9KjJc3ACK; cf_clearance=z9kEgtOSx3us4aluy5_5MfYEL6Ei8RJ3jCbcFTD2R1E-1745122952-1.2.1.1-UYjW2QUhPKUmojZE3XUE.gqHf3g5O6lvdl0qDCNPb5IjjavrpZIOpbE64osKxLbcblCAWynfNLv6bKSO75WzURG.FqDtfcu_si3MrCHECNtbMJC.k9cuhqDRcsz8hHPgpQE2fY8rR1z5Z4HfGmCw2MWMT6GelsZW_RQrTMHUYtIqjaEiAtxfcg.O4v_RGPwio_2J2V3rP16JbWO8wRh_dObNvWSMwMW.t44PhOZml_xWuh7DH.EIxLu3AzI91wggYU9rw6JJkaWY.UBbvWB0ThZRPTAJZy_9wlx2QFyh80AXU2c5BPHwEZPQhTQHBGQZZ0BGZkzoAB8pYI3f3eEEpBUW9fEbEJ9uoDKs7WOow8g"
        )

        val res = app.get("${request.data}&page=$page", headers).documentLarge
        val items = res.select("div.aitem-wrapper div.aitem").map { it.toSearchResult() }

        return newHomePageResponse(request.name, items)
    }

    override suspend fun load(url: String): LoadResponse {
        val TAG = "Animekai"
        val document = app.get(url).documentLarge

        // 1. Mejora de Póster: Buscamos la imagen original de alta calidad
        val poster = document.selectFirst(".anisc-poster img")?.attr("src")
            ?: document.selectFirst("div.poster img")?.attr("src")
            ?: document.select("meta[property=og:image]").attr("content")

        val title = document.selectFirst("h1.title")?.text() ?: "Sin título"
        val plot = document.selectFirst("div.desc")?.text() ?: ""

        // 2. IDs para sincronización
        val malid = document.select("div.watch-section").attr("data-mal-id")
        val aniid = document.select("div.watch-section").attr("data-al-id")

        // 3. ID de Anime para episodios
        val animeId = document.selectFirst("div.rate-box")?.attr("data-id")
            ?: document.selectFirst(".watch-section")?.attr("data-id")
            ?: url.split("-").lastOrNull()?.filter { it.isDigit() || it.isLetter() }

        val subEpisodes = mutableListOf<Episode>()
        val dubEpisodes = mutableListOf<Episode>()

        if (!animeId.isNullOrEmpty()) {
            try {
                // Decodificación
                val decodedToken = decode(animeId)

                // Si el token sigue siendo null, intentamos usar el ID crudo como respaldo
                val finalToken = if (decodedToken == "null" || decodedToken.isNullOrEmpty()) animeId else decodedToken

                val epUrl = "$mainUrl/ajax/episodes/list?ani_id=$animeId&_=$finalToken"
                Log.i(TAG, "Cargando episodios: $epUrl")

                val responseText = app.get(epUrl).text
                val jsonResponse = JSONObject(responseText)
                val htmlResult = jsonResponse.optString("result")

                if (!htmlResult.isNullOrBlank()) {
                    val epDocument = Jsoup.parse(htmlResult)
                    val episodesList = epDocument.select("div.eplist a")

                    episodesList.forEachIndexed { index, ep ->
                        val episodeNum = index + 1
                        val token = ep.attr("token")
                        val name = ep.selectFirst("span")?.text() ?: "Episodio $episodeNum"

                        val episodeObj = newEpisode("sub|$token") {
                            this.name = name
                            this.episode = episodeNum
                        }
                        subEpisodes.add(episodeObj)

                        if (ep.hasClass("dub") || ep.selectFirst(".dub") != null) {
                            dubEpisodes.add(newEpisode("dub|$token") {
                                this.name = name
                                this.episode = episodeNum
                            })
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en lista de episodios: ${e.message}")
            }
        }

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.engName = title
            this.posterUrl = poster
            addEpisodes(DubStatus.Subbed, subEpisodes)
            addEpisodes(DubStatus.Dubbed, dubEpisodes)
            this.plot = plot
            // Quitamos recomendaciones para mayor velocidad
            addMalId(malid.toIntOrNull())
            addAniListId(aniid.toIntOrNull())
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val TAG = "AnimekaiLinks"
        Log.i(TAG, "Iniciando loadLinks con data: $data")

        // 1. Extraer el token del episodio
        val token = data.split("|").last()
        val dubType = data.split("|").firstOrNull() ?: "sub"
        val types = if (dubType == "sub") listOf("sub", "softsub") else listOf(dubType)

        // 2. Decodificar el token para el parámetro de seguridad _
        val decodeToken = decode(token)
        Log.d(TAG, "Token de episodio: $token | Decodificado (_): $decodeToken")

        // 3. Obtener lista de servidores (AJAX)
        val linkListUrl = "$mainUrl/ajax/links/list?token=$token&_=$decodeToken"
        val responseText = app.get(linkListUrl).text

        val jsonResponse = try { JSONObject(responseText) } catch(e: Exception) {
            Log.e(TAG, "Error al parsear JSON de servidores: ${e.message}")
            return false
        }

        val htmlResult = jsonResponse.optString("result")
        if (htmlResult.isNullOrBlank()) {
            Log.e(TAG, "No se recibió HTML de servidores. Respuesta: $responseText")
            return false
        }

        val document = Jsoup.parse(htmlResult)
        val servers = types.flatMap { type ->
            document.select("div.server-items[data-id=$type] span.server[data-lid]")
                .map { server ->
                    val lid = server.attr("data-lid")
                    val serverName = server.text()
                    Log.i(TAG, "Servidor encontrado: $serverName (LID: $lid) para tipo: $type")
                    Triple(type, lid, serverName)
                }
        }.distinctBy { it.second } // Evitar duplicados por LID

        // 4. Procesar cada servidor para extraer el iframe
        servers.amap { (type, lid, serverName) ->
            try {
                val decodeLid = decode(lid)
                val viewUrl = "$mainUrl/ajax/links/view?id=$lid&_=$decodeLid"

                val viewRes = app.get(viewUrl).text
                val viewJson = JSONObject(viewRes)
                val encryptedResult = viewJson.optString("result")

                if (encryptedResult.isNotEmpty()) {
                    // Desencriptar el resultado para obtener la URL del iframe
                    val decryptedIframeJson = decodeReverse(encryptedResult)
                    val finalUrl = JSONObject(decryptedIframeJson).getString("url")

                    Log.d(TAG, "Extrayendo video de: $finalUrl")

                    val nameSuffix = if (type == "softsub") " [Soft Sub]" else ""
                    val displayName = "⌜ AnimeKai ⌟ | $serverName$nameSuffix"

                    loadExtractor(finalUrl, displayName, subtitleCallback, callback)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando servidor $serverName: ${e.message}")
            }
        }
        return true
    }

    // Clases de apoyo actualizadas para evitar errores de Jackson
    data class Response(
        @JsonProperty("status") val status: Any? = null, // Cambiado a Any por si viene int o bool
        @JsonProperty("result") val result: String? = null
    )

    private fun extractVideoUrlFromJson(jsonData: String): String {
        return try {
            JSONObject(jsonData).getString("url")
        } catch (e: Exception) {
            ""
        }
    }
}
