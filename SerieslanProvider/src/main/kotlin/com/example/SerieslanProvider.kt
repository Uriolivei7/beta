package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.jsoup.nodes.Element
import java.net.URLDecoder

class SeriesLanProvider : MainAPI() {
    override var mainUrl = "https://serieslan.lat"
    override var name = "SeriesLan"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Cartoon
    )

    override val mainPage = mainPageOf(
        "/" to "Últimas Agregadas"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val doc = app.get(mainUrl).document
        val items = doc.select("a[href^=\"/serie/\"]").mapNotNull { it.toSearchResult() }
        if (items.isEmpty()) return null
        return newHomePageResponse(
            list = HomePageList(request.name, items),
            hasNext = false
        )
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val href = attr("href")
        val title = selectFirst("h2.serie-title")?.text() ?: return null
        val poster = selectFirst("img.poster-img")?.attr("src")?.let { fixUrl(it) } ?: return null
        val year = selectFirst("span.year-tag")?.text()?.toIntOrNull()
        return newTvSeriesSearchResponse(title, fixUrl(href), TvType.Cartoon) {
            this.posterUrl = poster
            this.year = year
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/?q=$query").document
        return doc.select("a[href^=\"/serie/\"]").mapNotNull { it.toSearchResult() }
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document

        val titleElement = doc.selectFirst("h1") ?: return null
        val fullTitle = titleElement.text()
        val title = fullTitle.substringBefore("(").trim()
        val year = Regex("""\((\d+)\)""").find(fullTitle)?.groupValues?.get(1)?.toIntOrNull()

        val poster = doc.selectFirst("aside.col-md-3 img")?.attr("src")?.let { fixUrl(it) } ?: ""
        val description = doc.selectFirst(".resumen")?.text() ?: ""

        val tags = doc.select("a.genero-link").mapNotNull { it.text() }

        val recommendations = doc.select("a.rec-card").mapNotNull { element ->
            val recTitle = element.selectFirst("span")?.text() ?: return@mapNotNull null
            val recUrl = element.attr("href")
            val recPoster = element.selectFirst("img")?.attr("src")?.let { fixUrl(it) } ?: ""
            newTvSeriesSearchResponse(recTitle, fixUrl(recUrl), TvType.Cartoon) {
                this.posterUrl = recPoster
            }
        }

        val episodes = mutableListOf<Episode>()

        val seasonLinks = doc.select("a.btn-temp").mapNotNull { element ->
            val href = element.attr("href")
            val seasonText = element.text()
            val seasonNum = Regex("""\d+""").find(seasonText)?.value?.toIntOrNull()
            val seasonId = Regex("""/ver/(\d+)""").find(href)?.groupValues?.get(1)
            if (seasonId != null && seasonNum != null) Pair(seasonNum, seasonId)
            else null
        }

        if (seasonLinks.isNotEmpty()) {
            val epDoc = app.get("$mainUrl/ver.php?id=${seasonLinks.first().second}").document
            val accordionItems = epDoc.select("#seriesAccordion .accordion-item")
            for (acc in accordionItems) {
                val seasonNum = Regex("""\d+""").find(
                    acc.selectFirst(".accordion-button")?.text() ?: continue
                )?.value?.toIntOrNull() ?: continue

                for ((index, epLink) in acc.select(".ep-link").withIndex()) {
                    val epHref = epLink.attr("href")
                    val epText = epLink.text()
                    val epNum = Regex("""E(\d+)""").find(epText)?.groupValues?.get(1)?.toIntOrNull()
                        ?: (index + 1)
                    val epName = epText.substringAfter("- ").trim()
                    val epId = Regex("""\d+""").find(epHref)?.value ?: continue

                    episodes.add(newEpisode("$mainUrl/ver.php?id=$epId") {
                        this.name = epName
                        this.season = seasonNum
                        this.episode = epNum
                    })
                }
            }
        }

        return newTvSeriesLoadResponse(title, url, TvType.Cartoon, episodes) {
            this.posterUrl = poster
            this.plot = description
            this.tags = tags
            this.recommendations = recommendations
            this.year = year
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("SeriesLan-Links", "=== loadLinks START - data: $data ===")

        val doc = app.get(data, headers = mapOf("Referer" to mainUrl)).document
        val iframeSrc = doc.selectFirst(".video-container iframe")?.attr("src")
        Log.d("SeriesLan-Links", "iframe src: $iframeSrc")
        if (iframeSrc.isNullOrBlank()) {
            Log.e("SeriesLan-Links", "No iframe found in page")
            return false
        }

        val extractorResult = loadExtractor(iframeSrc, data, subtitleCallback, callback)
        Log.d("SeriesLan-Links", "loadExtractor result: $extractorResult")
        if (extractorResult) return true

        val videoUrl = if (iframeSrc.contains("bflix.store")) {
            val encoded = Regex("""[?&]v=([^&]+)""").find(iframeSrc)?.groupValues?.get(1)
            Log.d("SeriesLan-Links", "bflix encoded v param: $encoded")
            if (encoded != null) {
                try {
                    val decoded = URLDecoder.decode(encoded, "UTF-8")
                    Log.d("SeriesLan-Links", "decoded video URL: $decoded")
                    decoded
                } catch (e: Exception) {
                    Log.e("SeriesLan-Links", "URL decode failed: ${e.message}")
                    iframeSrc
                }
            } else {
                Log.d("SeriesLan-Links", "No v param found, using iframe src as-is")
                iframeSrc
            }
        } else {
            Log.d("SeriesLan-Links", "Not bflix.store, using iframe src directly")
            iframeSrc
        }

        Log.d("SeriesLan-Links", "Emitting link: $videoUrl")
        Log.d("SeriesLan-Links", "Referer: $data, iframeSrc referer: $iframeSrc")

        try {
            callback.invoke(
                newExtractorLink(name, "SeriesLan", videoUrl) {
                    this.referer = data
                    this.quality = Qualities.Unknown.value
                    this.type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    this.headers = mapOf(
                        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                        "Referer" to iframeSrc
                    )
                }
            )
            Log.d("SeriesLan-Links", "Link emitted successfully")
        } catch (e: Exception) {
            Log.e("SeriesLan-Links", "Failed to emit link: ${e.message}", e)
            return false
        }
        return true
    }
}
