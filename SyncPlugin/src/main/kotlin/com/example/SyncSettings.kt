package com.example

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Space
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SyncSettings(private val plugin: SyncPlugin) {

    fun show(activity: AppCompatActivity) {
        val appContext = activity.applicationContext
        val scroll = ScrollView(appContext).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
        val root = LinearLayout(appContext).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 24, 32, 24)
        }
        scroll.addView(root)

        fun addTitle(text: String) {
            root.addView(TextView(appContext).apply {
                this.text = text
                setTypeface(null, Typeface.BOLD)
                textSize = 16f
                setPadding(0, 24, 0, 8)
            })
        }

        fun addLabel(text: String) {
            root.addView(TextView(appContext).apply {
                this.text = text
                textSize = 13f
                setPadding(0, 8, 0, 4)
            })
        }

        fun addSpace() = root.addView(Space(appContext).apply {
            layoutParams = LinearLayout.LayoutParams(1, 8)
        })

        addTitle("CloudStream Sync")
        addLabel("Usa un proyecto GitHub (Projects → ProjectV2) como almacen. Cada dispositivo se guarda como un DraftIssue dentro del proyecto.")
        addLabel("Paso 1: crea un token con permisos read/write:project en GitHub → Settings → Developer settings → Personal access tokens (classic) → repo + read:org")

        val tokenInput = EditText(appContext).apply {
            hint = "Token GitHub"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
            setText(SyncStorage.token.orEmpty())
        }
        root.addView(tokenInput)

        val projectNumInput = EditText(appContext).apply {
            hint = "Numero del proyecto (ej: 3)"
            inputType = InputType.TYPE_CLASS_NUMBER
            isSingleLine = true
            setText(SyncStorage.projectNum.orEmpty())
        }
        root.addView(projectNumInput)

        // default: enable settings + bookmarks backup/restore
        val catCheckboxes = mutableMapOf<SyncCategory, Pair<CheckBox, CheckBox>>()
        addSpace()
        addTitle("Categorias")
        for (cat in SyncCategory.entries) {
            val title = when (cat) {
                SyncCategory.SETTINGS -> "Ajustes"
                SyncCategory.BOOKMARKS -> "Favoritos (libro de marcadores)"
                SyncCategory.RESUME_WATCHING -> "En curso / progreso"
                SyncCategory.SEARCH_HISTORY -> "Historial de busqueda"
                SyncCategory.EXTENSIONS -> "Repos de extensiones"
            }
            val row = LinearLayout(appContext).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }
            row.addView(TextView(appContext).apply {
                text = title
                textSize = 13f
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            })
            val backupBox = CheckBox(appContext).apply {
                text = "Backup"
                isChecked = SyncStorage.isBackupEnabled(cat)
            }
            val restoreBox = CheckBox(appContext).apply {
                text = "Restaurar"
                isChecked = SyncStorage.isRestoreEnabled(cat)
            }
            row.addView(backupBox)
            row.addView(restoreBox)
            root.addView(row)
            catCheckboxes[cat] = backupBox to restoreBox
        }

        addSpace()
        addTitle("Acciones")
        root.addView(TextView(appContext).apply {
            text = "Sincroniza cada 30s mientras la app este abierta. Cambios locales se suben ~2s despues de producirse."
            textSize = 12f
        })

        val save = androidx.appcompat.widget.AppCompatButton(appContext).apply {
            text = "Guardar y sincronizar"
            setOnClickListener {
                SyncStorage.token = tokenInput.text.toString().trim().ifEmpty { null }
                SyncStorage.projectNum = projectNumInput.text.toString().trim().ifEmpty { null }
                for ((cat, pair) in catCheckboxes) {
                    SyncStorage.setBackupEnabled(cat, pair.first.isChecked)
                    SyncStorage.setRestoreEnabled(cat, pair.second.isChecked)
                }
                SyncStorage.projectId = null
                SyncStorage.ownItemId = null
                SyncStorage.ownContentId = null
                if (!SyncStorage.isLoggedIn()) {
                    plugin.activity?.let {
                        android.widget.Toast.makeText(
                            it, "Completa el token y numero de proyecto", android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@setOnClickListener
                }
                plugin.forceSync(showToastResult = true)
            }
        }
        root.addView(save)

        val dialog = AlertDialog.Builder(activity)
            .setTitle("CloudStream Sync")
            .setView(scroll)
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }
}