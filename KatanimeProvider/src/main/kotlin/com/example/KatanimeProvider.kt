package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.Jsoup
import kotlinx.coroutines.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64 as AndroidBase64
import kotlinx.serialization.*

class KatanimeProvider : MainAPI() {
    override var mainUrl = "https://katanime.net"
    override var name = "KATANIME"
    override val supportedTypes = setOf(TvType.Anime)
    override var lang = "mx"
    override val hasMainPage = true

    private val TAG = "KatanimeProvider"
    private val DECRYPTION_PASSWORD = "hanabi"

    @Serializable
    data class CryptoDto(val ct: String? = null, val s: String? = null)

    @Serializable
    data class EpisodeLoadData(val url: String)

    @Serializable
    data class EpisodeList(val ep: Ep? = null)

    @Serializable
    data class Ep(
        @SerialName("last_page") val lastPage: Int? = null,
        @SerialName("current_page") val currentPage: Int? = null,
        @SerialName("total") val total: Int? = null,
        val data: List<DataEpisode> = emptyList()
    )

    @Serializable
    data class DataEpisode(
        val numero: String? = null,
        val thumb: String? = null,
        val url: String? = null
    )

    private fun cleanTitle(title: String?): String? {
        return title?.replace(Regex("(?i)^(Ver|Ver Online)\\s+|\\s+Online\\s+Gratis.*$|\\s+\\(\\d{4}\\)$"), "")?.trim()
    }

