package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.UserPaymentHistory
import com.protect.jikigo.data.repo.PointHistoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointHistoryViewModel @Inject constructor(
    private val pointHistoryRepo: PointHistoryRepo
): ViewModel() {

    private val _pointData = MutableLiveData<MutableList<UserPaymentHistory>>()
    val pointData: LiveData<MutableList<UserPaymentHistory>> get() = _pointData

    fun loadPointData(userId: String, selectDate: String) {
        pointHistoryRepo.loadPointHistory(userId, selectDate) {
            _pointData.value = it
        }
    }
}