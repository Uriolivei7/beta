package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AnimeParadiseProvider : MainAPI() {
    override var mainUrl = "https://www.animeparadise.moe"
    private val apiUrl = "https://api.animeparadise.moe"
    override var name = "AnimeParadise"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val TAG = "AnimeParadise"

    private val mapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val apiHeaders = mapOf(
        "accept" to "*/*",
        "origin" to mainUrl,
        "referer" to "$mainUrl/",
        "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: --- INICIANDO MAIN PAGE ---")
        return try {
            val recentRes = app.get("$apiUrl/ep/recently-added?v=1", headers = apiHeaders)
            val popularRes = app.get("$apiUrl/search?sort=POPULARITY&limit=15&v=1", headers = apiHeaders)

            Log.d(TAG, "Logs: API Status - Recent: ${recentRes.code}, Popular: ${popularRes.code}")

            val recentData = parseNextJsJson<AnimeListResponse>(recentRes.text)
            val popularData = parseNextJsJson<AnimeListResponse>(popularRes.text)

            val homePages = mutableListOf<HomePageList>()
            recentData?.data?.let { list ->
                Log.d(TAG, "Logs: Agregando ${list.size} items a Recientes")
                homePages.add(HomePageList("Recién Agregados", list.map { it.toSearchResponse() }))
            }
            popularData?.data?.let { list ->
                Log.d(TAG, "Logs: Agregando ${list.size} items a Populares")
                homePages.add(HomePageList("Populares", list.map { it.toSearchResponse() }))
            }

            newHomePageResponse(homePages, false)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: --- BUSCANDO: $query ---")
        return try {
            val searchHeaders = mapOf(
                "accept" to "text/x-component",
                "next-action" to "70e67558d075165016c3b05cb0bba485a351870862",
                "content-type" to "text/plain;charset=UTF-8",
                "origin" to mainUrl,
                "referer" to "$mainUrl/search"
            )

            val requestBody = "[\"$query\",{\"genres\":[],\"year\":null,\"season\":null,\"page\":1,\"limit\":25,\"sort\":null},\"\$undefined\"]"

            val response = app.post(
                "$mainUrl/search",
                headers = searchHeaders,
                requestBody = requestBody.toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            )

            Log.d(TAG, "Logs: Search Status: ${response.code}")
            val cleanJson = parseNextJsJson<AnimeListResponse>(response.text)
            val results = cleanJson?.data?.map { it.toSearchResponse() } ?: emptyList()
            Log.d(TAG, "Logs: Encontrados ${results.size} resultados")
            results
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en search: ${e.message}")
            emptyList()
        }
    }

    private inline fun <reified T> parseNextJsJson(input: String): T? {
        return try {
            val startIndex = input.indexOf("{\"success\":true")
            if (startIndex == -1) {
                Log.e(TAG, "Logs: No se encontró JSON de éxito en la respuesta")
                return null
            }

            var braceCount = 0
            var endIndex = -1
            for (i in startIndex until input.length) {
                if (input[i] == '{') braceCount++
                else if (input[i] == '}') braceCount--
                if (braceCount == 0) {
                    endIndex = i + 1
                    break
                }
            }
            val json = input.substring(startIndex, endIndex)
            mapper.readValue<T>(json)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error parseando JSON: ${e.message}")
            null
        }
    }

    private fun fixImageUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null
        if (url.startsWith("http")) return url
        if (url.startsWith("//")) return "https:$url"
        return if (url.startsWith("/")) "$mainUrl$url" else "$mainUrl/$url"
    }

    private fun AnimeObject.toSearchResponse(): SearchResponse {
        val rawImage = this.image ?: this.posterImage?.large ?: this.posterImage?.original
        val rawLink = this.link ?: ""
        val cleanSlug = rawLink.trim('/').split('/').lastOrNull() ?: this.id ?: ""

        return newAnimeSearchResponse(
            this.title ?: "Sin título",
            "$mainUrl/anime/$cleanSlug",
            TvType.Anime
        ) {
            this.posterUrl = fixImageUrl(rawImage)
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.substringAfterLast("/")
        Log.d(TAG, "Logs: --- CARGANDO DETALLES: $slug ---")
        if (slug.isBlank() || slug == "null") return null

        return try {
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val internalId = animeData.data?.id ?: throw Exception("ID no encontrado")

            val epResponse = app.get("$apiUrl/anime/$internalId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            val episodes = epData.data?.map { ep ->
                val rawId = ep.id ?: ""
                val cleanEpId = rawId.substringAfterLast("/")

                newEpisode("$cleanEpId|$internalId") {
                    this.name = ep.title ?: "Episodio ${ep.number}"
                    this.episode = ep.number?.toIntOrNull() ?: 0
                    this.posterUrl = fixImageUrl(ep.image)
                }
            }?.sortedBy { it.episode } ?: emptyList()

            Log.d(TAG, "Logs: ${episodes.size} episodios cargados")

            newAnimeLoadResponse(animeData.data?.title ?: "Anime", url, TvType.Anime) {
                this.posterUrl = fixImageUrl(animeData.data?.posterImage?.large)
                this.plot = animeData.data?.synopsis
                this.tags = animeData.data?.genres
                addEpisodes(DubStatus.Subbed, episodes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en load: ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val parts = data.split("|")
        val rawEpId = parts.getOrNull(0) ?: return false
        val originId = parts.getOrNull(1) ?: ""

        val epUuid = rawEpId.substringAfterLast("/")
        val watchUrl = "$mainUrl/watch/$epUuid?origin=$originId"

        return try {
            val pageReq = app.get(watchUrl, headers = apiHeaders)
            val cookieStr = pageReq.cookies.entries.joinToString("; ") { "${it.key}=${it.value}" }

            val actionHeaders = mapOf(
                "accept" to "text/x-component",
                "next-action" to "603712faba47e30723d32819533284371173c10bbd",
                "content-type" to "text/plain;charset=UTF-8",
                "cookie" to cookieStr,
                "referer" to watchUrl,
                "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            )

            val response = app.post(
                watchUrl,
                headers = actionHeaders,
                requestBody = "[\"$epUuid\",\"$originId\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            )

            val resText = response.text

            val subRegex = Regex("""\{"src":"([^"]+)","label":"([^"]+)","type":"([^"]+)"\}""")
            val subMap = mutableMapOf<String, String>()

            subRegex.findAll(resText).forEach { match ->
                val (src, label, _) = match.destructured
                if (src.isNotBlank() && !subMap.containsKey(label)) {
                    val subUrl = if (!src.startsWith("http")) {
                        "https://docs.google.com/uc?export=download&id=$src"
                    } else {
                        src
                    }
                    subMap[label] = subUrl
                }
            }

            subMap.forEach { (label, url) ->
                Log.d(TAG, "Logs: Subtítulo único encontrado: $label")
                subtitleCallback.invoke(newSubtitleFile(label, url))
            }

            val cleanResponse = resText.replace("\\u002F", "/").replace("\\/", "/").replace("\\\"", "\"").replace("\\", "")
            val videoRegex = Regex("""https?://[^\s"']+\.m3u8[^\s"']*""")

            val links = videoRegex.findAll(cleanResponse)
                .map { it.value }
                .distinctBy { it.substringBefore(".m3u8") }
                .toList()

            links.forEachIndexed { index, rawUrl ->
                val finalUrl = if (rawUrl.contains("stream.animeparadise.moe")) rawUrl
                else "https://stream.animeparadise.moe/m3u8?url=${rawUrl.replace("/", "%2F").replace(":", "%3A")}"

                val linkName = when {
                    rawUrl.contains("windflash") -> "Paradise Wind"
                    rawUrl.contains("stream") -> "Paradise Stream"
                    else -> "Mirror ${index + 1}"
                }

                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = linkName,
                        url = finalUrl,
                        type = ExtractorLinkType.M3U8
                    ).apply {
                        this.quality = Qualities.P1080.value
                        this.referer = watchUrl
                        this.headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
                            "Origin" to "https://www.animeparadise.moe",
                            "Connection" to "keep-alive"
                        )
                    }
                )
            }

            links.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en loadLinks: ${e.message}")
            false
        }
    }

}

data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null,
    val link: String? = null,
    val status: String? = null,
    @JsonAlias("synopsys", "synopsis") val synopsis: String? = null,
    val genres: List<String>? = null,
    val posterImage: ImageInfo? = null,
    val image: String? = null
)
data class ImageInfo(val original: String? = null, val large: String? = null)
data class AnimeDetailResponse(val data: AnimeObject? = null)
data class EpisodeListResponse(val data: List<Episode>? = null)
data class Episode(
    @JsonProperty("_id") val id: String? = null,
    val number: String? = null,
    val title: String? = null,
    val image: String? = null
)
data class AnimeListResponse(val data: List<AnimeObject>? = null)