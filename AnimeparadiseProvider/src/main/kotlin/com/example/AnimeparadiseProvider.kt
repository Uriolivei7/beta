package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
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
        setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
    }

    private val apiHeaders = mapOf(
        "Accept" to "application/json, text/plain, */*",
        "Origin" to mainUrl,
        "Referer" to "$mainUrl/",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: Iniciando getMainPage")
        return try {
            val url = "$apiUrl/?sort=%7B%22rate%22:-1%7D"
            val response = app.get(url, headers = apiHeaders).text
            val resData: AnimeListResponse = mapper.readValue(response)

            val animeList = resData.data?.map {
                newAnimeSearchResponse(it.title ?: "Sin título", it.link ?: "", TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            } ?: emptyList()
            Log.d(TAG, "Logs: getMainPage exitoso, encontrados ${animeList.size} animes")
            newHomePageResponse(listOf(HomePageList("Popular Anime", animeList)), true)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando anime: $query")
        return try {
            val response = app.get("$apiUrl/?title=$query", headers = apiHeaders).text
            val resData: AnimeListResponse = mapper.readValue(response)

            val results = resData.data?.map {
                newAnimeSearchResponse(it.title ?: "Sin título", it.link ?: "", TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            } ?: emptyList()
            Log.d(TAG, "Logs: Búsqueda exitosa, ${results.size} resultados")
            results
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: Iniciando load seguro para: $url")
        return try {
            val animeSlug = url.substringAfterLast("/")

            val response = app.get(url)
            val document = response.document

            val nextDataElement = document.selectFirst("script#__NEXT_DATA__")
            if (nextDataElement == null) {
                Log.e(TAG, "Logs: No se encontró el script __NEXT_DATA__")
                return null
            }

            val json = mapper.readTree(nextDataElement.data())
            val pageProps = json?.get("props")?.get("pageProps")
            val animeData = pageProps?.get("anime")

            if (animeData == null) {
                Log.e(TAG, "Logs: No se encontraron datos del anime en el JSON")
                return null
            }

            val episodes = animeData.get("ep")?.map { ep ->
                val epId = ep.get("id")?.asText() ?: ""
                val epTitle = ep.get("title")?.asText() ?: "Episodio ${ep.get("number")?.asText()}"
                val epNum = ep.get("number")?.asText()?.toIntOrNull()
                val epThumb = ep.get("image")?.asText()

                newEpisode("/watch/$epId?origin=$animeSlug") {
                    this.name = epTitle
                    this.episode = epNum
                    this.posterUrl = epThumb
                }
            } ?: emptyList()

            Log.d(TAG, "Logs: Extraídos ${episodes.size} episodios para $animeSlug")

            val recommendations = pageProps.get("recommendations")?.mapNotNull { rec ->
                val recTitle = rec.get("title")?.asText() ?: return@mapNotNull null
                val recLink = rec.get("link")?.asText() ?: ""
                newAnimeSearchResponse(recTitle, recLink, TvType.Anime) {
                    this.posterUrl = rec.get("posterImage")?.get("large")?.asText()
                }
            }

            val mainTitle = animeData.get("title")?.asText() ?: "Sin título"

            newAnimeLoadResponse(mainTitle, url, TvType.Anime) {
                this.plot = animeData.get("synopsys")?.asText()
                this.posterUrl = animeData.get("posterImage")?.get("large")?.asText()
                this.year = animeData.get("animeSeason")?.get("year")?.asInt()
                this.showStatus = if (animeData.get("status")?.asText() == "finished")
                    ShowStatus.Completed else ShowStatus.Ongoing

                addEpisodes(DubStatus.Subbed, episodes)
                this.recommendations = recommendations

                val trailer = animeData.get("trailer")?.asText()
                if (!trailer.isNullOrBlank()) addTrailer(trailer)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error crítico en load: ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val currentEpId = data.substringAfter("/watch/").substringBefore("?")
        Log.d(TAG, "Logs: === INICIO LOADLINKS === ID: $currentEpId")
        return try {
            val watchUrl = if (data.startsWith("http")) data else "$mainUrl$data"
            val currentOriginId = data.substringAfter("origin=").substringBefore("&")

            val actionHeaders = mapOf(
                "next-action" to "6002b0ce935408ccf19f5fa745fc47f1d3a4e98b24",
                "content-type" to "text/plain;charset=UTF-8",
                "referer" to watchUrl
            )

            val response = app.post(watchUrl, headers = actionHeaders, requestBody = "[\"$currentEpId\",\"$currentOriginId\"]".toRequestBody()).text

            val videoRegex = Regex("""https://[a-zA-Z0-9.-]+\.[a-z]{2,}/_v7/[^"\\\s]+master\.m3u8""")
            val index = response.indexOf(currentEpId)
            val searchArea = if (index != -1) {
                response.substring((index - 5000).coerceAtLeast(0), (index + 5000).coerceAtMost(response.length))
            } else response

            val videoMatch = videoRegex.find(searchArea) ?: videoRegex.find(response)

            videoMatch?.let {
                val rawUrl = it.value.replace("\\", "").replace("u0026", "&")
                Log.d(TAG, "Logs: Video encontrado: $rawUrl")
                callback.invoke(
                    newExtractorLink("AnimeParadise", "AnimeParadise", "https://stream.animeparadise.moe/m3u8?url=$rawUrl").apply {
                        this.quality = Qualities.P1080.value
                        this.type = ExtractorLinkType.M3U8
                        this.headers = mapOf("referer" to "https://www.animeparadise.moe/")
                    }
                )
            } ?: Log.e(TAG, "Logs: No se encontró link de video en la respuesta")

            true
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en loadLinks: ${e.message}")
            false
        }
    }
}

data class AnimeListResponse(val data: List<AnimeObject>? = null)
data class AnimeDetailResponse(val data: AnimeObject? = null)

data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null,
    val link: String? = null,
    val status: String? = null,
    val synopsys: String? = null,
    val genres: List<String>? = null,
    val trailer: String? = null,
    val animeSeason: SeasonInfo? = null,
    val posterImage: ImageInfo? = null,
    @JsonDeserialize(contentUsing = EpisodeDeserializer::class)
    val ep: List<EpisodeDetail>? = null
)

data class EpisodeDetail(
    val id: String? = null,
    val title: String? = null,
    val image: String? = null,
    val number: String? = null
)

data class SeasonInfo(val year: Int? = null)
data class ImageInfo(val original: String? = null, val large: String? = null)

class EpisodeDeserializer : JsonDeserializer<EpisodeDetail>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): EpisodeDetail {
        val node = p.codec.readTree<com.fasterxml.jackson.databind.JsonNode>(p)
        return if (node.isTextual) {
            // Si es un String (como en tus logs), lo guardamos como ID
            EpisodeDetail(id = node.asText())
        } else {
            EpisodeDetail(
                id = node.get("uid")?.asText() ?: node.get("_id")?.asText() ?: node.get("id")?.asText(),
                title = node.get("title")?.asText(),
                image = node.get("image")?.asText(),
                number = node.get("number")?.asText()
            )
        }
    }
}