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

        val requestBuilder = okhttp3.Request.Builder()
            .method(httpMethod, request.dataToSend()?.toRequestBody(null, 0, request.dataToSend()!!.size))
            .url(url)
            // CAMBIO 1: Usar un User Agent de Navegador Real (Chrome Desktop es más seguro)
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
            .addHeader("Accept-Language", "es-ES,es;q=0.9")

        // CAMBIO 2: Asegurar que la cookie se añada correctamente
        val currentCookies = cookies ?: ""
        if (currentCookies.isNotEmpty()) {
            requestBuilder.removeHeader("Cookie") // Evitar duplicados
            requestBuilder.addHeader("Cookie", currentCookies)
            Log.d("NP_Downloader", "Logs: Usando cookie en petición: ${currentCookies.take(30)}...")
        }

        val response = client.newCall(requestBuilder.build()).execute()

        // Si sigue saliendo "Reloaded", imprimimos log de advertencia
        val bodyString = response.body?.string() ?: ""
        if (bodyString.contains("reloaded") || bodyString.contains("player-error")) {
            Log.e("NP_Downloader", "Logs: YouTube detectó bot incluso con cookies. URL: $url")
        }

        return Response(response.code, response.message, response.headers.toMultimap(), bodyString, response.request.url.toString())
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

    // En NewPipeDownloader.kt
    fun checkYoutubeSession(): SessionStatus {
        if (cookies.isNullOrEmpty()) return SessionStatus.NoCookie

        return try {
            // Hacemos una petición pequeña a la página de configuración de cuenta
            val request = okhttp3.Request.Builder()
                .url("https://www.youtube.com/config")
                .addHeader("Cookie", cookies!!)
                .addHeader("User-Agent", customUserAgent)
                .build()

            client.newCall(request).execute().use { response ->
                // Si YouTube nos devuelve info de sesión en los headers o la página carga bien
                if (response.isSuccessful && cookies!!.contains("LOGIN_INFO")) {
                    SessionStatus.Active
                } else {
                    SessionStatus.Expired
                }
            }
        } catch (e: Exception) {
            Log.e("NP_Downloader", "Logs: Error validando sesión: ${e.message}")
            SessionStatus.Error
        }
    }

    enum class SessionStatus { Active, Expired, NoCookie, Error }
}