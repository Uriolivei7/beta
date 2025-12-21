package it.dogior.example

import android.content.SharedPreferences
import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
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
        val DTAG = "YT_DASH_DEBUG"
        Log.d(DTAG, "--- Iniciando extracción para: $data ---")

        return try {
            val service = org.schabi.newpipe.extractor.ServiceList.YouTube
            val extractor = service.getStreamExtractor(data)
            extractor.fetchPage()

            val referer = extractor.url ?: data

            // 1. LOG DE AUDIO
            val audioStream = extractor.audioStreams?.maxByOrNull { it.bitrate }
            val audioUrl = audioStream?.url?.replace("&", "&amp;")
            Log.d(DTAG, "Audio URL obtenido: ${if (audioUrl != null) "SÍ" else "NO"}")

            if (audioUrl != null) {
                val videoVariants = extractor.videoOnlyStreams?.distinctBy { it.resolution } ?: emptyList()
                Log.d(DTAG, "Variantes de video encontradas: ${videoVariants.size}")

                val videoRepresentations = videoVariants.mapIndexed { index, video ->
                    val res = video.resolution?.removeSuffix("p") ?: "unknown"
                    val vUrl = video.url?.replace("&", "&amp;")
                    Log.d(DTAG, "Añadiendo variante al DASH: $res ($index)")

                    """
                    <Representation id="video_$res" bandwidth="${video.bitrate}" codecs="avc1.640028" width="1280" height="${if(res == "unknown") 720 else res}">
                        <BaseURL>$vUrl</BaseURL>
                    </Representation>
                    """.trimIndent()
                }.joinToString("\n")

                val dashTemplate = """
                    <?xml version="1.0" encoding="utf-8"?>
                    <MPD xmlns="urn:mpeg:dash:schema:mpd:2011" profiles="urn:mpeg:dash:profile:isoff-on-demand:2011" type="static" minBufferTime="PT1.5S">
                      <Period duration="PT${extractor.length}S">
                        <AdaptationSet mimeType="video/mp4" subsegmentAlignment="true">
                          $videoRepresentations
                        </AdaptationSet>
                        <AdaptationSet mimeType="audio/mp4" subsegmentAlignment="true">
                          <Representation id="audio" bandwidth="${audioStream.bitrate}" codecs="mp4a.40.2">
                            <BaseURL>$audioUrl</BaseURL>
                          </Representation>
                        </AdaptationSet>
                      </Period>
                    </MPD>
                """.trimIndent()

                Log.d(DTAG, "XML DASH GENERADO:\n$dashTemplate")

                val dashData = "data:application/dash+xml;base64," +
                        android.util.Base64.encodeToString(dashTemplate.toByteArray(), android.util.Base64.NO_WRAP)

                callback.invoke(
                    newExtractorLink(this.name, "YouTube Multi-Calidad (HD)", dashData, com.lagradost.cloudstream3.utils.ExtractorLinkType.DASH) {
                        this.referer = referer
                        this.quality = Qualities.P1080.value
                    }
                )
            }

            extractor.videoStreams?.forEach { stream ->
                callback.invoke(
                    newExtractorLink(this.name, "YouTube ${stream.resolution} (Respaldo)",
                        stream.url!!, com.lagradost.cloudstream3.utils.ExtractorLinkType.VIDEO) {
                        this.referer = referer
                        this.quality = 360
                    }
                )
            }

            true
        } catch (e: Exception) {
            Log.e(DTAG, "CRASH en loadLinks: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}