package com.protect.jikigo.ui.rank

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.repo.RankingRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RankingRewardWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val rankingRepo = RankingRepo(FirebaseFirestore.getInstance())

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RankingRewardWorker", "보상 지급 시작")

                // 유저 랭킹 데이터 가져오기
                val userRankings = rankingRepo.getUserRankings()

                // 보상 지급 (예: 1위 100포인트, 2위 50포인트, 3위 30포인트)
                val rewardPoints = listOf(100, 50, 30)
                userRankings.take(3).forEachIndexed { index, user ->
                    rankingRepo.setRankingRewardHistory(user.id, rewardPoints[index])
                }

                Log.d("RankingRewardWorker", "보상 지급 완료")
                Result.success()
            } catch (e: Exception) {
                Log.e("RankingRewardWorker", "보상 지급 실패", e)
                Result.retry()
            }
        }
    }
}
