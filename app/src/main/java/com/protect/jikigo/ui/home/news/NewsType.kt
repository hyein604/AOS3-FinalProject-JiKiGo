package com.protect.jikigo.ui.home.news

enum class NewsType(val title: String) {
    ENVIRONMENT_ALL("전체"),
    ENVIRONMENT_AIR("대기"),
    ENVIRONMENT_WATER("수질"),
    ENVIRONMENT_ECOSYSTEM("생태계"),
    ENVIRONMENT_POLICY("정책"),

    TRAVEL_ALL("전체"),
    TRAVEL_DESTINATION("여행지"),
    TRAVEL_ACCOMMODATION("숙소"),
    TRAVEL_FOOD("맛집"),
    TRAVEL_FESTIVAL("축제/행사"),

    HEALTH_ALL("전체"),
    HEALTH_EXERCISE("운동"),
    HEALTH_FOOD("식품");

    fun getTodayNewsEnvironmentTabTitle(): String {
        return title
    }

    companion object {
        fun fromCategoryTitle(categoryTitle: String?): List<NewsType> {
            return when (categoryTitle) {
                "여행" -> listOf(
                    TRAVEL_ALL, TRAVEL_DESTINATION, TRAVEL_ACCOMMODATION, TRAVEL_FOOD, TRAVEL_FESTIVAL
                )
                "건강" -> listOf(
                    HEALTH_ALL, HEALTH_EXERCISE, HEALTH_FOOD
                )
                else -> listOf(
                    ENVIRONMENT_ALL, ENVIRONMENT_AIR, ENVIRONMENT_WATER, ENVIRONMENT_ECOSYSTEM, ENVIRONMENT_POLICY
                )
            }
        }
    }
}