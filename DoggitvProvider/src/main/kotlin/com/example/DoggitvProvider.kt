package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONArray
import kotlinx.coroutines.CancellationException

class DoggitvProvider : MainAPI() {
    override var mainUrl = "https://sswwswsw.github.io"
    override var name = "DoggiTV"
    override var lang = "mx"
    override val supportedTypes = setOf(TvType.Live)
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = false

    private data class Country(val name: String, val listUrl: String)

    private data class ChannelInfo(
        val name: String,
        val url: String,
        val logo: String?,
        val group: String?,
    )

    private val countriesCache = mutableMapOf<String, String>()

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        if (page > 1) return null
        val countries = fetchCountries()
        if (countries.isEmpty()) {
            Log.e(TAG, "getMainPage: sin paises")
            return null
        }
        val items = countries.map { c ->
            countriesCache[c.listUrl] = c.name
            newLiveSearchResponse(c.name, c.listUrl, TvType.Live)
        }
        Log.d(TAG, "getMainPage: ${items.size} paises")
        return newHomePageResponse(
            listOf(HomePageList("Paises", items, isHorizontalImages = true)),
            hasNext = false,
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val q = query.trim()
        if (q.isBlank()) return emptyList()
        val qn = fold(q)
        return try {
            val countries = fetchCountries()
            val lists = countries.amap { c -> fetchChannels(c.listUrl) }
            val results = lists.flatten()
                .asSequence()
                .filter { fold(it.name).contains(qn) }
                .distinctBy { it.url }
                .take(80)
                .map { ch ->
                    newLiveSearchResponse(ch.name, ch.url, TvType.Live) {
                        if (!ch.logo.isNullOrBlank()) this.posterUrl = ch.logo
                    }
                }
                .toList()
            Log.d(TAG, "search '$q' -> ${results.size}")
            results
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
            if (isPlaylist(u)) {
                val channels = fetchChannels(u)
                if (channels.isEmpty()) return null
                val title = countriesCache[u] ?: "Canales"
                val episodes = channels.map { ch ->
                    newEpisode(ch.url) {
                        this.name = ch.name
                        if (!ch.logo.isNullOrBlank()) this.posterUrl = ch.logo
                        this.description = ch.group
                    }
                }
                Log.d(TAG, "load pais '$title': ${episodes.size} canales")
                newTvSeriesLoadResponse(title, u, TvType.Live, episodes) {
                    this.plot = "$title - ${channels.size} canales (iptv-org)"
                }
            } else {
                newTvSeriesLoadResponse("En Vivo", u, TvType.Live, listOf(newEpisode(u) { this.name = "En Vivo" }))
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
        if (u.isBlank() || !u.startsWith("http")) {
            Log.e(TAG, "loadLinks: url invalida ($u)")
            return false
        }
        val path = u.substringBefore('?').lowercase()
        val type = when {
            path.endsWith(".mpd") -> ExtractorLinkType.DASH
            path.endsWith(".mp4") || path.endsWith(".mkv") || path.endsWith(".webm") -> ExtractorLinkType.VIDEO
            else -> ExtractorLinkType.M3U8
        }
        Log.d(TAG, "loadLinks -> $type $u")
        callback(newExtractorLink(name, name, u, type) {
            this.quality = Qualities.Unknown.value
        })
        return true
    }

    private suspend fun fetchCountries(): List<Country> {
        return try {
            val txt = app.get(CANALES_URL, timeout = 30L).text
            val arr = JSONArray(txt)
            val out = ArrayList<Country>()
            for (i in 0 until arr.length()) {
                val o = arr.optJSONObject(i) ?: continue
                val name = o.optString("Name").ifBlank { "Pais ${i + 1}" }
                val listUrl = o.optString("listURL")
                if (listUrl.isNotBlank()) out.add(Country(name, listUrl))
            }
            out
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "fetchCountries fallo: ${e.message}")
            emptyList()
        }
    }

    private suspend fun fetchChannels(listUrl: String): List<ChannelInfo> {
        return try {
            val txt = app.get(listUrl, timeout = 40L).text
            parseM3U(txt)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "fetchChannels '$listUrl' fallo: ${e.message}")
            emptyList()
        }
    }

    private fun parseM3U(content: String): List<ChannelInfo> {
        val out = ArrayList<ChannelInfo>()
        val rxGroup = Regex("""group-title="([^"]+)"""")
        val rxId = Regex("""tvg-id="([^"]+)"""")
        val rxLogo = Regex("""tvg-logo="([^"]+)"""")
        var group: String? = null
        var id: String? = null
        var logo: String? = null
        var nm: String? = null
        for (raw in content.lineSequence()) {
            val line = raw.trim()
            if (line.startsWith("#EXTINF")) {
                group = rxGroup.find(line)?.groupValues?.get(1)
                id = rxId.find(line)?.groupValues?.get(1)
                logo = rxLogo.find(line)?.groupValues?.get(1)
                nm = line.substringAfterLast("\",", "").ifBlank { line.substringAfter(",", "").trim() }
            } else if (line.startsWith("http")) {
                val channelName = nm?.takeIf { it.isNotBlank() } ?: id?.takeIf { it.isNotBlank() } ?: "Canal"
                out.add(ChannelInfo(channelName, line, logo, group))
                group = null; id = null; logo = null; nm = null
            }
        }
        return out
    }

    private fun isPlaylist(u: String): Boolean =
        u.substringBefore('?').lowercase().endsWith(".m3u")

    private fun fold(s: String): String =
        java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
            .lowercase()

    companion object {
        const val TAG = "DoggiTV"
        private const val CANALES_URL = "https://sswwswsw.github.io/canales.json"
    }
}
