package com.protect.jikigo.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.UserInfo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    fun addUserInfo(userInfo: UserInfo, callback: (Boolean, String?) -> Unit) {
        val collection = firestore.collection("UserInfo")

        // FireStore에 데이터 저장
        collection.add(userInfo)
            .addOnSuccessListener { document ->
                val docuId = document.id

                collection.document(docuId).update("id", document)
                    .addOnSuccessListener {
                        callback(true, docuId)
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message)
                    }
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }

    }

    // 아이디 중복 확인
    suspend fun isIdAvailable(id: String): Boolean {
        return try {
            val snapshot = firestore
                .collection("UserInfo")
                .whereEqualTo("UserInfo", id)
                .get()
                .await()
            snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }
}