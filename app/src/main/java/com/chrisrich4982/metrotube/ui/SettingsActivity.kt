package com.chrisrich4982.metrotube.ui

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.chrisrich4982.metrotube.R
import com.chrisrich4982.metrotube.data.AppTheme
import com.chrisrich4982.metrotube.data.Prefs

class SettingsActivity : AppCompatActivity() {

    private lateinit var apiKeyValue: TextView
    private lateinit var lookValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        apiKeyValue = findViewById(R.id.apiKeyValue)
        lookValue = findViewById(R.id.lookValue)

        refreshApiKeyDisplay()
        refreshLookDisplay()

        findViewById<LinearLayout>(R.id.apiKeyRow).setOnClickListener {
            showEditKeyDialog()
        }

        findViewById<LinearLayout>(R.id.lookRow).setOnClickListener {
            showLookDialog()
        }
    }

    private fun refreshApiKeyDisplay() {
        val key = Prefs.getApiKey(this)
        apiKeyValue.text = if (key.isNullOrBlank()) {
            "not set"
        } else {
            maskKey(key)
        }
    }

    private fun maskKey(key: String): String {
        if (key.length <= 8) return "••••••••"
        return key.take(6) + "•".repeat(8) + key.takeLast(4)
    }

    private fun showEditKeyDialog() {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            setText(Prefs.getApiKey(this@SettingsActivity) ?: "")
            setSelection(text.length)
        }

        AlertDialog.Builder(this)
            .setTitle("api key")
            .setView(input)
            .setPositiveButton("save") { _, _ ->
                val newKey = input.text.toString().trim()
                if (newKey.isNotEmpty()) {
                    Prefs.setApiKey(this, newKey)
                    refreshApiKeyDisplay()
                }
            }
            .setNegativeButton("cancel", null)
            .show()
    }

    private fun refreshLookDisplay() {
        lookValue.text = when (Prefs.getAppTheme(this)) {
            AppTheme.CLASSIC -> "classic (2013 tile hub)"
            AppTheme.MODERN -> "modern (pivot list)"
        }
    }

    private fun showLookDialog() {
        val options = arrayOf("modern (pivot list)", "classic (2013 tile hub)")
        val current = Prefs.getAppTheme(this)
        val checkedIndex = if (current == AppTheme.CLASSIC) 1 else 0

        AlertDialog.Builder(this)
            .setTitle("look")
            .setSingleChoiceItems(options, checkedIndex) { dialog, which ->
                val chosen = if (which == 1) AppTheme.CLASSIC else AppTheme.MODERN
                Prefs.setAppTheme(this, chosen)
                refreshLookDisplay()
                dialog.dismiss()
            }
            .setNegativeButton("cancel", null)
            .show()
    }
}
