package com.example

import android.view.*
import android.widget.*
import android.os.Bundle
import android.content.*
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lagradost.cloudstream3.CloudStreamApp.Companion.getKey
import com.lagradost.cloudstream3.CloudStreamApp.Companion.setKey
import com.lagradost.cloudstream3.CommonActivity.showToast

class IptvSettingsFragment(private val plugin: IptvPlugin) : BottomSheetDialogFragment() {
    private fun <T : View> View.findView(name: String): T {
        val id = plugin.resources!!.getIdentifier(name, "id", BuildConfig.LIBRARY_PACKAGE_NAME)
        return this.findViewById(id)
    }

    private fun getLayout(name: String, inflater: LayoutInflater, container: ViewGroup?): View {
        val id = plugin.resources!!.getIdentifier(name, "layout", BuildConfig.LIBRARY_PACKAGE_NAME)
        val layout = plugin.resources!!.getLayout(id)
        return inflater.inflate(layout, container, false)
    }

    private fun getDrawable(name: String): Drawable? {
        val id = plugin.resources!!.getIdentifier(name, "drawable", BuildConfig.LIBRARY_PACKAGE_NAME)
        return ResourcesCompat.getDrawable(plugin.resources!!, id, null)
    }

    private fun View.makeTvCompatible() {
        val outlineId = plugin.resources!!.getIdentifier("outline", "drawable", BuildConfig.LIBRARY_PACKAGE_NAME)
        this.background = plugin.resources!!.getDrawable(outlineId, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settings = getLayout("settings", inflater, container)
        settings.findView<TextView>("add_link_text").text = "Add link"
        settings.findView<TextView>("list_link_text").text = "List link"

        val addLinkButton = settings.findView<ImageView>("button_add_link")
        addLinkButton.setImageDrawable(getDrawable("edit_icon"))
        addLinkButton.makeTvCompatible()

        addLinkButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                selectAddType(inflater, container)
            }
        })

        val listLinkButton = settings.findView<ImageView>("button_list_link")
        listLinkButton.setImageDrawable(getDrawable("settings_icon"))
        listLinkButton.makeTvCompatible()

        listLinkButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val credsView = getLayout("manager", inflater, container)
                val linkListLayout = credsView.findView<LinearLayout>("list_link")
                val savedLinks = getKey<Array<IptvLink>>("iptv_links") ?: emptyArray()
                Log.d(IptvPlugin.TAG, "listar links: ${savedLinks.size}")
                if (savedLinks.isEmpty()) {
                    val noLinksTextView = TextView(context).apply {
                        text = "There are no links yet"
                        textSize = 16f
                        setTextColor(Color.GRAY)
                        gravity = Gravity.START
                    }
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        leftMargin = dpToPx(8)
                    }
                    noLinksTextView.layoutParams = layoutParams
                    linkListLayout.addView(noLinksTextView)
                } else {
                    savedLinks.forEach { link ->
                        val linkItemView = getLayout("list_link", inflater, container)
                        linkItemView.findView<TextView>("name").text = "${link.name}  ${typeTag(link.type)}"
                        linkItemView.findView<TextView>("link").text = link.link
                        val deleteButton = linkItemView.findView<ImageView>("delete_button")
                        deleteButton.setImageDrawable(getDrawable("delete_icon"))
                        deleteButton.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                try {
                                    val currentLinks = getKey<Array<IptvLink>>("iptv_links") ?: emptyArray()
                                    val updatedLinks = currentLinks.filter { it.name != link.name }.toTypedArray()
                                    setKey("iptv_links", updatedLinks)
                                    Log.d(IptvPlugin.TAG, "borrado '${link.name}' restantes=${updatedLinks.size}")
                                    showToast("Remove success ${link.name}")
                                    linkListLayout.removeView(linkItemView)
                                    plugin.reload()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    showToast("Error remove link")
                                }
                            }
                        })
                        linkListLayout.addView(linkItemView)
                    }
                }

                AlertDialog.Builder(context ?: throw Exception("Unable to build alert dialog"))
                    .setTitle("List link IPTV")
                    .setView(credsView)
                    .show()
            }
        })

        return settings
    }

    private fun typeTag(type: String?): String = when (type) {
        TYPE_XTREAM -> "[Xtream]"
        TYPE_M3U -> "[M3U]"
        else -> "[Auto]"
    }

    private fun selectAddType(inflater: LayoutInflater, container: ViewGroup?) {
        AlertDialog.Builder(context ?: return)
            .setTitle("Tipo de entrada")
            .setItems(arrayOf<CharSequence>("URL M3U", "Cuenta Xtream")) { _, which ->
                if (which == 0) {
                    showAddLinkDialog(inflater, container, TYPE_M3U)
                } else {
                    showAddLinkDialog(inflater, container, TYPE_XTREAM)
                }
            }
            .show()
    }

    private fun showAddLinkDialog(inflater: LayoutInflater, container: ViewGroup?, type: String) {
        val isXtream = type == TYPE_XTREAM
        val credsView = getLayout("add_link", inflater, container)
        val nameInput = credsView.findView<EditText>("name")
        val linkInput = credsView.findView<EditText>("link")
        val linkLabel = credsView.findView<TextView>("link_label")
        val xtreamFields = credsView.findView<LinearLayout>("xtream_fields")
        val hostInput = credsView.findView<EditText>("host")
        val userInput = credsView.findView<EditText>("user")
        val passInput = credsView.findView<EditText>("pass")

        linkLabel.visibility = if (isXtream) View.GONE else View.VISIBLE
        linkInput.visibility = if (isXtream) View.GONE else View.VISIBLE
        xtreamFields.visibility = if (isXtream) View.VISIBLE else View.GONE

        val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clipboardText = clipboardManager?.primaryClip?.getItemAt(0)?.text?.toString()
        if (!clipboardText.isNullOrEmpty()) {
            if (isXtream) {
                fillXtreamFields(clipboardText, hostInput, userInput, passInput)
            } else if (looksLike(clipboardText, type)) {
                linkInput.setText(clipboardText)
            }
        }

        AlertDialog.Builder(context ?: throw Exception("Unable to build alert dialog"))
            .setTitle(if (isXtream) "Add Xtream account" else "Add M3U link")
            .setView(credsView)
            .setPositiveButton("Save", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface, p1: Int) {
                    val name = nameInput.text.trim().toString()

                    if (isXtream) {
                        val raw = xtreamRaw(hostInput, userInput, passInput)
                        if (name.isEmpty() || raw == null) {
                            showToast("Completa host, usuario y clave")
                        } else {
                            saveLink(name, raw, TYPE_XTREAM, normalizeXtreamLink(raw))
                        }
                    } else {
                        val raw = linkInput.text.trim().toString()
                        if (name.isEmpty() || !looksLike(raw, TYPE_M3U)) {
                            showToast("Pega una URL M3U valida")
                        } else {
                            saveLink(name, raw, TYPE_M3U, normalizeM3uLink(raw))
                        }
                    }
                }
            })
            .show()
    }

    private fun fillXtreamFields(clipboard: String, host: EditText, user: EditText, pass: EditText) {
        val s = clipboard.trim()
        val parts = s.split("|").map { it.trim() }
        if (parts.size == 3 && parts.none { it.isEmpty() }) {
            host.setText(parts[0])
            user.setText(parts[1])
            pass.setText(parts[2])
            return
        }
        val creds = parseXtreamCreds(s)
        if (creds != null) {
            host.setText(creds.base)
            user.setText(creds.user)
            pass.setText(creds.pass)
        } else if (s.startsWith("http", true)) {
            host.setText(s)
        }
    }

    private fun xtreamRaw(host: EditText, user: EditText, pass: EditText): String? {
        var hostValue = host.text.trim().toString()
        var userValue = user.text.trim().toString()
        var passValue = pass.text.trim().toString()
        if (hostValue.isEmpty()) return null

        if (hostValue.contains("|")) {
            val parts = hostValue.split("|").map { it.trim() }
            if (parts.size == 3) {
                hostValue = parts[0]
                if (userValue.isEmpty()) userValue = parts[1]
                if (passValue.isEmpty()) passValue = parts[2]
            }
        }

        if (userValue.isEmpty() || passValue.isEmpty()) {
            val creds = parseXtreamCreds(hostValue)
            if (creds != null) {
                if (userValue.isEmpty()) userValue = creds.user
                if (passValue.isEmpty()) passValue = creds.pass
                if (creds.base.isNotEmpty()) hostValue = creds.base
            }
        }

        if (hostValue.isEmpty() || userValue.isEmpty() || passValue.isEmpty()) return null
        return "$hostValue|$userValue|$passValue"
    }

    private fun saveLink(name: String, raw: String, type: String, normalized: String) {
        try {
            val existingLinks = getKey<Array<IptvLink>>("iptv_links") ?: emptyArray()
            Log.d(IptvPlugin.TAG, "guardar '$name' type=$type raw=${raw.take(60)} -> ${normalized.take(100)}")
            if (existingLinks.any { it.name == name }) {
                showToast("This name already exists")
            } else {
                val updatedLinks = existingLinks.toMutableList().apply {
                    add(IptvLink(name, normalized, type))
                }
                setKey("iptv_links", updatedLinks)
                showToast("Save link success")
                plugin.reload()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Error add link")
        }
    }

    private fun looksLike(raw: String, type: String): Boolean {
        val s = raw.trim()
        if (s.isEmpty()) return false
        return if (type == TYPE_XTREAM) {
            s.split("|").size == 3 || s.contains("get.php", true) || s.contains("player_api.php", true)
        } else {
            s.startsWith("http", true) || s.contains(".m3u", true)
        }
    }

    fun dpToPx(dp: Int): Int {
        val scale = context!!.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
