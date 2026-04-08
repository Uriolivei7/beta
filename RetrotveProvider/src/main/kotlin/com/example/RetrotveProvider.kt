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

    private suspend fun extractSendvid(url: String, referer: String, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        return try {
            val page = app.get(url, referer = referer).text
            val videoId = Regex("""video\s*:\s*["']([^"']+)["']""").find(page)?.groupValues?.get(1)
                ?: Regex(""""video_url"\s*:\s*"([^"]+)"""").find(page)?.groupValues?.get(1)
            
            if (videoId != null) {
                val videoUrl = if (videoId.startsWith("http")) videoId else "https://sendvid.com/$videoId"
                callback(
                    newExtractorLink(
                        source = "Sendvid",
                        name = "Sendvid",
                        url = videoUrl,
                        type = ExtractorLinkType.VIDEO
                    ) {
                        this.referer = "https://sendvid.com/"
                    }
                )
                Log.d("RetrotveProvider", "Sendvid: Found video URL: $videoUrl")
                return true
            }
            
            Log.d("RetrotveProvider", "Sendvid: No video found in page, using generic extractor")
            loadExtractor(url, referer, subtitleCallback, callback)
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "Sendvid error: ${e.message}")
            loadExtractor(url, referer, subtitleCallback, callback)
        }
    }

    private suspend fun extractFilemoon(url: String, referer: String, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        return try {
            val doc = app.get(url, referer = referer).document
            val script = doc.select("script:containsData(sources:)").firstOrNull()?.html()
                ?: doc.select("script").firstOrNull { it.html().contains("sources") }?.html()
            
            if (script != null) {
                val srcMatch = Regex("""src\s*:\s*["']([^"']+)["']""").find(script)
                val videoUrl = srcMatch?.groupValues?.get(1)
                
                if (videoUrl != null && (videoUrl.contains(".mp4") || videoUrl.contains(".m3u8"))) {
                    callback(
                        newExtractorLink(
                            source = "Filemoon",
                            name = "Filemoon",
                            url = videoUrl,
                            type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                        )
                    )
                    Log.d("RetrotveProvider", "Filemoon: Found video URL: $videoUrl")
                    return true
                }
            }
            
            Log.d("RetrotveProvider", "Filemoon: No video found, using generic extractor")
            loadExtractor(url, referer, subtitleCallback, callback)
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "Filemoon error: ${e.message}")
            loadExtractor(url, referer, subtitleCallback, callback)
        }
    }

    private suspend fun processPlayerPage(
        playerUrl: String,
        referer: String,
        serverName: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("RetrotveProvider", "processPlayerPage: $playerUrl (server: $serverName)")
        
        return try {
            val playerDoc = app.get(playerUrl, referer = referer).document
            var found = false
            
            playerDoc.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("src") ?: return@forEach
                if (src.isBlank()) return@forEach
                
                val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                Log.d("RetrotveProvider", "Found iframe: $fixedSrc")
                
                var extractorFound = false
                
                when {
                    fixedSrc.contains("sendvid.com") -> {
                        Log.d("RetrotveProvider", "-> Using custom Sendvid extractor")
                        extractorFound = extractSendvid(fixedSrc, playerUrl, subtitleCallback, callback)
                    }
                    fixedSrc.contains("filemoon.") || fixedSrc.contains("filemoon.to") -> {
                        Log.d("RetrotveProvider", "-> Using custom Filemoon extractor")
                        extractorFound = extractFilemoon(fixedSrc, playerUrl, subtitleCallback, callback)
                    }
                    fixedSrc.contains("ok.ru") || fixedSrc.contains("odnoklassniki") -> {
                        Log.d("RetrotveProvider", "-> Using OK.RU extractor")
                        extractorFound = loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)
                    }
                    fixedSrc.contains("vk.com") || fixedSrc.contains("vkvideo") -> {
                        Log.d("RetrotveProvider", "-> Using VKVideo extractor")
                        extractorFound = loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)
                    }
                    else -> {
                        Log.d("RetrotveProvider", "-> Using generic extractor for: $fixedSrc")
                        extractorFound = loadExtractor(fixedSrc, playerUrl, subtitleCallback, callback)
                    }
                }
                
                if (extractorFound) {
                    found = true
                    Log.d("RetrotveProvider", "Extractor returned links successfully")
                } else {
                    Log.d("RetrotveProvider", "Extractor returned no links")
                }
            }
            
            Log.d("RetrotveProvider", "processPlayerPage result: found=$found")
            found
        } catch (e: Exception) {
            Log.e("RetrotveProvider", "processPlayerPage error: ${e.message}")
            e.printStackTrace()
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
        val trtype = if (data.contains("/pelicula/")) "1" else "2"
        
        Log.d("RetrotveProvider", "Searching for player URLs in episode page")
        
        val trembedUrls = mutableListOf<Pair<String, String>>()
        
        var correctTrid: String? = null
        
        document.select("iframe[src*='trembed']").forEach { iframe ->
            val src = iframe.attr("src")
            if (src.contains("trembed") && src.contains("trid")) {
                val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                val tridMatch = Regex("""trid=(\d+)""").find(fixedSrc)
                tridMatch?.groupValues?.get(1)?.let { trid ->
                    if (correctTrid == null) {
                        correctTrid = trid
                        Log.d("RetrotveProvider", "Found correct trid from iframe: $trid")
                    }
                }
                trembedUrls.add(fixedSrc to "Iframe")
                Log.d("RetrotveProvider", "Found trembed iframe: $fixedSrc")
            }
        }
        
        document.select(".TPlayerTb[id], .TPlayer[id]").forEach { tab ->
            val tabId = tab.attr("id")
            
            tab.select("iframe[src]").forEach { iframe ->
                val src = iframe.attr("src")
                if (src.contains("retrotve.com") && src.contains("trembed") && src.contains("trid")) {
                    val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                    val tridMatch = Regex("""trid=(\d+)""").find(fixedSrc)
                    tridMatch?.groupValues?.get(1)?.let { trid ->
                        if (correctTrid == null) {
                            correctTrid = trid
                            Log.d("RetrotveProvider", "Found correct trid from tab $tabId: $trid")
                        }
                    }
                    if (!trembedUrls.any { it.first == fixedSrc }) {
                        trembedUrls.add(fixedSrc to tabId)
                        Log.d("RetrotveProvider", "Found trembed iframe in $tabId: $fixedSrc")
                    }
                }
            }
            
            tab.select("div[data-src*='trembed']").forEach { div ->
                val src = div.attr("data-src")
                if (src.contains("trembed") && src.contains("trid")) {
                    val fixedSrc = if (src.startsWith("//")) "https:$src" else src
                    val tridMatch = Regex("""trid=(\d+)""").find(fixedSrc)
                    tridMatch?.groupValues?.get(1)?.let { trid ->
                        if (correctTrid == null) {
                            correctTrid = trid
                            Log.d("RetrotveProvider", "Found correct trid from data-src: $trid")
                        }
                    }
                    if (!trembedUrls.any { it.first == fixedSrc }) {
                        trembedUrls.add(fixedSrc to tabId)
                        Log.d("RetrotveProvider", "Found trembed data-src in $tabId: $fixedSrc")
                    }
                }
            }
        }
        
        if (correctTrid != null) {
            Log.d("RetrotveProvider", "Using correct trid: $correctTrid, trying all trembed options")
            
            for (trembed in 0..5) {
                val playerUrl = "$mainUrl/?trembed=$trembed&trid=$correctTrid&trtype=$trtype"
                if (!trembedUrls.any { it.first.contains("trembed=$trembed&") && it.first.contains("trid=$correctTrid") }) {
                    trembedUrls.add(playerUrl to "Opt${trembed + 1}")
                    Log.d("RetrotveProvider", "Generated trembed $trembed: $playerUrl")
                }
            }
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
