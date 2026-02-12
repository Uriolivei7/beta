package it.dogior.example

import com.lagradost.cloudstream3.plugins.CloudstreamPlugin
import com.lagradost.cloudstream3.plugins.Plugin
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lagradost.cloudstream3.CommonActivity.activity
import it.dogior.example.settings.SettingsFragment
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.localization.ContentCountry
import org.schabi.newpipe.extractor.localization.Localization

@CloudstreamPlugin
class YouTubePlugin : Plugin() {
    // Referencia al downloader singleton
    val downloader: NewPipeDownloader = NewPipeDownloader.init(null)

    // Usamos un getter para sharedPref para asegurar que no falle si el activity es null al inicio
    private val sharedPref get() = activity?.getSharedPreferences("Youtube", Context.MODE_PRIVATE)

    override fun load(context: Context) {
        val sp = sharedPref
        var language = sp?.getString("language", "mx") ?: "mx"
        var country = sp?.getString("country", "MX") ?: "MX"

        // --- CORRECCIÓN CRÍTICA: Sincronizar Cookies ---
        val savedCookie = sp?.getString("youtube_cookie", null)
        downloader.cookies = savedCookie

        Log.d("Youtube", "Logs: Cargando plugin. Cookie detectada: ${!(savedCookie.isNullOrEmpty())}")

        NewPipe.init(downloader)
        NewPipe.setupLocalization(Localization(language), ContentCountry(country))

        registerMainAPI(YoutubeProvider(language, sp))
        registerExtractorAPI(YouTubeExtractor())

        openSettings = { ctx ->
            val activity = ctx as? AppCompatActivity
            if (activity != null) {
                val frag = SettingsFragment(this, sp)
                frag.show(activity.supportFragmentManager, "Frag")
            } else {
                Log.e("Youtube", "Logs: No se pudo abrir ajustes, context no es AppCompatActivity")
            }
        }
    }
}