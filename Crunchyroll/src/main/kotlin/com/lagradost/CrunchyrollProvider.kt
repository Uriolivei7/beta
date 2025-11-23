package com.lagradost

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.lagradost.api.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.APIHolder.capitalize
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.ExtractorLinkType
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.Qualities
import com.lagradost.cloudstream3.utils.getQualityFromName
import com.lagradost.cloudstream3.utils.newExtractorLink
import com.lagradost.nicehttp.NiceResponse
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.*

private fun String.toAscii() = this.map { it.code }.joinToString()

class KrunchyGeoBypasser(
    client: OkHttpClient
) {

    companion object {
        const val PROXY_BASE_URL = "https://us.community-proxy.meganeko.dev"
        const val LOG_TAG = "Crunchyroll"

        private fun String.toAscii() = this.map { it.code }.joinToString()

        val baseHeaders = mapOf(
            "accept" to "*/*",
            "connection" to "keep-alive",
            "Host" to "www.crunchyroll.com",
            "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36".toAscii()
        )
    }

    val session = CustomSession(client)

    suspend fun geoBypassRequest(url: String): NiceResponse {

        val finalHeaders: Map<String, String> = baseHeaders +
                mapOf("X-Proxy-Host" to "www.crunchyroll.com")

        Log.i(LOG_TAG, "--- INICIANDO GEO BYPASS REQUEST ---")
        Log.i(LOG_TAG, "URL de Destino: $url")
        Log.i(LOG_TAG, "--------------------------------------")

        val response = session.get(
            url = url,
            headers = finalHeaders,
            cookies = emptyMap()
        )

        Log.i(LOG_TAG, "--- RESPUESTA GEO BYPASS ---")
        Log.i(LOG_TAG, "Status: ${response.code}")

        if (response.code != 200) {
            Log.e(LOG_TAG, "ERROR: Geo Bypass falló con código ${response.code}. URL: $url")
            Log.e(LOG_TAG, "Respuesta Body (Primeras 500 chars): ${response.text.take(500)}")
        } else {
            Log.i(LOG_TAG, "Respuesta OK. Content Type: ${response.headers["Content-Type"]}")
            Log.i(LOG_TAG, "Respuesta Body (Primeras 200 chars): ${response.text.take(200)}")
        }
        Log.i(LOG_TAG, "----------------------------------")

        return response
    }
}

class KrunchyProvider : MainAPI() {
    companion object {
        val crUnblock = KrunchyGeoBypasser(app.baseClient)
        val episodeNumRegex = Regex("""Episode (\d+)""")
        const val LOG_TAG = "Crunchyroll"
    }

    override var mainUrl = "http://www.crunchyroll.com"
    override var name: String = "Crunchyroll"
    override var lang = "en"
    override val hasQuickSearch = false
    override val hasMainPage = true

    override val supportedTypes = setOf(
        TvType.AnimeMovie,
        TvType.Anime,
        TvType.OVA
    )

    override val mainPage = mainPageOf(
        "$mainUrl/videos/anime/popular/ajax_page?pg=" to "Popular",
        "$mainUrl/videos/anime/simulcasts/ajax_page" to "Simulcasts"
    )

    private fun isContentUrl(url: String): Boolean {
        val crHost = "crunchyroll.com"

        if (url.endsWith("$crHost/") || url.endsWith(crHost)) {
            return true
        }

        return url.contains("/watch/") ||
                url.contains("/series/") ||
                url.contains("/search") ||
                url.contains("/videos/anime/") ||
                url.contains("/ajax/")
    }

