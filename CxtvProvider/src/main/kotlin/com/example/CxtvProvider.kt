package com.example

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.CancellationException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class CxtvProvider : MainAPI() {
    override var mainUrl = "https://www.cxtvenvivo.com"
    override var name = "CXTv"
    override var lang = "mx"
    override val supportedTypes = setOf(TvType.Live)
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "getMainPage inicio page=$page request='${request.name}' data='${request.data}'")
        if (page > 1) {
            Log.d(TAG, "getMainPage omitido: solo hay pagina 1")
            return null
        }
        val sections = mutableListOf<HomePageList>()
        val doc = fetchDoc(mainUrl + "/tv") ?: run {
            Log.e(TAG, "getMainPage: no se pudo cargar /tv; no se crean secciones")
            return null
        }

        val destacadosCards = doc.select("div.listagem-cards a[href*=/tv-en-vivo/]")
        val destacados = parseChannelCards(destacadosCards)
        Log.d(TAG, "getMainPage destacados: tarjetas=${destacadosCards.size} canales=${destacados.size}")
        if (destacados.isNotEmpty()) {
            sections.add(HomePageList("Lo más destacado", destacados.map { channelItem(it) }))
        }

        val paisesDoc = fetchDoc(mainUrl + "/tv/paises")
        val countryAnchors = paisesDoc?.select("a[href*=/tv/paises/]")
        val paises = if (countryAnchors != null) parseSectionCards(countryAnchors) else emptyList()
        Log.d(TAG, "getMainPage paises: pagina=${paisesDoc != null} anchors=${countryAnchors?.size ?: 0} parseados=${paises.size}")
        val seccionesPais = paises.amap { pais ->
            val paisDoc = fetchDoc(pais.url)
            val countryCards = paisDoc?.select("a[href*=/tv-en-vivo/]")
            val canales = countryCards?.let { parseChannelCards(it) }.orEmpty()
            if (canales.isEmpty()) {
                val candidates = paisDoc?.select("a[href]")?.filter { it.attr("abs:href").contains("/tv-en-vivo/") }.orEmpty()
                Log.w(TAG, "getMainPage pais='${pais.name}': canales=0 candidates=${candidates.size} firstText='${candidates.firstOrNull()?.text()?.take(100)}'")
            }
            Log.d(TAG, "getMainPage pais='${pais.name}' url=${pais.url} pagina=${paisDoc != null} canales=${canales.size}")
            pais.name to canales
        }
        seccionesPais.filter { it.second.isNotEmpty() }.forEach { (pais, canales) ->
            sections.add(HomePageList(pais, canales.distinctBy { it.url }.map { channelItem(it) }))
        }

        val topDoc = fetchDoc(mainUrl + "/tv/mas-vistas")
        val topCards = topDoc?.select("div.listagem-cards a[href*=/tv-en-vivo/]")
        val top50 = topCards?.let { parseChannelCards(it) }.orEmpty()
        Log.d(TAG, "getMainPage top50: pagina=${topDoc != null} tarjetas=${topCards?.size ?: 0} canales=${top50.size}")
        if (top50.isNotEmpty()) {
            sections.add(HomePageList("Top 50", top50.map { channelItem(it) }))
        }

        if (sections.isEmpty()) {
            Log.e(TAG, "getMainPage FIN: sin secciones; destacados=${destacados.size}, paises=${paises.size}, top50=${top50.size}")
            return null
        }
        Log.d(TAG, "getMainPage FIN: ${sections.size} secciones -> ${sections.joinToString { "${it.name}=${it.list.size}" }}")
        return newHomePageResponse(sections, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()
        return try {
            val doc = fetchDoc("$mainUrl/busqueda/?query=${java.net.URLEncoder.encode(q, "UTF-8")}") ?: return emptyList()
            val cards = parseChannelCards(doc.select("a[href*=/tv-en-vivo/]"))
            val matched = cards.asSequence()
                .filter { fold(it.name).contains(fold(q)) }
                .distinctBy { it.url }
                .take(40)
                .toList()
            matched.map { channelItem(it) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "search '$q' fallo: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val u = url.trim()
        if (u.isBlank()) return null
        return try {
            if (u.contains("/tv-en-vivo/")) {

                val doc = fetchDoc(u) ?: return null
                val title = cleanChannelTitle(
                    doc.selectFirst("h1")?.text()
                        ?: doc.selectFirst("meta[property=og:title]")?.attr("content")
                        ?: u.substringAfterLast("/").replace("-", " ").capitalize()
                )
                val poster = doc.selectFirst("meta[property=og:image]")?.attr("content") ?: ""
                newTvSeriesLoadResponse(title, u, TvType.Live, listOf(newEpisode(u) {
                    this.name = title
                    this.posterUrl = posterFor(title, poster.takeIf { it.isNotBlank() })
                })) {
                    this.plot = title
                    this.posterUrl = posterFor(title, poster.takeIf { it.isNotBlank() })
                }
            } else {

                val doc = fetchDoc(u) ?: return null
                val cards = parseChannelCards(doc.select("a[href*=/tv-en-vivo/]"))
                val title = doc.selectFirst("h1")?.text()
                    ?: doc.title().replace("| CXTv", "").trim()
                    ?: "Sección"
                val episodes = cards.map { ch ->
                    newEpisode(ch.url) {
                        this.name = ch.name
                        this.posterUrl = posterFor(ch.name, ch.logo)
                    }
                }
                newTvSeriesLoadResponse(title, u, TvType.Live, episodes) {
                    this.plot = title
                    this.posterUrl = posterFor(title, null)
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "load '$u' fallo: ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val u = data.trim()
        if (u.isBlank() || !u.startsWith("http")) return false
        return try {
            val doc = fetchDoc(u) ?: return false
            val video = doc.selectFirst("video[data-stream-url], source[data-stream-url]")
            val streamUrl = video?.attr("data-stream-url") ?: null
            if (streamUrl.isNullOrBlank()) return false
            val ok = try {
                val txt = app.get(streamUrl, timeout = 8L).text
                txt.contains("#EXTM3U")
            } catch (e: Exception) {
                false
            }
            if (!ok) return false
            val title = cleanChannelTitle(doc.selectFirst("h1")?.text()
                ?: doc.selectFirst("meta[property=og:title]")?.attr("content")
                ?: name)
            callback(newExtractorLink(name, title, streamUrl, ExtractorLinkType.M3U8) {
                this.quality = Qualities.Unknown.value
            })
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "loadLinks '$u' fallo: ${e.message}")
            false
        }
    }

    private suspend fun fetchDoc(url: String): Document? {
        return try {
            val response = app.get(url, timeout = 15L)
            val txt = response.text
            Log.d(TAG, "fetchDoc HTTP ${response.code} len=${txt.length} url=$url")
            Jsoup.parse(txt, url)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "fetchDoc '$url' fallo: ${e.message}")
            null
        }
    }

    private fun parseChannelCards(elements: org.jsoup.select.Elements): List<ChannelInfo> {
        val out = ArrayList<ChannelInfo>()
        for (a in elements) {
            val href = a.attr("abs:href")
            if (!href.contains("/tv-en-vivo/")) continue
            val rawTitle = a.selectFirst("h4.resumo")?.text()?.takeIf { it.isNotBlank() }
                ?: a.selectFirst("h5")?.text()?.takeIf { it.isNotBlank() }
                ?: a.attr("data-item_name").takeIf { it.isNotBlank() }
                ?: a.text().trim()
            val title = cleanChannelTitle(rawTitle)
            val logo = a.selectFirst("img")?.attr("abs:src") ?: ""
            if (href.isNotBlank() && title.isNotBlank()) {
                out.add(ChannelInfo(title.trim(), href, logo.takeIf { it.isNotBlank() }, null))
            }
        }
        return out
    }

    private fun parseSectionCards(elements: org.jsoup.select.Elements): List<ChannelInfo> {
        val out = ArrayList<ChannelInfo>()
        val seen = mutableSetOf<String>()
        for (a in elements) {
            val href = a.attr("abs:href")
            if (!href.contains("/tv/") || href.contains("/tv-en-vivo/")) continue
            if (href in seen) continue
            seen.add(href)
            val title = (a.selectFirst("h4, h5")?.text()?.takeIf { it.isNotBlank() }
                ?: a.text().trim()).replace(Regex("\\s+\\d+$"), "").trim()
            val logo = a.selectFirst("img")?.attr("abs:src") ?: ""
            if (href.isNotBlank() && title.isNotBlank()) {
                out.add(ChannelInfo(title.trim(), href, logo.takeIf { it.isNotBlank() }, null))
            }
        }
        return out
    }

    private fun cleanChannelTitle(raw: String): String {
        var title = raw.trim()
            .substringBefore("|")
            .replace(Regex("(?i)\\s*[-–—]\\s*(?:ver\\s+online|ver\\s+en\\s+vivo|online\\s+gratis|gratis\\s+online).*$"), "")
            .replace(Regex("(?i)\\s*[-–—]\\s*(?:[a-záéíóúñ ]+\\s*,\\s*[a-záéíóúñ ]+)$"), "")
            .replace(Regex("(?i)\\s+(?:en\\s+vivo|online\\s+gratis|gratis\\s+online|online)$"), "")
            .trim()
        if (title.isBlank()) title = raw.substringBefore("|").trim()
        return title
    }

    private fun channelItem(ch: ChannelInfo): SearchResponse =
        newLiveSearchResponse(ch.name, ch.url, TvType.Live) {
            this.posterUrl = posterFor(ch.name, ch.logo)
        }

    private fun extractM3u8(doc: Document, baseUrl: String): String? {
        val video = doc.selectFirst("video[data-stream-url], source[data-stream-url]")
        return video?.attr("data-stream-url") ?: null
    }

    private fun posterFor(name: String, logo: String?): String {
        val placeholder = "https://placehold.co/320x320/1e1e2e/e0e0e0/png?text=" + enc(name)
        val l = logo?.trim()
        if (l.isNullOrBlank()) return placeholder
        return "https://wsrv.nl/?url=${enc(l)}&w=320&h=320&fit=contain&default=${enc(placeholder)}"
    }

    private fun enc(s: String): String =
        java.net.URLEncoder.encode(s, "UTF-8").replace("+", "%20")

    private fun fold(s: String): String =
        java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
            .lowercase()

    private data class ChannelInfo(
        val name: String,
        val url: String,
        val logo: String?,
        val group: String?,
    )

    companion object {
        const val TAG = "CxtvEnvivo"

        @JvmStatic
        var pluginContext: Context? = null
    }
}
