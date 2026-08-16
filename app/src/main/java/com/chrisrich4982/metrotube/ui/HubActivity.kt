package com.chrisrich4982.metrotube.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.chrisrich4982.metrotube.R
import com.chrisrich4982.metrotube.data.Prefs

class HubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Prefs.hasApiKey(this)) {
            startActivity(android.content.Intent(this, SetupActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_hub)

        // "trending" and "subscriptions" both hand off to the shared video
        // list screen (MainActivity's pivot tabs) - the classic hub is a
        // different front door, not a full parallel feature set.
        val openVideoList = { startActivity(android.content.Intent(this, MainActivity::class.java)) }

        findViewById<LinearLayout>(R.id.tileTrending).setOnClickListener { openVideoList() }
        findViewById<LinearLayout>(R.id.tileSubscriptions).setOnClickListener { openVideoList() }
        findViewById<LinearLayout>(R.id.tileHistory).setOnClickListener { openVideoList() }
        findViewById<LinearLayout>(R.id.tilePlaylists).setOnClickListener { openVideoList() }
        findViewById<LinearLayout>(R.id.tileDownloads).setOnClickListener { openVideoList() }

        findViewById<ImageView>(R.id.settingsButton).setOnClickListener {
            startActivity(android.content.Intent(this, SettingsActivity::class.java))
        }
    }
}
