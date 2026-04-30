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
    override var name = "PlayHub"
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
        val splitIndex = data.indexOf(':')
        val type = if (splitIndex > 0) data.substring(0, splitIndex) else "content"
        val uuid = if (splitIndex > 0) data.substring(splitIndex + 1) else data
        val url = "$apiUrl/$type/$uuid/sources"
        Log.d("PlayHub", "Fetching sources: $url")
        val sourceRes = app.get(url, headers = headers)
        Log.d("PlayHub", "Response status: ${sourceRes.code}, body: ${sourceRes.text.take(100)}")
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
                callback.invoke(
                    newExtractorLink(
                        source.hostName ?: "PlayHub",
                        source.hostName ?: "PlayHub",
                        source.url,
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.referer = mainUrl
                    }
                )
            }
        }

        return true
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
