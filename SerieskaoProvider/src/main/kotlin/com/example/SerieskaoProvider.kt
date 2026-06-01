package com.example

import android.util.Base64
import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import kotlinx.coroutines.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SerieskaoProvider : MainAPI() {
    override var mainUrl = "https://serieskao.top"
    override var name = "SeriesKao"
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Cartoon,
    )

    override var lang = "es"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val TAG = "SeriesKao"
    private val cryptoKey = "Ak7qrvvH4WKYxV2OgaeHAEg2a5eh16vE"

    companion object {
        private val fixHosts = mapOf(
            "https://hglink.to" to "https://streamwish.to",
            "https://swdyu.com" to "https://streamwish.to",
            "https://cybervynx.com" to "https://streamwish.to",
            "https://dumbalag.com" to "https://streamwish.to",
            "https://awish.pro" to "https://streamwish.to",
            "https://mivalyo.com" to "https://vidhidepro.com",
            "https://dinisglows.com" to "https://vidhidepro.com",
            "https://dhtpre.com" to "https://vidhidepro.com",
            "https://filemoon.link" to "https://filemoon.sx",
            "https://sblona.com" to "https://watchsb.com",
            "https://lulu.st" to "https://lulustream.com",
            "https://uqload.io" to "https://uqload.com",
            "https://do7go.com" to "https://dood.la",
            "https://doodstream.com" to "https://dood.la",
            "https://streamtape.com" to "https://streamtape.cc",
            "https://minochinos.com" to "https://vidhidepro.com",
        )
    }

    private fun fixHostsTitle(url: String): String {
        var result = url
        fixHosts.forEach { (old, new) ->
            result = result.replaceFirst(old, new)
        }
        return result
    }

    private fun cleanTitle(title: String?): String? {
        if (title == null) return null
        return title
            .replace(Regex("(?i)^(Ver|Ver Online)\\s+"), "")
            .replace(Regex("(?i)\\s+Online\\s+Gratis.*$"), "")
            .replace(Regex("(?i)\\s+HD$"), "")
            .replace(Regex("\\s+\\(\\d{4}\\)$"), "")
            .replace(Regex("\\s+\\d{4}$"), "")
            .trim()
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val urls = listOf(
            Pair("Series", "$mainUrl/series"),
            Pair("Películas", "$mainUrl/peliculas"),
            Pair("Animes", "$mainUrl/animes"),
            Pair("Doramas", "$mainUrl/generos/dorama"),
        )

        val homePageLists = urls.amap { (name, url) ->
            val doc = app.get(url).document
            val homeItems = doc.select("div.Posters a.Posters-link").mapNotNull { element ->
                val rawTitle = element.selectFirst("div.listing-content p")?.text()
                    ?: element.attr("data-title")
                val clean = cleanTitle(rawTitle) ?: return@mapNotNull null
                val link = element.attr("href") ?: return@mapNotNull null
                val img = element.selectFirst("img")?.attr("src")
                newAnimeSearchResponse(clean, fixUrl(link)) {
                    this.posterUrl = fixUrlNull(img)
                    this.type = if (url.contains("peliculas")) TvType.Movie else TvType.TvSeries
                }
            }
            HomePageList(name, homeItems)
        }
        return newHomePageResponse(homePageLists, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        if (query.isBlank()) return emptyList()
        val url = "$mainUrl/search?s=$query"
        Log.d(TAG, "Búsqueda: $url")
        val doc = app.get(url).document
        return doc.select("div.Posters a.Posters-link").mapNotNull {
            val rawTitle = it.selectFirst("div.listing-content p")?.text()
                ?: it.attr("data-title")
            val clean = cleanTitle(rawTitle) ?: return@mapNotNull null
            val link = it.attr("href") ?: return@mapNotNull null
            val img = it.selectFirst("img")?.attr("src")
            newAnimeSearchResponse(clean, fixUrl(link)) {
                this.posterUrl = fixUrlNull(img)
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Cargando: $url")
        val doc = app.get(url).document
        val isMovie = url.contains("/pelicula/")

        val rawTitle = doc.selectFirst("h1.m-b-5")?.text() ?: ""
        val title = cleanTitle(rawTitle) ?: rawTitle

        val year = doc.select("div.p-v-20.p-r-15 span.text-info").text().toIntOrNull()
            ?: Regex("""(\d{4})""").find(rawTitle)?.groupValues?.get(1)?.toIntOrNull()

        val genres = doc.select("a[href*='/generos/'] span").map { it.text() }
        val country = doc.selectFirst("a[href*='/pais/']")?.text()
        val allTags = if (country != null) genres + country else genres

        val ratingText = doc.select("span.ion-md-star").text().split("/").firstOrNull()?.trim()
        val score = ratingText?.toDoubleOrNull()?.let { Score.from10(it) }

        val poster = fixUrl(doc.selectFirst("img.img-fluid")?.attr("src") ?: "")
        val description = doc.selectFirst("div.text-large")?.text() ?: ""

        return if (isMovie) {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.tags = allTags
                this.score = score
            }
        } else {
            val episodes = doc.select("div.tab-content div a").mapNotNull { element ->
                val epUrl = fixUrl(element.attr("href") ?: "")
                val epName = element.text().trim()
                if (epUrl.isBlank() || epName.isBlank()) return@mapNotNull null

                val season = Regex("(?i)T(\\d+)").find(epName)?.groupValues?.get(1)?.toIntOrNull() ?: 1
                val episode = Regex("(?i)E(\\d+)").find(epName)?.groupValues?.get(1)?.toIntOrNull()

                newEpisode(epUrl) {
                    this.name = epName
                    this.season = season
                    this.episode = episode
                }
            }

            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                this.posterUrl = poster
                this.plot = description
                this.year = year
                this.tags = allTags
                this.score = score
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean = coroutineScope {
        val doc = app.get(data).document

        val scriptContent = doc.select("script").joinToString("\n") { it.html() }
        val playerUrls = Regex("""video\[\d+\]\s*=\s*['"](https?:\/\/[^"']+)['"]""")
            .findAll(scriptContent).map { it.groupValues[1] }.toList()

        doc.selectFirst("iframe")?.attr("src")?.let { if (it.isNotBlank()) playerUrls.toMutableList().add(fixUrl(it)) }

        if (playerUrls.isEmpty()) return@coroutineScope false

        playerUrls.distinct().amap { playerUrl ->
            try {
                val resp = app.get(fixUrl(playerUrl), referer = data)
                val html = resp.text
                val dataLinkMatch = Regex("""dataLink\s*=\s*(\[.*?\])\s*;""").find(html)
                if (dataLinkMatch == null) {
                    Log.w(TAG, "Sin dataLink en: $playerUrl")
                    loadExtractor(fixHostsTitle(fixUrl(playerUrl)), data, subtitleCallback, callback)
                    return@amap
                }

                val items = tryParseJson<List<Item>>(dataLinkMatch.groupValues[1])
                if (items == null) {
                    Log.e(TAG, "Error parseando dataLink JSON")
                    return@amap
                }

                val langMap = mapOf("LAT" to "LATINO", "ESP" to "CASTELLANO", "SUB" to "SUBTITULADO")
                items.amap { item ->
                    val langTag = langMap[item.videoLanguage] ?: item.videoLanguage ?: "??"
                    item.sortedEmbeds.amap { embed ->
                        val decrypted = cryptoAESDecrypt(embed.link ?: return@amap, cryptoKey)
                        if (decrypted != null) {
                            loadExtractor(fixHostsTitle(decrypted), data, subtitleCallback) { link ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    callback(
                                        newExtractorLink(
                                            "$langTag[${link.source}]",
                                            "$langTag[${link.source}]",
                                            link.url
                                        ) {
                                            this.quality = link.quality
                                            this.type = link.type
                                            this.referer = link.referer
                                            this.headers = link.headers
                                            this.extractorData = link.extractorData
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando $playerUrl: ${e.message}")
            }
        }
        return@coroutineScope true
    }

    private fun cryptoAESDecrypt(data: String, key: String): String? {
        return try {
            val keyBytes = MessageDigest.getInstance("MD5").digest(key.toByteArray(Charsets.UTF_8))
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val decoded = Base64.decode(data, Base64.NO_WRAP)
            val iv = decoded.copyOfRange(0, 16)
            val encrypted = decoded.copyOfRange(16, decoded.size)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
            String(cipher.doFinal(encrypted), Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e(TAG, "CryptoAES error: ${e.message}")
            null
        }
    }

    data class Item(
        @JsonProperty("file_id") val fileId: Int? = null,
        @JsonProperty("video_language") val videoLanguage: String? = null,
        @JsonProperty("sortedEmbeds") val sortedEmbeds: List<Embed> = emptyList(),
    )

    data class Embed(
        @JsonProperty("servername") val servername: String? = null,
        @JsonProperty("link") val link: String? = null,
        @JsonProperty("type") val type: String? = null,
    )
}
