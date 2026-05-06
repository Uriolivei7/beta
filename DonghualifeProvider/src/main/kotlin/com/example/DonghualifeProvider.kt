package com.example

import android.util.Log
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
    override var lang = "es"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA,
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

    private fun parseSearchItem(item: Element): SearchResponse? {
        val poster = item.getPosterUrl()
        val href = item.getSeriesLink() ?: return null
        val title = item.getTitle()
        if (title.isBlank()) return null
        val isMovie = item.hasClass("movie")
        return newAnimeSearchResponse(title, href, if (isMovie) TvType.AnimeMovie else TvType.Anime) {
            this.posterUrl = poster
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d("DLife-Main", "=== getMainPage START ===")
        
        val html = app.get("$mainUrl/").text
        Log.d("DLife-Main", "HTML length: ${html.length}")
        val doc = app.get("$mainUrl/").document

        val carouselItems = doc.select(".carousel-inner .views-row .banner")
        Log.d("DLife-Main", "Carousel items found: ${carouselItems.size}")
        val carousel = carouselItems.mapNotNull { banner ->
            val poster = extractImgSrc(banner.selectFirst(".imagen img"))
            val href = extractHref(banner.selectFirst(".titulo a")) ?: extractHref(banner.selectFirst(".vermas a"))
            val title = fixTitle(banner.selectFirst(".titulo")?.text())
            Log.d("DLife-Main", "  Carousel: title='$title', href='$href', poster='${poster != null}'")
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }
        Log.d("DLife-Main", "Carousel parsed: ${carousel.size} items")

        val latestItems = doc.select(".view-patreon .views-row .episode")
        Log.d("DLife-Main", "Latest episodes items found: ${latestItems.size}")
        val latestEpisodes = latestItems.mapNotNull { ep ->
            val poster = extractImgSrc(ep.selectFirst(".imagen img"))
            val href = extractHref(ep.selectFirst(".imagen a"))
            val title = fixTitle(ep.selectFirst(".titulo")?.text())
            val subtitulo = ep.selectFirst(".subtitulo")?.text()?.trim()
            val fullName = if (!subtitulo.isNullOrBlank()) "$title $subtitulo" else title
            Log.d("DLife-Main", "  Latest: title='$title', sub='$subtitulo', href='$href'")
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(fullName, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }
        Log.d("DLife-Main", "Latest episodes parsed: ${latestEpisodes.size} items")

        val movieItems = doc.select(".view-pelis-donghuas .views-row .movie")
        Log.d("DLife-Main", "Movie items found: ${movieItems.size}")
        val movies = movieItems.mapNotNull { movie ->
            val poster = extractImgSrc(movie.selectFirst(".imagen img"))
            val href = extractHref(movie.selectFirst(".imagen a"))
            val title = fixTitle(movie.selectFirst(".titulo")?.text())
            Log.d("DLife-Main", "  Movie: title='$title', href='$href'")
            if (href.isNullOrBlank() || title.isBlank()) null else newMovieSearchResponse(title, href, TvType.AnimeMovie) {
                this.posterUrl = poster
            }
        }
        Log.d("DLife-Main", "Movies parsed: ${movies.size} items")

        val popularItems = doc.select(".view-mas-populares .views-row .serie")
        Log.d("DLife-Main", "Popular items found: ${popularItems.size}")
        val popular = popularItems.mapNotNull { serie ->
            val poster = serie.getPosterUrl()
            val href = serie.getSeriesLink()
            val title = serie.getTitle()
            Log.d("DLife-Main", "  Popular: title='$title', href='$href'")
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }
        Log.d("DLife-Main", "Popular parsed: ${popular.size} items")

        val viewedItems = doc.select(".view-mas-vistos-hoy .views-row .episodio")
        Log.d("DLife-Main", "Most viewed items found: ${viewedItems.size}")
        val mostViewed = viewedItems.mapNotNull { ep ->
            val poster = extractImgSrc(ep.selectFirst(".imagen img"))
            val href = extractHref(ep.selectFirst(".titulo a"))
            val title = fixTitle(ep.selectFirst(".titulo")?.text())
            Log.d("DLife-Main", "  MostViewed: title='$title', href='$href'")
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }
        Log.d("DLife-Main", "Most viewed parsed: ${mostViewed.size} items")

        val pages = mutableListOf<HomePageList>()
        if (carousel.isNotEmpty()) pages.add(HomePageList("Destacados", carousel, isHorizontalImages = true))
        if (latestEpisodes.isNotEmpty()) pages.add(HomePageList("Últimos Episodios", latestEpisodes))
        if (movies.isNotEmpty()) pages.add(HomePageList("Películas", movies))
        if (popular.isNotEmpty()) pages.add(HomePageList("Populares", popular))
        if (mostViewed.isNotEmpty()) pages.add(HomePageList("Más Vistos", mostViewed))

        Log.d("DLife-Main", "Total pages: ${pages.size}")
        
        return if (pages.isEmpty()) null else newHomePageResponse(pages, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("DLife-Search", "=== search START - query: '$query' ===")
        val searchUrl = "$mainUrl/search?search_api_fulltext=$query"
        Log.d("DLife-Search", "URL: $searchUrl")
        
        val html = app.get(searchUrl).text
        Log.d("DLife-Search", "Response length: ${html.length}")
        val doc = app.get(searchUrl).document
        
        val allItems = doc.select(".view-content .views-row .serie, .view-content .views-row .movie")
        Log.d("DLife-Search", "Items found with selector: ${allItems.size}")
        
        val results = allItems.mapNotNull { item ->
            val poster = item.getPosterUrl()
            val href = item.getSeriesLink()
            val title = item.getTitle()
            Log.d("DLife-Search", "  Item: title='$title', href='$href', poster='${poster != null}', isMovie=${item.hasClass("movie")}")
            if (href.isNullOrBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, if (item.hasClass("movie")) TvType.AnimeMovie else TvType.Anime) {
                this.posterUrl = poster
            }
        }
        
        Log.d("DLife-Search", "=== search END - returning ${results.size} results ===")
        return results
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
                Log.d("DonghuaLife", "Loading season: $seasonUrl (season $seasonNum)")
                
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
                    Log.e("DonghuaLife", "Failed to load season $seasonUrl: ${e.message}")
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
        Log.d("DonghuaLife", "loadLinks: $data")
        val doc = app.get(data).document
        
        var found = false
        
        doc.select(".embed-links li a").forEach { link ->
            val videoUrl = link.attr("data-video")
            val serverName = link.attr("title").takeIf { it.isNotBlank() } ?: link.text().trim()
            
            if (videoUrl.isNotBlank()) {
                Log.d("DonghuaLife", "Found server: $serverName, url: $videoUrl")
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