package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlin.coroutines.cancellation.CancellationException

class PeliculaTvProvider : MainAPI() {

    override var mainUrl = "https://www.themoviedb.org"
    override var name = "PeliCulónTV"
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)
    override var lang = "mx"
    override val hasMainPage = true

    private val tmdbApiKey = "8267afabedf52d963f4d29a95254e784"
    private val apiBase = "https://api.themoviedb.org/3"
    private val imgBase = "https://image.tmdb.org/t/p/w500"
    private val headers = mapOf("Accept" to "application/json")

    override val mainPage = mainPageOf(
        "/trending/movie/week" to "Películas Tendencias",
        "/movie/popular" to "Películas Populares",
        "/trending/tv/week" to "Series Tendencias",
        "/tv/popular" to "Series Populares",
    )

    private fun fixPoster(path: String?): String {
        if (path.isNullOrBlank()) return ""
        return if (path.startsWith("http")) path else "$imgBase$path"
    }

    private suspend fun tmdbRequest(endpoint: String): String? {
        return try {
            val sep = if (endpoint.contains("?")) "&" else "?"
            val url = "$apiBase$endpoint${sep}api_key=$tmdbApiKey&language=es-MX"
            val resp = app.get(url, headers = headers)
            if (resp.text.isNullOrBlank() || resp.text.startsWith("<!DOCTYPE")) {
                Log.w("PeliCulonTV", "tmdbRequest: response no JSON (${resp.text.take(80)}) para $endpoint")
                return null
            }
            resp.text
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "tmdbRequest error ${e.message} para $endpoint")
            null
        }
    }

    private fun parseObj(e: JsonElement?): JsonObject? {
        if (e != null && e !is JsonObject) Log.w("PeliCulonTV", "parseObj esperaba Object, era ${e::class.simpleName}: ${e.toString().take(100)}")
        return e as? JsonObject
    }

    private fun parseArr(e: JsonElement?): JsonArray? {
        if (e != null && e !is JsonArray) Log.w("PeliCulonTV", "parseArr esperaba Array, era ${e::class.simpleName}: ${e.toString().take(100)}")
        return e as? JsonArray
    }

    private fun parseStr(e: JsonElement?): String? = (e as? JsonPrimitive)?.asString

    private fun parseSearchResults(json: String?, type: TvType): List<SearchResponse> {
        val results = mutableListOf<SearchResponse>()
        if (json.isNullOrBlank()) return results
        val root = parseObj(JsonParser.parseString(json)) ?: return results
        val arr = parseArr(root.get("results")) ?: return results
        for (item in arr) {
            val obj = parseObj(item) ?: continue
            val id = obj.get("id")?.asInt ?: continue
            val title = obj.get("title").let { parseStr(it) } ?: obj.get("name").let { parseStr(it) } ?: continue
            val poster = fixPoster(obj.get("poster_path").let { parseStr(it) })
            val year = obj.get("release_date").let { parseStr(it) }?.take(4)?.toIntOrNull()
                ?: obj.get("first_air_date").let { parseStr(it) }?.take(4)?.toIntOrNull()
            val url = "$mainUrl/${if (type == TvType.Movie) "movie" else "tv"}/$id"
            results.add(newMovieSearchResponse(title, url, type) {
                this.posterUrl = poster
                if (year != null) this.year = year
            })
        }
        return results
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        try {
            val json = tmdbRequest(if (page > 1) "${request.data}?page=$page" else request.data) ?: return null
            val type = if (request.data.contains("movie", ignoreCase = true)) TvType.Movie else TvType.TvSeries
            val items = parseSearchResults(json, type)
            val root = parseObj(JsonParser.parseString(json)) ?: return null
            val totalPages = root.get("total_pages")?.asInt ?: 1
            return newHomePageResponse(listOf(HomePageList(request.name, items.take(50))), page < totalPages)
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "getMainPage error", e)
            return null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        if (query.isBlank()) return emptyList()
        val results = mutableListOf<SearchResponse>()
        try {
            val q = query.replace(" ", "%20")
            parseSearchResults(tmdbRequest("/search/movie?query=$q"), TvType.Movie).let { results.addAll(it) }
            parseSearchResults(tmdbRequest("/search/tv?query=$q"), TvType.TvSeries).let { results.addAll(it) }
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "search error query='$query'", e)
        }
        return results.distinctBy { it.url }.take(50)
    }

    override suspend fun load(url: String): LoadResponse? {
        try {
            val movieId = Regex("""/movie/(\d+)""").find(url)?.groupValues?.get(1)
            if (movieId != null) return loadMovie(movieId, url)
            val tvId = Regex("""/tv/(\d+)""").find(url)?.groupValues?.get(1)
            if (tvId != null) return loadTvSeries(tvId, url)
            Log.w("PeliCulonTV", "load: URL no reconocida: $url")
            return null
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "load error (${url.take(60)})", e)
            return null
        }
    }

    private suspend fun loadMovie(id: String, pageUrl: String): LoadResponse? {
        val json = tmdbRequest("/movie/$id?append_to_response=credits,videos,recommendations") ?: return null
        val root = parseObj(JsonParser.parseString(json)) ?: return null
        val title = root.get("title").let { parseStr(it) } ?: run {
            Log.w("PeliCulonTV", "loadMovie $id: sin title, keys=${root.keySet()}")
            return null
        }
        val poster = fixPoster(root.get("poster_path").let { parseStr(it) })
        val bg = fixPoster(root.get("backdrop_path").let { parseStr(it) })
        val year = root.get("release_date").let { parseStr(it) }?.take(4)?.toIntOrNull()
        val voteAvg = root.get("vote_average")?.asFloat
        val genres = parseArr(root.get("genres"))?.mapNotNull { parseObj(it)?.get("name").let { parseStr(it) } } ?: emptyList()

        return newMovieLoadResponse(title, pageUrl, TvType.Movie, "movie:$id") {
            this.posterUrl = poster
            this.backgroundPosterUrl = bg
            this.plot = root.get("overview").let { parseStr(it) } ?: ""
            if (year != null) this.year = year
            if (voteAvg != null) this.score = Score.from10((voteAvg * 10).toInt())
            this.tags = genres
            this.duration = root.get("runtime")?.asInt
            this.recommendations = parseRecommendations(parseArr(parseObj(root.get("recommendations"))?.get("results")))
        }
    }

    private suspend fun loadTvSeries(id: String, pageUrl: String): LoadResponse? {
        val json = tmdbRequest("/tv/$id?append_to_response=credits,videos,recommendations") ?: return null
        val root = parseObj(JsonParser.parseString(json)) ?: return null
        val title = root.get("name").let { parseStr(it) } ?: run {
            Log.w("PeliCulonTV", "loadTvSeries $id: sin name, keys=${root.keySet()}")
            return null
        }
        val poster = fixPoster(root.get("poster_path").let { parseStr(it) })
        val bg = fixPoster(root.get("backdrop_path").let { parseStr(it) })
        val year = root.get("first_air_date").let { parseStr(it) }?.take(4)?.toIntOrNull()
        val voteAvg = root.get("vote_average")?.asFloat
        val genres = parseArr(root.get("genres"))?.mapNotNull { parseObj(it)?.get("name").let { parseStr(it) } } ?: emptyList()
        val seasons = parseArr(root.get("seasons"))?.mapNotNull {
            parseObj(it)?.get("season_number")?.asInt?.takeIf { s -> s > 0 }
        }?.sorted() ?: listOf(1)
        if (seasons.isEmpty()) return null
        Log.i("PeliCulonTV", "loadTvSeries $id: $title, ${seasons.size} temporadas: $seasons")

        val episodes = mutableListOf<Episode>()
        for (seasonNum in seasons) {
            val seasonJson = tmdbRequest("/tv/$id/season/$seasonNum") ?: continue
            val seasonRoot = parseObj(JsonParser.parseString(seasonJson))
            if (seasonRoot == null) {
                Log.w("PeliCulonTV", "S${seasonNum} de $id: root no es Object, json=${seasonJson.take(100)}")
                continue
            }
            val epList = parseArr(seasonRoot.get("episodes"))
            if (epList == null) {
                Log.w("PeliCulonTV", "S${seasonNum} de $id: 'episodes' no es Array, keys=${seasonRoot.keySet()}")
                continue
            }
            for (ep in epList) {
                val epObj = parseObj(ep) ?: continue
                val epNum = epObj.get("episode_number")?.asInt ?: continue
                episodes.add(newEpisode("tv:$id:$seasonNum:$epNum") {
                    this.name = epObj.get("name").let { parseStr(it) } ?: "Episodio $epNum"
                    this.episode = epNum
                    this.season = seasonNum
                    this.description = epObj.get("overview").let { parseStr(it) } ?: ""
                    this.posterUrl = fixPoster(epObj.get("still_path").let { parseStr(it) })
                })
            }
        }
        episodes.sortWith(compareBy({ it.season ?: 1 }, { it.episode ?: 1 }))
        Log.i("PeliCulonTV", "loadTvSeries $id: ${episodes.size} episodios totales (pre-return)")
        if (episodes.isEmpty()) {
            Log.w("PeliCulonTV", "loadTvSeries $id: 0 episodios, no se crea LoadResponse")
            return null
        }

        return newTvSeriesLoadResponse(title, pageUrl, TvType.TvSeries, episodes) {
            this.posterUrl = poster
            this.backgroundPosterUrl = bg
            this.plot = root.get("overview").let { parseStr(it) } ?: ""
            if (year != null) this.year = year
            if (voteAvg != null) this.score = Score.from10((voteAvg * 10).toInt())
            this.tags = genres
            this.recommendations = parseRecommendations(parseArr(parseObj(root.get("recommendations"))?.get("results")))
        }
    }

    private fun parseRecommendations(arr: JsonArray?): List<SearchResponse>? {
        if (arr == null) return null
        val list = mutableListOf<SearchResponse>()
        for (item in arr) {
            try {
                val obj = parseObj(item) ?: continue
                val id = obj.get("id")?.asInt ?: continue
                val title = obj.get("title").let { parseStr(it) } ?: obj.get("name").let { parseStr(it) } ?: continue
                val poster = fixPoster(obj.get("poster_path").let { parseStr(it) })
                val isTv = obj.get("media_type").let { parseStr(it) } == "tv"
                val type = if (isTv) TvType.TvSeries else TvType.Movie
                list.add(newMovieSearchResponse(title, "$mainUrl/${if (isTv) "tv" else "movie"}/$id", type) {
                    this.posterUrl = poster
                })
            } catch (_: Exception) {}
        }
        return list.ifEmpty { null }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        try {
            val movieMatch = Regex("""movie:(\d+)""").find(data)
            val tvMatch = Regex("""tv:(\d+):(\d+):(\d+)""").find(data)
            val candidateUrls = mutableListOf<String>()
            var debugType = ""

            if (movieMatch != null) {
                val tmdbId = movieMatch.groupValues[1]
                debugType = "movie:$tmdbId"
                candidateUrls.add("https://unlimplay.com/play/embed/movie/$tmdbId")
                candidateUrls.add("https://embed.su/embed/movie/$tmdbId")
                candidateUrls.add("https://vidsrc.pro/embed/movie/$tmdbId")
            } else if (tvMatch != null) {
                val tmdbId = tvMatch.groupValues[1]
                val season = tvMatch.groupValues[2]
                val episode = tvMatch.groupValues[3]
                debugType = "tv:$tmdbId S${season}E${episode}"
                candidateUrls.add("https://unlimplay.com/play/embed/tv/$tmdbId/$season/$episode")
                candidateUrls.add("https://embed.su/embed/tv/$tmdbId/$season/$episode")
                candidateUrls.add("https://vidsrc.pro/embed/tv/$tmdbId/$season/$episode")
            } else {
                Log.w("PeliCulonTV", "loadLinks: data no reconocida: $data")
                return false
            }

            Log.i("PeliCulonTV", "loadLinks[$debugType]: candidatos=${candidateUrls.size} -> ${candidateUrls.joinToString(" | ") { it.substringAfter("//") }}")

            var linksCount = 0
            for (url in candidateUrls) {
                try {
                    val before = linksCount
                    linksCount += if (url.contains("unlimplay.com")) {
                        processUnlimplay(url, subtitleCallback, callback, debugType)
                    } else {
                        var count = 0
                        val trackCb: (ExtractorLink) -> Unit = { link -> count++; callback(link) }
                        val found = loadExtractor(url, mainUrl, subtitleCallback, trackCb)
                        Log.i("PeliCulonTV", "loadLinks[$debugType]: ${url.substringAfter("//embed.")} -> loadExtractor=$found links=$count")
                        count
                    }
                    val added = linksCount - before
                    Log.i("PeliCulonTV", "loadLinks[$debugType]: ${url.substringAfter("//")} -> +${added} links (total=$linksCount)")
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.w("PeliCulonTV", "loadLinks[$debugType]: error ${e.message} en ${url.substringAfter("//")}")
                }
            }
            Log.i("PeliCulonTV", "loadLinks[$debugType]: FINAL links=$linksCount")
            return linksCount > 0
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("PeliCulonTV", "loadLinks error: ${e.message}")
            return false
        }
    }

    private suspend fun processUnlimplay(
        url: String,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit,
        debugType: String
    ): Int {
        var count = 0
        val html = app.get(url).text
        Log.i("PeliCulonTV", "processUnlimplay[$debugType]: HTML len=${html.length}")

        val embedsMatch = Regex("""const\s+EMBEDS\s*=\s*(\{.+?\});""", RegexOption.DOT_MATCHES_ALL)
            .find(html)
        if (embedsMatch == null) {
            Log.w("PeliCulonTV", "processUnlimplay[$debugType]: no EMBEDS encontrado en HTML")
            return 0
        }

        val root = parseObj(JsonParser.parseString(embedsMatch.groupValues[1]))
        if (root == null) {
            Log.w("PeliCulonTV", "processUnlimplay[$debugType]: EMBEDS no es JSON object: ${embedsMatch.groupValues[1].take(100)}")
            return 0
        }

        val langCount = root.entrySet().size
        Log.i("PeliCulonTV", "processUnlimplay[$debugType]: EMBEDS parseado OK, ${langCount} idioma(s)")

        for ((lang, serversElement) in root.entrySet()) {
            val serversObj = parseObj(serversElement)
            if (serversObj == null) {
                Log.w("PeliCulonTV", "processUnlimplay[$debugType]: lang='$lang' no es Object")
                continue
            }
            Log.i("PeliCulonTV", "processUnlimplay[$debugType]: idioma=$lang servidores=${serversObj.entrySet().map { it.key }}")

            for ((name, urlElement) in serversObj.entrySet()) {
                val embedUrl = parseStr(urlElement)
                if (embedUrl == null) {
                    Log.w("PeliCulonTV", "processUnlimplay[$debugType]: lang=$lang server=$name URL no es string")
                    continue
                }
                Log.i("PeliCulonTV", "processUnlimplay[$debugType]: -> server=$name lang=$lang url=$embedUrl")

                try {
                    if (embedUrl.contains("remux.unlimplay.com")) {
                        callback(newExtractorLink("Remux", "Remux", embedUrl, INFER_TYPE) {
                            this.referer = "https://unlimplay.com"
                        })
                        count++
                        Log.i("PeliCulonTV", "processUnlimplay[$debugType]: REMUX link agregado: $embedUrl")
                    } else {
                        var subCount = 0
                        val trackCb: (ExtractorLink) -> Unit = { link ->
                            subCount++
                            Log.i("PeliCulonTV", "processUnlimplay[$debugType]: loadExtractor link #$subCount: ${link.url.take(80)}")
                            callback(link)
                        }
                        val ok = loadExtractor(embedUrl, "https://unlimplay.com", subtitleCallback, trackCb)
                        Log.i("PeliCulonTV", "processUnlimplay[$debugType]: loadExtractor($name)=$ok links=$subCount")

                        if (!ok || subCount == 0) {
                            Log.i("PeliCulonTV", "processUnlimplay[$debugType]: fallback tryExtractDirect($name)")
                            val direct = tryExtractDirect(embedUrl, name, callback)
                            Log.i("PeliCulonTV", "processUnlimplay[$debugType]: tryExtractDirect($name)=$direct")
                            subCount += direct
                        }
                        count += subCount
                        Log.i("PeliCulonTV", "processUnlimplay[$debugType]: server=$name total subCount=$subCount")
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.w("PeliCulonTV", "processUnlimplay[$debugType]: error ${e.message} en $name ($embedUrl)")
                }
            }
        }
        Log.i("PeliCulonTV", "processUnlimplay[$debugType]: total links de unlimplay=$count")
        return count
    }

    private suspend fun tryExtractDirect(
        embedUrl: String,
        name: String,
        callback: (ExtractorLink) -> Unit
    ): Int {
        return try {
            Log.i("PeliCulonTV", "tryExtractDirect: fetching $name ($embedUrl)")
            val embedHtml = app.get(embedUrl).text
            Log.i("PeliCulonTV", "tryExtractDirect: $name HTML len=${embedHtml.length}")
            val m3u8 = Regex("""(https?://[^"'<>\s]+\.m3u8[^"'<>\s]*)""").find(embedHtml)?.value
            if (m3u8 != null) {
                Log.i("PeliCulonTV", "tryExtractDirect: $name M3U8 encontrado: ${m3u8.take(100)}")
                callback(newExtractorLink(name, name, m3u8, ExtractorLinkType.M3U8) {
                    this.referer = embedUrl
                })
                1
            } else {
                Log.w("PeliCulonTV", "tryExtractDirect: $name sin M3U8 en HTML")
                0
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.w("PeliCulonTV", "tryExtractDirect: $name error: ${e.message}")
            0
        }
    }
}
