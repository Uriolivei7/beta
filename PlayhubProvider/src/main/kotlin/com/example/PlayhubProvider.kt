package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink

class PlayhubProvider : MainAPI() {
    override var mainUrl = "https://www.playhubmax.com"
    override var name = "PlayHUB"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.Cartoon)
    override var lang = "mx"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true

    private val apiUrl = "https://api.playhubmax.com/api"

    private val headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36",
        "Accept" to "application/json, text/plain, */*",
        "Accept-Language" to "es",
        "Origin" to mainUrl,
        "Referer" to "$mainUrl/",
        "sec-ch-ua" to "\"Brave\";v=\"147\", \"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"147\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "empty",
        "sec-fetch-mode" to "cors",
        "sec-fetch-site" to "same-site",
    )

    private fun getImageUrl(artwork: Artwork?): String {
        return artwork?.poster?.medium?.takeIf { it.isNotBlank() }
            ?: artwork?.backdrop?.medium?.takeIf { it.isNotBlank() }
            ?: artwork?.poster?.small.orEmpty()
    }

    private fun getBackdropUrl(artwork: Artwork?): String {
        return artwork?.backdrop?.large?.takeIf { it.isNotBlank() }
            ?: artwork?.backdrop?.medium?.takeIf { it.isNotBlank() }
            ?: artwork?.poster?.medium.orEmpty()
    }

    data class ApiContent(
        val id: Int,
        val uuid: String,
        val title: String,
        val overview: String?,
        val type: String?,
        val releaseDate: String?,
        val runtime: Int?,
        val certification: String?,
        val seasonCount: Int?,
        val episodeCount: Int?,
        val languages: List<String>?,
        val artwork: Artwork?,
        val genres: List<GenreItem>?,
        val people: List<PeopleItem>?,
        val seasons: List<SeasonItem>?,
    )

    data class Artwork(
        val poster: ImageSizes?,
        val backdrop: ImageSizes?,
        val logo: ImageSizes?,
    )

    data class ImageSizes(
        val small: String?,
        val medium: String?,
        val large: String?,
    )

    data class GenreItem(
        val id: Int,
        val name: String,
    )

    data class PeopleItem(
        val id: Int,
        val name: String,
    )

    data class SeasonItem(
        val id: Int,
        @JsonProperty("seasonNumber") val seasonNumber: Int,
    )

    data class ContentListResponse(
        val data: List<ApiContent>,
        val currentPage: Int,
        val hasMore: Boolean,
    )

    data class ContentDetailResponse(
        val id: Int,
        val uuid: String,
        val title: String,
        val overview: String?,
        val type: String?,
        val releaseDate: String?,
        val runtime: Int?,
        val certification: String?,
        val seasonCount: Int?,
        val episodeCount: Int?,
        val languages: List<String>?,
        val artwork: Artwork?,
        val genres: List<GenreItem>?,
        val people: List<PeopleItem>?,
        val seasons: List<SeasonItem>?,
    )

    data class SectionItem(
        val id: Int,
        val name: String,
        @JsonProperty("componentType") val componentType: String,
        val path: String,
        val showAll: Boolean,
    )

    data class SectionsResponse(
        val data: List<SectionItem>,
    )

    data class EpisodeItem(
        val id: Int,
        val uuid: String,
        @JsonProperty("seasonId") val seasonId: Int,
        @JsonProperty("contentId") val contentId: Int,
        @JsonProperty("episodeNumber") val episodeNumber: Int,
        @JsonProperty("seasonNumber") val seasonNumber: Int,
        val name: String,
        val overview: String?,
        val runtime: Int?,
        val artwork: EpisodeArtwork?,
        @JsonProperty("contentData") val contentData: EpisodeContentData?,
    )

    data class EpisodeArtwork(
        val backdrop: ImageSizes?,
    )

    data class EpisodeContentData(
        val id: Int,
        val uuid: String,
        val title: String,
    )

    data class EpisodesResponse(
        val data: List<EpisodeItem>,
        val currentPage: Int,
        val hasMore: Boolean,
    )

    data class SourceResponse(
        val data: String,
    )

    data class DecryptedSource(
        val url: String,
        @JsonProperty("hostName") val hostName: String?,
        val languages: List<String>?,
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val sectionsRes = app.get(
            "$apiUrl/es/content-sections",
            headers = headers
        ).parsed<SectionsResponse>()

        val homePageLists = sectionsRes.data.mapNotNull { section ->
            val items = try {
                val contentsUrl = "$apiUrl${section.path}&page=$page"
                val res = app.get(contentsUrl, headers = headers)
                val parsed = tryParseJson<ContentListResponse>(res.text)
                parsed?.data?.map { content ->
                    newAnimeSearchResponse(content.title, "$mainUrl/content/${content.uuid}") {
                        this.type = when (content.type) {
                            "Movie" -> TvType.Movie
                            "Show" -> TvType.TvSeries
                            else -> TvType.Others
                        }
                        this.posterUrl = getImageUrl(content.artwork)
                    }
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("PlayHub", "Error loading section ${section.name}: ${e.message}")
                emptyList()
            }

            if (items.isNotEmpty()) {
                HomePageList(section.name, items)
            } else null
        }

        return newHomePageResponse(homePageLists)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val res = app.get(
            "$apiUrl/ES/es/contents?q=$query",
            headers = headers
        )
        val parsed = tryParseJson<ContentListResponse>(res.text)
        return parsed?.data?.map { content ->
            newAnimeSearchResponse(content.title, "$mainUrl/content/${content.uuid}") {
                this.type = when (content.type) {
                    "Movie" -> TvType.Movie
                    "Show" -> TvType.TvSeries
                    else -> TvType.Others
                }
                this.posterUrl = getImageUrl(content.artwork)
            }
        } ?: emptyList()
    }

    override suspend fun load(url: String): LoadResponse? {
        val uuid = url.substringAfterLast("/")
        val detailRes = app.get(
            "$apiUrl/es/contents/$uuid",
            headers = headers
        )
        val content = tryParseJson<ContentDetailResponse>(detailRes.text) ?: return null

        val tvType = if (content.type == "Movie") TvType.Movie else TvType.TvSeries
        val posterUrl = getImageUrl(content.artwork)
        val backdropUrl = getBackdropUrl(content.artwork)
        val year = content.releaseDate?.substringBefore("-")?.toIntOrNull()

        if (tvType == TvType.Movie) {
            return newMovieLoadResponse(content.title, url, tvType, "content:$uuid") {
                this.posterUrl = posterUrl
                this.backgroundPosterUrl = backdropUrl
                this.plot = content.overview
                this.year = year
                this.tags = content.genres?.map { it.name }
                this.duration = content.runtime
            }
        }

        val episodes = mutableListOf<Episode>()
        val seasons = content.seasons ?: emptyList()

        for (season in seasons.sortedBy { it.seasonNumber }) {
            var page = 1
            var hasMore = true
            while (hasMore) {
                val epRes = app.get(
                    "$apiUrl/es/episodes?season_id=${season.id}&page=$page",
                    headers = headers
                )
                val epParsed = tryParseJson<EpisodesResponse>(epRes.text)
                if (epParsed != null) {
                    epParsed.data.forEach { ep ->
                        episodes.add(
                            newEpisode("episode:${ep.uuid}") {
                                this.name = ep.name
                                this.season = ep.seasonNumber
                                this.episode = ep.episodeNumber
                                this.posterUrl = ep.artwork?.backdrop?.medium
                                this.description = ep.overview
                            }
                        )
                    }
                    hasMore = epParsed.hasMore
                    page++
                } else {
                    hasMore = false
                }
            }
        }

        return newTvSeriesLoadResponse(content.title, url, tvType, episodes) {
            this.posterUrl = posterUrl
            this.backgroundPosterUrl = backdropUrl
            this.plot = content.overview
            this.year = year
            this.tags = content.genres?.map { it.name }
            this.duration = content.runtime
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val splitIndex = data.lastIndexOf(':')
        val type = if (splitIndex > 0 && data.substringBeforeLast(':').contains("/episode")) "episode" else "content"
        val uuid = data.substring(splitIndex + 1)
        val url = "$apiUrl/$type/$uuid/sources"
        Log.d("PlayHub", "loadLinks data: $data -> type=$type uuid=$uuid url=$url")
        val sourceRes = app.get(url, headers = headers)
        Log.d("PlayHub", "Response status: ${sourceRes.code}")
        val sourceParsed = tryParseJson<SourceResponse>(sourceRes.text)
        if (sourceParsed == null || sourceParsed.data.isEmpty()) {
            Log.e("PlayHub", "Failed to parse source response or empty data")
            return false
        }

        val decrypted = decryptData(sourceParsed.data)
        Log.d("PlayHub", "Decrypted: $decrypted")
        val sources = tryParseJson<List<DecryptedSource>>(decrypted)
        if (sources.isNullOrEmpty()) {
            Log.e("PlayHub", "Failed to parse decrypted sources or empty list")
            return false
        }

        sources.forEach { source ->
            if (source.url.isNotBlank()) {
                Log.d("PlayHub", "Found link: ${source.hostName} -> ${source.url}")
                resolveEmbed(source.url, source.hostName, subtitleCallback, callback)
            }
        }

        return true
    }

    private suspend fun resolveEmbed(
        url: String,
        hostName: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        try {
            val res = app.get(url, headers = headers)
            val body = res.text

            val evalPattern = Regex("""eval\(function\(p,a,c,k,e,d\)\{(.*)\}\)""", RegexOption.DOT_MATCHES_ALL)
            val evalMatch = evalPattern.find(body)
            if (evalMatch != null) {
                try {
                    val evalBody = evalMatch.groupValues[1]
                    val pMatch = Regex("""p=["'](.+?)["']""").find(evalBody)
                    val aMatch = Regex("""a=(\d+)""").find(evalBody)
                    val cMatch = Regex("""c=(\d+)""").find(evalBody)
                    val kMatch = Regex("""k=\[(.+?)\]""").find(evalBody)

                    if (pMatch != null && aMatch != null && cMatch != null && kMatch != null) {
                        val p = pMatch.groupValues[1].replace("\\'", "'").replace("\\\\", "\\")
                        val a = aMatch.groupValues[1].toInt()
                        val c = cMatch.groupValues[1].toInt()
                        val kList = kMatch.groupValues[1].split(",").map {
                            it.trim().removeSurrounding("'").removeSurrounding("\"")
                        }
                        val decoded = decodeEval(p, a, c, kList)
                        Log.d("PlayHub", "Eval decoded (first 2000): ${decoded.take(2000)}")

                        val streamRegex = Regex("""(https?://[^"'\s]+/(?:stream|hls|playlist|master|index)[^"'\s]+\.(?:m3u8|txt))""")
                        val streamUrl = streamRegex.find(decoded)?.groupValues?.getOrNull(1)
                        if (streamUrl != null) {
                            Log.d("PlayHub", "Found stream in eval: $streamUrl")
                            callback.invoke(
                                newExtractorLink(
                                    hostName ?: "PlayHub",
                                    hostName ?: "PlayHub",
                                    streamUrl,
                                    type = ExtractorLinkType.M3U8
                                ) {
                                    this.referer = url
                                }
                            )
                            return
                        }

                        val fileRegex = Regex("""file["\s]*[:=]["\s]*(https?://[^"'\s]+)""")
                        val fileMatch = fileRegex.find(decoded)?.groupValues?.getOrNull(1)
                        if (fileMatch != null) {
                            Log.d("PlayHub", "Found file in eval: $fileMatch")
                            callback.invoke(
                                newExtractorLink(
                                    hostName ?: "PlayHub",
                                    hostName ?: "PlayHub",
                                    fileMatch,
                                    type = ExtractorLinkType.M3U8
                                ) {
                                    this.referer = url
                                }
                            )
                            return
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PlayHub", "Eval decode failed: ${e.message}")
                }
            }

            val streamRegex = Regex("""(https?://[^"'\s]+/(?:stream|hls|playlist|master|index)[^"'\s]+\.(?:m3u8|txt))""")
            val streamUrl = streamRegex.find(body)?.groupValues?.getOrNull(1)
            if (streamUrl != null) {
                Log.d("PlayHub", "Found stream directly in body: $streamUrl")
                callback.invoke(
                    newExtractorLink(
                        hostName ?: "PlayHub",
                        hostName ?: "PlayHub",
                        streamUrl,
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.referer = url
                    }
                )
                return
            }

            Log.e("PlayHub", "Could not find video URL in $url")
        } catch (e: Exception) {
            Log.e("PlayHub", "resolveEmbed failed: ${e.message}")
        }
    }

    private fun decodeEval(p: String, a: Int, c: Int, k: List<String>): String {
        var decoded = p
        if (c == 0) return decoded
        for (i in (0 until c).reversed()) {
            if (k[i].isBlank()) continue
            val baseStr = i.toString(a)
            if (baseStr.length == 1) {
                val regex = Regex("\\b$baseStr\\b")
                decoded = regex.replace(decoded, k[i])
            } else {
                decoded = decoded.replace(baseStr, k[i])
            }
        }
        return decoded
    }

    private val sha256Hash = sha256("Dx5VYERoLOVevR9C")
    private val aesKey = sha256Hash.substring(0, 32)
    private val aesIv = sha256Hash.substring(0, 16)

    private fun sha256(input: String): String {
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun decryptData(encrypted: String): String {
        try {
            val keySpec = javax.crypto.spec.SecretKeySpec(aesKey.toByteArray(Charsets.UTF_8), "AES")
            val ivSpec = javax.crypto.spec.IvParameterSpec(aesIv.toByteArray(Charsets.UTF_8))
            val cipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decoded = android.util.Base64.decode(encrypted, android.util.Base64.DEFAULT)
            val decrypted = cipher.doFinal(decoded)
            return String(decrypted, Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e("PlayHub", "Decryption failed: ${e.message}")
            return "[]"
        }
    }
}
