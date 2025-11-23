package com.lagradost

import com.lagradost.nicehttp.Requests
import okhttp3.*

class CustomSession(
    client: OkHttpClient
) : Requests() {
    var cookies = mutableMapOf<String, Cookie>()

    init {
        this.baseClient = client
            .newBuilder()
            .cookieJar(CustomCookieJar())
            .build()
    }

    inner class CustomCookieJar : CookieJar {
        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return this@CustomSession.cookies.values.toList()
        }

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            this@CustomSession.cookies += cookies.map { it.name to it }
        }
    }
}