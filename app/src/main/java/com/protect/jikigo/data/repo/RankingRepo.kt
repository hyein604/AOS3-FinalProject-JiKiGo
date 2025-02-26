package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.model.UserRanking
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RankingRepo @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getUserRankings(): List<UserRanking> {
        return try {
            firestore.collection("UserInfo")
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    val name = document.getString("userNickName") ?: ""
                    val profilePicture = document.getString("userProfileImg") ?: ""
                    val walkCountDaily = document.getLong("userStepDaily")?.toInt() ?: 0
                    val walkCountWeekly = document.getLong("userStepWeekly")?.toInt() ?: 0

                    UserRanking(name, profilePicture, walkCountDaily, walkCountWeekly)
                }
                .sortedByDescending { it.walkCountWeekly } // 걸음 수 기준 내림차순 정렬
        } catch (e: Exception) {
            emptyList() // 에러 발생 시 빈 리스트 반환
        }
    }

    suspend fun getUserInfo(id: String): UserInfo? {
        return try {
            val db = FirebaseFirestore.getInstance() // 캐시 먼저 쓰면 값을 못 가져옴..
            val document = db.collection("UserInfo").document(id).get().await()//.get(Source.CACHE).await() // 캐시 우선 사용
                ?: db.collection("UserInfo").document(id).get().await() // 네트워크에서 가져오기

            if (document.exists()) {
                document.toObject(UserInfo::class.java)?.also {
                    Log.d("UserRepo", "UserInfo loaded successfully for id: $id")
                }
            } else {
                Log.e("UserRepo", "UserInfo not found for id: $id")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepo", "Error fetching user info: ${e.message}", e)
            null
        }
    }

    // 순위 차등 적립
    fun setRankingRewardHistory(userRanking: UserRanking, rewardPoint: Int) {
        firestore.collection("UserInfo")
            .whereEqualTo("userNickName", userRanking.name) // userNickName과 일치하는 문서 찾기
            .get()
            .addOnSuccessListener { documents ->    // 유저가 없을 경우
                if (documents.isEmpty) {
                    Log.d("PaymentHistory", "일치하는 유저 없음: ${userRanking.name}")
                    return@addOnSuccessListener
                }

                for (document in documents) {   // 유저가 있을 경우
                    val userDocId = document.id // 찾은 사용자의 document ID
                    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                    // Calendar/PaymentHistory에 적립 기록 저장
                    val paymentHistoryRef = firestore.collection("UserInfo")
                        .document(userDocId)
                        .collection("Calendar")
                        .document(currentDate)
                        .collection("PaymentHistory")

                    val newPaymentDocRef = paymentHistoryRef.document() // 랜덤 문서 ID 생성

                    val paymentData = hashMapOf(
                        "docId" to newPaymentDocRef.id,
                        "reason" to "걷기 랭킹 적립",
                        "amount" to rewardPoint,
                        "paymentDate" to currentDate,
                        "payType" to "적립",
                    )

                    newPaymentDocRef.set(paymentData)
                        .addOnSuccessListener {
                            Log.d("PaymentHistory", "결제 저장 완료: ${userRanking.name}")
                        }
                        .addOnFailureListener {
                            Log.d("PaymentHistory", "결제 저장 실패: ${userRanking.name}")
                        }
                }
            }
            .addOnFailureListener {
                Log.d("PaymentHistory", "유저 검색 실패: ${it.message}")
            }
    }


}
