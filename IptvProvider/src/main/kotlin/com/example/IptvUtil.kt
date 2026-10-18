package com.example

import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder

private val TYPE_PARAM = Regex("(?i)([?&])type=[^&]*")

fun normalizeIptvLink(raw: String): String {
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

    if (s.contains("player_api.php", ignoreCase = true)) {
        val user = queryParam(s, "username")
        val pass = queryParam(s, "password")
        if (!user.isNullOrBlank() && !pass.isNullOrBlank()) {
            return buildXtreamUrl(baseUrl(s), user, pass)
        }
        return s
    }

    if (s.contains("get.php", ignoreCase = true)) {
        return ensureLiveOnly(s)
    }

    return s
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
