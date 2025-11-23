package com.lagradost

import com.fasterxml.jackson.annotation.JsonProperty
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

        val baseHeaders = mapOf(
            "accept" to "*/*",
            "connection" to "keep-alive",
            "Host" to "www.crunchyroll.com",
            "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36".toAscii()
        )
    }

    val session = CustomSession(client)


    suspend fun geoBypassRequest(url: String): NiceResponse {

        val finalHeaders: Map<String, String> = KrunchyGeoBypasser.Companion.baseHeaders +
                mapOf("X-Proxy-Host" to "www.crunchyroll.com")

        Log.i("CrunchyrollGeo", "--- INICIANDO GEO BYPASS REQUEST ---")
        Log.i("CrunchyrollGeo", "Proxy Base Server: ${KrunchyGeoBypasser.Companion.PROXY_BASE_URL}")
        Log.i("CrunchyrollGeo", "URL de Destino: $url")
        Log.i("CrunchyrollGeo", "Encabezados Enviados:")

        finalHeaders.forEach { (key, value) ->
            Log.i("CrunchyrollGeo", "  $key: $value")
        }
        Log.i("CrunchyrollGeo", "--------------------------------------")

        val response = session.get(
            url = url,
            headers = finalHeaders,
            cookies = emptyMap()
        )

        Log.i("CrunchyrollGeo", "--- RESPUESTA GEO BYPASS ---")
        Log.i("CrunchyrollGeo", "Status: ${response.code}")

        if (response.code != 200) {
            Log.e("CrunchyrollGeo", "ERROR: Geo Bypass falló con código ${response.code}. URL: $url")
            Log.e("CrunchyrollGeo", "Respuesta Body (Primeras 500 chars): ${response.text.take(500)}")
        } else {
            Log.i("CrunchyrollGeo", "Respuesta Body (Primeras 200 chars): ${response.text.take(200)}")
        }
        Log.i("CrunchyrollGeo", "----------------------------------")

        return response
    }


}

class KrunchyProvider : MainAPI() {
    companion object {
        val crUnblock = KrunchyGeoBypasser(app.baseClient)
        val episodeNumRegex = Regex("""Episode (\d+)""")
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
        return url.contains("/watch/") ||
                url.contains("/series/") ||
                url.contains("/videos/anime/") ||
                url.contains("/ajax/")
    }

