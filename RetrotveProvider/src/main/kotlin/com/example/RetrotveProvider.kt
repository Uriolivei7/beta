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

    private suspend fun processPlayerPage(
        playerUrl: String,
        referer: String,
        serverName: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("RetrotveProvider", "processPlayerPage: $playerUrl")
        
        return try {
            val playerDoc = app.get(playerUrl, referer = referer).document
            var found = false
            
            playerDoc.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("src") ?: return@forEach
                if (src.isBlank()) return@forEach
                
                val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                Log.d("RetrotveProvider", "Found iframe in player: $fixedSrc")
                
                if (fixedSrc.contains("ok.ru") || fixedSrc.contains("odnoklassniki")) {
                    Log.d("RetrotveProvider", "-> Using OK.RU extractor")
                    try {
                        if (loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)) found = true
                    } catch (e: Exception) {
                        Log.e("RetrotveProvider", "OK.RU error: ${e.message}")
                    }
                } else if (fixedSrc.contains("yourupload.com")) {
                    Log.d("RetrotveProvider", "-> Using YourUpload extractor")
                    try {
                        if (loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)) found = true
                    } catch (e: Exception) {
                        Log.e("RetrotveProvider", "YourUpload error: ${e.message}")
                    }
                } else if (fixedSrc.contains("mega.nz") || fixedSrc.contains("mega.")) {
                    Log.d("RetrotveProvider", "-> Using Mega extractor")
                    try {
                        if (loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)) found = true
                    } catch (e: Exception) {
                        Log.e("RetrotveProvider", "Mega error: ${e.message}")
                    }
                } else if (fixedSrc.contains("uqload.com") || fixedSrc.contains("uqload.")) {
                    Log.d("RetrotveProvider", "-> Using Uqload extractor")
                    try {
                        if (loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)) found = true
                    } catch (e: Exception) {
                        Log.e("RetrotveProvider", "Uqload error: ${e.message}")
                    }
                } else if (fixedSrc.contains("gdriveplayer")) {
                    Log.d("RetrotveProvider", "-> Using GDrivePlayer extractor")
                    try {
                        if (loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)) found = true
                    } catch (e: Exception) {
                        Log.e("RetrotveProvider", "GDrivePlayer error: ${e.message}")
                    }
                } else {
                    Log.d("RetrotveProvider", "-> Using generic extractor")
                    try {
                        if (loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)) found = true
                    } catch (e: Exception) {
                        Log.e("RetrotveProvider", "Generic extractor error: ${e.message}")
                    }
                }
            }
            
            found
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "processPlayerPage error: ${e.message}")
            false
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
        
        val document = app.get(data).document
        
        Log.d("RetrotveProvider", "Searching for player URLs in episode page")
        
        val trembedUrls = mutableListOf<Pair<String, String>>()
        
        val trid = document.selectFirst("article[data-tr-post-id]")?.attr("data-tr-post-id") ?: 
                   Regex("""trid[=&](\d+)""").find(data)?.groupValues?.get(1) ?: "0"
        val trtype = if (data.contains("/pelicula/")) "1" else "2"
        
        Log.d("RetrotveProvider", "trid=$trid, trtype=$trtype")
        
        val tabIds = listOf("Opt1", "Opt2", "Opt3", "Opt4", "Opt5")
        
        document.select(".TPlayerTb[id], .TPlayer[id]").forEach { tab ->
            val tabId = tab.attr("id")
            Log.d("RetrotveProvider", "Found tab: $tabId")
            
            tab.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("src") ?: return@forEach
                if (src.isBlank()) return@forEach
                
                if (src.contains("retrotve.com") && src.contains("trembed")) {
                    val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                    trembedUrls.add(fixedSrc to tabId)
                    Log.d("RetrotveProvider", "Found trembed iframe in $tabId: $fixedSrc")
                }
            }
            
            tab.select("div[data-src]").forEach { div ->
                val src = div.attr("data-src") ?: return@forEach
                if (src.isBlank()) return@forEach
                
                if (src.contains("trembed")) {
                    trembedUrls.add(src to tabId)
                    Log.d("RetrotveProvider", "Found trembed data-src in $tabId: $src")
                }
            }
        }
        
        val visibleIframes = document.select("iframe[src]").filter { iframe ->
            val src = iframe.attr("src") ?: ""
            !src.contains("google") && !src.contains("facebook") && !src.contains("twitter")
        }
        
        visibleIframes.forEach { iframe ->
            val src = iframe.attr("src") ?: return@forEach
            if (src.isBlank()) return@forEach
            
            if (src.contains("retrotve.com") && src.contains("trembed")) {
                val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                if (!trembedUrls.any { it.first == fixedSrc }) {
                    trembedUrls.add(fixedSrc to "Visible")
                    Log.d("RetrotveProvider", "Found visible trembed iframe: $fixedSrc")
                }
            }
        }
        
        val tabIndex = tabIds.indexOfFirst { tabId -> 
            document.selectFirst("#$tabId") != null 
        }
        
        val missingTrembed = (0..3).filter { index ->
            val trembedValue = if (tabIndex >= 0) tabIndex + index else index
            !trembedUrls.any { it.first.contains("trembed=$index&") || it.first.contains("trembed=$trembedValue&") }
        }
        
        missingTrembed.forEach { index ->
            val playerUrl = "$mainUrl/?trembed=$index&trid=$trid&trtype=$trtype"
            trembedUrls.add(playerUrl to "Generated")
            Log.d("RetrotveProvider", "Generated trembed $index: $playerUrl")
        }
        
        val uniqueTrembedUrls = trembedUrls.distinctBy { it.first }
        Log.d("RetrotveProvider", "Found ${uniqueTrembedUrls.size} unique trembed URLs to process")
        
        uniqueTrembedUrls.forEach { (playerUrl, serverName) ->
            val found = processPlayerPage(playerUrl, data, serverName, subtitleCallback, callback)
            if (found) linksFound = true
        }
        
        Log.d("RetrotveProvider", "loadLinks result: linksFound=$linksFound")
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
