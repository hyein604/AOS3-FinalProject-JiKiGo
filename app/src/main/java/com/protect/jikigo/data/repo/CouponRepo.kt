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
    companion object{
        private const val COUPON_COLLECTION = "Coupon"
    }
    suspend fun getAllCoupon() : List<Coupon>{
        return try{
            val snapshot = firestore.collection(COUPON_COLLECTION).get().await()
            snapshot.toObjects(Coupon::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }

    suspend fun getCouponById(couponDocId: String): Coupon? {
        return try {
            val snapshot = firestore.collection(COUPON_COLLECTION)
                .document(couponDocId)
                .get()
                .await()
            snapshot.toObject(Coupon::class.java)
        }catch (e: Exception){
          null
        }
    }

    suspend fun getCouponByCategory(couponCategory: String): List<Coupon> {
        return try {
            val snapshot = firestore.collection(COUPON_COLLECTION)
                .whereEqualTo("couponCategory", couponCategory)
                .get()
                .await()
            snapshot.toObjects(Coupon::class.java)
        }
        catch (e : Exception){
            emptyList()
        }
    }


    suspend fun getCouponByCategoryAndSort(category: String, sortOption: String): List<Coupon> {
        val query = firestore.collection(COUPON_COLLECTION)
            .whereEqualTo("couponCategory", category)

        val sortedQuery = when (sortOption) {
            "추천순" -> query.orderBy("couponSalesCount", Query.Direction.DESCENDING)
            "낮은 가격순" -> query.orderBy("couponPrice", Query.Direction.ASCENDING)
            "높은 가격순" -> query.orderBy("couponPrice", Query.Direction.DESCENDING)
            else -> query
        }

        return sortedQuery.get().await().documents.mapNotNull { it.toObject(Coupon::class.java) }
    }

    suspend fun getCouponByBrandAndCategoryAndSort(brand: String, category: String, sortOption: String): List<Coupon> {
        val query = firestore.collection(COUPON_COLLECTION)
            .whereEqualTo("couponCategory", category)
            .whereEqualTo("couponBrand", brand)

        val sortedQuery = when (sortOption) {
            "추천순" -> query.orderBy("couponSalesCount", Query.Direction.DESCENDING)
            "낮은 가격순" -> query.orderBy("couponPrice", Query.Direction.ASCENDING)
            "높은 가격순" -> query.orderBy("couponPrice", Query.Direction.DESCENDING)
            else -> query
        }

        return sortedQuery.get().await().documents.mapNotNull { it.toObject(Coupon::class.java) }
    }


    suspend fun getAllCouponSortedBySales():List<Coupon>{
        return try {
            val query = firestore.collection(COUPON_COLLECTION)
                .orderBy("couponSalesCount", Query.Direction.DESCENDING)

            val snapshot = query.get().await()
            snapshot.toObjects(Coupon::class.java)
        } catch (e: Exception) {
            emptyList()  // 오류 발생 시 빈 리스트 반환
        }
    }


    suspend fun addCoupon(coupon: Coupon) {
        try {
            val docRef = firestore.collection(COUPON_COLLECTION).document()
            val newCoupon = coupon.copy(couponDocId = docRef.id)
            docRef.set(newCoupon).await()
        } catch (e: Exception) {
            Log.e("addCouponError", "Error adding coupon: ${e.message}", e)
        }
    }
}