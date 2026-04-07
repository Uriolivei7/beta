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
            "$mainUrl${request.data.removeSuffix("/")}/page/$page/"
        }
        Log.d("RetrotveProvider", "getMainPage: URL = $url")
        
        val document = app.get(url).document
        val home = ArrayList<SearchResponse>()
        val seenLinks = mutableSetOf<String>()
        
        document.select(".TpRwCont .MovieList > li, .TpRwCont .MovieList .TPostMv, section .MovieList > li").forEach { element ->
            val linkElement = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")
            val link = linkElement?.attr("href") ?: return@forEach
            if (seenLinks.contains(link)) return@forEach
            seenLinks.add(link)
            
            val title = element.selectFirst(".Title")?.text() ?: element.selectFirst("h3")?.text() ?: return@forEach
            val poster = element.selectFirst(".Image img")?.attr("src")
            
            if (title.isNotEmpty() && title.length > 2) {
                val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                home.add(
                    newTvSeriesSearchResponse(title, link, tvType) {
                        this.posterUrl = poster
                    }
                )
            }
        }
        
        Log.d("RetrotveProvider", "getMainPage: found ${home.size} items")
        
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
        val results = ArrayList<SearchResponse>()
        val seenLinks = mutableSetOf<String>()
        
        document.select("section .MovieList > li, section .TPostMv, .TpRwCont main .MovieList > li").forEach { element ->
            val link = element.selectFirst("a[href*='/serie/'], a[href*='/pelicula/']")?.attr("href") ?: return@forEach
            
            if (!link.contains("/serie/") && !link.contains("/pelicula/")) return@forEach
            if (seenLinks.contains(link)) return@forEach
            seenLinks.add(link)
            
            val title = element.selectFirst(".Title")?.text() ?: element.selectFirst("h3")?.text() ?: return@forEach
            val poster = element.selectFirst(".Image img")?.attr("src")
            
            if (title.isEmpty() || title.length < 3) return@forEach
            
            val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
            results.add(
                newTvSeriesSearchResponse(title, link, tvType) {
                    this.posterUrl = poster
                }
            )
        }
        Log.d("RetrotveProvider", "search: ${results.size} resultados")
        return results
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("RetrotveProvider", "load: url = $url")
        val document = app.get(url).document
        
        val title = document.selectFirst("h1.Title, h1")?.text() ?: return null
        val poster = document.selectFirst(".TPost.Single .Image img, .TPostBg img, .poster img, article img")?.attr("src")
            ?: document.selectFirst("meta[property='og:image']")?.attr("content")
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
        
        val recommendations = document.select(".TpSbList li, .Relacionados li").mapNotNull { element ->
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

        val tridMatch = Regex("""trid=(\d+)""").find(data)
        val trtypeMatch = Regex("""trtype=(\d+)""").find(data)
        val trid = tridMatch?.groupValues?.get(1) ?: "0"
        val trtype = trtypeMatch?.groupValues?.get(1) ?: "2"
        
        for (trembed in listOf("0", "1", "2", "3")) {
            val trembedUrl = "$mainUrl/?trembed=$trembed&trid=$trid&trtype=$trtype"
            Log.d("RetrotveProvider", "Trying trembed=$trembed: $trembedUrl")
            
            try {
                val playerPage = app.get(trembedUrl, referer = "$mainUrl/").document
                var thisServerFound = false
                
                playerPage.select("script").forEach { script ->
                    val scriptContent = script.html()
                    
                    val oidPattern = Regex("""(?:owner_id|oid)\s*[=:]\s*["']?(-?\d+)["']?""")
                    val vidPattern = Regex("""(?:video_id|vid)\s*[=:]\s*["']?(\d+)["']?""")
                    
                    val oidMatch = oidPattern.find(scriptContent)
                    val vidMatch = vidPattern.find(scriptContent)
                    
                    if (oidMatch != null && vidMatch != null) {
                        val oid = oidMatch.groupValues[1]
                        val vid = vidMatch.groupValues[1]
                        Log.d("RetrotveProvider", "Found VK: oid=$oid, video_id=$vid")
                        val vkUrl = "https://vk.com/video_ext.php?oid=$oid&video_id=$vid"
                        callback.invoke(
                            newExtractorLink(
                                source = "VKVideo",
                                name = "VKVideo (trembed=$trembed)",
                                url = vkUrl,
                                type = ExtractorLinkType.M3U8
                            ) {
                                referer = "$mainUrl/"
                                quality = Qualities.Unknown.value
                            }
                        )
                        linksFound = true
                        thisServerFound = true
                    }
                    
                    Regex("""vk\.com/video_ext\.php\?oid=-?\d+&video_id=\d+""").find(scriptContent)?.let { match ->
                        Log.d("RetrotveProvider", "Found complete VK URL: ${match.value}")
                        callback.invoke(
                            newExtractorLink(
                                source = "VKVideo",
                                name = "VKVideo (trembed=$trembed)",
                                url = match.value,
                                type = ExtractorLinkType.M3U8
                            ) {
                                referer = "$mainUrl/"
                                quality = Qualities.Unknown.value
                            }
                        )
                        linksFound = true
                        thisServerFound = true
                    }
                }
                
                playerPage.select("iframe[src]").forEach { playerIframe ->
                    val playerSrc = playerIframe.attr("src")
                    if (playerSrc.isNullOrBlank() || playerSrc.contains("retrotve.com")) return@forEach
                    
                    Log.d("RetrotveProvider", "loadLinks: player iframe = $playerSrc")
                    
                    if (playerSrc.contains("vk.com") || playerSrc.contains("vkvideo")) {
                        callback.invoke(
                            newExtractorLink(
                                source = "VKVideo",
                                name = "VKVideo (trembed=$trembed)",
                                url = playerSrc,
                                type = ExtractorLinkType.M3U8
                            ) {
                                referer = "$mainUrl/"
                                quality = Qualities.Unknown.value
                            }
                        )
                        linksFound = true
                        thisServerFound = true
                    } else if (playerSrc.contains("filemoon")) {
                        val link = fixHostsLinks(playerSrc)
                        callback.invoke(
                            newExtractorLink(
                                source = "Filemoon",
                                name = "Filemoon (trembed=$trembed)",
                                url = link,
                                type = ExtractorLinkType.VIDEO
                            ) {
                                referer = "$mainUrl/"
                                quality = Qualities.Unknown.value
                            }
                        )
                        linksFound = true
                        thisServerFound = true
                    } else {
                        val success = loadExtractor(playerSrc, "$mainUrl/", subtitleCallback, callback)
                        if (success) {
                            linksFound = true
                            thisServerFound = true
                        }
                    }
                }
                
                if (!thisServerFound) {
                    Regex("""file\s*:\s*["']([^"']+\.(?:m3u8|mp4)[^"']*)["']""").find(playerPage.html())?.let { match ->
                        val videoUrl = match.groupValues[1]
                        Log.d("RetrotveProvider", "Found direct video: $videoUrl")
                        val linkType = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                        callback.invoke(
                            newExtractorLink(
                                source = "RetroTVE",
                                name = "Video (trembed=$trembed)",
                                url = videoUrl,
                                type = linkType
                            ) {
                                referer = "$mainUrl/"
                                quality = Qualities.Unknown.value
                            }
                        )
                        linksFound = true
                    }
                }
                
            } catch (e: Exception) {
                Log.e("RetrotveProvider", "Error loading trembed $trembed: ${e.message}")
            }
        }

        res.select(".TPlayerTb iframe, .TPlayer iframe, iframe[src*='retrotve']:not([src*='trembed'])").forEach { element ->
            val rawLink = element.attr("src")
            if (rawLink.isNullOrBlank()) return@forEach
            
            Log.d("RetrotveProvider", "loadLinks: direct iframe = $rawLink")
            
            if (rawLink.contains("vk.com") || rawLink.contains("vkvideo")) {
                callback.invoke(
                    newExtractorLink(
                        source = "VKVideo",
                        name = "VKVideo",
                        url = rawLink,
                        type = ExtractorLinkType.M3U8
                    ) {
                        referer = "$mainUrl/"
                        quality = Qualities.Unknown.value
                    }
                )
                linksFound = true
            } else {
                val link = fixHostsLinks(rawLink)
                val success = loadExtractor(link, "$mainUrl/", subtitleCallback, callback)
                if (success) {
                    linksFound = true
                } else {
                    callback.invoke(
                        newExtractorLink(
                            source = getBaseName(link),
                            name = getBaseName(link),
                            url = link,
                            type = ExtractorLinkType.VIDEO
                        ) {
                            referer = "$mainUrl/"
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
