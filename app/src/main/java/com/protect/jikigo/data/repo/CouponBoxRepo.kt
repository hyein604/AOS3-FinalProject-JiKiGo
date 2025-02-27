package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.PurchasedCoupon
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
}