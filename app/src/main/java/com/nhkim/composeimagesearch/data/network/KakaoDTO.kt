package com.nhkim.composeimagesearch.data.network

import com.squareup.moshi.Json

data class KakaoDTO<T>(
    val meta: Meta,
    val documents: List<T>
) {
    data class Meta(
        @Json(name = "total_count")
        val totalCount: Int,
        @Json(name = "pageable_count")
        val pageableCount: Int,
        @Json(name = "is_end")
        val isEnd: Boolean
    )

    data class ImageDocument(
        val collection: String,
        @Json(name = "thumbnail_url")
        val thumbnailUrl: String,
        @Json(name = "image_url")
        val imageUrl: String,
        val width: Int,
        val height: Int,
        @Json(name = "display_sitename")
        val displaySitename: String,
        @Json(name = "doc_url")
        val docUrl: String,
        val datetime: String
    )

    data class VideoDocument(
        val title: String,
        val url: String,
        val datetime: String,
        @Json(name = "play_time")
        val playTime: Int,
        val thumbnail: String,
        val author: String
    )
}
