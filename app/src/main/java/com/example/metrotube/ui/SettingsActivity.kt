package com.example.metrotube.ui

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.metrotube.R
import com.example.metrotube.data.Prefs

class SettingsActivity : AppCompatActivity() {

    private lateinit var apiKeyValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        apiKeyValue = findViewById(R.id.apiKeyValue)
        refreshApiKeyDisplay()

        findViewById<LinearLayout>(R.id.apiKeyRow).setOnClickListener {
            showEditKeyDialog()
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
}
