package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.StringUtils.encodeUri
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink

class YoutubeProvider : MainAPI() {
    override var mainUrl             = "https://inv.perditum.com"
    override var name                = "YouTube"
    override val hasMainPage         = true
    override var lang                = "us"
    override val supportedTypes      = setOf(TvType.Others)

    private val TAG = "YouTubeProvider"


    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "Iniciando getMainPage. URL base: $mainUrl")

        val trendingResponse = app.get("${mainUrl}/api/v1/trending?region=${lang.uppercase()}&type=news&fields=videoId,title").text
        val trending = tryParseJson<List<SearchEntry>>(trendingResponse)
        Log.d(TAG, "Datos de Tendencias (Noticias) obtenidos: ${trending?.size ?: 0} entradas.")

        val musicResponse = app.get("${mainUrl}/api/v1/trending?region=${lang.uppercase()}&type=music&fields=videoId,title").text
        val music = tryParseJson<List<SearchEntry>>(musicResponse)
        Log.d(TAG, "Datos de Música obtenidos: ${music?.size ?: 0} entradas.")

        val moviesResponse = app.get("${mainUrl}/api/v1/trending?region=${lang.uppercase()}&type=movies&fields=videoId,title").text
        val movies = tryParseJson<List<SearchEntry>>(moviesResponse)
        Log.d(TAG, "Datos de Películas obtenidos: ${movies?.size ?: 0} entradas.")

        val gamingResponse = app.get("${mainUrl}/api/v1/trending?region=${lang.uppercase()}&type=gaming&fields=videoId,title").text
        val gaming = tryParseJson<List<SearchEntry>>(gamingResponse)
        Log.d(TAG, "Datos de Videojuegos obtenidos: ${gaming?.size ?: 0} entradas.")

        return newHomePageResponse(
            listOf(
                HomePageList(
                    "Tendencias (Noticias)",
                    trending?.map { it.toSearchResponse(this) } ?: emptyList(),
                    true
                ),
                HomePageList(
                    "Música",
                    music?.map { it.toSearchResponse(this) } ?: emptyList(),
                    true
                ),
                HomePageList(
                    "Películas",
                    movies?.map { it.toSearchResponse(this) } ?: emptyList(),
                    true
                ),
                HomePageList(
                    "Videojuegos",
                    gaming?.map { it.toSearchResponse(this) } ?: emptyList(),
                    true
                )
            ),
            false
        )
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Buscando query: $query")

        val res = tryParseJson<List<SearchEntry>>(
            app.get("${mainUrl}/api/v1/search?q=${query.encodeUri()}&region=${lang.uppercase()}&page=1&type=video&fields=videoId,title&limit=50").text
        )

        Log.d(TAG, "Resultados de búsqueda encontrados: ${res?.size ?: 0}")

        return res?.map { it.toSearchResponse(this) } ?: emptyList()
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Iniciando load para URL: $url")

        val videoId = Regex("watch\\?v=([a-zA-Z0-9_-]+)").find(url)?.groupValues?.get(1)

        Log.d(TAG, "Video ID extraído: $videoId")

        if (videoId.isNullOrEmpty()) return null

        val res = tryParseJson<VideoEntry>(
            app.get("${mainUrl}/api/v1/videos/${videoId}?region=${lang.uppercase()}&fields=videoId,title,description,recommendedVideos,author,authorThumbnails").text
        )

        Log.d(TAG, "Carga de video: ${if (res != null) "Éxito" else "Fallo al parsear la respuesta de la API"}")

        return res?.toLoadResponse(this)
    }

    private data class VideoStreamsResponse(val formatStreams: List<FormatStream>?)
    private data class FormatStream(
        val url: String,
        val quality: String,
        val container: String
    )

    private data class SearchEntry(val title: String, val videoId: String) {
        fun toSearchResponse(provider: YoutubeProvider): SearchResponse {
            return provider.newMovieSearchResponse(
                title,
                "${provider.mainUrl}/watch?v=${videoId}",
                TvType.Others
            ) {
                this.posterUrl = "${provider.mainUrl}/vi/${videoId}/mqdefault.jpg"
            }
        }
    }

    private data class VideoEntry(
        val title: String,
        val description: String,
        val videoId: String,
        val recommendedVideos: List<SearchEntry>,
        val author: String,
        val authorThumbnails: List<Thumbnail>
    ) {
        suspend fun toLoadResponse(provider: YoutubeProvider): LoadResponse {
            return provider.newMovieLoadResponse(
                title,
                "${provider.mainUrl}/watch?v=${videoId}",
                TvType.Others,
                videoId
            ) {
                plot            = description
                posterUrl       = "${provider.mainUrl}/vi/${videoId}/hqdefault.jpg"
                recommendations = recommendedVideos.map { it.toSearchResponse(provider) }
                actors          = listOf(ActorData(Actor(
                    author,
                    if (authorThumbnails.isNotEmpty()) authorThumbnails.last().url else ""
                )))
            }
        }
    }

    private data class Thumbnail(val url: String)

    private fun parseQuality(quality: String): Int {
        return quality.replace("p", "").toIntOrNull() ?: Qualities.Unknown.value
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        Log.d(TAG, "Iniciando loadLinks para Video ID: $data. Usando API de Invidious para calidad.")

        val streamResponse = tryParseJson<VideoStreamsResponse>(
            app.get("${mainUrl}/api/v1/videos/${data}?fields=formatStreams").text
        )

        var hasDirectLinks = false

        streamResponse?.formatStreams?.let { streams ->
            streams.forEach { stream ->
                if (stream.container.contains("mp4", ignoreCase = true) || stream.container.contains("webm", ignoreCase = true) || stream.container.contains("hls", ignoreCase = true)) {

                    val linkName = "${stream.quality} (${stream.container})"

                    callback(
                        newExtractorLink(
                            source = name,
                            name = linkName,
                            url = stream.url
                        ) {
                            quality = parseQuality(stream.quality)
                            headers = mapOf("Referer" to mainUrl)
                        }
                    )
                    hasDirectLinks = true
                }
            }
        }

        if (hasDirectLinks) {
            Log.d(TAG, "Enlaces directos de stream de alta calidad cargados con éxito.")
        } else {
            Log.d(TAG, "No se encontraron enlaces directos de stream. Usando Extractor Estándar como fallback.")
            loadExtractor("https://youtube.com/watch?v=${data}", subtitleCallback, callback)
        }

        return true
    }
}