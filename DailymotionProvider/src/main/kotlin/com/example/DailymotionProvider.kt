package com.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.api.Log
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.SearchResponseList
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieLoadResponse
import com.lagradost.cloudstream3.newMovieSearchResponse
import com.lagradost.cloudstream3.toNewSearchResponseList
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.StringUtils.encodeUri
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.loadExtractor
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONArray
import org.json.JSONObject

class DailymotionProvider : MainAPI() {

    data class VideoSearchResponse(
        @JsonProperty("list") val list: List<VideoItem>
    )

    data class VideoItem(
        @JsonProperty("id") val id: String,
        @JsonProperty("title") val title: String,
        @JsonProperty("thumbnail_360_url") val thumbnail360Url: String
    )

    data class VideoDetailResponse(
        @JsonProperty("id") val id: String,
        @JsonProperty("title") val title: String,
        @JsonProperty("description") val description: String,
        @JsonProperty("thumbnail_720_url") val thumbnail720Url: String
    )

    override var mainUrl = "https://api.dailymotion.com"
    override var name = "Dailymotion"
    override val supportedTypes = setOf(TvType.Others)

    override var lang = "en"

    override val hasMainPage = true

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val response = app.get("$mainUrl/videos?fields=id,title,thumbnail_360_url&limit=26&page=$page").text
        val popular = tryParseJson<VideoSearchResponse>(response)?.list ?: emptyList()