    suspend fun myRequestFunction(url: String): NiceResponse {
        val crHost = "crunchyroll.com"

        if (!url.contains(crHost)) {
            Log.e(LOG_TAG, "ALERTA: Solicitud externa NO BLOQUEADA: $url")
            return crUnblock.session.get(url)
        }

        if (!isContentUrl(url)) {
            Log.i(LOG_TAG, "IGNORADO: URL de Crunchyroll sin ruta de contenido válida (ej. footer/error): $url")

            val fakeRequest = Request.Builder().url("http://fakedata.com").build()
            val fakeResponse = Response.Builder()
                .request(fakeRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(404)
                .message("Ignored Content")
                .build()

            return NiceResponse(fakeResponse, null)
        }

        Log.i(LOG_TAG, "USANDO BYPASS: URL de contenido detectada: $url")
        return crUnblock.geoBypassRequest(url)
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.i(LOG_TAG, "getMainPage INICIADO. Page: $page, Category: ${request.name}")

        val categoryData = request.data
        val paginated = categoryData.endsWith("=")
        val pagedLink = if (paginated) categoryData + page else categoryData
        val items = mutableListOf<HomePageList>()

        Log.i(LOG_TAG, "PAGED LINK: $pagedLink")


        if (page <= 1 && request.name == "Popular") {
            Log.i(LOG_TAG, "Fetching featured and now showing content...")
            val doc = Jsoup.parse(myRequestFunction(mainUrl).text)

            val featured = doc.select(".js-featured-show-list > li").mapNotNull { anime ->
                val url = fixUrlNull(anime?.selectFirst("a")?.attr("href")) ?: return@mapNotNull null

                if (!url.contains("/series/") && !url.contains("/watch/")) return@mapNotNull null
                val imgEl = anime.selectFirst("img")
                val name = imgEl?.attr("alt") ?: ""
                val posterUrl = (imgEl?.attr("src") ?: imgEl?.attr("data-src"))
                    ?.replace("small", "full")

                newAnimeSearchResponse(name, url, TvType.Anime) {
                    this.posterUrl = posterUrl
                    this.dubStatus = EnumSet.of(DubStatus.Subbed)
                }
            }

            val recent =
                doc.select("div.welcome-countdown-day:contains(Now Showing) li").mapNotNull {
                    val link = fixUrlNull(it.selectFirst("a")?.attr("href")) ?: return@mapNotNull null
                    val name = it.selectFirst("span.welcome-countdown-name")?.text() ?: ""

                    val img = (it.selectFirst("img")?.attr("src") ?: it.selectFirst("img")?.attr("data-src"))
                        ?.replace("medium", "full")

                    val dubstat = if (name.contains("Dub)", true)) EnumSet.of(DubStatus.Dubbed) else
                        EnumSet.of(DubStatus.Subbed)
                    val details = it.selectFirst("span.welcome-countdown-details")?.text()
                    val epnum =
                        if (details.isNullOrBlank()) null else episodeNumRegex.find(details)?.value?.replace(
                            "Episode ",
                            ""
                        ) ?: "0"
                    val episodesMap = mutableMapOf<DubStatus, Int>()
                    episodesMap[DubStatus.Subbed] = epnum?.toIntOrNull() ?: 0
                    episodesMap[DubStatus.Dubbed] = epnum?.toIntOrNull() ?: 0

                    newAnimeSearchResponse("★ $name ★", link.replace(Regex("(\\/episode.*)"), ""), TvType.Anime) {
                        this.posterUrl = fixUrlNull(img)
                        this.dubStatus = dubstat
                        this.episodes = episodesMap
                    }
                }

            Log.i(LOG_TAG, "Found ${recent.size} recent items.")
            if (recent.isNotEmpty()) {
                items.add(
                    HomePageList(
                        name = "Now Showing",
                        list = recent,
                    )
                )
            }
            Log.i(LOG_TAG, "Found ${featured.size} featured items.")
            if (featured.isNotEmpty()) {
                items.add(HomePageList("Featured", featured))
            }
        }

        if (paginated || !paginated && page <= 1) {
            Log.i(LOG_TAG, "Fetching paginated content from: $pagedLink")
            myRequestFunction(pagedLink).let { respText ->

                Log.i(LOG_TAG, "RESPUESTA COMPLETA DEL AJAX:")
                Log.i(LOG_TAG, respText.text.substring(0, minOf(respText.text.length, 5000))) // Muestra hasta 5000 caracteres

                val soup = Jsoup.parse(respText.text)

                val episodes = soup.select(".group-item a, .grid-item a, a[title][href*=/series/]").mapNotNull { innerA ->
                    val urlEps = fixUrlNull(innerA.attr("href")) ?: return@mapNotNull null

                    if (!urlEps.contains("/series/") && !urlEps.contains("/watch/")) return@mapNotNull null

                    val item = innerA.parent() ?: innerA

                    val title = item.selectFirst(".series-title, .series-card-title, a[title]")?.text()
                        ?: innerA.attr("title")
                        ?: return@mapNotNull null

                    val posterUrl = item.selectFirst("img")?.attr("data-src")
                        ?: item.selectFirst("img")?.attr("src")

                    newAnimeSearchResponse(title, urlEps, TvType.Anime) {
                        this.posterUrl = posterUrl
                        this.dubStatus = EnumSet.of(DubStatus.Subbed)
                    }
                }
                Log.i(LOG_TAG, "Found ${episodes.size} items for list: ${request.name}")
                if (episodes.isNotEmpty()) {
                    items.add(
                        HomePageList(
                            name = request.name,
                            list = episodes,
                        )
                    )
                }
            }
        }

        if (items.isNotEmpty()) {
            Log.i(LOG_TAG, "getMainPage FINALIZADO. Lists generated: ${items.size}")
            return newHomePageResponse(items)
        }

        Log.e(LOG_TAG, "getMainPage FALLÓ. No items were generated.")
        throw ErrorLoadingException()
    }

    override suspend fun search(query: String): ArrayList<SearchResponse> {
        Log.i(LOG_TAG, "search INICIADO. Query: $query")

        val url = "$mainUrl/videos/anime/search/ajax_page?q=$query"

        val response = myRequestFunction(url)

        if (response.code != 200) {
            Log.e(LOG_TAG, "search FALLÓ. Código: ${response.code} para la query: $query")
            return ArrayList()
        }

        val doc = Jsoup.parse(response.text)
        val searchResults = ArrayList<SearchResponse>()

        val items = doc.select(".group-item, .series-card-wrapper, .search-result-item, .list-block > li")

        Log.i(LOG_TAG, "Found ${items.size} potential results with common selectors.")

        for (item in items) {
            val linkEl = item.selectFirst("a[href*=/series/], a[href*=/watch/]") ?: continue
            val link = fixUrlNull(linkEl.attr("href")) ?: continue

            val title = item.selectFirst(".series-title, .text-bold")?.text()?.trim()
                ?: item.selectFirst("span.series-title")?.text()?.trim()
                ?: linkEl.attr("title")
                ?: continue

            val imgEl = item.selectFirst("img")
            val posterUrl = (imgEl?.attr("src") ?: imgEl?.attr("data-src"))
                ?.replace("small", "full")

            val dubstat =
                if (title.contains("Dub)", true)) EnumSet.of(DubStatus.Dubbed) else
                    EnumSet.of(DubStatus.Subbed)

            searchResults.add(
                newAnimeSearchResponse(title, link, TvType.Anime) {
                    this.posterUrl = posterUrl
                    this.dubStatus = dubstat
                }
            )
        }

        Log.i(LOG_TAG, "search FINALIZADO. Results found: ${searchResults.size}")
        return searchResults
    }

    override suspend fun load(url: String): LoadResponse {
        Log.i(LOG_TAG, "load INICIADO. URL: $url")

        if (!isContentUrl(url)) {
            Log.e(LOG_TAG, "load FALLÓ. URL: $url no es una ruta de contenido válida.")
            throw ErrorLoadingException()
        }

        val response = myRequestFunction(url)
        if (response.code != 200) {
            Log.e(LOG_TAG, "load FALLÓ. Geo Bypass falló o la URL fue ignorada (Code: ${response.code}). URL: $url")
            throw ErrorLoadingException()
        }

        val soup = Jsoup.parse(response.text)

        val metadataScript = soup.selectFirst("script[type=\"application/ld+json\"]")?.html()

        var title: String? = null
        var description: String? = null
        var posterU: String? = null
        var genres: List<String> = emptyList()
        var year: Int? = null

        if (metadataScript != null) {
            try {
                val json = mapper.readValue<Map<String, Any>>(
                    metadataScript,
                    object : TypeReference<Map<String, Any>>() {}
                )

                title = json["name"] as? String
                description = json["description"] as? String
                posterU = json["image"] as? String

                val genreData = json["genre"]
                genres = when (genreData) {
                    is String -> listOf(genreData)
                    is List<*> -> genreData.filterIsInstance<String>()
                    else -> emptyList()
                }

                Log.i(LOG_TAG, "Metadata JSON success. Title: $title")
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error parsing JSON-LD metadata: ${e.message}")
            }
        }

        if (title.isNullOrEmpty()) {
            title = soup.selectFirst("h1.text--is-xl--Q7c9n")?.text()?.trim()
                ?: soup.selectFirst("title")?.text()?.replace(" - Crunchyroll", "")?.trim()

            description = soup.selectFirst("div[data-t=\"show-description-text\"]")?.text()?.trim()

            posterU = soup.selectFirst("img[data-t=\"show-header-poster\"]")?.attr("src")
                ?.replace("medium", "full")
        }

        if (title.isNullOrEmpty()) {
            Log.e(LOG_TAG, "load FALLÓ. No se pudo obtener el título de la página de carga (ni JSON ni Fallback).")
            throw ErrorLoadingException()
        }

        Log.i(LOG_TAG, "Load Info: Title='$title', PosterUrl='$posterU'")

        val subEpisodes = mutableListOf<Episode>()
        val dubEpisodes = mutableListOf<Episode>()

        val seasonContainer = soup.selectFirst("div.erc-series-season-selector")

        val episodeItems = soup.select("div[data-t=\"episode-card\"]")

        Log.i(LOG_TAG, "Found ${episodeItems.size} potential episode items using modern selector.")


        for (ep in episodeItems) {
            val linkEl = ep.selectFirst("a[href*=/watch/]") ?: continue
            val epLink = fixUrl(linkEl.attr("href"))

            val epTitle = ep.selectFirst("h4.text--is-l--iccTo")?.text()?.trim()

            val epNumText = ep.selectFirst("span.text--is-xs--UyvXH")?.text()

            val epNum = epNumText?.let {
                episodeNumRegex.find(it)?.destructured?.component1()?.toIntOrNull()
            } ?: 1

            val poster = ep.selectFirst("img[data-t=\"episode-card-poster\"]")?.attr("src")
                ?.replace("medium", "full")

            val languageTag = ep.selectFirst("div[data-t=\"episode-card-meta-tags\"] span")?.text() ?: ""

            val isDubbed = languageTag.contains("Dob", ignoreCase = true)
            val isPremium = ep.selectFirst("div[data-t=\"premium-badge\"]") != null

            val epi = newEpisode(
                url = epLink,
                initializer = {
                    this.name = epTitle ?: "Episodio $epNum"
                    this.posterUrl = poster
                    this.description = epTitle ?: ""
                    this.season = if (isPremium) -1 else 1
                    this.episode = epNum
                }
            )

            if (isDubbed) {
                dubEpisodes.add(epi)
            } else {
                subEpisodes.add(epi)
            }
        }

        Log.i(LOG_TAG, "Episodes found: Sub: ${subEpisodes.size}, Dub: ${dubEpisodes.size}")

        val recommendations = soup.select("[data-t=\"carousel-item\"]").mapNotNull { element ->
            val recLink = element.selectFirst("a") ?: return@mapNotNull null
            val recUrl = fixUrl(recLink.attr("href"))
            val recTitle = element.selectFirst("h4")?.text() ?: return@mapNotNull null
            val image = element.selectFirst("img")?.attr("src")

            newAnimeSearchResponse(recTitle, recUrl, TvType.Anime) {
                this.posterUrl = image
                this.dubStatus =
                    if (recTitle.contains("DUB", ignoreCase = true)) EnumSet.of(DubStatus.Dubbed) else EnumSet.of(DubStatus.Subbed)
            }
        }

        Log.i(LOG_TAG, "Found ${recommendations.size} recommendations.")

        Log.i(LOG_TAG, "load FINALIZADO.")
        return newAnimeLoadResponse(title.toString(), url, TvType.Anime) {
            this.posterUrl = posterU
            this.engName = title
            this.plot = description
            this.tags = genres
            this.year = year

            if (subEpisodes.isNotEmpty()) addEpisodes(DubStatus.Subbed, subEpisodes.reversed())
            if (dubEpisodes.isNotEmpty()) addEpisodes(DubStatus.Dubbed, dubEpisodes.reversed())

            this.seasonNames = listOf(
                SeasonData(1, "Subbed Episodes", null),
                SeasonData(2, "Dubbed Episodes", null)
            )

            this.recommendations = recommendations
        }
    }

    data class Subtitles(
        @JsonProperty("language") val language: String,
        @JsonProperty("url") val url: String,
        @JsonProperty("title") val title: String?,
        @JsonProperty("format") val format: String?
    )

    data class Streams(
        @JsonProperty("format") val format: String?,
        @JsonProperty("audio_lang") val audioLang: String?,
        @JsonProperty("hardsub_lang") val hardsubLang: String?,
        @JsonProperty("url") val url: String,
        @JsonProperty("resolution") val resolution: String?,
        @JsonProperty("title") var title: String?
    ) {
        fun title(): String {
            return when {
                this.hardsubLang == "enUS" && this.audioLang == "jaJP" -> "Hardsub (English)"
                this.hardsubLang == "esLA" && this.audioLang == "jaJP" -> "Hardsub (Latino)"
                this.hardsubLang == "esES" && this.audioLang == "jaJP" -> "Hardsub (Español España)"
                this.audioLang == "esLA" -> "Latino"
                this.audioLang == "esES" -> "Español España"
                this.audioLang == "enUS" -> "English (US)"
                else -> "RAW"
            }
        }
    }

    data class KrunchyVideo(
        @JsonProperty("streams") val streams: List<Streams>,
        @JsonProperty("subtitles") val subtitles: List<Subtitles>,
    )

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.i(LOG_TAG, "loadLinks INICIADO. Data URL: $data")

        val contentRegex = Regex("""(?s)vilos\.config\.media = (\s*\{.+\}\s*)""")

        val response = myRequestFunction(data)

        Log.i(LOG_TAG, "loadLinks: Geo bypass response status: ${response.code}")

        if (response.code == 404 && response.text.isEmpty()) {
            Log.e(LOG_TAG, "loadLinks: URL de pie de página/error ignorada.")
            return false
        }

        val dat = contentRegex.find(response.text)?.groupValues?.get(1)

        if (!dat.isNullOrEmpty()) {
            Log.i(LOG_TAG, "loadLinks: Found Vilos config data. Length: ${dat.length}")
            val json = parseJson<KrunchyVideo>(dat)
            val streams = ArrayList<Streams>()

            val validFormats = listOf(
                "adaptive_hls", "adaptive_dash",
                "multitrack_adaptive_hls_v2",
                "vo_adaptive_dash", "vo_adaptive_hls",
                "trailer_hls",
            )

            val validAudioLangs = listOf("jaJP", "esLA", "esES", "enUS")

            for (stream in json.streams) {
                if (stream.format in validFormats) {
                    if (stream.audioLang in validAudioLangs || stream.format == "trailer_hls") {
                        stream.title = stream.title()
                        streams.add(stream)
                    }
                }
            }

            Log.i(LOG_TAG, "loadLinks: Found ${streams.size} streams after filtering")

            streams.forEach { stream ->
                if (stream.url.contains("m3u8") && stream.format!!.contains("adaptive")) {
                    callback(
                        newExtractorLink(
                            source = "Crunchyroll",
                            name = "Crunchy - ${stream.title}",
                            url = stream.url,
                            type = ExtractorLinkType.M3U8
                        ) {
                            this.referer = ""
                            this.quality = getQualityFromName(stream.resolution)
                        }
                    )
                } else if (stream.format == "trailer_hls") {
                    val premiumStream = stream.url
                        .replace("\\/", "/")
                        .replace(Regex("\\/clipFrom.*?index.m3u8"), "").replace("'_,'", "'_'")
                        .replace(stream.url.split("/")[2], "fy.v.vrv.co")
                    callback(
                        newExtractorLink(
                            source = this.name,
                            name = "Crunchy - ${stream.title} ★",
                            url = premiumStream,
                            type = ExtractorLinkType.VIDEO
                        ) {
                            this.referer = ""
                            this.quality = Qualities.Unknown.value
                        }
                    )
                }
            }

            Log.i(LOG_TAG, "loadLinks: Processed ${json.subtitles.size} subtitles")
            json.subtitles.forEach {
                val langclean = it.language.replace("esLA", "Spanish")
                    .replace("enUS", "English")
                    .replace("esES", "Spanish (Spain)")
                subtitleCallback(
                    SubtitleFile(langclean, it.url)
                )
            }

            Log.i(LOG_TAG, "loadLinks FINALIZADO con éxito.")
            return true
        }
        Log.e(LOG_TAG, "loadLinks FALLÓ. No Vilos config found for URL: $data. Response length: ${response.text.length}")
        return false
    }
}