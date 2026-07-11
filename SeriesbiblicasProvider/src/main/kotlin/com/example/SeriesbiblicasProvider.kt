package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import android.util.Log

class SeriesbiblicasProvider : MainAPI() {
    override var mainUrl = "https://seriesbiblicas.net"
    override var name = "SeriesBiblicas"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.TvSeries)

    override val mainPage = mainPageOf("/" to "Todas las Series")

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(mainUrl).document
        Log.d("SeriesBiblicas", "getMainPage: fetched homepage, title=${document.title()}")

        val home = ArrayList<SearchResponse>()
        val seen = mutableSetOf<String>()

        val figures = document.select("figure[class*='ct-hover'] a[href]")
        Log.d("SeriesBiblicas", "getMainPage: found ${figures.size} ct-hover figures")

        figures.forEach { link ->
            val url = link.attr("abs:href")
            if (url.isBlank() || !url.startsWith(mainUrl) || !seen.add(url)) return@forEach

            val poster = link.parent()?.selectFirst("img")?.let { img ->
                img.attr("data-lazy-src").ifBlank { img.attr("src") }
            }

            val slug = url.removeSuffix("/").substringAfterLast("/")
            val title = slugToTitle(slug)
            Log.d("SeriesBiblicas", "getMainPage: series $slug -> $title, poster=$poster")
            home.add(newTvSeriesSearchResponse(title, url, TvType.TvSeries) {
                this.posterUrl = fixUrl(poster)
            })
        }

        Log.d("SeriesBiblicas", "getMainPage: returning ${home.size} series")
        return newHomePageResponse(
            list = HomePageList(name = request.name, list = home, isHorizontalImages = false),
            hasNext = false
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchUrl = "$mainUrl/?s=$query"
        Log.d("SeriesBiblicas", "search: query=$query url=$searchUrl")
        val doc = app.get(searchUrl).document
        Log.d("SeriesBiblicas", "search: page title=${doc.title()}")

        val results = ArrayList<SearchResponse>()
        var modules = doc.select("div.td_module_wrap")
        Log.d("SeriesBiblicas", "search: found ${modules.size} td_module_wrap entries")
        if (modules.isEmpty()) {
            modules = doc.select("article, .td-module, .td_module")
            Log.d("SeriesBiblicas", "search: fallback selectors gave ${modules.size} entries")
        }

        modules.forEach { module ->
            val link = module.selectFirst("h3.entry-title a[href], h3 a[href], .entry-title a[href]")
                ?: module.selectFirst("a[href^='$mainUrl/']")
            val url = link?.attr("abs:href") ?: return@forEach
            val title = link.ownText().trim().ifBlank { link.text().trim() }
            if (title.isBlank()) return@forEach
            val poster = module.selectFirst("img")?.let { img ->
                img.attr("data-lazy-src").ifBlank { img.attr("src") }
            }
            Log.d("SeriesBiblicas", "search result: $title -> $url")
            results.add(newTvSeriesSearchResponse(title, url, TvType.TvSeries) {
                this.posterUrl = fixUrl(poster)
            })
        }

        if (results.isEmpty()) {
            Log.d("SeriesBiblicas", "search: trying VC grid fallback")
            doc.select("figure[class*='ct-hover'] a[href]").forEach { link ->
                val url = link.attr("abs:href")
                if (url.isBlank()) return@forEach
                val poster = link.parent()?.selectFirst("img")?.let { img ->
                    img.attr("data-lazy-src").ifBlank { img.attr("src") }
                }
                val slug = url.removeSuffix("/").substringAfterLast("/")
                val title = slugToTitle(slug)
                Log.d("SeriesBiblicas", "search (VC): $title -> $url")
                results.add(newTvSeriesSearchResponse(title, url, TvType.TvSeries) {
                    this.posterUrl = fixUrl(poster)
                })
            }
        }

        Log.d("SeriesBiblicas", "search: total=${results.size}")
        return results
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("SeriesBiblicas", "load: $url")
        val document = app.get(url).document
        Log.d("SeriesBiblicas", "load: page title=${document.title()}")

        val title = document.selectFirst("title")?.text()
            ?.replace(Regex("""\s*[-–|]\s*Series\s*Biblicas"""), "")
            ?.trim()
            ?: document.selectFirst(".entry-title")?.text()
            ?: slugToTitle(url.removeSuffix("/").substringAfterLast("/"))
        Log.d("SeriesBiblicas", "load: cleaned title=$title")

        val poster = fixUrl(document.selectFirst("meta[property='og:image']")?.attr("content")
            ?: document.selectFirst("img.aligncenter.size-full")?.attr("src"))
        Log.d("SeriesBiblicas", "load: poster=$poster")

        val description = document.select(".su-tabs-pane").getOrNull(1)?.text()
            ?: document.selectFirst("meta[name='description']")?.attr("content")
        Log.d("SeriesBiblicas", "load: description len=${description?.length ?: 0}")

        val tabsCount = document.select(".su-tabs").size
        val accordionCount = document.select(".su-accordion").size
        Log.d("SeriesBiblicas", "load: su-tabs=$tabsCount, su-accordion=$accordionCount")

        val episodes = ArrayList<Episode>()
        val spoilers = document.select(".su-accordion .su-spoiler")
        Log.d("SeriesBiblicas", "load: found ${spoilers.size} spoilers")
        spoilers.forEachIndexed { index, spoiler ->
            val epTitle = spoiler.selectFirst(".su-spoiler-title")?.text() ?: "Capítulo ${index + 1}"
            val epNum = Regex("""\d+""").find(epTitle)?.value?.toIntOrNull() ?: (index + 1)
            val lightboxCount = spoiler.select(".su-lightbox[data-mfp-src]").size
            Log.d("SeriesBiblicas", "load: ep$index title=$epTitle num=$epNum lightboxes=$lightboxCount")
            episodes.add(newEpisode("$url?ep=$index") {
                this.name = epTitle
                this.episode = epNum
                this.season = 1
            })
        }

        Log.d("SeriesBiblicas", "load: returning ${episodes.size} episodes for $title")
        return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
            this.posterUrl = poster
            this.plot = description
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val epIndex = Regex("""[?&]ep=(\d+)""").find(data)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val seriesUrl = data.substringBefore("?ep=").substringBefore("&ep=")
        Log.d("SeriesBiblicas", "loadLinks: epIndex=$epIndex seriesUrl=$seriesUrl")

        val document = app.get(seriesUrl).document
        val spoilers = document.select(".su-accordion .su-spoiler")
        Log.d("SeriesBiblicas", "loadLinks: total spoilers=${spoilers.size}, requested index=$epIndex")

        val spoiler = spoilers.getOrNull(epIndex)
        if (spoiler == null) {
            Log.d("SeriesBiblicas", "loadLinks: spoiler $epIndex not found!")
            return false
        }

        val lightboxes = spoiler.select(".su-lightbox[data-mfp-src]")
        Log.d("SeriesBiblicas", "loadLinks: found ${lightboxes.size} lightboxes in spoiler $epIndex")
        if (lightboxes.isEmpty()) {
            val allLinks = spoiler.select("a[href]")
            Log.d("SeriesBiblicas", "loadLinks: no lightboxes, ${allLinks.size} links in spoiler instead")
            allLinks.forEach { Log.d("SeriesBiblicas", "loadLinks: link href=${it.attr("abs:href")} class=${it.attr("class")}") }
        }

        var found = false
        lightboxes.forEach { lightbox ->
            val videoUrl = lightbox.attr("data-mfp-src")
            Log.d("SeriesBiblicas", "loadLinks: videoUrl=$videoUrl")
            if (videoUrl.isNotBlank()) {
                loadExtractor(videoUrl, seriesUrl, subtitleCallback, callback)
                found = true
            }
        }

        Log.d("SeriesBiblicas", "loadLinks: found=$found")
        return found
    }

    private fun slugToTitle(slug: String): String {
        val clean = slug
            .replace("-hd", " HD")
            .replace("-google-drive", "")
            .replace("-ok-ru", "")
            .replace("-imagentv", " ImagenTV")
            .replace("-unife-google-drive", "")
            .replace("-unife", "")
            .replace("-audio-latino", " (Audio Latino)")
            .replace("-latino", " (Latino)")
            .replace("-subtitulada", " (Subtitulada)")
            .replace("-sub", " (Sub)")
            .replace("-espanol", " (Español)")
            .replace("-portugues", " (PT)")
        return clean.split("-").joinToString(" ") { word ->
            if (word.all { it.isUpperCase() }) word else word.replaceFirstChar { it.uppercase() }
        }.trim()
    }

    private fun fixUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        return if (url.startsWith("//")) "https:$url" else url
    }
}
