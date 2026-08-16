package com.example.metrotube.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.metrotube.R
import com.example.metrotube.data.Prefs
import com.example.metrotube.net.YouTubeApi
import kotlinx.coroutines.launch

private const val ARG_MODE = "mode"
private const val MODE_POPULAR = "popular"
private const val MODE_BROWSE = "browse"

class VideoListFragment : Fragment(R.layout.fragment_video_list) {

    private val api by lazy { YouTubeApi.create() }

    companion object {
        fun whatToWatch() = VideoListFragment().apply {
            arguments = Bundle().apply { putString(ARG_MODE, MODE_POPULAR) }
        }

        fun browse() = VideoListFragment().apply {
            arguments = Bundle().apply { putString(ARG_MODE, MODE_BROWSE) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val emptyState = view.findViewById<TextView>(R.id.emptyState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = VideoAdapter { video ->
            video.videoId?.let { openVideo(it) }
        }
        recyclerView.adapter = adapter

        val apiKey = Prefs.getApiKey(requireContext())
        if (apiKey == null) {
            emptyState.text = "add an api key in settings"
            emptyState.visibility = View.VISIBLE
            return
        }

        val mode = arguments?.getString(ARG_MODE) ?: MODE_POPULAR

        lifecycleScope.launch {
            try {
                val response = if (mode == MODE_POPULAR) {
                    api.mostPopular(apiKey = apiKey)
                } else {
                    // A neutral default query for the browse tab; a real build
                    // would swap this for a search bar driven by user input.
                    api.search(query = "technology", apiKey = apiKey)
                }
                if (response.items.isEmpty()) {
                    emptyState.visibility = View.VISIBLE
                } else {
                    adapter.submitList(response.items)
                }
            } catch (e: Exception) {
                emptyState.text = "couldn't load videos: ${e.message}"
                emptyState.visibility = View.VISIBLE
            }
        }
    }

    private fun openVideo(videoId: String) {
        // Hands off playback to the YouTube app or browser rather than
        // embedding a player — keeps this project focused on the browsing UI.
        val intent = android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            android.net.Uri.parse("https://www.youtube.com/watch?v=$videoId")
        )
        startActivity(intent)
    }
}
