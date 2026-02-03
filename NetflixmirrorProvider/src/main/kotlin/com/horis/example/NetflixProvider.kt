package com.horis.example

import android.util.Log
import com.horis.example.entities.EpisodesData
import com.horis.example.entities.PlayList
import com.horis.example.entities.PostData
import com.horis.example.entities.SearchData
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.AppUtils.tryParseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.httpsify
import com.lagradost.cloudstream3.utils.getQualityFromName
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import org.jsoup.nodes.Element
import com.lagradost.cloudstream3.APIHolder.unixTime

class NetflixProvider : MainAPI() {
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.AsianDrama
    )
    override var lang = "en"

    override var mainUrl = "https://net20.cc"
    private var newUrl = "https://net51.cc"
    override var name = "Netflix"

    override val hasMainPage = true
    private var cookie_value = ""

    private val TAG = "NetflixProvider"
    private val headers = mapOf(
        "X-Requested-With" to "XMLHttpRequest"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "user_token" to "233123f803cf02184bf6c67e149cdd50",
            "ott" to "nf",
            "hd" to "on"
        )
        val document = app.get(
            "$mainUrl/home",
            cookies = cookies,
            referer = "$mainUrl/",
        ).document
        val items = document.select(".lolomoRow").map {
            it.toHomePageList()
        }
        return newHomePageResponse(items, false)
    }

    private fun Element.toHomePageList(): HomePageList {
        val name = select("h2 > span > div").text()
        val items = select("img.lazy").mapNotNull {
            it.toSearchResult()
        }
        return HomePageList(name, items)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val id = attr("data-src").substringAfterLast("/").substringBefore(".")
        val posterUrl = "https://imgcdn.kim/poster/v/$id.jpg"

        return newAnimeSearchResponse("", Id(id).toJson()) {
            this.posterUrl = posterUrl
            posterHeaders = mapOf("Referer" to "$mainUrl/home")
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "hd" to "on",
            "ott" to "nf"
        )
        val url = "$mainUrl/search.php?s=$query&t=${APIHolder.unixTime}"
        val data = app.get(
            url,
            referer = "$mainUrl/tv/home",
            cookies = cookies
        ).parsed<SearchData>()

        return data.searchResult.map {
            newAnimeSearchResponse(it.t, Id(it.id).toJson()) {
                posterUrl = "https://imgcdn.kim/poster/v/${it.id}.jpg"
                posterHeaders = mapOf("Referer" to "$mainUrl/home")
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val id = parseJson<Id>(url).id
        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "ott" to "nf",
            "hd" to "on"
        )
        val data = app.get(
            "$mainUrl/post.php?id=$id&t=${APIHolder.unixTime}",
            headers,
            referer = "$mainUrl/tv/home",
            cookies = cookies
        ).parsed<PostData>()

        val episodes = arrayListOf<Episode>()

        val title = data.title
        val castList = data.cast?.split(",")?.map { it.trim() } ?: emptyList()
        val cast = castList.map {
            ActorData(
                Actor(it),
            )
        }
        val genre = data.genre?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
        val rating = data.match?.replace("IMDb ", "")
        val runTime = convertRuntimeToMinutes(data.runtime.toString())
        val suggest = data.suggest?.map {
            newAnimeSearchResponse("", Id(it.id).toJson()) {
                this.posterUrl = "https://imgcdn.kim/poster/v/${it.id}.jpg"
                posterHeaders = mapOf("Referer" to "$mainUrl/home")
            }
        }

        if (data.episodes.first() == null) {
            episodes.add(newEpisode(LoadData(title, id)) {
                name = data.title
            })
        } else {
            data.episodes?.filterNotNull()?.forEach { it ->
                episodes.add(newEpisode(LoadData(title ?: "", it.id ?: "")) {
                    this.name = it.t
                    this.episode = it.ep?.replace("E", "")?.toIntOrNull()
                    this.season = it.s?.replace("S", "")?.toIntOrNull()
                    this.posterUrl = "https://imgcdn.kim/epimg/150/${it.id}.jpg"
                    this.runTime = it.time?.replace("m", "")?.toIntOrNull()
                })
            }

            if (data.nextPageShow == 1) {
                episodes.addAll(getEpisodes(title, url, data.nextPageSeason!!, 2))
            }

            data.season?.dropLast(1)?.amap {
                episodes.addAll(getEpisodes(title, url, it.id, 1))
            }
        }

        val type = if (data.episodes.first() == null) TvType.Movie else TvType.TvSeries

        return newTvSeriesLoadResponse(title, url, type, episodes) {
            posterUrl = "https://imgcdn.kim/poster/v/$id.jpg"
            backgroundPosterUrl ="https://imgcdn.kim/poster/h/$id.jpg"
            posterHeaders = mapOf("Referer" to "$mainUrl/home")
            plot = data.desc
            year = data.year.toIntOrNull()
            tags = genre
            actors = cast
            this.score =  Score.from10(rating)
            this.duration = runTime
            this.contentRating = data.ua
            this.recommendations = suggest
        }
    }

    private suspend fun getEpisodes(
        title: String, eid: String, sid: String, page: Int
    ): List<Episode> {
        val episodes = arrayListOf<Episode>()
        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "ott" to "nf",
            "hd" to "on"
        )
        var pg = page
        while (true) {
            val data = app.get(
                "$mainUrl/episodes.php?s=$sid&series=$eid&t=${APIHolder.unixTime}&page=$pg",
                headers,
                referer = "$mainUrl/tv/home",
                cookies = cookies
            ).parsed<EpisodesData>()
            data.episodes?.forEach { it ->
                if (it != null) {
                    episodes.add(newEpisode(LoadData(title ?: "", it.id ?: "")) {
                        this.name = it.t
                        this.episode = it.ep?.replace("E", "")?.toIntOrNull()
                        this.season = it.s?.replace("S", "")?.toIntOrNull()
                        this.posterUrl = "https://imgcdn.kim/epimg/150/${it.id}.jpg"
                        this.runTime = it.time?.replace("m", "")?.toIntOrNull()
                    })
                }
            }
            if (data.nextPageShow == 0) break
            pg++
        }
        return episodes
    }

    init {
        val client = app.baseClient.newBuilder()
            .addInterceptor(NetflixInterceptor())
            .build()
        app.baseClient = client
    }

    private class NetflixInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val url = request.url.toString()

            if (url.contains("net51.cc") || url.contains("net52.cc") ||
                url.contains("nm-cdn6.top") || url.endsWith(".m3u8") || url.endsWith(".ts")) {

                val newRequest = request.newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .header("Referer", "https://net51.cc/")
                    .header("Origin", "https://net51.cc")
                    .header("Accept", "*/*")
                    .build()

                return chain.proceed(newRequest)
            }

            return chain.proceed(request)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "Cargando links con data: $data")

        val (title, id) = try {
            parseJson<LoadData>(data)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR: Fallo al parsear LoadData: ${e.message}")
            return false
        }

        Log.i(TAG, "Iniciando extracción para: $title (ID: $id)")

        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "ott" to "nf",
            "hd" to "on"
        )

        val playlistUrl = "$newUrl/tv/playlist.php?id=$id&t=$title&tm=${APIHolder.unixTime}"
        Log.d(TAG, "URL de Playlist generada: $playlistUrl")

        val res = try {
            app.get(playlistUrl, headers = headers, referer = "$newUrl/home", cookies = cookies, timeout = 15)
        } catch (e: Exception) {
            Log.e(TAG, "ERROR DE RED: ${e.message}")
            return false
        }

        Log.i(TAG, "Respuesta recibida: Código HTTP ${res.code} | URL Final: ${res.url}")

        if (res.code != 200 || res.text.isEmpty()) {
            Log.e(TAG, "ERROR: Respuesta inválida")
            return false
        }

        val playlist = try {
            res.parsed<PlayList>()
        } catch (e: Exception) {
            Log.e(TAG, "ERROR DE PARSEO: ${e.message}")
            return false
        }

        Log.d(TAG, "Playlist parseada. Encontrados ${playlist.size} items.")

        var linksFound = 0
        val extractor = NetflixExtractor()

        playlist.forEachIndexed { index, item ->
            Log.d(TAG, "Procesando Item #$index con ${item.sources.size} sources.")

            item.sources.forEach { source ->
                val rawFile = source.file ?: ""
                if (rawFile.isBlank()) return@forEach

                val finalUrl = if (rawFile.startsWith("http")) {
                    rawFile
                } else {
                    "$newUrl${rawFile.replace("/tv/", "/")}"
                }

                Log.d(TAG, "🔄 Procesando: ${source.label} -> $finalUrl")

                try {
                    val m3u8Response = app.get(
                        finalUrl,
                        headers = mapOf(
                            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                            "Referer" to "$newUrl/"
                        ),
                        timeout = 15
                    )

                    if (!m3u8Response.text.startsWith("#EXTM3U")) {
                        Log.e(TAG, "❌ No es un M3U8 válido")
                        return@forEach
                    }

                    Log.d(TAG, "✅ M3U8 descargado correctamente")

                    val lines = m3u8Response.text.lines()
                    var i = 0
                    while (i < lines.size) {
                        val line = lines[i].trim()

                        if (line.startsWith("#EXT-X-STREAM-INF:")) {
                            val resolution = Regex("RESOLUTION=(\\d+)x(\\d+)").find(line)?.groupValues?.get(2)?.toIntOrNull()

                            if (i + 1 < lines.size) {
                                val streamUrl = lines[i + 1].trim()

                                if (streamUrl.startsWith("http")) {
                                    extractor.getUrl(
                                        url = streamUrl,
                                        referer = "$newUrl/",
                                        subtitleCallback = subtitleCallback,
                                        callback = callback
                                    )

                                    linksFound++
                                    Log.i(TAG, "✅ Stream agregado [#$linksFound]: ${resolution}p -> $streamUrl")
                                }
                            }
                        }
                        i++
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error: ${e.message}")
                }
            }

            // Procesar subtítulos
            item.tracks?.filter { it.kind == "captions" }?.forEach { track ->
                val subUrl = httpsify(track.file.toString())
                Log.d(TAG, "SUBTÍTULO: [${track.label}] -> $subUrl")
                subtitleCallback.invoke(
                    SubtitleFile(track.label.toString(), subUrl)
                )
            }
        }

        Log.i(TAG, "Proceso finalizado. Total de links enviados al reproductor: $linksFound")
        return linksFound > 0
    }

    data class Id(
        val id: String
    )

    data class LoadData(
        val title: String, val id: String
    )
}