    private fun String.decodeHex(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    private fun decryptWithSalt(cipherText: String, salt: String, password: String): String {
        return try {
            val ctBytes = AndroidBase64.decode(cipherText, AndroidBase64.DEFAULT)
            val saltBytes = salt.decodeHex()
            val passBytes = password.toByteArray(Charsets.UTF_8)

            val md5 = MessageDigest.getInstance("MD5")
            val generatedData = ByteArray(48)
            var generatedLength = 0
            var lastDigest = ByteArray(0)

            while (generatedLength < 48) {
                md5.reset()
                md5.update(lastDigest)
                md5.update(passBytes)
                md5.update(saltBytes, 0, 8)
                lastDigest = md5.digest()
                System.arraycopy(lastDigest, 0, generatedData, generatedLength, lastDigest.size)
                generatedLength += lastDigest.size
            }

            val key = generatedData.copyOfRange(0, 32)
            val iv = generatedData.copyOfRange(32, 48)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
            val decrypted = String(cipher.doFinal(ctBytes), Charsets.UTF_8)
            decrypted.replace("\"", "").trim()
        } catch (e: Exception) {
            Log.e(TAG, "FALLO CRITICO DESCIFRADO: ${e.message}")
            ""
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val response = app.get(mainUrl)
        val doc = Jsoup.parse(response.text)
        val homePageLists = mutableListOf<HomePageList>()

        val recientesCaps = doc.select("div#article-div.chap div._135yj").mapNotNull {
            val title = it.selectFirst("a._2uHIS")?.text() ?: return@mapNotNull null
            val capLink = it.selectFirst("a._1A2Dc")?.attr("href") ?: ""
            val animeLink = if (capLink.contains("/capitulo/")) {
                val slug = capLink.substringAfter("/capitulo/").substringBeforeLast("-")
                "$mainUrl/anime/$slug"
            } else capLink
            val img = it.selectFirst("img")?.let { i -> i.attr("data-src").ifBlank { i.attr("src") } }
            newAnimeSearchResponse(cleanTitle(title)!!, fixUrl(animeLink)) { this.posterUrl = fixUrl(img ?: "") }
        }
        if (recientesCaps.isNotEmpty()) homePageLists.add(HomePageList("Capítulos Recientes", recientesCaps))

        return newHomePageResponse(homePageLists, false)
    }

    // --- SEARCH REINSERTADO ---
    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "LOG-SEARCH: Buscando $query")
        val doc = app.get("$mainUrl/buscar?q=$query").document
        return doc.select("div._135yj").mapNotNull {
            val title = it.selectFirst("a._2uHIS")?.text() ?: return@mapNotNull null
            val img = it.selectFirst("img")?.let { i -> i.attr("data-src").ifBlank { i.attr("src") } }
            newAnimeSearchResponse(cleanTitle(title)!!, fixUrl(it.selectFirst("a._1A2Dc")?.attr("href") ?: "")) {
                this.posterUrl = fixUrl(img ?: "")
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "LOG-LOAD: Iniciando en $url")
        val response = app.get(url)
        val doc = Jsoup.parse(response.text)

        val title = cleanTitle(doc.selectFirst("h1.comics-title")?.text()) ?: ""
        val imgElem = doc.selectFirst("div#animeinfo img")
        val mainPoster = fixUrl(imgElem?.attr("data-src")?.ifBlank { imgElem.attr("src") } ?: "")

        val apiToken = doc.selectFirst("meta[name=csrf-token]")?.attr("content")
        val apiUrl = doc.selectFirst("div._pagination")?.attr("data-url")

        val episodesList = mutableListOf<Episode>()

        if (apiToken != null && apiUrl != null) {
            var page = 1
            var lastPage = 1
            do {
                Log.d(TAG, "LOG-API: Pidiendo página $page de $lastPage")
                val apiRes = app.post(
                    apiUrl,
                    data = mapOf("_token" to apiToken, "pagina" to page.toString()),
                    headers = mapOf("X-Requested-With" to "XMLHttpRequest", "Referer" to url),
                    cookies = response.cookies
                )

                val parsed = tryParseJson<EpisodeList>(apiRes.text)
                val items = parsed?.ep?.data ?: emptyList()
                Log.d(TAG, "LOG-API: Pag $page tiene ${items.size} eps. Total meta: ${parsed?.ep?.total}")

                items.forEach { ep ->
                    episodesList.add(newEpisode(EpisodeLoadData(fixUrl(ep.url ?: "")).toJson()) {
                        this.name = "Episodio ${ep.numero}"
                        this.episode = ep.numero?.toIntOrNull()
                        this.posterUrl = fixUrl(ep.thumb ?: mainPoster)
                    })
                }
                lastPage = parsed?.ep?.lastPage ?: 1
                page++
            } while (page <= lastPage)
        }

        Log.d(TAG, "LOG-LOAD: Final con ${episodesList.size} episodios")
        return newTvSeriesLoadResponse(title, url, TvType.Anime, episodesList.distinctBy { it.episode }.sortedBy { it.episode }) {
            this.posterUrl = mainPoster
            this.plot = doc.selectFirst("#sinopsis p")?.text()
            this.year = doc.selectFirst(".details-by")?.text()?.let { Regex("\\d{4}").find(it)?.value?.toIntOrNull() }
            this.tags = doc.select(".anime-genres a").map { it.text() }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean = coroutineScope {
        val episodeUrl = tryParseJson<EpisodeLoadData>(data)?.url ?: data
        Log.d(TAG, "LOG-LINKS: Entrando a $episodeUrl")

        val doc = app.get(episodeUrl).document
        val players = doc.select("ul.ul-drop li a[data-player]")
        Log.d(TAG, "LOG-LINKS: ${players.size} servidores encontrados")

        players.amap { element ->
            val serverName = element.text()
            try {
                val dataPlayer = element.attr("data-player")
                val playerPage = app.get("$mainUrl/reproductor?url=$dataPlayer", referer = episodeUrl).text
                val encrypted = playerPage.substringAfter("var e = '", "").substringBefore("';", "")

                if (encrypted.isNotEmpty()) {
                    val rawJson = String(AndroidBase64.decode(encrypted, AndroidBase64.DEFAULT))
                    val crypto = tryParseJson<CryptoDto>(rawJson)

                    if (crypto?.ct != null && crypto.s != null) {
                        val decryptedUrl = decryptWithSalt(crypto.ct, crypto.s, DECRYPTION_PASSWORD)
                        Log.d(TAG, "LOG-DECRYPT: $serverName -> $decryptedUrl")

                        if (decryptedUrl.startsWith("http")) {
                            loadExtractor(decryptedUrl, episodeUrl, subtitleCallback, callback)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "LOG-ERROR-LINK: $serverName -> ${e.message}")
            }
        }
        true
    }

    private fun fixUrl(url: String): String {
        if (url.isEmpty()) return ""
        if (url.startsWith("//")) return "https:$url"
        if (url.startsWith("/")) return mainUrl + url
        return url
    }
}