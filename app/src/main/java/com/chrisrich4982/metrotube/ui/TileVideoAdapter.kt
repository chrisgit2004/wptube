package com.chrisrich4982.metrotube.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.chrisrich4982.metrotube.R
import com.chrisrich4982.metrotube.net.VideoItem

class TileVideoAdapter(
    private val onClick: (VideoItem) -> Unit
) : RecyclerView.Adapter<TileVideoAdapter.TileViewHolder>() {

    private val items = mutableListOf<VideoItem>()

    fun submitList(newItems: List<VideoItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video_tile, parent, false)
        return TileViewHolder(view)
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int = items.size

    class TileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.tileThumbnail)
        private val title: TextView = itemView.findViewById(R.id.tileTitle)
        private val duration: TextView = itemView.findViewById(R.id.tileDuration)

        fun bind(item: VideoItem, onClick: (VideoItem) -> Unit) {
            title.text = item.snippet?.title ?: ""

            val thumbUrl = item.snippet?.thumbnails?.high?.url
                ?: item.snippet?.thumbnails?.medium?.url
            thumbnail.load(thumbUrl) {
                crossfade(false)
            }

            val durationText = item.contentDetails?.duration?.let { formatDuration(it) }
            duration.text = durationText ?: ""
            duration.visibility = if (durationText != null) View.VISIBLE else View.GONE

            itemView.setOnClickListener { onClick(item) }
        }

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
