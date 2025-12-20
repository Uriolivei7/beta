package com.example

import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder

class AnimeonsenProvider : MainAPI() {
    override var mainUrl = "https://animeonsen.xyz"
    override var name = "AnimeOnsen"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val apiUrl = "https://api.animeonsen.xyz/v4"
    private var accessToken: String? = null
    private val userAgent = "Aniyomi/App (mobile)"

    private suspend fun getAuthToken(): String? {
        if (accessToken != null) return accessToken
        return try {
            val bodyString = """{"client_id":"f296be26-28b5-4358-b5a1-6259575e23b7","client_secret":"349038c4157d0480784753841217270c3c5b35f4281eaee029de21cb04084235","grant_type":"client_credentials"}"""
            val responseText = app.post(
                "https://auth.animeonsen.xyz/oauth/token",
                headers = mapOf("user-agent" to userAgent),
                requestBody = bodyString.toRequestBody("application/json".toMediaType())
            ).text
            val json = AppUtils.parseJson<Map<String, String>>(responseText)
            accessToken = json["access_token"]
            accessToken
        } catch (e: Exception) {
            Log.e("AnimeOnsen", "Error obteniendo Token: ${e.message}")
            null
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val token = getAuthToken()
        val res = app.get(
            "$apiUrl/content/index?start=${(page - 1) * 20}&limit=20",
            headers = mapOf("Authorization" to "Bearer $token")
        ).parsed<AnimeListResponse>()

        val animeList = res.content.map {
            newAnimeSearchResponse(it.content_title ?: it.content_title_en ?: "Anime", it.content_id) {
                this.posterUrl = "$apiUrl/image/210x300/${it.content_id}"
            }
        }
        return newHomePageResponse(listOf(HomePageList("Latest Anime", animeList)), true)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val TAG = "AnimeOnsenSearch"
        val token = getAuthToken() ?: return emptyList()

        val encodedQuery = URLEncoder.encode(query, "UTF-8")

        return try {
            val response = app.get(
                "$apiUrl/search/$encodedQuery",
                headers = mapOf("Authorization" to "Bearer $token")
            )

            Log.d(TAG, "Búsqueda: $query | Código: ${response.code}")

            if (response.code == 404) {
                Log.d(TAG, "No se encontraron resultados para: $query")
                return emptyList()
            }

            val res = AppUtils.parseJson<SearchResponseDto>(response.text)

            res.result?.map {
                val title = it.content_title ?: it.content_title_en ?: "Unknown Anime"
                newAnimeSearchResponse(title, it.content_id) {
                    this.posterUrl = "$apiUrl/image/210x300/${it.content_id}"
                }
            } ?: emptyList()

        } catch (e: Exception) {
            Log.e(TAG, "Error en búsqueda: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val token = getAuthToken()
        val contentId = if (url.startsWith("http")) url.split("/").last() else url

        val details = app.get(
            "$apiUrl/content/$contentId/extensive",
            headers = mapOf("Authorization" to "Bearer $token")
        ).parsed<AnimeDetailsDto>()

        val epResponseText = app.get(
            "$apiUrl/content/$contentId/episodes",
            headers = mapOf("Authorization" to "Bearer $token")
        ).text

        val epRes = AppUtils.parseJson<Map<String, EpisodeDto>>(epResponseText)

        val episodesList = epRes.map { (epNum, item) ->
            newEpisode("$contentId/video/$epNum") {
                this.name = item.name ?: "Episode $epNum"
                this.episode = epNum.toIntOrNull()
            }
        }.sortedBy { it.episode }

        return newAnimeLoadResponse(
            details.content_title ?: details.content_title_en ?: "Anime",
            url,
            TvType.Anime
        ) {
            this.posterUrl = "$apiUrl/image/210x300/$contentId"
            this.plot = details.mal_data?.synopsis
            this.showStatus = if (details.mal_data?.status == "finished_airing") ShowStatus.Completed else ShowStatus.Ongoing
            addEpisodes(DubStatus.Subbed, episodesList)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val TAG = "AnimeOnsen"
        val cleanData = if (data.contains("animeonsen.xyz/")) data.substringAfter("animeonsen.xyz/") else data
        val token = getAuthToken() ?: return false

        return try {
            val response = app.get(
                "$apiUrl/content/$cleanData",
                headers = mapOf(
                    "Authorization" to "Bearer $token",
                    "Referer" to mainUrl,
                    "User-Agent" to userAgent
                )
            )

            val res = AppUtils.parseJson<VideoDataDto>(response.text)
            val videoUrl = res.uri.stream

            if (videoUrl.isNotEmpty()) {
                res.uri.subtitles.forEach { (langPrefix, subUrl) ->
                    val langName = res.metadata.subtitles[langPrefix] ?: langPrefix
                    subtitleCallback(SubtitleFile(langName, subUrl))
                }

                callback(
                    newExtractorLink(this.name, "AnimeOnsen", videoUrl) {
                        this.referer = mainUrl
                        this.quality = Qualities.P720.value
                    }
                )
                true
            } else false
        } catch (e: Exception) {
            Log.e(TAG, "Error en loadLinks: ${e.message}")
            false
        }
    }

    @Serializable data class AnimeListResponse(val content: List<AnimeListItem>)
    @Serializable
    data class SearchResponseDto(
        val result: List<AnimeListItem>? = null
    )
    @Serializable data class AnimeListItem(
        val content_id: String,
        val content_title: String? = null,
        val content_title_en: String? = null
    )
    @Serializable data class EpisodeDto(
        @JsonProperty("contentTitle_episode_en") @SerialName("contentTitle_episode_en")
        val name: String? = null
    )
    @Serializable data class VideoDataDto(val metadata: MetaDataDto, val uri: StreamDataDto)
    @Serializable data class MetaDataDto(val subtitles: Map<String, String>)
    @Serializable data class StreamDataDto(val stream: String, val subtitles: Map<String, String>)
    @Serializable data class AnimeDetailsDto(
        val content_id: String,
        val content_title: String?,
        val content_title_en: String?,
        val mal_data: MalDataDto? = null
    )
    @Serializable data class MalDataDto(val synopsis: String? = null, val status: String? = null)
}