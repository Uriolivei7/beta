package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink

class DonghualifeProvider : MainAPI() {
    override var mainUrl = "https://donghualife.com"
    override var name = "DonghuaLife"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Anime
    )

    private fun fixTitle(title: String?): String {
        return title?.replace(Regex("\\s*\\|.*$"), "")?.trim() ?: ""
    }

    private fun extractYearFromDatetime(datetime: String?): String? {
        return datetime?.take(4)?.takeIf { it.all { c -> c.isDigit() } }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val doc = app.get("$mainUrl/").document

        val carousel = doc.select(".carousel-inner .views-row .banner").mapNotNull {
            val a = it.selectFirst("a") ?: return@mapNotNull null
            val title = fixTitle(a.selectFirst(".title")?.text())
            val poster = a.selectFirst("img")?.absUrl("src")
            val href = a.attr("abs:href")
            if (href.isBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        val latestEpisodes = doc.select(".view-episodios-home .views-row .episode").mapNotNull {
            val a = it.selectFirst("a") ?: return@mapNotNull null
            val title = fixTitle(a.selectFirst(".title")?.text())
            val poster = a.selectFirst("img")?.absUrl("src")
            val href = a.attr("abs:href")
            if (href.isBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        val movies = doc.select(".view-pelis-donghuas .views-row .movie").mapNotNull {
            val a = it.selectFirst("a") ?: return@mapNotNull null
            val title = fixTitle(a.selectFirst(".title")?.text())
            val poster = a.selectFirst("img")?.absUrl("src")
            val href = a.attr("abs:href")
            if (href.isBlank() || title.isBlank()) null else newMovieSearchResponse(title, href, TvType.AnimeMovie) {
                this.posterUrl = poster
            }
        }

        val sidebarItems = doc.select(".view-mas-populares .views-row, .view-mas-vistos-hoy .views-row").mapNotNull {
            val a = it.selectFirst("a") ?: return@mapNotNull null
            val title = fixTitle(a.selectFirst(".title")?.text())
            val poster = a.selectFirst("img")?.absUrl("src")
            val href = a.attr("abs:href")
            if (href.isBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
            }
        }

        val pages = mutableListOf<HomePageList>()
        if (carousel.isNotEmpty()) pages.add(HomePageList("Destacados", carousel, isHorizontalImages = true))
        if (latestEpisodes.isNotEmpty()) pages.add(HomePageList("Últimos Episodios", latestEpisodes))
        if (movies.isNotEmpty()) pages.add(HomePageList("Películas", movies))
        if (sidebarItems.isNotEmpty()) pages.add(HomePageList("Populares", sidebarItems))

        return newHomePageResponse(pages, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/search?search_api_fulltext=$query").document
        return doc.select("div.serie, div.movie").mapNotNull {
            val a = it.selectFirst("a") ?: return@mapNotNull null
            val title = fixTitle(a.selectFirst(".title")?.text())
            val poster = a.selectFirst("img")?.absUrl("src")
            val href = a.attr("abs:href")
            val isMovie = it.hasClass("movie")
            if (href.isBlank() || title.isBlank()) null else newAnimeSearchResponse(title, href, if (isMovie) TvType.AnimeMovie else TvType.Anime) {
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
        
        val isMovie = url.contains("/movie/") || url.contains("/movies/")
        
        val title = fixTitle(doc.selectFirst(".field--name-title")?.text())
            ?: fixTitle(doc.selectFirst("h2 a span")?.text())
            ?: "Sin título"
            
        val poster = doc.selectFirst(".field--name-field-poster img")?.absUrl("src")
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
            it.attr("abs:href").takeIf { href -> href.isNotBlank() }
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
                        val epHref = epLink.attr("abs:href")
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
                val epHref = a.attr("abs:href")
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