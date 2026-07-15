package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import android.util.Log

class LegadoskywalkerProvider : MainAPI() {
    override var mainUrl = "https://serieslegadoskywalker.blogspot.com"
    override var name = "LegadoSkywalker"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false
    override val usesWebView = false
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Movie,
        TvType.Cartoon
    )

    override val mainPage = mainPageOf(
        "/search/label/The%20Clone%20Wars" to "The Clone Wars",
        "/search/label/Rebels" to "Rebels",
        "/search/label/The%20Bad%20Batch" to "The Bad Batch",
        "/search/label/The%20Mandalorian" to "The Mandalorian",
        "/search/label/Ahsoka" to "Ahsoka",
        "/search/label/Andor" to "Andor",
        "/search/label/Star%20Wars%20Visions" to "Star Wars Visions",
        "/search/label/Tales%20of%20the%20Jedi" to "Tales of the Jedi",
        "/search/label/Tales%20of%20the%20Empire" to "Tales of the Empire",
        "/search/label/Peliculas" to "Películas",
        "/search/label/Fan-edits" to "Fan Edits",
        "/search/label/Obi-Wan%20Kenobi" to "Obi-Wan Kenobi",
        "/search/label/The%20Book%20of%20Boba%20Fett" to "The Book of Boba Fett",
        "/search/label/The%20Acolyte" to "The Acolyte",
        "/search/label/Skeleton%20Crew" to "Skeleton Crew",
        "/search/label/Maul%20%3A%20Shadow%20Lord" to "Maul: Shadow Lord",
        "/search/label/Star%20Wars%20Vintage" to "Star Wars Vintage",
        "/search/label/Lego%20Star%20Wars" to "Lego Star Wars",
    )

    private fun extractPostCards(doc: org.jsoup.nodes.Document): List<SearchResponse> {
        return doc.select("div.post").mapNotNull { post ->
            try {
                val titleEl = post.select("h1 a, h2 a, h1, h2").firstOrNull() ?: return@mapNotNull null
                val title = titleEl.text().trim()
                val link = titleEl.attr("abs:href").ifBlank {
                    post.select("a[href*='/20']").firstOrNull()?.attr("abs:href") ?: return@mapNotNull null
                }
                val img = post.select("img").firstOrNull()
                val poster = img?.attr("abs:src")
                    ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
                    ?: "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEjPD6oOfBS3rlp2NLWKzykwwjWq6o14m81l1KP6IOzDt4cabtM5UlkFbDb3NagADdvfhpRwOu2T8KjHhSnsvR1hf9CZL9ATngHYBX-L3GPkbxW62nHK0xcVk9zajOl2Woj4fi32pQHqCtXc/s400/no-video.gif"
                val body = post.select(".post-body, .entry-content").text().trim()
                val labels = post.select("a[rel=tag]").eachText()
                val type = when {
                    title.contains("[T", ignoreCase = true) -> TvType.TvSeries
                    labels.any { it.contains("Peliculas", ignoreCase = true) } -> TvType.Movie
                    else -> TvType.Cartoon
                }
                newMovieSearchResponse(title, link, type) {
                    this.posterUrl = poster
                }
            } catch (e: Exception) { null }
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "${mainUrl}${request.data}"
        val doc = app.get(url, timeout = 30L).document
        val posts = extractPostCards(doc)
        return newHomePageResponse(
            list = HomePageList(request.name, posts, isHorizontalImages = false),
            hasNext = false
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val encoded = java.net.URLEncoder.encode(query, "UTF-8")
        val url = "$mainUrl/search?q=$encoded&max-results=20"
        return try {
            val posts = extractPostCards(app.get(url, timeout = 30L).document)
            if (posts.isNotEmpty()) return posts
            extractPostCards(app.get("$mainUrl/search/label/$encoded", timeout = 30L).document)
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun load(url: String): LoadResponse {
        val doc = app.get(url, timeout = 30L).document
        val title = doc.select("h1, h2").firstOrNull()?.text()?.trim()
            ?: doc.select("title").text().trim().substringBefore("|").trim()
        val postBody = doc.select(".post-body.entry-content").firstOrNull()
            ?: doc.select(".post-body").firstOrNull()
        val poster = postBody?.select("img")?.firstOrNull()?.attr("abs:src")
            ?.replace(Regex("""/s\d+(-c)?/"""), "/s400/")
            ?: doc.select("img[src~=(?i)tmdb]").firstOrNull()?.attr("abs:src")
            ?: ""
        val plot = postBody?.text()?.trim()?.take(500)
        val tags = doc.select("a[rel=tag]").eachText()
        val year = Regex("""\b(19\d{2}|20\d{2})\b""").find(url)?.value?.toIntOrNull()

        val epMatch = Regex("""[Tt](\d+)[-\s]*[Cc](\d+)""").find(title)
        if (epMatch != null) {
            val season = epMatch.groupValues[1].toIntOrNull() ?: 1
            val episode = epMatch.groupValues[2].toIntOrNull() ?: 1
            val epName = title.replace(Regex("""\s*\[T\d+[-\s]*C\d+\]\s*"""), "").trim()
            val episodeData = newEpisode(url) {
                this.name = epName
                this.season = season
                this.episode = episode
                this.posterUrl = poster
            }
            val seriesName = tags.firstOrNull { it.contains("Temporada", ignoreCase = true) }
                ?.substringBefore("- Temporada")?.trim()
                ?: tags.firstOrNull { it != "post" } ?: "Star Wars"
            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(episodeData)) {
                this.posterUrl = poster
                this.plot = plot
                this.year = year
                this.tags = tags.filter { it != "post" }
            }
        }

        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
            this.plot = plot
            this.year = year
            this.tags = tags.filter { it != "post" }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val doc = app.get(data, timeout = 30L).document
        val iframes = doc.select("iframe[data-src]")
        if (iframes.isEmpty()) {
            doc.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("abs:src").ifBlank { iframe.attr("src") }
                if (src.isNotBlank() && !src.startsWith("data:")) {
                    processVideoUrl(src, data, subtitleCallback, callback)
                }
            }
            return true
        }
        iframes.forEach { iframe ->
            val src = iframe.attr("data-src").ifBlank { iframe.attr("src") }
            val absSrc = when {
                src.startsWith("//") -> "https:$src"
                src.startsWith("/") -> "$mainUrl$src"
                src.startsWith("http") -> src
                else -> src
            }
            if (absSrc.isNotBlank() && !absSrc.startsWith("data:")) {
                processVideoUrl(absSrc, data, subtitleCallback, callback)
            }
        }
        return true
    }

    private suspend fun processVideoUrl(
        url: String,
        referer: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            when {
                url.contains("filemoon.") -> extractFilemoon(url, subtitleCallback, callback)
                url.contains("powvideo.") -> extractPowvideo(url, referer, subtitleCallback, callback)
                url.contains("hqq.ac") || url.contains("netu") -> extractHqq(url, referer, subtitleCallback, callback)
                url.contains("mega.") || url.contains("mega.nz") -> callback(newExtractorLink("Mega", "Mega.nz", url) {
                    this.referer = referer; this.quality = 1080
                })
                else -> loadExtractor(url, referer, subtitleCallback, callback)
            }
        } catch (e: Exception) {
            Log.e("LegadoSkywalker", "processVideoUrl: ${e.message}")
        }
    }

    private suspend fun extractFilemoon(
        url: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            val fixed = url
                .replace("https://filemoon.link", "https://filemoon.sx")
                .replace("https://filemoon.to", "https://filemoon.sx")
            val page = app.get(fixed, referer = "https://filemoon.sx/").text
            val src = Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*"([^"]+)"\s*\}?\s*\]?""").find(page)
            if (src != null) {
                callback(newExtractorLink("Filemoon", "Filemoon", src.groupValues[1].replace("\\/", "/")) {
                    this.referer = "https://filemoon.sx/"; this.quality = 1080
                })
                return
            }
            val src2 = Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*'([^']+)'\s*\}?\s*\]?""").find(page)
            if (src2 != null) {
                callback(newExtractorLink("Filemoon", "Filemoon", src2.groupValues[1].replace("\\/", "/")) {
                    this.referer = "https://filemoon.sx/"; this.quality = 1080
                })
                return
            }
            val direct = Regex("""https?://[^"'\s<>]+\.(?:mp4|m3u8)[^"'\s<>]*""").find(page)
            if (direct != null) {
                callback(newExtractorLink("Filemoon", "Filemoon", direct.value) {
                    this.referer = "https://filemoon.sx/"; this.quality = 1080
                })
                return
            }
            val iframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""").find(page)
            if (iframe != null) {
                loadExtractor(fixUrl(iframe.groupValues[1]), "https://filemoon.sx/", subtitleCallback, callback)
                return
            }
            loadExtractor(fixed, "https://filemoon.sx/", subtitleCallback, callback)
        } catch (e: Exception) {
            loadExtractor(url, "https://filemoon.sx/", subtitleCallback, callback)
        }
    }

    private suspend fun extractPowvideo(
        url: String,
        referer: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            val page = app.get(url, referer = referer, timeout = 30L).text
            val src = Regex("""sources\s*:\s*\[?\s*\{?\s*file\s*:\s*"([^"]+)"\s*\}?\s*\]?""").find(page)
            if (src != null) {
                callback(newExtractorLink("Powvideo", "Powvideo", src.groupValues[1].replace("\\/", "/")) {
                    this.referer = url; this.quality = 720
                })
                return
            }
            val video = Regex("""<video[^>]+src=["']([^"']+)["']""").find(page)
            if (video != null) {
                callback(newExtractorLink("Powvideo", "Powvideo", video.groupValues[1]) {
                    this.referer = url; this.quality = 720
                })
                return
            }
            val iframe = Regex("""<iframe[^>]+src=["']([^"']+)["']""").find(page)
            if (iframe != null) {
                loadExtractor(fixUrl(iframe.groupValues[1]), url, subtitleCallback, callback)
                return
            }
            loadExtractor(url, referer, subtitleCallback, callback)
        } catch (e: Exception) {
            loadExtractor(url, referer, subtitleCallback, callback)
        }
    }

    private suspend fun extractHqq(
        url: String,
        referer: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            val page = app.get(url, referer = referer, timeout = 60L).text
            val file = Regex("""(?:file|src)\s*:\s*["']([^"']+\.(?:mp4|m3u8)[^"']*)["']""").find(page)
            if (file != null) {
                callback(newExtractorLink("HQQ", "HQQ", file.groupValues[1].replace("\\/", "/")) {
                    this.referer = url; this.quality = 1080
                })
                return
            }
            val direct = Regex("""https?://[^"'\s<>]+\.(?:m3u8|mp4)[^"'\s<>]*""").find(page)
            if (direct != null) {
                callback(newExtractorLink("HQQ", "HQQ", direct.value) {
                    this.referer = url; this.quality = 1080
                })
                return
            }
            loadExtractor(url, referer, subtitleCallback, callback)
        } catch (e: Exception) {
            loadExtractor(url, referer, subtitleCallback, callback)
        }
    }

    private fun fixUrl(url: String): String {
        return when {
            url.startsWith("//") -> "https:$url"
            url.startsWith("/") -> "$mainUrl$url"
            else -> url
        }
    }
}
