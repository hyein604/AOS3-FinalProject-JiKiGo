package com.protect.jikigo.data.model

data class UserCalendar(
    // 사용자 고유 ID
    val calendarDocId: String = "",
    val todayCheck: Boolean = false,
    val point: Int = 0,
)