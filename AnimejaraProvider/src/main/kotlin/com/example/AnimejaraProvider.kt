package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.nodes.Element

class AnimejaraProvider : MainAPI() {
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
        val doc = app.get(data).document
        doc.select("#iframe-video, #iframe-video-movie").forEach { container ->
            val videoUrl = container.attr("src")
            if (videoUrl.isNotBlank()) {
                loadExtractor(videoUrl, data, subtitleCallback, callback)
            }
        }
        return true
    }
}
