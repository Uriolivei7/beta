package com.example

import com.lagradost.cloudstream3.DubStatus
import com.lagradost.cloudstream3.HomePageList
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.LoadResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.ShowStatus
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.addEpisodes
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newAnimeLoadResponse
import com.lagradost.cloudstream3.newEpisode
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.newMovieSearchResponse
import com.lagradost.cloudstream3.utils.AppUtils.toJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.newExtractorLink
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AnizoneProvider : MainAPI() {

    override var mainUrl = "https://anizone.to"
    override var name = "AniZone"
    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
    )

    override var lang = "en"
    override val hasMainPage = true
    override val hasQuickSearch = true
    override val hasDownloadSupport = true

    override val mainPage = mainPageOf(
        "a-2" to "Últimos Animes",
        "a-4" to "Últimas Películas",
        "a-6" to "Recientes",
        "e-list" to "Últimos Episodios"
    )

    private var cookies = mutableMapOf<String, String>()
    private var wireData = mutableMapOf(
        "wireSnapshot" to "",
        "token" to ""
    )

    private suspend fun initializeLiveWire(): Boolean {
        if (!wireData["wireSnapshot"].isNullOrBlank()) return true

        try {
            val initReq = app.get("$mainUrl/anime")
            val doc = initReq.document

            val csrfToken = doc.select("script[data-csrf]").attr("data-csrf")
            val snapshot = getSnapshot(doc)

            if (csrfToken.isBlank() || snapshot.isBlank()) {
                Log.e("AniZone Init", "Fallo la inicialización: token o snapshot vacíos. Esto puede ser un error de HTML/CAPTCHA en la página inicial.")
                return false
            }

            this.cookies = initReq.cookies.toMutableMap()
            wireData["token"] = csrfToken
            wireData["wireSnapshot"] = snapshot

            return true

        } catch (e: Exception) {
            Log.e("AniZone Init", "Error fatal durante initializeLiveWire: ${e.message}")
            return false
        }
    }

    private fun getSnapshot(doc : Document) : String {
        return doc.select("main div[wire:snapshot]")
            .attr("wire:snapshot").replace("&quot;", "\"")
    }
    private fun getSnapshot(json : JSONObject) : String {
        return json.getJSONArray("components")
            .getJSONObject(0).getString("snapshot")
    }

    private  fun getHtmlFromWire(json: JSONObject): Document {
        return Jsoup.parse(json.getJSONArray("components")
            .getJSONObject(0).getJSONObject("effects")
            .getString("html"))
    }

    private suspend fun liveWireBuilder (
        updates : Map<String,String>, calls: List<Map<String, Any>>,
        biscuit : MutableMap<String, String>,
        wireCreds : MutableMap<String,String>,
        remember : Boolean): JSONObject {

        val payload = mapOf(
            "_token" to wireCreds["token"], "components" to listOf(
                mapOf("snapshot" to wireCreds["wireSnapshot"], "updates" to updates,
                    "calls" to calls
                )
            )
        )

        val jsonString = payload.toJson()!!

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = jsonString.toRequestBody(mediaType)

        val req = app.post(
            url = "$mainUrl/livewire/update",
            requestBody = requestBody,
            headers = mapOf(
                "X-CSRF-TOKEN" to wireCreds["token"]!!
            ),
            cookies = biscuit,
            referer = "$mainUrl/anime"
        )

        val bodyString = req.text

        if (bodyString.isBlank()) {
            throw Exception("Respuesta Livewire vacía o en blanco (HTTP ${req.code}).")
        }

        if (bodyString.trim().startsWith("<!DOCTYPE", ignoreCase = true) ||
            bodyString.trim().startsWith("<html", ignoreCase = true)) {
            Log.e("AniZone", "Respuesta inesperada: Recibido HTML/<!DOCTYPE en lugar de JSON. El sitio podría estar bloqueando el acceso o mostrando un CAPTCHA/error.")
            throw Exception("Livewire no devolvió JSON. Código de estado HTTP: ${req.code}. URL: ${req.url}")
        }

        val responseJson = JSONObject(bodyString)

        if (remember) {
            wireCreds["wireSnapshot"] = getSnapshot(responseJson)
            biscuit.putAll(req.cookies)
        }

        return responseJson
    }

    private suspend fun getEpisodePage(page: Int, name: String): HomePageResponse {
        val episodeUrl = "$mainUrl/episode"
        val initReq = app.get(episodeUrl)
        var currentDoc = initReq.document
        val cookies = initReq.cookies.toMutableMap()

        val wireCreds = mutableMapOf(
            "wireSnapshot" to getSnapshot(currentDoc),
            "token" to currentDoc.select("script[data-csrf]").attr("data-csrf")
        )

        if (wireCreds["token"].isNullOrBlank() || wireCreds["wireSnapshot"].isNullOrBlank()) {
            Log.e("AniZone Episode", "Fallo al inicializar LiveWire para /episode.")
            return newHomePageResponse(HomePageList(name, emptyList(), isHorizontalImages = true), hasNext = false)
        }

        try {
            for (i in 1 until page) {
                if (currentDoc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") == null) break

                val responseJson = liveWireBuilder(
                    mutableMapOf(),
                    mutableListOf(mapOf("path" to "", "method" to "loadMore", "params" to listOf<String>())),
                    cookies,
                    wireCreds,
                    true
                )
                currentDoc = getHtmlFromWire(responseJson)
            }
        } catch (e: Exception) {
            Log.e("AniZone", "Fallo al paginar LiveWire para episodios: ${e.message}")
        }

        val home: List<Element> = currentDoc.select("li[x-data]")

        return newHomePageResponse(
            HomePageList(name, home.mapNotNull { toEpisodeResult(it) }, isHorizontalImages = true),
            hasNext = (currentDoc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null)
        )
    }

    private fun toEpisodeResult(post: Element): SearchResponse? {
        val animeLink = post.selectFirst("a[href*=/anime/]") ?: return null

        val animeUrl = animeLink.attr("href").substringBeforeLast("/")

        val animeTitle = animeLink.attr("title") ?: animeLink.text() ?: "Anime Desconocido"

        val posterUrl = post.selectFirst("img")?.attr("src")

        return newMovieSearchResponse(animeTitle, animeUrl, TvType.Anime) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest
    ): HomePageResponse {

        if (request.data.startsWith("e-")) {
            return getEpisodePage(page, request.name)
        }

        val initialized = initializeLiveWire()
        if (!initialized) {
            Log.w("AniZone", "Inicialización LiveWire fallida. Retornando lista de inicio vacía.")
            return newHomePageResponse(
                HomePageList(request.name, emptyList(), isHorizontalImages = false),
                hasNext = false
            )
        }
        try {
            val typeKey = request.data.substringAfter("a-")

            var responseJson = liveWireBuilder(
                mapOf("type" to typeKey, "sort" to "new"), mutableListOf(), this.cookies, this.wireData, true
            )
            var doc = getHtmlFromWire(responseJson)

            for (i in 1 until page) {
                if (doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") == null) break

                responseJson = liveWireBuilder(
                    mutableMapOf(), mutableListOf(
                        mapOf("path" to "", "method" to "loadMore", "params" to listOf<String>())
                    ), this.cookies, this.wireData, true
                )
                doc = getHtmlFromWire(responseJson)
            }

            val home: List<Element> = doc.select("div[wire:key]")

            return newHomePageResponse(
                HomePageList(request.name, home.map { toResult(it) }, isHorizontalImages = false),
                hasNext = (doc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null)
            )
        } catch (e: Exception) {
            Log.e("AniZone", "Fallo al procesar LiveWire en getMainPage: ${e.message}")
            return newHomePageResponse(
                HomePageList(request.name, emptyList(), isHorizontalImages = false),
                hasNext = false
            )
        }
    }

    private fun toResult(post: Element): SearchResponse {
        val title = post.selectFirst("img")?.attr("alt") ?: ""
        val url = post.selectFirst("a")?.attr("href") ?: ""

        return newMovieSearchResponse(title, url, TvType.Movie) {
            this.posterUrl = post.selectFirst("img")
                ?.attr("src")
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {

        val initialized = initializeLiveWire()
        if (!initialized) return emptyList()

        try {
            val doc = getHtmlFromWire(liveWireBuilder(mapOf("search" to query),mutableListOf(), this.cookies,
                this.wireData,false))
            return doc.select("div[wire:key]").mapNotNull { toResult(it) }
        } catch (e: Exception) {
            Log.e("AniZone Search", "Error en la búsqueda LiveWire: ${e.message}")
            return emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val r = Jsoup.connect(url)
            .method(Connection.Method.GET).execute()
        var doc = Jsoup.parse(r.body())
        val cookie = r.cookies()

        val wireData = mutableMapOf(
            "wireSnapshot" to getSnapshot(doc=r.parse()),
            "token" to doc.select("script[data-csrf]").attr("data-csrf")
        )
        val title = doc.selectFirst("h1")?.text()
            ?: throw NotImplementedError("Unable to find title")
        val bgImage = doc.selectFirst("main img")?.attr("src")
        val synopsis = doc.selectFirst(".sr-only + div")?.text() ?: ""
        val rowLines = doc.select("span.inline-block").map { it.text() }
        val releasedYear = rowLines.getOrNull(3)
        val status = if (rowLines.getOrNull(1) == "Completed") ShowStatus.Completed
        else if (rowLines.getOrNull(1) == "Ongoing") ShowStatus.Ongoing else null
        val genres = doc.select("a[wire:navigate][wire:key]").map { it.text() }

        var currentDoc = doc
        var attempts = 0
        val maxAttempts = 100

        while (currentDoc.selectFirst(".h-12[x-intersect=\"\$wire.loadMore()\"]") != null && attempts < maxAttempts) {
            attempts++
            try {
                val responseJson = liveWireBuilder(
                    mutableMapOf(), mutableListOf(
                        mapOf("path" to "", "method" to "loadMore", "params" to listOf<String>())
                    ), cookie, wireData, true
                )
                currentDoc = getHtmlFromWire(responseJson)
            } catch (e: Exception) {
                Log.e("AniZone Load", "Error al cargar más episodios en el intento $attempts: ${e.message}")
                break
            }
        }

        val epiElms = currentDoc.select("li[x-data]")

        val episodes = epiElms.map{ elt ->
            newEpisode(
                data = elt.selectFirst("a")?.attr("href") ?: "") {
                this.name = elt.selectFirst("h3")?.text()
                    ?.substringAfter(":")?.trim()
                this.season = 0
                this.posterUrl = elt.selectFirst("img")?.attr("src")

                this.date = elt.selectFirst("span[title]")
                    ?.selectFirst("span.line-clamp-1")
                    ?.text()
                    ?.trim()
                    ?.replace(Regex("\\s+"), "")
                    ?.ifEmpty { null }
                    ?.let { dateText ->
                        try {
                            val parsedTime = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).parse(dateText)?.time
                            parsedTime
                        } catch (e: Exception) {
                            Log.e("AniZone", "FALLO de parseo para fecha '$dateText': ${e.message}")
                            null
                        }
                    } ?: 0L
            }
        }

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = bgImage
            this.plot = synopsis
            this.tags = genres
            this.year = releasedYear?.toIntOrNull()
            this.showStatus = status
            addEpisodes(DubStatus.None, episodes)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val web = app.get(data).document
        val sourceName = web.selectFirst("span.truncate")?.text() ?: ""
        val mediaPlayer = web.selectFirst("media-player")
        val m3U8 = mediaPlayer?.attr("src") ?: ""

        mediaPlayer?.select("track")?.forEach {
            subtitleCallback.invoke(
                SubtitleFile (
                    it.attr("label"),
                    it.attr("src")
                )
            )
        }

        callback.invoke(
            newExtractorLink(
                sourceName,
                name,
                m3U8,
                type = ExtractorLinkType.M3U8
            )
        )
        return true
    }
}