package com.protect.jikigo.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteIdRepo @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun deleteId(userId: String, onComplete: (Boolean) -> Unit) {
        firestore.collection("UserInfo").document(userId)
            .delete()
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}