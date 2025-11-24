package com.lagradost

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.api.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.nicehttp.NiceResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import java.util.*
import okhttp3.RequestBody.Companion.toRequestBody
import com.lagradost.cloudstream3.app

class KrunchyGeoBypasser(
    client: OkHttpClient
) {
    companion object {
        const val LOG_TAG = "Crunchyroll"
        const val AUTH_URL = "https://www.crunchyroll.com/auth/v1/token"

        const val BASIC_AUTH_HEADER = "Basic Y3Jfd2ViOg=="

        private val MEDIA_TYPE_FORM = "application/x-www-form-urlencoded".toMediaType()

        val baseHeaders = mapOf(
            "accept" to "*/*",
            "connection" to "keep-alive",
            "Host" to "www.crunchyroll.com",
            "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36"
        )
    }

    val session = CustomSession(client)

    var currentBearerToken: String? = null
    private var tokenExpiryTime: Long = 0

    private fun isTokenExpired(): Boolean {
        return currentBearerToken == null || (System.currentTimeMillis() / 1000) >= (tokenExpiryTime - 60)
    }

    suspend fun authenticateAnonymously() {
        if (!isTokenExpired()) {
            Log.i(LOG_TAG, "Token todavía es válido. No se requiere renovación.")
            return
        }

        Log.i(LOG_TAG, "Token caducado o nulo. Intentando obtener nuevo token...")

        val headers = mapOf(
            "authorization" to BASIC_AUTH_HEADER,
            "content-type" to "application/x-www-form-urlencoded",
            "Accept-Language" to "es-419,es;q=0.9,en;q=0.8",
            "Client-Request-ID" to UUID.randomUUID().toString()
        )

        val body = "grant_type=client_id"

        val response = session.post(
            url = AUTH_URL,
            headers = headers,
            requestBody = body.toRequestBody(MEDIA_TYPE_FORM)
        )

        if (response.code == 200) {
            try {
                val tokenData = parseJson<AuthTokenResponse>(response.text)
                currentBearerToken = tokenData.accessToken

                tokenExpiryTime = System.currentTimeMillis() / 1000 + tokenData.expiresIn

                Log.i(LOG_TAG, "¡Nuevo Token obtenido con éxito! Expira en ${tokenData.expiresIn} segundos.")
            } catch (e: Exception) {
                Log.e(LOG_TAG, "ERROR al parsear JSON del token: ${e.message}")
                currentBearerToken = null
            }
        } else {
            Log.e(LOG_TAG, "ERROR al obtener el token. Código: ${response.code}. Body: ${response.text.take(500)}")
            currentBearerToken = null
        }
    }


    suspend fun geoBypassRequest(url: String, extraHeaders: Map<String, String> = emptyMap()): NiceResponse {
        authenticateAnonymously()

        val authHeader = if (currentBearerToken != null) {
            mapOf("authorization" to "Bearer $currentBearerToken", "accept" to "application/json")
        } else {
            Log.e(LOG_TAG, "No hay token, la solicitud a la API probablemente fallará.")
            emptyMap()
        }

        val finalHeaders: Map<String, String> = baseHeaders + authHeader + extraHeaders +
                mapOf("X-Proxy-Host" to "www.crunchyroll.com")

        val response = session.get(
            url = url,
            headers = finalHeaders,
            cookies = emptyMap()
        )
        return response
    }
}

class KrunchyProvider : MainAPI() {
    companion object {
        const val LOG_TAG = "Crunchyroll"
        private const val mainUrl = "https://www.crunchyroll.com"

        private const val CONTENT_BASE_URL = "https://www.crunchyroll.com/"
        private const val CRUNCHYROLL_ASSET_KEY = "e206b12a-3532-4e0d-85e7-37b51e06d69e"

        private const val LOCALE = "es-419"
    }

    val crUnblock by lazy {
        KrunchyGeoBypasser(app.baseClient)
    }

