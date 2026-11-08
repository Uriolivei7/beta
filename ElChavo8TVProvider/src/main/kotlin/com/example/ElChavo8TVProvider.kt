package com.example

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.net.URLEncoder

class ElChavo8TVProvider : MainAPI() {
    companion object {
        var pluginContext: Context? = null
        private const val TAG = "ElChavo8TV"
        private const val BYSE_API = "https://bysedikamoum.com/api/source"
        private const val MEDIA_REFERER = "https://pokemonlaserielatino.xyz/"
        private const val UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    }

    override var mainUrl = "https://elchavodel8tv.com"
    override var name = "ElChavo8TV"
    override val supportedTypes = setOf(TvType.TvSeries, TvType.Cartoon)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasDownloadSupport = true

    private val browserHeaders = mapOf(
        "User-Agent" to UA,
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
        "Accept-Language" to "es-ES,es;q=0.9",
        "Referer" to mainUrl,
    )

    private val mediaHeaders = mapOf(
        "User-Agent" to UA,
        "Referer" to MEDIA_REFERER,
        "Origin" to "https://pokemonlaserielatino.xyz",
        "Accept" to "*/*",
    )

    // Bases de media por "tipo" (igual que el JS de la web: rutasSeries)
    private val mediaBases = mapOf(
        "chavo" to "https://media.pokemonlaserielatino.xyz/Series/El Chavo del 8 y Chapulin Colorado/El chavo del 8/",
        "animado" to "https://media.pokemonlaserielatino.xyz/Series/El Chavo del 8 y Chapulin colorado animado/El Chavo animado/",
        "chapulinreal" to "https://media.pokemonlaserielatino.xyz/Series/El Chavo del 8 y Chapulin Colorado/El Chapulin Colorado/",
        "chapulinanimado" to "https://media.pokemonlaserielatino.xyz/Series/El Chavo del 8 y Chapulin colorado animado/El Chapulin colorado animado/",
        "chespirito" to "https://media.pokemonlaserielatino.xyz/Series/Chespirito/",
        "sinquererqueriendo" to "https://media.pokemonlaserielatino.xyz/Series/Sin querer queriendo/",
        "Pelis" to "https://media.pokemonlaserielatino.xyz/Series/Pelis chesp/",
        "kiko" to "https://media.pokemonlaserielatino.xyz/Series/Programas de Kiko/",
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

    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor {
        return Interceptor { chain ->
            val req = chain.request()
            val host = req.url.host
            if (host.contains("pokemonlaserielatino")) {
                val b = req.newBuilder()
                for ((k, v) in mediaHeaders) b.header(k, v)
                chain.proceed(b.build())
            } else {
                chain.proceed(req)
            }
        }
    }

    // ------------------------------------------------------------------
    // getMainPage: UNA fila por serie (nombre de la serie) con sus temporadas como items
    // ------------------------------------------------------------------

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val t0 = System.currentTimeMillis()
        val path = request.data
        val doc = try {
            app.get("$mainUrl/$path", headers = browserHeaders, timeout = 60L).document
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "getMainPage $path falló: ${e.message}")
            return null
        }

        val items = mutableListOf<SearchResponse>()
        val seasonLinks = doc.select("a[href*='temporada-'], a[href*=/temporada/]")
            .map { it.attr("abs:href") }
            .filter { it.isNotBlank() }
            .distinct()

        if (seasonLinks.isNotEmpty()) {
            for (u in seasonLinks) {
                val seasonName = cleanSeasonName(u)
                items.add(newTvSeriesSearchResponse(seasonName, u) {
                    this.posterUrl = null
                })
            }
        } else {
            val pageTitle = doc.selectFirst("h1")?.text()?.trim() ?: request.name
            items.add(newTvSeriesSearchResponse(pageTitle, "$mainUrl/$path") {
                this.posterUrl = null
            })
        }

