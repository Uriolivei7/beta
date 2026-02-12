package it.dogior.example

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException
import java.util.concurrent.TimeUnit

class NewPipeDownloader(builder: OkHttpClient.Builder): Downloader() {
    private val client: OkHttpClient = builder.readTimeout(30, TimeUnit.SECONDS).build()
    private val TAG = "NP_Downloader"

    var customUserAgent: String = DEFAULT_USER_AGENT
    var cookies: String? = null

    override fun execute(request: Request): Response {
        val url = request.url()
        val httpMethod = request.httpMethod()

        val requestBody = request.dataToSend()?.toRequestBody(null, 0, request.dataToSend()!!.size)

        val requestBuilder = okhttp3.Request.Builder()
            .method(httpMethod, requestBody)
            .url(url)
            .addHeader("User-Agent", customUserAgent)
            // Headers adicionales para evitar el "Reloaded"
            .addHeader("x-youtube-client-name", "1")
            .addHeader("x-youtube-client-version", "2.20260206.08.00")

        // LOGS: Verificación de sesión
        if (!cookies.isNullOrEmpty()) {
            requestBuilder.addHeader("Cookie", cookies!!)
            Log.d(TAG, "Logs: Enviando petición con COOKIES activas a: $url")
        } else {
            Log.w(TAG, "Logs: Enviando petición SIN COOKIES. Riesgo de error 'Reloaded'.")
        }

        val response = try {
            client.newCall(requestBuilder.build()).execute()
        } catch (e: Exception) {
            Log.e(TAG, "Logs: Error de red ejecutando petición: ${e.message}")
            throw e
        }

        Log.d(TAG, "Logs: Código de respuesta: ${response.code} para URL: $url")

        if (response.code == 429) {
            Log.e(TAG, "Logs: YouTube ha bloqueado la IP (429 - ReCaptcha).")
            response.close()
            throw ReCaptchaException("reCaptcha Challenge requested", url)
        }

        val responseBodyToReturn = response.body?.string() ?: ""
        val latestUrl = response.request.url.toString()

        return Response(
            response.code, response.message, response.headers.toMultimap(),
            responseBodyToReturn, latestUrl
        )
    }

    companion object {
        private const val DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36"
        private var instance: NewPipeDownloader? = null

        fun init(builder: OkHttpClient.Builder?): NewPipeDownloader {
            instance = NewPipeDownloader(builder ?: OkHttpClient.Builder())
            return instance!!
        }

        fun getInstance(): NewPipeDownloader = instance ?: init(null)
    }
}