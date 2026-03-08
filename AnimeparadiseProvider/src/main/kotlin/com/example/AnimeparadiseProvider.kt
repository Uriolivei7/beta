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
        "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: --- INICIANDO MAIN PAGE ---")
        return try {
            val recentRes = app.get("$apiUrl/ep/recently-added?v=1", headers = apiHeaders)
            Log.d(TAG, "Logs: Recent API Status: ${recentRes.code}")

            val popularRes = app.get("$apiUrl/search?sort=POPULARITY&limit=15&v=1", headers = apiHeaders)
            Log.d(TAG, "Logs: Popular API Status: ${popularRes.code}")

            val recentData = parseNextJsJson<AnimeListResponse>(recentRes.text)
            val popularData = parseNextJsJson<AnimeListResponse>(popularRes.text)

            Log.d(TAG, "Logs: Recent items: ${recentData?.data?.size ?: "null"}")
            Log.d(TAG, "Logs: Popular items: ${popularData?.data?.size ?: "null"}")

            val homePages = mutableListOf<HomePageList>()
            recentData?.data?.let { list ->
                homePages.add(HomePageList("Recién Agregados", list.map { it.toSearchResponse() }))
            }
            popularData?.data?.let { list ->
                homePages.add(HomePageList("Populares", list.map { it.toSearchResponse() }))
            }

            newHomePageResponse(homePages, false)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error crítico en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: --- INICIANDO BÚSQUEDA: $query ---")
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

            Log.d(TAG, "Logs: Search Response Code: ${response.code}")
            if (response.text.isBlank()) Log.e(TAG, "Logs: La respuesta de búsqueda está VACÍA")

            val cleanJson = parseNextJsJson<AnimeListResponse>(response.text)
            val results = cleanJson?.data?.map { it.toSearchResponse() } ?: emptyList()

            Log.d(TAG, "Logs: Resultados encontrados: ${results.size}")
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
                Log.e(TAG, "Logs: No se encontró el inicio del JSON (success:true)")
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
            Log.d(TAG, "Logs: JSON extraído con éxito")
            mapper.readValue<T>(json)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error de parseo: ${e.message}")
            null
        }
    }

    private fun fixImageUrl(url: String?): String? {
        if (url == null) return null
        if (url.startsWith("http")) return url

        if (url.startsWith("//")) return "https:$url"

        return if (url.startsWith("/")) "$mainUrl$url" else "$mainUrl/$url"
    }

    private fun AnimeObject.toSearchResponse(): SearchResponse {
        val rawImage = this.image ?: this.posterImage?.large ?: this.posterImage?.original
        val cleanSlug = this.link?.substringBefore("/") ?: ""

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
        if (slug == "null" || slug.isBlank()) {
            Log.e(TAG, "Logs: Abortando load, slug es inválido")
            return null
        }
        Log.d(TAG, "Logs: Iniciando load para slug: $slug")

        return try {
            val detailRes = app.get("$apiUrl/anime/$slug", headers = apiHeaders).text
            val animeData: AnimeDetailResponse = mapper.readValue(detailRes)
            val internalAnimeId = animeData.data?.id ?: throw Exception("ID de anime no encontrado")

            val epResponse = app.get("$apiUrl/anime/$internalAnimeId/episode", headers = apiHeaders).text
            val epData: EpisodeListResponse = mapper.readValue(epResponse)

            val episodes = epData.data?.map { ep ->
                val epUuid = ep.id ?: ""
                newEpisode("$epUuid|$internalAnimeId") {
                    this.name = ep.title ?: "Episodio ${ep.number}"
                    this.episode = ep.number?.toIntOrNull() ?: 0
                    this.posterUrl = fixImageUrl(ep.image)
                }
            }?.sortedBy { it.episode } ?: emptyList()

            Log.d(TAG, "Logs: Se cargaron ${episodes.size} episodios para $slug")

            newAnimeLoadResponse(animeData.data?.title ?: "Anime", url, TvType.Anime) {
                this.posterUrl = fixImageUrl(animeData.data?.posterImage?.large ?: animeData.data?.posterImage?.original)
                this.plot = animeData.data?.synopsis
                this.tags = animeData.data?.genres
                addEpisodes(DubStatus.Subbed, episodes)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en load para $slug: ${e.message}")
            e.printStackTrace()
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
        val epUuid = parts.getOrNull(0) ?: ""
        val originId = parts.getOrNull(1) ?: ""

        Log.d(TAG, "Logs: === INICIANDO LOADLINKS CON TOKEN ACTUALIZADO ===")

        return try {
            val watchUrl = "$mainUrl/watch/$epUuid?origin=$originId"

            val actionHeaders = mapOf(
                "accept" to "text/x-component",
                "next-action" to "603712faba47e30723d32819533284371173c10bbd",
                "content-type" to "text/plain;charset=UTF-8",
                "origin" to mainUrl,
                "referer" to watchUrl,
                "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36"
            )

            val requestBodyString = "[\"$epUuid\",\"$originId\"]"
            val response = app.post(
                watchUrl,
                headers = actionHeaders,
                requestBody = requestBodyString.toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            ).text

            val subRegex = Regex("""https?[:\\/]+[^"\\\s]+?\.vtt""")
            subRegex.findAll(response).forEach {
                val subUrl = it.value.replace("\\/", "/")
                Log.d(TAG, "Logs: Subtítulo encontrado: $subUrl")
                subtitleCallback.invoke(newSubtitleFile("Spanish", subUrl))
            }

            val videoRegex = Regex("""https?[:\\/]+[^"\\\s]+?\.m3u8[^"\\\s]*""")

            val links = videoRegex.findAll(response)
                .map { it.value.replace("\\u002F", "/").replace("\\/", "/").replace("\\", "") }
                .distinct()
                .toList()

            Log.d(TAG, "Logs: Enlaces crudos encontrados: ${links.size}")

            links.forEachIndexed { index, rawUrl ->
                val finalUrl = if (rawUrl.contains("stream.animeparadise.moe")) rawUrl
                else "https://stream.animeparadise.moe/m3u8?url=${rawUrl.replace("/", "%2F").replace(":", "%3A")}"

                val serverName = when {
                    finalUrl.contains("windflash") -> "Paradise Wind"
                    finalUrl.contains("lightning") -> "Paradise Light"
                    else -> "Paradise Mirror ${index + 1}"
                }

                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = serverName,
                        url = finalUrl,
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.quality = Qualities.P1080.value
                        this.referer = "$mainUrl/"
                        this.headers = mapOf(
                            "Origin" to mainUrl,
                            "User-Agent" to actionHeaders["user-agent"]!!
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

data class SeasonInfo(val year: Int? = null)
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