package com.protect.jikigo.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coupon(
    val couponDocId: String = "",
    val couponName: String = "",
    val couponPrice: Int = 0,
    val couponBrand: String = "",
    val couponImg: String = "",
    val couponCategory: String = "",
    val couponInfo: String = "",
    val couponValidDays: Int = 0,
    val couponBarCode: String = "",
    val couponSalesCount: Int = 0,
    val couponStatus: Int = 0,
    val couponCreateAt: Long = System.currentTimeMillis(),
) : Parcelable
