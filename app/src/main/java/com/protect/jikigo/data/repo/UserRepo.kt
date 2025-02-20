package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.UserInfo
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
                collection.document(docuId).update("userDocId", docuId)
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

    private fun createSubCollections(userDocRef: DocumentReference) {
        // 현재 날짜를 "YYYY-MM-DD" 형식으로 가져오기
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayDate = sdf.format(Date())  // 예: "2025-02-20"

        userDocRef.collection("UserCalendar").document(todayDate)
        userDocRef.collection("UserQR").document("qr")
    }

    // 아이디 중복 확인
    suspend fun isIdAvailable(id: String): Boolean {
        return try {
            val snapshot = firestore
                .collection("UserInfo")
                .whereEqualTo("userId", id)
                .get()
                .await()
            snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    // 닉네임 중복 확인
    suspend fun isNickNameAvailable(nickName: String): Boolean {
        return try {
            val snapshot = firestore
                .collection("UserInfo")
                .whereEqualTo("userNickName", nickName)
                .get()
                .await()
            snapshot.isEmpty
        } catch (e: Exception) {
            Log.d("isNickNameAvailable Error isNickNameAvailable: ${e.message}", e.toString())
            false
        }
    }
}