package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.network.CloudflareKiller
import org.jsoup.nodes.Document
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Element

class AnimeLatinoHDProvider : MainAPI() {
    override var mainUrl = "https://www.animelatinohd.com"
    override var name = "AnimeLatinoHD"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasDownloadSupport = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val cloudflareKiller = CloudflareKiller()

    override val mainPage = mainPageOf(
        "$mainUrl/animes" to "Animes",
        "$mainUrl/animes/populares" to "Populares",
        "$mainUrl/animes/mas-vistos" to "Más Vistos",
        "$mainUrl/animes/latino" to "Latino",
        "$mainUrl/animes/castellano" to "Castellano",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (request.data.contains('?')) "${request.data}&page=$page" else "${request.data}?page=$page"
        val document = app.get(url, interceptor = cloudflareKiller).document

        val ssrScript = document.selectFirst("script#ssr-init")
        if (ssrScript != null) {
            val items = parseSsrJson(ssrScript.data())
            if (items.isNotEmpty()) {
                return newHomePageResponse(
                    list = HomePageList(
                        name = request.name,
                        list = items,
                        isHorizontalImages = false
                    ),
                    hasNext = true
                )
            }
        }

        val fallback = document.select("a.animeCard_animeCard__A3lxl, div.listAnime a").mapNotNull { it.toAnimeCardResult() }
        return newHomePageResponse(
            list = HomePageList(name = request.name, list = fallback, isHorizontalImages = false),
            hasNext = true
        )
    }

    private fun parseSsrJson(json: String): List<SearchResponse> {
        val results = mutableListOf<SearchResponse>()
        return try {
            val root = JSONObject(json)
            val b = root.opt("b")
            val children = mutableListOf<String>()
            
            if (b is JSONArray) {
                for (i in 0 until b.length()) {
                    children.add(b.getString(i))
                }
            }
            
            if (children.isEmpty()) return results

            val ssrData = children.find { it.contains("\"episodes\"") || it.contains("\"animeSlug\"") }
            if (ssrData != null) {
                val cleanJson = ssrData.replace("&quot;", "\"")
                val dataObj = JSONObject(cleanJson)
                val animeName = dataObj.optString("animeName", "")
                val animeSlug = dataObj.optString("animeSlug", "")
                val animeBanner = dataObj.optString("animeBanner", "")
                if (animeSlug.isNotBlank()) {
                    results.add(
                        newTvSeriesSearchResponse(animeName, "$mainUrl/anime/$animeSlug", TvType.Anime) {
                            posterUrl = if (animeBanner.startsWith("http")) animeBanner else "https://media.themoviedb.org/t/p/w300$animeBanner"
                        }
                    )
                }
            }

            if (results.isEmpty()) {
                val allContent = children.joinToString("\n")
                Regex("\"href\":\"([^\"]+)\",\"className\":\"animeCard[^\"]*\"").findAll(allContent).forEach { match ->
                    val href = match.groupValues[1]
                    val fullHref = if (href.startsWith("http")) href else "$mainUrl$href"
                    if (href.contains("/anime/")) {
                        val title = Regex("\"children\":\"([^\"]+)\"").find(allContent)?.groupValues?.get(1) ?: ""
                        if (title.isNotBlank()) {
                            results.add(newTvSeriesSearchResponse(title, fullHref, TvType.Anime))
                        }
                    }
                }
            }
            results
        } catch (e: Exception) {
            Log.e("AnimeLatinoHD", "Error parsing SSR JSON: ${e.message}")
            results
        }
    }

    private fun parseSearchJson(json: String): List<SearchResponse> {
        val results = mutableListOf<SearchResponse>()
        return try {
            val root = JSONObject(json)
            val data = root.optJSONArray("data") ?: root.optJSONArray("animes") ?: return results
            
            for (i in 0 until data.length()) {
                val item = data.getJSONObject(i)
                val title = item.optString("name", item.optString("title", ""))
                val slug = item.optString("slug", "")
                val poster = item.optString("poster", item.optString("image", ""))

                val href = if (slug.isNotBlank()) "$mainUrl/anime/$slug" else ""
                if (title.isNotBlank() && href.isNotBlank()) {
                    results.add(
                        newTvSeriesSearchResponse(title, href, TvType.Anime) {
                            this.posterUrl = if (poster.startsWith("http")) poster else "https://media.themoviedb.org/t/p/w300$poster"
                        }
                    )
                }
            }
            results
        } catch (e: Exception) {
            Log.e("AnimeLatinoHD", "Error parsing search API JSON: ${e.message}")
            results
        }
    }

