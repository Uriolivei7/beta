package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import android.util.Log // Importar la clase Log

class PelisplusicuProvider : MainAPI() {
    override var mainUrl = "https://v4.pelis-plus.icu"
    override var name = "PelisPlusIcu"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
    )

    data class ApiResponse(
        val videos: List<Video>?,
    )
    data class Video(
        val slug: String,
        val titulo: String,
        val modo: Int,
        val imagen: String,
        val release_date: String?,
    )

    data class Capitulo(
        val titulo: String? = null,
        val descripcion: String? = null,
        val temporada: Int? = null,
        val capitulo: Int? = null,
        val imagen: String? = null,
    )

    data class Link(
        val lang: String?,
        val url: String?
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(name, "getMainPage: Iniciando carga de página principal.")
        val items = ArrayList<HomePageList>()

        val urls = listOf(
            Pair("Series de Estreno", "/series"),
            Pair("Animes Actualizados", "/animes"),
            Pair("Películas de Estreno", "/peliculas"),
            Pair("Doramas de Estreno", "/doramas"),
            Pair("Directorio (General)", "/directorio"),
        )

        urls.amap { (name, path) ->
            try {
                val url = "$mainUrl$path"
                val doc = app.get(url, timeout = 15L).document

                val home = doc.select("a[href*='/pelicula/'], a[href*='/serie/'], a[href*='/anime/'], a[href*='/dorama/']").mapNotNull { element ->
                    val title = element.selectFirst("span.overflow-hidden")?.text()?.trim()
                    val link = element.attr("href")
                    val img = element.selectFirst("div.w-full img.w-full")?.attr("src")

                    val year = element.select("div:not([class]) span")
                        .firstOrNull { span -> span.text().trim().matches(Regex("""\d{4}""")) }
                        ?.text()?.toIntOrNull()

                    if (title.isNullOrBlank() || link.isNullOrBlank() || img.isNullOrBlank()) {
                        null
                    } else {
                        val type = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                        newTvSeriesSearchResponse(title, fixUrl(link), type){
                            this.posterUrl = fixUrl(img)
                            this.year = year
                        }
                    }
                }
                if(!home.isNullOrEmpty()){
                    items.add(HomePageList(name, home.distinctBy { it.url })) // Usamos distinctBy para evitar duplicados
                    Log.d(this.name, "getMainPage: Lista '$name' cargada con ${home.size} ítems.")
                } else {
                    Log.w(this.name, "getMainPage: Lista '$name' vacía o selectores fallaron para URL: $url")
                }
            } catch (e: Exception) {
                Log.e(this.name, "getMainPage: Error al procesar la lista '$name': ${e.message}", e)
            }
        }
        if (items.isEmpty()) {
            Log.e(this.name, "getMainPage: No se pudo cargar ninguna lista.")
        }

        return newHomePageResponse(items, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(name, "search: Buscando query '$query' mediante HTML de Directorio.")
        val searchUrl = "$mainUrl/directorio?search=$query"

        val headers = mapOf(
            "Referer" to "$mainUrl/",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"
        )

        try {
            val doc = app.get(searchUrl, headers = headers, timeout = 15L).document

            val results = doc.select("main div.grid a[href]").mapNotNull { element ->
                val title = element.selectFirst("span.overflow-hidden")?.text()?.trim()
                val link = element.attr("href")
                val img = element.selectFirst("div.w-full img.w-full")?.attr("src")

                val year = element.select("div:not([class]) span")
                    .firstOrNull { span -> span.text().trim().matches(Regex("""\d{4}""")) }
                    ?.text()?.toIntOrNull()

                if (title.isNullOrBlank() || link.isNullOrBlank() || img.isNullOrBlank()) {
                    null
                } else {
                    val type = if (link.contains("/pelicula/")) TvType.Movie else TvType.TvSeries
                    newTvSeriesSearchResponse(title, fixUrl(link), type){
                        this.posterUrl = fixUrl(img)
                        this.year = year
                    }
                }
            }

            Log.d(name, "search: Búsqueda HTML completada. ${results.size} resultados encontrados.")
            return results

        } catch (e: Exception) {
            Log.e(name, "search: Error crítico en la búsqueda HTML: ${e.message}", e)
            return emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(name, "load: Cargando URL de información: $url")
        try {
            val doc = app.get(url).document
            val tvType = if (url.contains("pelicula")) TvType.Movie else TvType.TvSeries
            Log.d(name, "load: Tipo detectado: $tvType")

            if(tvType == TvType.Movie){
                val title = doc.selectFirst("h1.text-3xl")?.text()?.trim() ?: doc.selectFirst("meta[property='og:title']")?.attr("content") ?: ""
                val yearMatch = Regex("""\((\d{4})\)""").find(title)
                val year = yearMatch?.groupValues?.get(1)?.toIntOrNull()
                Log.d(name, "load(Movie): Título='$title', Año=$year")

                val poster = doc.selectFirst("div.w-full.h-full.relative img")?.attr("src")
                val backimage = doc.selectFirst("div.w-full.h-full.absolute img")?.attr("src")
                val description = doc.selectFirst("span.w-full.text-textsec.text-sm")?.text() ?: doc.selectFirst("meta[name='description']")?.attr("content") ?: ""
                val tags = doc.select("div.flex-wrap.gap-3.mt-4 a").map{ it.text().trim()}

                return newMovieLoadResponse(title, url, tvType, url) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backimage ?: poster
                    this.plot = description
                    this.tags = tags
                    this.year = year
                }
            }

            else{
                val title = doc.selectFirst("h1.text-3xl")?.text()?.trim() ?: doc.selectFirst("meta[property='og:title']")?.attr("content") ?: ""
                val yearMatch = Regex("""\((\d{4})\)""").find(title)
                val year = yearMatch?.groupValues?.get(1)?.toIntOrNull()
                Log.d(name, "load(Series): Título='$title', Año=$year")

                val poster = doc.selectFirst("div.w-full.h-full.relative img")?.attr("src")
                val backimage = doc.selectFirst("div.w-full.h-full.absolute img")?.attr("src")
                val description = doc.selectFirst("span.w-full.text-textsec.text-sm")?.text() ?: doc.selectFirst("meta[name='description']")?.attr("content") ?: ""
                val tags = doc.select("div.flex-wrap.gap-3.mt-4 a").map{ it.text().trim()}

                val jsonScript = doc.select("script").firstOrNull {
                    it.html().contains("capitulos") && it.html().contains("__NEXT_DATA__")
                }?.html()

                val capitulosJson = jsonScript?.substringAfter("\"capitulos\":[")?.substringBefore("],\"")?.replace("\\\"", "\"")
                Log.d(name, "load(Series): JSON de capítulos extraído (Longitud: ${capitulosJson?.length})")

                val capitulos = AppUtils.tryParseJson<List<Capitulo>>("[$capitulosJson]")

                val episodes = if (!capitulos.isNullOrEmpty()) {
                    Log.d(name, "load(Series): ${capitulos.size} capítulos parseados. Usando amap.")

                    capitulos.amap { capitulo ->

                        val season = capitulo.temporada
                        val episode = capitulo.capitulo
                        val epTitle = capitulo.titulo
                        val epImage = capitulo.imagen

                        if (season == null || episode == null || epTitle == null) {
                            Log.w(name, "load(Series): Saltando episodio con Temporada, Capítulo o Título nulo.")
                            return@amap null
                        }

                        val episodeUrl = "$url/$season-$episode"

                        newEpisode(episodeUrl){
                            this.name = epTitle.replace(title.substringBefore(" ("), "")?.trim()
                            this.season = season
                            this.episode = episode
                            this.posterUrl = epImage?.let { "https://image.tmdb.org/t/p/w185/$it.jpg" } ?: poster
                        }
                    }.filterNotNull()

                } else {
                    Log.w(name, "load(Series): No se pudieron extraer episodios (JSON vacío o nulo).")
                    listOf()
                }

                return newTvSeriesLoadResponse(
                    title,
                    url, tvType, episodes,
                ) {
                    this.posterUrl = poster
                    this.backgroundPosterUrl = backimage ?: poster
                    this.plot = description
                    this.tags = tags
                    this.year = year
                }
            }
        } catch (e: Exception) {
            Log.e(name, "load: Error crítico al cargar la información: ${e.message}", e)
            return null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(name, "loadLinks: Iniciando extracción para URL: $data")
        try {
            val doc = app.get(data).document

            val text = doc.select("script").firstOrNull {
                it.html().contains("enlace") && it.html().contains("__NEXT_DATA__")
            }?.html()

            if (text.isNullOrEmpty()) {
                Log.e(name, "loadLinks: ERROR. No se encontró el script de datos principal (Next.js).")
                return false
            }

            val linksJson = text.substringAfter("\"links\":[")?.substringBefore("],\"")?.replace("\\\"", "\"")

            val finalLinkData = if (linksJson.isNullOrBlank()) {
                Log.w(name, "loadLinks: Links no encontrados con el método principal. Usando fallback.")
                doc.select("script").filter {
                    it.html().trim().startsWith("self.__next_f.push([1")
                }.joinToString("") {
                    it.html().replaceFirst("self.__next_f.push([1,\"", "")
                        .replace(""""]\)$""".toRegex(), "")
                }
            } else linksJson

            Log.d(name, "loadLinks: JSON de enlaces extraído (Longitud: ${finalLinkData.length})")

            val extractedLinks = fetchLinks(finalLinkData)

            if (extractedLinks.isEmpty()) {
                Log.e(name, "loadLinks: ERROR. fetchLinks devolvió 0 enlaces.")
                return false
            }

            Log.d(name, "loadLinks: ${extractedLinks.size} enlaces parseados. Iniciando extracción de host.")

            extractedLinks.amap { link ->
                val fixedUrl = fixHostsLinks(link.url!!)
                Log.d(name, "loadLinks: Procesando ${link.lang} | URL original: ${link.url} | URL corregida: $fixedUrl")
                loadSourceNameExtractor(link.lang!!, fixedUrl, data, subtitleCallback, callback)
            }
            return true
        } catch (e: Exception) {
            Log.e(name, "loadLinks: Error crítico durante la extracción de enlaces: ${e.message}", e)
            return false
        }
    }

    fun fetchLinks(text: String?): List<Link> {
        if (text.isNullOrEmpty()) {
            return listOf()
        }

        val linkRegex =
            Regex(""""enlace":"(https?://[^"]+)","tipo":\d+,"idioma":(\d+)""")

        return linkRegex.findAll(text).map { match ->
            Link(getLang(match.groupValues[2]), match.groupValues[1].replace("\\/", "/"))
        }.toList()
    }

    fun getLang(str: String): String {
        return when (str) {
            "1" -> "Latino"
            "2" -> "Español"
            "3" -> "Subtitulado"
            else -> "Otros"
        }
    }


    fun fixHostsLinks(url: String): String {
        return url
            .replaceFirst("https://hglink.to", "https://streamwish.to")
            .replaceFirst("https://swdyu.com", "https://streamwish.to")
            .replaceFirst("https://cybervynx.com", "https://streamwish.to")
            .replaceFirst("https://dumbalag.com", "https://streamwish.to")
            .replaceFirst("https://mivalyo.com", "https://vidhidepro.com")
            .replaceFirst("https://dinisglows.com", "https://vidhidepro.com")
            .replaceFirst("https://dhtpre.com", "https://vidhidepro.com")
            .replaceFirst("https://filemoon.link", "https://filemoon.sx")
            .replaceFirst("https://sblona.com", "https://watchsb.com")
            .replaceFirst("https://lulu.st", "https://lulustream.com")
            .replaceFirst("https://uqload.io", "https://uqload.com")
            .replaceFirst("https://do7go.com", "https://dood.la")
            .replaceFirst("https://v4.pelis-plus.icu/frame", "$mainUrl/frame")
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
                    source = "$source[${link.source}]",
                    name = "$source[${link.source}]",
                    url = link.url,
                    type = link.type,
                ) {
                    this.quality = link.quality
                    this.referer = link.referer
                    this.headers = link.headers
                    this.extractorData = link.extractorData
                }
            )
        }
    }
}