package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.UserPaymentHistory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointHistoryRepo @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun loadPointHistory(userId: String, selectedDate: String, item: (MutableList<UserPaymentHistory>) -> Unit) {
        firestore.collection("UserInfo").document(userId)
            .collection("Calendar").document(selectedDate)
            .collection("PaymentHistory").get()
            .addOnSuccessListener { documents ->
                val paymentHistoryList = mutableListOf<UserPaymentHistory>()
                for (document in documents) {
                    val paymentData = document.toObject(UserPaymentHistory::class.java).copy(docId = document.id)
                    paymentHistoryList.add(paymentData)
                }
                item(paymentHistoryList)
            }
            .addOnFailureListener { exception ->
                Log.w("FirestoreError", "Error getting documents: ", exception)
            }
    }
}