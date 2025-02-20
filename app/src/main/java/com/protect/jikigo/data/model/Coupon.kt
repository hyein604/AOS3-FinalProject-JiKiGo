package com.protect.jikigo.data.model

data class Coupon(
    val couponDocId: String = "",
    val couponName: String = "",
    val couponPrice: Int = 0,
    val couponBrand: String = "",
    val couponImg: String = "",
    val couponCategory: String = "",
    val couponInfo: String = "",
    val couponExpiryDate: Int = 0,
    val couponBarCode: String = "",
    val couponSaleCount: Int = 0,
    val couponStatus: Int = 0,
    val couponCreateAt: Long = System.currentTimeMillis(),
)
