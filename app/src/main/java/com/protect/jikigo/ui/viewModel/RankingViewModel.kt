package com.protect.jikigo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.UserRanking
import com.protect.jikigo.data.repo.RankingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val rankingRepo: RankingRepo
) : ViewModel() {

    private val _rankingList = MutableLiveData<List<UserRanking>>()
    val rankingList: LiveData<List<UserRanking>> get() = _rankingList

    init {
        fetchRankingData()
    }

    private fun fetchRankingData() {
        viewModelScope.launch {
            _rankingList.value = rankingRepo.getUserRankings()
            Log.d("rankingData","가져온 랭킹유저 리스트 : ${rankingRepo.getUserRankings()}")
        }
    }
}
