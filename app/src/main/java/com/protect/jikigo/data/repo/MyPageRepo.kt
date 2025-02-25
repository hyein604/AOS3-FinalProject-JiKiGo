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

    fun getProfile(userId: String, onResult: (UserInfo?) -> Unit) {
        firestore.collection("UserInfo").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userInfo = document.toObject(UserInfo::class.java)
                    onResult(userInfo!!)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(null)
            }
    }

    fun updateUserInfo(userId: String, updates: Map<String, Any>, onComplete: (Boolean) -> Unit) {
        firestore.collection("UserInfo").document(userId)
            .update(updates) // 특정 필드만 업데이트
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onComplete(false)
            }
    }


}