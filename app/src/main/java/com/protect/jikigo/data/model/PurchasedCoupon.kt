package com.protect.jikigo.data.model

import com.google.firebase.Timestamp

data class PurchasedCoupon (
    val purchasedCouponName: String = "",
    val purchasedCouponBrand: String = "",
    val purchasedCouponBarCode: String = "",
    val purchasedCouponValidDays: String = "",
    val purchasedCouponDate: Timestamp = Timestamp.now(),
    val purchasedCouponImage: String = "",
    val purchasedCouponUsed: Boolean = false,
    val purchasedCouponIsExpiry : Boolean = false,
    val purchasedCouponUsedDate: String = "",
)