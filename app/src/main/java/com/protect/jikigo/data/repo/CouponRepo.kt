package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.protect.jikigo.data.model.Coupon
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CouponRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
) {
    suspend fun getAllCoupon() : List<Coupon>{
        return try{
            val snapshot = firestore.collection("Coupon").get().await()
            snapshot.toObjects(Coupon::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }

    suspend fun getCouponById(couponDocId: String): Coupon? {
        return try {
            val snapshot = firestore.collection("Coupon")
                .document(couponDocId)
                .get()
                .await()
            snapshot.toObject(Coupon::class.java)
        }catch (e: Exception){
          null
        }
    }

    suspend fun getCouponByCategory(couponCcategory: String): List<Coupon> {
        return try {
            val snapshot = firestore.collection("Coupon")
                .whereEqualTo("couponCategory", couponCcategory)
                .get()
                .await()
            snapshot.toObjects(Coupon::class.java)
        }
        catch (e : Exception){
            emptyList()
        }
    }

    suspend fun getCouponByBrand(couponBrand: String): List<Coupon> {
        return try {
            val snapshot = firestore.collection("Coupon")
                .whereEqualTo("couponBrand", couponBrand)
                .get()
                .await()
            snapshot.toObjects(Coupon::class.java)
        }
        catch (e : Exception){
            emptyList()
        }
    }

    suspend fun getCouponsSortedByPrice(ascending: Boolean): List<Coupon> {
        return try {
            val snapshot = firestore.collection("Coupon")
                .orderBy("couponPrice", if (ascending) Query.Direction.ASCENDING else Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.toObjects(Coupon::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCouponsSortedBySales(): List<Coupon> {
        return try {
            val snapshot = firestore.collection("Coupon")
                .orderBy("couponSalesCount", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.toObjects(Coupon::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addCoupon(coupon: Coupon) {
        try {
            firestore.collection("Coupon")
                .document(coupon.couponDocId)
                .set(coupon)
                .await()
        } catch (e: Exception) {
            Log.e("addCouponError", "Error adding coupon: ${e.message}")
        }
    }
}