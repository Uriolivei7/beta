package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import kotlinx.coroutines.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64 as AndroidBase64
import kotlinx.serialization.*
import kotlinx.serialization.json.*

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
        @SerialName("current_page") val currentPage: Int? = null,
        @SerialName("last_page") val lastPage: Int? = null,
        val data: List<DataEpisode> = emptyList()
    )

    @Serializable
    data class DataEpisode(
        val numero: String? = null,
        val thumb: String? = null,
        val url: String? = null,
        @SerialName("created_at") val createdAt: String? = null
    )

    private fun cleanTitle(title: String?): String? {
        if (title == null) return null
        return title.replace(Regex("(?i)^(Ver|Ver Online)\\s+"), "")
            .replace(Regex("(?i)\\s+Online\\s+Gratis.*$"), "")
            .replace(Regex("\\s+\\(\\d{4}\\)$"), "").trim()
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
            String(cipher.doFinal(ctBytes), Charsets.UTF_8).replace("\"", "")
        } catch (e: Exception) {
            Log.e(TAG, "Error decriptando: ${e.message}")
            ""
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/buscar?q=$query"
        val response = app.get(url)
        val doc = Jsoup.parse(response.text)

        return doc.select("div#article-div div._135yj").mapNotNull {
            val title = cleanTitle(it.selectFirst("a._2uHIS")?.text()) ?: return@mapNotNull null
            val href = it.selectFirst("a._1A2Dc")?.attr("href") ?: return@mapNotNull null
            val poster = it.selectFirst("img")?.attr("src") ?: it.selectFirst("img")?.attr("data-src")

            newAnimeSearchResponse(title, fixUrl(href)) {
                this.posterUrl = fixUrl(poster ?: "")
            }
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val html = app.get(mainUrl).text
        val doc = Jsoup.parse(html)
        val homePageLists = mutableListOf<HomePageList>()

        val recientes = doc.select("div#article-div.recientes div._135yj").mapNotNull {
            val title = cleanTitle(it.selectFirst("a._2uHIS")?.text()) ?: return@mapNotNull null
            val link = fixUrl(it.selectFirst("a._1A2Dc")?.attr("href") ?: "")
            val poster = it.selectFirst("img")?.attr("data-src") ?: it.selectFirst("img")?.attr("src")

            newAnimeSearchResponse(title, link) {
                this.posterUrl = fixUrl(poster ?: "")
            }
        }
        if (recientes.isNotEmpty()) homePageLists.add(HomePageList("Animes Recientes", recientes))

        return newHomePageResponse(homePageLists, false)
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Iniciando carga: $url")

        val response = app.get(url)
        val doc = Jsoup.parse(response.text)
        val cookies = response.cookies

        val title = cleanTitle(doc.selectFirst("h1.comics-title")?.text()) ?: ""
        val mainPoster = fixUrl(doc.selectFirst("div#animeinfo img")?.attr("data-src") ?: "")
        val description = doc.selectFirst("#sinopsis p")?.text() ?: ""

        val apiToken = doc.selectFirst("meta[name=csrf-token]")?.attr("content")
        val apiUrl = doc.selectFirst("div._pagination")?.attr("data-url")

        val allEpisodes = mutableListOf<Episode>()

        if (apiToken != null && apiUrl != null) {
            var pageToLoad = 1
            var totalPages = 1

            try {
                do {
                    Log.d(TAG, "Solicitando API Pag $pageToLoad a $apiUrl con Token $apiToken")

                    val apiRes = app.post(
                        apiUrl,
                        data = mapOf("_token" to apiToken, "pagina" to pageToLoad.toString()),
                        cookies = cookies,
                        headers = mapOf(
                            "Referer" to url,
                            "X-Requested-With" to "XMLHttpRequest",
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        )
                    )

                    if (!apiRes.isSuccessful) {
                        Log.e(TAG, "Error en API: Código ${apiRes.code}. Cuerpo: ${apiRes.text}")
                        break
                    }

                    val parsed = tryParseJson<EpisodeList>(apiRes.text)
                    val epData = parsed?.ep?.data

                    if (epData.isNullOrEmpty()) {
                        Log.w(TAG, "La API devolvió datos vacíos en la página $pageToLoad")
                        break
                    }

                    if (pageToLoad == 1) {
                        totalPages = parsed.ep?.lastPage ?: 1
                        Log.d(TAG, "Total de páginas detectadas: $totalPages")
                    }

                    epData.forEach { item ->
                        val epUrl = item.url ?: ""
                        if (epUrl.isNotEmpty()) {
                            allEpisodes.add(newEpisode(EpisodeLoadData(fixUrl(epUrl)).toJson()) {
                                this.name = "Episodio ${item.numero}"
                                this.episode = item.numero?.toIntOrNull()
                                this.posterUrl = if (!item.thumb.isNullOrBlank()) fixUrl(item.thumb) else mainPoster
                            })
                        }
                    }

                    pageToLoad++
                } while (pageToLoad <= totalPages)
            } catch (e: Exception) {
                Log.e(TAG, "Excepción cargando episodios: ${e.message}")
                e.printStackTrace()
            }
        } else {
            Log.e(TAG, "No se encontró apiToken ($apiToken) o apiUrl ($apiUrl)")
        }

        if (allEpisodes.isEmpty()) {
            Log.e(TAG, "CRÍTICO: No se pudo extraer ningún episodio de $url")
        }

        return newTvSeriesLoadResponse(title, url, TvType.Anime, allEpisodes.sortedBy { it.episode }) {
            this.posterUrl = mainPoster
            this.plot = description
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
        val doc = app.get(episodeUrl).document

        doc.select("ul.ul-drop li a[data-player]").amap { element ->
            val serverTitle = element.text()
            val dataPlayer = element.attr("data-player")

            val playerDoc = app.get("$mainUrl/reproductor?url=$dataPlayer").document
            val script = playerDoc.selectFirst("script:containsData(var e =)")?.data()
            val encryptedBase64 = script?.substringAfter("var e = '")?.substringBefore("';")

            if (!encryptedBase64.isNullOrBlank()) {
                val jsonCrypto = tryParseJson<CryptoDto>(
                    String(AndroidBase64.decode(encryptedBase64, AndroidBase64.DEFAULT))
                )

                if (jsonCrypto?.ct != null && jsonCrypto.s != null) {
                    val decryptedUrl = decryptWithSalt(jsonCrypto.ct, jsonCrypto.s, DECRYPTION_PASSWORD)
                    loadExtractor(decryptedUrl, episodeUrl, subtitleCallback, callback)
                }
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