    override var mainUrl = "http://www.crunchyroll.com"
    override var name = "Crunchyroll"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
    )
    override var lang = "mx"

    suspend fun myRequestFunction(url: String, headers: Map<String, String> = emptyMap()): NiceResponse {
        return crUnblock.geoBypassRequest(url, headers)
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        if (page > 1) return newHomePageResponse(arrayListOf(), false)

        val home = ArrayList<HomePageList>()
        val url = "${CONTENT_BASE_URL}content/v2/discover/home?locale=$LOCALE"

        val response = myRequestFunction(url)

        if (response.code != 200) {
            Log.e(LOG_TAG, "getMainPage FALLÓ. Código: ${response.code}. URL: $url. Body: ${response.text.take(500)}")
            return newHomePageResponse(arrayListOf(), false)
        }

        return try {
            val apiResponse = parseJson<ApiSearchResponse>(response.text)

            for (dataItem in apiResponse.data.orEmpty()) {
                val items = dataItem.items.orEmpty()

                if (items.isNotEmpty()) {
                    val searchResponses = ArrayList<SearchResponse>()

                    for (item in items) {
                        if (item.type != "series" && item.type != "movie_listing") continue

                        val seriesUrl = if (item.slugTitle != null) {
                            "$mainUrl/series/${item.id}/${item.slugTitle}"
                        } else {
                            "$mainUrl/series/${item.id}"
                        }
                        val poster = item.getPosterUrl()
                        val title = item.title

                        if (poster != null) {
                            searchResponses.add(
                                newAnimeSearchResponse(title, seriesUrl, TvType.Anime) {
                                    this.posterUrl = poster
                                    this.dubStatus = EnumSet.of(DubStatus.Subbed)
                                }
                            )
                        }
                    }

                    val listName = dataItem.type?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    } ?: "Resultados Destacados"

                    if (searchResponses.isNotEmpty()) {
                        home.add(
                            HomePageList(listName, searchResponses)
                        )
                    }
                }
            }

            Log.i(LOG_TAG, "getMainPage FINALIZADO. Se encontraron ${home.size} listas.")
            newHomePageResponse(home, false)

        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error parseando JSON de página principal: ${e.message}. Body: ${response.text.take(500)}")
            newHomePageResponse(arrayListOf(), false)
        }
    }

    override suspend fun search(query: String): ArrayList<SearchResponse> {
        Log.i(LOG_TAG, "search INICIADO (API V2). Query: $query")

        val searchTypes = "music,series,episode,top_results,movie_listing"
        val url = "${CONTENT_BASE_URL}content/v2/discover/search?q=$query&n=50&type=$searchTypes&ratings=true&locale=$LOCALE"

        val response = myRequestFunction(url)

        if (response.code != 200) {
            Log.e(LOG_TAG, "search FALLÓ. Código: ${response.code}. URL: $url. Body: ${response.text.take(500)}")
            return ArrayList()
        }

        return try {
            val apiResponse = parseJson<ApiSearchResponse>(response.text)
            val searchResults = ArrayList<SearchResponse>()

            for (dataItem in apiResponse.data.orEmpty()) {
                for (item in dataItem.items.orEmpty()) {
                    if (item.type != "series" && item.type != "movie_listing") continue

                    val seriesUrl = if (item.slugTitle != null) {
                        "$mainUrl/series/${item.id}/${item.slugTitle}"
                    } else {
                        "$mainUrl/series/${item.id}"
                    }
                    val poster = item.getPosterUrl()
                    val title = item.title
                    val dubstat = EnumSet.of(DubStatus.Subbed)

                    if (poster != null) {
                        searchResults.add(
                            newAnimeSearchResponse(title, seriesUrl, TvType.Anime) {
                                this.posterUrl = poster
                                this.dubStatus = dubstat
                            }
                        )
                    }
                }
            }

            Log.i(LOG_TAG, "search FINALIZADO. Results found: ${searchResults.size}")
            searchResults

        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error parseando JSON de búsqueda: ${e.message}. Body: ${response.text.take(500)}")
            ArrayList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.i(LOG_TAG, "load INICIADO (API V2). URL: $url")

        val seriesId = getSeriesIdFromUrl(url) ?: throw ErrorLoadingException("No se pudo obtener el Series ID de la URL: $url")

        val detailsUrl = "${CONTENT_BASE_URL}content/v2/cms/series/$seriesId?locale=$LOCALE"
        val detailsResponse = myRequestFunction(detailsUrl)

        if (detailsResponse.code != 200) {
            Log.e(LOG_TAG, "load FALLÓ al obtener detalles. Code: ${detailsResponse.code}. URL: $detailsUrl")
            throw ErrorLoadingException("Fallo al obtener detalles de la serie.")
        }

        val seriesDetailsList = parseJson<ApiSeriesResponseWrapper>(detailsResponse.text)
        val seriesDetails = seriesDetailsList.items.firstOrNull() ?: throw ErrorLoadingException("No se encontró información de la serie.")

        val title = seriesDetails.title
        val description = seriesDetails.description
        val posterU = seriesDetails.getPosterUrl()
        val tags = seriesDetails.genres ?: emptyList() // Usa la propiedad calculada 'genres'

        Log.i(LOG_TAG, "Load Info: Title='$title', PosterUrl='${posterU}'")

        val subEpisodes = mutableListOf<Episode>()
        val dubEpisodes = mutableListOf<Episode>()
        val seasonNamesList = mutableListOf<SeasonData>()

        val seasonsUrl = "${CONTENT_BASE_URL}content/v2/cms/series/$seriesId/seasons?force_locale=&locale=$LOCALE"
        val seasonsResponse = myRequestFunction(seasonsUrl)

        if (seasonsResponse.code != 200) {
            Log.w(LOG_TAG, "load adv: Fallo al obtener temporadas, continuando. Code: ${seasonsResponse.code}. URL: $seasonsUrl")
        } else {
            val seasonsData = try {
                parseJson<ApiSeasonsResponse>(seasonsResponse.text)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error parseando la lista de temporadas: ${e.message}")
                null
            }

            if (seasonsData?.items != null) {
                Log.i(LOG_TAG, "Found ${seasonsData.items.size} seasons.")
                var seasonCounter = 1

                for (season in seasonsData.items.orEmpty()) {
                    val currentSeasonNumber = seasonCounter++
                    seasonNamesList.add(SeasonData(currentSeasonNumber, season.title, null))

                    val episodesUrl = "${CONTENT_BASE_URL}content/v2/cms/seasons/${season.id}/episodes?locale=$LOCALE"
                    val episodesResponse = myRequestFunction(episodesUrl)

                    val episodesData = try {
                        parseJson<ApiEpisodesResponse>(episodesResponse.text)
                    } catch (e: Exception) {
                        Log.e(LOG_TAG, "Error parsing episodes for season ${season.title}: ${e.message}")
                        continue
                    }

                    for (epItem in episodesData.items.orEmpty().reversed()) {
                        val versions = epItem.versions.orEmpty()

                        val subVersion = versions.find { it.audioLocale == "ja-JP" || it.audioLocale == "enUS" }
                        val dubVersion = versions.find { it.audioLocale == "es-LA" || it.audioLocale == "es-ES" }

                        val subGuid = subVersion?.guid ?: epItem.id
                        val dubGuid = dubVersion?.guid ?: ""

                        val epLinkData = "${epItem.id}|$subGuid|$dubGuid"

                        if (subVersion != null || dubVersion != null) {

                            val epi = newEpisode(
                                url = epLinkData,
                                initializer = {
                                    this.name = epItem.title
                                    this.posterUrl = epItem.getThumbnailUrl()
                                    this.description = epItem.title
                                    this.season = currentSeasonNumber
                                    this.episode = epItem.episodeNumber
                                }
                            )

                            if (subVersion != null) {
                                subEpisodes.add(epi)
                            }

                            if (dubVersion != null) {
                                dubEpisodes.add(epi)
                            }
                        }
                    }
                }
            }
        }

        val recommendations = emptyList<SearchResponse>()

        Log.i(LOG_TAG, "load FINALIZADO. Sub: ${subEpisodes.size}, Dub: ${dubEpisodes.size}")

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = posterU
            this.engName = title
            this.plot = description
            this.tags = tags

            this.episodes = HashMap<DubStatus, List<Episode>>().apply {
                if (subEpisodes.isNotEmpty()) this[DubStatus.Subbed] = subEpisodes
                if (dubEpisodes.isNotEmpty()) this[DubStatus.Dubbed] = dubEpisodes
            }

            this.seasonNames = seasonNamesList.sortedBy { it.season }
            this.recommendations = recommendations
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {

        Log.i(LOG_TAG, "loadLinks INICIADO (Playback V3). Data: $data")

        val parts = data.split("|")
        if (parts.size < 3) {
            Log.e(LOG_TAG, "loadLinks FALLÓ. Data mal formada (esperado ID|SubGUID|DubGUID).")
            return false
        }

        val episodeId = parts[0]
        val subGuid = parts[1]
        val dubGuid = parts[2]

        val streamGuid = if (isCasting && dubGuid.isNotEmpty()) {
            Log.i(LOG_TAG, "Usando GUID Doblado: $dubGuid")
            dubGuid
        } else {
            Log.i(LOG_TAG, "Usando GUID Subtitulado/Original: $subGuid")
            subGuid
        }

        if (streamGuid.isEmpty()) {
            Log.e(LOG_TAG, "loadLinks FALLÓ. No se encontró GUID para el idioma solicitado.")
            return false
        }

        val vilosApiUrl = "${CONTENT_BASE_URL}playback/v3/$streamGuid/web/chrome/play"

        val playbackHeaders = mapOf(
            "Accept-Language" to LOCALE,
            "Referer" to "https://www.crunchyroll.com/",
            "X-Crunchyroll-Asset-Key" to CRUNCHYROLL_ASSET_KEY
        )

        val response = myRequestFunction(vilosApiUrl, headers = playbackHeaders)

        if (response.code != 200) {
            Log.e(LOG_TAG, "loadLinks FALLÓ. API de Playback v3 falló (Code: ${response.code}). URL: $vilosApiUrl")
            return false
        }

        val dat = response.text

        if (!dat.isNullOrEmpty()) {
            val json = try {
                parseJson<KrunchyVideo>(dat)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error parseando JSON de KrunchyVideo (Playback V3): ${e.message}")
                return false
            }

            val streams = ArrayList<Streams>()

            val validFormats = listOf(
                "adaptive_hls", "adaptive_dash",
                "multitrack_adaptive_hls_v2",
                "vo_adaptive_dash", "vo_adaptive_hls",
                "trailer_hls",
            )

            val validAudioLangs = listOf("jaJP", "esLA", "esES", "enUS")

            for (stream in json.streams) {
                if (stream.format in validFormats) {
                    if (stream.audioLang in validAudioLangs || stream.format == "trailer_hls") {
                        stream.title = stream.title()
                        streams.add(stream)
                    }
                }
            }

            streams.forEach { stream ->
                if (stream.url.contains("m3u8") && stream.format!!.contains("adaptive")) {
                    callback(
                        newExtractorLink(
                            source = "Crunchyroll",
                            name = "Crunchy - ${stream.title}",
                            url = stream.url,
                            type = ExtractorLinkType.M3U8
                        ) {
                            this.referer = ""
                            this.quality = getQualityFromName(stream.resolution)
                        }
                    )
                }
            }

            json.subtitles.forEach {
                val langclean = it.language.replace("esLA", "Spanish")
                    .replace("enUS", "English")
                    .replace("esES", "Spanish (Spain)")

                if (it.format == "srt" || it.format == "ass" || it.format == "vtt" || it.format == null) {
                    subtitleCallback(
                        SubtitleFile(langclean, it.url)
                    )
                }
            }

            Log.i(LOG_TAG, "loadLinks FINALIZADO con éxito (Playback V3).")
            return true
        }

        Log.e(LOG_TAG, "loadLinks FALLÓ. La API de Vilos no devolvió streams.")
        return false
    }
}

data class AuthTokenResponse(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Int,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("refresh_token") val refreshToken: String?
)

data class ApiImage(
    @JsonProperty("type") val type: String?,
    @JsonProperty("source") val source: String?,
    @JsonProperty("width") val width: Int?,
    @JsonProperty("height") val height: Int?,
)

data class ApiImages(
    @JsonProperty("poster_tall") val posterTall: List<List<ApiImage>>?,
    @JsonProperty("poster_wide") val posterWide: List<List<ApiImage>>?,
    @JsonProperty("thumbnail") val thumbnailRaw: Any?
)

data class ApiSeriesResponseWrapper(
    @JsonProperty("total") val total: Int?,
    @JsonProperty("data") val items: List<ApiSeriesItem>
)

data class ApiSeriesItem(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("description") val description: String?,
    @JsonProperty("images") val images: ApiImages?,
    @JsonProperty("type") val type: String?,
    @JsonProperty("slug_title") val slugTitle: String?,
    @JsonProperty("genres") private val rawGenres: List<Any>?,
) {
    val genres: List<String>?
        get() = rawGenres?.filterIsInstance<String>()

    fun getPosterUrl(): String? {
        val tallPoster = images
            ?.posterTall
            ?.firstOrNull()
            ?.firstOrNull()
            ?.source

        if (tallPoster != null) return tallPoster

        val thumbnailData = images?.thumbnailRaw

        if (thumbnailData is List<*>) {
            val firstList = thumbnailData.firstOrNull() as? List<*>
            val firstItem = firstList?.firstOrNull()
            // When Jackson deserializes Any?, it creates Map objects, not ApiImage objects
            return when (firstItem) {
                is ApiImage -> firstItem.source
                is Map<*, *> -> firstItem["source"] as? String
                else -> null
            }
        }

        return null
    }
}

data class ApiSearchResponse(
    @JsonProperty("total") val total: Int,
    @JsonProperty("data") val data: List<ApiSearchData>?
)

data class ApiSearchData(
    @JsonProperty("type") val type: String?,
    @JsonProperty("count") val count: Int?,
    @JsonProperty("items") val items: List<ApiSeriesItem>?
)

data class ApiSeason(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
)

data class ApiSeasonsResponse(
    @JsonProperty("total") val total: Int?,
    @JsonProperty("data") val items: List<ApiSeason>
)

data class ApiEpisodeItem(
    @JsonProperty("id") val id: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("episode_number") val episodeNumber: Int?,
    @JsonProperty("images") val images: Map<String, List<List<ApiImage>>>?,
    @JsonProperty("versions") val versions: List<ApiEpisodeVersion>?,
    @JsonProperty("slug_title") val slugTitle: String,
) {
    fun getThumbnailUrl(): String? {
        return images
            ?.get("thumbnail")
            ?.firstOrNull()
            ?.firstOrNull()
            ?.source
    }
}

data class ApiEpisodeVersion(
    @JsonProperty("audio_locale") val audioLocale: String?,
    @JsonProperty("guid") val guid: String?
)

data class ApiEpisodesResponse(
    @JsonProperty("total") val total: Int?,
    @JsonProperty("data") val items: List<ApiEpisodeItem>
)

data class Subtitles(
    @JsonProperty("language") val language: String,
    @JsonProperty("url") val url: String,
    @JsonProperty("title") val title: String?,
    @JsonProperty("format") val format: String?
)

data class Streams(
    @JsonProperty("format") val format: String?,
    @JsonProperty("audio_lang") val audioLang: String?,
    @JsonProperty("hardsub_lang") val hardsubLang: String?,
    @JsonProperty("url") val url: String,
    @JsonProperty("resolution") val resolution: String?,
    @JsonProperty("title") var title: String?
) {
    fun title(): String {
        return when {
            this.hardsubLang == "enUS" && this.audioLang == "jaJP" -> "Hardsub (English)"
            this.hardsubLang == "esLA" && this.audioLang == "jaJP" -> "Hardsub (Latino)"
            this.hardsubLang == "esES" && this.audioLang == "jaJP" -> "Hardsub (Español España)"
            this.audioLang == "esLA" -> "Latino"
            this.audioLang == "esES" -> "Español España"
            this.audioLang == "enUS" -> "English (US)"
            else -> "Subbed (Original)"
        }
    }
}

data class LinkData(
    val episodeId: String,
    val slugTitle: String,
    val requestedLangCode: String
)

data class KrunchyVideo(
    @JsonProperty("streams") val streams: List<Streams>,
    @JsonProperty("subtitles") val subtitles: List<Subtitles>,
)

fun getSeriesIdFromUrl(url: String): String? {
    val match = Regex("series/(.*?)/").find(url)
    return match?.groupValues?.getOrNull(1)
}