package com.example

import android.util.Log
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

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "=== getMainPage: page=$page ===")
        val doc = app.get(mainUrl).document
        val homeList = mutableListOf<HomePageList>()

        val recentEpisodes = doc.select("ul.eps-list > li.lep").mapNotNull { it.toSearchResult() }
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

        val title = doc.selectFirst(".hero__header h2")?.text()
            ?: doc.selectFirst("h2")?.text()
        Log.d(TAG, "load: title=$title isMovie=$isMovie")
        if (title == null) { Log.w(TAG, "load: title not found"); return null }

        val poster = doc.selectFirst(".hero__poster img")?.attr("src")?.let { fixUrl(it) }
        val backdrop = doc.selectFirst(".hero__backdrop img")?.attr("src")?.let { fixUrl(it) }
        val year = doc.selectFirst(".details__row:contains(Año) .details__col:last-child")?.text()?.trim()?.toIntOrNull()
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
        Log.d(TAG, "load: episodes=${episodeList.size}")

        return newTvSeriesLoadResponse(title, url, TvType.AsianDrama, episodeList) {
            this.posterUrl = poster
            this.backgroundPosterUrl = backdrop
            this.plot = description
            this.tags = tags
            this.year = year
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

            loadExtractor(
                url = embedUrl,
                referer = mainUrl,
                subtitleCallback = subtitleCallback,
                callback = { link ->
                    foundLinks.add(serverName to link)
                    Log.d(TAG, "loadLinks: extractor OK para $serverName -> ${link.url.take(60)}")
                }
            )
        }

        Log.d(TAG, "loadLinks: ${foundLinks.size} total links extraídos")
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
