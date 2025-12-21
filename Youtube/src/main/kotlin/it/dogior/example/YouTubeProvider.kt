package it.dogior.example

import android.content.SharedPreferences
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import org.schabi.newpipe.extractor.NewPipe

class YoutubeProvider(
    language: String = "mx",
    private val sharedPrefs: SharedPreferences? = null
) : MainAPI() {

    override var mainUrl = MAIN_URL
    override var name = "YouTube"
    override val supportedTypes = setOf(TvType.Others)
    override val hasMainPage = true
    override var lang = language

    private val ytParser = YouTubeParser(this.name)

    companion object {
        const val MAIN_URL = "https://www.youtube.com"
        const val TAG = "Youtube"
    }

    init {
        try {
            NewPipe.init(NewPipeDownloader.getInstance())
        } catch (e: Exception) {
            Log.e(TAG, "Error inicializando NewPipe: ${e.message}")
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "Iniciando getMainPage para página: $page")
        val isTrendingEnabled = sharedPrefs?.getBoolean("trending", true) ?: true
        val sections = mutableListOf<HomePageList>()
        if (isTrendingEnabled) {
            val videos = ytParser.getTrendingVideoUrls(page)
            videos?.let { sections.add(it) }
        }

        val playlistsData = sharedPrefs?.getStringSet("playlists", emptySet()) ?: emptySet()
        if (playlistsData.isNotEmpty()) {
            val triples = playlistsData.map { parseJson<Triple<String, String, Long>>(it) }
            val list = triples.amap { data ->
                val playlistUrl = data.first
                val urlPath = playlistUrl.substringAfter("youtu").substringAfter("/")
                val isPlaylist = urlPath.startsWith("playlist?list=")
                val isChannel = urlPath.startsWith("@") || urlPath.startsWith("channel")
                val customSections = if (isPlaylist && !isChannel) {
                    ytParser.playlistToSearchResponseList(playlistUrl, page)
                } else if (!isPlaylist && isChannel) {
                    ytParser.channelToSearchResponseList(playlistUrl, page)
                } else null
                customSections to data.third
            }
            list.sortedBy { it.second }.forEach { it.first?.let { section -> sections.add(section) } }
        }

        if (sections.isEmpty()) {
            sections.add(HomePageList("All sections are disabled. Go to settings", emptyList()))
        }
        return newHomePageResponse(sections, true)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val allResults = ytParser.parseSearch(query)
        return allResults?.filterIsInstance<SearchResponse>() ?: emptyList()
    }

    override suspend fun load(url: String): LoadResponse {
        return ytParser.videoToLoadResponse(url)
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ): Boolean {
        val DTAG = "YT_AUDIO_DEBUG"
        Log.d(DTAG, "--- Iniciando extracción para: $data ---")

        return try {
            val service = org.schabi.newpipe.extractor.ServiceList.YouTube
            val extractor = service.getStreamExtractor(data)
            extractor.fetchPage()

            val referer = extractor.url ?: data

            val audioStream = extractor.audioStreams?.maxByOrNull { it.bitrate }
            val audioUrl = audioStream?.url
            if (audioUrl != null) {
                Log.d(DTAG, "AUDIO ENCONTRADO: Bitrate=${audioStream.bitrate}, Formato=${audioStream.content}")
            } else {
                Log.e(DTAG, "ERROR: No se encontró ninguna pista de audio en el extractor.")
            }

            val dashUrl = try { extractor.dashMpdUrl } catch (e: Exception) { null }
            if (!dashUrl.isNullOrEmpty()) {
                Log.d(DTAG, "DASH OFICIAL DISPONIBLE. Enviando...")
                callback.invoke(
                    newExtractorLink(this.name, "${this.name} (Auto HD)", dashUrl!!, ExtractorLinkType.DASH) {
                        this.referer = referer
                        this.quality = Qualities.P1080.value
                    }
                )
            } else {
                Log.w(DTAG, "DASH OFICIAL VACÍO. Intentando inyección manual de audio.")

                extractor.videoOnlyStreams?.distinctBy { it.resolution }?.forEach { video ->
                    val res = video.resolution ?: return@forEach
                    val qualInt = res.removeSuffix("p").toIntOrNull() ?: 0
                    val vUrl = video.url ?: return@forEach

                    if (qualInt >= 720) {
                        Log.d(DTAG, "Procesando $res: Inyectando audio extra...")
                        callback.invoke(
                            newExtractorLink(
                                this.name,
                                "${this.name} $res (HD + Audio)",
                                vUrl,
                                ExtractorLinkType.VIDEO
                            ) {
                                this.referer = referer
                                this.quality = qualInt
                                if (audioUrl != null) {
                                    this.headers = mapOf(
                                        "extra_audio_url" to audioUrl,
                                        "X-Audio-Url" to audioUrl,
                                        "User-Agent" to "com.google.android.youtube.tv/12.05.05 (Linux; U; Android 12; Build/STTE.221215.005)"
                                    )
                                }
                            }
                        )
                    }
                }
            }

            extractor.videoStreams?.forEach { stream ->
                Log.d(DTAG, "Cargando Muxed de respaldo: ${stream.resolution}")
                callback.invoke(
                    newExtractorLink(this.name, "${this.name} ${stream.resolution} (SD)", stream.url!!, ExtractorLinkType.VIDEO) {
                        this.referer = referer
                        this.quality = 360
                    }
                )
            }

            true
        } catch (e: Exception) {
            Log.e(DTAG, "CRASH en loadLinks: ${e.message}")
            false
        }
    }
}