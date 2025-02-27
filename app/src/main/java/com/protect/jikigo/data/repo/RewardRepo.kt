package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.UserCalendar
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.model.UserQR
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardRepo @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // 출석체크
    suspend fun setAttendData(userId: String, point: Int, userPoint: Int, onComplete: (Boolean, Int?) -> Unit) {
        val document = firestore.collection("UserInfo").document(userId)

        // 현재 날짜를 YYYY-MM-DD 형식으로 가져오기
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        Log.d("test", point.toString())
        Log.d("test", userPoint.toString())

        val attendHistoryRef = document.collection("Calendar").document(currentDate)

        val attendData = hashMapOf(
            "calendarDocId" to currentDate,
            "todayCheck" to true,
            "point" to point,
        )

        attendHistoryRef.set(attendData)
            .addOnSuccessListener {
                val paymentHistoryRef =
                    document.collection("Calendar")
                        .document(currentDate)
                        .collection("PaymentHistory")

                // 새 문서를 추가하고 ID를 가져오기
                val newPaymentDocRef = paymentHistoryRef.document() // 랜덤 문서 ID 생성


                val paymentData = hashMapOf(
                    "docId" to newPaymentDocRef.id,
                    "reason" to "출석체크",
                    "amount" to point,
                    "paymentDate" to currentDate,
                    "payType" to "적립",
                )

                newPaymentDocRef.set(paymentData)
                    .addOnSuccessListener {
                        val updates = mapOf(
                            "userPoint" to userPoint
                        )
                        firestore.collection("UserInfo").document(userId)
                            .update(updates)
                            .addOnSuccessListener {
                                onComplete(true, point)
                            }
                            .addOnFailureListener { exception ->
                                exception.printStackTrace()
                                onComplete(false, null)
                            }
                    }
                    .addOnFailureListener {
                        onComplete(false, null)
                    }
            }
            .addOnFailureListener {
                onComplete(false, null)
            }
    }
    // 출석체크 확인
    suspend fun loadAttendData(userId: String, onComplete: (Boolean, UserCalendar?) -> Unit) {
        val document = firestore.collection("UserInfo").document(userId)

        // 현재 날짜를 YYYY-MM-DD 형식으로 가져오기
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val isAttend = document.collection("Calendar").whereEqualTo("calendarDocId",currentDate).whereEqualTo("todayCheck", true).get().await()
        if (isAttend.isEmpty) {
            onComplete(false, null)
        }
        else {
            val attend = isAttend.toObjects(UserCalendar::class.java)
            onComplete(true, attend[0])
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
}