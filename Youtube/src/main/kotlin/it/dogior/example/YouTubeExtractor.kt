package it.dogior.example

import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.schemaStripRegex
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.ServiceList
import org.schabi.newpipe.extractor.services.youtube.extractors.YoutubeStreamExtractor
import org.schabi.newpipe.extractor.services.youtube.linkHandler.YoutubeStreamLinkHandlerFactory
import org.schabi.newpipe.extractor.stream.VideoStream
import android.util.Log
import com.lagradost.cloudstream3.newSubtitleFile

open class YouTubeExtractor() : ExtractorApi() {
    override val mainUrl = "https://www.youtube.com"
    override val requiresReferer = false
    override val name = "YouTube"

    companion object {
        const val PC_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36"
    }

    private fun mapResolutionToQuality(resolution: String): Qualities {
        return when (resolution.lowercase().trim()) {
            "2160p" -> Qualities.P2160
            "1440p" -> Qualities.P1440
            "1080p" -> Qualities.P1080
            "720p" -> Qualities.P720
            "480p" -> Qualities.P480
            "360p" -> Qualities.P360
            "240p" -> Qualities.P240
            "144p" -> Qualities.P144
            else -> Qualities.Unknown
        }
    }

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
    ) {
        try {
            val downloader = NewPipe.getDownloader()
            val fields = downloader.javaClass.declaredFields
            fields.find { it.name == "userAgent" }?.let {
                it.isAccessible = true
                it.set(downloader, PC_USER_AGENT)
            }
        } catch (e: Exception) {
            Log.e("YT_EXTRACTOR", "Error UserAgent: ${e.message}")
        }

        val link = YoutubeStreamLinkHandlerFactory.getInstance().fromUrl(
            url.replace(schemaStripRegex, "")
        )

        val extractor = object : YoutubeStreamExtractor(ServiceList.YouTube, link) {}

        try {
            extractor.fetchPage()
        } catch (e: Exception) {
            Log.e("YT_EXTRACTOR", "Error fetchPage: ${e.message}")
            return
        }

        val audioStream = extractor.audioStreams
            ?.filter { it.format?.name?.contains("m4a") == true || it.format?.name?.contains("mp4") == true }
            ?.maxByOrNull { it.averageBitrate }

        val videoStreams = extractor.videoStreams ?: emptyList()
        val videoOnlyStreams = extractor.videoOnlyStreams ?: emptyList()
        val allVideoStreams = videoStreams + videoOnlyStreams

        allVideoStreams.filterNotNull().forEach { stream ->
            val quality = mapResolutionToQuality(stream.resolution ?: "")
            if (quality != Qualities.Unknown) {

                val isVideoOnly = stream.isVideoOnly

                callback.invoke(
                    newExtractorLink(
                        source = this.name,
                        name = "${this.name} ${stream.resolution ?: ""}",
                        url = stream.url ?: return@forEach,
                        type = ExtractorLinkType.VIDEO
                    ) {
                        this.quality = quality.value
                        this.referer = url

                        if (isVideoOnly) {
                            this.extractorData = audioStream?.url
                        }
                    }
                )
            }
        }

        try {
            extractor.subtitlesDefault?.filterNotNull()?.forEach { sub ->
                subtitleCallback.invoke(
                    newSubtitleFile(sub.languageTag ?: "en", sub.url ?: return@forEach)
                )
            }
        } catch (e: Exception) {
            Log.e("YT_EXTRACTOR", "Error en subtítulos: ${e.message}")
        }
    }
}