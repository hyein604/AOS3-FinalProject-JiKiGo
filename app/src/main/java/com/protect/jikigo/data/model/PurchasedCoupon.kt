package com.protect.jikigo.data.model

import com.google.firebase.Timestamp

data class PurchasedCoupon (
    val purchasedCouponDocId: String = "",
    val purchasedCouponName: String = "",
    val purchasedCouponBrand: String = "",
    val purchasedCouponBarCode: String = "",
    val purchasedCouponValidDays: String = "",
    val purchasedCouponDate: Timestamp = Timestamp.now(),
    val purchasedCouponImage: String = "",
    val purchasedCouponStatus: Int = 0,
    val purchasedCouponUsedDate: String = "",
)