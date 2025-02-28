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
                    val id = document.getString("userId") ?: ""
                    val name = document.getString("userNickName") ?: ""
                    val profilePicture = document.getString("userProfileImg") ?: ""
                    val walkCountDaily = document.getLong("userStepDaily")?.toInt() ?: 0
                    val walkCountWeekly = document.getLong("userStepWeekly")?.toInt() ?: 0

                    UserRanking(id, name, profilePicture, walkCountDaily, walkCountWeekly)
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
    fun setRankingRewardHistory(userId: String, rewardPoint: Int) {
        Log.d("ttttest","레포지토리 // $${userId}, ${rewardPoint} 지급")
        val document = firestore.collection("UserInfo").document(userId)

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
            "reason" to "걷기 랭킹 보상",
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

        // userPoint 업데이트
        document.get()
            .addOnSuccessListener { userSnapshot ->
                val currentPoint = userSnapshot.getLong("userPoint") ?: 0
                val newPoint = currentPoint + rewardPoint

                document.update("userPoint", newPoint)
                    .addOnSuccessListener {
                        Log.d("UserPoint", "포인트 업데이트 완료: $newPoint")
                    }
                    .addOnFailureListener {
                        Log.d("UserPoint", "포인트 업데이트 실패")
                    }
            }
            .addOnFailureListener {
                Log.d("UserPoint", "유저 정보 가져오기 실패")
            }
    }


    // 현재 날짜를 "yyyy/MM/dd HH:mm:ss" 형식으로 반환하는 함수
    private fun getCurrentFormattedDate(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date()) // 현재 시간 변환
    }
}