        Log.d(TAG, "getMainPage '${request.name}' $path -> ${items.size} temporadas ${System.currentTimeMillis() - t0}ms")
        return if (items.isNotEmpty()) newHomePageResponse(request.name, items, false) else null
    }

    private fun cleanSeasonName(url: String): String {
        val slug = url.substringAfterLast("/").substringBefore("?").removeSuffix(".html")
        val lower = slug.lowercase()
        val idx = lower.indexOf("temporada")
        if (idx >= 0) {
            val tail = lower.substring(idx + "temporada".length).trim('-', '_')
            val nums = Regex("\\d+").findAll(tail).map { it.value }.toList()
            if (nums.isNotEmpty()) {
                return if (nums.size >= 2) {
                    if (nums[0].length == 4) "Temporada ${nums[0]}-${nums[1]}"
                    else "Temporada ${nums[0]} (${nums[1]})"
                } else {
                    "Temporada ${nums[0]}"
                }
            }
        }
        val readable = slug.replace("-", " ").replace("_", " ").trim()
        return readable.ifBlank { url }
    }

    // ------------------------------------------------------------------
    // search
    // ------------------------------------------------------------------

    override suspend fun search(query: String): List<SearchResponse>? {
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
            Log.d(TAG, "search '$query' -> ${results.size} resultados")
            results
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "search falló: ${e.message}")
            null
        }
    }

    // ------------------------------------------------------------------
    // load: temporada -> episodios (dedupe por id global, renumerados 1..N)
    // ------------------------------------------------------------------

    override suspend fun load(url: String): LoadResponse? {
        val t0 = System.currentTimeMillis()
        Log.d(TAG, "load($url)")

        val baseUrl = url.substringBefore("#")
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

        // id global -> título (primera aparición, que corresponde al Servidor 1)
        val epMap = LinkedHashMap<Int, String>()
        for (el in doc.select("a[href^='javascript:Servidor']")) {
            val m = Regex("""Servidor\w+\((\d+)\)""").find(el.attr("href")) ?: continue
            val id = m.groupValues[1].toIntOrNull() ?: continue
            if (!epMap.containsKey(id)) {
                epMap[id] = precedingTitle(el) ?: "Episodio $id"
            }
        }

        if (epMap.isEmpty()) {
            // Página sin episodios (p. ej. película): un solo item
            Log.d(TAG, "load '$title' sin episodios (película) ${System.currentTimeMillis() - t0}ms")
            return newMovieLoadResponse(title, baseUrl, TvType.Movie, baseUrl) {
                this.posterUrl = posterUrl
                this.plot = description.ifBlank { null }
            }
        }

        val episodes = mutableListOf<Episode>()
        var n = 0
        for ((id, epTitle) in epMap.entries.sortedBy { it.key }) {
            n++
            val epUrl = "$baseUrl#ep$id"
            episodes.add(newEpisode(epUrl) {
                this.name = cleanEpTitle(epTitle)
                this.season = 1
                this.episode = n
                this.posterUrl = posterUrl
            })
        }

        Log.d(TAG, "load '$title' episodios=${episodes.size} ${System.currentTimeMillis() - t0}ms")
        return newTvSeriesLoadResponse(title, baseUrl, TvType.TvSeries, episodes) {
            this.posterUrl = posterUrl
            this.plot = description.ifBlank { null }
        }
    }

    /** Texto inmediatamente anterior al enlace (saltando <br> y separadores). */
    private fun precedingTitle(el: Element): String? {
        var node = el.previousSibling()
        var hops = 0
        while (node != null && hops < 8) {
            if (node is TextNode) {
                val t = node.text().trim()
                if (t.isNotEmpty() && t != "|" && t != "Opción 1" && t != "Opción 2") return t
            }
            node = node.previousSibling()
            hops++
        }
        return null
    }

    private fun cleanEpTitle(s: String): String =
        s.replace(Regex("^\\s*Cap[ií]tulo\\s*\\d+\\.\\s*", RegexOption.IGNORE_CASE), "")
            .trim().ifBlank { s }

    // ------------------------------------------------------------------
    // loadLinks: resuelve SOLO el episodio pulsado (#ep<id>)
    // Servidor 1 = Byse (pokemonlaserielatino.com/e/...)
    // Servidor 2 = MP4 directo (media.pokemonlaserielatino.xyz + &tipo=)
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
        val wantedId = data.substringAfter("#ep", "").toIntOrNull()
        Log.d(TAG, "loadLinks wantedId=$wantedId")

        val html = try {
            app.get(baseUrl, headers = browserHeaders, timeout = 120L).text
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.e(TAG, "loadLinks página no disponible: ${e.message}")
            return false
        }
        val doc = Jsoup.parse(html, baseUrl)

        // Recolecta los enlaces del episodio pulsado
        val servers = mutableListOf<Pair<String, Int>>()
        for (el in doc.select("a[href^='javascript:Servidor']")) {
            val m = Regex("""(Servidor\w+)\((\d+)\)""").find(el.attr("href")) ?: continue
            val id = m.groupValues[2].toIntOrNull() ?: continue
            if (wantedId != null && id != wantedId) continue
            servers.add(m.groupValues[1] to id)
        }
        Log.d(TAG, "loadLinks servidores del episodio: ${servers.size}")

        var found = false

        // PASO 1: MP4 directos (entradas con &tipo=)
        for ((func, id) in servers) {
            val entry = extractArrayEntry(html, func, id) ?: continue
            if (!entry.contains("&tipo=")) continue
            val filePart = entry.substringBefore("&tipo=")
            val tipo = entry.substringAfter("&tipo=").substringBefore("&")
            val base = mediaBases[tipo] ?: continue
            val mediaUrl = encodePath(base + filePart)
            Log.d(TAG, "MP4 $func -> $mediaUrl")
            callback(newExtractorLink(name, "$func MP4", mediaUrl, ExtractorLinkType.VIDEO) {
                this.referer = MEDIA_REFERER
                this.headers = mediaHeaders
                this.quality = Qualities.Unknown.value
            })
            found = true
        }

        // PASO 2: Byse (entradas http://.../e/...)
        for ((func, id) in servers) {
            val entry = extractArrayEntry(html, func, id) ?: continue
            if (!entry.startsWith("http")) continue
            val collected = mutableListOf<ExtractorLink>()
            val collector: (ExtractorLink) -> Unit = { collected.add(it) }
            try {
                withTimeout(15_000L) { fetchByseLinks(entry, baseUrl, subtitleCallback, collector) }
            } catch (e: TimeoutCancellationException) {
                Log.w(TAG, "Byse timeout para $func($id)")
                continue
            }
            for (link in collected) {
                callback(newExtractorLink(name, "$func Byse", link.url) {
                    this.referer = link.referer ?: baseUrl
                    this.quality = link.quality
                    this.type = if (link.isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                    this.headers = link.headers
                })
            }
            if (collected.isNotEmpty()) found = true
        }

        Log.d(TAG, "loadLinks $baseUrl -> ${if (found) "OK" else "SIN LINKS"} ${System.currentTimeMillis() - t0}ms")
        return found
    }

    /** Extrae array[index] del script `function <func>(id) { var urls = [...]; }`. */
    private fun extractArrayEntry(html: String, funcName: String, index: Int): String? {
        val pat = Regex("function\\s+" + Regex.escape(funcName) + "\\s*\\(id\\)\\s*\\{[\\s\\S]*?var\\s+urls\\s*=\\s*\\[([\\s\\S]*?)\\];")
        val match = pat.findAll(html).lastOrNull() ?: run {
            Log.d(TAG, "extractArrayEntry: no array para $funcName")
            return null
        }
        val items = Regex("\"(.*?)\"").findAll(match.groupValues[1]).map { it.groupValues[1] }.toList()
        if (index < 0 || index >= items.size) {
            Log.d(TAG, "extractArrayEntry: $funcName index=$index fuera de rango (${items.size})")
            return null
        }
        return items[index]
    }

    private fun encodePath(url: String): String {
        val sb = StringBuilder()
        for (ch in url) {
            if (ch.isLetterOrDigit() || ch in "-._~/:") {
                sb.append(ch)
            } else {
                for (b in ch.toString().toByteArray(Charsets.UTF_8)) {
                    sb.append('%').append(String.format("%02X", b.toInt() and 0xFF))
                }
            }
        }
        return sb.toString()
    }

    /** Lógica inline de Byse: POST a bysedikamoum.com/api/source */
    private suspend fun fetchByseLinks(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
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
                requestBody = FormBody.Builder().add("r", "").add("s", videoId).build()
            )
            val text = apiRes.text
            val fileUrl = Regex("""file["']\s*:\s*["']([^"']+)["']""").find(text)?.groupValues?.get(1)
                ?: Regex("""url["']\s*:\s*["']([^"']+)["']""").find(text)?.groupValues?.get(1)
                ?: return
            callback.invoke(newExtractorLink("Byse", "Byse", fileUrl) {
                this.referer = "https://bysedikamoum.com"
                this.quality = Qualities.Unknown.value
                this.type = if (fileUrl.contains(".m3u8")) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
            })
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Log.w(TAG, "[Byse] error: ${e.message}")
        }
    }
}
