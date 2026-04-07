package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element
import android.util.Log
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink

class RetrotveProvider : MainAPI() {
    override var mainUrl = "https://retrotve.com"
    override var name = "RetroTVE"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Movie,
        TvType.Cartoon
    )

    override val mainPage = mainPageOf(
        "/series/" to "Series",
        "/peliculas/" to "Películas"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = "$mainUrl${request.data}?page=$page"
        Log.d("RetrotveProvider", "getMainPage: URL = $url")
        
        val document = app.get(url).document
        val home = ArrayList<SearchResponse>()
        val seenLinks = mutableSetOf<String>()
        
        document.select(".MovieList li, .TpSbList li, .MovieListSldCn li, .TPTblCn tbody tr").forEach { element ->
            val link = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")?.attr("href") ?: return@forEach
            if (seenLinks.contains(link)) return@forEach
            seenLinks.add(link)
            
            val title = element.selectFirst(".Title")?.text() 
                ?: element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")?.text()
                ?: return@forEach
            
            val poster = element.selectFirst("img")?.attr("src")
            
            if (title.isNotEmpty()) {
                val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                home.add(
                    newTvSeriesSearchResponse(title, link, tvType) {
                        this.posterUrl = poster
                    }
                )
            }
        }
        
        if (home.isEmpty()) {
            document.select(".Wdgt .MovieList a, .TpSbList a").forEach { element ->
                val link = element.attr("href") ?: return@forEach
                if (!link.contains("/serie/") && !link.contains("/pelicula/")) return@forEach
                if (seenLinks.contains(link)) return@forEach
                seenLinks.add(link)
                
                val title = element.selectFirst(".Title")?.text() ?: element.text()
                val poster = element.selectFirst("img")?.attr("src")
                
                if (title.isNotEmpty()) {
                    val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                    home.add(
                        newTvSeriesSearchResponse(title, link, tvType) {
                            this.posterUrl = poster
                        }
                    )
                }
            }
        }
        
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home,
                isHorizontalImages = false
            ),
            hasNext = home.size >= 20
        )
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val title = this.selectFirst(".Title")?.text() ?: return null
        val href = this.selectFirst("a[href]")?.attr("href") ?: return null
        val poster = this.selectFirst("img")?.attr("src")
        
        if (title.isEmpty() || href.isEmpty()) return null
        
        val tvType = if (href.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
        return newTvSeriesSearchResponse(title, href, tvType) {
            this.posterUrl = poster
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("RetrotveProvider", "search: query = $query")
        val document = app.get("$mainUrl/?s=$query").document
        val results = document.select(".MovieList li, .TPost, article.post, .search-results li").mapNotNull { element ->
            val link = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")?.attr("href") ?: return@mapNotNull null
            val title = element.selectFirst("h2, h3, .Title")?.text() 
                ?: element.selectFirst("img")?.attr("alt")
                ?: return@mapNotNull null
            val poster = element.selectFirst("img")?.attr("src")
            
            if (title.isEmpty()) return@mapNotNull null
            
            val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
            newTvSeriesSearchResponse(title, link, tvType) {
                this.posterUrl = poster
            }
        }
        Log.d("RetrotveProvider", "search: ${results.size} resultados")
        return results
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("RetrotveProvider", "load: url = $url")
        val document = app.get(url).document
        
        val title = document.selectFirst("h1.Title")?.text() 
            ?: document.selectFirst("h1")?.text()
            ?: return null
        
        val poster = document.selectFirst(".Image figure img")?.attr("src")
            ?: document.selectFirst("article img")?.attr("src")
        
        val description = document.selectFirst(".Description p")?.text()
        
        val year = document.selectFirst(".Date")?.text()?.filter { it.isDigit() }?.take(4)?.toIntOrNull()
        
        val isMovie = url.contains("/pelicula/")
        
        val episodes = ArrayList<Episode>()
        
        if (!isMovie) {
            document.select(".TPTblCn tbody tr, .Wdgt.AABox tbody tr").forEach { row ->
                val episodeUrl = row.selectFirst("a[href*='/seriestv/']")?.attr("href") ?: return@forEach
                val episodeName = row.selectFirst(".MvTbTtl a")?.text() 
                    ?: row.selectFirst("td:nth-child(3) a")?.text()
                    ?: "Episodio"
                val episodeNum = row.selectFirst(".Num")?.text()?.toIntOrNull()
                    ?: row.selectFirst("td:first-child span")?.text()?.toIntOrNull()
                
                val seasonMatch = Regex("""temporada(\d+)""").find(episodeUrl)
                val season = seasonMatch?.groupValues?.getOrNull(1)?.toIntOrNull() ?: 1
                
                episodes.add(newEpisode(episodeUrl) {
                    this.name = episodeName
                    this.episode = episodeNum ?: episodes.size + 1
                    this.season = season
                })
            }
            
            if (episodes.isEmpty()) {
                document.select(".episode-list li a, .episodes a").forEach { element ->
                    val episodeUrl = element.attr("href") ?: return@forEach
                    val episodeName = element.text()
                    episodes.add(newEpisode(episodeUrl) {
                        this.name = episodeName
                    })
                }
            }
        }
        
        val recommendations = document.select(".TpSbList li, .Relacionados li").mapNotNull { element ->
            val recUrl = element.selectFirst("a[href]")?.attr("href") ?: return@mapNotNull null
            val recTitle = element.selectFirst(".Title")?.text() ?: return@mapNotNull null
            val recPoster = element.selectFirst("img")?.attr("src")
            
            if (recUrl.isEmpty() || recTitle.isEmpty()) return@mapNotNull null
            
            val recType = if (recUrl.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
            newTvSeriesSearchResponse(recTitle, recUrl, recType) {
                this.posterUrl = recPoster
            }
        }
        
        return if (isMovie) {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.recommendations = recommendations
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.recommendations = recommendations
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("RetrotveProvider", "loadLinks: data = $data")
        var linksFound = false
        val res = app.get(data).document

        // Extract all iframe sources from TPlayerTb divs
        res.select(".TPlayerTb iframe, .TPlayer iframe, iframe[src*='trembed'], iframe[src*='retrotve']").forEach { element ->
            val rawLink = element.attr("src")
            if (rawLink.isNullOrBlank()) {
                Log.w("RetrotveProvider", "Empty iframe source found")
                return@forEach
            }
            
            Log.d("RetrotveProvider", "loadLinks: iframe src = $rawLink")
            
            // If the iframe is a retrotve player URL, we need to extract the actual video
            if (rawLink.contains("trembed") && rawLink.contains("retrotve.com")) {
                // This is an internal player URL - try to load it and extract video
                try {
                    val playerPage = app.get(rawLink).document
                    
                    // Look for video sources in the player page
                    playerPage.select("iframe[src]").forEach { playerIframe ->
                        val playerSrc = playerIframe.attr("src")
                        if (!playerSrc.isNullOrBlank() && !playerSrc.contains("retrotve.com")) {
                            val link = fixHostsLinks(playerSrc)
                            Log.d("RetrotveProvider", "loadLinks: player iframe src = $link")
                            val success = loadExtractor(link, rawLink, subtitleCallback, callback)
                            if (success) {
                                linksFound = true
                            }
                        }
                    }
                    
                    // Also check for direct video patterns
                    val videoPatterns = listOf(
                        Regex("""file\s*:\s*["']([^"']+\.(m3u8|mp4|webm)[^"']*)["']"""),
                        Regex("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']"""),
                        Regex("""videoUrl\s*[:=]\s*["']([^"']+)["']"""),
                        Regex("""video\s*:\s*["']([^"']+)["']""")
                    )
                    
                    for (pattern in videoPatterns) {
                        pattern.findAll(playerPage.html()).forEach { match ->
                            val videoUrl = match.groupValues[1]
                            if (videoUrl.startsWith("http") && (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4"))) {
                                Log.d("RetrotveProvider", "loadLinks: direct video = $videoUrl")
                                callback.invoke(
                                    newExtractorLink(
                                        source = "RetroTVE",
                                        name = "RetroTVE Video",
                                        url = videoUrl,
                                        type = ExtractorLinkType.M3U8
                                    ) {
                                        referer = rawLink
                                        quality = Qualities.Unknown.value
                                    }
                                )
                                linksFound = true
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("RetrotveProvider", "Error loading player page: ${e.message}")
                }
            } else {
                // External iframe - use standard extraction
                val link = fixHostsLinks(rawLink)
                val success = loadExtractor(link, data, subtitleCallback, callback)
                if (success) {
                    linksFound = true
                }
            }
        }

        // Fallback: look for video patterns directly on the page
        val videoPattern = Regex("""file\s*:\s*["']([^"']+)["']""")
        videoPattern.findAll(res.html()).forEach { match ->
            val videoUrl = match.groupValues[1]
            if (videoUrl.startsWith("http")) {
                Log.d("RetrotveProvider", "loadLinks: video file = $videoUrl")
                callback.invoke(
                    newExtractorLink(
                        source = "RetroTVE",
                        name = "RetroTVE",
                        url = videoUrl,
                        type = ExtractorLinkType.M3U8
                    ) {
                        referer = data
                        quality = Qualities.Unknown.value
                    }
                )
                linksFound = true
            }
        }
        
        val jsPlayerPattern = Regex("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']""")
        jsPlayerPattern.findAll(res.html()).forEach { match ->
            val videoUrl = match.groupValues[1]
            if (videoUrl.startsWith("http")) {
                Log.d("RetrotveProvider", "loadLinks: js player = $videoUrl")
                callback.invoke(
                    newExtractorLink(
                        source = "RetroTVE",
                        name = "RetroTVE",
                        url = videoUrl,
                        type = ExtractorLinkType.M3U8
                    ) {
                        referer = data
                        quality = Qualities.Unknown.value
                    }
                )
                linksFound = true
            }
        }
        
        return linksFound
    }
}

fun fixHostsLinks(url: String): String {
    return url
        .replaceFirst("https://hglink.to", "https://streamwish.to")
        .replaceFirst("https://swdyu.com", "https://streamwish.to")
        .replaceFirst("https://cybervynx.com", "https://streamwish.to")
        .replaceFirst("https://dumbalag.com", "https://streamwish.to")
        .replaceFirst("https://mivalyo.com", "https://vidhidepro.com")
        .replaceFirst("https://dinisglows.com", "https://vidhidepro.com")
        .replaceFirst("https://dhtpre.com", "https://vidhidepro.com")
        .replaceFirst("https://filemoon.link", "https://filemoon.sx")
        .replaceFirst("https://sblona.com", "https://watchsb.com")
        .replaceFirst("https://lulu.st", "https://lulustream.com")
        .replaceFirst("https://uqload.io", "https://uqload.com")
        .replaceFirst("https://uqload.cx", "https://uqload.com")
        .replaceFirst("https://do7go.com", "https://dood.la")
        .replaceFirst("https://short.ink/", "https://")
}
