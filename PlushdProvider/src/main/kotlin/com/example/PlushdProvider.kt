package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.nodes.Element
import android.util.Base64
import kotlinx.coroutines.delay
import java.net.URL

class PlushdProvider : MainAPI() {
    override var mainUrl = "https://tioplus.app"
    override var name = "PlusHD"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries,
        TvType.Anime,
        TvType.Cartoon,
        TvType.Movie,
        TvType.AsianDrama,
    )

    override val mainPage = mainPageOf(
        "series" to "Series",
        "animes" to "Animes",
        "peliculas" to "Peliculas",
        "doramas" to "Doramas",
    )

    private fun base64Encode(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    private fun fixPelisplusHostsLinks(url: String): String {
        if (url.isBlank()) return url

        return when {
            // Vidhide / Earnvids: El extractor prefiere /e/ para saltar anuncios
            url.contains("vidhide") || url.contains("mivalyo") || url.contains("dinisglows") || url.contains("dhtpre") -> {
                url.replace(Regex("vidhideplus\\.com|vidhidepre\\.com|mivalyo\\.com|dinisglows\\.com|dhtpre\\.com"), "vidhidepro.com")
                    .replace("/v/", "/e/")
            }

            // Netu/Waaw: Forzamos hqq.tv que es el extractor que sí tiene los subs
            url.contains("waaw.to") || url.contains("netu.to") -> {
                url.replace(Regex("/[fv]/"), "/e/").replace("waaw.to", "hqq.tv").replace("netu.to", "hqq.tv")
            }

            // Pelisplus / Upstream / Plus: Limpieza de Hash y normalización
            url.contains("upns.pro") || url.contains("rpmstream") || url.contains("strp2p") || url.contains("emturbovid") -> {
                url.replace("/#", "/").replace("emturbovid.com", "turbovid.eu")
            }

            else -> url.replace("/#", "/")
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get("$mainUrl/${request.data}/$page").document
        val home = document.select(".articlesList article").filter { !it.selectFirst("a")?.attr("target").equals("_blank") }
            .mapNotNull { it.toSearchResult() }
        return newHomePageResponse(
            list = HomePageList(
                name = request.name,
                list = home,
                isHorizontalImages = false
            ),
            hasNext = true
        )
    }

    private fun Element.toSearchResult(): SearchResponse? {
        var title = this.selectFirst("a h2")?.text() ?: return null
        val link = this.selectFirst("a.itemA")?.attr("href") ?: return null
        val img = this.selectFirst("picture img")?.attr("data-src")

        val yearRegex = Regex("""\s*\((\d{4})\)$""")
        val match = yearRegex.find(title)
        val year = match?.groupValues?.get(1)?.toIntOrNull()

        if (match != null) {
            title = title.replace(yearRegex, "").trim()
            Log.d("PlushdProvider", "Título limpiado: $title | Año detectado: $year")
        }

        val searchType = when {
            link.contains("/serie") -> TvType.TvSeries
            link.contains("/anime") -> TvType.Anime
            link.contains("/pelicula") -> TvType.Movie
            link.contains("/dorama") -> TvType.AsianDrama
            else -> TvType.Movie
        }

        return when (searchType) {
            TvType.Movie -> newMovieSearchResponse(title, link, searchType) {
                this.posterUrl = img
                this.year = year // Asignamos el año aquí
            }
            else -> newTvSeriesSearchResponse(title, link, searchType) {
                this.posterUrl = img
                this.year = year // Asignamos el año aquí
            }
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val document = app.get("$mainUrl/api/search/$query").document
        val results = document.select("article.item").mapNotNull { it.toSearchResult() }
        return results
    }


    data class MainTemporada(val elements: Map<String, List<MainTemporadaElement>>)

    data class MainTemporadaElement(
        val title: String? = null,
        val image: String? = null,
        val season: Int? = null,
        val episode: Int? = null
    )

    override suspend fun load(url: String): LoadResponse? {
        val doc = app.get(url).document

        val tvType = when {
            url.contains("/serie") -> TvType.TvSeries
            url.contains("/anime") -> TvType.Anime
            url.contains("/pelicula") -> TvType.Movie
            url.contains("/dorama") -> TvType.AsianDrama
            else -> TvType.TvSeries
        }

        var title = doc.selectFirst(".slugh1")?.text() ?: ""

        // Aplicamos la misma lógica de limpieza
        val yearRegex = Regex("""\s*\((\d{4})\)$""")
        val match = yearRegex.find(title)
        val year = match?.groupValues?.get(1)?.toIntOrNull()

        if (match != null) {
            title = title.replace(yearRegex, "").trim()
            Log.d("PlushdProvider", "Cargando: $title (Año: $year)")
        }

        val backimage = doc.selectFirst("head meta[property=og:image]")?.attr("content") ?: ""
        val poster = backimage.replace("original", "w500")
        val description = doc.selectFirst("div.description")?.text() ?: ""
        val tags = doc.select("div.home__slider .genres:contains(Generos) a").map { it.text() }
        val epi = ArrayList<Episode>()

        if (tvType != TvType.Movie) {
            val script = doc.select("script").firstOrNull { it.html().contains("seasonsJson = ") }?.html()
            if (!script.isNullOrEmpty()) {
                val jsonRegex = Regex("seasonsJson\\s*=\\s*(\\{[^;]*\\});")
                val match = jsonRegex.find(script)
                val jsonscript: String? = match?.groupValues?.get(1)

                if (!jsonscript.isNullOrEmpty()) {
                    try {
                        val seasonsMap = parseJson<Map<String, List<MainTemporadaElement>>>(jsonscript)
                        seasonsMap.values.map { list ->
                            list.map { info ->
                                val epTitle = info.title
                                val seasonNum = info.season
                                val epNum = info.episode
                                val img = info.image
                                val realimg =
                                    if (img.isNullOrEmpty()) null else "https://image.tmdb.org/t/p/w342${
                                        img.replace(
                                            "\\/",
                                            "/"
                                        )
                                    }"
                                val epurl = "$url/season/$seasonNum/episode/$epNum"
                                if (epTitle != null && seasonNum != null && epNum != null) {
                                    epi.add(
                                        newEpisode(epurl) {
                                            this.name = epTitle
                                            this.season = seasonNum
                                            this.episode = epNum
                                            this.posterUrl = realimg
                                        }
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("PlushdProvider", "Error al parsear seasonsJson: ${e.message}")
                    }
                }
            }
        }

        return when (tvType) {
            TvType.TvSeries, TvType.Anime, TvType.AsianDrama -> {
                newTvSeriesLoadResponse(title, url, tvType, epi) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                    this.year = year // Guardamos el año
                }
            }
            TvType.Movie -> {
                newMovieLoadResponse(title, url, tvType, url) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backimage
                    this.plot = description
                    this.tags = tags
                    this.year = year // Guardamos el año
                }
            }
            else -> null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        var linksFound = false
        val linkRegex = Regex("window\\.location\\.href\\s*=\\s*'([^']*)'")

        val headers = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Referer" to data
        )

        val doc = app.get(data, headers = headers).document
        val servers = doc.select("div ul.subselect li")

        servers.forEach { serverLi ->
            // Declaramos la variable aquí afuera para que el catch pueda leerla
            val serverName = serverLi.text().trim()

            try {
                val serverData = serverLi.attr("data-server").takeIf { it.isNotEmpty() } ?: return@forEach

                val encodedTwo = base64Encode(serverData.toByteArray())
                val playerUrl = "$mainUrl/player/$encodedTwo"

                Log.d("PlushdProvider", "Intentando servidor: $serverName")

                val playerResponse = app.get(playerUrl, headers = headers)
                val text = playerResponse.text

                if (text.contains("bloqueo temporal")) {
                    Log.w("PlushdProvider", "Bloqueo detectado en $serverName")
                    return@forEach
                }

                val link = linkRegex.find(text)?.destructured?.component1()

                if (!link.isNullOrBlank()) {
                    val fixedLink = fixPelisplusHostsLinks(link)

                    val extractorReferer = when {
                        fixedLink.contains("hqq.tv") || fixedLink.contains("vidhide") -> playerUrl
                        fixedLink.contains("upns.pro") -> "https://pelisplus.upns.pro/"
                        else -> fixedLink
                    }

                    val loaded = loadExtractor(
                        url = fixedLink,
                        referer = extractorReferer,
                        subtitleCallback = { sub ->
                            Log.i("PlushdProvider", "Subtítulo detectado: ${sub.url}")
                            subtitleCallback.invoke(sub)
                        },
                        callback = { link ->
                            // Si entra aquí, es porque REALMENTE hay un video
                            linksFound = true
                            callback.invoke(link)
                        }
                    )

// También marcamos true si el extractor dice que tuvo éxito
                    if (loaded) linksFound = true
                }
            } catch (e: Exception) {
                // Ahora $serverName ya es accesible aquí
                Log.e("PlushdProvider", "Error cargando $serverName: ${e.message}")
            }
            delay(800L)
        }
        return linksFound
    }

}