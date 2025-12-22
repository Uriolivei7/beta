package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// 1. DATA CLASSES CORREGIDAS PARA NEXT.JS DINÁMICO
@Serializable
data class PopularModel(
    val props: Props? = null,
    // A veces pageProps viene en la raíz del JSON
    val pageProps: PageProps? = null
)

@Serializable
data class Props(
    val pageProps: PageProps? = null
)

@Serializable
data class PageProps(
    val results: Results? = null,
    val post: SeasonPost? = null,
    val episode: EpisodeData? = null,
    // A veces los datos vienen dentro de un objeto "data" en lugar de "post"
    val data: SeasonPost? = null
)

@Serializable data class Results(val data: List<Daum> = emptyList())

@Serializable data class Daum(
    val titles: Titles = Titles(),
    val images: Images = Images(),
    val slug: Slug = Slug(),
    val url: Url = Url(),
    val releaseDate: String? = null,
    @SerialName("__typename") val typeName: String? = null
)

@Serializable data class SeasonPost(
    val titles: Titles = Titles(),
    val images: Images = Images(),
    val overview: String? = null,
    val seasons: List<Season> = emptyList(),
    val players: Players? = null,
    val releaseDate: String? = null,
    val runtime: Int? = null,
    val genres: List<Genre>? = null
)

@Serializable data class Genre(val name: String? = null)
@Serializable data class Titles(val name: String? = null)
@Serializable data class Images(val poster: String? = null)
@Serializable data class Slug(val name: String? = null)
@Serializable data class Url(val slug: String? = null)
@Serializable data class Season(val number: Long? = null, val episodes: List<SeasonEpisode> = emptyList())

@Serializable
data class SeasonEpisode(
    val title: String? = null,
    val number: Long? = null,
    val slug: Slug2 = Slug2(),
    val images: Images = Images(),
    val overview: String? = null
)

@Serializable data class Slug2(val name: String? = null, val season: String? = null, val episode: String? = null)
@Serializable data class EpisodeData(val players: Players? = null)

@Serializable data class Players(
    val latino: List<Region> = emptyList(),
    val spanish: List<Region> = emptyList(),
    val english: List<Region> = emptyList()
)

@Serializable
data class Region(
    val result: String = "",
    val url: String? = null,
    val link: String? = null
)

class GnulaProvider : MainAPI() {
    override var mainUrl = "https://gnula.life"
    override var name = "GNULA"
    override val hasMainPage = true
    override var lang = "mx"
    override val supportedTypes = setOf(
        TvType.Movie, TvType.TvSeries, TvType.Anime, TvType.Cartoon
    )

    private val TAG = "GNULA"

