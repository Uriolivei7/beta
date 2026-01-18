package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import com.lagradost.cloudstream3.network.CloudflareKiller
import org.jsoup.nodes.Document
import kotlinx.serialization.json.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class AnimelatinoProvider : MainAPI() {
    override var mainUrl = "https://www.animelatinohd.com"
    override var name = "AnimeLatinoHD"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)
    override var lang = "es"
    override val hasMainPage = true

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private val cfKiller = CloudflareKiller()

    // Headers maestros basados en tu CURL de Brave/Chrome 144
    private val rscHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
        "RSC" to "1",
        "Next-Router-Prefetch" to "1",
        "Next-Url" to "/",
        "Accept" to "*/*",
        "Accept-Language" to "es-ES,es;q=0.7",
        "sec-ch-ua" to "\"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"144\", \"Brave\";v=\"144\"",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "empty",
        "sec-fetch-mode" to "cors",
        "sec-fetch-site" to "same-origin"
    )

    private fun getNextProps(document: Document): JsonObject? {
        return try {
            val script = document.select("script#__NEXT_DATA__").first()
                ?: document.select("script").find { it.data().contains("props") }

            val dataText = script?.data() ?: return null
            val root = json.parseToJsonElement(dataText).jsonObject
            val pageProps = root["props"]?.jsonObject?.get("pageProps")?.jsonObject
            pageProps?.get("data")?.jsonObject ?: pageProps
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse = coroutineScope {
        Log.d("AnimeLatino", "Iniciando peticiones RSC...")
        val items = mutableListOf<HomePageList>()

        // URLs con el token rsc que obtuviste
        val sections = listOf(
            Pair("$mainUrl/animes?_rsc=1ld0r", "Animes"),
            Pair("$mainUrl/animes/latino?_rsc=1ld0r", "Latino"),
            Pair("$mainUrl/animes/populares?_rsc=1ld0r", "Populares")
        )

        for ((url, title) in sections) {
            try {
                val res = app.get(url, interceptor = cfKiller, headers = rscHeaders, timeout = 30)

                // Extraemos la lista de animes del texto RSC
                val animeList = parseRscList(res.text)
                val searchResponses = animeList.map { obj ->
                    val name = obj["name"]?.jsonPrimitive?.content ?: ""
                    val slug = obj["slug"]?.jsonPrimitive?.content ?: ""
                    val posterPath = obj["poster"]?.jsonPrimitive?.content ?: ""
                    val poster = if (posterPath.startsWith("http")) posterPath else "https://image.tmdb.org/t/p/w200$posterPath"

                    newAnimeSearchResponse(name, "$mainUrl/anime/$slug") {
                        this.posterUrl = poster
                    }
                }

                if (searchResponses.isNotEmpty()) {
                    items.add(HomePageList(title, searchResponses))
                }
            } catch (e: Exception) {
                Log.e("AnimeLatino", "Error en sección $title: ${e.message}")
            }
        }
        newHomePageResponse(items)
    }

    override suspend fun load(url: String): LoadResponse {
        // En load usamos la página normal (HTML), por eso no enviamos el header RSC
        val res = app.get(url, interceptor = cfKiller, headers = mapOf("User-Agent" to rscHeaders["User-Agent"]!!))
        val data = getNextProps(res.document) ?: throw ErrorLoadingException("Error al cargar datos del anime")

        val title = data["name"]?.jsonPrimitive?.content ?: ""
        val posterPath = data["poster"]?.jsonPrimitive?.content ?: ""
        val poster = if (posterPath.startsWith("http")) posterPath else "https://image.tmdb.org/t/p/w600_and_h900_bestv2$posterPath"

        val episodes = data["episodes"]?.jsonArray?.mapNotNull { ep ->
            val obj = ep.jsonObject
            val num = obj["number"]?.jsonPrimitive?.content ?: return@mapNotNull null
            newEpisode("$mainUrl/ver/${data["slug"]?.jsonPrimitive?.content}/$num") {
                this.name = "Episodio $num"
                this.episode = num.split(".")[0].toIntOrNull()
            }
        }?.reversed() ?: emptyList()

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = data["overview"]?.jsonPrimitive?.content
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean = coroutineScope {
        val res = app.get(data, interceptor = cfKiller, headers = mapOf("User-Agent" to rscHeaders["User-Agent"]!!))
        val nextData = getNextProps(res.document)
        val players = nextData?.get("players")?.jsonArray ?: return@coroutineScope false

        for (langGroup in players) {
            for (player in langGroup.jsonArray) {
                val obj = player.jsonObject
                val id = obj["id"]?.jsonPrimitive?.content ?: continue
                val langLabel = if (obj["languaje"]?.jsonPrimitive?.content == "1") "Lat" else "Sub"

                val apiRes = app.get(
                    "https://api.animelatinohd.com/stream/$id",
                    headers = mapOf(
                        "User-Agent" to rscHeaders["User-Agent"]!!,
                        "Referer" to "$mainUrl/",
                        "X-Requested-With" to "XMLHttpRequest"
                    )
                )

                val streamUrl = fetchUrlFromResponse(apiRes.text)
                if (streamUrl != null) {
                    loadExtractor(streamUrl, "$mainUrl/", subtitleCallback) { link ->
                        runBlocking {
                            callback.invoke(
                                newExtractorLink(link.source, "${link.name} [$langLabel]", link.url) {
                                    this.referer = mainUrl
                                }
                            )
                        }
                    }
                }
            }
        }
        true
    }

    private fun fetchUrlFromResponse(text: String): String? {
        val linkRegex = """https?://[^\s"']+""".toRegex()
        return linkRegex.find(text)?.value
    }

    // Nueva función para extraer la lista de animes del formato RSC de Next.js
    private fun parseRscList(text: String): List<JsonObject> {
        return try {
            // Buscamos todos los fragmentos que parecen objetos de anime dentro del RSC
            val regex = Regex("""\{"id":\d+,"name":"[^"]+","slug":"[^"]+","poster":"[^"]+"[^\}]*\}""")
            regex.findAll(text).map {
                json.parseToJsonElement(it.value).jsonObject
            }.toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}