    private fun Element.toAnimeCardResult(): SearchResponse {
        val href = this.attr("abs:href").ifBlank { this.attr("href") }
        val img = this.selectFirst("img")
        val poster = img?.run { attr("src").ifBlank { attr("data-src") } }
        val title = this.selectFirst("h2")?.text()?.trim()
            ?: img?.attr("alt")?.replace("Ver ", "")?.replace(" Online", "")?.trim() ?: ""
        return newTvSeriesSearchResponse(title, href, TvType.Anime) {
            this.posterUrl = poster
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        val apiUrl = "$mainUrl/api/search?query=${query.replace(" ", "+")}"
        return try {
            val response = app.get(apiUrl, interceptor = cloudflareKiller).text
            val apiResults = parseSearchJson(response)
            if (apiResults.isNotEmpty()) return apiResults

            val docUrl = "$mainUrl/animes?search=${query.replace(" ", "+")}"
            val document = app.get(docUrl, interceptor = cloudflareKiller).document
            val ssrScript = document.selectFirst("script#ssr-init")
            if (ssrScript != null) {
                val allItems = parseSsrJson(ssrScript.data())
                if (allItems.isNotEmpty()) {
                    val q = query.lowercase()
                    return allItems.filter { it.name.lowercase().contains(q) }
                }
            }
            document.select("a.animeCard_animeCard__A3lxl, div.listAnime a").mapNotNull { it.toAnimeCardResult() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url, interceptor = cloudflareKiller).document

        val title = document.selectFirst("h1")?.ownText()?.trim()
            ?: document.selectFirst("h1")?.text()?.trim()
            ?: "Unknown"

        val poster = extractPoster(document)
        val description = document.selectFirst("p.page_overview__iOivM")?.text()
            ?: document.selectFirst("div.page_animeInfoRight__DwfVi > p")?.text()
            ?: ""
        val tags = document.select("div.page_genres__NsuIW a, div.page_genres__NsuIW span").map { it.text() }
        val statusText = document.select("div.page_animeDetailItem__wB8Ty").find { it.selectFirst("span")?.text()?.contains("Estado", ignoreCase = true) == true }?.selectFirst("small")?.text()
        val showStatus = when {
            statusText?.contains("Emisión", ignoreCase = true) == true || statusText?.contains("En emisión", ignoreCase = true) == true -> ShowStatus.Ongoing
            statusText?.contains("Finalizado", ignoreCase = true) == true -> ShowStatus.Completed
            else -> null
        }

        val yearText = document.select("div.page_animeDetailItem__wB8Ty").find { it.selectFirst("span")?.text()?.contains("Estreno", ignoreCase = true) == true }?.selectFirst("small")?.text()
        val year = Regex("(\\d{4})").find(yearText ?: "")?.groupValues?.get(1)?.toIntOrNull()
        val scoreText = document.select("div.page_animeDetailItem__wB8Ty").find { it.selectFirst("span")?.text()?.contains("Avg", ignoreCase = true) == true || it.selectFirst("span")?.text()?.contains("Score", ignoreCase = true) == true }?.selectFirst("small")?.text()
        val score = Regex("([0-9]+\\.[0-9]+)").find(scoreText ?: "")?.groupValues?.get(1)?.toDoubleOrNull()

        val episodes = extractEpisodes(document, url, poster)

        return if (episodes.isNotEmpty()) {
            newAnimeLoadResponse(title, url, TvType.Anime) {
                addEpisodes(DubStatus.Subbed, episodes)
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.showStatus = showStatus
                this.tags = tags
                this.score = score?.let { Score.from10(it) }
            }
        } else {
            newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.plot = description
                this.tags = tags
                this.year = year
            }
        }
    }

    private suspend fun extractEpisodes(document: Document, animeUrl: String, posterUrl: String): List<Episode> {
        val episodes = mutableListOf<Episode>()
        val slug = Regex("/anime/([^/]+)").find(animeUrl)?.groupValues?.get(1)
            ?: return extractEpisodesFromHtml(document, animeUrl, posterUrl)

        val ssrScript = document.selectFirst("script#ssr-init")
        if (ssrScript != null) {
            try {
                val json = ssrScript.data()
                val root = JSONObject(json)
                val b = root.opt("b")
                if (b is JSONArray) {
                    for (i in 0 until b.length()) {
                        val str = b.getString(i)
                        if (str.contains("\"episodes\"")) {
                            val dataObj = JSONObject(str.replace("&quot;", "\""))
                            val episodesArray = dataObj.optJSONArray("episodes")
                            val animeSlug = dataObj.optString("animeSlug", "")
                            if (episodesArray != null && animeSlug.isNotBlank()) {
                                for (j in 0 until episodesArray.length()) {
                                    val ep = episodesArray.getJSONObject(j)
                                    val epNum = ep.optInt("number", 0)
                                    if (epNum > 0) {
                                        val epUrl = "$mainUrl/ver/$animeSlug/$epNum"
                                        val thumb = ep.optString("image", ep.optString("thumbnail", ""))
                                        episodes.add(newEpisode(epUrl) {
                                            this.name = "Episodio $epNum"
                                            this.episode = epNum
                                            this.posterUrl = if (thumb.startsWith("http")) thumb else posterUrl.ifBlank { null }
                                        })
                                    }
                                }
                                if (episodes.isNotEmpty()) {
                                    return episodes.sortedBy { it.episode }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AnimeLatinoHD", "API error: ${e.message}")
            }
        }
        return extractEpisodesFromHtml(document, animeUrl, posterUrl)
    }

    private fun extractEpisodesFromHtml(document: Document, animeUrl: String, posterUrl: String): List<Episode> {
        val episodes = mutableListOf<Episode>()
        val slug = Regex("/anime/([^/]+)").find(animeUrl)?.groupValues?.get(1) ?: return episodes
        document.select("#episodes-grid a, div.episode-grid a, a[data-episode]").forEach { card ->
            val epHref = card.attr("abs:href")
            val epNum = card.attr("data-episode").toIntOrNull() ?: Regex("/ver/[^/]+/(\\d+)").find(epHref)?.groupValues?.get(1)?.toIntOrNull()
            if (epHref.isNotBlank() && epNum != null) {
                episodes.add(newEpisode(epHref) {
                    this.name = "Episodio $epNum"
                    this.episode = epNum
                    this.posterUrl = posterUrl.ifBlank { null }
                })
            }
        }
        return episodes.sortedBy { it.episode }
    }

    private fun extractPoster(document: Document): String {
        val ogImage = document.selectFirst("meta[property='og:image']")?.attr("content")
        if (!ogImage.isNullOrBlank()) return ogImage
        val coverImg = document.selectFirst("div.page_cover___eEeC img, img[src*='themoviedb']")
        if (coverImg != null) {
            val src = coverImg.attr("src")
            if (src.isNotBlank()) return src
        }
        return ""
    }

    @Suppress("DEPRECATION", "DEPRECATION_ERROR")
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val langMap = mapOf("0" to "Subtitulado", "1" to "Latino", "2" to "Castellano")

        try {
            val dataObj = JSONObject(data)
            val players = dataObj.optJSONArray("players")
            if (players != null && players.length() > 0) {
                for (i in 0 until players.length()) {
                    val player = players.getJSONObject(i)
                    val embedUrl = player.optString("embed_url", "")
                    val lang = player.optString("language", "0")
                    if (embedUrl.isNotBlank()) loadExtractor(embedUrl, mainUrl, subtitleCallback, callback)
                }
                return true
            }
        } catch (e: Exception) {}

        val document = app.get(data, interceptor = cloudflareKiller).document

        val ssrScript = document.selectFirst("script#ssr-init")
        if (ssrScript != null) {
            try {
                val json = ssrScript.data()
                val root = JSONObject(json)
                val b = root.opt("b")
                if (b is JSONArray) {
                    for (i in 0 until b.length()) {
                        val str = b.getString(i)
                        if (str.contains("\"videoUrlEncrypted\"") || str.contains("\"server\"")) {
                            parsePlayersJson(str, langMap, subtitleCallback, callback)
                        }
                    }
                }
            } catch (e: Exception) {}
        }

        document.select("div.VidePlayer_options__yxprW select option").forEach { option ->
            val serverName = option.text()
            val iframe = document.selectFirst("div.VidePlayer_iframeContainer__qIOOy iframe")
            if (iframe != null) {
                val src = iframe.attr("src")
                if (src.isNotBlank()) loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
            }
        }

        document.select("button.server-btn").forEach { btn ->
            val serverUrl = btn.attr("data-url")
            if (serverUrl.isNotBlank()) loadExtractor(serverUrl, mainUrl, subtitleCallback, callback)
        }

        document.select("iframe#videoFrame").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.isNotBlank()) loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
        }

        return true
    }

    private suspend fun parsePlayersJson(json: String, langMap: Map<String, String>, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit) {
        try {
            val dataObj = JSONObject(json.replace("&quot;", "\""))
            val playersArray = when {
                dataObj.has("data") -> dataObj.optJSONArray("data")
                dataObj.has("episodes") -> dataObj.optJSONArray("episodes")
                else -> null
            }

            val targetArray = when {
                playersArray != null && playersArray.length() > 0 && playersArray.opt(0) is JSONArray -> playersArray.getJSONArray(0)
                playersArray != null -> playersArray
                else -> {
                    val dataArray = JSONArray("[$json]")
                    if (dataArray.length() > 0 && dataArray.opt(0) is JSONArray) dataArray.getJSONArray(0)
                    else if (dataArray.length() > 0 && dataArray.opt(0) is JSONObject) JSONArray().put(dataArray.getJSONObject(0))
                    else null
                }
            }

            if (targetArray != null) {
                for (i in 0 until targetArray.length()) {
                    val player = targetArray.getJSONObject(i)
                    val videoUrlEncrypted = player.optString("videoUrlEncrypted", "")
                    val lang = player.optString("languaje", "0")
                    val langLabel = langMap[lang] ?: "Subtitulado"
                    val server = player.optJSONObject("server")
                    val serverName = server?.optString("title", "Unknown") ?: "Unknown"

                    if (videoUrlEncrypted.isNotBlank()) {
                        callback(newExtractorLink(
                            source = "[$langLabel] $serverName",
                            name = "[$langLabel] $serverName",
                            url = videoUrlEncrypted,
                        ) {
                            this.referer = mainUrl
                            this.quality = Qualities.Unknown.value
                            this.type = ExtractorLinkType.M3U8
                        })
                    }
                }
            }
        } catch (e: Exception) {}
    }
}
