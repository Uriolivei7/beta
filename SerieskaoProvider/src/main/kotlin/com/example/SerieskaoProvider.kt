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

    override var lang = "mx"
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

        Log.d(TAG, "getMainPage page=$page, categorías=${urls.size}")
        val homePageLists = urls.amap { (name, url) ->
            try {
                val doc = app.get(url).document
                val items = doc.select("div.Posters a.Posters-link")
                Log.d(TAG, "getMainPage[$name] elementos HTML=${items.size}")
                val homeItems = items.mapNotNull { element ->
                    try {
                        val rawTitle = element.selectFirst("div.listing-content p")?.text()
                            ?: element.attr("data-title")
                        val clean = cleanTitle(rawTitle)
                        if (clean == null) {
                            Log.w(TAG, "getMainPage[$name] título nulo: raw='$rawTitle'")
                            return@mapNotNull null
                        }
                        val link = element.attr("href")
                        if (link == null) {
                            Log.w(TAG, "getMainPage[$name] link nulo para '$clean'")
                            return@mapNotNull null
                        }
                        val img = element.selectFirst("img")?.attr("src")
                        newAnimeSearchResponse(clean, fixUrl(link)) {
                            this.posterUrl = fixUrlNull(img)
                            this.type = if (url.contains("peliculas")) TvType.Movie else TvType.TvSeries
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "getMainPage[$name] error en item: ${e.message}")
                        null
                    }
                }
                Log.d(TAG, "getMainPage[$name] items válidos=${homeItems.size}")
                HomePageList(name, homeItems)
            } catch (e: Exception) {
                Log.e(TAG, "getMainPage[$name] error cargando '$url': ${e.message}")
                HomePageList(name, emptyList())
            }
        }
        val totalItems = homePageLists.sumOf { it.list.size }
        Log.d(TAG, "getMainPage total items=$totalItems")
        return newHomePageResponse(homePageLists, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        if (query.isBlank()) {
            Log.w(TAG, "search query vacía")
            return emptyList()
        }
        val url = "$mainUrl/search?s=$query"
        Log.d(TAG, "search query='$query' url=$url")
        return try {
            val doc = app.get(url).document
            val items = doc.select("div.Posters a.Posters-link")
            Log.d(TAG, "search elementos HTML=${items.size}")
            val results = items.mapNotNull {
                try {
                    val rawTitle = it.selectFirst("div.listing-content p")?.text()
                        ?: it.attr("data-title")
                    val clean = cleanTitle(rawTitle)
                    if (clean == null) {
                        Log.w(TAG, "search título nulo: raw='$rawTitle'")
                        return@mapNotNull null
                    }
                    val link = it.attr("href")
                    if (link == null) {
                        Log.w(TAG, "search link nulo para '$clean'")
                        return@mapNotNull null
                    }
                    val img = it.selectFirst("img")?.attr("src")
                    newAnimeSearchResponse(clean, fixUrl(link)) {
                        this.posterUrl = fixUrlNull(img)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "search error en item: ${e.message}")
                    null
                }
            }
            Log.d(TAG, "search resultados=${results.size}")
            results
        } catch (e: Exception) {
            Log.e(TAG, "search error cargando '$url': ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "load url=$url")
        return try {
            val doc = app.get(url).document
            val isMovie = url.contains("/pelicula/")
            Log.d(TAG, "load isMovie=$isMovie")

            val rawTitle = doc.selectFirst("h1.m-b-5")?.text() ?: ""
            val title = cleanTitle(rawTitle) ?: rawTitle
            Log.d(TAG, "load title='$title' raw='$rawTitle'")

            val year = doc.select("div.p-v-20.p-r-15 span.text-info").text().toIntOrNull()
                ?: Regex("""(\d{4})""").find(rawTitle)?.groupValues?.get(1)?.toIntOrNull()
            Log.d(TAG, "load year=$year")

            val genres = doc.select("a[href*='/generos/'] span").map { it.text() }
            val country = doc.selectFirst("a[href*='/pais/']")?.text()
            val allTags = if (country != null) genres + country else genres
            Log.d(TAG, "load genres=${genres.size} country=$country")

            val ratingText = doc.select("span.ion-md-star").text().split("/").firstOrNull()?.trim()
            val score = ratingText?.toDoubleOrNull()?.let { Score.from10(it) }
            Log.d(TAG, "load rating='$ratingText' score=$score")

            val poster = fixUrl(doc.selectFirst("img.img-fluid")?.attr("src") ?: "")
            val description = doc.selectFirst("div.text-large")?.text() ?: ""
            Log.d(TAG, "load poster='$poster' desc length=${description.length}")

            if (isMovie) {
                Log.d(TAG, "load -> MovieResponse: $title")
                newMovieLoadResponse(title, url, TvType.Movie, url) {
                    this.posterUrl = poster
                    this.plot = description
                    this.year = year
                    this.tags = allTags
                    this.score = score
                }
            } else {
                val episodeElements = doc.select("div.tab-content div a")
                Log.d(TAG, "load episodios HTML=${episodeElements.size}")
                val episodes = episodeElements.mapNotNull { element ->
                    try {
                        val epUrl = fixUrl(element.attr("href") ?: "")
                        val epName = element.text().trim()
                        if (epUrl.isBlank() || epName.isBlank()) {
                            Log.w(TAG, "load episodio saltado: url='$epUrl' name='$epName'")
                            return@mapNotNull null
                        }
                        val season = Regex("(?i)T(\\d+)").find(epName)?.groupValues?.get(1)?.toIntOrNull() ?: 1
                        val episode = Regex("(?i)E(\\d+)").find(epName)?.groupValues?.get(1)?.toIntOrNull()
                        newEpisode(epUrl) {
                            this.name = epName
                            this.season = season
                            this.episode = episode
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "load error creando episodio: ${e.message}")
                        null
                    }
                }
                Log.d(TAG, "load -> TvSeriesResponse: $title, episodios=${episodes.size}")
                newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes) {
                    this.posterUrl = poster
                    this.plot = description
                    this.year = year
                    this.tags = allTags
                    this.score = score
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "load error cargando '$url': ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean = coroutineScope {
        Log.d(TAG, "loadLinks data='$data'")
        val doc = try {
            app.get(data).document
        } catch (e: Exception) {
            Log.e(TAG, "loadLinks error fetching page: ${e.message}")
            return@coroutineScope false
        }

        val scriptContent = doc.select("script").joinToString("\n") { it.html() }
        val playerUrls = Regex("""video\[\d+\]\s*=\s*['"](https?:\/\/[^"']+)['"]""")
            .findAll(scriptContent).map { it.groupValues[1] }.toList().toMutableList()

        doc.selectFirst("iframe")?.attr("src")?.let { if (it.isNotBlank()) playerUrls.add(fixUrl(it)) }

        Log.d(TAG, "loadLinks scripts=${doc.select("script").size} playerUrls=${playerUrls.size} urls=$playerUrls")

        if (playerUrls.isEmpty()) {
            Log.e(TAG, "loadLinks no se encontraron player URLs en el HTML")
            return@coroutineScope false
        }

        playerUrls.distinct().amap { playerUrl ->
            try {
                val fixedUrl = fixUrl(playerUrl)
                Log.d(TAG, "loadLinks procesando playerUrl=$fixedUrl")
                val resp = app.get(fixedUrl, referer = data)
                val html = resp.text
                Log.d(TAG, "loadLinks resp length=${html.length}")

                val dataLinkMatch = Regex("""dataLink\s*=\s*(\[.*?\])\s*;""").find(html)
                if (dataLinkMatch == null) {
                    Log.w(TAG, "loadLinks sin dataLink en $fixedUrl, intentando loadExtractor directo")
                    loadExtractor(fixHostsTitle(fixedUrl), data, subtitleCallback, callback)
                    return@amap
                }

                val jsonStr = dataLinkMatch.groupValues[1]
                Log.d(TAG, "loadLinks dataLink JSON length=${jsonStr.length}")
                val items = tryParseJson<List<Item>>(jsonStr)
                if (items == null) {
                    Log.e(TAG, "loadLinks error parseando dataLink JSON: substr=${jsonStr.take(200)}")
                    return@amap
                }
                Log.d(TAG, "loadLinks dataLink items=${items.size}")

                val langMap = mapOf("LAT" to "LATINO", "ESP" to "CASTELLANO", "SUB" to "SUBTITULADO")
                items.amap { item ->
                    val langTag = langMap[item.videoLanguage] ?: item.videoLanguage ?: "??"
                    Log.d(TAG, "loadLinks item lang=$langTag embeds=${item.sortedEmbeds.size}")
                    item.sortedEmbeds.amap { embed ->
                        val encrypted = embed.link
                        if (encrypted == null) {
                            Log.w(TAG, "loadLinks embed sin link")
                            return@amap
                        }
                        Log.d(TAG, "loadLinks descifrando embed server=${embed.servername} encrypted=${encrypted.take(50)}...")
                        val decrypted = cryptoAESDecrypt(encrypted, cryptoKey)
                        if (decrypted == null) {
                            Log.e(TAG, "loadLinks falló descifrado para ${embed.servername}")
                            return@amap
                        }
                        Log.d(TAG, "loadLinks descifrado OK -> $decrypted")
                        val fixedDec = fixHostsTitle(decrypted)
                        loadExtractor(fixedDec, data, subtitleCallback) { link ->
                            Log.d(TAG, "loadLinks extractor OK source=${link.source} url=${link.url.take(80)}")
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
            } catch (e: Exception) {
                Log.e(TAG, "loadLinks error procesando $playerUrl: ${e.message}")
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
