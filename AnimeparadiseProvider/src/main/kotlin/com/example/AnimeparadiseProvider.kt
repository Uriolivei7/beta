package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lagradost.cloudstream3.utils.StringUtils.encodeUri
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
        "accept-language" to "es-ES,es;q=0.9,en;q=0.8",
        "origin" to mainUrl,
        "referer" to "$mainUrl/",
        "sec-ch-ua" to "\"Chromium\";v=\"146\", \"Not-A.Brand\";v=\"24\", \"Brave\";v=\"146\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "empty",
        "sec-fetch-mode" to "cors",
        "sec-fetch-site" to "same-site",
        "sec-gpc" to "1",
        "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36"
    )

    companion object {
        // Default hashes (will be updated dynamically if they fail)
        private var searchActionHash = "70bc90e5d6f376d6614c3a08d7c8aca80385f082c9"
        private var watchActionHash = "600dc21e94ea824156f9863dfc1bd5118623ebe0a0"
        private var lastRefreshTime = 0L
    }

    private data class PageSession(val cookie: String, val actionHash: String)

    // Lightweight refresh: Scans only ONE chunk file to avoid timeouts
    private suspend fun refreshActionHashesIfNeeded() {
        if (System.currentTimeMillis() - lastRefreshTime < 60000) return // Cache for 1 min

        try {
            Log.d(TAG, "Refreshing hashes (scanning 1 chunk)...")
            val res = app.get(
                "$mainUrl/search?q=test",
                headers = mapOf("user-agent" to apiHeaders["user-agent"]!!)
            )
            val html = res.text

            // Find the first JS chunk URL
            val jsUrlMatch = Regex("\"(/_next/static/chunks/[^\\\"]+\\.js)\"").find(html)
            if (jsUrlMatch != null) {
                val url = jsUrlMatch.groupValues[1]
                // Fetch only that specific chunk
                val js = app.get("$mainUrl$url", headers = mapOf("referer" to "$mainUrl/")).text

                // Extract hex strings (likely hashes) - looking for 40+ chars
                val hashes = Regex("\"([a-f0-9]{40,})\"").findAll(js)
                    .map { it.groupValues[1] }
                    .distinct()
                    .toList()

                if (hashes.isNotEmpty()) {
                    searchActionHash = hashes[0]
                    // Try to find a second distinct hash for watch actions, otherwise use the first
                    watchActionHash = hashes.getOrElse(1) { hashes[0] }
                    lastRefreshTime = System.currentTimeMillis()
                    Log.d(TAG, "Hashes updated: ${searchActionHash.take(10)}... / ${watchActionHash.take(10)}...")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Refresh hashes failed: ${e.message}")
        }
    }

    private suspend fun getPageSession(path: String, isWatch: Boolean = false): PageSession {
        // Note: We do NOT refresh here to prevent timeouts on every page load
        return try {
            val res = app.get(
                "$mainUrl/$path",
                headers = mapOf(
                    "accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                    "user-agent" to apiHeaders["user-agent"]!!,
                    "referer" to "$mainUrl/"
                )
            )
            val cookie = res.headers["set-cookie"]
                ?.split(";")
                ?.firstOrNull { it.trimStart().startsWith("anp_session") }
                ?.trim() ?: ""
            PageSession(cookie, if (isWatch) watchActionHash else searchActionHash)
        } catch (e: Exception) {
            Log.e(TAG, "Error getPageSession: ${e.message}")
            PageSession("", if (isWatch) watchActionHash else searchActionHash)
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: --- INICIANDO MAIN PAGE ---")
        return try {
            val recentRes = app.get("$apiUrl/ep/recently-added?v=1", headers = apiHeaders)
            val popularRes = app.get("$apiUrl/search?sort=POPULARITY&limit=15&v=1", headers = apiHeaders)

            val recentData = parseNextJsJson<AnimeListResponse>(recentRes.text)
            val popularData = parseNextJsJson<AnimeListResponse>(popularRes.text)

            val homePages = mutableListOf<HomePageList>()
            popularData?.data?.let { list ->
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
            val searchPath = "search?q=${query.encodeUri()}&page=1"
            var session = getPageSession(searchPath)

            val searchHeaders = mapOf(
                "accept" to "text/x-component",
                "content-type" to "text/plain;charset=UTF-8",
                "next-action" to session.actionHash,
                "next-router-state-tree" to """["",{"children":["search",{"children":["__PAGE__",{},null,null]},null,null]}]""",
                "origin" to mainUrl,
                "referer" to "$mainUrl/$searchPath",
                "user-agent" to apiHeaders["user-agent"]!!,
                "cookie" to session.cookie
            )

            val body = "[\"$query\",{\"genres\":[],\"year\":null,\"season\":null,\"page\":1,\"limit\":25,\"sort\":null},\"\$undefined\"]"

            var response = app.post(
                "$mainUrl/$searchPath",
                headers = searchHeaders,
                requestBody = body.toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            )

            // If search fails, try refreshing hashes and retrying
            if (response.code !in 200..299) {
                Log.w(TAG, "Search failed (Code: ${response.code}), refreshing hashes...")
                lastRefreshTime = 0 // Force refresh
                refreshActionHashesIfNeeded()
                
                // Retry with new hash
                val newSession = getPageSession(searchPath)
                val retryHeaders = searchHeaders + ("next-action" to newSession.actionHash) + ("cookie" to newSession.cookie)
                response = app.post(
                    "$mainUrl/$searchPath",
                    headers = retryHeaders,
                    requestBody = body.toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
                )
            }

            processSearchResponse(response.text)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en search: ${e.message}")
            emptyList()
        }
    }

    private fun processSearchResponse(text: String): List<SearchResponse> {
        val cleanJson = parseNextJsJson<AnimeListResponse>(text)
        val results = cleanJson?.data?.map { it.toSearchResponse() } ?: emptyList()
        Log.d(TAG, "Logs: Encontrados ${results.size} resultados")
        return results
    }

    private inline fun <reified T> parseNextJsJson(input: String): T? {
        return try {
            val startIndex = input.indexOf("{\"success\":true")
            if (startIndex == -1) return null
            var braceCount = 0
            var endIndex = -1
            for (i in startIndex until input.length) {
                if (input[i] == '{') braceCount++
                else if (input[i] == '}') braceCount--
                if (braceCount == 0) { endIndex = i + 1; break }
            }
            mapper.readValue<T>(input.substring(startIndex, endIndex))
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

    private fun getEnglishTitle(title: String?, altTitle: AlternativeTitle?): String {
        return altTitle?.english ?: title ?: "Unknown"
    }

    private fun AnimeObject.toSearchResponse(): SearchResponse {
        val rawImage = this.image ?: this.posterImage?.large ?: this.posterImage?.original
        val rawLink = this.link ?: ""
        val cleanSlug = rawLink.trim('/').split('/').lastOrNull() ?: this.id ?: ""
        val displayTitle = getEnglishTitle(this.title, this.alternativeTitle)
        return newAnimeSearchResponse(
            displayTitle,
            "$mainUrl/anime/$cleanSlug",
            TvType.Anime
        ) {
            this.posterUrl = fixImageUrl(rawImage)
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val slug = url.substringAfterLast("/")
        if (slug.isBlank() || slug == "null") return null

        return try {
            val detailRes = app.get("$apiUrl/anime/$slug?v=1", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val data = animeData.data ?: throw Exception("Data de anime nula")
            val internalId = data._id ?: data.id ?: throw Exception("ID interno no encontrado")

            val epData: EpisodeListResponse = mapper.readValue(
                app.get("$apiUrl/anime/$internalId/episode", headers = apiHeaders).text
            )
            val simData: SimilarResponse = mapper.readValue(
                app.get("$apiUrl/anime/similar?animeId=$internalId", headers = apiHeaders).text
            )

            val episodes = epData.data?.mapNotNull { ep ->
                val uid = ep.uid ?: return@mapNotNull null
                newEpisode("$uid|$internalId") {
                    this.episode = ep.number?.toIntOrNull() ?: 0
                    this.name = ep.title ?: "Episodio ${ep.number}"
                    this.posterUrl = ep.image
                }
            }?.sortedBy { it.episode } ?: emptyList()

            val recommendations = simData.data?.map { sim ->
                newAnimeSearchResponse(getEnglishTitle(sim.title, sim.alternativeTitle), "$mainUrl/anime/${sim.link}") {
                    this.posterUrl = sim.posterImage?.large
                }
            }

            newAnimeLoadResponse(getEnglishTitle(data.title, data.alternativeTitle), url, TvType.Anime) {
                this.posterUrl = data.posterImage?.original ?: data.posterImage?.large
                this.plot = data.synopsis ?: data.synopsys
                this.tags = if (data.tags?.isNotEmpty() == true) data.tags else data.genres
                this.year = data.animeSeason?.year
                this.recommendations = recommendations
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
        val uid = parts.getOrNull(0)?.substringAfterLast("/") ?: return false
        val origin = parts.getOrNull(1)?.substringAfterLast("/") ?: return false

        return try {
            val watchPath = "watch/$uid?origin=$origin"
            var session = getPageSession(watchPath, isWatch = true)

            val routerStateTree = """["",{"children":["watch",{"children":[["id","$uid","d"],{"children":["__PAGE__",{},null,null]}]},null,null]}]"""

            val actionHeaders = mapOf(
                "accept" to "text/x-component",
                "content-type" to "text/plain;charset=UTF-8",
                "next-action" to session.actionHash,
                "next-router-state-tree" to routerStateTree,
                "origin" to mainUrl,
                "referer" to "$mainUrl/$watchPath",
                "user-agent" to apiHeaders["user-agent"]!!,
                "cookie" to session.cookie
            )

            var resText = app.post(
                "$mainUrl/$watchPath",
                headers = actionHeaders,
                requestBody = "[\"$uid\",\"$origin\"]"
                    .toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            ).text.replace("\\/", "/")

            // If streamLink not found, try refreshing hashes and retrying
            if (!resText.contains("streamLink")) {
                Log.w(TAG, "loadLinks failed (no streamLink), refreshing hashes...")
                lastRefreshTime = 0
                refreshActionHashesIfNeeded()
                
                val newSession = getPageSession(watchPath, isWatch = true)
                val retryHeaders = actionHeaders + ("next-action" to newSession.actionHash) + ("cookie" to newSession.cookie)
                resText = app.post(
                    "$mainUrl/$watchPath",
                    headers = retryHeaders,
                    requestBody = "[\"$uid\",\"$origin\"]"
                        .toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
                ).text.replace("\\/", "/")
            }

            val streamLink = Regex(""""streamLink"\s*:\s*"([^"]+)""")
                .find(resText)?.groupValues?.getOrNull(1)

            if (streamLink != null) {
                val proxyUrl = "https://stream.animeparadise.moe/m3u8?url=${streamLink.encodeUri()}"

                callback.invoke(
                    newExtractorLink(
                        this.name, "AnimeParadise",
                        proxyUrl,
                        ExtractorLinkType.M3U8
                    ) {
                        this.referer = "$mainUrl/"
                        this.quality = Qualities.Unknown.value
                        this.headers = apiHeaders
                    }
                )
            }

            val subDataJson = Regex(""""subData"\s*:\s*(\[.*?])""", RegexOption.DOT_MATCHES_ALL)
                .find(resText)?.groupValues?.getOrNull(1)

            if (subDataJson != null) {
                val subList = mapper.readValue<List<SubData>>(subDataJson)
                subList.forEach { sub ->
                    val label = sub.label ?: "Unknown"
                    val src = sub.src ?: return@forEach
                    val type = sub.type ?: "vtt"
                    try {
                        when (type.lowercase()) {
                            "vtt" -> subtitleCallback.invoke(newSubtitleFile(label, src))
                            "ass" -> subtitleCallback.invoke(newSubtitleFile(label, "$apiUrl/stream/file/$src"))
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Logs: Error sub $label: ${e.message}")
                    }
                }
            }

            streamLink != null
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
    val image: String? = null,
    val alternativeTitle: AlternativeTitle? = null
)
data class ImageInfo(val original: String? = null, val large: String? = null)
data class AlternativeTitle(val english: String? = null, val native: String? = null, val romaji: String? = null)
data class AnimeListResponse(val data: List<AnimeObject>? = null)
data class AnimeDetailResponse(val success: Boolean?, val data: AnimeData?)
data class AnimeData(
    val _id: String?,
    @JsonProperty("id") val id: String?,
    val title: String?,
    val link: String?,
    val synopsis: String?,
    val synopsys: String?,
    val genres: List<String>?,
    val tags: List<String>?,
    val posterImage: PosterImages?,
    val animeSeason: SeasonInfo?,
    val alternativeTitle: AlternativeTitle? = null
)
data class SeasonInfo(val season: String?, val year: Int?)
data class PosterImages(val large: String?, val original: String?)
data class SimilarResponse(val success: Boolean?, val data: List<SimilarAnime>?)
data class SimilarAnime(val title: String?, val link: String?, val posterImage: PosterImages?, val alternativeTitle: AlternativeTitle? = null)
data class EpisodeListResponse(val success: Boolean?, val data: List<EpisodeData>?)
data class EpisodeData(
    @JsonProperty("_id") val id: String?,
    val uid: String?,
    val title: String?,
    val number: String?,
    val image: String?
)
data class SubData(val src: String?, val label: String?, val type: String?)
