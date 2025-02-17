package com.protect.jikigo.ui.home.news

enum class NewsType {
    ALLENVIRONMENT,
    AIR,
    WATER,
    ECOSYSTEM,
    POLICY;

    fun getTodayNewsEnvironmentTabTitle(): String = when(this) {
        ALLENVIRONMENT -> "전체"
        AIR -> "대기"
        WATER -> "수질"
        ECOSYSTEM -> "생태계"
        POLICY -> "정책"
    }

    companion object {
        fun fromString(type: String): NewsType {
            return NewsType.valueOf(type.uppercase())
        }
    }
}