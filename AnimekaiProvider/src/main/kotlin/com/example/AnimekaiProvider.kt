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

        val imgElement = this.select("a.poster img")
        val posterUrl = fixUrl(imgElement.attr("data-src").ifBlank { imgElement.attr("src") })

        val typeStr = this.selectFirst("div.fd-infor > span.fdi-item")?.text() ?: ""
        val type = getType(typeStr)

        return newAnimeSearchResponse(title, href, type) {
            this.posterUrl = posterUrl
            addDubStatus(dubCount != null, subCount != null, dubCount, subCount)
        }
    }

    private fun getType(type: String): TvType {
        return when (type.lowercase()) {
            "tv", "anime" -> TvType.Anime
            "movie", "película" -> TvType.AnimeMovie
            "ova", "special", "ona" -> TvType.OVA
            else -> TvType.Anime
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
        private const val KAI_ENC = "https://api.animekai.to/api/v1/encrypt"
        private const val KAI_DEC = "https://api.animekai.to/api/v1/decrypt"
        private const val KAI_SVA = "https://sva.animekai.la"

        suspend fun decode(text: String?): String? {
            if (text.isNullOrEmpty()) return null
            return try {
                val res = app.get("$KAI_ENC?text=$text").text
                JSONObject(res).optString("result").takeIf { it.isNotBlank() && it != "null" }
                    ?: throw Exception()
            } catch (_: Exception) {
                try {
                    val res = app.get("$KAI_SVA/?f=e&d=$text").text
                    res.takeIf { it.isNotBlank() && it != "null" } ?: text
                } catch (_: Exception) { text }
            }
        }

        suspend fun decodeReverse(text: String): String {
            val jsonBody = """{"text":"$text"}""".toRequestBody("application/json; charset=utf-8".toMediaType())
            return try {
                val res = app.post(KAI_DEC, requestBody = jsonBody).text
                JSONObject(res).getString("result")
            } catch (_: Exception) {
                try { app.get("$KAI_SVA/?f=d&d=$text").text } catch (_: Exception) { text }
            }
        }

        fun getStatus(t: String?): ShowStatus {
            return when (t?.lowercase()) {
                "finished airing", "finalizado" -> ShowStatus.Completed
                "releasing", "en emisión" -> ShowStatus.Ongoing
                else -> ShowStatus.Completed
            }
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/browser?keyword=&status[]=releasing&sort=trending" to "Trending",
        "$mainUrl/browser?keyword=&status[]=releasing&sort=updated_date" to "Latest Episode",
        "$mainUrl/browser?keyword=&type[]=tv&status[]=releasing&sort=added_date&language[]=sub&language[]=softsub" to "Recently SUB",
        "$mainUrl/browser?keyword=&type[]=tv&status[]=releasing&sort=added_date&language[]=dub" to "Recently DUB",
    )

    override suspend fun search(query: String, page: Int): SearchResponseList {
        val link = "$mainUrl/browser?keyword=$query&page=$page"
        val res = app.get(link).documentLarge
        return res.select("div.aitem-wrapper div.aitem").map { it.toSearchResult() }.toNewSearchResponseList()
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val res = app.get("${request.data}&page=$page").documentLarge
        val items = res.select("div.aitem-wrapper div.aitem").map { it.toSearchResult() }
        return newHomePageResponse(request.name, items)
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).documentLarge

        val poster = document.selectFirst(".anisc-poster img")?.attr("src")?.replace("-600x900", "")
            ?: document.selectFirst("div.poster img")?.attr("src")

        val title = document.selectFirst("h1.title")?.text() ?: "Sin título"
        val plot = document.selectFirst("div.desc")?.text() ?: ""
        val statusText = document.select("div:containsOwn(Status) span").firstOrNull()?.text()

        val malid = document.select("div.watch-section").attr("data-mal-id")
        val aniid = document.select("div.watch-section").attr("data-al-id")

        // Extracción del ID numérico real desde los scripts de la página
        val scriptData = document.select("script").map { it.data() }
        val animeId = scriptData.firstOrNull { it.contains("anime_id") }
            ?.substringAfter("anime_id = '")?.substringBefore("'")
            ?: document.selectFirst("#syncData")?.attr("data-id")
            ?: document.selectFirst(".watch-section")?.attr("data-id")

        Log.i("AnimeKai", "ID detectado: $animeId")

        val subEpisodes = mutableListOf<Episode>()
        val dubEpisodes = mutableListOf<Episode>()

        if (!animeId.isNullOrEmpty()) {
            try {
                val decodedToken = decode(animeId)
                val finalToken = if (decodedToken.isNullOrBlank() || decodedToken == "null") animeId else decodedToken

                val responseText = app.get("$mainUrl/ajax/episodes/list?ani_id=$animeId&_=$finalToken").text
                val htmlResult = JSONObject(responseText).optString("result")

                if (htmlResult.isNotBlank()) {
                    val epDocument = Jsoup.parse(htmlResult)
                    epDocument.select("div.eplist a").forEachIndexed { index, ep ->
                        val episodeNum = index + 1
                        val token = ep.attr("token")
                        val epName = ep.selectFirst("span")?.text() ?: "Episodio $episodeNum"

                        subEpisodes.add(newEpisode("sub|$token") {
                            this.name = epName
                            this.episode = episodeNum
                        })

                        if (ep.hasClass("dub") || ep.selectFirst(".dub") != null) {
                            dubEpisodes.add(newEpisode("dub|$token") {
                                this.name = epName
                                this.episode = episodeNum
                            })
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AnimeKai", "Error episodios: ${e.message}")
            }
        }

        // Recuperar recomendaciones
        val recommendations = document.select("div.aitem-col a").map { it.toRecommendResult() }

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            addEpisodes(DubStatus.Subbed, subEpisodes)
            addEpisodes(DubStatus.Dubbed, dubEpisodes)
            this.plot = plot
            this.showStatus = getStatus(statusText)
            this.recommendations = recommendations
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
        val token = data.split("|").last()
        val dubType = data.split("|").firstOrNull() ?: "sub"
        val types = if (dubType == "sub") listOf("sub", "softsub") else listOf(dubType)

        val decodeToken = decode(token)
        val responseText = app.get("$mainUrl/ajax/links/list?token=$token&_=$decodeToken").text
        val htmlResult = JSONObject(responseText).optString("result")

        if (htmlResult.isBlank()) return false

        val document = Jsoup.parse(htmlResult)
        val servers = types.flatMap { type ->
            document.select("div.server-items[data-id=$type] span.server[data-lid]")
                .map { Triple(type, it.attr("data-lid"), it.text()) }
        }.distinctBy { it.second }

        servers.amap { (type, lid, serverName) ->
            try {
                val decodeLid = decode(lid)
                val viewRes = app.get("$mainUrl/ajax/links/view?id=$lid&_=$decodeLid").text
                val encryptedResult = JSONObject(viewRes).optString("result")

                if (encryptedResult.isNotEmpty()) {
                    val decryptedIframeJson = decodeReverse(encryptedResult)
                    val finalUrl = JSONObject(decryptedIframeJson).getString("url")
                    val nameSuffix = if (type == "softsub") " [Soft Sub]" else ""
                    loadExtractor(finalUrl, "⌜ AnimeKai ⌟ | $serverName$nameSuffix", subtitleCallback, callback)
                }
            } catch (e: Exception) {
                Log.e("AnimeKaiLinks", "Error server $serverName: ${e.message}")
            }
        }
        return true
    }
}