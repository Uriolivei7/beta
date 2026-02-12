package it.dogior.example.settings

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.BuildConfig
import it.dogior.example.YouTubePlugin

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment(
    private val plugin: YouTubePlugin,
    val sharedPref: SharedPreferences?
) : BottomSheetDialogFragment() {
    private val res = plugin.resources ?: throw Exception("Unable to read resources")

    private fun <T : View> View.findView(name: String): T {
        val id = res.getIdentifier(name, "id", BuildConfig.LIBRARY_PACKAGE_NAME)
        return this.findViewById(id)
    }

    private fun View.makeTvCompatible() {
        this.setPadding(
            this.paddingLeft + 10,
            this.paddingTop + 10,
            this.paddingRight + 10,
            this.paddingBottom + 10
        )
        this.background = getDrawable("outline")
    }

    private fun getDrawable(name: String): Drawable? {
        val id =
            res.getIdentifier(name, "drawable", BuildConfig.LIBRARY_PACKAGE_NAME)
        return ResourcesCompat.getDrawable(res, id, null)
    }

    private fun getString(name: String): String? {
        val id =
            res.getIdentifier(name, "string", BuildConfig.LIBRARY_PACKAGE_NAME)
        return res.getString(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val id = res.getIdentifier(
            "settings_fragment",
            "layout",
            BuildConfig.LIBRARY_PACKAGE_NAME
        )
        val layout = res.getLayout(id)
        return inflater.inflate(layout, container, false)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val headerTw = view.findView<TextView>("header_tw")
        headerTw.text = getString("header_tw")

        val hlsSwitch = view.findView<Switch>("hls_switch")
        hlsSwitch.text = getString("hls")
        hlsSwitch.isChecked = sharedPref?.getBoolean("hls", true) ?: true
        hlsSwitch.setOnCheckedChangeListener { compoundButton, b ->
            with(sharedPref?.edit()) {
                this?.putBoolean("hls", hlsSwitch.isChecked)
                this?.apply()
            }
        }

        try {
            val loginButton = view.findView<View>("login_button")
            loginButton.setOnClickListener {
                val context = context ?: return@setOnClickListener
                val webView = android.webkit.WebView(context)
                val cookieManager = android.webkit.CookieManager.getInstance()

                // IMPORTANTE: Limpiar cookies viejas para forzar login limpio
                cookieManager.removeAllCookies(null)

                val dialog = android.app.AlertDialog.Builder(context)
                    .setTitle("Login YouTube")
                    .setView(webView)
                    .setCancelable(false) // Para que no lo cierren sin querer
                    .setPositiveButton("He terminado") { d, _ ->
                        val currentCookies = cookieManager.getCookie("https://www.youtube.com")

                        sharedPref?.edit()?.putString("youtube_cookie", currentCookies)?.apply()
                        plugin.downloader.cookies = currentCookies

                        Log.d("YT_SETTINGS", "Logs: Sesión guardada. ¿Hay datos?: ${!currentCookies.isNullOrEmpty()}")
                        d.dismiss()
                    }
                    .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
                    .create()

                webView.settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true // Necesario para YouTube moderno
                    userAgentString = "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.91 Mobile Safari/537.36"
                }

                webView.webViewClient = object : android.webkit.WebViewClient() {
                    override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                        val cookies = cookieManager.getCookie(url)
                        // Vamos guardando en tiempo real por si el diálogo se cierra inesperadamente
                        if (url?.contains("youtube.com") == true && !cookies.isNullOrEmpty()) {
                            sharedPref?.edit()?.putString("youtube_cookie", cookies)?.apply()
                            plugin.downloader.cookies = cookies
                            Log.d("YT_SETTINGS", "Logs: Cookie actualizada en navegación: $url")
                        }
                    }
                }

                webView.loadUrl("https://accounts.google.com/ServiceLogin?service=youtube")
                dialog.show()
            }
        } catch (e: Exception) {
            Log.e("YT_SETTINGS", "Logs: Fallo crítico al abrir WebView: ${e.message}")
        }

        // Cargar cookie guardada al abrir ajustes
        val savedCookie = sharedPref?.getString("youtube_cookie", null)
        plugin.downloader.cookies = savedCookie

        val localizationTW = view.findView<TextView>("localization_tw")
        val homepageTW = view.findView<TextView>("homepage_tw")

        localizationTW.text = getString("localization_tw")
        homepageTW.text = getString("homepage_tw")


        val changeLocalizationButton = view.findView<ImageButton>("changeLocalization_button")
        changeLocalizationButton.setImageDrawable(getDrawable("settings_icon"))
        changeLocalizationButton.makeTvCompatible()

        changeLocalizationButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                LocalizationSettings(plugin, sharedPref).show(
                    activity?.supportFragmentManager
                        ?: throw Exception("Unable to open localization settings"),
                    ""
                )
                dismiss()
            }
        })

        val changeHomepageButton = view.findView<ImageButton>("changeHomepage_button")
        changeHomepageButton.setImageDrawable(getDrawable("settings_icon"))
        changeHomepageButton.makeTvCompatible()

        changeHomepageButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                HomepageSettings(plugin, sharedPref).show(
                    activity?.supportFragmentManager
                        ?: throw Exception("Unable to open localization settings"),
                    ""
                )
                dismiss()
            }
        })

    }
}
