package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import org.jsoup.nodes.Element
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DanimadosProvider : MainAPI() {
    companion object {
        private const val BASE_URL = "https://danimados.cc"
        private val browserHeaders = mapOf(
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36",
            "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Accept-Language" to "es-ES,es;q=0.9,en;q=0.8",
        )

        private fun unpackPackedJs(html: String): String? {
            val packedRegex = Regex(
                """eval\s*\(\s*function\s*\(\s*p\s*,\s*a\s*,\s*c\s*,\s*k\s*,\s*e\s*,\s*d\s*\)\s*\{[\s\S]*?\}\s*\(\s*'((?:[^'\\]|\\.)*)'\s*,\s*(\d+)\s*,\s*(\d+)\s*,\s*'(.*?)'\s*\.split\s*\(\s*'\|'\s*\)"""
            )
            val match = packedRegex.find(html) ?: return null
            val packed = match.groupValues[1]
            val base = match.groupValues[2].toIntOrNull() ?: return null
            val count = match.groupValues[3].toIntOrNull() ?: return null
            val keywords = match.groupValues[4].split("|")
            if (count != keywords.size) return null
            var result = packed
            for (i in 0 until count) {
                val kw = keywords[i]
                if (kw.isNotEmpty()) {
                    result = result.replace(Regex("\\b${i.toString(base)}\\b"), kw)
                }
            }
            return result
        }

        private fun hexToBytes(hex: String): ByteArray {
            val bytes = ByteArray(hex.length / 2)
            for (i in bytes.indices) {
                bytes[i] = hex.substring(i * 2, i * 2 + 2).toInt(16).toByte()
            }
            return bytes
        }

        private fun decryptAesCbc(hexInput: String, key: String, iv: String): String {
            val keySpec = SecretKeySpec(key.encodeToByteArray(), "AES")
            val ivSpec = IvParameterSpec(iv.encodeToByteArray())
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decrypted = cipher.doFinal(hexToBytes(hexInput))
            return String(decrypted, Charsets.UTF_8)
        }
    }

    override var mainUrl = BASE_URL
    override var name = "Danimados"
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(TvType.Cartoon, TvType.TvSeries)

    override val mainPage = mainPageOf(
        "/" to "Últimas Series Agregadas",
        "/genero/60s/" to "Años 60",
        "/genero/70s/" to "Años 70",
        "/genero/80s/" to "Años 80",
        "/genero/90s/" to "Años 90",
        "/genero/00s/" to "Años 2000",
        "/genero/10s/" to "Años 2010",
        "/genero/20s/" to "Años 2020",
        "/genero/sitcom/" to "Sitcoms",
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        val url = if (request.data == "/") {
            mainUrl
        } else {
            "${mainUrl}${request.data.removeSuffix("/")}/page/$page/"
        }
        Log.d("Danimados", "getMainPage: name=${request.name}, url=$url, page=$page")
        val resp = app.get(url, headers = browserHeaders)
        Log.d("Danimados", "getMainPage: code=${resp.code}, html.len=${resp.text.length}")
        val doc = resp.document
        Log.d("Danimados", "getMainPage: title=${doc.title()}")

        val items = doc.select("article.item.tvshows").mapNotNull { it.toSearchResponse() }
        Log.d("Danimados", "getMainPage: found ${items.size} items")
        if (items.isEmpty()) {
            Log.d("Danimados", "getMainPage: NO ITEMS - checking raw selectors...")
            Log.d("Danimados", "getMainPage: article.tvshows=${doc.select("article.item").size}")
            Log.d("Danimados", "getMainPage: any article=${doc.select("article").size}")
            Log.d("Danimados", "getMainPage: body length=${doc.html().length}")
            return null
        }

        return newHomePageResponse(
            list = HomePageList(request.name, items),
            hasNext = items.size >= 20
        )
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        Log.d("Danimados", "search: query=$query")
        val resp = app.get("$BASE_URL/?s=$query", headers = browserHeaders)
        Log.d("Danimados", "search: code=${resp.code}, html.len=${resp.text.length}")
        val doc = resp.document
        Log.d("Danimados", "search: title=${doc.title()}")
        Log.d("Danimados", "search: article.item=${doc.select("article.item").size}")
        Log.d("Danimados", "search: article=${doc.select("article").size}")
        Log.d("Danimados", "search: .item=${doc.select(".item").size}")
        Log.d("Danimados", "search: .result-item=${doc.select(".result-item").size}")

        val results = mutableListOf<SearchResponse>()
        for (selector in listOf("article.item.tvshows", "article.item", ".result-item", ".search-item")) {
            val found = doc.select(selector).mapNotNull { it.toSearchResponse() }
            results.addAll(found)
            if (results.isNotEmpty()) break
        }

        if (results.isEmpty()) {
            for (article in doc.select("article")) {
                val link = article.select(".data h3 a[href*='/series/']").first()
                    ?: article.select("h3 a[href*='/series/']").first()
                    ?: article.select("a[href*='/series/']").first() ?: continue
                val href = link.attr("abs:href")
                val title = link.text().trim()
                if (title.isBlank() || !href.contains("/series/")) continue
                val poster = article.select("img").first()?.attr("src")?.let { fixUrl(it) }
                results.add(newMovieSearchResponse(title, href, TvType.Cartoon) { this.posterUrl = poster })
            }
        }

        Log.d("Danimados", "search: found ${results.size} items")
        return results.ifEmpty { null }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d("Danimados", "load: url=$url")
        val resp = app.get(url, headers = browserHeaders)
        Log.d("Danimados", "load: code=${resp.code}, html.len=${resp.text.length}")
        val doc = resp.document
        Log.d("Danimados", "load: title=${doc.title()}")

        val title = doc.selectFirst(".sheader .data h1")?.text()
            ?: doc.selectFirst("title")?.text()?.substringBefore(" –")?.substringBefore(" -")?.trim()
        Log.d("Danimados", "load: parsed title=$title")
        if (title == null) return null

        val poster = doc.selectFirst(".sheader .poster img")?.attr("src")
            ?: doc.selectFirst("meta[property='og:image']")?.attr("content")
            ?: ""
        Log.d("Danimados", "load: poster=$poster")

        val description = doc.selectFirst(".hero-overview, .wp-content p")?.text()
            ?: doc.selectFirst("meta[name='description']")?.attr("content")

        val year = doc.selectFirst(".sheader .data .meta-top span")?.text()?.let { extractYear(it) }

        val rating = doc.selectFirst("#repimdb strong")?.text()?.toDoubleOrNull()

        val episodes = extractEpisodes(doc)
        Log.d("Danimados", "load: ${episodes.size} episodes")

        if (episodes.isNotEmpty()) {
            return newTvSeriesLoadResponse(title, url, TvType.Cartoon, episodes) {
                this.posterUrl = fixUrl(poster)
                this.plot = description
                this.year = year
                this.score = rating?.let { Score.from10(it) }
            }
        }

        return newMovieLoadResponse(title, url, TvType.Cartoon, url) {
            this.posterUrl = fixUrl(poster)
            this.plot = description
            this.year = year
            this.score = rating?.let { Score.from10(it) }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("Danimados", "loadLinks: data=$data")
        val resp = app.get(data, headers = browserHeaders)
        Log.d("Danimados", "loadLinks: code=${resp.code}, html.len=${resp.text.length}")
        val doc = resp.document

        val ajaxScript = doc.selectFirst("#dt_main_ajax-js-extra")
        val ajaxUrl = if (ajaxScript != null) {
            val html = ajaxScript.html()
            Log.d("Danimados", "loadLinks: dtAjax script found, len=${html.length}")
            val match = Regex("""url["']?\s*:\s*["']([^"']+)""").find(html)
            val url = match?.groupValues?.get(1)
            Log.d("Danimados", "loadLinks: ajax url from script=$url")
            url?.let { fixRelativeUrl(it) } ?: "$BASE_URL/wp-admin/admin-ajax.php"
        } else {
            Log.d("Danimados", "loadLinks: dtAjax script NOT found!")
            "$BASE_URL/wp-admin/admin-ajax.php"
        }
        Log.d("Danimados", "loadLinks: resolved ajaxUrl=$ajaxUrl")

        val playerOptions = doc.select(".dooplay_player_option")
        Log.d("Danimados", "loadLinks: player options count=${playerOptions.size}")
        if (playerOptions.isEmpty()) {
            Log.d("Danimados", "loadLinks: NO player options found")
            return false
        }

        var anySuccess = false
        for (option in playerOptions) {
            val postId = option.attr("data-post")
            val nume = option.attr("data-nume").toIntOrNull() ?: 1
            val type = option.attr("data-type").ifBlank { "tv" }
            Log.d("Danimados", "loadLinks: trying player nume=$nume postId=$postId type=$type")

            val playerResp = app.post(
                ajaxUrl,
                headers = mapOf(
                    "X-Requested-With" to "XMLHttpRequest",
                    "Accept" to "*/*",
                    "Content-Type" to "application/x-www-form-urlencoded; charset=UTF-8",
                    "Referer" to data,
                ),
                data = mapOf(
                    "action" to "doo_player_ajax",
                    "post" to postId,
                    "nume" to nume.toString(),
                    "type" to type,
                )
            ).parsedSafe<PlayerResponse>()
            if (playerResp == null) {
                Log.d("Danimados", "loadLinks: player nume=$nume returned null response")
                continue
            }
            val embedHtml = playerResp.embedUrl ?: continue
            Log.d("Danimados", "loadLinks: embedHtml (nume=$nume)=${embedHtml.take(400)}")

            val videoUrl = if (embedHtml.startsWith("http://") || embedHtml.startsWith("https://")) {
                embedHtml
            } else {
                Regex("""src=["']([^"']+)["']""").find(embedHtml)?.groupValues?.get(1)
            }
            if (videoUrl.isNullOrBlank()) continue
            Log.d("Danimados", "loadLinks: trying videoUrl=$videoUrl")

            val embedLabel = when {
                videoUrl.contains("callistanise.com") -> "VidHide"
                videoUrl.contains("cubeembed") -> "CubeEmbed"
                videoUrl.contains("turbovid") -> "TurboVid"
                videoUrl.contains("waaw") -> "WaaW"
                else -> "Server$nume"
            }

            val extracted = loadExtractor(videoUrl, data, subtitleCallback, callback)
            Log.d("Danimados", "loadLinks: loadExtractor returned $extracted for nume=$nume")
            if (extracted) anySuccess = true

            Log.d("Danimados", "loadLinks: trying direct fetch of $videoUrl")
            try {
                // --- CubeEmbed handler (Lacartoons version) ---
                if (videoUrl.contains("cubeembed.rpmvid.com") || videoUrl.contains("cubeembed.")) {
                    val hash = videoUrl.substringAfterLast("#").substringAfter("/")
                    val baseUrl = "https://cubeembed.rpmvid.com"
                    Log.d("Danimados", "loadLinks: CubeEmbed detected, hash=$hash baseUrl=$baseUrl")
                    val cubeHeaders = mapOf("User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:134.0) Gecko/20100101 Firefox/134.0")
                    val encrypted = try {
                        app.get("$baseUrl/api/v1/video?id=$hash", headers = cubeHeaders).text.trim()
                    } catch (_: Exception) { null }
                    if (!encrypted.isNullOrEmpty()) {
                        val key = "kiemtienmua911ca"
                        val ivList = listOf("1234567890oiuytr", "0123456789abcdef")
                        val decryptedText = ivList.firstNotNullOfOrNull { iv ->
                            try { decryptAesCbc(encrypted, key, iv) }
                            catch (_: Exception) { null }
                        }
                        if (decryptedText != null) {
                            Log.d("Danimados", "loadLinks: CubeEmbed decrypted OK")
                            val cubeLinkName = "Danimados ($embedLabel)"
                            // Subtitles
                            val subtitleSection = Regex("\"subtitle\":\\{(.*?)\\}").find(decryptedText)?.groupValues?.get(1)
                            subtitleSection?.let { section ->
                                Regex("\"([^\"]+)\":\\s*\"([^\"]+)\"").findAll(section).forEach { match ->
                                    val lang = match.groupValues[1]
                                    val rawPath = match.groupValues[2].split("#")[0]
                                    if (rawPath.isNotEmpty()) {
                                        val path = rawPath.replace("\\/", "/")
                                        val subUrl = "$baseUrl$path"
                                        subtitleCallback(newSubtitleFile(lang, subUrl))
                                    }
                                }
                            }
                            // Source M3U8
                            val source = Regex("\"source\":\"(.*?)\"").find(decryptedText)
                                ?.groupValues?.get(1)?.replace("\\/", "/") ?: ""
                            if (source.isNotEmpty()) {
                                val sourceHttp = source.replaceFirst("https://", "http://")
                                Log.d("Danimados", "loadLinks: CubeEmbed source M3U8: $sourceHttp")
                                callback.invoke(ExtractorLink(
                                    source = cubeLinkName, name = cubeLinkName,
                                    url = sourceHttp,
                                    type = ExtractorLinkType.M3U8, quality = 720,
                                    referer = data,
                                ))
                                anySuccess = true
                            }
                            // TikTok HLS
                            val hlsTiktok = Regex("\"hlsVideoTiktok\":\"(.*?)\"").find(decryptedText)
                                ?.groupValues?.get(1)?.replace("\\/", "/") ?: ""
                            if (hlsTiktok.isNotEmpty()) {
                                val tiktokUrl = "$baseUrl$hlsTiktok"
                                Log.d("Danimados", "loadLinks: CubeEmbed TikTok M3U8: $tiktokUrl")
                                callback.invoke(ExtractorLink(
                                    source = cubeLinkName, name = cubeLinkName,
                                    url = tiktokUrl,
                                    type = ExtractorLinkType.M3U8, quality = 720,
                                    referer = data,
                                ))
                                anySuccess = true
                            }
                        }
                    }
                    continue // CubeEmbed handled, skip generic extraction below
                }

                val embedResp = app.get(videoUrl, headers = browserHeaders + mapOf(
                    "Referer" to data,
                    "Accept" to "*/*",
                ))
                Log.d("Danimados", "loadLinks: embed page code=${embedResp.code}, len=${embedResp.text.length}")
                Log.d("Danimados", "loadLinks: embed html=${embedResp.text.take(1000)}")

                val unpacked = unpackPackedJs(embedResp.text)
                val allVideoCandidates = mutableListOf<String>()
                if (unpacked != null) {
                    Log.d("Danimados", "loadLinks: unpacked JS, len=${unpacked.length}")

                    allVideoCandidates.addAll(
                        Regex("""https?://[^\s"'<>]+\.m3u8[^\s"'<>]*""").findAll(unpacked)
                            .map { it.value }.distinct()
                    )

                    val fileUrls = Regex(""""file"\s*:\s*"(https?://[^"]+)""").findAll(unpacked)
                        .map { it.groupValues[1] }.filter { !it.contains(".m3u8") && !it.contains(".mp4") }.toList()
                    allVideoCandidates.addAll(fileUrls)
                    Log.d("Danimados", "loadLinks: video candidates from unpacked: $allVideoCandidates")

                    // Follow acek-cdn urlset URLs
                    val urlsetUrls = Regex("""urlset\s*['=]\s*'(https?://[^']+)'""").findAll(unpacked)
                        .map { it.groupValues[1] }.distinct().toList()
                    for (urlsetUrl in urlsetUrls) {
                        try {
                            Log.d("Danimados", "loadLinks: following urlset: $urlsetUrl")
                            val resolvedResp = app.get(urlsetUrl,
                                headers = browserHeaders + mapOf("Referer" to data))
                            val bodyText = resolvedResp.text
                            Log.d("Danimados", "loadLinks: urlset response code=${resolvedResp.code} body=${bodyText.take(300)}")
                            val m3u8FromRedirect = Regex("""https?://[^\s"'<>]+\.m3u8[^\s"'<>]*""").find(bodyText)?.value
                            if (m3u8FromRedirect != null) {
                                Log.d("Danimados", "loadLinks: found M3U8 from urlset: $m3u8FromRedirect")
                                allVideoCandidates.add(0, m3u8FromRedirect)
                            }
                            if (bodyText.trimStart().startsWith("#EXTM3U")) {
                                allVideoCandidates.add(0, urlsetUrl)
                            }
                        } catch (e: Exception) {
                            Log.d("Danimados", "loadLinks: urlset follow failed: ${e.message}")
                        }
                    }
                }

                val directSrc = Regex("""(?:src|file|source|url|link)\s*[=:]\s*["']([^"']+\.(?:m3u8|mp4))["']""",
                    RegexOption.IGNORE_CASE).find(embedResp.text)?.groupValues?.get(1)
                if (directSrc != null && directSrc !in allVideoCandidates) {
                    allVideoCandidates.add(0, directSrc)
                }

                val linkLabel = "Danimados ($embedLabel)"
                for (candidate in allVideoCandidates) {
                    val fullUrl = if (candidate.startsWith("http")) candidate else "https:$candidate"
                    try {
                        val headCall = app.get(fullUrl, headers = browserHeaders + mapOf("Referer" to data))
                        val headBuf = ByteArray(100)
                        val headRead = headCall.body.byteStream().use { s -> s.read(headBuf) }
                        val headStr = if (headRead > 0) String(headBuf, 0, headRead) else ""
                        val linkType = when {
                            headStr.trimStart().startsWith("#EXTM3U") -> ExtractorLinkType.M3U8
                            headStr.contains("ftyp") -> ExtractorLinkType.VIDEO
                            else -> null
                        }
                        if (linkType != null) {
                            Log.d("Danimados", "loadLinks: validated video candidate: $fullUrl type=$linkType")
                            callback.invoke(ExtractorLink(
                                source = linkLabel,
                                name = linkLabel,
                                url = fullUrl,
                                type = linkType,
                                quality = 720,
                                referer = data,
                            ))
                            anySuccess = true
                        } else {
                            Log.d("Danimados", "loadLinks: candidate not video: ${headStr.take(40)}")
                        }
                    } catch (e: Exception) {
                        Log.d("Danimados", "loadLinks: candidate failed: $fullUrl - ${e.message}")
                    }
                }
                val b64Match = Regex("""(?:src|file|source|url)\s*[=:]\s*["']([A-Za-z0-9+/=]{20,})["']""",
                    RegexOption.IGNORE_CASE).find(embedResp.text)
                if (b64Match != null) {
                    val decoded = try {
                        String(android.util.Base64.decode(b64Match.groupValues[1], android.util.Base64.DEFAULT))
                    } catch (_: Exception) { null }
                    if (decoded?.contains("m3u8") == true || decoded?.contains("mp4") == true) {
                        Log.d("Danimados", "loadLinks: found base64 source $decoded")
                        callback.invoke(ExtractorLink(
                            source = linkLabel,
                            name = linkLabel,
                            url = decoded,
                            type = ExtractorLinkType.M3U8,
                            quality = 720,
                            referer = data,
                        ))
                        anySuccess = true
                    }
                }
            } catch (e: Exception) {
                Log.d("Danimados", "loadLinks: direct fetch failed: ${e.message}")
            }
        }

        Log.d("Danimados", "loadLinks: anySuccess=$anySuccess (${playerOptions.size} players tried)")
        return anySuccess
    }

    private fun extractEpisodes(doc: org.jsoup.nodes.Document): List<Episode> {
        val episodes = mutableListOf<Episode>()

        Log.d("Danimados", "extractEpisodes: #seasons size=${doc.select("#seasons").size}")
        Log.d("Danimados", "extractEpisodes: .se-c size=${doc.select("#seasons > .se-c").size}")

        val seasonContainers = doc.select("#seasons > .se-c")
        if (seasonContainers.isNotEmpty()) {
            for (seasonContainer in seasonContainers) {
                val seasonNum = seasonContainer.selectFirst(".se-q .se-t")?.text()?.toIntOrNull() ?: continue
                Log.d("Danimados", "extractEpisodes: season $seasonNum")
                val episodeItems = seasonContainer.select(".se-a ul.episodios > li")
                for ((epIdx, li) in episodeItems.withIndex()) {
                    val link = li.selectFirst(".episodiotitle a") ?: continue
                    val epHref = link.attr("abs:href")
                    val epTitle = link.text().trim()
                    val numText = li.selectFirst(".numerando")?.text()
                    val epNum = Regex("""\d+\s*-\s*(\d+)""").find(numText ?: "")?.groupValues?.get(1)?.toIntOrNull()
                        ?: (epIdx + 1)

                    if (epHref.isNotBlank()) {
                        episodes.add(newEpisode(epHref) {
                            this.name = epTitle.ifEmpty { "Episodio $epNum" }
                            this.season = seasonNum
                            this.episode = epNum
                        })
                    }
                }
            }
            return episodes
        }

        val flatEps = doc.select("#seasons .se-a ul.episodios > li")
        for ((ei, li) in flatEps.withIndex()) {
            val link = li.selectFirst(".episodiotitle a") ?: continue
            val epHref = link.attr("abs:href")
            val epTitle = link.text().trim()
            val numText = li.selectFirst(".numerando")?.text()
            val sMatch = Regex("""(\d+)\s*-\s*(\d+)""").find(numText ?: "")
            val seasonNum = sMatch?.groupValues?.get(1)?.toIntOrNull() ?: 1
            val epNum = sMatch?.groupValues?.get(2)?.toIntOrNull() ?: (ei + 1)
            if (epHref.isNotBlank()) {
                episodes.add(newEpisode(epHref) {
                    this.name = epTitle.ifEmpty { "Episodio $epNum" }
                    this.season = seasonNum
                    this.episode = epNum
                })
            }
        }

        return episodes
    }

    private fun Element.toSearchResponse(): SearchResponse? {
        val titleEl = select(".data h3 a[href*='/series/']").first()
            ?: select("h3 a[href*='/series/']").first()
            ?: select(".title").first()
        if (titleEl == null) {
            Log.d("Danimados", "toSearchResponse: no title element in ${this.className()} #${this.id()}")
            return null
        }
        val linkEl = if (titleEl.tagName() == "a") titleEl
            else titleEl.selectFirst("a[href*='/series/']") ?: titleEl.closest("a[href*='/series/']")
        val href = linkEl?.attr("abs:href") ?: titleEl.attr("abs:href")
        val title = titleEl.text().trim()
        if (title.isBlank()) {
            Log.d("Danimados", "toSearchResponse: blank title for href=$href")
            return null
        }
        if (!href.contains("/series/")) return null
        val poster = select("img").first()?.attr("src")?.let { fixUrl(it) }
        val year = select(".data span, .year, span.date").first()?.text()?.let { extractYear(it) }
        val rating = select(".rating").first()?.text()?.toDoubleOrNull()

        Log.d("Danimados", "toSearchResponse: title=$title href=$href")
        return newMovieSearchResponse(title, href, TvType.Cartoon) {
            this.posterUrl = poster
            this.year = year
            this.score = rating?.let { Score.from10(it) }
        }
    }

    private fun extractYear(text: String): Int? {
        return Regex("""(19\d{2}|20\d{2})""").find(text)?.groupValues?.get(1)?.toIntOrNull()
    }

    private fun String?.fixUrl(): String? {
        if (this.isNullOrBlank()) return null
        return if (startsWith("//")) "https:$this" else this
    }

    private fun fixRelativeUrl(url: String): String {
        if (url.startsWith("http://") || url.startsWith("https://")) return url
        return "${BASE_URL}${if (url.startsWith("/")) "" else "/"}$url"
    }

    data class PlayerResponse(
        @JsonProperty("embed_url") val embedUrl: String? = null,
        @JsonProperty("type") val type: String? = null,
    )

}
