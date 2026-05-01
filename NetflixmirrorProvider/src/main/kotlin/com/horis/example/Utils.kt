package com.horis.example

import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.utils.AppUtils.parseJson as appParseJson

inline fun <reified T> parseJson(json: String): T = appParseJson(json)

suspend fun resolveApiUrl(): String {
    return "https://net52.cc"
}

fun buildNewTvHeaders(ott: String, extra: Map<String, String> = mapOf()): Map<String, String> {
    return mapOf(
        "Ott" to ott,
        "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:133.0) Gecko/20100101 Firefox/133.0"
    ) + extra
}

fun buildPosterUrl(template: String?, id: String): String? {
    if (template.isNullOrBlank()) return null
    return template.replace("- -.jpg", "$id.jpg")
}

fun buildVerticalPosterUrl(id: String, ott: String): String {
    return when (ott) {
        "nf" -> "https://imgcdn.kim/poster/v/$id.jpg"
        "pv" -> "https://imgcdn.kim/pv/v/$id.jpg"
        else -> "https://imgcdn.kim/poster/v/$id.jpg"
    }
}

fun buildBackgroundPosterUrl(id: String, ott: String): String {
    return when (ott) {
        "nf" -> "https://imgcdn.kim/poster/h/$id.jpg"
        "pv" -> "https://imgcdn.kim/pv/h/$id.jpg"
        else -> "https://imgcdn.kim/poster/h/$id.jpg"
    }
}

fun convertRuntimeToMinutes(runtime: String): Int {
    if (runtime.isBlank()) return 0
    val regex = Regex("(\\d+)\\s*([hH])\\s*(\\d+)?\\s*([mM])?")
    val match = regex.find(runtime)
    return if (match != null) {
        val h = match.groupValues[1].toIntOrNull() ?: 0
        val m = match.groupValues[3].toIntOrNull() ?: 0
        h * 60 + m
    } else {
        runtime.filter { it.isDigit() }.toIntOrNull() ?: 0
    }
}

data class NewTvMainResponse(
    val post: List<NewTvCategory>? = null,
    val imgcdn_v: String? = null,
    val imgcdn_h: String? = null,
    val img_referer: String? = null
)

data class NewTvCategory(
    val cate: String? = null,
    val ids: String? = null
)

data class NewTvSearchResponse(
    val searchResult: List<NewTvSearchResult>? = null,
    val img_referer: String? = null,
    val imgcdn: String? = null,
    val detailsimgcdn: String? = null
)

data class NewTvSearchResult(
    val id: String,
    val t: String
)

data class NewTvId(val id: String)

data class NewTvLoadData(val title: String, val id: String)

@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
data class NewTvPostResponse(
    val status: String? = null,
    val title: String? = null,
    val main_id: String? = null,
    val desc: String? = null,
    val year: String? = null,
    val genre: String? = null,
    val cast: String? = null,
    val match: String? = null,
    val runtime: String? = null,
    val episodes: List<NewTvApiEpisode?>? = null,
    val season: List<NewTvSeason>? = null,
    val nextPageSeason: String? = null,
    val nextPageShow: Int? = null,
    val type: String? = null,
    val main_poster: String? = null,
    val morelike_poster: String? = null,
    val ep_poster: String? = null,
    val suggest: List<NewTvSuggest>? = null
)

data class NewTvApiEpisode(
    val id: String? = null,
    val t: String? = null,
    val ep: String? = null,
    val epNum: String? = null,
    val s: String? = null,
    val sNum: String? = null,
    val time: String? = null,
    val timeVal: String? = null,
    val ep_desc: String? = null
)

data class NewTvSeason(
    val id: String? = null,
    val selected: Boolean? = null,
    val s: String? = null
)

data class NewTvSuggest(
    val id: String
)

data class NewTvEpisodesResponse(
    val episodes: List<NewTvApiEpisode>? = null,
    val nextPageShow: Int? = null
)

data class NewTvPlayerResponse(
    val status: String? = null,
    val video_link: String? = null,
    val referer: String? = null
)
