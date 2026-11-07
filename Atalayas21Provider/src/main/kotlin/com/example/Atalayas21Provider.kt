package com.example

import android.content.Context
import android.util.Log
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.regex.Pattern

class Atalayas21Provider : MainAPI() {
    companion object {
        private const val BASE_URL = "https://atalayas21.com"
        const val TAG = "Atalayas21"
    }

    override var mainUrl = BASE_URL
    override var name = "Atalayas21"
    override var lang = "es"
    override val hasMainPage = true
    override val hasChromecastSupport = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.TvSeries, TvType.Cartoon
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ApiResponse<T>(
        @JsonProperty("success") var success: Boolean = false,
        @JsonProperty("message") var message: String? = null,
        @JsonProperty("data") var data: T? = null,
        @JsonProperty("seasons") var seasons: List<Int>? = null,
        @JsonProperty("total") var total: Int? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SerieItem(
        @JsonProperty("id") var id: Long? = null,
        @JsonProperty("titulo") var titulo: String? = null,
        @JsonProperty("nombre_categoria") var nombreCategoria: String? = null,
        @JsonProperty("thumb") var thumb: String? = null,
        @JsonProperty("imagen_url") var imagenUrl: String? = null,
        @JsonProperty("portada_url") var portadaUrl: String? = null,
        @JsonProperty("slug") var slug: String? = null,
        @JsonProperty("temporadas") var temporadas: Int? = null,
        @JsonProperty("total") var total: Int? = null,
        @JsonProperty("episodios") var episodios: Int? = null,
        @JsonProperty("descripcion") var descripcion: String? = null,
        @JsonProperty("status") var status: String? = null,
        @JsonProperty("finalizada") var finalizada: Boolean? = null,
        @JsonProperty("año_estreno") var añoEstreno: Int? = null,
        @JsonProperty("año") var año: Int? = null,
        @JsonProperty("popularidad") var popularidad: Double? = null,
        @JsonProperty("url") var url: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EpisodeItem(
        @JsonProperty("id") var id: Long? = null,
        @JsonProperty("titulo") var titulo: String? = null,
        @JsonProperty("title") var title: String? = null,
        @JsonProperty("numero_episodio") var numeroEpisodio: Int? = null,
        @JsonProperty("numero") var numero: Int? = null,
        @JsonProperty("temporada") var temporada: Int? = null,
        @JsonProperty("serie_id") var serieId: Long? = null,
        @JsonProperty("duracion") var duracion: String? = null,
        @JsonProperty("imagen_url") var imagenUrl: String? = null,
        @JsonProperty("descripcion") var descripcion: String? = null,
        @JsonProperty("video_url") var videoUrl: String? = null,
        @JsonProperty("link_video") var linkVideo: String? = null,
        @JsonProperty("embed_url") var embedUrl: String? = null,
        @JsonProperty("url_video") var urlVideo: String? = null,
        @JsonProperty("vistas") var vistas: Int? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SearchData(
        @JsonProperty("series") var series: List<SearchItem>? = null,
        @JsonProperty("episodes") var episodes: List<SearchItem>? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SearchItem(
        @JsonProperty("id") var id: Long? = null,
        @JsonProperty("titulo") var titulo: String? = null,
        @JsonProperty("imagen_url") var imagenUrl: String? = null,
        @JsonProperty("tipo") var tipo: String? = null,
        @JsonProperty("url") var url: String? = null,
        @JsonProperty("slug") var slug: String? = null,
        @JsonProperty("episodios") var episodios: Int? = null,
        @JsonProperty("descripcion") var descripcion: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EpisodeLinkData(
        @JsonProperty("url") var url: String? = null,
        @JsonProperty("type") var type: String? = null
    )

    private val apiHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36",
        "Accept" to "application/json, text/plain, */*",
        "Referer" to "$BASE_URL/",
        "Accept-Language" to "es-ES,es;q=0.9"
    )

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "getMainPage page=$page request=${request.name}")
        try {
            val popularJson = app.get("$BASE_URL/api/series.php?popular=true&limit=50&lang=es", headers = apiHeaders).text
            Log.d(TAG, "popular json len=${popularJson.length} success=${parseJson<ApiResponse<List<SerieItem>>>(popularJson).success}")
            val popularResp = parseJson<ApiResponse<List<SerieItem>>>(popularJson)
            val popularItems = popularResp.data?.mapNotNull { it.toSearchResponse() } ?: emptyList()
            Log.d(TAG, "popular items=${popularItems.size}")

            val recentJson = app.get("$BASE_URL/api/series.php?recent=true&limit=50&lang=es", headers = apiHeaders).text
            Log.d(TAG, "recent json len=${recentJson.length} success=${parseJson<ApiResponse<List<SerieItem>>>(recentJson).success}")
            val recentResp = parseJson<ApiResponse<List<SerieItem>>>(recentJson)
            val recentItems = recentResp.data?.mapNotNull { it.toSearchResponse() } ?: emptyList()
            Log.d(TAG, "recent items=${recentItems.size}")

            return newHomePageResponse(
                listOf(
                    HomePageList("Populares", popularItems),
                    HomePageList("Recientes", recentItems)
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "getMainPage error: ${e.message}", e)
            return null
        }
    }

    override suspend fun search(query: String): List<SearchResponse>? {
        Log.d(TAG, "search query='$query'")
        try {
            val encoded = java.net.URLEncoder.encode(query, "UTF-8")
            val json = app.get("$BASE_URL/api/search-advanced.php?query=$encoded&lang=es", headers = apiHeaders).text
            Log.d(TAG, "search json len=${json.length}")
            val resp = parseJson<ApiResponse<SearchData>>(json)
            Log.d(TAG, "search success=${resp.success} series=${resp.data?.series?.size} episodes=${resp.data?.episodes?.size}")
            val series = resp.data?.series?.filter { it.tipo == "serie" }?.mapNotNull { it.toSearchResponse() } ?: emptyList()
            Log.d(TAG, "search results=${series.size}")
            return series
        } catch (e: Exception) {
            Log.e(TAG, "search error: ${e.message}", e)
            return emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "load url=$url")
        try {
            val id = url.substringAfterLast("?id=").substringBefore("&")
            Log.d(TAG, "load id=$id")
            val json = app.get("$BASE_URL/api/series.php?id=$id&lang=es", headers = apiHeaders).text
            Log.d(TAG, "load series json len=${json.length}")
            val resp = parseJson<ApiResponse<List<SerieItem>>>(json)
            val serie = resp.data?.firstOrNull() ?: throw ErrorLoadingException("Serie no encontrada")
            Log.d(TAG, "load serie id=${serie.id} title=${serie.titulo} seasons=${serie.temporadas} eps=${serie.episodios}")

            val epJson = app.get("$BASE_URL/api/episodios.php?serie_id=$id&limit=500", headers = apiHeaders).text
            Log.d(TAG, "load episodes json len=${epJson.length}")
            val epResp = parseJson<ApiResponse<List<EpisodeItem>>>(epJson)
            val allEps = epResp.data ?: emptyList()
            Log.d(TAG, "load all episodes count=${allEps.size}")
            allEps.forEach { ep ->
                Log.d(TAG, "  ep id=${ep.id} num=${ep.numeroEpisodio ?: ep.numero} season=${ep.temporada} title=${ep.titulo ?: ep.title} urlVideo=${ep.urlVideo ?: ep.linkVideo ?: ep.embedUrl ?: ep.videoUrl}")
            }

            val seasons = allEps.mapNotNull { it.temporada }.distinct().sorted()
                .ifEmpty { epResp.seasons?.ifEmpty { listOf(1) } ?: listOf(1) }
            Log.d(TAG, "load seasons=$seasons")

            val episodes = mutableListOf<Episode>()
            for (season in seasons) {
                val seasonEps = allEps.filter { (it.temporada ?: 1) == season }
                    .sortedBy { it.numeroEpisodio ?: it.numero ?: 0 }
                Log.d(TAG, "load season $season eps=${seasonEps.size}")
                for (ep in seasonEps) {
                    val epNum = ep.numeroEpisodio ?: ep.numero ?: 0
                    val epTitle = cleanEpisodeTitle(ep.titulo ?: ep.title ?: "", epNum)
                    episodes.add(
                        newEpisode("$BASE_URL/api/episodios.php?id=${ep.id}") {
                            this.name = epTitle.ifEmpty { "Episodio $epNum" }
                            this.season = season
                            this.episode = epNum
                            this.posterUrl = ep.imagenUrl?.let { fixUrl(it) }
                        }
                    )
                }
            }

            val poster = serie.imagenUrl ?: serie.thumb
            val showStatus = when {
                serie.finalizada == true -> ShowStatus.Completed
                else -> ShowStatus.Ongoing
            }

            return newTvSeriesLoadResponse(
                serie.titulo ?: "Serie",
                url,
                TvType.TvSeries,
                episodes
            ) {
                this.posterUrl = poster?.let { fixUrl(it) }
                this.plot = serie.descripcion
                this.year = serie.añoEstreno ?: serie.año
                this.showStatus = showStatus
            }
        } catch (e: Exception) {
            Log.e(TAG, "load error: ${e.message}", e)
            return null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "loadLinks data=$data")
        val u = data.trim()
        if (u.isBlank()) {
            Log.e(TAG, "loadLinks: data is blank")
            return false
        }

        // Extraer el apiId de la URL
        var apiId: String
        if (u.contains("?id=")) {
            apiId = u.substringAfter("?id=").substringBefore("&").trim()
        } else {
            apiId = u.substringAfterLast("/").trim()
        }
        Log.d(TAG, "loadLinks apiId=$apiId")

        var found = false

        // Intento 1: Llamar a la API de episodios
        try {
            val epJson = app.get("$BASE_URL/api/episodios.php?id=$apiId", headers = apiHeaders).text
            Log.d(TAG, "loadLinks episodios json len=${epJson.length}")
            val epResp = parseJson<ApiResponse<List<EpisodeItem>>>(epJson)
            Log.d(TAG, "loadLinks episodios success=${epResp.success} dataSize=${epResp.data?.size}")
            val ep = epResp.data?.firstOrNull()
            if (ep != null) {
                Log.d(TAG, "loadLinks episode id=${ep.id} title=${ep.titulo ?: ep.title} video_url=${ep.videoUrl} link_video=${ep.linkVideo} embed_url=${ep.embedUrl} url_video=${ep.urlVideo}")
            }

            val videoUrl = ep?.videoUrl ?: ep?.linkVideo ?: ep?.urlVideo ?: ep?.embedUrl
            if (videoUrl != null) {
                val cleanUrl = videoUrl.replace("\\/", "/")
                Log.d(TAG, "loadLinks cleanUrl=$cleanUrl")
                found = handleVideoUrl(cleanUrl, data, subtitleCallback, callback) || found
                if (found) {
                    Log.d(TAG, "loadLinks found via episodios API")
                    return true
                }
            } else {
                Log.d(TAG, "loadLinks no videoUrl in episode data")
            }
        } catch (e: Exception) {
            Log.e(TAG, "loadLinks episodios error: ${e.message}", e)
        }

        // Intento 2: Llamar a get-episode-link.php
        try {
            Log.d(TAG, "loadLinks trying get-episode-link.php")
            val linkJson = app.get("$BASE_URL/api/get-episode-link.php?episode_id=$apiId", headers = apiHeaders).text
            Log.d(TAG, "loadLinks get-episode-link json len=${linkJson.length}")
            val linkResp = parseJson<ApiResponse<EpisodeLinkData>>(linkJson)
            val linkUrl = linkResp.data?.url
            if (linkUrl != null) {
                val cleanUrl = linkUrl.replace("\\/", "/")
                Log.d(TAG, "loadLinks link cleanUrl=$cleanUrl")
                found = handleVideoUrl(cleanUrl, data, subtitleCallback, callback) || found
                if (found) {
                    Log.d(TAG, "loadLinks found via get-episode-link")
                    return true
                }
            } else {
                Log.d(TAG, "loadLinks get-episode-link no url in data")
            }
        } catch (e: Exception) {
            Log.e(TAG, "loadLinks get-episode-link error: ${e.message}", e)
        }

        Log.d(TAG, "loadLinks found=$found")
        return found
    }

    private fun cleanEpisodeTitle(raw: String, epNum: Int = 0): String {
        val parts = raw.split(" - ")
        val last = if (parts.size > 1) parts.last().trim() else raw.trim()
        if (last.isEmpty()) return ""
        if (last.contains(Regex("""\d+x\d+"""))) return ""
        if (epNum > 0) {
            val epPattern = Regex("""^(cap[íi]tulo\s+)?$epNum(\s*\(.*\))?$""", RegexOption.IGNORE_CASE)
            if (last.replace(".", "").matches(epPattern)) return ""
        }
        return last
    }

    private suspend fun handleVideoUrl(
        url: String,
        data: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "handleVideoUrl url=$url")
        val cleanUrl = url.trim()
        if (cleanUrl.isBlank()) return false

        return when {
            cleanUrl.endsWith(".m3u8", ignoreCase = true) || cleanUrl.endsWith(".mp4", ignoreCase = true) || cleanUrl.endsWith(".mpd", ignoreCase = true) -> {
                Log.d(TAG, "handleVideoUrl: direct HLS/MP4/MPD -> emit direct")
                callback.invoke(
                    newExtractorLink(name, name, cleanUrl, ExtractorLinkType.M3U8) {
                        this.referer = "$BASE_URL/"
                        this.quality = Qualities.Unknown.value
                    }
                )
                true
            }
            cleanUrl.contains("youtube.com") || cleanUrl.contains("youtu.be") -> {
                Log.d(TAG, "handleVideoUrl: YouTube -> emit direct YouTube link")
                // YouTube URLs directas funcionan en ExoPlayer si se le dan con headers
                val ytId = extractYouTubeId(cleanUrl)
                if (ytId != null) {
                    val ytUrl = "https://www.youtube.com/watch?v=$ytId"
                    callback.invoke(
                        newExtractorLink(name, "YouTube", ytUrl, ExtractorLinkType.M3U8) {
                            this.referer = "https://www.youtube.com/"
                            this.quality = Qualities.Unknown.value
                            this.headers = mapOf(
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36",
                                "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
                                "Accept-Language" to "es-ES,es;q=0.9"
                            )
                        }
                    )
                    return true
                }
                // Fallback: intentar extractor nativo
                Log.d(TAG, "handleVideoUrl: YouTube -> trying loadExtractor")
                return loadExtractor(cleanUrl, data, subtitleCallback, callback)
            }
            cleanUrl.contains("ok.ru") -> {
                Log.d(TAG, "handleVideoUrl: ok.ru -> trying native extractor first")
                val nativeResult = loadExtractor(cleanUrl, data, subtitleCallback, callback)
                Log.d(TAG, "handleVideoUrl: native ok.ru result=$nativeResult")
                if (nativeResult) return true

                Log.d(TAG, "handleVideoUrl: ok.ru -> trying custom ok.ru extractor")
                return tryOkruExtractor(cleanUrl, data, subtitleCallback, callback)
            }
            cleanUrl.contains("vk.com") || cleanUrl.contains("vimeo.com") || cleanUrl.contains("dailymotion.com") -> {
                Log.d(TAG, "handleVideoUrl: other known host, using loadExtractor")
                return loadExtractor(cleanUrl, data, subtitleCallback, callback)
            }
            else -> {
                Log.d(TAG, "handleVideoUrl: unknown host, using loadExtractor")
                return loadExtractor(cleanUrl, data, subtitleCallback, callback)
            }
        }
    }

    private fun extractYouTubeId(url: String): String? {
        val patterns = listOf(
            Regex("""[?&]v=([a-zA-Z0-9_-]{11})"""),
            Regex("""youtu\.be/([a-zA-Z0-9_-]{11})"""),
            Regex("""embed/([a-zA-Z0-9_-]{11})""")
        )
        for (p in patterns) {
            val m = p.find(url)
            if (m != null) return m.groupValues[1]
        }
        return null
    }

    /**
     * Extractor propio para ok.ru cuando el nativo de CS3 falla.
     * Descarga la página del embed y busca el HLS/MP4 directo.
     */
    private suspend fun tryOkruExtractor(
        url: String,
        data: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "tryOkruExtractor url=$url")
        return try {
            val html = withContext(Dispatchers.IO) {
                okHttpClient.newCall(
                    Request.Builder()
                        .url(url)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36")
                        .header("Referer", "https://ok.ru/")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("Accept-Language", "es-ES,es;q=0.9")
                        .build()
                ).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.w(TAG, "tryOkruExtractor: HTTP ${response.code}")
                        null
                    } else {
                        response.body?.string() ?: null
                    }
                }
            }

            if (html == null || html.isEmpty()) {
                Log.w(TAG, "tryOkruExtractor: empty HTML")
                return false
            }

            Log.d(TAG, "tryOkruExtractor: HTML len=${html.length}")

            // Buscar URLs de video en el HTML
            val patterns = listOf(
                Regex("""https?://[^"'\s<>]+\.m3u8[^"'\s<>]*"""),
                Regex("""https?://[^"'\s<>]+\.mp4[^"'\s<>]*"""),
                Regex("""file\s*:\s*["']([^"']+)["']"""),
                Regex("""src\s*:\s*["']([^"']+)["']"""),
                Regex("""hls\s*:\s*["']([^"']+)["']"""),
                Regex("""videoUrl\s*:\s*["']([^"']+)["']""")
            )

            for (pattern in patterns) {
                val match = pattern.find(html)
                if (match != null) {
                    val foundUrl = match.groupValues.last()
                    Log.d(TAG, "tryOkruExtractor: found URL pattern=${pattern.pattern} url=$foundUrl")
                    if (foundUrl.contains(".m3u8") || foundUrl.contains(".mp4")) {
                        callback.invoke(
                            newExtractorLink(name, "Ok.ru", foundUrl, ExtractorLinkType.M3U8) {
                                this.referer = url
                                this.quality = Qualities.Unknown.value
                                this.headers = mapOf(
                                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36",
                                    "Referer" to url,
                                    "Origin" to "https://ok.ru/"
                                )
                            }
                        )
                        return true
                    }
                }
            }

            // Buscar en datos embebidos (JSON en el HTML)
            val videoUrlPatterns = listOf(
                Regex("""["'](https?://[^"']+\.m3u8[^"']*)["']"""),
                Regex("""["'](https?://[^"']+\.mp4[^"']*)["']""")
            )
            for (pattern in videoUrlPatterns) {
                val match = pattern.find(html)
                if (match != null) {
                    val foundUrl = match.groupValues[1]
                    Log.d(TAG, "tryOkruExtractor: found quoted URL $foundUrl")
                    callback.invoke(
                        newExtractorLink(name, "Ok.ru", foundUrl, ExtractorLinkType.M3U8) {
                            this.referer = url
                            this.quality = Qualities.Unknown.value
                            this.headers = mapOf(
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/149.0.0.0 Safari/537.36",
                                "Referer" to url,
                                "Origin" to "https://ok.ru/"
                            )
                        }
                    )
                    return true
                }
            }

            Log.w(TAG, "tryOkruExtractor: no video URL found in HTML")
            return false
        } catch (e: Exception) {
            Log.e(TAG, "tryOkruExtractor error: ${e.message}", e)
            return false
        }
    }

    private fun SerieItem.toSearchResponse(): SearchResponse? {
        val id = this.id ?: return null
        val poster = this.imagenUrl ?: this.thumb
        return newMovieSearchResponse(
            this.titulo ?: "Sin título",
            "$BASE_URL/api/series.php?id=$id&lang=es",
            TvType.TvSeries
        ) {
            this.posterUrl = when {
                poster?.startsWith("http") == true -> fixUrl(poster)
                poster != null -> "$BASE_URL/$poster"
                else -> null
            }
            this.year = this@toSearchResponse.añoEstreno
        }
    }

    private fun SearchItem.toSearchResponse(): SearchResponse? {
        val id = this.id ?: return null
        return newMovieSearchResponse(
            this.titulo ?: "Sin título",
            "$BASE_URL/api/series.php?id=$id&lang=es",
            TvType.TvSeries
        ) {
            this.posterUrl = this@toSearchResponse.imagenUrl?.let {
                if (it.startsWith("http")) fixUrl(it) else "$BASE_URL/$it"
            }
        }
    }

    private fun String.encodeURI(): String {
        return java.net.URLEncoder.encode(this, "UTF-8")
    }
}