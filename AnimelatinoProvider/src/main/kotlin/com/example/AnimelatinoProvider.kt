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

    // Definimos los headers como una variable normal
    private val commonHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36",
        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8",
        "Accept-Language" to "es-MX,es;q=0.9,en-US;q=0.8,en;q=0.7",
    )

    private fun getNextProps(document: Document): JsonObject? {
        return try {
            val script = document.select("script#__NEXT_DATA__").first()
                ?: document.select("script").find { it.data().contains("props") }

            val dataText = script?.data()
            if (dataText == null) {
                Log.e("AnimeLatino", "Script no encontrado. Título: ${document.title()}")
                return null
            }

            val root = json.parseToJsonElement(dataText).jsonObject
            val pageProps = root["props"]?.jsonObject?.get("pageProps")?.jsonObject
            pageProps?.get("data")?.jsonObject ?: pageProps
        } catch (e: Exception) {
            Log.e("AnimeLatino", "Error parseando NextProps: ${e.message}")
            null
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse = coroutineScope {
        Log.d("AnimeLatino", "Iniciando bypass de red...")

        // Calentamiento
        try { app.get(mainUrl, interceptor = cfKiller, headers = commonHeaders) } catch (e: Exception) {}

        val items = mutableListOf<HomePageList>()
        val urls = listOf(
            Pair("$mainUrl/animes", "Animes"),
            Pair("$mainUrl/animes/latino", "Latino"),
            Pair("$mainUrl/animes/populares", "Populares")
        )

        for ((url, title) in urls) {
            try {
                Log.d("AnimeLatino", "Solicitando $title...")
                val res = app.get(
                    url,
                    interceptor = cfKiller,
                    timeout = 45,
                    headers = commonHeaders + mapOf("Referer" to "$mainUrl/")
                )

                Log.d("AnimeLatino", "Respuesta recibida: ${res.code}")

                val data = getNextProps(res.document)
                val list = mutableListOf<SearchResponse>()
                val array = if (url.contains("populares")) data?.get("popular_today")?.jsonArray
                else data?.get("data")?.jsonArray ?: data?.get("animes")?.jsonArray

                array?.forEach { item ->
                    val obj = item.jsonObject
                    val name = obj["name"]?.jsonPrimitive?.content ?: ""
                    val slug = obj["slug"]?.jsonPrimitive?.content ?: ""
                    val posterPath = obj["poster"]?.jsonPrimitive?.content ?: ""
                    val poster = if (posterPath.startsWith("http")) posterPath else "https://image.tmdb.org/t/p/w200$posterPath"

                    list.add(newAnimeSearchResponse(name, "$mainUrl/anime/$slug") {
                        this.posterUrl = poster
                    })
                }
                if (list.isNotEmpty()) items.add(HomePageList(title, list))
            } catch (e: Exception) {
                Log.e("AnimeLatino", "Error en sección $title: ${e.message}")
            }
        }
        newHomePageResponse(items)
    }

    override suspend fun load(url: String): LoadResponse {
        val res = app.get(url, interceptor = cfKiller, headers = commonHeaders)
        val data = getNextProps(res.document) ?: throw ErrorLoadingException("JSON null")

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
        val res = app.get(data, interceptor = cfKiller, headers = commonHeaders)
        val nextData = getNextProps(res.document)
        val players = nextData?.get("players")?.jsonArray ?: return@coroutineScope false

        for (langGroup in players) {
            for (player in langGroup.jsonArray) {
                val obj = player.jsonObject
                val id = obj["id"]?.jsonPrimitive?.content ?: continue
                val langLabel = if (obj["languaje"]?.jsonPrimitive?.content == "1") "Lat" else "Sub"

                val apiRes = app.get(
                    "https://api.animelatinohd.com/stream/$id",
                    headers = commonHeaders + mapOf("Referer" to "$mainUrl/", "X-Requested-With" to "XMLHttpRequest")
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
}