package com.protect.jikigo.data.model

data class UserInfo(
    // 사용자 고유 ID
    val userDocId: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val userMobile: String = "",
    val userName: String = "",
    val userNickName: String = "",
    val userPoint: Int = 0,
    val userQR: UserQR = UserQR(),
    val userProfileImg: String = "https://www.studiopeople.kr/common/img/default_profile.png",
    val userStepDaily: Int = 0,
    val userStepWeekly: Int = 0,
)