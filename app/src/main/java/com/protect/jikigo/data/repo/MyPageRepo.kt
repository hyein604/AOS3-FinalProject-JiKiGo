package com.protect.jikigo.data.repo

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.protect.jikigo.data.model.UserInfo
import javax.inject.Inject

class MyPageRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    fun uploadProfileImage(userId: String, uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference
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
}