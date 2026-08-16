package com.example.metrotube.net

import com.google.gson.annotations.SerializedName

data class VideoListResponse(
    @SerializedName("items") val items: List<VideoItem> = emptyList()
)

data class VideoItem(
    @SerializedName("id") val id: Any?, // string for search, object for videos.list
    @SerializedName("snippet") val snippet: Snippet?,
    @SerializedName("contentDetails") val contentDetails: ContentDetails?,
    @SerializedName("statistics") val statistics: Statistics?
) {
    val videoId: String?
        get() = when (id) {
            is String -> id
            is Map<*, *> -> id["videoId"] as? String
            else -> null
        }
}

data class Snippet(
    @SerializedName("title") val title: String?,
    @SerializedName("channelTitle") val channelTitle: String?,
    @SerializedName("thumbnails") val thumbnails: Thumbnails?
)

data class Thumbnails(
    @SerializedName("medium") val medium: Thumbnail?,
    @SerializedName("high") val high: Thumbnail?
)

data class Thumbnail(
    @SerializedName("url") val url: String?
)

data class ContentDetails(
    @SerializedName("duration") val duration: String?
)

data class Statistics(
    @SerializedName("viewCount") val viewCount: String?
)
