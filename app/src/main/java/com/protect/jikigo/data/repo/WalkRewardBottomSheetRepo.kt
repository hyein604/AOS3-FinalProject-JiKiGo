package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkRewardBottomSheetRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
){
    fun setWalkRewardBottomSheetHistory(userId: String, rewardPoint: Int) {
        val document = firestore.collection("UserInfo").document(userId)
        Log.d("ttest","레포지토리 document: ${document} userId: ${userId}, reward: ${rewardPoint}")

        // 현재 날짜를 YYYY-MM-DD 형식으로 가져오기
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val paymentHistoryRef =
            document.collection("Calendar")
                .document(currentDate)
                .collection("PaymentHistory")
        Log.d("ttest","레포지토리 currentDate: ${currentDate}")

        // 새 문서를 추가하고 ID를 가져오기
        val newPaymentDocRef = paymentHistoryRef.document() // 랜덤 문서 ID 생성


        val paymentData = hashMapOf(
            "docId" to newPaymentDocRef.id,
            "reason" to "걸음수 보상",
            "amount" to rewardPoint,
            "paymentDate" to getCurrentFormattedDate(),
            "payType" to "적립"
        )

        newPaymentDocRef.set(paymentData)
            .addOnSuccessListener {
                Log.d("PaymentHistory", "결제 저장")
            }
            .addOnFailureListener {
                Log.d("PaymentHistory", "결제 실패")
            }

    }

    // 현재 날짜를 "yyyy/MM/dd HH:mm:ss" 형식으로 반환하는 함수
    private fun getCurrentFormattedDate(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date()) // 현재 시간 변환
    }
}