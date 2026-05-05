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
            val combined = matches.joinToString("") { it.groupValues[1] }
                .replace("\\\\\"", "\"")
                .replace("\\\\n", "")
                .replace("\\n", "")
            Log.d("Mhdflix-RSC", "parseRscPayload - Length: ${combined.length}")
            combined
        } catch (e: Exception) {
            Log.e("Mhdflix-RSC", "parseRscPayload - Error: ${e.message}")
            ""
        }
    }

    private fun extractPosterFromRsc(rscPayload: String, mediaId: Long?): String {
        if (mediaId == null) return ""
        return try {
            val pattern = """"id":${mediaId},"title":"[^"]*","poster":"([^"]*)"""".toRegex().find(rscPayload)
            val result = pattern?.groupValues?.get(1)?.takeIf { it.isNotBlank() && !it.contains("undefined") } ?: ""
            Log.d("Mhdflix-Poster", "extractPosterFromRsc - id=$mediaId, result=$result")
            result
        } catch (e: Exception) {
            Log.e("Mhdflix-Poster", "extractPosterFromRsc - Error: ${e.message}")
            ""
        }
    }

    private fun extractItemListFromRsc(rscPayload: String, sectionKey: String): List<SearchItem> {
        return try {
            Log.d("Mhdflix-Items", "extractItemListFromRsc - Looking for key: '$sectionKey'")
            val sectionPattern = """"$sectionKey":\s*\[""".toRegex().find(rscPayload)
            if (sectionPattern == null) {
                Log.d("Mhdflix-Items", "extractItemListFromRsc - Key '$sectionKey' not found")
                return emptyList()
            }
            
            val afterSection = rscPayload.substring(sectionPattern.range.last)
            val itemsRegex = """"id":(\d+),"title":"([^"]*)","poster":"([^"]*)"""".toRegex().findAll(afterSection)
            
            val results = mutableListOf<SearchItem>()
            var count = 0
            for (match in itemsRegex) {
                if (count >= 30) break
                val id = match.groupValues[1].toLongOrNull() ?: continue
                val title = match.groupValues[2]
                val poster = match.groupValues[3].takeIf { it.isNotBlank() && !it.contains("undefined") } ?: ""
                
                if (title.isNotBlank() && title.length > 2) {
                    Log.d("Mhdflix-Items", "  Found: id=$id, title='$title', poster='${poster.take(30)}...'")
                    results.add(SearchItem(id, title, poster, "movie"))
                }
                count++
            }
            Log.d("Mhdflix-Items", "extractItemListFromRsc - Found ${results.size} items for key '$sectionKey'")
            results.distinctBy { it.id }
        } catch (e: Exception) {
            Log.e("Mhdflix-Items", "extractItemListFromRsc - Error: ${e.message}")
            emptyList()
        }
    }

    private fun extractMediaFromRsc(html: String): RscMedia? {
        return try {
            val rsc = parseRscPayload(html)
            val mediaPattern = """"media":\s*\{[^}]*"id":(\d+),"title":"([^"]*)","poster":"([^"]*)","backdrop":"([^"]*)","genre":\[(.*?)\],"date":"([^"]*)","duration":"([^"]*)","rating":(\d+)?,"description":"([^"]*)","type":"([^"]*)"""".toRegex(RegexOption.DOT_MATCHES_ALL).find(rsc)
            
            if (mediaPattern != null) {
                val media = RscMedia(
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
                Log.d("Mhdflix-Media", "extractMediaFromRsc - Found: title='${media.title}', type='${media.type}', poster='${media.poster?.take(30)}'")
                media
            } else {
                Log.d("Mhdflix-Media", "extractMediaFromRsc - No media pattern found")
                null
            }
        } catch (e: Exception) {
            Log.e("Mhdflix-Media", "extractMediaFromRsc - Error: ${e.message}")
            null
        }
    }

    private fun extractEpisodeListFromRsc(html: String, serieId: Long): List<EpisodeItem> {
        return try {
            val rsc = parseRscPayload(html)
            val episodes = mutableListOf<EpisodeItem>()
            
            Log.d("Mhdflix-Episodes", "extractEpisodeListFromRsc - serieId=$serieId, rsc length=${rsc.length}")
            
            val epPattern = """"idEpisodios":(\d+),"title":"([^"]*)","overview":"([^"]*)","numEpisode":(\d+),"numSeason":(\d+)""".toRegex(RegexOption.DOT_MATCHES_ALL).findAll(rsc)
            var count = 0
            for (match in epPattern) {
                val ep = EpisodeItem(
                    id = match.groupValues[1].toLong(),
                    title = match.groupValues[2],
                    overview = match.groupValues[3],
                    episodeNum = match.groupValues[4].toInt(),
                    seasonNum = match.groupValues[5].toInt()
                )
                episodes.add(ep)
                count++
                if (count <= 5) {
                    Log.d("Mhdflix-Episodes", "  Ep ${ep.seasonNum}x${ep.episodeNum}: ${ep.title}")
                }
            }
            Log.d("Mhdflix-Episodes", "extractEpisodeListFromRsc - Pattern1 found $count episodes")
            
            if (episodes.isEmpty()) {
                val simplePattern = """"idEpisodios":(\d+),"title":"([^"]*)","numEpisode":(\d+),""".toRegex().findAll(rsc)
                var count2 = 0
                for (match in simplePattern) {
                    val ep = EpisodeItem(
                        id = match.groupValues[1].toLong(),
                        title = match.groupValues[2],
                        overview = "",
                        episodeNum = match.groupValues[3].toInt(),
                        seasonNum = 1
                    )
                    episodes.add(ep)
                    count2++
                    if (count2 <= 5) {
                        Log.d("Mhdflix-Episodes", "  Ep (simple) ${ep.episodeNum}: ${ep.title}")
                    }
                }
                Log.d("Mhdflix-Episodes", "extractEpisodeListFromRsc - Simple pattern found $count2 episodes")
            }
            
            Log.d("Mhdflix-Episodes", "extractEpisodeListFromRsc - Total unique: ${episodes.distinctBy { it.id }.size}")
            episodes.distinctBy { it.id }
        } catch (e: Exception) {
            Log.e("Mhdflix-Episodes", "extractEpisodeListFromRsc - Error: ${e.message}")
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
        Log.d("Mhdflix-MainPage", "=== getMainPage START ===")
        Log.d("Mhdflix-MainPage", "request.name: ${request.name}")
        Log.d("Mhdflix-MainPage", "request.data: ${request.data}")
        
        val html = app.get(request.data, headers = baseHeaders).text
        Log.d("Mhdflix-MainPage", "HTML length: ${html.length}")
        
        val rscPayload = parseRscPayload(html)
        Log.d("Mhdflix-MainPage", "RSC payload length: ${rscPayload.length}")
        
        val tvType = when {
            request.name.contains("Película") -> TvType.Movie
            request.name.contains("Anime") -> TvType.Anime
            request.name.contains("Dorama") -> TvType.AsianDrama
            else -> TvType.TvSeries
        }
        Log.d("Mhdflix-MainPage", "tvType: $tvType")

        val sectionKeys = listOf("movies", "series", "animes", "doramas", "recent", "latest", "items", "data")
        var allItems = emptyList<SearchItem>()
        
        Log.d("Mhdflix-MainPage", "--- Trying RSC extraction ---")
        for (key in sectionKeys) {
            val items = extractItemListFromRsc(rscPayload, key)
            Log.d("Mhdflix-MainPage", "Key '$key' returned ${items.size} items")
            if (items.isNotEmpty()) {
                allItems = items
                Log.d("Mhdflix-MainPage", "Using items from key '$key'")
                break
            }
        }
        
        if (allItems.isEmpty()) {
            Log.d("Mhdflix-MainPage", "--- RSC extraction empty, trying DOM fallback ---")
            val doc = Jsoup.parse(html)
            val links = doc.select("a[href*='/movies/'], a[href*='/tvs/']")
            Log.d("Mhdflix-MainPage", "Total links found: ${links.size}")
            
            val filteredLinks = links
                .filter { !it.attr("href").contains("/episode/") && !it.attr("href").contains("/category/") && !it.attr("href").contains("/genres") }
            Log.d("Mhdflix-MainPage", "Filtered links (no episode/category/genres): ${filteredLinks.size}")
            
            allItems = filteredLinks.mapNotNull { link ->
                val href = link.attr("href")
                val idRegex = Regex("""/(?:movies|tvs)/(\d+)/""")
                val id = idRegex.find(href)?.groupValues?.get(1)?.toLongOrNull() ?: return@mapNotNull null
                
                val img = link.selectFirst("img")
                val poster = img?.let { 
                    val dataSrc = it.attr("data-src")
                    val src = it.attr("src")
                    val styleUrl = Regex("""url\(['"]?([^'")]+)['"]?\)""").find(it.attr("style"))?.groupValues?.get(1)
                    val result = dataSrc.ifBlank { src.ifBlank { styleUrl ?: "" } }
                    Log.d("Mhdflix-MainPage", "  Poster extraction: dataSrc='$dataSrc', src='$src', styleUrl='$styleUrl', final='${result.take(30)}'")
                    result
                } ?: ""
                val title = img?.attr("alt") ?: link.text()
                Log.d("Mhdflix-MainPage", "  Link: href='$href', id=$id, title='$title'")
                
                if (title.isNotBlank() && title.length > 2) {
                    SearchItem(id, title, poster, if (href.contains("/movies/")) "movie" else "tv")
                } else null
            }.distinctBy { it.id }.take(20)
        }

        Log.d("Mhdflix-MainPage", "Total items before mapping: ${allItems.size}")
        
        val searchResponses = allItems.map { item ->
            val slug = item.title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
            val url = if (item.type == "movie") "$mainUrl/movies/${item.id}/$slug" else "$mainUrl/tvs/${item.id}/$slug"
            val posterFixed = fixUrlPath(item.poster)
            Log.d("Mhdflix-MainPage", "Item: title='${item.title}', poster='${item.poster.take(30)}', fixed='$posterFixed', url='$url'")
            newMovieSearchResponse(item.title, url, tvType) {
                this.posterUrl = posterFixed
            }
        }.take(20)

        Log.d("Mhdflix-MainPage", "Final searchResponses: ${searchResponses.size}")
        Log.d("Mhdflix-MainPage", "=== getMainPage END ===")

        return if (searchResponses.isNotEmpty()) {
            newHomePageResponse(
                list = HomePageList(request.name, searchResponses),
                hasNext = false
            )
        } else {
            Log.e("Mhdflix-MainPage", "Returning null - no items found")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("Mhdflix-Search", "=== search START - query: '$query' ===")
        val searchUrl = "$mainUrl/search?q=${query.replace(" ", "+")}"
        Log.d("Mhdflix-Search", "searchUrl: $searchUrl")
        
        val html = app.get(searchUrl, headers = baseHeaders).text
        Log.d("Mhdflix-Search", "Search HTML length: ${html.length}")
        Log.d("Mhdflix-Search", "Search HTML snippet: ${html.take(500)}")
        
        val rscPayload = parseRscPayload(html)
        Log.d("Mhdflix-Search", "Search RSC length: ${rscPayload.length}")
        
        val searchKeys = listOf("results", "search", "movies", "series", "items", "data")
        var items = emptyList<SearchItem>()
        
        Log.d("Mhdflix-Search", "--- Trying RSC extraction for search ---")
        for (key in searchKeys) {
            val extracted = extractItemListFromRsc(rscPayload, key)
            Log.d("Mhdflix-Search", "Key '$key' returned ${extracted.size} items")
            if (extracted.isNotEmpty()) {
                items = extracted
                break
            }
        }
        
        if (items.isEmpty()) {
            Log.d("Mhdflix-Search", "--- RSC empty, trying DOM fallback ---")
            val doc = Jsoup.parse(html)
            val links = doc.select("a[href*='/movies/'], a[href*='/tvs/']")
            Log.d("Mhdflix-Search", "DOM links found: ${links.size}")
            
            items = links.mapNotNull { link ->
                val href = link.attr("href")
                val idRegex = Regex("""/(?:movies|tvs)/(\d+)/""")
                val id = idRegex.find(href)?.groupValues?.get(1)?.toLongOrNull() ?: return@mapNotNull null
                
                val img = link.selectFirst("img")
                val poster = img?.let { 
                    it.attr("data-src").ifBlank { it.attr("src") } 
                } ?: ""
                val title = img?.attr("alt") ?: link.text()
                Log.d("Mhdflix-Search", "DOM link: id=$id, title='$title', matches query: ${title.contains(query, ignoreCase = true)}")
                
                if (title.contains(query, ignoreCase = true)) {
                    SearchItem(id, title, poster, if (href.contains("/movies/")) "movie" else "tv")
                } else null
            }.distinctBy { it.id }.take(20)
        }

        Log.d("Mhdflix-Search", "Total search items: ${items.size}")
        
        val results = items.map { item ->
            val slug = item.title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
            val url = if (item.type == "movie") "$mainUrl/movies/${item.id}/$slug" else "$mainUrl/tvs/${item.id}/$slug"
            val tvType = if (item.type == "movie") TvType.Movie else TvType.TvSeries
            val posterFixed = fixUrlPath(item.poster)
            Log.d("Mhdflix-Search", "Result: title='${item.title}', poster='$posterFixed', url='$url'")
            newMovieSearchResponse(item.title, url, tvType) {
                this.posterUrl = posterFixed
            }
        }
        
        Log.d("Mhdflix-Search", "=== search END - returning ${results.size} results ===")
        return results
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("Mhdflix-Load", "=== load START - url: $url ===")
        val html = app.get(url, headers = baseHeaders).text
        Log.d("Mhdflix-Load", "Load HTML length: ${html.length}")

        if (url.contains("/episode/")) {
            Log.d("Mhdflix-Load", "Episode page detected")
            return loadEpisodePage(url, html)
        }

        val media = extractMediaFromRsc(html)
        val idFromUrl = Regex("""/(?:movies|tvs)/(\d+)/""").find(url)?.groupValues?.get(1)?.toLongOrNull()
        Log.d("Mhdflix-Load", "idFromUrl: $idFromUrl")

        val title = media?.title
            ?: Jsoup.parse(html).selectFirst("h1")?.text()
            ?: Jsoup.parse(html).selectFirst("title")?.text()?.substringBefore("|")?.trim()
            ?: "Sin título"
        Log.d("Mhdflix-Load", "title: $title")

        val posterUrl = fixUrlPath(media?.poster)
        Log.d("Mhdflix-Load", "posterUrl from media: $posterUrl")
        val finalPoster = if (posterUrl.isNotEmpty()) posterUrl
            else extractPosterFromRsc(parseRscPayload(html), idFromUrl)
            .ifEmpty { Jsoup.parse(html).selectFirst("meta[property='og:image']")?.attr("content") ?: "" }
        Log.d("Mhdflix-Load", "finalPoster: $finalPoster")

        val backdrop = fixUrlPath(media?.backdrop?.takeIf { it.isNotBlank() ?: false })
        Log.d("Mhdflix-Load", "backdrop: $backdrop")

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
        Log.d("Mhdflix-Load", "tvType: $tvType, media.type: ${media?.type}")

        val slug = title.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')

        if (tvType == TvType.Movie) {
            Log.d("Mhdflix-Load", "Returning movie load response")
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
            Log.d("Mhdflix-Load", "Serie detected - serieId: $serieId")
            
            val episodes = if (serieId != null) {
                extractEpisodeListFromRsc(html, serieId).map { ep ->
                    val epUrl = "$mainUrl/tvs/episode/${ep.id}/$slug"
                    Log.d("Mhdflix-Load", "Episode URL: $epUrl")
                    newEpisode(epUrl) {
                        this.name = ep.title
                        this.season = ep.seasonNum
                        this.episode = ep.episodeNum
                        this.description = ep.overview.takeIf { it.isNotBlank() }
                        this.posterUrl = finalPoster
                    }
                }
            } else emptyList()

            Log.d("Mhdflix-Load", "Episodes from RSC: ${episodes.size}")

            val fallbackEpisodes = if (episodes.isEmpty()) {
                Log.d("Mhdflix-Load", "--- Trying DOM fallback for episodes ---")
                val doc = Jsoup.parse(html)
                val epLinks = doc.select("a[href*='/episode/']")
                Log.d("Mhdflix-Load", "Episode links in DOM: ${epLinks.size}")
                
                epLinks.mapNotNull { link ->
                    val href = link.attr("href")
                    val epId = Regex("""/episode/(\d+)""").find(href)?.groupValues?.get(1)?.toLongOrNull() ?: return@mapNotNull null
                    val epTitle = link.text().trim()
                    Log.d("Mhdflix-Load", "DOM episode: id=$epId, title='$epTitle', href='$href'")
                    newEpisode(fixUrl(href)) {
                        this.name = epTitle.ifBlank { "Episodio" }
                        this.season = 1
                        this.episode = 1
                    }
                }.distinctBy { it.data }.take(50)
            } else emptyList()

            val finalEpisodes = episodes.ifEmpty { fallbackEpisodes }
            Log.d("Mhdflix-Load", "Final episodes count: ${finalEpisodes.size}")
            
            if (finalEpisodes.isNotEmpty()) {
                finalEpisodes.take(5).forEachIndexed { i, ep ->
                    Log.d("Mhdflix-Load", "  Ep[$i]: name='${ep.name}', S${ep.season}E${ep.episode}, data='${ep.data}'")
                }
            }

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
                Log.d("Mhdflix-Load", "No episodes found, returning single episode")
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
        Log.d("Mhdflix-EpPage", "=== loadEpisodePage - url: $url ===")
        val rsc = parseRscPayload(html)
        
        val episodeId = Regex("""/episode/(\d+)""").find(url)?.groupValues?.get(1)?.toLongOrNull()
        Log.d("Mhdflix-EpPage", "episodeId: $episodeId")
        
        val epTitlePattern = """"idEpisodios":${episodeId}[^}]*"title":"([^"]*)"""".toRegex().find(rsc)
        val episodeTitle = epTitlePattern?.groupValues?.get(1) ?: "Episodio"
        Log.d("Mhdflix-EpPage", "episodeTitle: $episodeTitle")
        
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
        Log.d("Mhdflix-Links", "=== loadLinks START - data: $data ===")
        
        val contentInfo = extractContentInfo(data)
        if (contentInfo == null) {
            Log.e("Mhdflix-Links", "Could not extract ID from: $data")
            return false
        }
        
        val (id, type) = contentInfo
        val apiUrl = "$apiBaseUrl/api/links?id=$id&type=$type"
        Log.d("Mhdflix-Links", "Extracted: id=$id, type=$type")
        Log.d("Mhdflix-Links", "API URL: $apiUrl")
        
        return try {
            val response = app.get(
                apiUrl,
                headers = mapOf(
                    "User-Agent" to baseHeaders.getValue("User-Agent"),
                    "Referer" to "$mainUrl/",
                )
            ).text
            
            Log.d("Mhdflix-Links", "API Response length: ${response.length}")
            Log.d("Mhdflix-Links", "API Response: $response")
            
            when {
                response.trim().startsWith("[") -> {
                    Log.d("Mhdflix-Links", "Response is JSON array")
                    val links = parseJson<List<ApiLink>>(response)
                    Log.d("Mhdflix-Links", "Parsed ${links.size} links from array")
                    processApiLinks(links, subtitleCallback, callback)
                }
                response.trim().startsWith("{") -> {
                    Log.d("Mhdflix-Links", "Response is JSON object")
                    val singleLink = tryParseJson<ApiLink>(response)
                    if (singleLink != null) {
                        processApiLinks(listOf(singleLink), subtitleCallback, callback)
                    } else {
                        Log.e("Mhdflix-Links", "Could not parse single link object")
                        false
                    }
                }
                else -> {
                    Log.d("Mhdflix-Links", "Unexpected format, trying iframe fallback")
                    extractIframesFromHtml(response, subtitleCallback, callback)
                }
            }
        } catch (e: Exception) {
            Log.e("Mhdflix-Links", "API error: ${e.message}", e)
            Log.d("Mhdflix-Links", "Trying HTML page fallback")
            val html = app.get(data, headers = baseHeaders).text
            Log.d("Mhdflix-Links", "HTML fallback length: ${html.length}")
            extractIframesFromHtml(html, subtitleCallback, callback)
        }
    }

    private suspend fun processApiLinks(
        links: List<ApiLink>,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var found = false
        Log.d("Mhdflix-Links", "Processing ${links.size} API links")
        
        for ((index, link) in links.withIndex()) {
            val videoUrl = link.url ?: link.embedUrl ?: link.iframeUrl
            Log.d("Mhdflix-Links", "Link[$index]: url=$videoUrl, server=${link.server?.name ?: link.serverName}, lang=${link.language?.name ?: link.languageName}")
            
            if (videoUrl.isNullOrBlank() || videoUrl.contains("undefined")) {
                Log.d("Mhdflix-Links", "Link[$index]: Skipping - blank or undefined")
                continue
            }
            
            val serverName = link.server?.name ?: link.serverName ?: "Unknown"
            val languageName = link.language?.name ?: link.languageName ?: "Latino"
            val qualityName = link.quality?.name ?: ""
            
            val linkName = "$serverName - $languageName"
            Log.d("Mhdflix-Links", "Link[$index]: Processing - name='$linkName', quality='$qualityName'")
            
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
                
                Log.d("Mhdflix-Links", "Link[$index]: Creating ExtractorLink - url='$videoUrl', quality=$qualityValue, isM3u8=$isM3u8")
                callback.invoke(
                    newExtractorLink(name, linkName, videoUrl) {
                        this.referer = mainUrl
                        this.quality = qualityValue
                        this.type = linkType
                    }
                )
                found = true
            } else {
                Log.d("Mhdflix-Links", "Link[$index]: Passing to loadExtractor - url='$videoUrl'")
                loadExtractor(fixUrl(videoUrl), mainUrl, subtitleCallback, callback)
                found = true
            }
            
            link.subtitles?.forEach { sub ->
                sub.url?.let { url ->
                    Log.d("Mhdflix-Links", "Link[$index]: Subtitle - name='${sub.name}', url='$url'")
                    subtitleCallback.invoke(
                        newSubtitleFile(sub.name ?: languageName, fixUrl(url))
                    )
                }
            }
        }
        
        Log.d("Mhdflix-Links", "processApiLinks - found=$found")
        return found
    }

    private suspend fun extractIframesFromHtml(
        html: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var found = false
        Log.d("Mhdflix-Links", "extractIframesFromHtml - HTML length: ${html.length}")
        
        val doc = Jsoup.parse(html)
        val iframes = doc.select("iframe[src]")
        Log.d("Mhdflix-Links", "Iframes found: ${iframes.size}")
        
        for (iframe in iframes) {
            val src = iframe.attr("src")
            Log.d("Mhdflix-Links", "Iframe src: $src")
            if (src.isNotBlank() && !src.contains("undefined")) {
                Log.d("Mhdflix-Links", "Loading extractor for iframe: $src")
                loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                found = true
            }
        }
        
        if (!found) {
            Log.d("Mhdflix-Links", "No iframes, searching RSC for URLs")
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
                    Log.d("Mhdflix-Links", "RSC URL found (pattern=$pattern): $url")
                    if (url.isNotBlank() && !url.contains("undefined")) {
                        loadExtractor(fixUrl(url), mainUrl, subtitleCallback, callback)
                        found = true
                    }
                }
            }
        }
        
        Log.d("Mhdflix-Links", "extractIframesFromHtml - found=$found")
        return found
    }

    private fun extractContentInfo(data: String): Pair<String, String>? {
        Regex("""/movies/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            Log.d("Mhdflix-Links", "extractContentInfo - Movie id=$id")
            return Pair(id, "movie")
        }
        Regex("""/tvs/episode/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            Log.d("Mhdflix-Links", "extractContentInfo - Episode id=$id")
            return Pair(id, "episode")
        }
        Regex("""/tvs/(\d+)""").find(data)?.groupValues?.get(1)?.let { id ->
            Log.d("Mhdflix-Links", "extractContentInfo - TV id=$id (treating as episode)")
            return Pair(id, "episode")
        }
        Log.d("Mhdflix-Links", "extractContentInfo - No match for: $data")
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
