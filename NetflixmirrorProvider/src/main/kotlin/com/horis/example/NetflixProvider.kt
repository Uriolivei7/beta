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
    private var newUrl = "https://net52.cc"
    override var name = "Netflix"

    override val hasMainPage = true
    private var cookie_value = ""

    private val TAG = "NetflixProvider"
    private val headers = mapOf(
        "X-Requested-With" to "XMLHttpRequest"
    )

    companion object {
        private var interceptorInstalled = false
    }

    init {
        if (!interceptorInstalled) {
            try {
                val currentClient = app.baseClient
                val newClient = currentClient.newBuilder()
                    .addInterceptor(NetflixInterceptor())
                    .build()

                app.baseClient = newClient
                interceptorInstalled = true
                Log.i(TAG, "✅ Interceptor HTTP instalado correctamente")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error instalando interceptor: ${e.message}")
            }
        }
    }

    private class NetflixInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val url = request.url.toString()

            if (url.contains("net51.cc") ||
                url.contains("net52.cc") ||
                url.contains("freecdn4.top") ||
                url.contains("nm-cdn6.top") ||
                url.endsWith(".m3u8") ||
                url.endsWith(".ts")) {

                val newRequest = request.newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .header("Referer", "https://net51.cc/")
                    .header("Origin", "https://net51.cc")
                    .header("Accept", "*/*")
                    .build()

                Log.d("NetflixInterceptor", "🔧 Headers agregados a: ${url.substringAfterLast("/").take(50)}")
                return chain.proceed(newRequest)
            }

            return chain.proceed(request)
        }
    }

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

        val playlistUrl = "$newUrl/tv/playlist.php?id=$id&t=$title&tm=${APIHolder.unixTime}"
        Log.d(TAG, "Solicitando Playlist: $playlistUrl")

        val res = try {
            app.get(
                playlistUrl,
                headers = headers,
                referer = "$newUrl/home",
                cookies = mapOf("t_hash_t" to cookie_value, "ott" to "nf", "hd" to "on"),
                timeout = 15
            )
        } catch (e: Exception) {
            Log.e(TAG, "ERROR DE RED: ${e.message}")
            return false
        }

        val playlist = try {
            res.parsed<PlayList>()
        } catch (e: Exception) {
            Log.e(TAG, "ERROR DE PARSEO JSON: ${e.message}")
            return false
        }

        var linksFound = 0
        playlist.forEach { item ->
            item.sources.forEach { source ->
                val rawFile = source.file ?: return@forEach
                val finalUrl = if (rawFile.startsWith("http")) rawFile else "$newUrl${rawFile.replace("/tv/", "/")}"

                Log.d(TAG, "Evaluando Source: ${source.label} | URL: $finalUrl")

                try {
                    callback.invoke(
                        newExtractorLink(
                            source = this.name,
                            name = "${this.name} ${source.label ?: "HLS"}",
                            url = finalUrl,
                            type = ExtractorLinkType.M3U8
                        ) {
                            this.referer = "$newUrl/"
                            this.quality = getQualityFromName(source.label ?: "")
                            this.headers = mapOf(
                                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
                                "Referer" to "$newUrl/",
                                "Origin" to "https://net51.cc",
                                "Accept" to "*/*"
                            )
                        }
                    )
                    linksFound++
                    Log.i(TAG, "✅ Enlace enviado: ${source.label}")
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error al crear link: ${e.message}")
                }
            }

            // Subtítulos
            item.tracks?.filter { it.kind == "captions" }?.forEach { track ->
                val subUrl = httpsify(track.file.toString())
                Log.d(TAG, "Subtítulo: ${track.label} -> $subUrl")
                subtitleCallback.invoke(newSubtitleFile(track.label.toString(), subUrl))
            }
        }

        Log.i(TAG, "✅ Total links cargados: $linksFound")
        return linksFound > 0
    }

    data class Id(val id: String)
    data class LoadData(val title: String, val id: String)
}