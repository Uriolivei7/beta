package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URLEncoder

class TokyvideoProvider : MainAPI() {
    override var mainUrl = "https://www.tokyvideo.com"
    override var name = "Tokyvideo"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Cartoon)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val TAG = "Tokyvideo"
    private val desktopHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9",
    )

    override val mainPage = mainPageOf(
        "/series/trends" to "Tendencia",
        "/cartoon-series" to "Dibujos",
        "/television-series" to "Series TV",
        "/movies" to "Películas",
        "/es/series" to "Series",
    )

    private suspend fun safeGet(url: String): String? {
        return try {
            val res = app.get(url, headers = desktopHeaders, timeout = 15L)
            if (res.isSuccessful) res.text else null
        } catch (e: Exception) {
            Log.e(TAG, "safeGet $url: ${e.message}")
            null
        }
    }

    private fun Element.toVideoCard(): SearchResponse? {
        val link = selectFirst("a.thumb-duracion[href]") ?: return null
        val href = link.attr("abs:href")
        if (href.isBlank() || !href.contains("/video/")) return null
        val title = selectFirst(".thumb-video__meta h3 a")?.text()?.trim()?.takeIf { it.isNotBlank() }
            ?: link.selectFirst("img")?.attr("alt")?.trim()?.takeIf { it.isNotBlank() }
            ?: return null
        val img = selectFirst("img")
        val poster = img?.attr("data-src")?.trim()?.takeIf { it.isNotBlank() && !it.startsWith("data:") }
            ?: img?.attr("src")?.trim()?.takeIf { it.isNotBlank() && !it.startsWith("data:") }
            ?.let { fixUrl(it) }
        return newMovieSearchResponse(title, href, TvType.Movie) { this.posterUrl = poster }
    }

    private fun parseVideoCards(doc: org.jsoup.nodes.Document): List<SearchResponse> {
        return doc.select("div.thumb-video").mapNotNull { it.toVideoCard() }.distinctBy { it.url }
    }

    private fun parseSerieCards(doc: org.jsoup.nodes.Document): List<SearchResponse> {

        val direct = doc.select("div.thumb-serie").mapNotNull { card ->
            val a = card.selectFirst("a[href*=/serie]") ?: return@mapNotNull null
            val href = a.attr("abs:href")
            if (href.isBlank()) return@mapNotNull null
            val title = card.selectFirst(".thumb-serie__name")?.text()?.trim()?.takeIf { it.isNotBlank() }
                ?: a.selectFirst("img")?.attr("alt")?.trim()?.takeIf { it.isNotBlank() }
                ?: return@mapNotNull null
            val poster = card.selectFirst("img")?.let { img ->
                img.attr("data-src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
                    ?: img.attr("src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
            }?.let { fixUrl(it) }
            newTvSeriesSearchResponse(title, href, TvType.TvSeries) { this.posterUrl = poster }
        }.distinctBy { it.url }
        if (direct.isNotEmpty()) return direct

        Log.d(TAG, "parseSerieCards: sin div.thumb-serie, genérico (html=${doc.html().length})")
        return doc.select("a[href*=/serie]").mapNotNull { a ->
            val href = a.attr("abs:href")
            val clean = href.removeSuffix("/")
            if (href.isBlank() || clean == "$mainUrl/series" || clean == "$mainUrl/es/series") return@mapNotNull null
            val title = a.selectFirst(".thumb-serie__name")?.text()?.trim()?.takeIf { it.isNotBlank() }
                ?: a.selectFirst("img")?.attr("alt")?.trim()?.takeIf { it.isNotBlank() }
                ?: a.text().trim().takeIf { it.isNotBlank() && it.length < 80 }
                ?: a.attr("title")?.trim()?.takeIf { it.isNotBlank() }
                ?: href.substringAfterLast("/").trim('/').replace("-", " ").takeIf { it.isNotBlank() }
                ?: return@mapNotNull null
            val poster = a.selectFirst("img")?.let { img ->
                img.attr("data-src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
                    ?: img.attr("src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
            }?.let { fixUrl(it) }
            newTvSeriesSearchResponse(title, href, TvType.TvSeries) { this.posterUrl = poster }
        }.distinctBy { it.url }
    }

    private fun fold(s: String): String =
        java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
            .lowercase()

    private fun pageUrl(base: String, page: Int): String {
        if (page <= 1) return base

        return if (base.endsWith("/series")) "$base/$page"
        else if (base.contains("?")) "$base&page=$page" else "$base?page=$page"
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val base = "$mainUrl${request.data}"
        val html = safeGet(pageUrl(base, page)) ?: return null
        val doc = Jsoup.parse(html, base)

        val items = if (request.data == "/series") parseSerieCards(doc) else parseVideoCards(doc)
        if (items.isEmpty()) {
            Log.w(TAG, "getMainPage '${request.name}' p$page: 0 items")
            return null
        }
        val hasNext = doc.select(".pagination a, a[rel=next], a[href*=page]").isNotEmpty()
        Log.d(TAG, "getMainPage '${request.name}' p$page: ${items.size}")
        return newHomePageResponse(HomePageList(request.name, items), hasNext)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val toks = fold(query).split(Regex("\\s+")).filter { it.length >= 3 }.distinct()
        val queries = (listOf(query) + toks).distinct().take(5)
        val videos = linkedMapOf<String, Pair<SearchResponse, Int>>()
        val apiSeries = linkedMapOf<String, Pair<SearchResponse, Int>>()
        for (q in queries) {
            val url = "https://api.tokyvideo.com/search-ajax?lang=es_ES&q=${URLEncoder.encode(q, "UTF-8")}&wr="
            val text = safeGet(url) ?: continue
            try {
                val groups = JSONObject(text).optJSONArray("results") ?: continue
                for (gi in 0 until groups.length()) {
                    val children = groups.optJSONObject(gi)?.optJSONArray("children") ?: continue
                    for (ci in 0 until children.length()) {
                        val c = children.optJSONObject(ci) ?: continue
                        if (c.optString("type") == "serie") {
                            val name = c.optString("name").trim()
                            val id = c.optString("id").trim()
                            if (name.isBlank() || id.isBlank()) continue
                            val s = if (toks.isEmpty()) 1 else toks.count { fold(name).contains(it) }
                            val prev = apiSeries[id]
                            if (s > 0 && (prev == null || s > prev.second)) {
                                val thumb = c.optString("thumb").takeIf { it.isNotBlank() }
                                apiSeries[id] = newTvSeriesSearchResponse(name, id, TvType.TvSeries) { this.posterUrl = thumb } to s
                            }
                        } else {
                            val title = c.optString("title").trim()
                            val id = c.optString("id").trim()
                            if (title.isBlank() || id.isBlank()) continue
                            val s = if (toks.isEmpty()) 1 else toks.count { fold(title).contains(it) }
                            val prev = videos[id]
                            if (s > 0 && (prev == null || s > prev.second)) {
                                val thumb = c.optString("thumb").takeIf { it.isNotBlank() }
                                videos[id] = newMovieSearchResponse(title, id, TvType.Movie) { this.posterUrl = thumb } to s
                            }
                        }
                    }
                }
            } catch (_: Exception) { }
        }
        val rankedVideos = videos.values.sortedByDescending { it.second }.map { it.first }
        val bestVideo = videos.values.maxOfOrNull { it.second } ?: 0
        val rankedApiSeries = apiSeries.values.sortedByDescending { it.second }.map { it.first }
        Log.d(TAG, "search '$query' api -> ${rankedApiSeries.size} series + ${rankedVideos.size} videos (best=$bestVideo)")

        val series = linkedMapOf<String, SearchResponse>()
        rankedApiSeries.forEach { series[it.url] = it }
        if ((toks.size >= 2 && bestVideo < 2) || (toks.size >= 3 && bestVideo <= 2)) {
            val top = rankedVideos.take(4)
            for (card in top) {
                try {
                    val vhtml = safeGet(fixUrl(card.url)) ?: continue
                    val slug = Regex("""global_tokyvideo_serie_name\s*=\s*'([^']+)'""").find(vhtml)?.groupValues?.get(1)?.trim()
                        ?: Regex("""serieVideo['"]\s*:\s*["']([^"']+)""").find(vhtml)?.groupValues?.get(1)?.trim()
                        ?: continue
                    if (slug.isBlank()) continue
                    val surl = "$mainUrl/es/serie/$slug"
                    if (!series.containsKey(surl)) {
                        val stitle = slug.split("-").joinToString(" ") { w -> w.replaceFirstChar { c -> c.uppercase() } }
                        Log.d(TAG, "search '$query' serie: $stitle ($surl)")
                        series[surl] = newTvSeriesSearchResponse(stitle, surl, TvType.TvSeries) { }
                    }
                } catch (_: Exception) { }
            }
        }
        return (series.values.toList() + rankedVideos).take(40)
    }

    private fun cleanTitle(t: String): String {
        return t.trim().substringBefore(" - Tokyvideo").substringBefore(" | ").trim()
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = safeGet(url) ?: return null
        val doc = Jsoup.parse(html, url)

        if (url.contains("/serie/") || url.contains("/series/")) {
            val title = cleanTitle(
                doc.selectFirst(".series-detail--content--title")?.text()?.trim()?.takeIf { it.isNotBlank() }
                    ?: doc.selectFirst("h2.series-detail--content--title")?.text()?.trim()
                    ?: doc.selectFirst("title")?.text()?.substringBefore(" - ")?.trim()
                    ?: "Serie"
            )
            val poster = doc.selectFirst(".series-detail--img img")?.let { img ->
                img.attr("data-src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
                    ?: img.attr("src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
            }?.let { fixUrl(it) }
                ?: doc.selectFirst("meta[property='og:image']")?.attr("content")?.let { fixUrl(it) }
            val plot = doc.selectFirst(".series-detail--content--desc")?.text()?.take(500)

            val serieBase = url.trimEnd('/').replace(Regex("/\\d+$"), "")
            val seenHrefs = linkedSetOf<String>()
            val cards = mutableListOf<org.jsoup.nodes.Element>()
            var p = 1
            while (p <= 10) {
                val pdoc = if (p == 1) doc else {
                    val phtml = safeGet(if (p <= 1) serieBase else "$serieBase/$p") ?: break
                    Jsoup.parse(phtml, serieBase)
                }
                var fresh = 0
                for (card in pdoc.select("div.thumb-video")) {
                    val href = card.selectFirst("a.thumb-duracion[href]")?.attr("abs:href")
                    if (href.isNullOrBlank() || !seenHrefs.add(href)) continue
                    cards.add(card)
                    fresh++
                }
                Log.d(TAG, "load serie p$p: +$fresh eps")
                if (fresh == 0) break
                p++
            }
            val episodes = cards.mapIndexedNotNull { idx, card ->
                val a = card.selectFirst("a.thumb-duracion[href]") ?: return@mapIndexedNotNull null
                val href = a.attr("abs:href")
                if (href.isBlank()) return@mapIndexedNotNull null
                val t = card.selectFirst(".thumb-video__meta h3 a")?.text()?.trim()
                    ?.takeIf { it.isNotBlank() } ?: "Episodio ${idx + 1}"
                val epPoster = card.selectFirst("img")?.let { img ->
                    img.attr("data-src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
                        ?: img.attr("src").trim().takeIf { it.isNotBlank() && !it.startsWith("data:") }
                }?.let { fixUrl(it) }

                val m = Regex("""^(\d+)\s*[xX]\s*(\d+)""").find(t)
                val season = m?.groupValues?.get(1)?.toIntOrNull()
                val epNum = m?.groupValues?.get(2)?.toIntOrNull() ?: (idx + 1)
                newEpisode(href) {
                    this.name = t
                    this.season = season
                    this.episode = epNum
                    this.posterUrl = epPoster
                }
            }.distinctBy { it.data }
            Log.d(TAG, "load serie: $title (${episodes.size} eps) -> $url")

            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = plot
            }
        }

        val title = cleanTitle(
            doc.selectFirst("h1")?.text()?.trim()?.takeIf { it.isNotBlank() }
                ?: doc.selectFirst("meta[property='og:title']")?.attr("content")?.trim()
                ?: "Video"
        )
        val poster = doc.selectFirst("meta[property='og:image']")?.attr("content")?.let { fixUrl(it) }
        Log.d(TAG, "load video: $title -> $url")
        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
        }
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val targetUrl = fixUrl(data)
        Log.d(TAG, "loadLinks: $targetUrl")
        val html = safeGet(targetUrl) ?: return false

        val src = Jsoup.parse(html).let { doc ->
            doc.selectFirst("video#tokyvideo_player source[src]")?.attr("src")
                ?.takeIf { it.isNotBlank() }
                ?: doc.selectFirst("video source[src]")?.attr("src")?.takeIf { it.isNotBlank() }
                ?: Regex("""https?://[^"'\s<>]+\.mp4[^"'\s<>]*""").find(html)?.value
        }
        if (src.isNullOrBlank()) {
            Log.w(TAG, "sin source en $targetUrl")
            return false
        }
        val videoUrl = if (src.startsWith("//")) "https:$src" else src
        Log.d(TAG, "mp4 directo: ${videoUrl.take(130)}")
        callback(newExtractorLink(name, name, videoUrl, ExtractorLinkType.VIDEO) {
            this.referer = targetUrl
        })
        return true
    }
}
