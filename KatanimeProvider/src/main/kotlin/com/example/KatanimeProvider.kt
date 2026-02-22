package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.Jsoup
import kotlinx.coroutines.*
import java.security.MessageDigest
import java.util.Arrays
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
        val total: Int? = null,
        @SerialName("per_page") val perPage: Int? = null,
        val data: List<DataEpisode> = emptyList()
    )

    @Serializable
    data class DataEpisode(
        val numero: String? = null,
        val thumb: String? = null,
        val url: String? = null
    )

    private fun decryptWithSalt(cipherTextBase64: String, saltHex: String, password: String): String {
        return try {
            val keySize = 32
            val ivSize = 16

            val ctBytes = AndroidBase64.decode(cipherTextBase64, AndroidBase64.DEFAULT)
            val saltBytes = saltHex.decodeHex()
            val passBytes = password.toByteArray(Charsets.UTF_8)

            val md = MessageDigest.getInstance("MD5")
            val generatedData = ByteArray(keySize + ivSize)
            var generatedLength = 0
            var lastDigest = ByteArray(0)

            while (generatedLength < keySize + ivSize) {
                md.reset()
                if (generatedLength > 0) md.update(lastDigest)
                md.update(passBytes)
                md.update(saltBytes, 0, 8)
                lastDigest = md.digest()

                val copyLen = minOf(lastDigest.size, generatedData.size - generatedLength)
                System.arraycopy(lastDigest, 0, generatedData, generatedLength, copyLen)
                generatedLength += copyLen
            }

            val key = generatedData.copyOfRange(0, keySize)
            val iv = generatedData.copyOfRange(keySize, keySize + ivSize)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))

            val decryptedBytes = cipher.doFinal(ctBytes)
            String(decryptedBytes, Charsets.UTF_8).replace("\"", "").trim()
        } catch (e: Exception) {
            Log.e(TAG, "ERROR DECRYPT: ${e.message}")
            ""
        }
    }

    private fun String.decodeHex(): ByteArray {
        return chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val doc = app.get(mainUrl).document
        val homePageLists = mutableListOf<HomePageList>()
        val recientesCaps = doc.select("div#article-div.chap div._135yj").mapNotNull {
            val title = it.selectFirst("a._2uHIS")?.text() ?: return@mapNotNull null
            val capLink = it.selectFirst("a._1A2Dc")?.attr("href") ?: ""
            val animeLink = if (capLink.contains("/capitulo/")) {
                val slug = capLink.substringAfter("/capitulo/").substringBeforeLast("-")
                "$mainUrl/anime/$slug"
            } else capLink
            newAnimeSearchResponse(title, fixUrl(animeLink)) { this.posterUrl = fixUrl(it.selectFirst("img")?.attr("data-src") ?: "") }
        }
        if (recientesCaps.isNotEmpty()) homePageLists.add(HomePageList("Capítulos Recientes", recientesCaps))
        return newHomePageResponse(homePageLists, false)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val doc = app.get("$mainUrl/buscar?q=$query").document
        return doc.select("div._135yj").mapNotNull {
            val title = it.selectFirst("a._2uHIS")?.text() ?: return@mapNotNull null
            newAnimeSearchResponse(title, fixUrl(it.selectFirst("a._1A2Dc")?.attr("href") ?: "")) {
                this.posterUrl = fixUrl(it.selectFirst("img")?.attr("data-src") ?: "")
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val response = app.get(url)
        val doc = Jsoup.parse(response.text)
        val title = doc.selectFirst("h1.comics-title")?.text() ?: ""
        val mainPoster = fixUrl(doc.selectFirst("div#animeinfo img")?.attr("data-src") ?: "")

        val apiToken = doc.selectFirst("meta[name=csrf-token]")?.attr("content") ?: ""
        val apiUrl = doc.selectFirst("div._pagination")?.attr("data-url") ?: ""

        val episodesList = mutableListOf<Episode>()

        if (apiToken.isNotEmpty() && apiUrl.isNotEmpty()) {
            var page = 1
            var totalProcessed = 0
            var maxEpisodes = 0
            do {
                val apiRes = app.post(apiUrl,
                    data = mapOf("_token" to apiToken, "pagina" to page.toString()),
                    headers = mapOf("X-Requested-With" to "XMLHttpRequest", "Referer" to url),
                    cookies = response.cookies
                )
                val parsed = tryParseJson<EpisodeList>(apiRes.text)
                val items = parsed?.ep?.data ?: emptyList()
                if (page == 1) maxEpisodes = parsed?.ep?.total ?: 0

                items.forEach { ep ->
                    episodesList.add(newEpisode(EpisodeLoadData(fixUrl(ep.url ?: "")).toJson()) {
                        this.name = "Episodio ${ep.numero}"
                        this.episode = ep.numero?.toIntOrNull()
                        this.posterUrl = fixUrl(ep.thumb ?: mainPoster)
                    })
                }
                totalProcessed += items.size
                page++
            } while (totalProcessed < maxEpisodes && items.isNotEmpty())
        }

        return newTvSeriesLoadResponse(title, url, TvType.Anime, episodesList.distinctBy { it.episode }.sortedBy { it.episode }) {
            this.posterUrl = mainPoster
            this.plot = doc.selectFirst("#sinopsis p")?.text()
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean = coroutineScope {
        val episodeUrl = tryParseJson<EpisodeLoadData>(data)?.url ?: data
        val doc = app.get(episodeUrl).document
        val players = doc.select("ul.ul-drop li a[data-player]")

        players.amap { element ->
            val serverName = element.text()
            try {
                val dataPlayer = element.attr("data-player")
                val playerPage = app.get("$mainUrl/reproductor?url=$dataPlayer", referer = episodeUrl).text

                // Limpieza de la variable e para evitar el error Base64
                val encrypted = playerPage.substringAfter("var e = '", "").substringBefore("';", "")
                    .replace("\\/", "/")
                    .replace(" ", "")

                if (encrypted.isNotEmpty()) {
                    val crypto = tryParseJson<CryptoDto>(encrypted)
                    if (crypto?.ct != null && crypto.s != null) {
                        val decryptedUrl = decryptWithSalt(crypto.ct, crypto.s, DECRYPTION_PASSWORD)
                        if (decryptedUrl.startsWith("http")) {
                            Log.d(TAG, "LOG-OK: $serverName -> $decryptedUrl")
                            loadExtractor(decryptedUrl, episodeUrl, subtitleCallback, callback)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "FAIL: $serverName -> ${e.message}")
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