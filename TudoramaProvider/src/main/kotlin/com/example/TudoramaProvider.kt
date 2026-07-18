package com.example

import android.net.Uri
import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.jsoup.nodes.Element

class TudoramaProvider : MainAPI() {
    override var mainUrl = "https://tudorama.com"
    override var name = "TuDorama"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.AsianDrama)

    override val mainPage = mainPageOf(
        "/" to "Episodios Recientes",
        "/genero/emision/" to "En Emisión",
        "/genero/series/" to "K-Dramas",
        "/genero/cdrama/" to "C-Dramas",
        "/genero/jdrama/" to "J-Dramas",
        "/genero/espanol-latino/" to "Español Latino",
        "/genero/sub-espanol/" to "Sub Español",
        "/genero/peliculas/" to "Películas",
    )

    data class EpisodeResult(
        @JsonProperty("permalink") val url: String = "",
        @JsonProperty("episode_number") val episode: Int = 0,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("episode_image") val image: String? = null
    )

    data class EpisodesResponse(
        @JsonProperty("success") val success: Boolean = false,
        @JsonProperty("data") val data: EpisodesData? = null
    )

    data class EpisodesData(
        @JsonProperty("results") val results: List<EpisodeResult>? = null,
        @JsonProperty("hasMore") val hasMore: Boolean = false
    )

    data class ServerResult(
        @JsonProperty("title") val title: String = "",
        @JsonProperty("type") val type: String = "",
        @JsonProperty("url") val url: String = "",
        @JsonProperty("status") val status: String = ""
    )

    data class ServersResponse(
        @JsonProperty("success") val success: Boolean = false,
        @JsonProperty("data") val data: List<ServerResult>? = null
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "=== getMainPage: name=${request.name}, page=$page ===")

        if (request.data == "/") {
            val doc = app.get(mainUrl).document
            val homeList = mutableListOf<HomePageList>()

            val recentEpisodes = doc.select("section.section--episode article.ieps").mapNotNull { it.toSearchResult() }
            Log.d(TAG, "getMainPage: recentEpisodes=${recentEpisodes.size}")
            if (recentEpisodes.isNotEmpty())
                homeList.add(HomePageList("Episodios Recientes", recentEpisodes))

            val trending = doc.select("div.items > article.ipst").mapNotNull { it.toSearchResult() }
            Log.d(TAG, "getMainPage: trending=${trending.size}")
            if (trending.isNotEmpty())
                homeList.add(HomePageList("Tendencias", trending))

            Log.d(TAG, "getMainPage: total lists=${homeList.size}")
            if (homeList.isEmpty()) return null
            return newHomePageResponse(homeList, hasNext = false)
        }

        val url = if (page <= 1) {
            "${mainUrl}${request.data}"
        } else {
            "${mainUrl}${request.data.removeSuffix("/")}/page/$page/"
        }
        Log.d(TAG, "getMainPage: fetching url=$url")
        val doc = app.get(url).document
        val items = doc.select("div.items > article.ipst").mapNotNull { it.toSearchResult() }
        Log.d(TAG, "getMainPage: items=${items.size}")
        if (items.isEmpty()) return null
        return newHomePageResponse(
            list = HomePageList(request.name, items),
            hasNext = items.size >= 20
        )
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        Log.d(TAG, "=== search: query=$query ===")
        val doc = app.get("$mainUrl/?s=$query").document
        val results = doc.select("article.ipst").mapNotNull { it.toSearchResult() }
        Log.d(TAG, "search: results=${results.size}")
        return results.ifEmpty { null }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "=== load: url=${url.take(80)} ===")
        val doc = app.get(url).document
        val isMovie = url.contains("/pelicula/")

        val title = if (isMovie) {
            doc.selectFirst("h1.hero__title")?.text()
        } else {
            doc.selectFirst("section#hero .hero--serie .hero__header h2")?.text()
        }
        Log.d(TAG, "load: title=$title isMovie=$isMovie")
        if (title == null) { Log.w(TAG, "load: title not found"); return null }

        val poster = doc.selectFirst(".hero__poster img")?.attr("src")?.let { fixUrl(it) }
        val backdrop = doc.selectFirst(".hero__backdrop img")?.attr("src")?.let { fixUrl(it) }
        val year = doc.selectFirst(".details__row:contains(A\u00f1o) .details__col:last-child")?.text()?.trim()?.toIntOrNull()
        val description = doc.selectFirst(".hero__overview, .synopsis p")?.text()?.trim()
        val tags = doc.select(".hero__genres a").mapNotNull { it.text().trim().takeIf { t -> t.isNotEmpty() } }

        if (isMovie) {
            Log.d(TAG, "load: returning MovieLoadResponse")
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = description
                this.tags = tags
                this.year = year
            }
        }

        val episodeList = doc.select("ul.eps-list > li.lep").mapNotNull { ep ->
            val epLink = ep.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val epNum = ep.attr("data-episode").toIntOrNull() ?: return@mapNotNull null
            val seasonNum = ep.attr("data-season").toIntOrNull() ?: 1
            val epName = ep.selectFirst(".lep__title")?.text()?.trim() ?: "Episodio $epNum"
            newEpisode(epLink) {
                this.name = epName
                this.episode = epNum
                this.season = seasonNum
                this.posterUrl = poster
            }
        }
        Log.d(TAG, "load: episodes from DOM=${episodeList.size}")

        val hasLoadMore = doc.selectFirst("#load-eps") != null
        val allEpisodes = if (hasLoadMore) {
            Log.d(TAG, "load: load-more button found, fetching more episodes via AJAX")
            val epsContainer = doc.selectFirst("div.eps")
            val nonce = epsContainer?.attr("data-nonce") ?: ""
            val postId = epsContainer?.attr("data-tmdb-id") ?: ""
            val seasonNum = epsContainer?.attr("data-season-number") ?: "1"
            val results = epsContainer?.attr("data-results")?.toIntOrNull() ?: episodeList.size
            val order = epsContainer?.attr("data-order") ?: "DESC"
            val ajaxUrl = epsContainer?.attr("data-ajaxurl") ?: ""

            val moreEpisodes = fetchMoreEpisodes(ajaxUrl, nonce, postId, seasonNum, results, results, order, poster)
            Log.d(TAG, "load: more episodes from AJAX=${moreEpisodes.size}")
            val seenEps = episodeList.mapNotNull { it.episode }.toSet()
            val all = episodeList.toMutableList()
            all.addAll(moreEpisodes.filter { it.episode != null && it.episode !in seenEps })
            all.sortedWith(compareBy({ it.season ?: 1 }, { it.episode ?: 0 }))
        } else {
            episodeList
        }
        Log.d(TAG, "load: total episodes=${allEpisodes.size}")

        return newTvSeriesLoadResponse(title, url, TvType.AsianDrama, allEpisodes) {
            this.posterUrl = poster
            this.backgroundPosterUrl = backdrop
            this.plot = description
            this.tags = tags
            this.year = year
        }
    }

    private suspend fun fetchMoreEpisodes(
        ajaxUrl: String, nonce: String, postId: String, season: String,
        results: Int, offset: Int, order: String,
        posterUrl: String? = null
    ): List<Episode> {
        if (ajaxUrl.isBlank() || nonce.isBlank() || postId.isBlank()) return emptyList()
        return try {
            val resp = app.post(
                url = "${ajaxUrl}admin-ajax.php",
                referer = mainUrl,
                data = mapOf(
                    "action" to "corvus_get_episodes",
                    "nonce" to nonce,
                    "post_id" to postId,
                    "season" to season,
                    "results" to results.toString(),
                    "offset" to offset.toString(),
                    "order" to order
                )
            )
            val parsed = resp.parsedSafe<EpisodesResponse>()
            if (parsed == null || !parsed.success) return emptyList()
            parsed.data?.results?.mapNotNull { item ->
                val epUrl = item.url
                if (epUrl.isBlank()) return@mapNotNull null
                val epNum = item.episode
                val epName = item.title ?: "Episodio $epNum"
                val epImage = item.image?.let { fixUrl(it) }
                newEpisode(epUrl) {
                    this.name = epName
                    this.episode = epNum
                    this.season = season.toIntOrNull() ?: 1
                    this.posterUrl = epImage ?: posterUrl
                }
            } ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "fetchMoreEpisodes error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "=== loadLinks: data=${data.take(80)} ===")
        val doc = app.get(data).document
        val foundLinks = mutableListOf<Pair<String, ExtractorLink>>()

        // Parse nonce and ajaxUrl for AJAX fallback
        val epDropdown = doc.selectFirst(".ep__dropdown, .servers")
        val nonce = epDropdown?.attr("data-nonce") ?: ""
        val postId = epDropdown?.attr("data-id") ?: ""
        val epsContainer = doc.selectFirst("div.eps")
        val ajaxUrl = epsContainer?.attr("data-ajaxurl") ?: "$mainUrl/"

        // Try download table first
        val rows = doc.select("div.downloads table tbody tr")
        Log.d(TAG, "loadLinks: ${rows.size} servidores (download)")
        for (row in rows) {
            val serverName = row.selectFirst("td:first-child")?.text()?.trim() ?: continue
            val downloadUrl = row.selectFirst("a[href]")?.attr("href") ?: continue
            Log.d(TAG, "loadLinks: server=$serverName url=$downloadUrl")
            val embedUrl = resolveServerUrl(downloadUrl)
            if (embedUrl == null) {
                Log.w(TAG, "loadLinks: no se pudo resolver embed URL para $serverName")
                continue
            }
            Log.d(TAG, "loadLinks: embedUrl=${embedUrl.take(80)}")
            extractFromEmbed(embedUrl, serverName, subtitleCallback) { link ->
                foundLinks.add(serverName to link)
            }
        }

        // Fallback: AJAX stream servers
        if (foundLinks.isEmpty() && nonce.isNotBlank() && postId.isNotBlank()) {
            Log.d(TAG, "loadLinks: trying AJAX stream servers (nonce=$nonce postId=$postId)")
            fetchStreamServers(ajaxUrl, nonce, postId, subtitleCallback) { name, link ->
                foundLinks.add(name to link)
            }
        }

        Log.d(TAG, "loadLinks: ${foundLinks.size} total links extra\u00eddos")
        foundLinks.forEach { (serverName, link) ->
            callback(newExtractorLink(link.source, "$serverName - ${link.name}", link.url) {
                this.referer = link.referer
                this.quality = link.quality
                this.headers = link.headers + mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                    "Accept" to "*/*"
                )
            })
        }
        Log.d(TAG, "=== loadLinks FIN: ${foundLinks.isNotEmpty()} ===")
        return foundLinks.isNotEmpty()
    }

    private suspend fun extractFromEmbed(
        embedUrl: String,
        serverName: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d(TAG, "extractFromEmbed: server=$serverName url=$embedUrl")
        var found = false

        val candidates = buildList {
            // Primary: /e/ path (embed player), but preserve /f/ for hgcloud.to
            val ePath = embedUrl
                .replace("/d/", "/e/")
                .replace("/download/", "/e/")
            add(ePath)
            // Fallback: original URL
            if (last() != embedUrl) add(embedUrl)
        }.distinct()

        for (candidate in candidates) {
            if (found) break
            Log.d(TAG, "extractFromEmbed: trying candidate=$candidate")
            loadExtractor(
                url = candidate,
                referer = mainUrl,
                subtitleCallback = subtitleCallback,
                callback = { link ->
                    found = true
                    Log.d(TAG, "extractFromEmbed: loadExtractor OK for $serverName -> ${link.url.take(60)}")
                    callback(link)
                }
            )
        }

        // Fallback: manual HTTP extraction (VidStack not available)
        if (!found) {
            Log.d(TAG, "extractFromEmbed: trying manual HTTP extraction for $serverName")
            for (candidate in candidates) {
                if (found) break
                try {
                    val page = app.get(candidate, referer = mainUrl).text
                    // Pattern 1: direct m3u8 URL
                    val m3u8Regex = Regex("""https?://[^"'<>]+\.m3u8[^"'<>]*""")
                    val m3u8Match = m3u8Regex.find(page)
                    if (m3u8Match != null) {
                        val videoUrl = m3u8Match.value
                        Log.d(TAG, "extractFromEmbed: manual m3u8 found: ${videoUrl.take(80)}")
                        callback(newExtractorLink(serverName, "$serverName - $videoUrl", videoUrl) {
                            this.referer = candidate
                        })
                        found = true
                        break
                    }
                    // Pattern 2: direct mp4 URL
                    val mp4Regex = Regex("""https?://[^"'<>]+\.mp4[^"'<>]*""")
                    val mp4Match = mp4Regex.find(page)
                    if (mp4Match != null) {
                        val videoUrl = mp4Match.value
                        Log.d(TAG, "extractFromEmbed: manual mp4 found: ${videoUrl.take(80)}")
                        callback(newExtractorLink(serverName, "$serverName - $videoUrl", videoUrl) {
                            this.referer = candidate
                        })
                        found = true
                        break
                    }
                    // Pattern 3: file: "url" or src: "url" in JS
                    val jsUrlRegex = Regex("""(?:file|src)\s*[:=]\s*"([^"]+\.(?:m3u8|mp4)[^"]*)""")
                    val jsMatch = jsUrlRegex.find(page)
                    if (jsMatch != null) {
                        val videoUrl = jsMatch.groupValues[1]
                        Log.d(TAG, "extractFromEmbed: manual JS url found: ${videoUrl.take(80)}")
                        callback(newExtractorLink(serverName, "$serverName - $videoUrl", videoUrl) {
                            this.referer = candidate
                        })
                        found = true
                        break
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "extractFromEmbed: manual extraction error: ${e.message}")
                }
            }
        }

        // Fallback: try direct API calls for known hosts
        if (!found) {
            for (candidate in candidates) {
                if (found) break
                Log.d(TAG, "extractFromEmbed: trying API extraction for $candidate")
                found = tryApiExtraction(candidate, serverName, subtitleCallback, callback)
            }
        }

        if (!found) {
            Log.w(TAG, "extractFromEmbed: no links found for $serverName ($embedUrl)")
        }
    }

    private suspend fun resolveServerUrl(downloadUrl: String): String? {
        return try {
            Log.d(TAG, "resolveServerUrl: fetching $downloadUrl")
            val doc = app.get(downloadUrl, referer = mainUrl).document
            val href = doc.selectFirst("a.download-button")?.attr("href")
            if (href == null) { Log.w(TAG, "resolveServerUrl: no download-button found"); return null }
            val sParam = href.substringAfter("?s=", "")
            val result = sParam.substringBefore("&").ifEmpty { null }
            Log.d(TAG, "resolveServerUrl: resolved=$result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "resolveServerUrl error: ${e.message}")
            null
        }
    }

    private suspend fun fetchStreamServers(
        ajaxUrl: String,
        nonce: String,
        postId: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (name: String, link: ExtractorLink) -> Unit
    ) {
        try {
            val resp = app.post(
                url = "${ajaxUrl}admin-ajax.php",
                referer = mainUrl,
                data = mapOf(
                    "action" to "corvus_get_servers",
                    "nonce" to nonce,
                    "post_id" to postId
                )
            )
            // Response is doubly-wrapped: ["{\"success\":true,\"data\":[...]}"]
            val raw = resp.text
            Log.d(TAG, "fetchStreamServers: raw=${raw.take(200)}")
            val outer = try {
                mapper.readValue<List<String>>(raw)
            } catch (e: Exception) {
                Log.w(TAG, "fetchStreamServers: outer parse failed: ${e.message}")
                return
            }
            if (outer.isEmpty()) { Log.w(TAG, "fetchStreamServers: empty outer array"); return }
            val innerJson = outer[0]
            val parsed = mapper.readValue<ServersResponse>(innerJson)
            if (parsed == null || !parsed.success || parsed.data == null) {
                Log.w(TAG, "fetchStreamServers: invalid inner response")
                return
            }
            for (server in parsed.data) {
                if (server.status != "ok" || server.type != "stream") continue
                val streamUrl = server.url
                Log.d(TAG, "fetchStreamServers: server=${server.title} url=$streamUrl")
                val iframeUrl = resolveStreamUrl(streamUrl) ?: continue
                Log.d(TAG, "fetchStreamServers: iframe=$iframeUrl")
                extractFromEmbed(iframeUrl, server.title, subtitleCallback) { link ->
                    callback(server.title, link)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "fetchStreamServers error: ${e.message}")
        }
    }

    private suspend fun resolveStreamUrl(streamUrl: String): String? {
        return try {
            val doc = app.get(streamUrl, referer = mainUrl).document
            val iframeSrc = doc.selectFirst("#myIframe")?.attr("src")
            if (iframeSrc.isNullOrBlank()) null else iframeSrc
        } catch (e: Exception) {
            Log.e(TAG, "resolveStreamUrl error: ${e.message}")
            null
        }
    }

    private suspend fun tryApiExtraction(
        embedUrl: String,
        serverName: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val host = try { Uri.parse(embedUrl).host } catch (e: Exception) { return false }
        val path = try { Uri.parse(embedUrl).path ?: "" } catch (e: Exception) { "" }
        val code = path.split("/").lastOrNull { it.length in 3..20 } ?: return false

        Log.d(TAG, "tryApiExtraction: host=$host code=$code")

        return when {
            host?.contains("hgcloud.to") == true -> tryHgcloudApi(embedUrl, code, serverName, callback)
            host?.contains("bysesukior.com") == true -> tryBysesukiorApi(embedUrl, code, serverName, callback)
            host?.contains("4meplayer.pro") == true -> try4meplayerApi(embedUrl, code, serverName, callback)
            else -> false
        }
    }

    private suspend fun tryHgcloudApi(
        embedUrl: String, code: String, serverName: String,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            val base = embedUrl.substringBefore("/f/").substringBefore("/e/").substringBefore("/d/")
            // Pattern 1: POST /api/source/{code}
            val resp1 = app.post(
                url = "$base/api/source/$code",
                referer = base,
                data = mapOf("r" to "", "d" to base)
            )
            val text1 = resp1.text
            Log.d(TAG, "tryHgcloudApi: resp1=${text1.take(200)}")
            val parsed1 = try { mapper.readValue<Map<String, Any>>(text1) } catch (e: Exception) { null }
            if (parsed1?.get("success") == true) {
                val data = parsed1["data"]
                if (data is List<*>) {
                    for (item in data) {
                        if (item is Map<*, *>) {
                            val file = item["file"]?.toString() ?: continue
                            val label = item["label"]?.toString() ?: "auto"
                            callback(newExtractorLink(serverName, "$serverName - $label", file) {
                                this.referer = base
                            })
                            return true
                        }
                    }
                }
            }
            // Pattern 2: POST /ajax.php
            val resp2 = app.post(
                url = "$base/ajax.php",
                referer = base,
                data = mapOf("a" to "getSources", "id" to code)
            )
            val text2 = resp2.text
            Log.d(TAG, "tryHgcloudApi: resp2=${text2.take(200)}")
            val parsed2 = try { mapper.readValue<Map<String, Any>>(text2) } catch (e: Exception) { null }
            if (parsed2 != null) {
                val sources = parsed2["sources"] ?: parsed2["data"]
                if (sources is List<*>) {
                    for (item in sources) {
                        if (item is Map<*, *>) {
                            val file = item["file"]?.toString() ?: continue
                            callback(newExtractorLink(serverName, serverName, file) {
                                this.referer = base
                            })
                            return true
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "tryHgcloudApi error: ${e.message}")
        }
        return false
    }

    private suspend fun tryBysesukiorApi(
        embedUrl: String, code: String, serverName: String,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            val base = embedUrl.substringBefore("/e/").substringBefore("/d/")
            // Try source API
            val resp = app.post(
                url = "$base/api/source",
                referer = embedUrl,
                data = mapOf("code" to code)
            )
            val text = resp.text
            Log.d(TAG, "tryBysesukiorApi: resp=${text.take(200)}")
            val parsed = try { mapper.readValue<Map<String, Any>>(text) } catch (e: Exception) { null }
            if (parsed?.get("success") == true) {
                val data = parsed["data"]
                if (data is List<*>) {
                    for (item in data) {
                        if (item is Map<*, *>) {
                            val file = item["file"]?.toString() ?: continue
                            val label = item["label"]?.toString() ?: "auto"
                            callback(newExtractorLink(serverName, "$serverName - $label", file) {
                                this.referer = embedUrl
                            })
                            return true
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "tryBysesukiorApi error: ${e.message}")
        }
        return false
    }

    private suspend fun try4meplayerApi(
        embedUrl: String, code: String, serverName: String,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            val base = embedUrl.substringBefore("#")
            val resp = app.post(
                url = "$base/master.php",
                referer = base,
                data = mapOf("id" to code, "type" to "direct")
            )
            val text = resp.text
            Log.d(TAG, "try4meplayerApi: resp=${text.take(200)}")
            // Try to find m3u8/mp4 in response
            val videoUrl = Regex("""https?://[^"'<>]+\.(?:m3u8|mp4)[^"'<>]*""").find(text)?.value
            if (videoUrl != null) {
                callback(newExtractorLink(serverName, serverName, videoUrl) {
                    this.referer = base
                })
                return true
            }
            // Try JSON parse
            val parsed = try { mapper.readValue<Map<String, Any>>(text) } catch (e: Exception) { null }
            if (parsed != null) {
                val file = parsed["file"]?.toString() ?: parsed["url"]?.toString() ?: parsed["src"]?.toString()
                if (file != null) {
                    callback(newExtractorLink(serverName, serverName, file) {
                        this.referer = base
                    })
                    return true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "try4meplayerApi error: ${e.message}")
        }
        return false
    }

    companion object {
        private const val TAG = "Tudorama"
        private val mapper = ObjectMapper()
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val link = selectFirst("a")?.attr("href") ?: return null
        val title = selectFirst(".ipst__title a, .lep__title, h3 a, .ipst__body h3 a")?.text()
            ?: selectFirst("h3")?.text()?.trim()
            ?: return null
        val poster = selectFirst("img")?.attr("src")?.let { fixUrl(it) }
        val type = when {
            link.contains("/pelicula/") -> TvType.Movie
            else -> TvType.AsianDrama
        }
        return newMovieSearchResponse(title, link, type) {
            this.posterUrl = poster
        }
    }
}