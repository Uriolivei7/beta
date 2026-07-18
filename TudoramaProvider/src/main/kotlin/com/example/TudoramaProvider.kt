package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
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
    override val supportedTypes = setOf(TvType.AsianDrama, TvType.Movie)

    data class EpisodeResult(
        @JsonProperty("url") val url: String = "",
        @JsonProperty("episode") val episode: Int = 0,
        @JsonProperty("title") val title: String? = null
    )

    data class EpisodesResponse(
        @JsonProperty("success") val success: Boolean = false,
        @JsonProperty("data") val data: EpisodesData? = null
    )

    data class EpisodesData(
        @JsonProperty("results") val results: List<EpisodeResult>? = null,
        @JsonProperty("hasMore") val hasMore: Boolean = false
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "=== getMainPage: page=$page ===")
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

            val moreEpisodes = fetchMoreEpisodes(ajaxUrl, nonce, postId, seasonNum, results, results, order)
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
        results: Int, offset: Int, order: String
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
                newEpisode(epUrl) {
                    this.name = epName
                    this.episode = epNum
                    this.season = season.toIntOrNull() ?: 1
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
        val rows = doc.select("div.downloads table tbody tr")
        Log.d(TAG, "loadLinks: ${rows.size} servidores encontrados")
        val foundLinks = mutableListOf<Pair<String, ExtractorLink>>()

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

        try {
            val resp = app.get(embedUrl, referer = mainUrl)
            val page = resp.text
            Log.d(TAG, "extractFromEmbed: HTTP ${resp.code}, len=${page.length}")

            val m3u8Match = Regex("""https?://[^"'\s<>]+\.(m3u8)[^"'\s<>]*""").find(page)
            if (m3u8Match != null) {
                val videoUrl = m3u8Match.value
                Log.d(TAG, "extractFromEmbed: found m3u8 -> $videoUrl")
                callback(newExtractorLink(serverName, serverName, videoUrl) {
                    this.referer = embedUrl
                    this.quality = 1080
                })
                found = true
                return
            }

            val sourcesMatch = Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*"([^"]+)"\s*\}?\s*\]?""").find(page)
            if (sourcesMatch != null) {
                val videoUrl = sourcesMatch.groupValues[1].replace("\\/", "/")
                Log.d(TAG, "extractFromEmbed: found sources file -> $videoUrl")
                callback(newExtractorLink(serverName, serverName, videoUrl) {
                    this.referer = embedUrl
                    this.quality = 1080
                })
                found = true
                return
            }

            val sourcesMatch2 = Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*'([^']+)'\s*\}?\s*\]?""").find(page)
            if (sourcesMatch2 != null) {
                val videoUrl = sourcesMatch2.groupValues[1].replace("\\/", "/")
                Log.d(TAG, "extractFromEmbed: found sources file (quote) -> $videoUrl")
                callback(newExtractorLink(serverName, serverName, videoUrl) {
                    this.referer = embedUrl
                    this.quality = 1080
                })
                found = true
                return
            }

            val mp4Match = Regex("""https?://[^"'\s<>]+\.(mp4)[^"'\s<>]*""").find(page)
            if (mp4Match != null) {
                val videoUrl = mp4Match.value
                Log.d(TAG, "extractFromEmbed: found mp4 -> $videoUrl")
                callback(newExtractorLink(serverName, serverName, videoUrl) {
                    this.referer = embedUrl
                    this.quality = 1080
                })
                found = true
                return
            }
        } catch (e: Exception) {
            Log.e(TAG, "extractFromEmbed: manual fetch error: ${e.message}")
        }

        val subUrl = embedUrl
            .replace("bysesukior.com", "filemoon.sx")
            .replace("minochinos.com", "filemoon.sx")
            .replace("tudorama.4meplayer.pro", "filemoon.sx")
        if (subUrl != embedUrl) {
            Log.d(TAG, "extractFromEmbed: trying loadExtractor with substituted URL: ${subUrl.take(60)}")
            loadExtractor(
                url = subUrl,
                referer = mainUrl,
                subtitleCallback = subtitleCallback,
                callback = { link ->
                    found = true
                    Log.d(TAG, "extractFromEmbed: loadExtractor(domSub) OK for $serverName")
                    callback(link)
                }
            )
            if (found) return
        }

        Log.d(TAG, "extractFromEmbed: trying loadExtractor with original URL")
        loadExtractor(
            url = embedUrl,
            referer = mainUrl,
            subtitleCallback = subtitleCallback,
            callback = { link ->
                found = true
                Log.d(TAG, "extractFromEmbed: loadExtractor(orig) OK for $serverName")
                callback(link)
            }
        )

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

    companion object {
        private const val TAG = "Tudorama"
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