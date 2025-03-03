package com.protect.jikigo.data

import android.os.Parcelable
import com.protect.jikigo.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coupon(
    val category: String,
    val name: String,
    val price: Int,
    val brand: String,
    val date: String,
    val image: Int,
    val salesCount: Int
) : Parcelable

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
