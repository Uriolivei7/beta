// ! Bu araç @ByAyzen tarafından | @CS-Karma için yazılmıştır.

package com.example

import android.util.Log
import org.json.JSONObject
import org.jsoup.nodes.Element
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.LoadResponse.Companion.addActors
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer

class DoramaslatinoxProvider : MainAPI() {
    override var mainUrl = "https://doramafox.es"
    override var name = "DoramasLatinoX"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasQuickSearch = false
    override val supportedTypes = setOf(TvType.AsianDrama)
    //Movie, AnimeMovie, TvSeries, Cartoon, Anime, OVA, Torrent, Documentary, AsianDrama, Live, NSFW, Others, Music, AudioBook, CustomMedia, Audio, Podcast,

    override val mainPage = mainPageOf(
        "$mainUrl/movies/" to "Todas las Películas",
        "$mainUrl/estado/completo/" to "Completos",
        "$mainUrl/estado/emision/" to "En Emision",
        "$mainUrl/audio/latino/" to "Latino",
        "$mainUrl/audio/subtitulado" to "Subtitulado",
        "$mainUrl/tipo/dorama/" to "Doramas",
        "$mainUrl/tipo/serie/" to "Series",
        "$mainUrl/pais/corea-del-sur/" to "Corea del Sur",
        "$mainUrl/pais/china/" to "China",
        "$mainUrl/pais/japon/" to "Japón",
        "$mainUrl/pais/tailandia/" to "Tailandia",
        "$mainUrl/pais/taiwan/" to "Taiwán",
        "$mainUrl/pais/singapur/" to "Singapur",
        "$mainUrl/pais/estados-unidos/" to "Estados Unidos",
        "$mainUrl/series/" to "Todas las Series"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val url = if (page <= 1) {
            request.data
        } else {
            "${request.data.removeSuffix("/")}/page/$page/"
        }

        val document = app.get(url).document
        val home = document.select("article.item").mapNotNull {
            it.toMainPageResult()
        }

        return newHomePageResponse(
            listOf(HomePageList(request.name, home)),
            hasNext = home.isNotEmpty()
        )
    }

