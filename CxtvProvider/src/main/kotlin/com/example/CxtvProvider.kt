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
    override var name = "CxtvEnvivo"
    override var lang = "es"
    override val supportedTypes = setOf(TvType.Live)
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        if (page > 1) return null
        val sections = mutableListOf<HomePageList>()
        val doc = fetchDoc(mainUrl + "/tv") ?: return null

        // Fila 1: Lo más destacado (canales directos)
        val destacados = parseChannelCards(doc.select("div.listagem-cards"))
        if (destacados.isNotEmpty()) {
            sections.add(HomePageList("Lo más destacado", destacados.map { channelItem(it) }))
        }

        // Fila 2: Categorías
        val categorias = parseSectionCards(doc.select("div.col-md-8 a[href*=/tv/categorias/]"))
        if (categorias.isNotEmpty()) {
            sections.add(HomePageList("Categorías", categorias.map { sectionItem(it) }))
        }

        // Fila 3: Países
        val paisesDoc = fetchDoc(mainUrl + "/tv/paises")
        val paises = if (paisesDoc != null) parseSectionCards(paisesDoc.select("a[href*=/tv/paises/]")) else emptyList()
        if (paises.isNotEmpty()) {
            sections.add(HomePageList("Países", paises.map { sectionItem(it) }))
        }

        // Fila 4: Top 50
        val topDoc = fetchDoc(mainUrl + "/tv/mas-vistas")
        val top50 = if (topDoc != null) parseChannelCards(topDoc.select("div.listagem-cards")) else emptyList()
        if (top50.isNotEmpty()) {
            sections.add(HomePageList("Top 50", top50.map { channelItem(it) }))
        }

        if (sections.isEmpty()) return null
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
                // Canal individual
                val doc = fetchDoc(u) ?: return null
                val title = doc.selectFirst("meta[property=og:title]")?.attr("content")
                    ?: doc.selectFirst("h1")?.text()
                    ?: u.substringAfterLast("/").replace("-", " ").capitalize()
                val poster = doc.selectFirst("meta[property=og:image]")?.attr("content") ?: ""
                newTvSeriesLoadResponse(title, u, TvType.Live, listOf(newEpisode(u) {
                    this.name = title
                    this.posterUrl = posterFor(title, poster.takeIf { it.isNotBlank() })
                })) {
                    this.plot = title
                    this.posterUrl = posterFor(title, poster.takeIf { it.isNotBlank() })
                }
            } else {
                // Sección (país, categoría, top, idioma): lista de canales
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
            val title = doc.selectFirst("meta[property=og:title]")?.attr("content") ?: name
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
            val txt = app.get(url, timeout = 15L).text
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
            val title = a.selectFirst("h4.resumo")?.text()
                ?: a.selectFirst("h5")?.text()
                ?: a.attr("data-item_name")
                ?: a.text().trim()
                .takeIf { it.isNotBlank() }
                ?: ""
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
            val title = a.selectFirst("h4, h5")?.text()
                ?: a.text().trim()
                .takeIf { it.isNotBlank() }
                ?: ""
            val logo = a.selectFirst("img")?.attr("abs:src") ?: ""
            if (href.isNotBlank() && title.isNotBlank()) {
                out.add(ChannelInfo(title.trim(), href, logo.takeIf { it.isNotBlank() }, null))
            }
        }
        return out
    }

    private fun channelItem(ch: ChannelInfo): SearchResponse =
        newLiveSearchResponse(ch.name, ch.url, TvType.Live) {
            this.posterUrl = posterFor(ch.name, ch.logo)
        }

    private fun sectionItem(ch: ChannelInfo): SearchResponse =
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
