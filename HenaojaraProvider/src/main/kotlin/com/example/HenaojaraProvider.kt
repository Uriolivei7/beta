package com.example

import android.util.Log
import org.jsoup.nodes.Element
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class HenaojaraProvider : MainAPI() {
    override var mainUrl = "https://ww1.henaojara.net"
    override var name = "HenaoJara"
    override val hasMainPage = true
    override var lang = "mx"
    override val hasQuickSearch = false
    override val supportedTypes = setOf(TvType.Anime, TvType.OVA)

    override val mainPage = mainPageOf(
        mainUrl to "Episodios nuevos",
        "${mainUrl}/animes?tipo=anime" to "Animes",
        "${mainUrl}/animes?genero=accion&orden=desc" to "Acción",
        "${mainUrl}/animes?genero=ciencia-ficcion&orden=desc" to "Ciencia Ficción",
        "${mainUrl}/animes?genero=comedia&orden=desc" to "Comedia",
        "${mainUrl}/animes?genero=drama&orden=desc" to "Drama",
        "${mainUrl}/animes?genero=misterio&orden=desc" to "Misterio",
        "${mainUrl}/animes?genero=shounen&orden=desc" to "Shounen",
        "${mainUrl}/animes?tipo=pelicula" to "Peliculas"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val isHome = request.name == "Episodios nuevos"
        val url = if (isHome || page <= 1) request.data else "${request.data}${if (request.data.contains("?")) "&" else "?"}pag=$page"
        val items = app.get(url).document.select(if (isHome) "div.ul.hm article.li" else "div.ul article.li").mapNotNull { it.toEpisodePageResult() }
        return newHomePageResponse(request.name, items, !isHome && items.isNotEmpty())
    }

    override suspend fun search(query: String, page: Int): SearchResponseList {
        val url = "$mainUrl/animes?buscar=$query${if (page > 1) "&pag=$page" else ""}"
        val results = app.get(url).document.select("div.ul article.li").mapNotNull { it.toEpisodePageResult() }
        return newSearchResponseList(results, results.isNotEmpty())
    }

    private fun Element.toEpisodePageResult(): SearchResponse? {
        val title = selectFirst("h3.h a")?.text() ?: return null
        val ep = selectFirst("b.e")?.text() ?: ""
        val href = fixUrlNull(select(".mep li.ls a, li.ls a").attr("href").ifBlank { selectFirst("a")?.attr("href") }) ?: return null
        val poster = fixUrlNull(selectFirst("img")?.let { it.attr("data-src").ifBlank { it.attr("src") } })

        return newAnimeSearchResponse(if (ep.isNotEmpty()) "$title - $ep" else title, href, TvType.Anime) { posterUrl = poster }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse>? = search(query)

    override suspend fun load(url: String): LoadResponse? {
        Log.d("HenaoJara", "Cargando URL -> $url")

        var doc = app.get(url).document
        if (url.contains("/ver/")) {
            doc.selectFirst("li.ls a")?.attr("href")?.let {
                val animeUrl = fixUrl(it)
                Log.d("HenaoJara", "Redirigiendo a página principal -> $animeUrl")
                doc = app.get(animeUrl).document
            }
        }

        val title = doc.selectFirst("div.info-b h1")?.text() ?: return null

        val mainPoster = fixUrlNull(
            doc.selectFirst("div.info-a img")?.attr("data-src")
                ?: doc.selectFirst("div.info-a img")?.attr("src")
        )
        Log.d("HenaoJara", "Póster de serie detectado -> $mainPoster")

        val slug = doc.selectFirst("div.th")?.attr("data-sl") ?: doc.location().split("/").last()
        val scriptData = doc.select("script").map { it.data() }.firstOrNull { it.contains("var eps =") } ?: ""

        if (scriptData.isEmpty()) {
            Log.e("HenaoJara", "ERROR: No se encontró el script de episodios (var eps)")
        }

        val episodes = Regex("""\["(\d+)","(\d+)","(.*?)","(.*?)"\]""").findAll(scriptData).map { match ->
            val (num, _, code, epThumb) = match.destructured

            val finalPoster = if (epThumb.isNotBlank() && epThumb != "null") {
                fixUrlNull(epThumb)
            } else {
                mainPoster
            }

            Log.v("HenaoJara", "Episodio $num -> Poster: ${if (epThumb.isNotBlank()) "PROPIO" else "SERIE"}")

            newEpisode(if (code.isNotEmpty()) "$mainUrl/ver/$slug-$num-$code" else "$mainUrl/ver/$slug-$num") {
                this.name = "Episodio $num"
                this.episode = num.toIntOrNull()
                this.posterUrl = finalPoster
            }
        }.toList().sortedByDescending { it.episode }

        Log.i("HenaoJara", "Total episodios cargados: ${episodes.size}")

        return newTvSeriesLoadResponse(title, doc.location(), TvType.Anime, episodes) {
            this.posterUrl = mainPoster
            this.plot = doc.selectFirst("div.tx p")?.text()
            this.tags = doc.select("ul.gn li a").map { it.text() }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val document = app.get(data).document
        val mainEncrypt = document.selectFirst(".opt")?.attr("data-encrypt") ?: return false

        val response = app.post(
            "$mainUrl/hj",
            data = mapOf("acc" to "opt", "i" to mainEncrypt),
            headers = mapOf("Referer" to data, "X-Requested-With" to "XMLHttpRequest")
        ).text

        org.jsoup.Jsoup.parse(response).select("li[encrypt]").forEach { element ->
            try {
                val decodedUrl = element.attr("encrypt").chunked(2)
                    .map { it.toInt(16).toChar() }
                    .joinToString("")

                if (decodedUrl.startsWith("http")) {
                    loadExtractor(decodedUrl, data, subtitleCallback, callback)
                }
            } catch (_: Exception) {
            }
        }
        return true
    }
}