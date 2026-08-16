package com.chrisrich4982.metrotube.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chrisrich4982.metrotube.R
import com.chrisrich4982.metrotube.data.AppTheme
import com.chrisrich4982.metrotube.data.Prefs
import com.chrisrich4982.metrotube.net.VideoItem
import com.chrisrich4982.metrotube.net.YouTubeApi
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

        val onVideoClick: (VideoItem) -> Unit = { video ->
            val id = video.videoId
            if (id != null) {
                val title = video.snippet?.title ?: ""
                val channel = video.snippet?.channelTitle ?: ""
                val views = video.statistics?.viewCount
                val meta = if (views != null) "$channel · $views views" else channel
                PlayerActivity.start(requireContext(), id, title, meta)
            }
        }

        // CLASSIC = the original 2013 tile-hub look, MODERN = the pivot-tab
        // list from the later, revised WP client. Toggled from Settings.
        val theme = Prefs.getAppTheme(requireContext())

        var listAdapter: VideoAdapter? = null
        var tileAdapter: TileVideoAdapter? = null

        if (theme == AppTheme.CLASSIC) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            tileAdapter = TileVideoAdapter(onVideoClick)
            recyclerView.adapter = tileAdapter
        } else {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            listAdapter = VideoAdapter(onVideoClick)
            recyclerView.adapter = listAdapter
        }

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
                    listAdapter?.submitList(response.items)
                    tileAdapter?.submitList(response.items)
                }
            } catch (e: Exception) {
                emptyState.text = "couldn't load videos: ${e.message}"
                emptyState.visibility = View.VISIBLE
            }
        }
    }

}
