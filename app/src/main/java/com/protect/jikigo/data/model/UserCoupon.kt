package com.protect.jikigo.data.model

data class UserCoupon(
    // 사용자 고유 ID
    val userCouponDocId: String = "",
    val userCouponBrand: String = "",
    val userCouponImg: String = "",
    val userCouponName: String = "",
    val userCouponPrice: Int = 0,
    val userCouponUUID: String = "",
    val userCouponUse: Boolean = false,
)