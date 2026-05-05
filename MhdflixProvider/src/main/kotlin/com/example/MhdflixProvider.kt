package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.Jsoup

class MhdflixProvider : MainAPI() {
    override var mainUrl = "https://mhdflix.com"
    override var name = "MHDFLIX"
    override val hasMainPage = true
    override var lang = "es"
    override val hasDownloadSupport = true
    override val hasChromecastSupport = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.Cartoon)

    private val apiBaseUrl = "https://ww1.mhdflix.com"

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "Referer" to "$mainUrl/"
    )

    private fun fixUrlPath(path: String?): String {
        if (path.isNullOrBlank() || path.contains("undefined")) return ""
        return if (path.startsWith("http")) path
        else "https://image.tmdb.org/t/p/w500$path"
    }

    private fun parseRscPayload(html: String): String {
        return try {
            val matches = Regex("""self\.__next_f\.push\(\[\d+,"(.*?)"\]\)""", RegexOption.DOT_MATCHES_ALL).findAll(html)
            matches.joinToString("") { it.groupValues[1] }
                .replace("\\\\\"", "\"")
                .replace("\\\\n", "")
                .replace("\\n", "")
        } catch (e: Exception) {
            ""
        }
    }

    private fun extractPosterFromRsc(rscPayload: String, mediaId: Long?): String {
        if (mediaId == null) return ""
        return try {
            val pattern = """"id":${mediaId},"title":"[^"]*","poster":"([^"]*)"""".toRegex().find(rscPayload)
            pattern?.groupValues?.get(1)?.takeIf { it.isNotBlank() && !it.contains("undefined") } ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun extractItemListFromRsc(rscPayload: String, sectionKey: String): List<SearchItem> {
        return try {
            val results = mutableListOf<SearchItem>()
            val sectionPattern = """"$sectionKey":\s*\[""".toRegex().find(rscPayload)
            if (sectionPattern == null) return emptyList()
            
            val afterSection = rscPayload.substring(sectionPattern.range.last)
            val itemsRegex = """"id":(\d+),"title":"([^"]*)","poster":"([^"]*)"""".toRegex().findAll(afterSection)
            
            for (match in itemsRegex.take(30)) {
                val id = match.groupValues[1].toLongOrNull() ?: continue
                val title = match.groupValues[2]
                val poster = match.groupValues[3].takeIf { it.isNotBlank() && !it.contains("undefined") } ?: ""
                
                if (title.isNotBlank() && title.length > 2) {
                    results.add(SearchItem(id, title, poster, "movie"))
                }
            }
            results.distinctBy { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun extractMediaFromRsc(html: String): RscMedia? {
        return try {
            val rsc = parseRscPayload(html)
            val mediaPattern = """"media":\s*\{[^}]*"id":(\d+),"title":"([^"]*)","poster":"([^"]*)","backdrop":"([^"]*)","genre":\[(.*?)\],"date":"([^"]*)","duration":"([^"]*)","rating":(\d+)?,"description":"([^"]*)","type":"([^"]*)"""".toRegex(RegexOption.DOT_MATCHES_ALL).find(rsc)
            
            if (mediaPattern != null) {
                RscMedia(
                    id = mediaPattern.groupValues[1].toLongOrNull(),
                    title = mediaPattern.groupValues[2],
                    poster = mediaPattern.groupValues[3],
                    backdrop = mediaPattern.groupValues[4],
                    genre = mediaPattern.groupValues[5].split(",").filter { it.isNotBlank() }.map { it.trim('"') },
                    date = mediaPattern.groupValues[6],
                    duration = mediaPattern.groupValues[7],
                    rating = mediaPattern.groupValues[8].toIntOrNull(),
                    description = mediaPattern.groupValues[9],
                    type = mediaPattern.groupValues[10]
                )
            } else null
        } catch (e: Exception) {
            Log.e("Mhdflix", "Error extracting media: ${e.message}")
            null
        }
    }

    private fun extractEpisodeListFromRsc(html: String, serieId: Long): List<EpisodeItem> {
        return try {
            val rsc = parseRscPayload(html)
            val episodes = mutableListOf<EpisodeItem>()
            
            val epPattern = """"idEpisodios":(\d+),"title":"([^"]*)","overview":"([^"]*)","numEpisode":(\d+),"numSeason":(\d+)""".toRegex(RegexOption.DOT_MATCHES_ALL).findAll(rsc)
            
            for (match in epPattern) {
                episodes.add(EpisodeItem(
                    id = match.groupValues[1].toLong(),
                    title = match.groupValues[2],
                    overview = match.groupValues[3],
                    episodeNum = match.groupValues[4].toInt(),
                    seasonNum = match.groupValues[5].toInt()
                ))
            }
            
            if (episodes.isEmpty()) {
                val simplePattern = """"idEpisodios":(\d+),"title":"([^"]*)","numEpisode":(\d+),""".toRegex().findAll(rsc)
                for (match in simplePattern) {
                    episodes.add(EpisodeItem(
                        id = match.groupValues[1].toLong(),
                        title = match.groupValues[2],
                        overview = "",
                        episodeNum = match.groupValues[3].toInt(),
                        seasonNum = 1
                    ))
                }
            }
            
            episodes.distinctBy { it.id }
        } catch (e: Exception) {
            Log.e("Mhdflix", "Error extracting episodes: ${e.message}")
            emptyList()
        }
    }

    override val mainPage = mainPageOf(
        "$mainUrl/movies" to "Películas Recientes",
        "$mainUrl/tvs" to "Series Recientes",
        "$mainUrl/tvs/category/anime" to "Animes Recientes",
        "$mainUrl/tvs/category/dorama" to "Doramas Recientes",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val html = app.get(request.data, headers = baseHeaders).text
        val rscPayload = parseRscPayload(html)
        
        val tvType = when {
            request.name.contains("Película") -> TvType.Movie
            request.name.contains("Anime") -> TvType.Anime
            request.name.contains("Dorama") -> TvType.AsianDrama
            else -> TvType.TvSeries
        }

        val sectionKeys = listOf("movies", "series", "animes", "doramas", "recent", "latest", "items", "data")
        var allItems = emptyList<SearchItem>()
        
        for (key in sectionKeys) {
            val items = extractItemListFromRsc(rscPayload, key)
            if (items.isNotEmpty()) {
                allItems = items
                break
            }
        }
        
        if (allItems.isEmpty()) {
            val doc = Jsoup.parse(html)
            val links = doc.select("a[href*='/movies/'], a[href*='/tvs/']")
                .filter { !it.attr("href").contains("/episode/") && !it.attr("href").contains("/category/") && !it.attr("href").contains("/genres") }
            
            allItems = links.mapNotNull { link ->
                val href = link.attr("href")
                val idRegex = Regex("""/(?:movies|tvs)/(\d+)/""")
                val id = idRegex.find(href)?.groupValues?.get(1)?.toLongOrNull() ?: return@mapNotNull null
                
                val img = link.selectFirst("img")
                val poster = img?.let { 
                    it.attr("data-src").ifBlank { it.attr("src") }.ifBlank { 
                        Regex("""url\(['"]?([^'")]+)['"]?\)""").find(it.attr("style"))?.groupValues?.get(1) 
                    }
                } ?: ""
                val title = img?.attr("alt") ?: link.text()
                
                if (title.isNotBlank() && title.length > 2) {
                    SearchItem(id, title, poster, if (href.contains("/movies/")) "movie" else "tv")
                } else null
            }.distinctBy { it.id }.take(20)
        }

        val searchResponses = allItems.map { item ->
            val slug = item.title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
            val url = if (item.type == "movie") "$mainUrl/movies/${item.id}/$slug" else "$mainUrl/tvs/${item.id}/$slug"
            newMovieSearchResponse(item.title, url, tvType) {
                this.posterUrl = fixUrlPath(item.poster)
            }
        }.take(20)

        return if (searchResponses.isNotEmpty()) {
            newHomePageResponse(
                list = HomePageList(request.name, searchResponses),
                hasNext = false
            )
        } else null
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchUrl = "$mainUrl/search?q=${query.replace(" ", "+")}"
        val html = app.get(searchUrl, headers = baseHeaders).text
        val rscPayload = parseRscPayload(html)
        
        val searchKeys = listOf("results", "search", "movies", "series", "items", "data")
        var items = emptyList<SearchItem>()
        
        for (key in searchKeys) {
            val extracted = extractItemListFromRsc(rscPayload, key)
            if (extracted.isNotEmpty()) {
                items = extracted
                break
            }
        }
        
        if (items.isEmpty()) {
            val doc = Jsoup.parse(html)
            val links = doc.select("a[href*='/movies/'], a[href*='/tvs/']")
                .filter { !it.attr("href").contains("/episode/") }
            
            items = links.mapNotNull { link ->
                val href = link.attr("href")
                val idRegex = Regex("""/(?:movies|tvs)/(\d+)/""")
                val id = idRegex.find(href)?.groupValues?.get(1)?.toLongOrNull() ?: return@mapNotNull null
                
                val img = link.selectFirst("img")
                val poster = img?.let { 
                    it.attr("data-src").ifBlank { it.attr("src") } 
                } ?: ""
                val title = img?.attr("alt") ?: link.text()
                
                if (title.contains(query, ignoreCase = true)) {
                    SearchItem(id, title, poster, if (href.contains("/movies/")) "movie" else "tv")
                } else null
            }.distinctBy { it.id }.take(20)
        }

        return items.map { item ->
            val slug = item.title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
            val url = if (item.type == "movie") "$mainUrl/movies/${item.id}/$slug" else "$mainUrl/tvs/${item.id}/$slug"
            val tvType = if (item.type == "movie") TvType.Movie else TvType.TvSeries
            newMovieSearchResponse(item.title, url, tvType) {
                this.posterUrl = fixUrlPath(item.poster)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = app.get(url, headers = baseHeaders).text

        if (url.contains("/episode/")) {
            return loadEpisodePage(url, html)
        }

        val media = extractMediaFromRsc(html)
        
        val idFromUrl = Regex("""/(?:movies|tvs)/(\d+)/""").find(url)?.groupValues?.get(1)?.toLongOrNull()

        val title = media?.title
            ?: Jsoup.parse(html).selectFirst("h1")?.text()
            ?: Jsoup.parse(html).selectFirst("title")?.text()?.substringBefore("|")?.trim()
            ?: "Sin título"

        val posterUrl = fixUrlPath(media?.poster)
        val finalPoster = if (posterUrl.isNotEmpty()) posterUrl
            else extractPosterFromRsc(parseRscPayload(html), idFromUrl)
            .ifEmpty { Jsoup.parse(html).selectFirst("meta[property='og:image']")?.attr("content") ?: "" }

        val backdrop = fixUrlPath(media?.backdrop?.takeIf { it.isNotBlank() ?: false })

        val description = media?.description?.takeIf { it.isNotBlank() }
            ?: Jsoup.parse(html).selectFirst("meta[name='description']")?.attr("content")
            ?: ""

        val year = media?.date?.takeIf { it.length >= 4 }?.substring(0, 4)?.toIntOrNull()

        val rating = media?.rating?.let { Score.from10(it.toFloat()) }

        val tags = media?.genre?.filter { it.isNotBlank() }?.takeIf { it.isNotEmpty() }

        val duration = media?.duration?.let { dur ->
            val h = Regex("""(\d+)h""").find(dur)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val m = Regex("""(\d+)min""").find(dur)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            (h * 60) + m
        }

        val tvType = if (media?.type == "movie" || url.contains("/movies/")) TvType.Movie else TvType.TvSeries

        val slug = title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')

        if (tvType == TvType.Movie) {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = finalPoster
                this.backgroundPosterUrl = backdrop.takeIf { it.isNotBlank() }
                this.plot = description
                this.year = year
                this.tags = tags
                this.score = rating
                this.duration = duration
            }
        } else {
            val serieId = idFromUrl ?: media?.id
            
            val episodes = if (serieId != null) {
                extractEpisodeListFromRsc(html, serieId).map { ep ->
                    val epUrl = "$mainUrl/tvs/episode/${ep.id}/$slug"
                    newEpisode(epUrl) {
                        this.name = ep.title
                        this.season = ep.seasonNum
                        this.episode = ep.episodeNum
                        this.description = ep.overview.takeIf { it.isNotBlank() }
                        this.posterUrl = finalPoster
                    }
                }
            } else emptyList()

            val fallbackEpisodes = if (episodes.isEmpty()) {
                val doc = Jsoup.parse(html)
                doc.select("a[href*='/episode/']").mapNotNull { link ->
                    val href = link.attr("href")
                    val epId = Regex("""/episode/(\d+)""").find(href)?.groupValues?.get(1)?.toLongOrNull() ?: return@mapNotNull null
                    val epTitle = link.text().trim()
                    newEpisode(fixUrl(href)) {
                        this.name = epTitle.ifBlank { "Episodio" }
                        this.season = 1
                        this.episode = 1
                    }
                }.distinctBy { it.data }.take(20)
            } else emptyList()

            val finalEpisodes = episodes.ifEmpty { fallbackEpisodes }

            return if (finalEpisodes.isNotEmpty()) {
                newTvSeriesLoadResponse(title, url, TvType.TvSeries, finalEpisodes) {
                    this.posterUrl = finalPoster
                    this.backgroundPosterUrl = backdrop.takeIf { it.isNotBlank() }
                    this.plot = description
                    this.year = year
                    this.tags = tags
                    this.score = rating
                    this.duration = duration
                }
            } else {
                newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(newEpisode(url) {
                    this.name = title
                    this.posterUrl = finalPoster
                })) {
                    this.posterUrl = finalPoster
                    this.backgroundPosterUrl = backdrop.takeIf { it.isNotBlank() }
                    this.plot = description
                    this.year = year
                    this.tags = tags
                }
            }
        }
    }

    private suspend fun loadEpisodePage(url: String, html: String): LoadResponse? {
        val rsc = parseRscPayload(html)
        
        val episodeId = Regex("""/episode/(\d+)""").find(url)?.groupValues?.get(1)?.toLongOrNull()
        
        val epTitlePattern = """"idEpisodios":${episodeId}[^}]*"title":"([^"]*)"""".toRegex().find(rsc)
        val episodeTitle = epTitlePattern?.groupValues?.get(1) ?: "Episodio"
        
        val overviewPattern = """"idEpisodios":${episodeId}[^}]*"overview":"([^"]*)"""".toRegex().find(rsc)
        val overview = overviewPattern?.groupValues?.get(1) ?: ""
        
        val seasonPattern = """"idEpisodios":${episodeId}[^}]*"numSeason":(\d+)""".toRegex().find(rsc)
        val seasonNum = seasonPattern?.groupValues?.get(1)?.toIntOrNull() ?: 1
        
        val epNumPattern = """"idEpisodios":${episodeId}[^}]*"numEpisode":(\d+)""".toRegex().find(rsc)
        val epNum = epNumPattern?.groupValues?.get(1)?.toIntOrNull() ?: 1
        
        val posterPattern = """"idEpisodios":${episodeId}[^}]*"posterPath":"([^"]*)"""".toRegex().find(rsc)
        val poster = fixUrlPath(posterPattern?.groupValues?.get(1))
        
        val serieSlugPattern = """"serieSlug":"([^"]*)"""".toRegex().find(rsc)
        val serieSlug = serieSlugPattern?.groupValues?.get(1)
        
        val serieUrl = if (serieSlug != null) "$mainUrl/tvs/$serieSlug" else url

        return newTvSeriesLoadResponse(episodeTitle, serieUrl, TvType.TvSeries, listOf(newEpisode(url) {
            this.name = episodeTitle
            this.season = seasonNum
            this.episode = epNum
            this.description = overview.takeIf { it.isNotBlank() }
            this.posterUrl = poster
        })) {
            this.posterUrl = poster
            this.plot = overview.takeIf { it.isNotBlank() }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("Mhdflix", "loadLinks - URL: $data")
        
        val contentInfo = extractContentInfo(data)
        if (contentInfo == null) {
            Log.e("Mhdflix", "Could not extract ID")
            return false
        }
        
        val (id, type) = contentInfo
        val apiUrl = "$apiBaseUrl/api/links?id=$id&type=$type"
        
        return try {
            Log.d("Mhdflix", "Calling API: $apiUrl")
            
            val response = app.get(
                apiUrl,
                headers = mapOf(
                    "User-Agent" to baseHeaders.getValue("User-Agent"),
                    "Referer" to "$mainUrl/",
                )
            ).text
            
            Log.d("Mhdflix", "API Response: ${response.take(500)}")
            
            when {
                response.trim().startsWith("[") -> {
                    val links = parseJson<List<ApiLink>>(response)
                    processApiLinks(links, subtitleCallback, callback)
                }
                response.trim().startsWith("{") -> {
                    val singleLink = tryParseJson<ApiLink>(response)
                    if (singleLink != null) {
                        processApiLinks(listOf(singleLink), subtitleCallback, callback)
                    } else {
                        Log.e("Mhdflix", "Could not parse single link object")
                        false
                    }
                }
                else -> {
                    Log.d("Mhdflix", "Unexpected response format, trying iframe fallback")
                    extractIframesFromHtml(response, subtitleCallback, callback)
                }
            }
        } catch (e: Exception) {
            Log.e("Mhdflix", "API error: ${e.message}")
            val html = app.get(data, headers = baseHeaders).text
            extractIframesFromHtml(html, subtitleCallback, callback)
        }
    }

    private suspend fun processApiLinks(
        links: List<ApiLink>,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var found = false
        
        for (link in links) {
            val videoUrl = link.url ?: link.embedUrl ?: link.iframeUrl ?: continue
            if (videoUrl.isBlank() || videoUrl.contains("undefined")) continue
            
            val serverName = link.server?.name ?: link.serverName ?: "Unknown"
            val languageName = link.language?.name ?: link.languageName ?: "Latino"
            val qualityName = link.quality?.name ?: ""
            
            val linkName = "$serverName - $languageName"
            
            if (videoUrl.startsWith("http")) {
                val qualityValue = when {
                    qualityName.contains("4k", ignoreCase = true) || qualityName.contains("2160") -> Qualities.P2160.value
                    qualityName.contains("1080") -> Qualities.P1080.value
                    qualityName.contains("720") -> Qualities.P720.value
                    qualityName.contains("480") -> Qualities.P480.value
                    qualityName.contains("360") -> Qualities.P360.value
                    else -> Qualities.Unknown.value
                }
                
                val isM3u8 = videoUrl.contains(".m3u8") || videoUrl.contains("m3u8")
                val linkType = if (isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                
                callback.invoke(
                    newExtractorLink(name, linkName, videoUrl) {
                        this.referer = mainUrl
                        this.quality = qualityValue
                        this.type = linkType
                    }
                )
                found = true
            } else {
                loadExtractor(fixUrl(videoUrl), mainUrl, subtitleCallback, callback)
                found = true
            }
            
            link.subtitles?.forEach { sub ->
                sub.url?.let { url ->
                    subtitleCallback.invoke(
                        newSubtitleFile(sub.name ?: languageName, fixUrl(url))
                    )
                }
            }
        }
        
        return found
    }

    private suspend fun extractIframesFromHtml(
        html: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var found = false
        
        val doc = Jsoup.parse(html)
        val iframes = doc.select("iframe[src]")
        
        for (iframe in iframes) {
            val src = iframe.attr("src")
            if (src.isNotBlank() && !src.contains("undefined")) {
                loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                found = true
            }
        }
        
        if (!found) {
            val rsc = parseRscPayload(html)
            val urlPatterns = listOf(
                """"url":"(https?://[^"]+)""",
                """"embedUrl":"(https?://[^"]+)""",
                """"iframe":"(https?://[^"]+)""",
                """"playerUrl":"(https?://[^"]+)""",
                """"videoUrl":"(https?://[^"]+)""",
            )
            
            for (pattern in urlPatterns) {
                val matches = pattern.toRegex().findAll(rsc)
                for (match in matches) {
                    val url = match.groupValues[1]
                    if (url.isNotBlank() && !url.contains("undefined")) {
                        loadExtractor(fixUrl(url), mainUrl, subtitleCallback, callback)
                        found = true
                    }
                }
            }
        }
        
        return found
    }

    private fun extractContentInfo(data: String): Pair<String, String>? {
        Regex("""/movies/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "movie")
        }
        Regex("""/tvs/episode/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "episode")
        }
        Regex("""/tvs/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "episode")
        }
        return null
    }

    data class SearchItem(
        val id: Long,
        val title: String,
        val poster: String,
        val type: String
    )

    data class EpisodeItem(
        val id: Long,
        val title: String,
        val overview: String,
        val episodeNum: Int,
        val seasonNum: Int
    )

    data class RscMedia(
        val id: Long?,
        val title: String?,
        val poster: String?,
        val backdrop: String?,
        val genre: List<String>,
        val date: String?,
        val duration: String?,
        val rating: Int?,
        val description: String?,
        val type: String?
    )

    data class ApiLink(
        @JsonProperty("url") val url: String? = null,
        @JsonProperty("embedUrl") val embedUrl: String? = null,
        @JsonProperty("iframeUrl") val iframeUrl: String? = null,
        @JsonProperty("language") val language: ApiLanguage? = null,
        @JsonProperty("languageName") val languageName: String? = null,
        @JsonProperty("quality") val quality: ApiQuality? = null,
        @JsonProperty("server") val server: ApiServer? = null,
        @JsonProperty("serverName") val serverName: String? = null,
        @JsonProperty("subtitles") val subtitles: List<ApiSubtitle>? = null
    )

    data class ApiLanguage(@JsonProperty("name") val name: String? = null)
    data class ApiQuality(@JsonProperty("name") val name: String? = null)
    data class ApiServer(@JsonProperty("name") val name: String? = null)
    data class ApiSubtitle(
        @JsonProperty("url") val url: String? = null,
        @JsonProperty("name") val name: String? = null
    )
}
