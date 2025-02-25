package com.protect.jikigo.ui.viewModel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.model.UserRanking
import com.protect.jikigo.data.repo.RankingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val rankingRepo: RankingRepo
) : ViewModel() {

    // 리사이클러뷰 랭킹 리스트
    private val _rankingList = MutableLiveData<List<UserRanking>>()
    val rankingList: LiveData<List<UserRanking>> get() = _rankingList

    // 로그인한 유저
    private val _item = MutableLiveData<UserInfo?>()
    val item: LiveData<UserInfo?> get() = _item

    init {
        fetchRankingData()
        scheduleDailyStepUpdate()
    }

    private fun fetchRankingData() {
        viewModelScope.launch {
            _rankingList.value = rankingRepo.getUserRankings()
            Log.d("rankingData","가져온 랭킹유저 리스트 : ${rankingRepo.getUserRankings()}")
        }
    }

    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            try {
                val userInfo = rankingRepo.getUserInfo(userId)
                if (userInfo != null) {
                    _item.value = userInfo //
                } else {
                    Log.e("HomeViewModel", "User info not found for id: $userId")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching user info", e)
            }
        }
    }

    private fun scheduleDailyStepUpdate() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                updateUserSteps()
                handler.postDelayed(this, 24 * 60 * 60 * 1000) // 하루마다 실행
            }
        }
        val delay = getMillisUntilMidnight()
        handler.postDelayed(runnable, delay)
    }

    private fun updateUserSteps() {
        viewModelScope.launch {
            val userId = _item.value?.userId ?: return@launch
            val userInfo = rankingRepo.getUserInfo(userId) ?: return@launch

            // 이번 주 월요일 체크 후, 필요하면 초기화
            resetWeeklyStepsIfNeeded(userInfo)

            // 오늘 걸음수를 userStepDaily에 저장
            val newDailyStep = fetchTodayStepsFromSamsungHealth() // 삼성 헬스에서 걸음 수 가져오기
            val newWeeklyStep = userInfo.userStepWeekly + newDailyStep

            val updatedUser = userInfo.copy(
                userStepDaily = newDailyStep,
                userStepWeekly = newWeeklyStep
            )

            rankingRepo.updateUserInfo(updatedUser)
            _item.postValue(updatedUser)
        }
    }

    private suspend fun resetWeeklyStepsIfNeeded(userInfo: UserInfo) {
        val calendar = Calendar.getInstance()
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            val resetUser = userInfo.copy(userStepWeekly = 0)
            rankingRepo.updateUserInfo(resetUser)
            _item.postValue(resetUser)
        }
    }

    private fun getMillisUntilMidnight(): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis - System.currentTimeMillis()
    }

    private fun fetchTodayStepsFromSamsungHealth(): Int {
        // TODO: 삼성 헬스 API 연동 후 데이터 가져오는 코드 추가
        return 0
    }
}
