package com.example.metrotube.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.metrotube.R
import com.example.metrotube.data.Prefs

class SetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Skip straight to the main hub if a key is already saved.
        if (Prefs.hasApiKey(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_setup)

        val apiKeyInput = findViewById<EditText>(R.id.apiKeyInput)
        val getKeyLink = findViewById<TextView>(R.id.getKeyLink)
        val saveButton = findViewById<LinearLayout>(R.id.saveButton)

        getKeyLink.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                android.net.Uri.parse("https://console.cloud.google.com/apis/library/youtube.googleapis.com")
            )
            startActivity(intent)
        }

        saveButton.setOnClickListener {
            val key = apiKeyInput.text.toString().trim()
            if (key.isEmpty()) {
                Toast.makeText(this, "enter an api key first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Prefs.setApiKey(this, key)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
