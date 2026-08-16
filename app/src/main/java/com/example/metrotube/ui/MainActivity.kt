package com.example.metrotube.ui

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.metrotube.R
import com.example.metrotube.data.Prefs
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Prefs.hasApiKey(this)) {
            startActivity(Intent(this, SetupActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
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
}
