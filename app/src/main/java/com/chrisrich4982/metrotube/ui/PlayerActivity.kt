package com.chrisrich4982.metrotube.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_VIDEO_ID = "video_id"
private const val EXTRA_TITLE = "title"
private const val EXTRA_META = "meta"

class PlayerActivity : AppCompatActivity() {

    companion object {
        fun start(context: android.content.Context, videoId: String, title: String, meta: String) {
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO_ID, videoId)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_META, meta)
            }
            context.startActivity(intent)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val videoId = intent.getStringExtra(EXTRA_VIDEO_ID) ?: run { finish(); return }
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val meta = intent.getStringExtra(EXTRA_META) ?: ""

        findViewById<TextView>(R.id.videoTitle).text = title
        findViewById<TextView>(R.id.videoMeta).text = meta

        val webView = findViewById<WebView>(R.id.playerWebView)
        webView.settings.apply {
            javaScriptEnabled = true
            mediaPlaybackRequiresUserGesture = false
            domStorageEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.webChromeClient = WebChromeClient()

        // Uses YouTube's official embedded IFrame player - the sanctioned way
        // for third-party apps to play back YouTube content. No stream URLs
        // are extracted or scraped; YouTube's own player runs unmodified.
        val embedHtml = """
            <html><body style="margin:0;padding:0;background:#000;">
            <iframe width="100%" height="100%"
                src="https://www.youtube.com/embed/$videoId?autoplay=1&playsinline=1&modestbranding=1&rel=0"
                frameborder="0"
                allow="autoplay; encrypted-media"
                allowfullscreen></iframe>
            </body></html>
        """.trimIndent()
        webView.loadDataWithBaseURL(
            "https://www.youtube.com",
            embedHtml,
            "text/html",
            "utf-8",
            null
        )

        findViewById<ImageView>(R.id.backButton).setOnClickListener { finish() }

        findViewById<LinearLayout>(R.id.openInAppRow).setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
            )
        }
    }

    override fun onDestroy() {
        findViewById<WebView>(R.id.playerWebView)?.destroy()
        super.onDestroy()
    }
}
