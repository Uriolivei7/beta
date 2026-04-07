package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup
import android.util.Log
import java.util.Calendar

class AsialiveactionProvider : MainAPI() {

    companion object {
        private const val TAG = "AsiaLiveAction"
    }

    override var mainUrl = "https://asialiveaction.com"
    override var name = "AsiaLiveAction"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.AsianDrama)

    private fun getNumberFromEpsString(epsStr: String): String = epsStr.filter { it.isDigit() }.take(4)

    private fun fetchUrls(text: String?): List<String> {
        if (text.isNullOrEmpty()) return listOf()
        val linkRegex = "(http|ftp|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])".toRegex()
        return linkRegex.findAll(text).map { it.value.trim().removeSurrounding("\"") }.toList()
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val items = ArrayList<HomePageList>()
        
        Log.d(TAG, "getMainPage: Obteniendo página principal")
        val response = app.get(mainUrl)
        val document = Jsoup.parse(response.text)
        
        val homeItems = ArrayList<SearchResponse>()
        
        document.select(".splide__slide").forEach { slide ->
            val link = slide.selectFirst("a")?.attr("href") ?: return@forEach
            val title = slide.selectFirst("h5")?.text() ?: return@forEach
            val poster = slide.selectFirst("img")?.attr("src")?.takeIf { it.isNotEmpty() } 
                ?: slide.selectFirst("img")?.attr("data-src")
            
            Log.d(TAG, "getMainPage: Title=$title, Poster=$poster, Link=$link")
            
            if (link.isNotEmpty() && title.isNotEmpty()) {
                val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.AsianDrama
                homeItems.add(
                    newAnimeSearchResponse(title, link, tvType) {
                        this.posterUrl = poster
                    }
                )
            }
        }
        
        Log.d(TAG, "getMainPage: ${homeItems.size} items encontrados")
        
        if (homeItems.isNotEmpty()) {
            items.add(HomePageList("Todos", homeItems))
        }
        
        if (items.isEmpty()) throw ErrorLoadingException()
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "search: Buscando '$query'")
        val response = app.get("$mainUrl/?s=$query")
        val document = Jsoup.parse(response.text)
        
        val results = document.select(".search-result-item, article.post, .result-item").mapNotNull { element ->
            val link = element.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val title = element.selectFirst("h2, h3, h4, .title")?.text() 
                ?: element.selectFirst("img")?.attr("alt") ?: return@mapNotNull null
            val poster = element.selectFirst("img")?.attr("src")?.takeIf { it.isNotEmpty() }
                ?: element.selectFirst("img")?.attr("data-src")
            
            Log.d(TAG, "search: Title=$title, Poster=$poster, Link=$link")
            
            if (link.isNotEmpty() && title.isNotEmpty()) {
                val tvType = if (link.contains("/pelicula/")) TvType.Movie else TvType.AsianDrama
                newAnimeSearchResponse(title, link, tvType) {
                    this.posterUrl = poster
                }
            } else {
                null
            }
        }
        
        Log.d(TAG, "search: ${results.size} resultados encontrados")
        return results
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "load: Cargando URL=$url")
        val response = app.get(url)
        val document = Jsoup.parse(response.text)
        
        val title = document.selectFirst("h2.Title, h1.Title")?.text() ?: ""
        Log.d(TAG, "load: Title=$title")
        
        val posterStyle = document.selectFirst(".Poster")?.attr("style") ?: ""
        val poster = if (posterStyle.isNotEmpty()) {
            Regex("""url\(['"]?(.*?)['"]?\)""").find(posterStyle)?.groupValues?.get(1)
        } else {
            document.selectFirst("article img")?.attr("src")
        }
        Log.d(TAG, "load: Poster=$poster")
        
        val description = document.selectFirst("article p")?.text() ?: ""
        
        val yearText = document.selectFirst(".estreno")?.text() ?: ""
        val year = yearText.filter { it.isDigit() }.take(4).toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val status = when {
            year < currentYear -> ShowStatus.Completed
            year == currentYear -> ShowStatus.Ongoing
            else -> null
        }
        
        val episodes = ArrayList<Episode>()
        val isMovie = url.contains("/pelicula/")
        
        if (!isMovie) {
            document.select(".lista-episodios .episodio-unico a").forEach { element ->
                val epUrl = element.attr("href")
                if (epUrl.isNotEmpty()) {
                    val epName = element.selectFirst(".numero-episodio")?.text()?.trim() ?: element.text().trim()
                    val epNum = getNumberFromEpsString(epName)
                    
                    Log.d(TAG, "load: Episodio - Name=$epName, Num=$epNum, URL=$epUrl")
                    
                    episodes.add(newEpisode(epUrl) {
                        this.name = epName
                        this.episode = epNum.toIntOrNull() ?: 1
                    })
                }
            }
            Log.d(TAG, "load: ${episodes.size} episodios encontrados")
        }
        
        return if (isMovie) {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.AsianDrama, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.showStatus = status
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "loadLinks: Extrayendo de URL=$data")
        val response = app.get(data)
        val document = Jsoup.parse(response.text)
        
        val videoUrls = mutableListOf<String>()
        
        document.select("script").forEach { script ->
            val scriptData = script.data()
            if (scriptData.contains("var videos") || scriptData.contains("player")) {
                val urls = fetchUrls(scriptData)
                Log.d(TAG, "loadLinks: URLs encontradas en script: $urls")
                videoUrls.addAll(urls)
            }
        }
        
        if (videoUrls.isEmpty()) {
            Log.d(TAG, "loadLinks: Buscando iframes")
            document.select("iframe[src*='player'], iframe[src*='embed'], iframe").forEach { iframe ->
                val iframeUrl = iframe.attr("src")
                if (iframeUrl.isNotEmpty() && iframeUrl.startsWith("http")) {
                    Log.d(TAG, "loadLinks: Iframe encontrado: $iframeUrl")
                    loadExtractor(iframeUrl, data, subtitleCallback, callback)
                }
            }
        }
        
        videoUrls.forEach { videoUrl ->
            Log.d(TAG, "loadLinks: Procesando URL=$videoUrl")
            
            val extractorName = when {
                videoUrl.contains("vk", ignoreCase = true) -> "Vk"
                videoUrl.contains("ok.ru", ignoreCase = true) || videoUrl.contains("okru", ignoreCase = true) -> "Ok.ru"
                videoUrl.contains("wishembed", ignoreCase = true) || videoUrl.contains("streamwish", ignoreCase = true) -> "StreamWish"
                videoUrl.contains("filemoon", ignoreCase = true) || videoUrl.contains("moonplayer", ignoreCase = true) -> "Filemoon"
                videoUrl.contains("vembed", ignoreCase = true) || videoUrl.contains("guard", ignoreCase = true) -> "VidGuard"
                videoUrl.contains("filelions", ignoreCase = true) || videoUrl.contains("lion", ignoreCase = true) -> "FileLions"
                (videoUrl.contains("amazon", ignoreCase = true) || videoUrl.contains("amz", ignoreCase = true)) && 
                !videoUrl.contains("disable") -> "Amazon"
                else -> null
            }
            
            if (extractorName != null) {
                Log.d(TAG, "loadLinks: Usando extractor=$extractorName para URL=$videoUrl")
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
            } else {
                Log.d(TAG, "loadLinks: URL no reconocida, intentando loadExtractor genérico")
                loadExtractor(videoUrl, data, subtitleCallback, callback)
            }
        }
        
        return true
    }
}
