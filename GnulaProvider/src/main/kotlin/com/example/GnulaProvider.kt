package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class PopularModel(val props: Props = Props())

@Serializable
data class Props(val pageProps: PageProps = PageProps())

@Serializable
data class PageProps(
    val results: Results = Results(),
    val post: SeasonPost? = null,
    val episode: EpisodeData? = null
)

@Serializable
data class Results(
    val data: List<Daum> = emptyList(),
    @SerialName("__typename") val typename: String? = null
)

@Serializable
data class Daum(
    val titles: Titles = Titles(),
    val images: Images = Images(),
    val slug: Slug = Slug(),
    val url: Url = Url()
)

@Serializable data class Titles(val name: String? = null)
@Serializable data class Images(val poster: String? = null)
@Serializable data class Slug(val name: String? = null)
@Serializable data class Url(val slug: String? = null)

@Serializable
data class SeasonPost(
    val titles: Titles = Titles(),
    val images: Images = Images(),
    val overview: String? = null,
    val seasons: List<Season> = emptyList(),
    val players: Players? = null
)

@Serializable
data class Season(val number: Long? = null, val episodes: List<SeasonEpisode> = emptyList())

@Serializable
data class SeasonEpisode(
    val title: String? = null,
    val number: Long? = null,
    val slug: Slug2 = Slug2(),
    val releaseDate: String? = null
)

@Serializable data class Slug2(val name: String? = null, val season: String? = null, val episode: String? = null)

@Serializable
data class EpisodeData(val players: Players? = null)

@Serializable
data class Players(
    val latino: List<Region> = emptyList(),
    val spanish: List<Region> = emptyList(),
    val english: List<Region> = emptyList()
)

@Serializable data class Region(val result: String = "")


