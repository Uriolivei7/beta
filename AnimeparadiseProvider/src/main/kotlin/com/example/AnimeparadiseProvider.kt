package com.example

import android.util.Log
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.Jsoup
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.lagradost.cloudstream3.LoadResponse.Companion.addTrailer
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AnimeParadiseProvider : MainAPI() {
    override var mainUrl = "https://www.animeparadise.moe"
    private val apiUrl = "https://api.animeparadise.moe"
    override var name = "AnimeParadise"
    override val hasMainPage = true
    override var lang = "en"
    override val supportedTypes = setOf(TvType.Anime, TvType.AnimeMovie)

    private val TAG = "AnimeParadise"

    // Código actualizado
    private val mapper = jacksonObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
    }

    private val apiHeaders = mapOf(
        "Accept" to "application/json, text/plain, */*",
        "Origin" to mainUrl,
        "Referer" to "$mainUrl/",
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? {
        Log.d(TAG, "Logs: Iniciando getMainPage")
        return try {
            val url = "$apiUrl/?sort=%7B%22rate%22:-1%7D"
            val response = app.get(url, headers = apiHeaders).text

            val resData: AnimeListResponse = mapper.readValue(response)

            val animeList = resData.data.map {
                // USAMOS EL SLUG (it.link) COMO IDENTIFICADOR ÚNICO
                val slug = it.link ?: ""
                newAnimeSearchResponse(it.title ?: "Sin título", slug, TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
            Log.d(TAG, "Logs: getMainPage exitoso, encontrados ${animeList.size} animes")
            newHomePageResponse(listOf(HomePageList("Popular Anime", animeList)), true)
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en getMainPage: ${e.message}")
            null
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Logs: Buscando anime: $query")
        return try {
            val response = app.get("$apiUrl/?title=$query", headers = apiHeaders).text
            val resData: AnimeListResponse = mapper.readValue(response)

            val results = resData.data.map {
                val slug = it.link ?: ""
                newAnimeSearchResponse(it.title ?: "Sin título", slug, TvType.Anime) {
                    this.posterUrl = it.posterImage?.large ?: it.posterImage?.original
                }
            }
            Log.d(TAG, "Logs: Búsqueda exitosa para '$query', ${results.size} resultados")
            results
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error en search: ${e.message}")
            emptyList()
        }
    }

    override suspend fun load(url: String): LoadResponse? {
        Log.d(TAG, "Logs: Iniciando load para URL/Slug: $url")
        return try {
            // La URL ahora es directamente el slug (ej: "kimetsu-no-yaiba")
            val slug = if (url.contains("/")) url.substringAfterLast("/") else url

            val apiPath = "$apiUrl/anime/$slug"
            Log.d(TAG, "Logs: Pidiendo datos a la API: $apiPath")

            val detailResponse = app.get(apiPath, headers = apiHeaders).text

            // Mapeamos al envoltorio "data"
            val wrapper: AnimeDetailResponse = mapper.readValue(detailResponse)

            val anime = wrapper.data
                ?: throw Exception("La API respondió pero el campo 'data' está vacío para: $slug")

            Log.d(TAG, "Logs: Datos obtenidos exitosamente para: ${anime.title}")

            val episodes = anime.ep?.mapIndexed { index, epId ->
                // Usamos el ID de episodio y mantenemos el origen para el extractor
                newEpisode("/watch/$epId?origin=${anime.id ?: slug}") {
                    this.name = "Episodio ${index + 1}"
                    this.episode = index + 1
                }
            } ?: emptyList()

            // Generamos la URL pública real para que no de 404 al compartir
            val publicUrl = "$mainUrl/anime/$slug"

            newAnimeLoadResponse(anime.title ?: "Sin título", publicUrl, TvType.Anime) {
                this.plot = anime.synopsys
                this.tags = anime.genres
                this.posterUrl = anime.posterImage?.large ?: anime.posterImage?.original
                this.year = anime.animeSeason?.year

                this.showStatus = when (anime.status) {
                    "finished" -> ShowStatus.Completed
                    "ongoing" -> ShowStatus.Ongoing
                    else -> null
                }

                addEpisodes(DubStatus.Subbed, episodes)
                if (!anime.trailer.isNullOrBlank()) {
                    addTrailer(anime.trailer)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error crítico en load: ${e.message}")
            null
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        Log.d("AnimeParadise", "Logs: === INICIO LOADLINKS ===")
        return try {
            val epId = data.substringAfter("/watch/").substringBefore("?")
            val originId = data.substringAfter("origin=").substringBefore("&")
            val watchUrl = if (data.startsWith("http")) data else "$mainUrl$data"

            val commonHeaders = mapOf(
                "origin" to "https://www.animeparadise.moe",
                "referer" to "https://www.animeparadise.moe/",
                "user-agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36"
            )

            val actionHeaders = commonHeaders + mapOf(
                "accept" to "text/x-component",
                "next-action" to "6002b0ce935408ccf19f5fa745fc47f1d3a4e98b24",
                "content-type" to "text/plain;charset=UTF-8"
            )

            val body = "[\"$epId\",\"$originId\"]".toRequestBody("text/plain;charset=UTF-8".toMediaTypeOrNull())
            val response = app.post(watchUrl, headers = actionHeaders, requestBody = body).text

            // --- PROCESAMIENTO DE RESPUESTA NEXT.JS ---
            // Eliminamos los prefijos de stream (ej: 1:I{"id"...) para que el JSON sea legible
            val cleanResponse = response
                .replace(Regex("""^[0-9]+:[A-Z]"""), "")
                .replace("\\u0022", "\"")
                .replace("\\\"", "\"")
                .replace("\\/", "/")

            Log.d("AnimeParadise", "Logs: === INICIO ESCANEO DE SUBS ===")

            // Buscamos el objeto de subtítulos: {"id":"UUID","lang":"Idioma"}
            // El ID de subtítulos suele ser un UUID (letras-números-guiones)
            val subRegex = Regex("""\{"id":"([a-f0-9-]{36})","lang":"([^"]+)"\}""")
            val subMatches = subRegex.findAll(cleanResponse).toList()

            if (subMatches.isNotEmpty()) {
                subMatches.forEach { match ->
                    val sId = match.groupValues[1]
                    val sLang = match.groupValues[2]
                    val sUrl = "https://api.animeparadise.moe/stream/file/$sId"

                    Log.d("AnimeParadise", "Logs: [+] SUB ENCONTRADO: $sLang -> URL: $sUrl")

                    subtitleCallback.invoke(
                        newSubtitleFile(lang = sLang, url = sUrl) {
                            this.headers = mapOf(
                                "referer" to "https://www.animeparadise.moe/",
                                "origin" to "https://www.animeparadise.moe",
                                "accept" to "text/plain, */*",
                                "connection" to "keep-alive"
                            )
                        }
                    )
                }
            } else {
                Log.d("AnimeParadise", "Logs: [!] No se hallaron subtítulos estructurados. Reintentando búsqueda genérica...")
                // Búsqueda de respaldo por si el formato UUID cambia
                val backupIds = Regex("""(?:"id")\s*:\s*"([a-zA-Z0-9_-]{25,})"""").findAll(cleanResponse)
                backupIds.forEach { Log.d("AnimeParadise", "Logs: [?] ID Genérico hallado: ${it.groupValues[1]}") }
            }

            // --- RASTREO DE VIDEO ---
            val lightningRegex = Regex("""https://lightningflash[a-zA-Z0-9.-]+/[^"\\\s]+""")
            val videoMatch = lightningRegex.find(response)

            if (videoMatch != null) {
                val rawLightningUrl = videoMatch.value.replace("\\", "").replace("u0026", "&")
                val finalUrl = "https://stream.animeparadise.moe/m3u8?url=$rawLightningUrl"

                Log.d("AnimeParadise", "Logs: [V] Video enviado: $finalUrl")

                callback.invoke(
                    newExtractorLink(
                        name = "AnimeParadise",
                        source = "AnimeParadise",
                        url = finalUrl,
                    ) {
                        this.quality = 1080
                        this.type = ExtractorLinkType.M3U8
                        this.headers = commonHeaders
                    }
                )
            } else {
                Log.d("AnimeParadise", "Logs: [!] Error: Video no encontrado.")
            }

            Log.d("AnimeParadise", "Logs: === FIN LOADLINKS ===")
            true
        } catch (e: Exception) {
            Log.e("AnimeParadise", "Logs: ERROR CRÍTICO: ${e.message}")
            false
        }
    }

}

data class AnimeListResponse(val data: List<AnimeObject>)

data class AnimeObject(
    @JsonProperty("_id") val id: String? = null,
    val title: String? = null, // Cambiado a opcional para evitar el crash
    val link: String? = null,
    val status: String? = null,
    val synopsys: String? = null,
    val genres: List<String>? = null,
    val trailer: String? = null,
    val ep: List<String>? = null,
    val animeSeason: SeasonInfo? = null,
    val posterImage: ImageInfo? = null
)

data class SeasonInfo(val season: String? = null, val year: Int? = null)

data class ImageInfo(val original: String? = null, val large: String? = null)

data class NextDataResponse(val props: NextProps)
data class NextProps(val pageProps: NextPageProps)
data class NextPageProps(val data: DetailedAnimeData)
data class DetailedAnimeData(val synopsys: String? = null, val genres: List<String>? = null)

data class EpisodeListResponse(val data: List<EpisodeEntry>)
data class EpisodeEntry(val uid: String, val origin: String, val number: String? = null, val title: String? = null)

data class NextDataVideo(val props: VideoNextProps)
data class VideoNextProps(val pageProps: VideoPageData)
data class VideoPageData(val subtitles: List<SubtitleEntry>? = null, val animeData: SimpleAnime, val episode: SimpleEpisode)
data class SubtitleEntry(val src: String, val label: String)
data class SimpleAnime(val title: String)
data class SimpleEpisode(val number: String)

// Esta clase representa el sobre/envoltorio de la API
data class AnimeDetailResponse(
    val data: AnimeObject? = null,
    val error: Boolean? = null
)

data class VideoDirectList(
    val directUrl: List<VideoSource>? = null
)

data class VideoSource(
    val src: String,
    val label: String
)