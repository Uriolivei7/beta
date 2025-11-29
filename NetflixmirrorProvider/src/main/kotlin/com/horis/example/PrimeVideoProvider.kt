package com.horis.example

import com.horis.example.entities.EpisodesData
import com.horis.example.entities.PlayList
import com.horis.example.entities.PostData
import com.horis.example.entities.SearchData
import com.horis.example.entities.MainPage
import com.horis.example.entities.PostCategory
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.httpsify
import com.lagradost.cloudstream3.utils.getQualityFromName
import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class PrimeVideoProvider : MainAPI() {
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.AsianDrama
    )
    override var lang = "en"

    override var mainUrl = "https://net20.cc"
    private var newUrl = "https://net51.cc"
    override var name = "PrimeVideo"

    override val hasMainPage = true
    private var cookie_value = ""
    private val headers = mapOf(
        "X-Requested-With" to "XMLHttpRequest"
    )

    private val TAG = "PrimeVideo"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.i(TAG, "Starting getMainPage (Page: $page)")

        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        Log.i(TAG, "Cookie value after bypass: $cookie_value")

        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "ott" to "pv",
            "hd" to "on"
        )
        val homeUrl = "$mainUrl/tv/pv/homepage.php"
        Log.i(TAG, "Fetching home page data from: $homeUrl")

        val data = try {
            app.get(
                homeUrl,
                cookies = cookies,
                referer = "$mainUrl/home",
            ).parsed<MainPage>()
        } catch (e: Exception) {
            Log.e(TAG, "getMainPage FAILED (Possible Timeout): ${e.message}", e)
            return null
        }


        val items = data.post.map {
            it.toHomePageList()
        }

        Log.i(TAG, "Found ${items.size} homepage lists.")
        Log.i(TAG, "Finished getMainPage.")

        return newHomePageResponse(items, false)
    }

    private fun PostCategory.toHomePageList(): HomePageList {
        val name = cate
        val idsList = ids.split(",")
        Log.i(TAG, "Processing list '$name'. Total IDs: ${idsList.size}")

        val items = idsList.mapNotNull {
            toSearchResult(it)
        }
        Log.i(TAG, "List '$name' extracted ${items.size} results.")
        return HomePageList(
            name,
            items,
            isHorizontalImages = false
        )
    }

    private fun toSearchResult(id: String): SearchResponse? {
        return newAnimeSearchResponse("", Id(id).toJson()) {
            this.posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/$id.jpg&w=500"
            posterHeaders = mapOf("Referer" to "$mainUrl/home")
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.i(TAG, "Starting search for query: $query")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "ott" to "pv",
            "hd" to "on"
        )
        val url = "$mainUrl/pv/search.php?s=$query&t=${APIHolder.unixTime}"
        Log.i(TAG, "Search URL: $url")

        val data = try {
            app.get(url, referer = "$mainUrl/home", cookies = cookies).parsed<SearchData>()
        } catch (e: Exception) {
            Log.e(TAG, "Search FAILED for query '$query': ${e.message}", e)
            return emptyList()
        }


        Log.i(TAG, "Search query '$query' returned ${data.searchResult.size} results.")

        return data.searchResult.map {
            newAnimeSearchResponse(it.t, Id(it.id).toJson()) {
                posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/${it.id}.jpg&w=500"
                posterHeaders = mapOf("Referer" to "$mainUrl/home")
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.i(TAG, "Starting load for URL: $url")
        val id = parseJson<Id>(url).id
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        Log.i(TAG, "Extracted ID: $id. Cookie: $cookie_value")

        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "ott" to "pv",
            "hd" to "on"
        )
        val postUrl = "$mainUrl/pv/post.php?id=$id&t=${APIHolder.unixTime}"
        Log.i(TAG, "Fetching post data from: $postUrl")

        val data = try {
            app.get(
                postUrl,
                headers,
                referer = "$mainUrl/tv/home",
                cookies = cookies
            ).parsed<PostData>()
        } catch (e: Exception) {
            Log.e(TAG, "Load FAILED for ID $id (Possible Timeout): ${e.message}", e)
            return null
        }

        val episodes = arrayListOf<Episode>()

        val title = data.title
        val typeCheck = if (data.episodes.first() == null) "Movie" else "Series"
        Log.i(TAG, "Title loaded: $title, Type check: $typeCheck")

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
                this.posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/${it.id}.jpg&w=500"
                posterHeaders = mapOf("Referer" to "$mainUrl/tv/home")
            }
        }

        if (data.episodes.first() == null) {
            episodes.add(newEpisode(LoadData(title, id)) {
                name = data.title
            })
            Log.i(TAG, "Added 1 episode for Movie type.")
        } else {
            data.episodes.filterNotNull().mapTo(episodes) {
                newEpisode(LoadData(title, it.id)) {
                    name = it.t
                    episode = it.ep.replace("E", "").toIntOrNull()
                    season = it.s.replace("S", "").toIntOrNull()
                    this.posterUrl = "https://imgcdn.kim/pvepimg/150/${it.id}.jpg"
                    this.runTime = it.time.replace("m", "").toIntOrNull()
                    Log.d(TAG, "Initial Episode: ${it.s}${it.ep} - ${it.t}")
                }
            }
            Log.i(TAG, "Added ${data.episodes.filterNotNull().size} episodes from initial post data.")


            if (data.nextPageShow == 1) {
                Log.i(TAG, "Next page indicated. Fetching next page episodes for season: ${data.nextPageSeason}")
                episodes.addAll(getEpisodes(title, url, data.nextPageSeason!!, 2))
            }

            data.season?.dropLast(1)?.amap {
                Log.i(TAG, "Fetching episodes for subsequent season ID: ${it.id}")
                episodes.addAll(getEpisodes(title, url, it.id, 1))
            }
            Log.i(TAG, "Total episodes collected: ${episodes.size}")
        }

        val type = if (data.episodes.first() == null) TvType.Movie else TvType.TvSeries

        Log.i(TAG, "Final Load Response metadata: Year=${data.year}, Rating=${rating}, Tags=${genre?.size}")

        return newTvSeriesLoadResponse(title, url, type, episodes) {
            posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/$id.jpg&w=500"
            backgroundPosterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/h/$id.jpg&w=500"
            posterHeaders = mapOf("Referer" to "$mainUrl/home")
            plot = data.desc
            year = data.year.toIntOrNull()
            tags = genre
            actors = cast
            //this.rating = (rating?.toDoubleOrNull()?.times(10.0))?.toInt()
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
            "ott" to "pv",
            "hd" to "on"
        )
        var pg = page
        while (true) {
            val epUrl = "$mainUrl/pv/episodes.php?s=$sid&series=$eid&t=${APIHolder.unixTime}&page=$pg"
            Log.i(TAG, "Fetching episodes page $pg for SID: $sid. URL: $epUrl")

            val data = try {
                app.get(
                    epUrl,
                    headers,
                    referer = "$mainUrl/home",
                    cookies = cookies
                ).parsed<EpisodesData>()
            } catch (e: Exception) {
                Log.e(TAG, "getEpisodes FAILED for page $pg (SID $sid): ${e.message}")
                break
            }


            val newEpsCount = data.episodes?.size ?: 0
            Log.i(TAG, "Fetched $newEpsCount episodes from page $pg.")

            data.episodes?.mapTo(episodes) {
                newEpisode(LoadData(title, it.id)) {
                    name = it.t
                    episode = it.ep.replace("E", "").toIntOrNull()
                    season = it.s.replace("S", "").toIntOrNull()
                    this.posterUrl = "https://img.nfmirrorcdn.top/pvepimg/${it.id}.jpg"
                    this.runTime = it.time.replace("m", "").toIntOrNull()
                    Log.v(TAG, "Added Ep: ${it.s}${it.ep} - ${it.t}")
                }
            }
            if (data.nextPageShow == 0) {
                Log.i(TAG, "Next page show is 0. Stopping pagination for SID: $sid")
                break
            }
            pg++
        }
        Log.i(TAG, "Finished fetching episodes for SID: $sid. Total: ${episodes.size}")
        return episodes
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val (title, id) = parseJson<LoadData>(data)
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val cookies = mapOf(
            "t_hash_t" to cookie_value,
            "ott" to "pv",
            "hd" to "on"
        )
        val playlist = app.get(
            "$newUrl/pv/playlist.php?id=$id&t=$title&tm=${APIHolder.unixTime}",
            headers,
            referer = "$newUrl/home",
            cookies = cookies
        ).parsed<PlayList>()

        val requiredReferer = "$newUrl/"

        playlist.forEach { item ->
            item.sources.forEach {
                callback.invoke(
                    newExtractorLink(
                        name,
                        it.label,
                        """$newUrl${it.file.replace("/tv/", "/")}""",
                        type = ExtractorLinkType.M3U8
                    ) {
                        this.referer = requiredReferer
                        this.quality = getQualityFromName(it.file.substringAfter("q=", ""))
                    }
                )
            }

            val requiredReferer = "$newUrl/"
            val subtitleHeaders = mapOf("Referer" to requiredReferer)

            item.tracks?.filter { it.kind == "captions" }?.map { track ->
                subtitleCallback.invoke(
                    SubtitleFile(
                        lang = lang,
                        url = httpsify(track.file.toString())
                    )
                )
            }
        }

        return true
    }

    @Suppress("ObjectLiteralToLambda")
    override fun getVideoInterceptor(extractorLink: ExtractorLink): Interceptor? {
        val requiredReferer = "$newUrl/"

        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val urlString = request.url.toString()

                if (urlString.contains(".m3u8")) {
                    val newRequest = request.newBuilder()
                        .header("Referer", requiredReferer)
                        .header("Cookie", "hd=on")
                        .build()
                    return chain.proceed(newRequest)
                }

                if (urlString.contains(".vtt") ||
                    urlString.contains(".srt") ||
                    urlString.contains("nfmirrorcdn.top")) {

                    val newRequest = request.newBuilder()
                        .header("Referer", requiredReferer)
                        .build()
                    return chain.proceed(newRequest)
                }

                return chain.proceed(request)
            }
        }
    }

    data class Id(
        val id: String
    )

    data class LoadData(
        val title: String, val id: String
    )

    data class Cookie(
        val cookie: String
    )
}