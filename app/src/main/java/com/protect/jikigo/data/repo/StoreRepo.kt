package com.protect.jikigo.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.data.model.Store
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
){
    companion object{
        private const val STORE_COLLECTION = "Store"
    }
    suspend fun getAllStore() : List<Store>{
        return try{
            val snapshot = firestore.collection(STORE_COLLECTION).get().await()
            snapshot.toObjects(Store::class.java)
        }catch (e: Exception){
            emptyList()
        }
    }
}