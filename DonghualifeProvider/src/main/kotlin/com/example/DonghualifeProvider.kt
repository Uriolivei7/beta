package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.jsoup.nodes.Element

class DonghualifeProvider : MainAPI() {
    override var mainUrl = "https://donghualife.com"
    override var name = "DonghuaLife"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie
    )

    private fun fixTitle(title: String?): String {
        return title?.replace(Regex("\\s*\\|.*$"), "")?.trim() ?: ""
    }

    private fun extractYearFromDatetime(datetime: String?): String? {
        return datetime?.take(4)?.takeIf { it.all { c -> c.isDigit() } }
    }

    private fun extractHref(element: Element?): String? {
        val href = element?.attr("href") ?: element?.attr("abs:href")
        return if (href.isNullOrBlank()) null else fixUrl(href)
    }

    private fun extractImgSrc(element: Element?): String? {
        val src = element?.attr("src") ?: element?.attr("abs:src")
        return if (src.isNullOrBlank()) null else fixUrl(src)
    }

    private fun Element.getPosterUrl(): String? {
        return extractImgSrc(selectFirst("img"))
    }

    private fun Element.getSeriesLink(): String? {
        val link = selectFirst(".titulo a") ?: selectFirst(".vermas a") ?: selectFirst(".imagen a")
        return extractHref(link)
    }

    private fun Element.getTitle(): String {
        return fixTitle(selectFirst(".titulo")?.text())
    }

    private fun episodeToSeriesUrl(episodeUrl: String): String? {
        val match = Regex("/episode/([^/]+)-\\d+$").find(episodeUrl) ?: return null
        return "$mainUrl/series/${match.groupValues[1]}"
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val html = app.get("$mainUrl/").text
        val doc = app.get("$mainUrl/").document

        val carouselItems = doc.select(".carousel-inner .views-row .banner")
        val carousel = carouselItems.mapNotNull { banner ->
            val poster = extractImgSrc(banner.selectFirst(".imagen img"))
            val href = extractHref(banner.selectFirst(".titulo a")) ?: extractHref(banner.selectFirst(".vermas a"))
            val title = fixTitle(banner.selectFirst(".titulo")?.text())
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        val latestItems = doc.select(".view-patreon .views-row .episode")
        val latestEpisodes = latestItems.mapNotNull { ep ->
            val poster = extractImgSrc(ep.selectFirst(".imagen img"))
            val episodeHref = extractHref(ep.selectFirst(".imagen a"))
            val title = fixTitle(ep.selectFirst(".titulo")?.text())
            val seriesUrl = episodeHref?.let { episodeToSeriesUrl(it) }
            val href = seriesUrl ?: episodeHref ?: return@mapNotNull null
            if (title.isBlank()) return@mapNotNull null
            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        val movieItems = doc.select(".view-pelis-donghuas .views-row .movie")
        val movies = movieItems.mapNotNull { movie ->
            val poster = extractImgSrc(movie.selectFirst(".imagen img"))
            val href = extractHref(movie.selectFirst(".imagen a"))
            val title = fixTitle(movie.selectFirst(".titulo")?.text())
            if (href.isNullOrBlank() || title.isBlank()) null else newMovieSearchResponse(title, href, TvType.AnimeMovie) {
                this.posterUrl = poster
            }
        }

        val popularItems = doc.select(".view-mas-populares .views-row .serie")
        val popular = popularItems.mapNotNull { serie ->
            val poster = serie.getPosterUrl()
            val href = serie.getSeriesLink()
            val title = serie.getTitle()
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        val viewedItems = doc.select(".view-mas-vistos-hoy .views-row .episodio")
        val mostViewed = viewedItems.mapNotNull { ep ->
            val poster = extractImgSrc(ep.selectFirst(".imagen img"))
            val episodeHref = extractHref(ep.selectFirst(".titulo a"))
            val title = fixTitle(ep.selectFirst(".titulo")?.text())
            val seriesUrl = episodeHref?.let { episodeToSeriesUrl(it) }
            val href = seriesUrl ?: episodeHref ?: return@mapNotNull null
            if (title.isBlank()) return@mapNotNull null
            newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        val pages = mutableListOf<HomePageList>()
        if (carousel.isNotEmpty()) pages.add(HomePageList("Destacados", carousel, isHorizontalImages = true))
        if (popular.isNotEmpty()) pages.add(HomePageList("Populares", popular))
        if (movies.isNotEmpty()) pages.add(HomePageList("Películas", movies))

        return if (pages.isEmpty()) null else newHomePageResponse(pages, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchUrl = "$mainUrl/search?search_api_fulltext=$query"
        val doc = app.get(searchUrl).document
        
        val allItems = doc.select(".view-content .views-row .serie, .view-content .views-row .movie")
        
        return allItems.mapNotNull { item ->
            val poster = item.getPosterUrl()
            val href = item.getSeriesLink()
            val title = item.getTitle()
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, if (item.hasClass("movie")) TvType.AnimeMovie else TvType.Anime) {
                this.posterUrl = poster
            }
        }
    }

    private fun parseSeasonUrl(url: String): Pair<String, Int>? {
        val match = Regex("/season/([^/]+)-(\\d+)").find(url) ?: return null
        return Pair(match.groupValues[1], match.groupValues[2].toInt())
    }

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document
        
        val isMovie = url.contains("/movie/")
        
        val title = fixTitle(doc.selectFirst(".field--name-title")?.text())
            ?: fixTitle(doc.selectFirst("h2 a span")?.text())
            ?: "Sin título"
            
        val poster = extractImgSrc(doc.selectFirst(".field--name-field-poster img"))
            ?: doc.selectFirst("meta[property=og:image]")?.attr("content")
            
        val description = doc.selectFirst(".field--name-field-synopsis")?.text()
        
        val year = extractYearFromDatetime(doc.selectFirst(".field--name-field-fecha-de-emision time")?.attr("datetime"))
        
        val tags = doc.select(".field--name-field-genero .field__item a").mapNotNull { it.text().trim() }.takeIf { it.isNotEmpty() }

        if (isMovie) {
            return newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.year = year?.toIntOrNull()
                this.plot = description
                this.tags = tags
            }
        }

        val seasonsUrls = doc.select(".view-temporadas .views-row .serie a").mapNotNull {
            extractHref(it)
        }

        val episodes = mutableListOf<Episode>()

        if (seasonsUrls.isNotEmpty()) {
            for (seasonUrl in seasonsUrls) {
                val seasonInfo = parseSeasonUrl(seasonUrl)
                val seasonNum = seasonInfo?.second ?: 1
                
                try {
                    val seasonDoc = app.get(seasonUrl).document
                    seasonDoc.select("table.table-hover tbody tr, .views-row tr").forEach { row ->
                        val epNum = row.selectFirst("th[scope=row]")?.text()?.trim()?.toIntOrNull()
                        val epLink = row.selectFirst("td a") ?: return@forEach
                        val epHref = extractHref(epLink) ?: return@forEach
                        val epName = epLink.text().trim()
                        
                        if (epHref.isNotBlank()) {
                            episodes.add(newEpisode(epHref) {
                                this.name = epName.takeIf { it.isNotBlank() } ?: "Episodio $epNum"
                                this.episode = epNum
                                this.season = seasonNum
                            })
                        }
                    }
                } catch (e: Exception) {
                }
            }
        } else {
            doc.select(".episode-item, .views-row .episode").forEach {
                val a = it.selectFirst("a") ?: return@forEach
                val epTitle = a.text().trim()
                val epHref = extractHref(a) ?: return@forEach
                val epNum = Regex("(?i)episodio\\s*(\\d+)").find(epTitle)?.groupValues?.get(1)?.toIntOrNull()
                episodes.add(newEpisode(epHref) {
                    this.name = epTitle
                    this.episode = epNum
                })
            }
        }

        episodes.sortWith(compareBy({ it.season ?: 0 }, { it.episode ?: 0 }))

        return if (episodes.isEmpty()) {
            newMovieLoadResponse(title, url, TvType.AnimeMovie, url) {
                this.posterUrl = poster
                this.year = year?.toIntOrNull()
                this.plot = description
                this.tags = tags
            }
        } else {
            newAnimeLoadResponse(title, url, TvType.Anime) {
                this.posterUrl = poster
                this.year = year?.toIntOrNull()
                addEpisodes(DubStatus.Subbed, episodes)
                this.plot = description
                this.tags = tags
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val doc = app.get(data).document
        
        var found = false
        
        doc.select(".embed-links li a").forEach { link ->
            val videoUrl = link.attr("data-video")
            
            if (videoUrl.isNotBlank()) {
                loadExtractor(videoUrl, data, subtitleCallback, callback)
                found = true
            }
        }
        
        doc.select("iframe").forEach { iframe ->
            val src = iframe.attr("abs:src")
            if (src.isNotBlank()) {
                loadExtractor(src, data, subtitleCallback, callback)
                found = true
            }
        }
        
        doc.select("source, video").forEach { video ->
            val src = video.attr("abs:src")
            if (src.isNotBlank()) {
                val isM3u8 = src.contains(".m3u8")
                callback.invoke(
                    newExtractorLink(this.name, this.name, src) {
                        this.referer = data
                        this.type = if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    }
                )
                found = true
            }
        }
        
        return found
    }
}