package com.horis.example

import com.horis.example.NetflixProvider.LoadData
import com.horis.example.entities.EpisodesData
import com.horis.example.entities.PlayList
import com.horis.example.entities.PostData
import com.horis.example.entities.SearchData
import com.horis.example.entities.MainPage
import com.horis.example.entities.PostCategory
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
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
    private val TAG = "PrimeVideoProvider"

    private val headers = mapOf("X-Requested-With" to "XMLHttpRequest")

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.i(TAG, "getMainPage: Cargando Home")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val cookies = mapOf("t_hash_t" to cookie_value, "ott" to "pv", "hd" to "on")

        val data = app.get("$mainUrl/tv/pv/homepage.php", cookies = cookies, referer = "$mainUrl/home").parsed<MainPage>()
        val items = data.post.map { it.toHomePageList() }

        return newHomePageResponse(items, false)
    }

    private fun PostCategory.toHomePageList(): HomePageList {
        val items = ids.split(",").mapNotNull { toSearchResult(it) }
        return HomePageList(cate ?: "Trending", items, isHorizontalImages = false)
    }

    private fun toSearchResult(id: String): SearchResponse {
        return newAnimeSearchResponse("", Id(id).toJson()) {
            this.posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/$id.jpg&w=500"
            posterHeaders = mapOf("Referer" to "$mainUrl/home")
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.i(TAG, "search: Buscando -> $query")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val url = "$mainUrl/pv/search.php?s=$query&t=${APIHolder.unixTime}"
        val data = app.get(url, cookies = mapOf("t_hash_t" to cookie_value, "ott" to "pv")).parsed<SearchData>()

        return data.searchResult.map {
            newAnimeSearchResponse(it.t ?: "", Id(it.id ?: "").toJson()) {
                posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/${it.id}.jpg&w=500"
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        val id = parseJson<Id>(url).id
        Log.i(TAG, "load: Cargando contenido ID -> $id")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value

        val data = app.get(
            "$mainUrl/pv/post.php?id=$id&t=${APIHolder.unixTime}",
            headers,
            cookies = mapOf("t_hash_t" to cookie_value, "ott" to "pv")
        ).parsed<PostData>()

        val episodes = arrayListOf<Episode>()
        val title = data.title ?: ""

        if (data.episodes.isNullOrEmpty() || data.episodes.first() == null) {
            episodes.add(newEpisode(LoadData(title, id)) { name = title })
        } else {
            data.episodes?.filterNotNull()?.forEach { it ->
                episodes.add(newEpisode(LoadData(title, it.id ?: "")) {
                    this.name = it.t
                    this.episode = it.ep?.replace("E", "")?.toIntOrNull()
                    this.season = it.s?.replace("S", "")?.toIntOrNull()
                    this.posterUrl = "https://imgcdn.kim/epimg/150/${it.id}.jpg"
                    this.runTime = it.time?.replace("m", "")?.toIntOrNull()
                })
            }

            if (data.nextPageShow == 1 && data.nextPageSeason != null) {
                episodes.addAll(getEpisodes(title, id, data.nextPageSeason!!, 2))
            }

            data.season?.dropLast(1)?.forEach { s ->
                if (s.id != id) episodes.addAll(getEpisodes(title, id, s.id, 1))
            }
        }

        val type = if (episodes.size <= 1) TvType.Movie else TvType.TvSeries

        return newTvSeriesLoadResponse(title, url, type, episodes.distinctBy { it.data }) {
            posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/$id.jpg&w=500"
            backgroundPosterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/h/$id.jpg&w=500"
            plot = data.desc
            year = data.year?.toIntOrNull()
            tags = data.genre?.split(",")?.map { it.trim() }
            recommendations = data.suggest?.map {
                newAnimeSearchResponse("", Id(it.id ?: "").toJson()) {
                    this.posterUrl = "https://wsrv.nl/?url=https://imgcdn.kim/pv/v/${it.id}.jpg&w=500"
                }
            }
        }
    }

    private suspend fun getEpisodes(title: String, eid: String, sid: String, page: Int): List<Episode> {
        val episodes = arrayListOf<Episode>()
        var pg = page
        while (true) {
            val res = app.get("$mainUrl/pv/episodes.php?s=$sid&series=$eid&t=${APIHolder.unixTime}&page=$pg", headers, cookies = mapOf("t_hash_t" to cookie_value, "ott" to "pv"))
            val data = res.parsed<EpisodesData>()
            data.episodes?.filterNotNull()?.forEach { it ->
                episodes.add(newEpisode(LoadData(title, it.id ?: "")) {
                    this.name = it.t
                    this.episode = it.ep?.replace("E", "")?.toIntOrNull()
                    this.season = it.s?.replace("S", "")?.toIntOrNull()
                    this.posterUrl = "https://imgcdn.kim/epimg/150/${it.id}.jpg"
                })
            }
            if (data.nextPageShow == 0 || data.nextPageShow == null) break
            pg++
        }
        return episodes
    }

    override suspend fun loadLinks(data: String, isCasting: Boolean, subtitleCallback: (SubtitleFile) -> Unit, callback: (ExtractorLink) -> Unit): Boolean {
        val (title, id) = parseJson<LoadData>(data)
        Log.i(TAG, "loadLinks: Extrayendo enlaces para $title")

        val playlist = app.get(
            "$newUrl/tv/pv/playlist.php?id=$id&t=$title&tm=${APIHolder.unixTime}",
            headers,
            cookies = mapOf("t_hash_t" to cookie_value, "ott" to "pv")
        ).parsed<PlayList>()

        playlist.forEach { item ->
            item.sources.forEach {
                val linkName = "${this.name} ${it.label ?: "HLS"}"

                callback.invoke(
                    newExtractorLink(this.name, linkName, """$newUrl${it.file?.replace("/tv/", "/") ?: ""}""", type = ExtractorLinkType.M3U8) {
                        this.referer = "$newUrl/"
                        this.headers = mapOf("User-Agent" to "Mozilla/5.0 (Android) ExoPlayer", "Cookie" to "hd=on")
                        this.quality = getQualityFromName(it.file?.substringAfter("q=", "")?.substringBefore("&") ?: "")
                    }
                )
            }

            item.tracks?.filter { it.kind == "captions" }?.forEach { track ->
                subtitleCallback.invoke(newSubtitleFile(track.label ?: "Sub", httpsify(track.file ?: "")))
            }
        }
        return true
    }

    data class Id(val id: String)
}