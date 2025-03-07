package com.protect.jikigo.data.repo

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.protect.jikigo.data.model.PurchasedCoupon
import com.protect.jikigo.data.model.UserInfo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MyPageRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    fun uploadProfileImage(userId: String, uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageReference = storage.reference
        val profileImageRef = storageReference.child("profile_images/${userId}_profile.jpg")
        profileImageRef.putFile(uri)
            .addOnSuccessListener {
                profileImageRef.downloadUrl.addOnSuccessListener { url ->
                    onSuccess(url.toString())
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun removeProfileImage(userId: String, onSuccess: (Boolean) -> Unit) {
        val storageReference = storage.reference
        val profileImageRef = storageReference.child("profile_images/${userId}_profile.jpg")
        profileImageRef.delete()
            .addOnSuccessListener { onSuccess(true) }
            .addOnFailureListener { onSuccess(false) }
    }

    fun getProfile(userId: String, onResult: (UserInfo?, String?) -> Unit) {
        firestore.collection("UserInfo")
            .whereEqualTo("userId", userId)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0] // 첫 번째 문서 가져오기
                    val userInfo = document.toObject(UserInfo::class.java)
                    onResult(userInfo, document.id) // 문서 ID도 함께 반환
                } else {
                    onResult(null, null)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(null, null)
            }
    }

    fun updateUserInfo(documentId: String, updates: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        firestore.collection("UserInfo").document(documentId)
            .update(updates)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onComplete(false)
            }
    }

    // 유저 쿠폰 가져오기
    fun getUserNickName(userNickName: String, isUsed: (Boolean) -> Unit){
        firestore.collection("UserInfo").whereEqualTo("userNickName", userNickName).get()
            .addOnSuccessListener {
                if(it.isEmpty) {
                    isUsed(false)
                }
                else {
                    isUsed(true)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                isUsed(true)
            }

    }
}