class NetflixExtractor : ExtractorApi() {
    override val name = "Netflix"
    override val mainUrl = "https://net51.cc"
    override val requiresReferer = true
    private val TAG = "NetflixProvider"

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        Log.i(TAG, "Iniciando Extracción en NetflixExtractor")
        Log.d(TAG, "URL Recibida: $url")
        Log.d(TAG, "Referer Recibido: $referer")

        try {
            if (url.isBlank()) {
                Log.e(TAG, "ERROR: La URL proporcionada al extractor está vacía.")
                return
            }

            val link = newExtractorLink(
                source = name,
                name = name,
                url = url,
                type = ExtractorLinkType.M3U8
            ) {
                this.referer = referer ?: "https://net51.cc/"
                this.quality = Qualities.Unknown.value

                this.headers = mapOf(
                    "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                    "Referer" to (referer ?: "https://net51.cc/"),
                    "Origin" to "https://net51.cc",
                    "Accept" to "*/*",
                    "Sec-Fetch-Mode" to "cors",
                    "Sec-Fetch-Site" to "cross-site"
                )
            }

            Log.i(TAG, "ExtractorLink creado exitosamente: ${link.name}")
            Log.d(TAG, "Headers configurados: ${link.headers}")

            callback.invoke(link)

        } catch (e: Exception) {
            Log.e(TAG, "ERROR CRÍTICO en getUrl de NetflixExtractor: ${e.message}")
            Log.e(TAG, "Trace: ", e)
        }
    }
}