    private fun Element.toMainPageResult(): SearchResponse? {
        val title = this.selectFirst("div.data h3 a")?.text()?.trim() ?: return null
        val href = this.selectFirst("div.poster a")?.attr("href") ?: return null
        val posterUrl = fixUrlNull(
            this.selectFirst("div.poster img")?.attr("src")
                ?: this.selectFirst("div.poster img")?.attr("data-src")
        )

        val type = if (this.hasClass("tvshows")) TvType.TvSeries else TvType.Movie

        return newMovieSearchResponse(title, href, type) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String, page: Int): SearchResponseList {
        val encoded = java.net.URLEncoder.encode(query, "UTF-8")
        val url = if (page == 1) {
            "$mainUrl/?s=$encoded"
        } else {
            "$mainUrl/page/$page/?s=$encoded"
        }

        val document = try {
            app.get(url, headers = mapOf("User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")).document
        } catch (e: Exception) {
            Log.e("DoramasLX", "Search error: ${e.message}")
            return newSearchResponseList(emptyList(), false)
        }
        val results = document.select("div.result-item article").mapNotNull {
            val titleElement = it.selectFirst("div.details div.title a") ?: return@mapNotNull null
            val title = titleElement.text()
            val href = titleElement.attr("href")
            val posterUrl = it.selectFirst("div.image div.thumbnail img")?.attr("src")

            newMovieSearchResponse(title, href, TvType.TvSeries) {
                this.posterUrl = posterUrl
            }
        }

        return newSearchResponseList(results, hasNext = results.isNotEmpty())
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = search(query)

    override suspend fun load(url: String): LoadResponse? {
        val document = app.get(url).document

        val title = document.selectFirst("h1")?.text()?.trim() ?: return null
        val poster = fixUrlNull(document.selectFirst("meta[property=og:image]")?.attr("content"))
        val description =
            document.selectFirst("meta[property=og:description]")?.attr("content")?.trim()
        val year = document.selectFirst("div.extra span")?.text()?.trim()?.toIntOrNull()

        val tags = document.select("div.sgeneros a").map { it.text() }
        val rating = document.selectFirst("span.dt_rating_vgs")?.text()?.trim()?.toDoubleOrNull()
        val duration =
            document.selectFirst("span.runtime")?.text()?.replace(Regex("[^0-9]"), "")?.trim()
                ?.toIntOrNull()

        val trailer = document.selectFirst("iframe[src*='youtube']")?.attr("src")
            ?: Regex("""embed\/(.*)\?rel""").find(document.html())?.groupValues?.get(1)
                ?.let { "https://www.youtube.com/embed/$it" }
        val actors = document.select("div.person").mapNotNull {
            val name = it.selectFirst("div.name a")?.text() ?: return@mapNotNull null
            val role = it.selectFirst("div.caracter")?.text()
            val image = it.selectFirst("img")?.attr("src")

            val actor = Actor(name, image)
            actor to role
        }

        val recommendations =
            document.select("div.srelacionados article").mapNotNull { it.toRecommendationResult() }

        val isTvSeries = document.selectFirst("ul.episodios") != null

        if (isTvSeries) {
            val episodes = document.select("ul.episodios a").mapNotNull {
                val href = fixUrlNull(it.attr("href")) ?: return@mapNotNull null
                val numText = it.selectFirst("div.numerando")?.text() ?: ""
                val season = numText.split("-").firstOrNull()?.trim()?.toIntOrNull() ?: 1
                val episode = numText.split("-").lastOrNull()?.trim()?.toIntOrNull() ?: 1
                val epTitle = it.selectFirst("div.episodiotitle")?.ownText()?.trim()
                val thumb = it.selectFirst("img")?.attr("src")

                newEpisode(href) {
                    this.name = epTitle
                    this.season = season
                    this.episode = episode
                    this.posterUrl = thumb
                }
            }

            return newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.tags = tags
                this.score = Score.from10(rating)
                this.recommendations = recommendations
                addActors(actors)
                addTrailer(trailer)
            }
        } else {
            return newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.tags = tags
                this.duration = duration
                this.score = Score.from10(rating)
                this.recommendations = recommendations
                addActors(actors)
                addTrailer(trailer)
            }
        }
    }

    private fun Element.toRecommendationResult(): SearchResponse? {
        val title = this.selectFirst("h3 a")?.text()
            ?: this.selectFirst("img")?.attr("alt")
            ?: return null
        val href = fixUrlNull(this.selectFirst("a")?.attr("href")) ?: return null
        val posterUrl = fixUrlNull(this.selectFirst("img")?.attr("src"))

        return newMovieSearchResponse(title, href, TvType.Movie) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("DoramasLX", "loadLinks llamado data=$data")
        var found = false
        val document = try {
            app.get(data).document
        } catch (e: Exception) {
            Log.e("DoramasLX", "Error loading page: ${e.message}")
            return false
        }

        val options = document.select("li.dooplay_player_option")
        Log.d("DoramasLX", "Opciones encontradas: ${options.size}")
        if (options.isEmpty()) {
            val html = document.html()
            val m3u8s = Regex("""https?://[^"'\s<>]+\.m3u8[^"'\s<>]*""").findAll(html)
                .map { it.value.replace("\\/", "/") }.toList()
            if (m3u8s.isNotEmpty()) {
                Log.d("DoramasLX", "m3u8 directo en página: ${m3u8s.size}")
                m3u8s.forEach { url ->
                    callback(newExtractorLink(name, name, url, ExtractorLinkType.M3U8) {
                        this.referer = data; this.quality = getQualityFromName(url)
                    })
                }
                return true
            }
            return false
        }

        options.forEach { option ->
            val post = option.attr("data-post")
            val type = option.attr("data-type")
            val nume = option.attr("data-nume")
            Log.d("DoramasLX", "Opción: post=$post type=$type nume=$nume")
            if (post.isEmpty()) return@forEach

            val apiUrl = "$mainUrl/wp-json/dooplayer/v2/$post/$type/$nume"
            Log.d("DoramasLX", "API URL: $apiUrl")
            try {
                val response = app.get(apiUrl).text
                Log.d("DoramasLX", "API respuesta: ${response.take(200)}")
                val json = try { JSONObject(response) } catch (_: Exception) { null }
                val embedUrl = json?.optString("embed_url", "")?.replace("\\/", "/") ?: ""
                if (embedUrl.isBlank()) { Log.e("DoramasLX", "embed_url vacío"); return@forEach }
                Log.d("DoramasLX", "embedUrl: $embedUrl")

                val icuUrl = embedUrl.replace("short.ink", "short.icu")
                val hash = embedUrl.substringAfterLast("/").substringBefore("?")

                // Generar todas las URLs candidatas para esta opción
                val candidates = mutableListOf<String>()
                candidates.add(embedUrl)
                if (icuUrl != embedUrl) candidates.add(icuUrl)
                if (hash.isNotBlank()) {
                    candidates.add("https://doramasfoxito.p2pplay.online/#/$hash")
                    // También probar foxito sin hash (por si el ID es otro)
                    candidates.add("https://doramasfoxito.p2pplay.online/#/$embedUrl")
                }

                // Probar cada candidato con extractores (trackeando links reales)
                for (candidate in candidates.distinct()) {
                    Log.d("DoramasLX", "Probando candidato: $candidate")
                    if (extractWithTracking(candidate, data, subtitleCallback, callback)) {
                        found = true; return@forEach
                    }
                }

                // Último recurso: seguir cadena de redirects JS hasta encontrar video
                for (pageUrl in listOf(embedUrl, icuUrl).distinct()) {
                    if (followRedirectChain(pageUrl, data, 0, subtitleCallback, callback)) {
                        found = true; return@forEach
                    }
                }
            } catch (e: Exception) {
                Log.e("DoramasLX", "Error en opción $nume: ${e.message}")
            }
        }
        Log.d("DoramasLX", "loadLinks finaliza found=$found")
        return found
    }

    /** Llama loadExtractor pero solo retorna true si el callback fue invocado */
    private suspend fun extractWithTracking(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var produced = false
        val trackingCallback: (ExtractorLink) -> Unit = { link ->
            produced = true
            callback(link)
        }
        try {
            loadExtractor(url, referer, subtitleCallback, trackingCallback)
        } catch (_: Exception) { }
        return produced
    }

    /** Sigue redirects JS hasta maxDepth, probando extractores en cada paso */
    private suspend fun followRedirectChain(
        startUrl: String,
        referer: String?,
        depth: Int = 0,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        if (depth > 4) return false
        Log.d("DoramasLX", "followRedirectChain depth=$depth: $startUrl")

        // Probar extractor directo
        if (extractWithTracking(startUrl, referer, subtitleCallback, callback)) return true

        // Construir foxUrl desde hash
        val h = startUrl.substringAfterLast("/").substringBefore("?")
        if (h.isNotBlank() && h.length < 100) {
            val foxUrl = "https://doramasfoxito.p2pplay.online/#/$h"
            if (extractWithTracking(foxUrl, startUrl, subtitleCallback, callback)) return true
        }

        // Cargar página
        val doc = try { app.get(startUrl, referer = referer).document } catch (_: Exception) { return false }
        val html = doc.html()

        // Buscar TODAS las URLs en la página y probar cada una
        val allUrls = Regex("""https?://[^"'\s<>)]+""").findAll(html)
            .map { it.value.replace("\\/", "/") }.distinct().toList()
        Log.d("DoramasLX", "URLs encontradas en depth=$depth: ${allUrls.size}")
        for (url in allUrls) {
            if (extractWithTracking(url, startUrl, subtitleCallback, callback)) return true
            // También probar foxUrl desde cada URL
            val uh = url.substringAfterLast("/").substringBefore("?")
            if (uh.isNotBlank() && uh.length < 100) {
                val fUrl = "https://doramasfoxito.p2pplay.online/#/$uh"
                if (fUrl != url && extractWithTracking(fUrl, url, subtitleCallback, callback)) return true
            }
        }

        // Buscar m3u8 directo
        val m3u8s = allUrls.filter { it.contains(".m3u8", ignoreCase = true) }
        if (m3u8s.isNotEmpty()) {
            Log.d("DoramasLX", "m3u8 en depth=$depth: ${m3u8s.size}")
            m3u8s.forEach { url ->
                callback(newExtractorLink(name, name, url, ExtractorLinkType.M3U8) {
                    this.referer = startUrl; this.quality = getQualityFromName(url)
                })
            }
            return true
        }

        // Buscar iframe y seguirlo
        val iframeSrc = doc.selectFirst("iframe")?.attr("src")
        if (!iframeSrc.isNullOrBlank()) {
            Log.d("DoramasLX", "iframe depth=$depth: $iframeSrc")
            if (followRedirectChain(iframeSrc, startUrl, depth + 1, subtitleCallback, callback)) return true
        }

        // Buscar JS redirect y seguirlo
        val redirectRegex = Regex("""(?:location\.href|window\.location)\s*=\s*['"]([^'"]+)['"]""")
        for (m in redirectRegex.findAll(html)) {
            val ru = m.groupValues[1].replace("\\/", "/")
            Log.d("DoramasLX", "JS redirect depth=$depth: $ru")
            if (followRedirectChain(ru, startUrl, depth + 1, subtitleCallback, callback)) return true
        }

        // Buscar base64 encodeado que podría tener URLs de video
        val b64Regex = Regex("""[A-Za-z0-9+/]{40,}={0,2}""")
        for (m in b64Regex.findAll(html)) {
            try {
                val decoded = java.net.URLDecoder.decode(
                    String(android.util.Base64.decode(m.value, android.util.Base64.DEFAULT)),
                    "UTF-8"
                )
                val decodedUrls = Regex("""https?://[^"'\s<>)]+""").findAll(decoded)
                    .map { it.value.replace("\\/", "/") }.toList()
                for (url in decodedUrls) {
                    if (extractWithTracking(url, startUrl, subtitleCallback, callback)) return true
                }
            } catch (_: Exception) { }
        }

        return false
    }
}