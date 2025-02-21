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
    val userQR: String = "",
    val userStep: Int = 0,
    val userProfileImg: String = "https://www.studiopeople.kr/common/img/default_profile.png",
    val userIsActive: Boolean = true,
    val userAutoLogin: String = "",
    val kakaoToken: String = ""
)