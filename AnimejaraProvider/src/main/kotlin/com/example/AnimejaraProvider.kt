package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Element

class AnimejaraProvider : MainAPI() {
    private val TAG = "AnimeJara"
    override var mainUrl = "https://animejara.com"
    override var name = "AnimeJara"
    override val hasMainPage = true
    override var lang = "es"
    override val supportedTypes = setOf(TvType.Anime, TvType.Movie)

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val items = mutableListOf<HomePageList>()
        val sections = listOf(
            Pair("$mainUrl/catalogo/?tipo=serie", "Últimos Animes"),
            Pair("$mainUrl/catalogo/?idioma=latino", "Latino"),
            Pair("$mainUrl/catalogo/?idioma=japones", "Japonés (Sub)"),
            Pair("$mainUrl/catalogo/?tipo=pelicula", "Películas")
        )
        sections.forEach { (url, title) ->
            try {
                val doc = app.get(url).document
                val list = doc.select("a.anime-card[data-anime]").map { it.toSearchResponse() }
                if (list.isNotEmpty()) {
                    items.add(HomePageList(title, list))
                }
            } catch (_: Exception) { }
        }
        return newHomePageResponse(items, items.isNotEmpty())
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/catalogo/?q=$query").document
        return doc.select("a.anime-card[data-anime]").map { it.toSearchResponse() }
    }

    private fun Element.toSearchResponse(): SearchResponse {
        val json = JSONObject(attr("data-anime"))
        val title = json.getString("titulo")
        val poster = json.optString("poster", "")
        val tipo = json.optString("tipo", "")
        val href = fixUrl(attr("href"))
        val tvType = when {
            tipo.equals("pelicula", true) || tipo.equals("movie", true) || tipo.equals("película", true) -> TvType.Movie
            else -> TvType.Anime
        }

        val langs = select(".card-langs .lang-icon img").map { it.attr("alt") }
        val dubStatus = when {
            langs.any { it.contains("LATINO") || it.contains("CASTELLANO") } -> DubStatus.Dubbed
            langs.any { it.contains("JAPONES") } -> DubStatus.Subbed
            else -> null
        }

        return when (tvType) {
            TvType.Movie -> newMovieSearchResponse(title, href, TvType.Movie) {
                this.posterUrl = poster
            }
            else -> newAnimeSearchResponse(title, href, TvType.Anime) {
                this.posterUrl = poster
                if (dubStatus != null) addDubStatus(dubStatus)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val response = app.get(url)
        val doc = response.document
        val html = response.text

        return if (doc.selectFirst("#iframe-video-movie") != null) {
            loadMovie(url, doc)
        } else {
            loadAnime(url, doc, html)
        }
    }

    private suspend fun loadMovie(url: String, doc: org.jsoup.nodes.Document): LoadResponse {
        val title = doc.selectFirst("title")?.text()?.substringBefore("|")?.trim() ?: ""
        val poster = doc.selectFirst("img.main-poster-img")?.attr("src")
        val description = doc.selectFirst(".anime-sinopsis-contenedor")?.text()

        return newMovieLoadResponse(title, url, TvType.Movie, url) {
            this.posterUrl = poster
            this.plot = description
        }
    }

    private suspend fun loadAnime(url: String, doc: org.jsoup.nodes.Document, html: String): LoadResponse {
        val title = doc.selectFirst("title")?.text()?.substringBefore("|")?.trim() ?: ""

        val posterRegex = Regex("""poster\s*:\s*'([^']+)'""")
        val poster = posterRegex.find(html)?.groupValues?.getOrNull(1)
            ?: doc.selectFirst("img.main-poster-img")?.attr("src")

        val description = doc.selectFirst(".anime-sinopsis-contenedor")?.text()
            ?: doc.selectFirst("meta[name=description]")?.attr("content")

        val pageText = doc.text()
        val status = when {
            pageText.contains("Finalizado", true) -> ShowStatus.Completed
            pageText.contains("Emision", true) -> ShowStatus.Ongoing
            else -> null
        }

        val slug = url.removePrefix("$mainUrl/anime/").trimEnd('/')
        val epRegex = Regex("""TEMPORADAS_DATA\s*=\s*(\[[\s\S]*?\]);""")
        val episodes = mutableListOf<Episode>()
        val epMatch = epRegex.find(html)
        if (epMatch != null) {
            val temporadas = JSONArray(epMatch.groupValues[1])
            for (i in 0 until temporadas.length()) {
                val season = temporadas.getJSONObject(i)
                val seasonNum = season.getInt("numero_temporada")
                val epList = season.getJSONArray("episodios")
                for (j in 0 until epList.length()) {
                    val ep = epList.getJSONObject(j)
                    val epNumStr = ep.getString("numero_episodio")
                    val epNum = epNumStr.toIntOrNull() ?: (j + 1)
                    val epName = ep.optString("nombre_episodio", "")
                    val epPoster = ep.optString("poster_episodio", "")
                    val epUrl = "$mainUrl/episode/$slug-${seasonNum}x$epNum/"

                    episodes.add(newEpisode(epUrl) {
                        this.name = if (epName.isNotBlank()) epName else "Episodio $epNum"
                        this.episode = epNum
                        this.season = seasonNum
                        if (epPoster.isNotBlank()) this.posterUrl = epPoster
                    })
                }
            }
        }

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = description
            this.showStatus = status
            addEpisodes(DubStatus.Subbed, episodes.sortedBy { it.episode })
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "loadLinks called with data: $data")
        var found = false
        var totalExtractorCalls = 0
        var totalLinksProduced = 0
        try {
            val response = app.get(data)
            val doc = response.document
            val html = response.text

            val enlacesRegex = Regex("""const\s+enlaces\s*=\s*(\[[\s\S]*?\]);""")
            val enlacesMatch = enlacesRegex.find(html)
            val langNames = doc.select(".boton-idioma .lang-name").map { it.text() }
            Log.d(TAG, "Languages found: $langNames")

            if (enlacesMatch != null) {
                val enlacesArray = JSONArray(enlacesMatch.groupValues[1])
                Log.d(TAG, "Enlaces array length: ${enlacesArray.length()}")
                for (i in 0 until enlacesArray.length()) {
                    val embedUrl = enlacesArray.getString(i).replace("\\/", "/")
                    val langLabel = langNames.getOrElse(i) { "IDIOMA $i" }
                    Log.d(TAG, "[$langLabel] Processing embed: $embedUrl")
                    try {
                        val embedDoc = app.get(embedUrl, referer = data).document
                        val urlRegex = Regex("""['"](https?://[^"']+)['"]""")
                        val serverElements = embedDoc.select("li[onclick]")
                        Log.d(TAG, "[$langLabel] Found ${serverElements.size} server element(s)")
                        serverElements.forEach { li ->
                            val click = li.attr("onclick")
                            val match = urlRegex.find(click)
                            if (match != null) {
                                val serverUrl = match.groupValues[1]
                                totalExtractorCalls++
                                Log.d(TAG, "[$langLabel] Calling loadExtractor #$totalExtractorCalls for: $serverUrl")
                                loadExtractor(serverUrl, embedUrl, subtitleCallback) { link ->
                                    totalLinksProduced++
                                    Log.d(TAG, "[$langLabel] LINK PRODUCED #$totalLinksProduced: ${link.name} | url=${link.url.take(80)} | quality=${link.quality}")
                                    runBlocking {
                                        callback(newExtractorLink(
                                            source = "[$langLabel] ${link.source}",
                                            name = "[$langLabel] ${link.name}",
                                            url = link.url,
                                        ) {
                                            this.referer = link.referer
                                            this.quality = link.quality
                                            this.type = if (link.isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                                        })
                                    }
                                }
                                found = true
                            } else {
                                Log.w(TAG, "[$langLabel] Could not extract URL from onclick: $click")
                            }
                        }
                    } catch (e: Exception) {
                        Log.w(TAG, "[$langLabel] Embed fetch failed: ${e.message}")
                    }
                }
            } else {
                Log.w(TAG, "No enlaces JS array found in page")
            }

            if (!found) {
                Log.d(TAG, "No enlaces found, trying iframe direct approach")
                doc.select("#iframe-video, #iframe-video-movie").forEach { container ->
                    val embedUrl = container.attr("src")
                    if (embedUrl.isNotBlank()) {
                        Log.d(TAG, "Direct iframe src: $embedUrl")
                        loadExtractor(embedUrl, data, subtitleCallback, callback)
                        found = true
                    }
                    val dataSrc = container.attr("data-src")
                    if (dataSrc.isNotBlank()) {
                        Log.d(TAG, "Direct data-src: $dataSrc")
                        loadExtractor(dataSrc, data, subtitleCallback, callback)
                        found = true
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "loadLinks error: ${e.message}")
        }
        Log.d(TAG, "loadLinks summary: extractorCalls=$totalExtractorCalls, linksProduced=$totalLinksProduced, found=$found")
        return found
    }
}
