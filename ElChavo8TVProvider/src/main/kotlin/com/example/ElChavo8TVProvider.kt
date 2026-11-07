package com.example

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import okhttp3.FormBody
import org.jsoup.Jsoup
import java.net.URLEncoder

class ElChavo8TVProvider : MainAPI() {
    companion object {
        var pluginContext: Context? = null
        private const val TAG = "ElChavo8TV"
        private const val BYSE_API = "https://bysedikamoum.com/api/source"
    }

    override var mainUrl = "https://elchavodel8tv.com"
    override var name = "ElChavo8TV"
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Cartoon)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasDownloadSupport = true

    private val browserHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9",
        "Referer" to mainUrl,
    )

    override val mainPage = mainPageOf(
        "el-chavo-del-8-capitulos-completos/" to "El Chavo del 8",
        "el-chapulin-colorado-capitulos-completos/" to "El Chapulín Colorado",
        "chespirito-capitulos-completos/" to "Serie Chespirito",
        "el-chavo-animado-serie-completa/" to "El Chavo Animado",
        "el-chapulin-colorado-animado-serie-completa/" to "El Chapulín Animado",
        "los-programas-de-kiko-carlos-villagran/" to "Programas de Kiko",
        "el-chanfle-y-el-chanfle-2/" to "Películas",
    )

    // ------------------------------------------------------------------
    // getMainPage: filas por serie. Cada fila lleva a su página de temporada.
    // ------------------------------------------------------------------

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val t0 = System.currentTimeMillis()
        val path = request.data
        val doc = try {
            app.get("$mainUrl/$path", headers = browserHeaders, timeout = 60L).document
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.d(TAG, "getMainPage $path falló: ${e.message}")
            return null
        }

        val homeLists = mutableListOf<HomePageList>()

        val seasonLinks = doc.select("a[href*='temporada-'], a[href*=/temporada/]")
            .map { it.attr("abs:href") }
            .distinct()

        if (seasonLinks.isNotEmpty()) {
            for (seasonUrl in seasonLinks) {
                val seasonName = seasonUrl.substringAfterLast("/").replace("-", " ").capitalize()
                val list = mutableListOf<SearchResponse>()
                list.add(newTvSeriesSearchResponse(seasonName, seasonUrl) {
                    this.posterUrl = null
                })
                homeLists.add(HomePageList(seasonName, list))
            }
        } else {
            val pageTitle = doc.selectFirst("h1")?.text()?.trim() ?: path
            val list = mutableListOf<SearchResponse>()
            list.add(newTvSeriesSearchResponse(pageTitle, "$mainUrl/$path") {
                this.posterUrl = null
            })
            homeLists.add(HomePageList(pageTitle, list))
        }

        Log.d(TAG, "getMainPage $path -> ${homeLists.size} filas ${System.currentTimeMillis() - t0}ms")
        return if (homeLists.isNotEmpty()) newHomePageResponse(homeLists, false) else null
    }

    // ------------------------------------------------------------------
    // search: búsqueda por query en WordPress
    // ------------------------------------------------------------------

    override suspend fun search(query: String): List<SearchResponse>? {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "search() llamado: '$query'")
        return try {
            val encoded = URLEncoder.encode(query, "UTF-8")
            val doc = app.get("$mainUrl/?s=$encoded", headers = browserHeaders, timeout = 60L).document
            val results = doc.select("article.post, article.type-post, h2.entry-title a, h1.entry-title a")
                .mapNotNull { el ->
                    val a = if (el.tagName() == "a") el else el.selectFirst("a")
                    if (a == null) return@mapNotNull null
                    val href = a.attr("abs:href").takeIf { it.isNotBlank() } ?: return@mapNotNull null
                    val title = a.text().trim().takeIf { it.isNotEmpty() } ?: return@mapNotNull null
                    newTvSeriesSearchResponse(title, href) {
                        this.posterUrl = null
                    }
                }
                .distinctBy { it.url }
                .take(40)
            Log.d(TAG, "search '$query' -> ${results.size} resultados ${System.currentTimeMillis() - t0}ms")
            results
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "search falló: ${e.message}")
            null
        }
    }

    // ------------------------------------------------------------------
    // load: detalle de temporada (lista de episodios) o episodio individual
    // ------------------------------------------------------------------

    override suspend fun load(url: String): LoadResponse? {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "load($url)")

        val baseUrl = url.substringBefore("#")
        val epFragment = url.substringAfter("#").takeIf { it.startsWith("ep") }

        val doc = try {
            app.get(baseUrl, headers = browserHeaders, timeout = 120L).document
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "load página falló: ${e.message}")
            return null
        }

        val title = doc.selectFirst("h1")?.text()?.trim()
            ?: doc.selectFirst("title")?.text()?.substringBefore("|")?.trim()
            ?: "Desconocido"

        val description = doc.selectFirst("meta[name=description]")?.attr("content")
            ?: doc.selectFirst("p")?.text()?.trim()
            ?: ""

        val posterUrl = doc.selectFirst("meta[property='og:image']")?.attr("content")
            ?: null

        if (epFragment != null) {
            val epNum = epFragment.removePrefix("ep").toIntOrNull() ?: 1
            Log.d(TAG, "load -> Episodio individual $epNum ${System.currentTimeMillis() - t0}ms")
            return newMovieLoadResponse("Episodio $epNum", baseUrl, TvType.TvSeries, baseUrl) {
                this.posterUrl = posterUrl
                this.plot = description
            }
        }

        val episodeLinks = doc.select("a[href^='javascript:Servidor']")
        Log.d(TAG, "load episodios encontrados: ${episodeLinks.size}")

        val episodes = episodeLinks.mapNotNull { el ->
            val href = el.attr("href")
            val idMatch = Regex("""Servidor\d+(?:ChespTV|Kiko)\((\d+)\)""").find(href)?.groupValues?.get(1)
            if (idMatch == null) return@mapNotNull null
            val episodeId = idMatch.toIntOrNull() ?: return@mapNotNull null
            val title = el.parent()?.ownText()?.trim()
                ?: el.text()?.trim()
                ?: "Episodio $episodeId"
            val cleanTitle = title.replace(Regex("""^\d+\.\s*"""), "").trim()
            val episodeUrl = "$baseUrl#ep$episodeId"
            newEpisode(episodeUrl) {
                this.name = cleanTitle
                this.season = 1
                this.episode = episodeId
                this.posterUrl = posterUrl
                this.description = description.ifBlank { null }
            }
        }.distinctBy { it.episode }

        val sortedEpisodes: List<Episode> = episodes.sortedWith(compareBy({ it.episode ?: 0 }))

        Log.d(TAG, "load $title temporadas=1 episodios=${sortedEpisodes.size} ${System.currentTimeMillis() - t0}ms")

        return newTvSeriesLoadResponse(title, baseUrl, TvType.TvSeries, sortedEpisodes) {
            this.posterUrl = posterUrl
            this.plot = description
        }
    }

    // ------------------------------------------------------------------
    // loadLinks: extraer video de Servidor1ChespTV / Servidor2ChespTV via Byse
    // ------------------------------------------------------------------

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "loadLinks data='$data'")

        val baseUrl = data.substringBefore("#")

        val html = try {
            app.get(baseUrl, headers = browserHeaders, timeout = 120L).text
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "loadLinks página no disponible: ${e.message}")
            return false
        }

        val doc = Jsoup.parse(html, baseUrl)

        val serverLinks = doc.select("a[href^='javascript:Servidor']")
        Log.d(TAG, "loadLinks servidores encontrados: ${serverLinks.size}")

        var found = false

        for (el in serverLinks) {
            val href = el.attr("href")
            val serverMatch = Regex("""Servidor(\d+)(?:ChespTV|Kiko)\((\d+)\)""").find(href)
            if (serverMatch == null) continue

            val serverNum = serverMatch.groupValues[1]
            val serverName = when (serverNum) {
                "1" -> "Servidor 1"
                "2" -> "Servidor 2"
                else -> "Servidor $serverNum"
            }
            val epId = serverMatch.groupValues[2].toIntOrNull() ?: continue

            val label = "$serverName (Capítulo $epId)"

            val byseUrl = extractByseUrlFromHtml(html, serverNum, epId)
            if (byseUrl == null) {
                Log.w(TAG, "loadLinks sin URL de video para $label (id=$epId)")
                continue
            }

            Log.d(TAG, "loadLinks Byse URL: $byseUrl")

            val collected = mutableListOf<ExtractorLink>()
            val collector: (ExtractorLink) -> Unit = { link -> collected.add(link) }
            try {
                withTimeout(25_000L) {
                    fetchByseLinks(byseUrl, baseUrl, subtitleCallback, collector)
                }
            } catch (e: TimeoutCancellationException) {
                Log.w(TAG, "Byse timeout 25s para $label")
                continue
            }

            if (collected.isEmpty()) {
                Log.w(TAG, "Byse no emitió links para $label")
                continue
            }

            for (link in collected) {
                callback(newExtractorLink(name, "$label Byse", link.url) {
                    this.referer = link.referer ?: baseUrl
                    this.quality = link.quality
                    this.type = if (link.isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    this.headers = link.headers
                })
            }
            found = true
        }

        Log.d(TAG, "loadLinks $baseUrl -> ${if (found) "OK" else "SIN LINKS"} ${System.currentTimeMillis() - t0}ms")
        return found
    }

    /** Extrae urls[epId] del array JS inline de ServidorXChespTV */
    private fun extractByseUrlFromHtml(html: String, serverNum: String, epId: Int): String? {
        val funcName = "Servidor$serverNum"
        val scriptPattern = Regex("function $funcName(?:ChespTV|Kiko)\\(id\\) \\{ [\\s\\S]*?var urls = \\[([\\s\\S]*?)\\];")
        val scriptMatch = scriptPattern.find(html)
        if (scriptMatch == null) {
            Log.d(TAG, "extractByseUrl: no se encontró array urls para $funcName")
            return null
        }

        val arrayContent = scriptMatch.groupValues[1]

        val entries = arrayContent.split(Regex("\",\"")).map { it.trim().trim('"') }
        if (epId < 0 || epId >= entries.size) {
            Log.d(TAG, "extractByseUrl: epId $epId fuera de rango (entries=${entries.size})")
            return null
        }
        val entry = entries[epId]
        if (entry.isBlank()) return null

        val urlCandidate = entry.split("&tipo=")[0].trim()

        Log.d(TAG, "extractByseUrl: epId=$epId -> $urlCandidate")
        return urlCandidate
    }

    /** Lógica inline de ByseExtractor: POST a bysedikamoum.com/api/source */
    private suspend fun fetchByseLinks(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.d(TAG, "[Byse] URL: $url")

        val videoId = Regex("""/e/(\w+)""").find(url)?.groupValues?.get(1) ?: return

        try {
            val apiRes = app.post(
                BYSE_API,
                headers = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                    "Referer" to url,
                    "Origin" to "https://bysedikamoum.com",
                ),
                referer = url,
                requestBody = FormBody.Builder()
                    .add("r", "")
                    .add("s", videoId)
                    .build()
            )
            Log.d(TAG, "[Byse] API response: ${apiRes.text}")

            val fileUrl = Regex("""file["']\s*:\s*["']([^"']+)["']""")
                .find(apiRes.text)?.groupValues?.get(1)
                ?: Regex("""url["']\s*:\s*["']([^"']+)["']""")
                .find(apiRes.text)?.groupValues?.get(1)
                ?: return

            callback.invoke(
                newExtractorLink("Byse", "Byse", fileUrl) {
                    this.referer = "https://bysedikamoum.com"
                    this.quality = Qualities.Unknown.value
                    this.type = if (fileUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "[Byse] Error: ${e.message}")
        }
    }
}
