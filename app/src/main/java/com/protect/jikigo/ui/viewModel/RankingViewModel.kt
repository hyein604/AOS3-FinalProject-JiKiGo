package com.protect.jikigo.ui.viewModel

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

    fun setRankingRewardPoint(userRanking: UserRanking, reward: Int) {
        viewModelScope.launch {
            rankingRepo.setRankingRewardHistory(userRanking, reward)
        }
    }

    fun distributeRankingRewards() {
        viewModelScope.launch {
            val rankingList = _rankingList.value ?: return@launch
            if (rankingList.size < 3) return@launch // 최소 3명이 있어야 지급 가능

            val rewards = listOf(100, 70, 50)
            rankingList.take(3).forEachIndexed { index, userRanking ->
                setRankingRewardPoint(userRanking, rewards[index])
            }
        }
    }
}
