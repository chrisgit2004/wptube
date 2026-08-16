package com.chrisrich4982.metrotube.ui

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.chrisrich4982.metrotube.R
import com.chrisrich4982.metrotube.data.AppTheme
import com.chrisrich4982.metrotube.data.Prefs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private var lastKnownTheme: AppTheme? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Prefs.hasApiKey(this)) {
            startActivity(Intent(this, SetupActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        lastKnownTheme = Prefs.getAppTheme(this)
        viewPager.adapter = PivotPagerAdapter(this)

        val tabTitles = listOf(
            getString(R.string.tab_what_to_watch),
            getString(R.string.tab_browse)
        )
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        findViewById<FrameLayout>(R.id.searchButton).setOnClickListener {
            // Placeholder: wire this to a search UI / query dialog.
        }

        findViewById<FrameLayout>(R.id.refreshButton).setOnClickListener {
            viewPager.adapter = PivotPagerAdapter(this)
        }

        findViewById<ImageView>(R.id.moreButton).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Settings may have changed the look (list vs. tile hub) while this
        // activity was paused - rebuild the pager so the change takes effect
        // as soon as the user comes back, without needing a manual refresh.
        val currentTheme = Prefs.getAppTheme(this)
        if (currentTheme != lastKnownTheme) {
            lastKnownTheme = currentTheme
            viewPager.adapter = PivotPagerAdapter(this)
        }
    }
}
