package com.protect.jikigo.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteIdRepo @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun deleteId(userId: String, onComplete: (Boolean) -> Unit) {
        val updates = mapOf(
            "userIsActive" to false
        )
        firestore.collection("UserInfo").document(userId)
            .update(updates)
            .addOnSuccessListener {
                onComplete(true)
            }
            .addOnFailureListener {
                onComplete(false)
            }
    }
}