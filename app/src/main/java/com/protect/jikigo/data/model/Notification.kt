package com.protect.jikigo.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Notification(
    val title: String ="",
    val date: Timestamp? = null,
    val content: String="",
    val image: String? = null,
    val important: Boolean = false
) : Parcelable