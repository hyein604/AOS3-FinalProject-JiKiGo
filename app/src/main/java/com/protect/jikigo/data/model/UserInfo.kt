package com.protect.jikigo.data.model

data class UserInfo(
    // 사용자 고유 ID
    val userDocId: String = "",
    val userId: String = "",
    val userMobile: String = "",
    val userName: String = "",
    val userNickName: String = "",
    val userPoint: Int = 0,
    val userPw: String = "",
    val userQR: UserQR = UserQR(),
//    val userStepDaily: Int = 0,
//    val userStepWeekly: Int = 0,
    val userProfileImg: String = "https://www.studiopeople.kr/common/img/default_profile.png",
    val userIsActive: Boolean = true,
    val userAutoLogin: String = "",
    val kakaoToken: String = "",
    val userQrUser: Boolean = false,
    val userStepDaily: Int = 0,
    val userStepWeekly: Int = 0,
)