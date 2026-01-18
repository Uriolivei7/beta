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

    // Mantenemos el killer pero lo usaremos con precaución
    private val cfKiller = CloudflareKiller()

    // Busca esta parte en tu código y reemplázala:

    private val fastHeaders = mapOf(
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36",
        "Accept" to "*/*",
        "RSC" to "1",
        "Next-Router-Prefetch" to "1",
        "Next-Url" to "/",
        "Referer" to "https://www.animelatinohd.com/",
        "Cookie" to "cf_clearance=zRxKw3hEBgSVPRHkUQ1Xs8oH49u2coFGVs4b0h.aFNU-1768761656-1.2.1.1-.FwveTOjvxEzz3eWOuqLUIuxI32SBydHjM8KpBq35_Ehi9WiH_pVfREnbVouSj7t.Ik8fYYRXtTMkC0cdhjEGq.1bCX.upRahfUH_e7vnopz3uCgxURtB2KRrDlkz.AodRkE2wODa9stNA1Sb2bXmncijP.oc7yglA8M4ZVlkQefe6ll1tnDYMV1jP7qgV0f8aIAEAT4.PKtZ9YHFzGk5cyEnIfwInpXT1Gop8b5097TDZEMhTwWnMKk6QN6So2b",
        "sec-ch-ua" to "\"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"144\", \"Brave\";v=\"144\"",
        "sec-ch-ua-mobile" to "?0",
        "sec-ch-ua-platform" to "\"Windows\"",
        "sec-fetch-dest" to "empty",
        "sec-fetch-mode" to "cors",
        "sec-fetch-site" to "same-origin"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse = coroutineScope {
        Log.d("AnimeLatino", "--- INICIANDO BYPASS AGRESIVO ---")
        val items = mutableListOf<HomePageList>()

        // Token extraído de tu inspección
        val rscToken = "1ld0r"

        val sections = listOf(
            Pair("$mainUrl/animes?_rsc=$rscToken", "Animes"),
            Pair("$mainUrl/animes/latino?_rsc=$rscToken", "Latino"),
            Pair("$mainUrl/animes/populares?_rsc=$rscToken", "Populares")
        )

        for ((url, title) in sections) {
            try {
                Log.d("AnimeLatino", ">> Intentando conexión rápida a: $title")

                // Intentamos la petición SIN el interceptor primero para saltar bloqueos de TLS
                val res = app.get(
                    url,
                    headers = fastHeaders,
                    timeout = 15 // Timeout corto: si no responde en 15s, Cloudflare nos bloqueó
                )

                Log.d("AnimeLatino", "<< Respuesta: ${res.code} | Longitud: ${res.text.length}")

                if (res.code == 200) {
                    val animeList = parseRscList(res.text)
                    Log.d("AnimeLatino", "++ Encontrados: ${animeList.size} animes")

                    val searchResponses = animeList.mapNotNull { obj ->
                        val name = obj["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
                        val slug = obj["slug"]?.jsonPrimitive?.content ?: return@mapNotNull null
                        val posterPath = obj["poster"]?.jsonPrimitive?.content ?: ""
                        val poster = if (posterPath.startsWith("http")) posterPath else "https://image.tmdb.org/t/p/w200$posterPath"

                        newAnimeSearchResponse(name, "$mainUrl/anime/$slug") {
                            this.posterUrl = poster
                        }
                    }
                    if (searchResponses.isNotEmpty()) items.add(HomePageList(title, searchResponses))
                }
            } catch (e: Exception) {
                Log.e("AnimeLatino", "XX Fallo en $title: ${e.message}. Reintentando con CFKiller...")
                // Segundo intento con el Interceptor si el primero falla
                try {
                    val resCf = app.get(url, interceptor = cfKiller, headers = fastHeaders, timeout = 20)
                    Log.d("AnimeLatino", "Respuesta reintento: ${resCf.code}")
                } catch (e2: Exception) {
                    Log.e("AnimeLatino", "Error persistente: ${e2.message}")
                }
            }
        }
        newHomePageResponse(items)
    }

    override suspend fun load(url: String): LoadResponse {
        // Para los detalles, pedimos la página normal pero con el User-Agent de Brave
        val res = app.get(url, interceptor = cfKiller, headers = mapOf("User-Agent" to fastHeaders["User-Agent"]!!))
        val document = res.document

        // Buscamos la data en el script __NEXT_DATA__
        val script = document.select("script#__NEXT_DATA__").first()?.data()
            ?: throw ErrorLoadingException("No se encontró la data del anime")

        val data = json.parseToJsonElement(script).jsonObject["props"]?.jsonObject?.get("pageProps")?.jsonObject?.get("data")?.jsonObject
            ?: throw ErrorLoadingException("Error al parsear el contenido")

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
        val res = app.get(data, interceptor = cfKiller, headers = mapOf("User-Agent" to fastHeaders["User-Agent"]!!))
        val script = res.document.select("script").find { it.data().contains("players") }?.data() ?: return@coroutineScope false

        val playersRegex = Regex("""\\"players\\":\s*(\[.*?\])""")
        val playersMatch = playersRegex.find(script)?.groupValues?.get(1)?.replace("\\\"", "\"")?.replace("\\\\", "\\") ?: return@coroutineScope false

        val playersArray = json.parseToJsonElement(playersMatch).jsonArray

        for (langGroup in playersArray) {
            for (player in langGroup.jsonArray) {
                val obj = player.jsonObject
                val id = obj["id"]?.jsonPrimitive?.content ?: continue
                val langLabel = if (obj["languaje"]?.jsonPrimitive?.content == "1") "Lat" else "Sub"

                val apiRes = app.get(
                    "https://api.animelatinohd.com/stream/$id",
                    headers = mapOf("Referer" to "$mainUrl/", "X-Requested-With" to "XMLHttpRequest")
                )

                val streamUrl = Regex("""https?://[^\s"']+""").find(apiRes.text)?.value
                if (streamUrl != null) {
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
            val regex = Regex("""\{"id":\d+,"name":"[^"]+","slug":"[^"]+","poster":"[^"]+"[^\}]*\}""")
            regex.findAll(text).map { json.parseToJsonElement(it.value).jsonObject }.toList()
        } catch (e: Exception) { emptyList() }
    }
}