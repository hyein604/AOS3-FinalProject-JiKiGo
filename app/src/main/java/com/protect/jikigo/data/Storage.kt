package com.protect.jikigo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsResponse(
    val items: List<NewsItem>
)

@Parcelize
data class NewsItem(
    val title: String,
    val originallink: String,
    val link: String,
    val description: String,
    val pubDate: String,
    var imageUrl: String? = null
): Parcelable
