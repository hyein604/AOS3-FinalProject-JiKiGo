package com.protect.jikigo.data.model

data class UserRanking(
    val id: String = "",
    val name: String = "",
    val profilePicture: String = "",
    val walkCountDaily: Int = 0,
    val walkCountWeekly: Int = 0,
)