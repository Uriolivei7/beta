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
        "/category/animacion/?tr_post_type=2" to "Series Animadas",
        "/category/animacion/?tr_post_type=1" to "Películas"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val url = if (page == 1) {
            "$mainUrl${request.data}"
        } else {
            "$mainUrl${request.data.replace("?", "/?")}page/$page/"
        }
        Log.d("RetrotveProvider", "getMainPage: URL = $url")
        
        val document = app.get(url).document
        val home = ArrayList<SearchResponse>()
        val seenLinks = mutableSetOf<String>()
        
        document.select(".MovieList li, .TPostMv").forEach { element ->
            val linkElement = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")
            val link = linkElement?.attr("href") ?: return@forEach
            if (seenLinks.contains(link)) return@forEach
            seenLinks.add(link)
            
            val title = element.selectFirst(".Title")?.text() ?: return@forEach
            val poster = element.selectFirst(".Image img")?.attr("src")
            
            if (title.isNotEmpty()) {
                val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                home.add(
                    newTvSeriesSearchResponse(title, link, tvType) {
                        this.posterUrl = poster
                    }
                )
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

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("RetrotveProvider", "search: query = $query")
        val document = app.get("$mainUrl/?s=$query").document
        val results = document.select(".MovieList li, .TPostMv, article.TPost").mapNotNull { element ->
            val link = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")?.attr("href") ?: return@mapNotNull null
            
            if (!link.contains("/serie/") && !link.contains("/pelicula/")) return@mapNotNull null
            
            val title = element.selectFirst(".Title, h2, h3")?.text() ?: return@mapNotNull null
            val poster = element.selectFirst(".Image img")?.attr("src")
            
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
        
        val title = document.selectFirst("h1.Title, h1")?.text() ?: return null
        val poster = document.selectFirst(".TPostBg .Image img, .poster img")?.attr("src")
        val description = document.selectFirst(".Description p")?.text()
        
        val year = document.selectFirst(".Year")?.text()?.filter { it.isDigit() }?.take(4)?.toIntOrNull()
        
        val isMovie = url.contains("/pelicula/")
        
        val episodes = ArrayList<Episode>()
        
        if (!isMovie) {
            document.select(".Wdgt.AABox").forEach { seasonBox ->
                val seasonTitle = seasonBox.selectFirst(".Title.AA-Season span")?.text()
                    ?: seasonBox.selectFirst(".Title")?.text()
                val seasonNum = seasonTitle?.filter { it.isDigit() }?.toIntOrNull() ?: 1
                
                Log.d("RetrotveProvider", "Found season: $seasonNum")
                
                seasonBox.select(".TPTblCn tbody tr").forEach { row ->
                    val episodeUrl = row.selectFirst("a[href*='/seriestv/']")?.attr("href") ?: return@forEach
                    val episodeName = row.selectFirst(".MvTbTtl a")?.text() ?: "Episodio"
                    val episodeNum = row.selectFirst(".Num")?.text()?.toIntOrNull()
                    
                    episodes.add(newEpisode(episodeUrl) {
                        this.name = episodeName
                        this.episode = episodeNum ?: episodes.size + 1
                        this.season = seasonNum
                    })
                }
            }
            
            if (episodes.isEmpty()) {
                document.select(".TPTblCn tbody tr").forEach { row ->
                    val episodeUrl = row.selectFirst("a[href*='/seriestv/']")?.attr("href") ?: return@forEach
                    val episodeName = row.selectFirst(".MvTbTtl a")?.text() ?: "Episodio"
                    val episodeNum = row.selectFirst(".Num")?.text()?.toIntOrNull()
                    
                    episodes.add(newEpisode(episodeUrl) {
                        this.name = episodeName
                        this.episode = episodeNum ?: episodes.size + 1
                    })
                }
            }
        }
        
        val recommendations = document.select(".TpSbList li, .Relacionados li, .MovieList li").mapNotNull { element ->
            val recUrl = element.selectFirst("a[href]")?.attr("href") ?: return@mapNotNull null
            if (!recUrl.contains("/serie/") && !recUrl.contains("/pelicula/")) return@mapNotNull null
            val recTitle = element.selectFirst(".Title")?.text() ?: return@mapNotNull null
            val recPoster = element.selectFirst("img")?.attr("src")
            
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

        res.select(".TPlayerTb iframe, .TPlayer iframe, iframe[src*='trembed'], iframe[src*='retrotve']").forEach { element ->
            val rawLink = element.attr("src")
            if (rawLink.isNullOrBlank()) {
                Log.w("RetrotveProvider", "Empty iframe source found")
                return@forEach
            }
            
            Log.d("RetrotveProvider", "loadLinks: iframe src = $rawLink")
            
            if (rawLink.contains("trembed") && rawLink.contains("retrotve.com")) {
                try {
                    val playerPage = app.get(rawLink).document
                    
                    playerPage.select("iframe[src]").forEach { playerIframe ->
                        val playerSrc = playerIframe.attr("src")
                        if (!playerSrc.isNullOrBlank() && !playerSrc.contains("retrotve.com")) {
                            val link = fixHostsLinks(playerSrc)
                            Log.d("RetrotveProvider", "loadLinks: player iframe src = $link")
                            val success = loadExtractor(link, rawLink, subtitleCallback, callback)
                            if (success) {
                                linksFound = true
                            } else {
                                callback.invoke(
                                    newExtractorLink(
                                        source = getBaseName(link),
                                        name = getBaseName(link),
                                        url = link,
                                        type = ExtractorLinkType.M3U8
                                    ) {
                                        referer = "$mainUrl/"
                                        quality = Qualities.Unknown.value
                                    }
                                )
                                linksFound = true
                            }
                        }
                    }
                    
                    val videoPatterns = listOf(
                        Regex("""file\s*:\s*["']([^"']+\.(m3u8|mp4|webm)[^"']*)["']"""),
                        Regex("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']"""),
                        Regex("""videoUrl\s*[:=]\s*["']([^"']+)["']"""),
                        Regex("""["']([^"']+\.m3u8[^"']*)["']"""),
                        Regex("""url\s*:\s*["']([^"']+\.(m3u8|mp4)[^"']*)["']""")
                    )
                    
                    for (pattern in videoPatterns) {
                        pattern.findAll(playerPage.html()).forEach { match ->
                            val videoUrl = match.groupValues[1]
                            if (videoUrl.startsWith("http") && (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4") || videoUrl.contains(".webm"))) {
                                Log.d("RetrotveProvider", "loadLinks: direct video = $videoUrl")
                                callback.invoke(
                                    newExtractorLink(
                                        source = "RetroTVE",
                                        name = "RetroTVE Video",
                                        url = videoUrl,
                                        type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
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
                val link = fixHostsLinks(rawLink)
                val success = loadExtractor(link, data, subtitleCallback, callback)
                if (success) {
                    linksFound = true
                } else {
                    callback.invoke(
                        newExtractorLink(
                            source = getBaseName(link),
                            name = getBaseName(link),
                            url = link,
                            type = ExtractorLinkType.M3U8
                        ) {
                            referer = "$mainUrl/"
                            quality = Qualities.Unknown.value
                        }
                    )
                    linksFound = true
                }
            }
        }

        listOf(
            Regex("""file\s*:\s*["']([^"']+)["']"""),
            Regex("""sources\s*:\s*\[\s*\{\s*file\s*:\s*["']([^"']+)["']""")
        ).forEach { pattern ->
            pattern.findAll(res.html()).forEach { match ->
                val videoUrl = match.groupValues[1]
                if (videoUrl.startsWith("http") && (videoUrl.contains(".m3u8") || videoUrl.contains(".mp4"))) {
                    Log.d("RetrotveProvider", "loadLinks: video file = $videoUrl")
                    callback.invoke(
                        newExtractorLink(
                            source = "RetroTVE",
                            name = "RetroTVE",
                            url = videoUrl,
                            type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                        ) {
                            referer = data
                            quality = Qualities.Unknown.value
                        }
                    )
                    linksFound = true
                }
            }
        }
        
        return linksFound
    }
}

fun getBaseName(url: String): String {
    return url.substringAfter("://").substringBefore("/").substringBefore(".")
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
