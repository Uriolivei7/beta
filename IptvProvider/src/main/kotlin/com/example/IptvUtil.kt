package com.example

import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder

const val TYPE_M3U = "m3u"
const val TYPE_XTREAM = "xtream"

data class XtreamCreds(val base: String, val user: String, val pass: String)

private val TYPE_PARAM = Regex("(?i)([?&])type=[^&]*")

fun parseXtreamCreds(url: String): XtreamCreds? {
    if (!url.contains("get.php", ignoreCase = true) && !url.contains("player_api.php", ignoreCase = true)) return null
    val user = queryParam(url, "username") ?: return null
    val pass = queryParam(url, "password") ?: return null
    if (user.isBlank() || pass.isBlank()) return null
    val base = baseUrl(url)
    if (base.isBlank()) return null
    return XtreamCreds(base, user, pass)
}

fun normalizeIptvLink(raw: String): String {
    val s = raw.trim()
    if (s.isEmpty()) return s
    if (s.contains("|") || s.contains("get.php", true) || s.contains("player_api.php", true)) {
        return normalizeXtreamLink(s)
    }
    return normalizeM3uLink(s)
}

fun normalizeM3uLink(raw: String): String {
    val s = raw.trim()
    if (s.isEmpty()) return s
    return if (s.startsWith("http", ignoreCase = true)) s else "http://" + s
}

fun normalizeXtreamLink(raw: String): String {
    val s = raw.trim()
    if (s.isEmpty()) return s

    val pipeParts = s.split("|").map { it.trim() }
    if (pipeParts.size == 3 && pipeParts.none { it.isEmpty() }) {
        val host = if (pipeParts[0].startsWith("http", ignoreCase = true)) {
            pipeParts[0]
        } else {
            "http://" + pipeParts[0]
        }
        return buildXtreamUrl(host, pipeParts[1], pipeParts[2])
    }

    val withScheme = if (s.contains("://")) s else "http://" + s

    if (withScheme.contains("player_api.php", ignoreCase = true)) {
        val user = queryParam(withScheme, "username")
        val pass = queryParam(withScheme, "password")
        if (!user.isNullOrBlank() && !pass.isNullOrBlank()) {
            return buildXtreamUrl(baseUrl(withScheme), user, pass)
        }
        return withScheme
    }

    if (withScheme.contains("get.php", ignoreCase = true)) {
        return ensureLiveOnly(withScheme)
    }

    return withScheme
}

private fun buildXtreamUrl(host: String, user: String, pass: String): String {
    val base = host.trim().trimEnd('/')
    val u = try { URLEncoder.encode(user, "UTF-8") } catch (e: Exception) { user }
    val p = try { URLEncoder.encode(pass, "UTF-8") } catch (e: Exception) { pass }
    return "$base/get.php?username=$u&password=$p&type=m3u"
}

private fun ensureLiveOnly(url: String): String {
    return if (TYPE_PARAM.containsMatchIn(url)) {
        TYPE_PARAM.replace(url) { m -> m.groupValues[1] + "type=m3u" }
    } else {
        url + (if (url.contains("?")) "&" else "?") + "type=m3u"
    }
}

private fun queryParam(url: String, name: String): String? {
    return Regex("(?i)[?&]" + name + "=([^&]*)").find(url)?.groupValues?.get(1)?.let {
        try { URLDecoder.decode(it, "UTF-8") } catch (e: Exception) { it }
    }
}

private fun baseUrl(url: String): String {
    return try {
        val uri = URI(url)
        if (uri.scheme != null && uri.host != null) {
            val port = if (uri.port > 0) ":" + uri.port else ""
            uri.scheme + "://" + uri.host + port
        } else {
            url.substringBefore("player_api.php").substringBefore("get.php").trimEnd('/', '?')
        }
    } catch (e: Exception) {
        url.substringBefore("player_api.php").substringBefore("get.php").trimEnd('/', '?')
    }
}
