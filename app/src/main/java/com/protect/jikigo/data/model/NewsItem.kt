package com.protect.jikigo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsItem(
    val title: String,
    val originallink: String,
    val link: String,
    val description: String,
    val pubDate: String,
    var imageUrl: String? = null
): Parcelable