package com.protect.jikigo.data.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.model.UserRanking
import kotlinx.coroutines.tasks.await
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
}
