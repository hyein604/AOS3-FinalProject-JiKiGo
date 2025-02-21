package com.protect.jikigo.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.Notification
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
){
    suspend fun getMySavedPosts(): List<Notification> {

        return try {
            firestore.collection("Notification")
                .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.toObject(Notification::class.java)
                }
        } catch (e: Exception) {
            emptyList() // 에러 발생 시 빈 리스트 반환
        }
    }
}