package com.protect.jikigo.data.repo

import com.google.firebase.firestore.FirebaseFirestore
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
                    val walkCount = document.getLong("userStep")?.toInt() ?: 0

                    UserRanking(name, profilePicture, walkCount)
                }
                .sortedByDescending { it.walkCount } // 걸음 수 기준 내림차순 정렬
        } catch (e: Exception) {
            emptyList() // 에러 발생 시 빈 리스트 반환
        }
    }
}
