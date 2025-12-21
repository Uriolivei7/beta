package it.dogior.example

import android.content.SharedPreferences
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.amap
import android.util.Log
import com.lagradost.cloudstream3.MovieSearchResponse
import com.lagradost.cloudstream3.newSubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class YoutubeProvider(
    language: String = "en",
    private val sharedPrefs: SharedPreferences? = null
) : MainAPI() {

    override var mainUrl = MAIN_URL
    override var name = "YouTube"  // Nombre del plugin
    override val supportedTypes = setOf(TvType.Others)
    override val hasMainPage = true
    override var lang = language

    private val ytParser = YouTubeParser(this.name)

    // Invidious instance fija (yewtu.be, estable en dic 2025)
    private val invidiousInstance = "https://yewtu.be"
    private val client = OkHttpClient()

    companion object {
        const val MAIN_URL = "https://www.youtube.com"
        const val TAG = "Youtube"
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "Iniciando getMainPage para página: $page")

        val isTrendingEnabled = sharedPrefs?.getBoolean("trending", true) ?: true
        val sections = mutableListOf<HomePageList>()
        if (isTrendingEnabled) {
            Log.d(TAG, "Obteniendo tendencias...")
            val videos = ytParser.getTrendingVideoUrls(page)
            if (videos == null) {
            Log.d(TAG, "Error: No se obtuvieron videos de tendencias en la página $page.")
        } else {
            Log.d(TAG, "Tendencias obtenidas. Total: ${videos.list.size} videos.")

        }
            videos?.let { sections.add(it) }
        }

        val playlistsData = sharedPrefs?.getStringSet("playlists", emptySet()) ?: emptySet()
        if (playlistsData.isNotEmpty()) {
            Log.d(TAG, "Cargando playlists y canales personalizados. Total de entradas: ${playlistsData.size}")
            val triples = playlistsData.map { parseJson<Triple<String, String, Long>>(it) }
            val list = triples.amap { data ->
            val playlistUrl = data.first
            Log.d(TAG, "Procesando URL: $playlistUrl")
            val urlPath = playlistUrl.substringAfter("youtu").substringAfter("/")
            val isPlaylist = urlPath.startsWith("playlist?list=")
            val isChannel = urlPath.startsWith("@") || urlPath.startsWith("channel")
            val customSections = if (isPlaylist && !isChannel) {
            Log.d(TAG, "Llamando a playlistToSearchResponseList para $playlistUrl")
            ytParser.playlistToSearchResponseList(playlistUrl, page)

        } else if (!isPlaylist && isChannel) {
            Log.d(TAG, "Llamando a channelToSearchResponseList para $playlistUrl")
            ytParser.channelToSearchResponseList(playlistUrl, page)

        } else {
            Log.d(TAG, "URL no reconocida (ni playlist ni canal): $playlistUrl")
            null

        }
            customSections to data.third

        }
            list.sortedBy { it.second }.forEach {
            val homepageSection = it.first
            if (homepageSection != null) {
            sections.add(homepageSection)

        }

        }


        }
        if (sections.isEmpty()) {
            Log.d(TAG, "No hay secciones activas. Se muestra mensaje de error en la UI.")
            sections.add(
            HomePageList(
            "All sections are disabled. Go to the settings to enable them",
            emptyList()
            )
            )

        }
        Log.d(TAG, "Finalizando getMainPage. Secciones totales: ${sections.size}")
        return newHomePageResponse(
        sections, true
        )

    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Iniciando búsqueda para: $query")

        val allResults = ytParser.parseSearch(query)

        Log.d(TAG, "Búsqueda terminada. Resultados encontrados: ${allResults?.size ?: 0}")

        return allResults?.filterIsInstance<MovieSearchResponse>() ?: emptyList()
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d(TAG, "Iniciando carga de LoadResponse para URL: $url")
        val video = ytParser.videoToLoadResponse(url)
        Log.d(TAG, "Carga de LoadResponse completada.")
        return video
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            // Extrae videoId de la URL (data es la URL de YouTube)
            val videoId = data.substringAfter("v=").substringBefore("&")

            // Request a Invidious API para formatos (calidades altas)
            val url = "$invidiousInstance/api/v1/videos/$videoId"
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute().use { it.body.string() }

            val json = JSONObject(response)

            val adaptiveFormats = json.optJSONArray("adaptiveFormats") ?: JSONArray()
            val formatStreams = json.optJSONArray("formatStreams") ?: JSONArray()

            val allFormats = JSONArray().apply {
                for (i in 0 until adaptiveFormats.length()) put(adaptiveFormats.opt(i))
                for (i in 0 until formatStreams.length()) put(formatStreams.opt(i))
            }

            Log.d(TAG, "Invidious - Formatos encontrados: ${allFormats.length()}")

            for (i in 0 until allFormats.length()) {
                val format = allFormats.optJSONObject(i) ?: continue
                val streamUrl = format.optString("url")
                if (streamUrl.isEmpty()) continue

                val qualityLabel = format.optString("qualityLabel", format.optString("quality", "Unknown"))
                val type = format.optString("type")

                // Filtrar solo formatos de video (opcional, puedes quitar si quieres audio también)
                if (!type.contains("video", ignoreCase = true)) continue

                val q = when {
                    qualityLabel.contains("2160", ignoreCase = true) || qualityLabel.contains("4k") -> Qualities.P2160.value
                    qualityLabel.contains("1440", ignoreCase = true) -> Qualities.P1440.value
                    qualityLabel.contains("1080", ignoreCase = true) -> Qualities.P1080.value
                    qualityLabel.contains("720", ignoreCase = true) -> Qualities.P720.value
                    qualityLabel.contains("480", ignoreCase = true) -> Qualities.P480.value
                    qualityLabel.contains("360", ignoreCase = true) -> Qualities.P360.value
                    else -> Qualities.Unknown.value
                }

                Log.d(TAG, "Calidad agregada: $qualityLabel -> $streamUrl")

                callback(
                    newExtractorLink(
                        source = this.name,
                        name = qualityLabel,
                        url = streamUrl
                    ) {
                        this.referer = invidiousInstance
                        this.quality = q
                    }
                )
            }

            // Subtítulos de Invidious
            val captions = json.optJSONArray("captions") ?: JSONArray()
            for (i in 0 until captions.length()) {
                val cap = captions.optJSONObject(i) ?: continue
                val label = cap.optString("label", "Unknown")
                val langCode = cap.optString("language_code")
                val capUrl = cap.optString("url")
                if (capUrl.isNotEmpty()) {
                    subtitleCallback(newSubtitleFile("$label ($langCode)", capUrl))
                }
            }

            return true

        } catch (e: Exception) {
            Log.e(TAG, "Error en loadLinks con Invidious: ${e.message}", e)
            return false
        }
    }

}