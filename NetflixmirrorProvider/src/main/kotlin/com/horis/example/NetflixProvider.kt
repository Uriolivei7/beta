package com.horis.example

import com.horis.example.entities.EpisodesData
import com.horis.example.entities.PlayList
import com.horis.example.entities.PostData
import com.horis.example.entities.SearchData
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import okhttp3.Interceptor
import okhttp3.Response
import org.jsoup.nodes.Element
import android.util.Log

class NetflixProvider : MainAPI() {
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries, TvType.Anime)
    override var lang = "en"
    override var mainUrl = "https://net20.cc"
    private var newUrl = "https://net52.cc" 
    override var name = "Netflix"

    override val hasMainPage = true
    private var cookie_value = ""

    private val headers = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8"
    )

    private val TAG = "NetflixProvider"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.i(TAG, "getMainPage: Iniciando carga")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value

        val homeUrl = "$newUrl/tv/home"
        val res = app.get(homeUrl, headers = headers, cookies = mapOf("t_hash_t" to cookie_value))

        Log.d(TAG, "getMainPage: URL: ${res.url} | Código: ${res.code}")

        val document = res.document
        val items = document.select(".tray-container, .post-list, #top10").mapNotNull {
            it.toHomePageList()
        }

        return newHomePageResponse(items, false)
    }

    private fun Element.toHomePageList(): HomePageList? {
        val name = this.select("h2, span").first()?.text() ?: "Trending"
        val items = this.select("article, .top10-post, .post").mapNotNull { it.toSearchResult() }
        return if (items.isEmpty()) null else HomePageList(name, items)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val id = this.attr("data-post").takeIf { it.isNotEmpty() }
            ?: this.select("a").attr("href").substringAfter("id=").substringBefore("&")

        if (id.isEmpty()) return null

        val title = this.select("img").attr("alt").takeIf { it.isNotEmpty() } ?: this.select(".title").text()

        return newAnimeSearchResponse(title, Id(id).toJson()) {
            this.posterUrl = "https://imgcdn.kim/poster/v/${id}.jpg"
            posterHeaders = mapOf("Referer" to "$newUrl/")
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.i(TAG, "load: Cargando ID: $url")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val id = parseJson<Id>(url).id

        val postUrl = "$newUrl/post.php?id=$id&t=${APIHolder.unixTime}"
        val res = app.get(postUrl, headers, cookies = mapOf("t_hash_t" to cookie_value, "ott" to "nf"))

        val data = res.parsed<PostData>()
        val episodes = arrayListOf<Episode>()
        val title = data.title ?: ""
        val isMovie = data.type == "m" || data.episodes.isNullOrEmpty()

        if (isMovie) {
            episodes.add(newEpisode(LoadData(title, id)) { this.name = title })
        } else {
            data.episodes?.forEach { it ->
                if (it != null) {
                    episodes.add(newEpisode(LoadData(title, it.id ?: "")) {
                        this.name = it.t
                        this.episode = it.ep?.replace("E", "")?.toIntOrNull()
                        this.season = it.s?.replace("S", "")?.toIntOrNull()
                        this.posterUrl = "https://imgcdn.kim/epimg/150/${it.id}.jpg"
                    })
                }
            }

            data.season?.forEach { s ->
                Log.d(TAG, "load: Detectada temporada ID: ${s.id}")
                if (s.id != id) {
                    episodes.addAll(getEpisodes(title, id, s.id, 1))
                }
            }

            if (data.nextPageShow == 1 && data.nextPageSeason != null) {
                episodes.addAll(getEpisodes(title, id, data.nextPageSeason, 2))
            }
        }

        return newTvSeriesLoadResponse(title, url, if(isMovie) TvType.Movie else TvType.TvSeries, episodes.distinctBy { it.data }) {
            this.posterUrl = "https://imgcdn.kim/poster/v/$id.jpg"
            this.backgroundPosterUrl = "https://imgcdn.kim/poster/h/$id.jpg"
            this.plot = data.desc
            this.year = data.year?.toIntOrNull()
            this.tags = data.genre?.split(",")?.map { it.trim() }
            this.recommendations = data.suggest?.map {
                newAnimeSearchResponse("", Id(it.id ?: "").toJson()) {
                    this.posterUrl = "https://imgcdn.kim/poster/v/${it.id}.jpg"
                }
            }
        }
    }

    private suspend fun getEpisodes(title: String, eid: String, sid: String, page: Int): List<Episode> {
        val eps = arrayListOf<Episode>()
        var pg = page
        while (true) {
            val epUrl = "$newUrl/episodes.php?s=$sid&series=$eid&t=${APIHolder.unixTime}&page=$pg"
            val res = app.get(epUrl, headers, cookies = mapOf("t_hash_t" to cookie_value))
            val data = res.parsed<EpisodesData>()

            if (data.episodes.isNullOrEmpty()) break

            data.episodes.forEach { it ->
                if (it != null) {
                    eps.add(newEpisode(LoadData(title, it.id ?: "")) {
                        this.name = it.t
                        this.episode = it.ep?.replace("E", "")?.toIntOrNull()
                        this.season = it.s?.replace("S", "")?.toIntOrNull()
                        this.posterUrl = "https://imgcdn.kim/epimg/150/${it.id}.jpg"
                    })
                }
            }
            if (data.nextPageShow == 0 || data.nextPageShow == null) break
            pg++
        }
        return eps
    }

    data class Id(val id: String)
    data class LoadData(val title: String, val id: String)
}