    suspend fun myRequestFunction(url: String): NiceResponse {
        val crHost = "crunchyroll.com"

        if (!url.contains(crHost)) {
            Log.e("CrunchyrollGeo", "ALERTA: Solicitud externa NO BLOQUEADA: $url")
            return crUnblock.session.get(url)
        }

        if (!isContentUrl(url)) {
            Log.e("CrunchyrollGeo", "IGNORADO: URL de Crunchyroll sin ruta de contenido válida (footer/error): $url")

            val fakeRequest = Request.Builder().url("http://fakedata.com").build()
            val fakeResponse = Response.Builder()
                .request(fakeRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(404)
                .message("Ignored Content")
                .build()

            return NiceResponse(fakeResponse, null)
        }

        Log.i("CrunchyrollGeo", "USANDO BYPASS: URL de contenido detectada: $url")
        return crUnblock.geoBypassRequest(url)
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        println("GETMAINPAGE ")
        val categoryData = request.data

        val paginated = categoryData.endsWith("=")
        val pagedLink = if (paginated) categoryData + page else categoryData
        val items = mutableListOf<HomePageList>()

        if (page <= 1 && request.name == "Popular") {
            val doc = Jsoup.parse(myRequestFunction(mainUrl).text)

            val featured = doc.select(".js-featured-show-list > li").mapNotNull { anime ->
                val url =
                    fixUrlNull(anime?.selectFirst("a")?.attr("href")) ?: return@mapNotNull null
                val imgEl = anime.selectFirst("img")
                val name = imgEl?.attr("alt") ?: ""
                val posterUrl = imgEl?.attr("src")?.replace("small", "full")

                newAnimeSearchResponse(name, url, TvType.Anime) {
                    this.posterUrl = posterUrl
                    this.dubStatus = EnumSet.of(DubStatus.Subbed)
                }
            }
            val recent =
                doc.select("div.welcome-countdown-day:contains(Now Showing) li").mapNotNull {
                    val link =
                        fixUrlNull(it.selectFirst("a")?.attr("href")) ?: return@mapNotNull null
                    val name = it.selectFirst("span.welcome-countdown-name")?.text() ?: ""
                    val img = it.selectFirst("img")?.attr("src")?.replace("medium", "full")
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
            if (recent.isNotEmpty()) {
                items.add(
                    HomePageList(
                        name = "Now Showing",
                        list = recent,
                    )
                )
            }
            if (featured.isNotEmpty()) {
                items.add(HomePageList("Featured", featured))
            }
        }

        if (paginated || !paginated && page <= 1) {
            myRequestFunction(pagedLink).let { respText ->
                val soup = Jsoup.parse(respText.text)

                val episodes = soup.select("li").mapNotNull {
                    val innerA = it.selectFirst("a") ?: return@mapNotNull null
                    val urlEps = fixUrlNull(innerA.attr("href")) ?: return@mapNotNull null

                    newAnimeSearchResponse(innerA.attr("title"), urlEps, TvType.Anime) {
                        this.posterUrl = it.selectFirst("img")?.attr("src")
                        this.dubStatus = EnumSet.of(DubStatus.Subbed)
                    }
                }
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
            return newHomePageResponse(items)
        }
        throw ErrorLoadingException()
    }

    private fun getCloseMatches(sequence: String, items: Collection<String>): List<String> {
        val a = sequence.trim().lowercase()

        return items.mapNotNull { item ->
            val b = item.trim().lowercase()
            if (b.contains(a))
                item
            else if (a.contains(b))
                item
            else null
        }
    }


    private data class CrunchyAnimeData(
        @JsonProperty("name") val name: String,
        @JsonProperty("img") var img: String,
        @JsonProperty("link") var link: String
    )

    private data class CrunchyJson(
        @JsonProperty("data") val data: List<CrunchyAnimeData>,
    )


    override suspend fun search(query: String): ArrayList<SearchResponse> {
        val json =
            myRequestFunction("http://www.crunchyroll.com/ajax/?req=RpcApiSearch_GetSearchCandidates").text.split(
                "*/"
            )[0].replace("\\/", "/")
        val data = parseJson<CrunchyJson>(
            json.split("\n").mapNotNull { if (!it.startsWith("/")) it else null }.joinToString("\n")
        ).data

        val results = getCloseMatches(query, data.map { it.name })
        if (results.isEmpty()) return ArrayList()
        val searchResutls = ArrayList<SearchResponse>()

        var count = 0
        for (anime in data) {
            if (count == results.size) {
                break
            }
            if (anime.name == results[count]) {
                val dubstat =
                    if (anime.name.contains("Dub)", true)) EnumSet.of(DubStatus.Dubbed) else
                        EnumSet.of(DubStatus.Subbed)
                anime.link = fixUrl(anime.link)
                anime.img = anime.img.replace("small", "full")

                searchResutls.add(
                    newAnimeSearchResponse(anime.name, anime.link, TvType.Anime) {
                        this.posterUrl = anime.img
                        this.dubStatus = dubstat
                    }
                )
                ++count
            }
        }

        return searchResutls
    }

    override suspend fun load(url: String): LoadResponse {
        val soup = Jsoup.parse(myRequestFunction(url).text)

        val title = soup.selectFirst("#showview-content-header .ellipsis")?.text()?.trim()
        val posterU = soup.selectFirst(".poster")?.attr("src")

        val p = soup.selectFirst(".description")
        var description = p?.selectFirst(".more")?.text()?.trim()
        if (description.isNullOrBlank()) {
            description = p?.selectFirst("span")?.text()?.trim()
        }

        val genres = soup.select(".large-margin-bottom > ul:nth-child(2) li:nth-child(2) a")
            .map { it.text().capitalize() }
        val year = genres.filter { it.toIntOrNull() != null }.map { it.toInt() }.sortedBy { it }
            .getOrNull(0)

        val subEpisodes = mutableListOf<Episode>()
        val dubEpisodes = mutableListOf<Episode>()
        val premiumSubEpisodes = mutableListOf<Episode>()
        val premiumDubEpisodes = mutableListOf<Episode>()
        soup.select(".season").forEach {
            val seasonName = it.selectFirst("a.season-dropdown")?.text()?.trim()
            it.select(".episode").forEach { ep ->
                val epTitle = ep.selectFirst(".short-desc")?.text()

                val epNum = episodeNumRegex.find(
                    ep.selectFirst("span.ellipsis")?.text().toString()
                )?.destructured?.component1()
                var poster = ep.selectFirst("img.landscape")?.attr("data-thumbnailurl")
                val poster2 = ep.selectFirst("img")?.attr("src")
                if (poster.isNullOrBlank()) {
                    poster = poster2
                }

                var epDesc =
                    (if (epNum == null) "" else "Episode $epNum") + (if (!seasonName.isNullOrEmpty()) " - $seasonName" else "")
                val isPremium = poster?.contains("widestar", ignoreCase = true) ?: false
                if (isPremium) {
                    epDesc = "★ $epDesc ★"
                }

                val isPremiumDubbed =
                    isPremium && seasonName != null && (seasonName.contains("Dub") || seasonName.contains(
                        "Russian"
                    ) || seasonName.contains("Spanish"))

                val epi = newEpisode(
                    url = fixUrl(ep.attr("href")),
                    initializer = {
                        this.name = "$epTitle"
                        this.runTime = 0
                        this.posterUrl = poster?.replace("widestar", "full")?.replace("wide", "full")
                        this.description = epDesc
                        this.season = if (isPremium) -1 else 1
                    }
                )

                if (isPremiumDubbed) {
                    premiumDubEpisodes.add(epi)
                } else if (isPremium) {
                    premiumSubEpisodes.add(epi)
                } else if (seasonName != null && (seasonName.contains("Dub"))) {
                    dubEpisodes.add(epi)
                } else {
                    subEpisodes.add(epi)
                }
            }
        }
        val recommendations =
            soup.select(".other-series > ul li")?.mapNotNull { element ->
                val recTitle =
                    element.select("span.ellipsis[dir=auto]").text() ?: return@mapNotNull null
                val image = element.select("img")?.attr("src")
                val recUrl = fixUrl(element.select("a").attr("href"))

                newAnimeSearchResponse(recTitle, fixUrl(recUrl), TvType.Anime) {
                    this.posterUrl = fixUrl(image!!)
                    this.dubStatus =
                        if (recTitle.contains("(DUB)") || recTitle.contains("Dub")) EnumSet.of(
                            DubStatus.Dubbed
                        ) else EnumSet.of(DubStatus.Subbed)
                }
            }
        return newAnimeLoadResponse(title.toString(), url, TvType.Anime) {
            this.posterUrl = posterU
            this.engName = title
            if (subEpisodes.isNotEmpty()) addEpisodes(DubStatus.Subbed, subEpisodes.reversed())
            if (dubEpisodes.isNotEmpty()) addEpisodes(DubStatus.Dubbed, dubEpisodes.reversed())

            if (premiumDubEpisodes.isNotEmpty()) addEpisodes(
                DubStatus.Dubbed,
                premiumDubEpisodes.reversed()
            )
            if (premiumSubEpisodes.isNotEmpty()) addEpisodes(
                DubStatus.Subbed,
                premiumSubEpisodes.reversed()
            )

            this.plot = description
            this.tags = genres
            this.year = year

            this.recommendations = recommendations
            this.seasonNames = listOf(
                SeasonData(
                    1,
                    "Free",
                    null
                ),
                SeasonData(
                    -1,
                    "Premium",
                    null
                ),
            )
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
        val contentRegex = Regex("""vilos\.config\.media = (\{.+\})""")

        Log.i("Crunchyroll", "LOADLINKS: Data URL received: $data")
        // LLAMADA CORREGIDA
        val response = myRequestFunction(data)
        Log.i("Crunchyroll", "LOADLINKS: Geo bypass response status: ${response.code}")

        // Si la respuesta fue el 404 que generamos para las URLs ignoradas, salimos.
        if (response.code == 404 && response.text.isEmpty()) {
            Log.e("Crunchyroll", "LOADLINKS: URL de pie de página/error ignorada.")
            return false
        }

        val hlsHelper = M3u8Helper()

        val dat = contentRegex.find(response.text)?.destructured?.component1()

        if (!dat.isNullOrEmpty()) {
            Log.i("Crunchyroll", "LOADLINKS: Found Vilos config data. Length: ${dat.length}")
            val json = parseJson<KrunchyVideo>(dat)
            val streams = ArrayList<Streams>()

            for (stream in json.streams) {
                if (
                    listOf(
                        "adaptive_hls", "adaptive_dash",
                        "multitrack_adaptive_hls_v2",
                        "vo_adaptive_dash", "vo_adaptive_hls",
                        "trailer_hls",
                    ).contains(stream.format)
                ) {
                    if (stream.format!!.contains("adaptive") && listOf(
                            "jaJP",
                            "esLA",
                            "esES",
                            "enUS"
                        )
                            .contains(stream.audioLang) && (listOf(
                            "esLA",
                            "esES",
                            "enUS",
                            null
                        ).contains(stream.hardsubLang))
//                        && URI(stream.url).path.endsWith(".m3u")
                    ) {
                        stream.title = stream.title()
                        streams.add(stream)
                    }
                    // Premium eps
                    else if (stream.format == "trailer_hls" && listOf(
                            "jaJP",
                            "esLA",
                            "esES",
                            "enUS"
                        ).contains(stream.audioLang) &&
                        (listOf("esLA", "esES", "enUS", null).contains(stream.hardsubLang))
                    ) {
                        stream.title = stream.title()
                        streams.add(stream)
                    }
                }
            }

            Log.i("Crunchyroll", "LOADLINKS: Found ${streams.size} streams after filtering")

            // CORRECCIÓN: Usando forEach para la extracción
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

            Log.i("Crunchyroll", "LOADLINKS: Processed ${json.subtitles.size} subtitles")
            json.subtitles.forEach {
                val langclean = it.language.replace("esLA", "Spanish")
                    .replace("enUS", "English")
                    .replace("esES", "Spanish (Spain)")
                subtitleCallback(
                    SubtitleFile(langclean, it.url)
                )
            }

            return true
        }
        Log.e("Crunchyroll", "LOADLINKS: Failed to find Vilos config for URL: $data. Response length: ${response.text.length}")
        return false
    }
}