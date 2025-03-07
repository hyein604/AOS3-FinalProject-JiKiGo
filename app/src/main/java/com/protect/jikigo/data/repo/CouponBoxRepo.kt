package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.PurchasedCoupon
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CouponBoxRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    fun loadCouponData(userId: String, item: (MutableList<PurchasedCoupon>) -> Unit) {
        firestore.collection("UserInfo").document(userId)
            .collection("PurchasedCoupon").whereEqualTo("purchasedCouponStatus", 0).get()
            .addOnSuccessListener { documents ->
                val couponList = mutableListOf<PurchasedCoupon>()
                for (document in documents) {
                    val couponData = document.toObject(PurchasedCoupon::class.java)
                    couponList.add(couponData)
                }
                couponList.sortByDescending { it.purchasedCouponValidDays }
                item(couponList)
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents: ", exception)
            }
    }

    fun loadUsedCouponData(userId: String, item: (MutableList<PurchasedCoupon>) -> Unit) {
        firestore.collection("UserInfo").document(userId)
            .collection("PurchasedCoupon").whereIn("purchasedCouponStatus", listOf(1, 2)).get()
            .addOnSuccessListener { documents ->
                val couponList = mutableListOf<PurchasedCoupon>()
                for (document in documents) {
                    val couponData = document.toObject(PurchasedCoupon::class.java)
                    couponList.add(couponData)
                }
                couponList.sortByDescending { it.purchasedCouponDate }
                item(couponList)
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents: ", exception)
            }
    }

    // *보관함 쿠폰 처리*

    // 사용한 쿠폰 처리
    suspend fun usePurchasedCoupon(userId: String, purchasedCouponId: String){
        val userDocRef = firestore.collection("UserInfo").document(userId)
        val purchasedCouponRef = userDocRef.collection("PurchasedCoupon").document(purchasedCouponId)

        val currentDate = Date()

        val dateFormat = SimpleDateFormat("yyyy-MM-dd a hh:mm", Locale.getDefault())
        val usedDate = dateFormat.format(currentDate)

        purchasedCouponRef.update(
            "purchasedCouponUsed", true,
            "purchasedCouponValidDate", usedDate
        ).await()
    }

    // 유저 쿠폰 가져오기
    private suspend fun getPurchasedCoupons(userId: String): List<PurchasedCoupon> {
        val userRef = firestore.collection("UserInfo").document(userId)
        val purchasedCouponRef = userRef.collection("PurchasedCoupon")

        val snapshot = purchasedCouponRef.get().await()
        val coupons = mutableListOf<PurchasedCoupon>()

        snapshot.documents.forEach { document ->
            val coupon = document.toObject(PurchasedCoupon::class.java)
            coupon?.let {
                coupons.add(it)
            }
        }
        return coupons
    }


    // 쿠폰 만료 여부 업데이트
    suspend fun updateCouponsExpiry(userId: String) {
        val batch = firestore.batch()

        val userCoupon = getPurchasedCoupons(userId)

        userCoupon.forEach { coupon ->
            val validDateStr = coupon.purchasedCouponValidDays
            val validDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(validDateStr)
            val isExpired = validDate?.before(Date()) ?: false

            var newStatus = coupon.purchasedCouponStatus

            if (isExpired && coupon.purchasedCouponStatus == 0) {
                newStatus = 2
            }

            // 만료 여부 업데이트
            val couponRef = firestore.collection("UserInfo")
                .document(userId)
                .collection("PurchasedCoupon")
                .document(coupon.purchasedCouponDocId)

            // 상태필드 변경으로 수정해야함
            // 만약 만료되었으면 업데이트
            if (coupon.purchasedCouponStatus != newStatus) {
                batch.update(couponRef, "purchasedCouponStatus", newStatus)
            }
        }

        batch.commit().await()
    }

}