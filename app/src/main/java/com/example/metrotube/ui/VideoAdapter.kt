package com.example.metrotube.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.metrotube.R
import com.example.metrotube.net.VideoItem

class VideoAdapter(
    private val onClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val items = mutableListOf<VideoItem>()

    fun submitList(newItems: List<VideoItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val meta: TextView = itemView.findViewById(R.id.meta)
        private val duration: TextView = itemView.findViewById(R.id.duration)

        fun bind(item: VideoItem, onClick: (VideoItem) -> Unit) {
            title.text = item.snippet?.title ?: ""
            val channel = item.snippet?.channelTitle ?: ""
            val views = item.statistics?.viewCount
            meta.text = if (views != null) "$channel · $views views" else channel

            val thumbUrl = item.snippet?.thumbnails?.high?.url
                ?: item.snippet?.thumbnails?.medium?.url
            thumbnail.load(thumbUrl) {
                crossfade(false)
            }

            duration.text = item.contentDetails?.duration?.let { formatDuration(it) } ?: ""
            duration.visibility = if (item.contentDetails?.duration != null) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onClick(item) }
        }

        // Converts ISO-8601 durations like "PT12M4S" to "12:04"
        private fun formatDuration(iso: String): String {
            val regex = Regex("PT(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?")
            val match = regex.matchEntire(iso) ?: return ""
            val (h, m, s) = match.destructured
            val hours = h.toIntOrNull() ?: 0
            val minutes = m.toIntOrNull() ?: 0
            val seconds = s.toIntOrNull() ?: 0
            return if (hours > 0) {
                String.format("%d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%d:%02d", minutes, seconds)
            }
        }
    }
}
