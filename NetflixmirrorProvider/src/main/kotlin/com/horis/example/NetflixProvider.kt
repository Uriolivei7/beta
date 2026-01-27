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

    private val headers = mapOf(
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
        "Accept-Language" to "en-US,en;q=0.9",
        "Connection" to "keep-alive",
        "Sec-Ch-Ua" to "\"Not A(Brand\";v=\"8\", \"Chromium\";v=\"132\", \"Google Chrome\";v=\"132\"",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/132.0.0.0 Safari/537.36",
    )

    private val TAG = "NetflixProvider"


    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.i(TAG, "getMainPage: Iniciando carga")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val cookies = mapOf("t_hash_t" to cookie_value, "ott" to "nf", "hd" to "on")

        val res = app.get("$newUrl/mobile/home?app=1", headers = headers, cookies = cookies)
        Log.d(TAG, "getMainPage: URL final: ${res.url} | Código: ${res.code}")

        if (res.text.contains("403 Forbidden")) {
            Log.e(TAG, "getMainPage: Error 403 detectado en la Home. Cloudflare bloqueó la petición.")
        }

        val document = res.document
        val items = document.select(".tray-container, #top10").mapNotNull {
            it.toHomePageList()
        }

        Log.i(TAG, "getMainPage: Se cargaron ${items.size} listas")
        return newHomePageResponse(items, false)
    }

    private fun Element.toHomePageList(): HomePageList? {
        val name = this.select("h2, span").first()?.text() ?: "Sin Nombre"
        Log.d(TAG, "toHomePageList: Procesando sección -> $name")

        val items = this.select("article, .top10-post").mapNotNull {
            it.toSearchResult()
        }

        return if (items.isEmpty()) null else HomePageList(name, items)
    }

    private fun Element.toSearchResult(): SearchResponse? {
        val id = this.attr("data-post").takeIf { it.isNotEmpty() }
            ?: run {
                val imgSrc = this.selectFirst("img")?.attr("data-src") ?: this.selectFirst("img")?.attr("src") ?: ""
                imgSrc.substringAfterLast("/").substringBefore(".")
            }

        if (id.isEmpty()) return null

        val title = this.selectFirst("img")?.attr("alt") ?: "No Title"
        Log.v(TAG, "toSearchResult: Item encontrado -> $title (ID: $id)")

        return newAnimeSearchResponse(title, Id(id).toJson()) {
            this.posterUrl = "https://imgcdn.kim/poster/v/${id}.jpg"
            posterHeaders = mapOf("Referer" to "$newUrl/")
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.i(TAG, "search: Buscando query -> $query")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val url = "$mainUrl/search.php?s=$query&t=${APIHolder.unixTime}"

        val res = app.get(url, headers = headers, cookies = mapOf("t_hash_t" to cookie_value, "ott" to "nf"))
        Log.d(TAG, "search: Respuesta servidor -> ${res.code}")

        val data = res.parsed<SearchData>()
        return data.searchResult.map {
            newAnimeSearchResponse(it.t ?: "", Id(it.id ?: "").toJson()) {
                posterUrl = "https://imgcdn.kim/poster/v/${it.id}.jpg"
            }
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.i(TAG, "load: Cargando URL -> $url")
        cookie_value = if(cookie_value.isEmpty()) bypass(mainUrl) else cookie_value
        val id = parseJson<Id>(url).id

        val postUrl = "$newUrl/post.php?id=$id&t=${APIHolder.unixTime}"
        val res = app.get(postUrl, headers, cookies = mapOf("t_hash_t" to cookie_value, "ott" to "nf"))

        if (res.text.startsWith("<html>")) {
            Log.e(TAG, "load: Bloqueo de Cloudflare detectado (Recibido HTML en lugar de JSON)")
        }

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
                    })
                }
            }
        }

        return newTvSeriesLoadResponse(title, url, if(isMovie) TvType.Movie else TvType.TvSeries, episodes) {
            this.posterUrl = "https://imgcdn.kim/poster/v/$id.jpg"
            this.plot = data.desc
        }
    }

    data class Id(val id: String)
    data class LoadData(val title: String, val id: String)
}