package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class GloboViewProvider : MainAPI() {
    override var mainUrl = "https://globoview.cam"
    override var name = "GloboView"
    override var lang = "mx"
    override val supportedTypes = setOf(TvType.Live)
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val categories = listOf(
        "Deportes" to "/categorias/Deportes/",
        "Noticias" to "/categorias/Noticias/",
        "Películas" to "/categorias/Películas/",
        "Series" to "/categorias/Series/",
        "Entretenimiento" to "/categorias/Entretenimiento/",
        "Música" to "/categorias/Música/",
        "Infantil" to "/categorias/Infantil/",
        "Documentales" to "/categorias/Documentales/",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d("GloboView", "getMainPage: page=$page, request=${request.name}")
        val home = mutableListOf<HomePageList>()
        for ((name, path) in categories) {
            try {
                val url = "$mainUrl$path"
                Log.d("GloboView", "getMainPage: fetching $url")
                val doc = app.get(url).document
                val channels = doc.select("a.card[href*=/directorio/]").mapNotNull { a ->
                    val link = a.attr("href")
                    val title = a.selectFirst("h3.card-title")?.text()?.trim() ?: return@mapNotNull null
                    val poster = a.selectFirst("img")?.attr("src")
                    newLiveSearchResponse(title, fixUrl(link), TvType.Live) {
                        this.posterUrl = if (poster?.startsWith("http") == true) poster else null
                    }
                }.distinctBy { it.url }
                Log.d("GloboView", "getMainPage: $name -> ${channels.size} canales")
                if (channels.isNotEmpty()) {
                    home.add(HomePageList(name, channels, isHorizontalImages = true))
                }
            } catch (e: Exception) {
                Log.e("GloboView", "getMainPage error for $name: ${e.message}")
            }
        }
        if (home.isEmpty()) {
            Log.e("GloboView", "getMainPage: 0 secciones, retornando null")
            return null
        }
        Log.d("GloboView", "getMainPage: ${home.size} secciones cargadas")
        return newHomePageResponse(home, hasNext = false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d("GloboView", "search: query=$query")
        val results = mutableListOf<SearchResponse>()
        val majorCountries = listOf(
            "/directorio/espana/",
            "/directorio/mexico/",
            "/directorio/argentina/",
            "/directorio/colombia/",
            "/directorio/estados-unidos/",
        )
        for (countryPath in majorCountries) {
            try {
                val url = "$mainUrl$countryPath"
                Log.d("GloboView", "search: scanning $url")
                val doc = app.get(url).document
                val channelCount = doc.select("a.card[href*=/directorio/]").size
                Log.d("GloboView", "search: $countryPath -> $channelCount canales en pagina")
                doc.select("a.card[href*=/directorio/]").forEach { a ->
                    val link = a.attr("href")
                    val title = a.selectFirst("h3.card-title")?.text()?.trim() ?: return@forEach
                    if (title.contains(query, ignoreCase = true)) {
                        val poster = a.selectFirst("img")?.attr("src")
                        Log.d("GloboView", "search: match found: $title")
                        results.add(newLiveSearchResponse(title, fixUrl(link), TvType.Live) {
                            this.posterUrl = if (poster?.startsWith("http") == true) poster else null
                        })
                    }
                }
            } catch (e: Exception) {
                Log.e("GloboView", "search error for $countryPath: ${e.message}")
            }
        }
        Log.d("GloboView", "search: ${results.size} resultados para query=$query")
        return results.distinctBy { it.url }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("GloboView", "load: url=$url")
        try {
            val resp = app.get(url)
            val html = resp.text
            Log.d("GloboView", "load: code=${resp.code}, html len=${html.length}")
            val doc = Jsoup.parse(html)

            val title = doc.selectFirst("h1")?.text()?.trim()
                ?: doc.selectFirst("title")?.text()?.trim()
                ?: "Canal"
            Log.d("GloboView", "load: title=$title")

            val poster = doc.selectFirst("meta[property='og:image']")?.attr("content")
                ?: doc.selectFirst(".card img")?.attr("src")
            Log.d("GloboView", "load: poster=$poster")

            val desc = doc.selectFirst("meta[property='og:description']")?.attr("content")
                ?: doc.selectFirst("meta[name=description]")?.attr("content")
                ?: ""
            Log.d("GloboView", "load: desc=${desc.take(100)}")

            val episodes = listOf(newEpisode(url) {
                this.name = "En Vivo"
                this.posterUrl = poster
            })
            return newTvSeriesLoadResponse(title, url, TvType.Live, episodes) {
                this.posterUrl = poster
                this.plot = desc
            }
        } catch (e: Exception) {
            Log.e("GloboView", "load error: ${e.message}")
            e.printStackTrace()
            return null
        }
    }

    override suspend fun loadLinks(
        data: String, isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("GloboView", "loadLinks: data=$data")
        try {
            val resp = app.get(data)
            val html = resp.text
            Log.d("GloboView", "loadLinks: code=${resp.code}, html len=${html.length}")

            val jsonLdPattern = Regex(""""contentUrl"\s*:\s*"([^"]+\.m3u8[^"]*)"""")
            val jsonLdMatch = jsonLdPattern.find(html)
            if (jsonLdMatch != null) {
                val m3u8Url = jsonLdMatch.groupValues[1].replace("\\/", "/")
                Log.d("GloboView", "loadLinks: found via JSON-LD: $m3u8Url")
                callback(newExtractorLink(name, "En Vivo", m3u8Url, ExtractorLinkType.M3U8) {
                    this.referer = data
                })
                return true
            }
            Log.d("GloboView", "loadLinks: JSON-LD pattern not found")

            val astroPattern = Regex(""""url"\s*:\s*\[0,\s*"([^"]+\.m3u8[^"]*)"""")
            val astroMatch = astroPattern.find(html)
            if (astroMatch != null) {
                val m3u8Url = astroMatch.groupValues[1].replace("\\/", "/")
                Log.d("GloboView", "loadLinks: found via astro-island: $m3u8Url")
                callback(newExtractorLink(name, "En Vivo", m3u8Url, ExtractorLinkType.M3U8) {
                    this.referer = data
                })
                return true
            }
            Log.d("GloboView", "loadLinks: astro-island pattern not found")

            val m3u8Pattern = Regex("""https?://[^"'\s<>]+\.m3u8[^"'\s<>]*""")
            val m3u8Match = m3u8Pattern.find(html)
            if (m3u8Match != null) {
                Log.d("GloboView", "loadLinks: found via generic regex: ${m3u8Match.value}")
                callback(newExtractorLink(name, "En Vivo", m3u8Match.value, ExtractorLinkType.M3U8) {
                    this.referer = data
                })
                return true
            }
            Log.d("GloboView", "loadLinks: no m3u8 found in page")

            return false
        } catch (e: Exception) {
            Log.e("GloboView", "loadLinks error: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

    private fun fixUrl(url: String): String {
        return if (url.startsWith("http")) url else "$mainUrl$url"
    }
}
