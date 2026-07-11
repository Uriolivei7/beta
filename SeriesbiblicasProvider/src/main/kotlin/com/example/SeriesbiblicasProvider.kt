package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import android.util.Log
import org.json.JSONArray

class SeriesbiblicasProvider : MainAPI() {
    override var mainUrl = "https://seriesbiblicas.net"
    override var name = "SeriesBiblicas"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Movie, TvType.Cartoon)

    override val mainPage = mainPageOf("/" to "Todas las Series")

    private val excludedSlugs = setOf(
        "portada", "peliculas", "biblias", "ensenanzas", "alabanzas",
        "devocionales", "donar", "aviso-legal", "politica-de-privacidad",
        "category"
    )

    private suspend fun fetchSeriesFromApi(): List<Pair<String, String>> {
        val resp = app.get("$mainUrl/wp-json/wp/v2/pages?per_page=100")
        val pages = JSONArray(resp.text)
        val series = mutableListOf<Pair<String, String>>()
        val seen = mutableSetOf<String>()

        for (i in 0 until pages.length()) {
            val page = pages.getJSONObject(i)
            val slug = page.getString("slug")
            if (slug.isBlank() || slug in excludedSlugs || !seen.add(slug)) continue
            val link = page.getString("link")
            val title = page.getJSONObject("title").getString("rendered")
                .replace("&#8211;", "-")
                .replace("&#8220;", "\"")
                .replace("&#8221;", "\"")
                .replace(Regex("""\s*\u25b6\s*$"""), "")
                .trim()
            series.add(title to link)
        }

        return series
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d("SeriesBiblicas", "getMainPage: fetching via REST API")
        val series = fetchSeriesFromApi()
        Log.d("SeriesBiblicas", "getMainPage: got ${series.size} series from API")

        val home = series.map { (title, url) ->
            newTvSeriesSearchResponse(title, url, TvType.TvSeries) {
                this.posterUrl = null
            }
        }

        return newHomePageResponse(
            list = HomePageList(name = request.name, list = home, isHorizontalImages = false),
            hasNext = false
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("SeriesBiblicas", "search: query=$query via REST API")
        val result = mutableListOf<SearchResponse>()

        try {
            val resp = app.get("$mainUrl/wp-json/wp/v2/pages?search=$query&per_page=30")
            val pages = JSONArray(resp.text)
            Log.d("SeriesBiblicas", "search: API returned ${pages.length()} results")

            for (i in 0 until pages.length()) {
                val page = pages.getJSONObject(i)
                val slug = page.getString("slug")
                if (slug.isBlank() || slug in excludedSlugs) continue
                val link = page.getString("link")
                val title = page.getJSONObject("title").getString("rendered")
                    .replace("&#8211;", "-")
                    .replace("&#8220;", "\"")
                    .replace("&#8221;", "\"")
                    .replace(Regex("""\s*\u25b6\s*$"""), "")
                    .trim()
                Log.d("SeriesBiblicas", "search: result $title -> $link")
                result.add(newTvSeriesSearchResponse(title, link, TvType.TvSeries))
            }
        } catch (e: Exception) {
            Log.e("SeriesBiblicas", "search: API failed, fallback to HTML. ${e.message}")
            val doc = app.get("$mainUrl/?s=$query").document
            doc.select("a[href^='$mainUrl/']").forEach { link ->
                val url = link.attr("abs:href")
                if (url.isBlank()) return@forEach
                val slug = url.removeSuffix("/").substringAfterLast("/")
                if (slug.isBlank() || slug in excludedSlugs) return@forEach
                val title = link.ownText().trim().ifBlank { link.text().trim() }
                if (title.isBlank() || title.length < 3) return@forEach
                result.add(newTvSeriesSearchResponse(title, url, TvType.TvSeries))
            }
        }

        Log.d("SeriesBiblicas", "search: total=${result.size}")
        return result
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("SeriesBiblicas", "load: $url")
        val document = app.get(url).document

        val title = document.selectFirst("title")?.text()
            ?.replace(Regex("""\s*[-–|]\s*SERIESBIBLICAS\.NET"""), "")
            ?.replace(Regex("""\s*[-–|]\s*Series\s*Biblicas"""), "")
            ?.trim()
            ?: document.selectFirst(".entry-title")?.text()
            ?: slugToTitle(url.removeSuffix("/").substringAfterLast("/"))
        Log.d("SeriesBiblicas", "load: title=$title")

        val poster = fixUrl(document.selectFirst("meta[property='og:image']")?.attr("content")
            ?: document.selectFirst("img.aligncenter.size-full")?.attr("src"))
        Log.d("SeriesBiblicas", "load: poster=$poster")

        val description = document.select(".su-tabs-pane").getOrNull(1)?.text()
            ?: document.selectFirst("meta[name='description']")?.attr("content")
        Log.d("SeriesBiblicas", "load: description len=${description?.length ?: 0}")

        val episodes = ArrayList<Episode>()
        val spoilers = document.select(".su-accordion .su-spoiler")
        Log.d("SeriesBiblicas", "load: total spoilers=${spoilers.size}")

        spoilers.forEachIndexed { index, spoiler ->
            val epTitle = spoiler.selectFirst(".su-spoiler-title")?.text() ?: "Capítulo ${index + 1}"
            val epNum = Regex("""\d+""").find(epTitle)?.value?.toIntOrNull() ?: (index + 1)
            val lightboxCount = spoiler.select(".su-lightbox[data-mfp-src]").size

            if (lightboxCount == 0) {
                Log.d("SeriesBiblicas", "load: skipping spoiler $index ($epTitle) - no lightboxes")
                return@forEachIndexed
            }

            Log.d("SeriesBiblicas", "load: ep$index title=$epNum lightboxes=$lightboxCount")
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
        val spoilersWithVideo = document.select(".su-accordion .su-spoiler").filter { spoiler ->
            spoiler.select(".su-lightbox[data-mfp-src]").isNotEmpty()
        }
        Log.d("SeriesBiblicas", "loadLinks: spoilers with video=${spoilersWithVideo.size}, index=$epIndex")

        val spoiler = spoilersWithVideo.getOrNull(epIndex)
        if (spoiler == null) {
            Log.d("SeriesBiblicas", "loadLinks: spoiler $epIndex not found!")
            return false
        }

        val lightboxes = spoiler.select(".su-lightbox[data-mfp-src]")
        Log.d("SeriesBiblicas", "loadLinks: found ${lightboxes.size} lightboxes")

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
