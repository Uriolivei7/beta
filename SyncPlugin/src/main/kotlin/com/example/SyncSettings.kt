package com.example

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils

class SyncSettings(private val plugin: SyncPlugin) {

    private fun resolveColor(ctx: Context, attr: Int): Int {
        val tv = TypedValue()
        return if (ctx.theme.resolveAttribute(attr, tv, true)) tv.data else 0xFF000000.toInt()
    }

    private fun isDarkTheme(ctx: Context): Boolean =
        ctx.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES

    private fun inputBackground(ctx: Context, accent: Int, corner: Float): GradientDrawable {
        val base = resolveColor(ctx, android.R.attr.colorBackground)
        val fill = ColorUtils.setAlphaComponent(
            base,
            if (isDarkTheme(ctx)) 0x2E else 0x66, // ~18% / ~40% alfa sobre el fondo del diálogo
        )
        val stroke = ColorUtils.setAlphaComponent(accent, 0x59) // ~35% acento
        return GradientDrawable().apply {
            cornerRadius = corner
            setColor(fill)
            setStroke(dpToPx(ctx, 1), stroke)
        }
    }

    private fun dpToPx(ctx: Context, dp: Int): Int =
        (dp * ctx.resources.displayMetrics.density).toInt()

    fun show(activity: AppCompatActivity) {
        val appContext = activity.applicationContext
        val primary = resolveColor(appContext, android.R.attr.textColorPrimary)
        val secondary = resolveColor(appContext, android.R.attr.textColorSecondary)
        val accent = resolveColor(appContext, android.R.attr.colorAccent)
        val corner = dpToPx(appContext, 12).toFloat()
        val inputBg = inputBackground(appContext, accent, corner)

        val scroll = ScrollView(appContext)
        val root = LinearLayout(appContext).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(appContext, 20), dpToPx(appContext, 12), dpToPx(appContext, 20), dpToPx(appContext, 12))
        }
        scroll.addView(root)

        fun addSectionTitle(text: String) {
            root.addView(AppCompatTextView(appContext).apply {
                this.text = text
                setTextColor(primary)
                setTypeface(null, Typeface.BOLD)
                textSize = 16f
                setPadding(0, dpToPx(appContext, 16), 0, dpToPx(appContext, 6))
            })
        }

        fun addBody(text: String, size: Float = 13f) {
            root.addView(AppCompatTextView(appContext).apply {
                this.text = text
                setTextColor(secondary)
                textSize = size
                setLineSpacing(0f, 1.15f)
                setPadding(0, dpToPx(appContext, 2), 0, dpToPx(appContext, 2))
            })
        }

        fun addFieldLabel(text: String) {
            root.addView(AppCompatTextView(appContext).apply {
                this.text = text
                setTextColor(primary)
                setTypeface(null, Typeface.BOLD)
                textSize = 13f
                setPadding(0, dpToPx(appContext, 10), 0, dpToPx(appContext, 4))
            })
        }

        fun addSpace(h: Int) = root.addView(View(appContext).apply {
            layoutParams = LinearLayout.LayoutParams(1, dpToPx(appContext, h))
        })

        addSectionTitle("CloudStream Sync")
        addBody("Sincroniza favoritos, progreso, historial, repos y ajustes entre tus dispositivos usando un proyecto GitHub (ProjectV2).")
        addBody("Paso 1: crea un token en GitHub → Settings → Developer settings → Personal access tokens (classic) con permisos repo + read:org.", 12f)

        // ---- Conexión ----
        addSectionTitle("Conexión")

        addFieldLabel("Token de GitHub")
        val tokenInput = AppCompatEditText(appContext).apply {
            hint = "ghp_..."
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            transformationMethod = PasswordTransformationMethod.getInstance()
            isSingleLine = true
            setText(SyncStorage.token.orEmpty())
            background = inputBg
            setPadding(dpToPx(appContext, 12), dpToPx(appContext, 10), dpToPx(appContext, 12), dpToPx(appContext, 10))
            setHintTextColor(secondary)
            setTextColor(primary)
        }
        root.addView(tokenInput)

        val showToken = CheckBox(appContext).apply {
            text = "Mostrar token"
            isChecked = false
            setOnCheckedChangeListener { _: CompoundButton?, checked: Boolean ->
                tokenInput.transformationMethod =
                    if (checked) null else PasswordTransformationMethod.getInstance()
                tokenInput.setSelection(tokenInput.text?.length ?: 0)
            }
        }
        root.addView(showToken)

        addFieldLabel("Número de proyecto")
        val projectNumInput = AppCompatEditText(appContext).apply {
            hint = "Ej: 3"
            inputType = InputType.TYPE_CLASS_NUMBER
            isSingleLine = true
            imeOptions = EditorInfo.IME_ACTION_DONE
            setText(SyncStorage.projectNum.orEmpty())
            background = inputBg
            setPadding(dpToPx(appContext, 12), dpToPx(appContext, 10), dpToPx(appContext, 12), dpToPx(appContext, 10))
            setHintTextColor(secondary)
            setTextColor(primary)
        }
        root.addView(projectNumInput)

        addBody("Abre GitHub → Projects → tu proyecto, y mira la URL: el último número es el número de proyecto (ej: /projects/3).", 12f)

        addSpace(6)

        // ---- Categorías ----
        addSectionTitle("Categorías")
        val catCheckboxes = mutableMapOf<SyncCategory, Pair<CheckBox, CheckBox>>()
        for (cat in SyncCategory.entries) {
            val title = when (cat) {
                SyncCategory.SETTINGS -> "Ajustes"
                SyncCategory.BOOKMARKS -> "Favoritos (marcadores)"
                SyncCategory.RESUME_WATCHING -> "En curso / progreso"
                SyncCategory.SEARCH_HISTORY -> "Historial de búsqueda"
                SyncCategory.EXTENSIONS -> "Repos de extensiones"
            }
            val row = LinearLayout(appContext).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(0, dpToPx(appContext, 2), 0, dpToPx(appContext, 2))
            }
            row.addView(AppCompatTextView(appContext).apply {
                text = title
                setTextColor(primary)
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

        addSpace(6)

        // ---- Acciones ----
        addSectionTitle("Acciones")
        addBody("Sincroniza cada 30s mientras la app esté abierta. Los cambios locales se suben ~2s después de producirse.")

        val save = AppCompatButton(appContext).apply {
            text = "Guardar y sincronizar"
            isAllCaps = false
            textSize = 15f
            setTextColor(0xFFFFFFFF.toInt())
            backgroundTintList = ColorStateList.valueOf(accent)
            stateListAnimator = null
            setPadding(dpToPx(appContext, 16), dpToPx(appContext, 12), dpToPx(appContext, 16), dpToPx(appContext, 12))
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            ).apply { topMargin = dpToPx(appContext, 16) }
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
                            it, "Completa el token y número de proyecto", android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@setOnClickListener
                }
                plugin.forceSync(showToastResult = true)
            }
        }
        root.addView(save)

        AlertDialog.Builder(activity)
            .setTitle("CloudStream Sync")
            .setView(scroll)
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
