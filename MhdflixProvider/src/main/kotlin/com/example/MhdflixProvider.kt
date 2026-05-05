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

    private val baseHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        "Referer" to mainUrl
    )

    private fun fixImageUrl(path: String?): String {
        if (path.isNullOrBlank() || path == "\$undefined") return ""
        return if (path.startsWith("http")) path
        else "https://image.tmdb.org/t/p/w500$path"
    }

    private fun fixBackdropUrl(path: String?): String {
        if (path.isNullOrBlank() || path == "\$undefined") return ""
        return if (path.startsWith("http")) path
        else "https://image.tmdb.org/t/p/original$path"
    }

    private fun parseRscPayload(html: String): String? {
        return try {
            val matches = Regex("""self\.__next_f\.push\(\[1,"(.+?)"\]\)""").findAll(html)
            val combined = matches.joinToString("") { it.groupValues[1] }
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\\n", "")
            combined.ifBlank { null }
        } catch (e: Exception) {
            Log.e("Mhdflix", "Error parsing RSC: ${e.message}")
            null
        }
    }

    private fun extractMediaListFromRsc(rscPayload: String, listKey: String): List<RscMedia> {
        return try {
            val results = mutableListOf<RscMedia>()
            val pattern = """"$listKey":\s*\[(.*?)\]""".toRegex(RegexOption.DOT_MATCHES_ALL).find(rscPayload)
            val arrayStr = pattern?.groupValues?.get(1) ?: return emptyList()
            val itemPattern = """\{[^{}]*(?:"id":\d+[^{}]*)\}""".toRegex().findAll(arrayStr)
            for (match in itemPattern) {
                try {
                    val item = tryParseJson<RscMedia>(match.value)
                    if (item?.id != null) results.add(item)
                } catch (_: Exception) { }
            }
            results
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun extractStructuredData(html: String, key: String): String? {
        return try {
            val rscPayload = parseRscPayload(html) ?: return null
            val pattern = """"$key":\s*(\{[^}]+(?:\{[^}]*\}[^}]*)*\})""".toRegex(RegexOption.DOT_MATCHES_ALL).find(rscPayload)
            pattern?.groupValues?.get(1)
        } catch (e: Exception) {
            null
        }
    }

    private fun extractEpisodeData(html: String): EpisodeRscData? {
        return try {
            val dataStr = extractStructuredData(html, "episode")
            if (dataStr != null) {
                tryParseJson<EpisodeRscData>(dataStr)
            } else null
        } catch (e: Exception) {
            Log.e("Mhdflix", "Error extracting episode data: ${e.message}")
            null
        }
    }

    private fun extractMediaData(html: String): MediaRscData? {
        return try {
            val dataStr = extractStructuredData(html, "media")
            if (dataStr != null) {
                tryParseJson<MediaRscData>(dataStr)
            } else null
        } catch (e: Exception) {
            Log.e("Mhdflix", "Error extracting media data: ${e.message}")
            null
        }
    }

    private fun extractSimilars(html: String): List<RscMedia> {
        return try {
            val rscPayload = parseRscPayload(html) ?: return emptyList()
            extractMediaListFromRsc(rscPayload, "similars")
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun extractServersFromRsc(html: String): List<String> {
        return try {
            val rscPayload = parseRscPayload(html) ?: return emptyList()
            val results = mutableListOf<String>()
            val serverPatterns = listOf(
                Regex("""videoUrlEncrypted":"([^"]+)"""),
                Regex("""serverUrl":"([^"]+)"""),
                Regex("""embedUrl":"([^"]+)"""),
                Regex("""iframeUrl":"([^"]+)"""),
                Regex("""playerUrl":"([^"]+)""")
            )
            for (pattern in serverPatterns) {
                pattern.findAll(rscPayload).forEach { match ->
                    val url = match.groupValues[1]
                    if (url.isNotBlank() && url != "\$undefined") {
                        results.add(fixUrl(url))
                    }
                }
            }
            results.distinct()
        } catch (e: Exception) {
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
        val doc = Jsoup.parse(html)

        val tvType = when {
            request.name.contains("Película") -> TvType.Movie
            request.name.contains("Anime") -> TvType.Anime
            request.name.contains("Dorama") -> TvType.TvSeries
            else -> TvType.TvSeries
        }

        val items = doc.select("a[href*='/movies/'], a[href*='/tvs/']").mapNotNull { link ->
            val href = link.attr("href")
            if (!href.startsWith("/movies/") && !href.startsWith("/tvs/")) return@mapNotNull null
            if (href.contains("/episode/")) return@mapNotNull null
            if (href.contains("/category/")) return@mapNotNull null
            if (href.contains("/genres")) return@mapNotNull null

            val img = link.selectFirst("img")
            val poster = img?.run {
                attr("src").ifBlank { attr("data-src") }.ifBlank {
                    val style = attr("style")
                    Regex("""url\(['"]?([^'")\s]+)['"]?\)""").find(style)?.groupValues?.get(1)
                }
            } ?: ""

            val title = img?.attr("alt")
                ?: link.selectFirst("h3, h4, p, span")?.text()
                ?: ""

            if (title.isNotBlank() && title.length > 1) {
                newMovieSearchResponse(title, fixUrl(href), tvType) {
                    this.posterUrl = fixImageUrl(poster)
                }
            } else null
        }.distinctBy { it.url }.take(20)

        return if (items.isNotEmpty()) {
            newHomePageResponse(
                list = HomePageList(request.name, items),
                hasNext = true
            )
        } else null
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/search?q=${query.replace(" ", "+")}"
        val html = app.get(url, headers = baseHeaders).text
        val doc = Jsoup.parse(html)

        return doc.select("a[href*='/movies/'], a[href*='/tvs/']").mapNotNull { link ->
            val href = link.attr("href")
            if (!href.startsWith("/movies/") && !href.startsWith("/tvs/")) return@mapNotNull null
            if (href.contains("/episode/")) return@mapNotNull null

            val img = link.selectFirst("img")
            val poster = img?.run { attr("src").ifBlank { attr("data-src") } } ?: ""
            val title = img?.attr("alt") ?: link.selectFirst("h3, h4, p, span")?.text() ?: ""

            if (title.isNotBlank() && title.contains(query, ignoreCase = true)) {
                val tvType = if (href.startsWith("/movies/")) TvType.Movie else TvType.TvSeries
                newMovieSearchResponse(title, fixUrl(href), tvType) {
                    this.posterUrl = fixImageUrl(poster)
                }
            } else null
        }.distinctBy { it.url }.take(20)
    }

    override suspend fun load(url: String): LoadResponse? {
        val html = app.get(url, headers = baseHeaders).text

        if (url.contains("/episode/")) {
            return loadEpisode(url, html)
        }

        val mediaData = extractMediaData(html)

        val title = mediaData?.title
            ?: Jsoup.parse(html).selectFirst("h1")?.text()?.trim()
            ?: Jsoup.parse(html).selectFirst("title")?.text()?.substringBefore("|")?.trim()
            ?: "Sin título"

        val poster = fixImageUrl(mediaData?.poster)
            ?: Jsoup.parse(html).selectFirst("meta[property='og:image']")?.attr("content")
            ?: ""

        val backdrop = fixBackdropUrl(mediaData?.backdrop)

        val description = mediaData?.description
            ?: Jsoup.parse(html).selectFirst("meta[name='description']")?.attr("content")
            ?: ""

        val year = mediaData?.date?.substring(0, 4)?.toIntOrNull()

        val rating = mediaData?.rating?.let { Score.from10(it.toFloat()) }

        val tags = mediaData?.genre?.filterNotNull()

        val duration = mediaData?.duration?.let { durText ->
            val h = Regex("""(\d+)h""").find(durText)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val m = Regex("""(\d+)min""").find(durText)?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val pureM = if (h == 0 && m == 0) Regex("""(\d+)""").find(durText)?.groupValues?.get(1)?.toIntOrNull() ?: 0 else 0
            (h * 60) + m + pureM
        }

        val tvType = if (mediaData?.type == "movie" || url.contains("/movies/")) TvType.Movie else TvType.TvSeries

        val recommendations = extractSimilars(html).mapNotNull { rec ->
            if (rec.title.isNullOrBlank()) return@mapNotNull null
            val recType = if (rec.type == "movie") TvType.Movie else TvType.TvSeries
            val recUrl = if (rec.slug != null) {
                if (rec.type == "movie") "$mainUrl/movies/${rec.id}/${rec.slug}"
                else "$mainUrl/tvs/${rec.id}/${rec.slug}"
            } else url
            newMovieSearchResponse(rec.title, recUrl, recType) {
                this.posterUrl = fixImageUrl(rec.poster)
            }
        }.take(12)

        if (tvType == TvType.Movie) {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.backgroundPosterUrl = backdrop
                this.plot = description
                this.year = year
                this.tags = tags
                this.score = rating
                this.duration = duration
                this.recommendations = recommendations
            }
        } else {
            val episodes = mutableListOf<Episode>()

            val epMatches = Regex("""/tvs/episode/(\d+)/[^"'\s]+""").findAll(html)
            val seasonIdMatches = Regex("""seasson=(\d+)""").findAll(html)
            val seasons: List<String> = seasonIdMatches.map { it.groupValues[1] }.toList().distinct()

            if (seasons.isNotEmpty()) {
                for ((idx, season) in seasons.withIndex()) {
                    val seasonHtml = app.get("$url?seasson=$season", headers = baseHeaders).text
                    val seasonEpMatches = Regex("""/tvs/episode/(\d+)/[^"'\s]+""").findAll(seasonHtml)
                    for (epMatch in seasonEpMatches) {
                        val epUrl = fixUrl(epMatch.groupValues[0])
                        val epTitle = Regex("""T\d+\s*·\s*EP\d+\s*-\s*(.+)""").find(seasonHtml)?.groupValues?.get(1)
                            ?: "Episodio ${episodes.size + 1}"
                        episodes.add(newEpisode(epUrl) {
                            this.name = epTitle
                            this.episode = episodes.size + 1
                            this.season = idx + 1
                            this.posterUrl = poster
                        })
                    }
                }
            } else {
                for (epMatch in epMatches) {
                    val epUrl = fixUrl(epMatch.groupValues[0])
                    episodes.add(newEpisode(epUrl) {
                        this.name = "Episodio ${episodes.size + 1}"
                        this.episode = episodes.size + 1
                        this.season = 1
                        this.posterUrl = poster
                    })
                }
            }

            return if (episodes.isNotEmpty()) {
                newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes.distinctBy { it.data }) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backdrop
                    this.plot = description
                    this.year = year
                    this.tags = tags
                    this.score = rating
                    this.duration = duration
                    this.recommendations = recommendations
                }
            } else {
                newTvSeriesLoadResponse(title, url, TvType.TvSeries, listOf(newEpisode(url) {
                    this.name = title
                    this.posterUrl = poster
                    this.description = description
                })) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backdrop
                    this.plot = description
                    this.year = year
                    this.tags = tags
                    this.score = rating
                    this.duration = duration
                    this.recommendations = recommendations
                }
            }
        }
    }

    private suspend fun loadEpisode(url: String, html: String): LoadResponse? {
        val episodeData = extractEpisodeData(html)

        val episodeTitle = episodeData?.title ?: "Episodio"
        val serieTitle = episodeData?.serie?.title ?: ""
        val fullTitle = if (serieTitle.isNotBlank()) "$serieTitle - $episodeTitle" else episodeTitle

        val description = episodeData?.overview ?: ""

        val poster = fixImageUrl(episodeData?.posterPath)
            ?: fixImageUrl(episodeData?.serie?.poster)
            ?: ""

        val seasonNum = episodeData?.season?.seasonNumber ?: 1
        val epNum = episodeData?.numEpisode?.toString()?.toIntOrNull() ?: 1

        val serieUrl = if (episodeData?.serie?.id != null && episodeData.serie.slug != null) {
            "$mainUrl/tvs/${episodeData.serie.id}/${episodeData.serie.slug}"
        } else url

        return newTvSeriesLoadResponse(fullTitle, serieUrl, TvType.TvSeries, listOf(newEpisode(url) {
            this.name = episodeTitle
            this.season = seasonNum
            this.episode = epNum
            this.posterUrl = poster
            this.description = description
        })) {
            this.posterUrl = poster
            this.plot = description
            this.year = episodeData?.serie?.date?.substring(0, 4)?.toIntOrNull()
            this.tags = episodeData?.serie?.genre?.filterNotNull()
        }
    }

    private val apiBaseUrl = "https://ww1.mhdflix.com"

    private fun extractContentInfo(data: String): Pair<String, String>? {
        val movieRegex = Regex("""/movies/(\d+)/""")
        val episodeRegex = Regex("""/tvs/episode/(\d+)/""")
        
        movieRegex.find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "movie")
        }
        episodeRegex.find(data)?.groupValues?.get(1)?.let { id ->
            return Pair(id, "episode")
        }
        return null
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
            Log.e("Mhdflix", "loadLinks - Could not extract ID from URL")
            return false
        }
        
        val (id, type) = contentInfo
        val apiUrl = "$apiBaseUrl/api/links?id=$id&type=$type"
        Log.d("Mhdflix", "loadLinks - Calling API: $apiUrl")
        
        return try {
            val response = app.get(
                apiUrl,
                headers = mapOf(
                    "User-Agent" to baseHeaders.getValue("User-Agent"),
                    "Referer" to "$mainUrl/",
                    "Accept" to "application/json",
                )
            ).text
            
            Log.d("Mhdflix", "loadLinks - API Response: $response")
            
            val links = parseJson<List<ApiLink>>(response)
            var found = false
            
            for (link in links) {
                val videoUrl = link.url ?: continue
                val serverName = link.server?.name ?: "Unknown"
                val languageName = link.language?.name ?: "Unknown"
                val qualityName = link.quality?.name ?: ""
                
                Log.d("Mhdflix", "loadLinks - Processing: server=$serverName, lang=$languageName, quality=$qualityName")
                
                if (videoUrl.startsWith("http")) {
                    val extractorName = "$serverName - $languageName"
                    val qualityValue = when {
                        qualityName.contains("4k", ignoreCase = true) || qualityName.contains("2160") -> Qualities.P2160.value
                        qualityName.contains("1080") -> Qualities.P1080.value
                        qualityName.contains("720") -> Qualities.P720.value
                        qualityName.contains("480") -> Qualities.P480.value
                        qualityName.contains("360") -> Qualities.P360.value
                        else -> Qualities.Unknown.value
                    }
                    
                    callback.invoke(
                        newExtractorLink(name, extractorName, videoUrl) {
                            this.referer = mainUrl
                            this.quality = qualityValue
                            this.type = if (videoUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                        }
                    )
                    
                    link.subtitles?.forEach { sub ->
                        sub.url?.let { url ->
                            subtitleCallback.invoke(
                                newSubtitleFile(sub.name ?: languageName, fixUrl(url))
                            )
                        }
                    }
                    
                    found = true
                } else {
                    loadExtractor(fixUrl(videoUrl), mainUrl, subtitleCallback, callback)
                    found = true
                }
            }
            
            if (!found && response.contains("url")) {
                Log.d("Mhdflix", "loadLinks - Fallback to iframe extraction")
                val iframes = Regex("""iframe.*?src=["'](.*?)["']""").findAll(response)
                iframes.forEach { match ->
                    val src = match.groupValues[1]
                    if (src.isNotBlank()) {
                        loadExtractor(fixUrl(src), mainUrl, subtitleCallback, callback)
                        found = true
                    }
                }
            }
            
            found
        } catch (e: Exception) {
            Log.e("Mhdflix", "loadLinks - Error: ${e.message}")
            false
        }
    }

    data class ApiLink(
        @JsonProperty("url") val url: String? = null,
        @JsonProperty("language") val language: ApiLanguage? = null,
        @JsonProperty("quality") val quality: ApiQuality? = null,
        @JsonProperty("server") val server: ApiServer? = null,
        @JsonProperty("subtitles") val subtitles: List<ApiSubtitle>? = emptyList()
    )
    
    data class ApiLanguage(
        @JsonProperty("name") val name: String? = null
    )
    
    data class ApiQuality(
        @JsonProperty("name") val name: String? = null
    )
    
    data class ApiServer(
        @JsonProperty("name") val name: String? = null
    )
    
    data class ApiSubtitle(
        @JsonProperty("url") val url: String? = null,
        @JsonProperty("name") val name: String? = null
    )

    data class RscMedia(
        @JsonProperty("id") val id: Long? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("poster") val poster: String? = null,
        @JsonProperty("backdrop") val backdrop: String? = null,
        @JsonProperty("genre") val genre: List<String?> = emptyList(),
        @JsonProperty("date") val date: String? = null,
        @JsonProperty("duration") val duration: String? = null,
        @JsonProperty("rating") val rating: Int? = null,
        @JsonProperty("description") val description: String? = null,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("slug") val slug: String? = null,
    )

    data class MediaRscData(
        @JsonProperty("id") val id: Long? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("poster") val poster: String? = null,
        @JsonProperty("backdrop") val backdrop: String? = null,
        @JsonProperty("genre") val genre: List<String?> = emptyList(),
        @JsonProperty("date") val date: String? = null,
        @JsonProperty("duration") val duration: String? = null,
        @JsonProperty("rating") val rating: Int? = null,
        @JsonProperty("description") val description: String? = null,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("slug") val slug: String? = null,
        @JsonProperty("trailers") val trailers: List<String?> = emptyList(),
        @JsonProperty("logo") val logo: String? = null,
        @JsonProperty("views") val views: Int? = null,
    )

    data class EpisodeRscData(
        @JsonProperty("idEpisodios") val idEpisodios: Long? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("overview") val overview: String? = null,
        @JsonProperty("airDate") val airDate: String? = null,
        @JsonProperty("numEpisode") val numEpisode: Any? = null,
        @JsonProperty("numSeason") val numSeason: Int? = null,
        @JsonProperty("posterPath") val posterPath: String? = null,
        @JsonProperty("mediaId") val mediaId: String? = null,
        @JsonProperty("serieTitle") val serieTitle: String? = null,
        @JsonProperty("serie") val serie: SerieRscData? = null,
        @JsonProperty("season") val season: SeasonRscData? = null,
        @JsonProperty("nextEpisode") val nextEpisode: String? = null,
        @JsonProperty("prevEpisode") val prevEpisode: PrevEpisodeData? = null,
    )

    data class SerieRscData(
        @JsonProperty("id") val id: Long? = null,
        @JsonProperty("title") val title: String? = null,
        @JsonProperty("poster") val poster: String? = null,
        @JsonProperty("backdrop") val backdrop: String? = null,
        @JsonProperty("genre") val genre: List<String?> = emptyList(),
        @JsonProperty("date") val date: String? = null,
        @JsonProperty("duration") val duration: String? = null,
        @JsonProperty("rating") val rating: Int? = null,
        @JsonProperty("description") val description: String? = null,
        @JsonProperty("type") val type: String? = null,
        @JsonProperty("slug") val slug: String? = null,
    )

    data class SeasonRscData(
        @JsonProperty("id") val id: Long? = null,
        @JsonProperty("name") val name: String? = null,
        @JsonProperty("overview") val overview: String? = null,
        @JsonProperty("posterPath") val posterPath: String? = null,
        @JsonProperty("seasonNumber") val seasonNumber: Int? = null,
        @JsonProperty("isSerie") val isSerie: Long? = null,
        @JsonProperty("serieSlug") val serieSlug: String? = null,
    )

    data class PrevEpisodeData(
        @JsonProperty("idEpisodios") val idEpisodios: Long? = null,
        @JsonProperty("title") val title: String? = null,
    )
}
