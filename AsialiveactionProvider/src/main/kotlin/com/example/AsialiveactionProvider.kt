package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import android.util.Log
import java.util.Calendar

class AsialiveactionProvider : MainAPI() {

    companion object {
        private const val TAG = "Asialiveaction"
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
        
        val seriesItems = ArrayList<SearchResponse>()
        val peliculasItems = ArrayList<SearchResponse>()
        
        document.select(".navegacion-grid a[href*='/tv/']").forEach { element ->
            val link = element.attr("href") ?: return@forEach
            val title = element.selectFirst("h5")?.text() 
                ?: element.selectFirst("h4")?.text()
                ?: element.selectFirst("img")?.attr("alt") ?: return@forEach
            val poster = element.selectFirst("img")?.attr("src")?.takeIf { it.isNotEmpty() && !it.contains("logo") }
                ?: element.selectFirst("img")?.attr("data-src")
            
            if (link.isNotEmpty() && title.isNotEmpty() && poster != null) {
                seriesItems.add(
                    newAnimeSearchResponse(title, link, TvType.AsianDrama) {
                        this.posterUrl = poster
                    }
                )
            }
        }
        
        document.select(".navegacion-grid a[href*='/pelicula/']").forEach { element ->
            val link = element.attr("href") ?: return@forEach
            val title = element.selectFirst("h5")?.text() 
                ?: element.selectFirst("h4")?.text()
                ?: element.selectFirst("img")?.attr("alt") ?: return@forEach
            val poster = element.selectFirst("img")?.attr("src")?.takeIf { it.isNotEmpty() && !it.contains("logo") }
                ?: element.selectFirst("img")?.attr("data-src")
            
            if (link.isNotEmpty() && title.isNotEmpty() && poster != null) {
                peliculasItems.add(
                    newMovieSearchResponse(title, link) {
                        this.posterUrl = poster
                    }
                )
            }
        }
        
        Log.d(TAG, "getMainPage: Series=${seriesItems.size}, Peliculas=${peliculasItems.size}")
        
        if (seriesItems.isNotEmpty()) {
            items.add(HomePageList("Series", seriesItems))
        }
        if (peliculasItems.isNotEmpty()) {
            items.add(HomePageList("Películas", peliculasItems))
        }
        
        if (items.isEmpty()) throw ErrorLoadingException()
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "search: Buscando '$query'")
        val response = app.get("$mainUrl/search/$query")
        val document = Jsoup.parse(response.text)
        
        val results = document.select(".navegacion-grid .splide__slide a, .search-result-item, article.post").mapNotNull { element ->
            val link = element.attr("href")?.takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            val title = element.selectFirst("h5")?.text() 
                ?: element.selectFirst("h2, h3, h4, .title")?.text()
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
        val htmlContent = response.text
        val document = Jsoup.parse(htmlContent)
        
        val allVideosPattern = """allVideos\s*=\s*(\{.*?\});""".toRegex(RegexOption.DOT_MATCHES_ALL)
        val allVideosMatch = allVideosPattern.find(htmlContent)
        
        if (allVideosMatch != null) {
            val allVideosJson = allVideosMatch.groupValues[1]
            Log.d(TAG, "loadLinks: allVideos encontrado: ${allVideosJson.take(200)}")
            
            try {
                val serverDataMap = parseJson<Map<String, List<List<String>>>>(allVideosJson)
                serverDataMap.forEach { (langId, serverList) ->
                    val langName = when {
                        langId.contains("1388") -> "Latino"
                        langId.contains("1385") -> "Subtitulado"
                        else -> "Default"
                    }
                    
                    serverList.forEach { serverData ->
                        if (serverData.size >= 2) {
                            val serverCode = serverData[0]
                            val encodedUrl = serverData[1]
                            val decodedUrl = encodedUrl
                                .replace("\\/", "/")
                                .replace("\\\\", "")
                                .removeSurrounding("\"")
                            
                            Log.d(TAG, "loadLinks: [$langName] Server=$serverCode, URL=$decodedUrl")
                            
                            if (decodedUrl.startsWith("http")) {
                                val serverName = when (serverCode) {
                                    "YM" -> "Yui"
                                    "MK" -> "Moa"
                                    "MM" -> "Momo"
                                    "FM" -> "Byse"
                                    else -> serverCode
                                }
                                
                                val fullName = "$serverName [$langName]"
                                loadSourceNameExtractor(fullName, decodedUrl, data, subtitleCallback, callback)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadLinks: Error parseando allVideos: ${e.message}")
            }
        }
        
        return true
    }
}

suspend fun loadSourceNameExtractor(
    source: String,
    url: String,
    referer: String? = null,
    subtitleCallback: (SubtitleFile) -> Unit,
    callback: (ExtractorLink) -> Unit,
) {
    loadExtractor(url, referer, subtitleCallback) { link ->
        CoroutineScope(Dispatchers.IO).launch {
            callback.invoke(
                newExtractorLink(
                    "$source[${link.source}]",
                    "$source[${link.source}]",
                    link.url,
                ) {
                    this.quality = link.quality
                    this.type = link.type
                    this.referer = link.referer
                    this.headers = link.headers
                    this.extractorData = link.extractorData
                }
            )
        }
    }
}
