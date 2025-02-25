package com.protect.jikigo.data.model

import com.google.firebase.Timestamp

data class Confirm (
    val userId: String = "",
    val confirmImage: String,
    val confirmDate: Timestamp,
    val confirmState: Int = 0,
    val confirmName: String = "",
)