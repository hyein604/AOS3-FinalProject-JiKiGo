package com.protect.jikigo.data.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class PurchasedCoupon (
    val purchasedCouponName: String = "",
    val purchasedCouponBrand: String = "",
    val purchasedCouponBarCode: String = "",
    val purchasedCouponValidDays: String = "",
    val purchasedCouponDate: Timestamp = Timestamp.now(),
    val purchasedCouponImage: String = "",
    val purchasedCouponUsed: Boolean = false,
    val purchasedCouponUsedDate: String = "",
    val purchasedCouponIsExpiry: Boolean = false,
): Parcelable