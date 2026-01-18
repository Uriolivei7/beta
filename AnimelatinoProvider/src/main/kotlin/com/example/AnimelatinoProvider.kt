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

    private val json = Json { ignoreUnknownKeys = true }

    // 1. Killer de Cloudflare para saltar protecciones
    private val cfKiller = CloudflareKiller()

    private fun getNextProps(document: Document): JsonObject? {
        return try {
            val script = document.select("script").find { it.data().contains("{\"props\":{\"pageProps\":") }
            val dataText = script?.data()
            if (dataText == null) {
                Log.e("AnimeLatino", "No se encontró el script de Next.js")
                return null
            }
            json.parseToJsonElement(dataText).jsonObject["props"]?.jsonObject?.get("pageProps")?.jsonObject?.get("data")?.jsonObject
        } catch (e: Exception) {
            Log.e("AnimeLatino", "Error parseando NextProps: ${e.message}")
            null
        }
    }

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        Log.d("AnimeLatino", "Cargando Home...")
        val items = mutableListOf<HomePageList>()

        // Usamos interceptor de Cloudflare en la petición
        val urls = listOf(
            Pair("$mainUrl/animes/populares", "Populares"),
            Pair("$mainUrl/animes?status=1", "En Emisión")
        )

        for ((url, title) in urls) {
            val res = app.get(url, interceptor = cfKiller)
            val data = getNextProps(res.document)
            val list = mutableListOf<SearchResponse>()

            val array = if (url.contains("populares")) data?.get("popular_today")?.jsonArray
            else data?.get("data")?.jsonArray

            array?.forEach { item ->
                val obj = item.jsonObject
                val name = obj["name"]?.jsonPrimitive?.content ?: ""
                val slug = obj["slug"]?.jsonPrimitive?.content ?: ""
                val poster = "https://image.tmdb.org/t/p/w200${obj["poster"]?.jsonPrimitive?.content}"

                list.add(newAnimeSearchResponse(name, "$mainUrl/anime/$slug") {
                    this.posterUrl = poster
                })
            }
            items.add(HomePageList(title, list))
        }
        return newHomePageResponse(items)
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("AnimeLatino", "Cargando info de: $url")
        val res = app.get(url, interceptor = cfKiller)
        val data = getNextProps(res.document) ?: throw ErrorLoadingException("JSON null")

        val title = data["name"]!!.jsonPrimitive.content
        val poster = "https://image.tmdb.org/t/p/w600_and_h900_bestv2${data["poster"]?.jsonPrimitive?.content}"

        val episodes = data["episodes"]?.jsonArray?.map { ep ->
            val obj = ep.jsonObject
            val num = obj["number"]!!.jsonPrimitive.content
            newEpisode("$mainUrl/ver/${data["slug"]!!.jsonPrimitive.content}/$num") {
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
        Log.d("AnimeLatino", "Cargando links para: $data")
        val res = app.get(data, interceptor = cfKiller)
        val nextData = getNextProps(res.document)
        val players = nextData?.get("players")?.jsonArray ?: return@coroutineScope false

        // 2. Uso de bucles 'for' para evitar errores de Corrutinas
        for (langGroup in players) {
            for (player in langGroup.jsonArray) {
                val obj = player.jsonObject
                val id = obj["id"]?.jsonPrimitive?.content
                val lang = if (obj["languaje"]?.jsonPrimitive?.content == "1") "Lat" else "Sub"

                // Petición a la API de streaming
                val apiRes = app.get(
                    "https://api.animelatinohd.com/stream/$id",
                    headers = mapOf("Referer" to "$mainUrl/")
                )

                Log.d("AnimeLatino", "Respuesta API Stream: ${apiRes.text}")

                val streamUrl = fetchUrlFromResponse(apiRes.text)

                if (streamUrl != null) {
                    Log.d("AnimeLatino", "Extrayendo de: $streamUrl")
                    loadExtractor(streamUrl, "$mainUrl/", subtitleCallback) { link ->
                        runBlocking { // Crea un puente para llamar a la suspend fun newExtractorLink
                            callback.invoke(
                                newExtractorLink(
                                    source = link.source,
                                    name = "${link.name} [$lang]",
                                    url = link.url
                                ) {
                                    this.quality = link.quality
                                    this.referer = link.referer
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