    // Helper para extraer el JSON de Next.js de forma segura
    private fun getNextData(res: String): PageProps? {
        return try {
            val jsonStr = res.substringAfter("id=\"__NEXT_DATA__\" type=\"application/json\">").substringBefore("</script>")
            val model = parseJson<PopularModel>(jsonStr)
            model.pageProps ?: model.props?.pageProps
        } catch (e: Exception) { null }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
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
                val pProps = getNextData(res)
                val results = pProps?.results?.data?.mapNotNull { item ->
                    val slugPath = item.url.slug ?: item.slug.name ?: return@mapNotNull null
                    val finalUrl = "$mainUrl/${slugPath.removePrefix("/")}"
                    newMovieSearchResponse(item.titles.name ?: "", finalUrl, if (finalUrl.contains("/series/")) TvType.TvSeries else TvType.Movie) {
                        this.posterUrl = item.images.poster?.replace("/original/", "/w300/")
                        this.year = item.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()
                    }
                } ?: emptyList()
                if (results.isNotEmpty()) items.add(HomePageList(title, results))
            } catch (e: Exception) { Log.e(TAG, "MainPage Error: ${e.message}") }
        }
        return newHomePageResponse(items)
    }

    override suspend fun search(query: String): List<SearchResponse> {
        return try {
            val url = "$mainUrl/search?q=${query.trim().replace(" ", "+")}"
            val res = app.get(url).text
            val pProps = getNextData(res)

            pProps?.results?.data?.mapNotNull { item ->
                val slugPath = item.url.slug ?: item.slug.name ?: return@mapNotNull null
                val finalUrl = "$mainUrl/${slugPath.removePrefix("/")}"
                newMovieSearchResponse(item.titles.name ?: "Sin título", finalUrl, if (finalUrl.contains("/series/")) TvType.TvSeries else TvType.Movie) {
                    this.posterUrl = item.images.poster?.replace("/original/", "/w300/")
                }
            } ?: emptyList()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun load(url: String): LoadResponse {
        val resText = app.get(url).text
        val pProps = getNextData(resText) ?: throw ErrorLoadingException("No se pudo cargar la info")

        // Buscamos en 'post' o en 'data'
        val post = pProps.post ?: pProps.data ?: throw ErrorLoadingException("Contenido vacío")
        val title = post.titles.name ?: "Sin título"
        val year = post.releaseDate?.split("-")?.firstOrNull()?.toIntOrNull()

        return if (post.seasons.isNotEmpty()) {
            val episodes = post.seasons.flatMap { season ->
                season.episodes.map { ep ->
                    newEpisode("$mainUrl/series/${ep.slug.name}/seasons/${ep.slug.season}/episodes/${ep.slug.episode}") {
                        this.name = ep.title
                        this.season = season.number?.toInt()
                        this.episode = ep.number?.toInt()
                        this.posterUrl = ep.images.poster?.replace("/original/", "/w300/")
                    }
                }
            }
            newTvSeriesLoadResponse(title, url, TvType.TvSeries, episodes.reversed()) {
                this.posterUrl = post.images.poster?.replace("/original/", "/w500/")
                this.plot = post.overview
                this.year = year
                this.tags = post.genres?.mapNotNull { it.name }
            }
        } else {
            newMovieLoadResponse(title, url, TvType.Movie, url) {
                this.posterUrl = post.images.poster?.replace("/original/", "/w500/")
                this.plot = post.overview
                this.year = year
                this.duration = post.runtime
                this.tags = post.genres?.mapNotNull { it.name }
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        return try {
            val res = app.get(data).text
            val pProps = getNextData(res)

            // Buscamos players en todas las combinaciones posibles
            val players = pProps?.episode?.players ?: pProps?.post?.players ?: pProps?.data?.players

            if (players == null) {
                Log.e(TAG, "loadLinks: No se encontró el objeto players")
                return false
            }

            var foundLinks = false
            val langs = listOf(players.latino to "Latino", players.spanish to "Castellano", players.english to "Subtitulado")

            for ((list, lang) in langs) {
                if (list.isNotEmpty()) {
                    processLinks(list, lang, callback)
                    foundLinks = true
                }
            }
            foundLinks
        } catch (e: Exception) { false }
    }

    private suspend fun processLinks(list: List<Region>, lang: String, callback: (ExtractorLink) -> Unit) {
        list.forEach { region ->
            try {
                // Gnula a veces usa 'result', otras 'url' o 'link'
                val targetUrl = region.result.ifBlank { region.url ?: region.link ?: "" }
                if (targetUrl.isBlank()) return@forEach

                val playerPage = app.get(targetUrl, referer = "$mainUrl/").text
                if (playerPage.contains("var url = '")) {
                    val videoUrl = playerPage.substringAfter("var url = '").substringBefore("';")
                    loadExtractor(videoUrl, mainUrl, subtitleCallback = { }) { link ->
                        GlobalScope.launch(kotlinx.coroutines.Dispatchers.Main) {
                            callback.invoke(
                                newExtractorLink(link.source, "${link.name} [$lang]", link.url, if (link.isM3u8) ExtractorLinkType.M3U8 else ExtractorLinkType.VIDEO) {
                                    this.referer = link.referer
                                    this.quality = link.quality
                                    this.headers = link.headers
                                }
                            )
                        }
                    }
                }
            } catch (e: Exception) { }
        }
    }
}