package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.Calendar

class AsialiveactionProvider : MainAPI() {

    override var mainUrl = "https://asialiveaction.com"
    override var name = "AsiaLiveAction"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.AsianDrama)

    private fun getHdImg(url: String?): String? {
        if (url.isNullOrEmpty() || !url.contains("tmdb")) return url
        return url.replace(Regex("""(https://image\.tmdb\.org/t/p/)[\w_]+(/[^\s]*)""")) { 
            "${it.groupValues[1]}w500${it.groupValues[2]}" 
        }
    }

    private fun getNumberFromEpsString(epsStr: String): String = epsStr.filter { it.isDigit() }

    private fun fetchUrls(text: String?): List<String> {
        if (text.isNullOrEmpty()) return listOf()
        val linkRegex = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])".toRegex()
        return linkRegex.findAll(text).map { it.value.trim().removeSurrounding("\"") }.toList()
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()
        
        val response = app.get("$mainUrl/todos/page/$page")
        val document = Jsoup.parse(response.text)
        
        val animes = document.select("div.TpRwCont main section ul.MovieList li.TPostMv article.TPost").map { element ->
            val title = element.select("a h3.Title").text()
            val url = element.select("a").attr("href")
            val poster = element.select("a div.Image figure img").attr("src").trim().replace("//", "https://")
            
            newAnimeSearchResponse(title, url) {
                this.posterUrl = poster
            }
        }
        
        items.add(HomePageList("Todos", animes))
        
        if (items.isEmpty()) throw ErrorLoadingException()
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val response = app.get("$mainUrl/page/1/?s=$query")
        val document = Jsoup.parse(response.text)
        
        return document.select("div.TpRwCont main section ul.MovieList li.TPostMv article.TPost").map { element ->
            val title = element.select("a h3.Title").text()
            val url = element.select("a").attr("href")
            val poster = element.select("a div.Image figure img").attr("src").trim().replace("//", "https://")
            
            newAnimeSearchResponse(title, url) {
                this.posterUrl = poster
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val response = app.get(url)
        val document = Jsoup.parse(response.text)
        
        val title = document.selectFirst("header div.asia-post-header h1.Title")?.text() ?: ""
        val poster = document.selectFirst("header div.Image figure img")?.attr("abs:src")?.let { getHdImg(it) }
        val description = document.selectFirst("header div.asia-post-main div.Description p:nth-child(2), header div.asia-post-main div.Description p")?.text()?.removeSurrounding("\"") ?: ""
        val genre = document.select("div.asia-post-main p.Info span.tags a").joinToString { it.text() }
        val yearText = document.select("header div.asia-post-main p.Info span.Date a").text()
        val year = yearText.toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val status = when {
            year < currentYear -> ShowStatus.Completed
            year == currentYear -> ShowStatus.Ongoing
            else -> null
        }
        
        val episodes = ArrayList<Episode>()
        
        document.select("#ep-list div.TPTblCn span a, #ep-list div.TPTblCn .accordion").forEach { element ->
            if (element.attr("class").contains("accordion")) {
                val epName = element.select("label span").text().trim()
                val epNum = getNumberFromEpsString(epName)
                val epUrl = element.selectFirst("ul li a")?.attr("abs:href") ?: return@forEach
                
                episodes.add(newEpisode(epUrl) {
                    this.name = epName
                    this.episode = epNum.toIntOrNull() ?: 1
                })
            } else {
                val epName = element.select("div.flex-grow-1 p").text().trim()
                val epNum = getNumberFromEpsString(epName)
                val epUrl = element.attr("abs:href")
                
                episodes.add(newEpisode(epUrl) {
                    this.name = epName
                    this.episode = epNum.toIntOrNull() ?: 1
                })
            }
        }
        
        return newTvSeriesLoadResponse(title, url, TvType.AsianDrama, episodes.reversed()) {
            this.posterUrl = poster
            this.plot = description
            this.tags = if (genre.isNotEmpty()) genre.split(", ").map { it.trim() } else emptyList()
            this.showStatus = status
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val response = app.get(data)
        val document = Jsoup.parse(response.text)
        
        val videoUrls = document.select("script:containsData(var videos)")
            .flatMap { fetchUrls(it.data()) }
        
        videoUrls.forEach { videoUrl ->
            val extractorName = when {
                videoUrl.contains("vk", ignoreCase = true) -> "Vk"
                videoUrl.contains("ok.ru", ignoreCase = true) || videoUrl.contains("okru", ignoreCase = true) -> "Ok.ru"
                videoUrl.contains("wishembed", ignoreCase = true) || videoUrl.contains("streamwish", ignoreCase = true) || 
                videoUrl.contains("strwish", ignoreCase = true) || videoUrl.contains("wish", ignoreCase = true) -> "StreamWish"
                videoUrl.contains("filemoon", ignoreCase = true) || videoUrl.contains("moonplayer", ignoreCase = true) -> "Filemoon"
                videoUrl.contains("vembed", ignoreCase = true) || videoUrl.contains("guard", ignoreCase = true) || 
                videoUrl.contains("listeamed", ignoreCase = true) || videoUrl.contains("bembed", ignoreCase = true) || 
                videoUrl.contains("vgfplay", ignoreCase = true) -> "VidGuard"
                videoUrl.contains("filelions", ignoreCase = true) || videoUrl.contains("lion", ignoreCase = true) || 
                videoUrl.contains("fviplions", ignoreCase = true) -> "FileLions"
                (videoUrl.contains("amazon", ignoreCase = true) || videoUrl.contains("amz", ignoreCase = true)) && 
                !videoUrl.contains("disable") -> "Amazon"
                else -> null
            }
            
            if (extractorName != null) {
                loadExtractor(videoUrl, data, subtitleCallback) { link ->
                    callback.invoke(
                        ExtractorLink(
                            source = "$extractorName ${link.source}",
                            name = "$extractorName ${link.name}",
                            url = link.url,
                            referer = link.referer,
                            quality = link.quality,
                            type = link.type,
                            headers = link.headers,
                            extractorData = link.extractorData
                        )
                    )
                }
            }
        }
        
        return true
    }
}
