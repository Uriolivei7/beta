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

    // Headers optimizados basados en tu inspección (CURL)
    private val rscHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
        "Accept" to "*/*",
        "Accept-Language" to "es-ES,es;q=0.7",
        "RSC" to "1",
        "Next-Router-Prefetch" to "1",
        "Next-Url" to "/",
        "sec-ch-ua" to "\"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"144\", \"Brave\";v=\"144\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "empty",
        "sec-fetch-mode" to "cors",
        "sec-fetch-site" to "same-origin"
    )

    private val rscToken = "_rsc=1ld0r"

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse = coroutineScope {
        Log.d("AnimeLatino", "--- INICIANDO MAIN PAGE (RSC MODE) ---")
        val items = mutableListOf<HomePageList>()

        val sections = listOf(
            Pair("$mainUrl/animes?$rscToken", "Animes"),
            Pair("$mainUrl/animes/latino?$rscToken", "Latino"),
            Pair("$mainUrl/animes/populares?$rscToken", "Populares")
        )

        for ((url, title) in sections) {
            try {
                Log.d("AnimeLatino", ">> Solicitando sección: $title | URL: $url")

                val res = app.get(url, interceptor = cfKiller, headers = rscHeaders, timeout = 30)

                Log.d("AnimeLatino", "<< Respuesta recibida: ${res.code} | Tamaño: ${res.text.length} bytes")

                if (res.code == 200) {
                    val animeList = parseRscList(res.text)
                    Log.d("AnimeLatino", "++ Animes parseados en $title: ${animeList.size}")

                    val searchResponses = animeList.map { obj ->
                        val name = obj["name"]?.jsonPrimitive?.content ?: ""
                        val slug = obj["slug"]?.jsonPrimitive?.content ?: ""
                        val posterPath = obj["poster"]?.jsonPrimitive?.content ?: ""
                        val poster = if (posterPath.startsWith("http")) posterPath else "https://image.tmdb.org/t/p/w200$posterPath"

                        newAnimeSearchResponse(name, "$mainUrl/anime/$slug") {
                            this.posterUrl = poster
                        }
                    }
                    if (searchResponses.isNotEmpty()) items.add(HomePageList(title, searchResponses))
                } else {
                    Log.e("AnimeLatino", "!! Error de servidor en $title: Código ${res.code}")
                }
            } catch (e: Exception) {
                Log.e("AnimeLatino", "XX Excepción en $title: ${e.message}")
                e.printStackTrace()
            }
        }
        newHomePageResponse(items)
    }

    override suspend fun load(url: String): LoadResponse {
        Log.d("AnimeLatino", "--- CARGANDO DETALLES: $url ---")
        val rscUrl = if (url.contains("?")) "$url&$rscToken" else "$url?$rscToken"

        val res = app.get(rscUrl, interceptor = cfKiller, headers = rscHeaders)
        Log.d("AnimeLatino", "Respuesta LOAD: ${res.code}")

        val data = parseRscList(res.text).firstOrNull()
            ?: throw ErrorLoadingException("No se encontró el objeto de anime en el RSC")

        val title = data["name"]?.jsonPrimitive?.content ?: ""
        Log.d("AnimeLatino", "Cargando anime: $title")

        val posterPath = data["poster"]?.jsonPrimitive?.content ?: ""
        val poster = if (posterPath.startsWith("http")) posterPath else "https://image.tmdb.org/t/p/w600_and_h900_bestv2$posterPath"

        val episodes = data["episodes"]?.jsonArray?.mapNotNull { ep ->
            val obj = ep.jsonObject
            val num = obj["number"]?.jsonPrimitive?.content ?: return@mapNotNull null
            val slug = data["slug"]?.jsonPrimitive?.content ?: return@mapNotNull null

            newEpisode("$mainUrl/ver/$slug/$num") {
                this.name = "Episodio $num"
                this.episode = num.split(".")[0].toIntOrNull()
            }
        }?.reversed() ?: emptyList()

        Log.d("AnimeLatino", "Total episodios encontrados: ${episodes.size}")

        return newAnimeLoadResponse(title, url, TvType.Anime) {
            this.posterUrl = poster
            this.plot = data["overview"]?.jsonPrimitive?.content
            this.showStatus = if (data["status"]?.jsonPrimitive?.content == "1") ShowStatus.Ongoing else ShowStatus.Completed
            addEpisodes(DubStatus.Subbed, episodes)
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean = coroutineScope {
        Log.d("AnimeLatino", "--- BUSCANDO LINKS EN: $data ---")

        val res = app.get(data, interceptor = cfKiller, headers = mapOf("User-Agent" to rscHeaders["User-Agent"]!!))

        val script = res.document.select("script").find { it.data().contains("players") }?.data()
        if (script == null) {
            Log.e("AnimeLatino", "No se encontró el script de players en el HTML")
            return@coroutineScope false
        }

        val playersRegex = Regex("""\\"players\\":\s*(\[.*?\])""")
        val playersMatch = playersRegex.find(script)?.groupValues?.get(1)

        if (playersMatch == null) {
            Log.e("AnimeLatino", "Regex falló al extraer el array de players")
            return@coroutineScope false
        }

        val cleanJson = playersMatch.replace("\\\"", "\"").replace("\\\\", "\\")
        val playersArray = json.parseToJsonElement(cleanJson).jsonArray
        Log.d("AnimeLatino", "Idiomas/Grupos de servidores encontrados: ${playersArray.size}")

        for (langGroup in playersArray) {
            for (player in langGroup.jsonArray) {
                val obj = player.jsonObject
                val id = obj["id"]?.jsonPrimitive?.content ?: continue
                val langLabel = if (obj["languaje"]?.jsonPrimitive?.content == "1") "Lat" else "Sub"

                Log.d("AnimeLatino", "Extrayendo stream ID: $id ($langLabel)")

                val apiRes = app.get(
                    "https://api.animelatinohd.com/stream/$id",
                    headers = mapOf(
                        "Referer" to "$mainUrl/",
                        "X-Requested-With" to "XMLHttpRequest",
                        "User-Agent" to rscHeaders["User-Agent"]!!
                    )
                )

                val streamUrl = Regex("""https?://[^\s"']+""").find(apiRes.text)?.value
                if (streamUrl != null) {
                    Log.d("AnimeLatino", "URL final para extractor: $streamUrl")
                    loadExtractor(streamUrl, "$mainUrl/", subtitleCallback) { link ->
                        runBlocking {
                            callback.invoke(newExtractorLink(link.source, "${link.name} [$langLabel]", link.url) {
                                this.referer = mainUrl
                            })
                        }
                    }
                }
            }
        }
        true
    }

    private fun parseRscList(text: String): List<JsonObject> {
        return try {
            // Regex ajustado para capturar la estructura de datos que envía Next.js en RSC
            val regex = Regex("""\{"id":\d+,"name":"[^"]+","slug":"[^"]+","poster":"[^"]+"[^\}]*\}""")
            val matches = regex.findAll(text).map {
                json.parseToJsonElement(it.value).jsonObject
            }.toList()
            matches
        } catch (e: Exception) {
            Log.e("AnimeLatino", "Error parseando RSC List: ${e.message}")
            emptyList()
        }
    }
}