        return newHomePageResponse(
            listOf(
                HomePageList(
                    "Popular",
                    popular.map { it.toSearchResponse(this) },
                    true
                ),
            )
        )
    }

    override suspend fun search(query: String, page: Int): SearchResponseList? {
        val response = app.get("$mainUrl/videos?fields=id,title,thumbnail_360_url&limit=26&page=$page&search=${query.encodeUri()}").text
        val searchResults = tryParseJson<VideoSearchResponse>(response)?.list
        return searchResults?.map {
            it.toSearchResponse(this)
        }?.toNewSearchResponseList()
    }

    override suspend fun load(url: String): LoadResponse? {
        val videoId = Regex("dailymotion.com/video/([a-zA-Z0-9]+)").find(url)?.groups?.get(1)?.value
        val response = app.get("$mainUrl/video/$videoId?fields=id,title,description,thumbnail_720_url").text
        val videoDetail = tryParseJson<VideoDetailResponse>(response) ?: return null
        return videoDetail.toLoadResponse(this)
    }

    private fun VideoItem.toSearchResponse(provider: DailymotionProvider): SearchResponse {
        return provider.newMovieSearchResponse(
            this.title,
            "https://www.dailymotion.com/video/${this.id}",
            TvType.Movie
        ) {
            this.posterUrl = thumbnail360Url
        }
    }

    private suspend fun VideoDetailResponse.toLoadResponse(provider: DailymotionProvider): LoadResponse {
        return provider.newMovieLoadResponse(
            this.title,
            "https://www.dailymotion.com/video/${this.id}",
            TvType.Movie,
            this.id
        ) {
            plot = description
            posterUrl = thumbnail720Url
        }
    }

    private val chromeUa =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36"

    private fun videoIdOf(data: String): String? {
        val raw = data.trim()
        if (raw.isEmpty()) return null
        if (Regex("^[A-Za-z0-9]{4,}$").matches(raw)) return raw
        Regex("[?&]video=([A-Za-z0-9]{4,})").find(raw)?.groups?.get(1)?.value?.let { return it }
        Regex("dailymotion\\.com/(?:embed/)?video/([A-Za-z0-9]{4,})").find(raw)?.groups?.get(1)?.value?.let { return it }
        return null
    }

    private fun qualityOf(key: String): Int {
        val q = getQualityFromName(key)
        return if (q > 0) q else Qualities.Unknown.value
    }

    private suspend fun emit(
        label: String,
        url: String,
        type: ExtractorLinkType,
        quality: Int,
        referer: String,
        callback: (ExtractorLink) -> Unit
    ) {
        callback.invoke(
            newExtractorLink(
                this.name,
                label,
                url,
                type
            ) {
                this.referer = referer
                this.quality = quality
                this.headers = mapOf(
                    "User-Agent" to chromeUa,
                    "Referer" to referer,
                    "Origin" to "https://www.dailymotion.com"
                )
            }
        )
    }

    private fun parseSubtitles(root: JSONObject, subtitleCallback: (SubtitleFile) -> Unit) {
        val subtitles = root.optJSONObject("subtitles") ?: return
        val data = subtitles.opt("data") ?: return

        val entries = ArrayList<Pair<String?, JSONObject>>()
        when (data) {
            is JSONArray -> {
                for (i in 0 until data.length()) {
                    data.optJSONObject(i)?.let { entries.add(null to it) }
                }
            }
            is JSONObject -> {
                val keys = data.keys()
                while (keys.hasNext()) {
                    val key = keys.next()
                    data.optJSONObject(key)?.let { entries.add(key to it) }
                }
            }
        }

        for ((key, item) in entries) {
            val label = item.optString("label").takeIf { it.isNotBlank() }
                ?: item.optString("language").takeIf { it.isNotBlank() }
                ?: key
                ?: "Subtitle"
            val urls = ArrayList<String>()
            item.optJSONArray("urls")?.let { arr ->
                for (i in 0 until arr.length()) {
                    arr.optString(i).takeIf { it.startsWith("http") }?.let { urls.add(it) }
                }
            }
            item.optString("url").takeIf { it.startsWith("http") }?.let { urls.add(it) }
            for (u in urls.distinct()) {
                subtitleCallback.invoke(SubtitleFile(label, u))
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val videoId = videoIdOf(data)
        if (videoId == null) {
            Log.w(this.name, "loadLinks: id invalido '$data'")
            return false
        }

        val referer = "https://www.dailymotion.com/embed/video/$videoId"
        var emitted = 0
        val counted: (ExtractorLink) -> Unit = {
            emitted++
            callback.invoke(it)
        }

        val metaUrl = "https://www.dailymotion.com/player/metadata/video/$videoId"
        val body = try {
            app.get(
                metaUrl,
                headers = mapOf(
                    "User-Agent" to chromeUa,
                    "Referer" to referer,
                    "Origin" to "https://www.dailymotion.com"
                ),
                timeout = 20L
            ).text
        } catch (t: Throwable) {
            if (t is kotlinx.coroutines.CancellationException) throw t
            Log.e(this.name, "metadata $videoId fallo: ${t.message}")
            null
        }

        if (body != null) {
            val root = try {
                JSONObject(body)
            } catch (t: Throwable) {
                Log.e(this.name, "metadata $videoId no es JSON: ${body.take(120)}")
                null
            }

            if (root != null) {
                parseSubtitles(root, subtitleCallback)

                val qualities = root.optJSONObject("qualities")
                if (qualities == null) {
                    Log.w(this.name, "metadata $videoId sin qualities")
                } else {
                    val seen = HashSet<String>()
                    val keys = qualities.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        val value = qualities.opt(key)
                        val items = when (value) {
                            is JSONArray -> (0 until value.length()).mapNotNull { value.optJSONObject(it) }
                            is JSONObject -> listOf(value)
                            else -> emptyList()
                        }
                        for (item in items) {
                            val url = item.optString("url")
                            if (url.isBlank() || !url.startsWith("http")) continue
                            if (!seen.add(url)) continue
                            val type = item.optString("type")
                            if (url.contains(".m3u8")) {
                                emit("Dailymotion $key", url, ExtractorLinkType.M3U8, qualityOf(key), referer, counted)
                            } else if (url.contains(".mp4") || type.contains("mp4")) {
                                emit("Dailymotion $key", url, ExtractorLinkType.VIDEO, qualityOf(key), referer, counted)
                            }
                        }
                    }
                }
            }
        }

        if (emitted == 0) {
            Log.w(this.name, "sin links propios para $videoId, probando loadExtractor")
            try {
                loadExtractor(
                    "https://www.dailymotion.com/embed/video/$videoId",
                    subtitleCallback,
                    counted
                )
            } catch (t: Throwable) {
                if (t is kotlinx.coroutines.CancellationException) throw t
                Log.e(this.name, "loadExtractor fallo: ${t.message}")
            }
        }

        Log.d(this.name, "loadLinks $videoId -> $emitted links")
        return emitted > 0
    }
}