class GnulaProvider : MainAPI() {
    override var mainUrl = "https://gnula.life"
    override var name = "GNULA"
    override val hasMainPage = true
    override var lang = "mx"
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.Anime,
        TvType.Cartoon
    )

    private val TAG = "GNULA"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d(TAG, "getMainPage: Cargando página principal $page")
        val items = mutableListOf<HomePageList>()

        val catalogs = listOf(
            Pair("$mainUrl/archives/series/page/$page", "Series"),
            Pair("$mainUrl/archives/series/releases/page/$page", "Series: Estrenos"),
            Pair("$mainUrl/archives/series/top/week/page/$page", "Series: Top Semana"),
            Pair("$mainUrl/archives/series/top/day/page/$page", "Series: Top Hoy"),
            Pair("$mainUrl/archives/movies/page/$page", "Películas"),
            Pair("$mainUrl/archives/movies/releases/page/$page", "Películas: Estrenos"),
            Pair("$mainUrl/archives/movies/top/week/page/$page", "Películas: Top Semana"),
            Pair("$mainUrl/archives/movies/top/day/page/$page", "Películas: Top Hoy"),
        )

        for ((url, title) in catalogs) {
            try {
                val res = app.get(url).text
                val jsonStr = res.substringAfter("id=\"__NEXT_DATA__\" type=\"application/json\">").substringBefore("</script>")
                val data = parseJson<PopularModel>(jsonStr)

                val results = data.props.pageProps.results.data.mapNotNull {
                    val slugPath = it.url.slug ?: it.slug.name ?: return@mapNotNull null
                    val cleanPath = slugPath.removePrefix("/")

                    val finalUrl = if (cleanPath.contains("/")) "$mainUrl/$cleanPath"
                    else if (url.contains("series")) "$mainUrl/series/$cleanPath"
                    else "$mainUrl/movies/$cleanPath"

                    newMovieSearchResponse(it.titles.name ?: "", finalUrl, if (finalUrl.contains("/series/")) TvType.TvSeries else TvType.Movie) {
                        this.posterUrl = it.images.poster?.replace("/original/", "/w300/")
                    }
                }
                if (results.isNotEmpty()) items.add(HomePageList(title, results))
            } catch (e: Exception) { Log.e(TAG, "Error en MainPage: ${e.message}") }
        }
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            val cleanQuery = query.trim().replace(" ", "+")
            val url = "$mainUrl/search?q=$cleanQuery"
            val res = app.get(url, timeout = 20).text

            val jsonStr = res.substringAfter("id=\"__NEXT_DATA__\" type=\"application/json\">")
                .substringBefore("</script>")

            val data = parseJson<PopularModel>(jsonStr)

            data.props.pageProps.results.data.mapNotNull { item ->
                val slugPath = item.url.slug ?: item.slug.name ?: return@mapNotNull null

                val finalUrl = when {
                    slugPath.contains("/") -> "$mainUrl/${slugPath.removePrefix("/")}"
                    item.url.slug?.contains("series") == true -> "$mainUrl/series/$slugPath"
                    else -> "$mainUrl/movies/$slugPath"
                }

                val type = if (finalUrl.contains("/series/")) TvType.TvSeries else TvType.Movie

                newMovieSearchResponse(item.titles.name ?: "Sin título", finalUrl, type) {
                    this.posterUrl = item.images.poster?.replace("/original/", "/w300/")
                }
            }
        } catch (e: Exception) {
            if (e is kotlinx.coroutines.CancellationException) throw e
            Log.e(TAG, "Search Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d(TAG, "load: Cargando -> $url")
        val res = app.get(url).text
        val jsonStr = res.substringAfter("id=\"__NEXT_DATA__\" type=\"application/json\">").substringBefore("</script>")
        val data = parseJson<PopularModel>(jsonStr)
        val pageProps = data.props.pageProps

        if (url.contains("/episodes/") || pageProps.episode != null) {
            return newTvSeriesLoadResponse("Episodio", url, TvType.TvSeries, emptyList()) {
                this.posterUrl = pageProps.post?.images?.poster?.replace("/original/", "/w500/")
                this.plot = pageProps.post?.overview
            }
        }

        val post = pageProps.post ?: throw ErrorLoadingException("No post data")
        val title = post.titles.name ?: "Sin título"

        val hasSeasons = post.seasons.isNotEmpty() && post.seasons.any { it.episodes.isNotEmpty() }

        return if (hasSeasons) {
            Log.d(TAG, "load: Detectada como SERIE (Temporadas: ${post.seasons.size})")
            val episodes = post.seasons.flatMap { season ->
                season.episodes.map { ep ->
                    newEpisode("$mainUrl/series/${ep.slug.name}/seasons/${ep.slug.season}/episodes/${ep.slug.episode}") {
                        this.name = ep.title
                        this.season = season.number?.toInt()
                        this.episode = ep.number?.toInt()
                    }
                }
            }
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes.reversed()) {
                this.posterUrl = post.images.poster?.replace("/original/", "/w500/")
                this.plot = post.overview
            }
        } else {
            Log.d(TAG, "load: Detectada como PELÍCULA")
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = post.images.poster?.replace("/original/", "/w500/")
                this.plot = post.overview
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d(TAG, "loadLinks: Extrayendo enlaces de -> $data")

        return try {
            val res = app.get(data).text

            val jsonStr = res.substringAfter("id=\"__NEXT_DATA__\" type=\"application/json\">")
                .substringBefore("</script>")

            val parsed = parseJson<PopularModel>(jsonStr)
            val pageProps = parsed.props.pageProps

            val players = if (data.contains("/movies/")) {
                pageProps.post?.players
            } else {
                pageProps.episode?.players ?: pageProps.post?.players
            }

            players?.let {
                processLinks(it.latino, "Latino", callback)
                processLinks(it.spanish, "Castellano", callback)
                processLinks(it.english, "Subtitulado", callback)
            }

            true
        } catch (e: Exception) {
            Log.e(TAG, "Error en loadLinks: ${e.message}")
            false
        }
    }

    private suspend fun processLinks(
        list: List<Region>,
        lang: String,
        callback: (ExtractorLink) -> Unit
    ) {
        list.forEach { region ->
            try {
                if (region.result.isBlank()) return@forEach

                val playerPage = app.get(region.result).text

                if (playerPage.contains("var url = '")) {
                    val videoUrl = playerPage.substringAfter("var url = '").substringBefore("';")

                    if (videoUrl.isNotBlank() && videoUrl.startsWith("http")) {

                        loadExtractor(videoUrl, mainUrl, subtitleCallback = { }, callback = { link ->
                            runBlocking {
                                callback.invoke(
                                    newExtractorLink(
                                        source = link.source,
                                        name = "${link.name} [$lang]",
                                        url = link.url,
                                        type = if (link.isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO
                                    ) {
                                        this.referer = link.referer
                                        this.quality = link.quality
                                    }
                                )
                            }
                        })
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en processLinks ($lang): ${e.message}")
            }
        }
    }

}