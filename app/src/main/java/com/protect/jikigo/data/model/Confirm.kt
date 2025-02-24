package com.protect.jikigo.data.model

import android.net.Uri
import com.google.firebase.Timestamp

data class Confirm (
    val userid: String = "",
    val confirmImage: String,
    val confirmDate: Timestamp,
    val confirmState: Int = 0,
    val confirmName: String = "",
)