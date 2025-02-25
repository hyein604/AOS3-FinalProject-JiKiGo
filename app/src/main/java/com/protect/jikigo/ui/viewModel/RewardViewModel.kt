package com.protect.jikigo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.UserCalendar
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.repo.RewardRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RewardViewModel @Inject constructor(
    private val rewardRepo: RewardRepo,
): ViewModel() {

    private val _profile = MutableLiveData<UserInfo>()
    val profile: LiveData<UserInfo> get() = _profile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isAttend = MutableLiveData<Boolean>()
    val isAttend: LiveData<Boolean> get() = _isAttend

    private val _isAttendData = MutableLiveData<Boolean>()
    val isAttendData: LiveData<Boolean> get() = _isAttendData

    private val _attendPoint = MutableLiveData<Int>()
    val attendPoint: LiveData<Int> get() = _attendPoint

    private val _userPoint = MutableLiveData<Int>()
    val userPoint: LiveData<Int> get() = _userPoint

    fun loadProfile(userid: String) {
        rewardRepo.getProfile(userid) { profile, id ->
            _profile.value = profile!!
            _userPoint.value = profile.userPoint
            loading(false)
        }
    }

    fun loading(boolean: Boolean) {
        _isLoading.value = boolean
    }

    suspend fun loadAttend(userid: String) {
        rewardRepo.loadAttendData(userid) { onComplete, attend ->
            _isAttendData.value = onComplete
            if (attend != null) {
                _attendPoint.value = attend.point
            }
        }
    }

    fun doneAttend() {
        _isAttend.value = false
    }

    suspend fun todayAttend(userid: String, point: Int) {
        rewardRepo.setAttendData(userid, point) { onComplete, point ->
            _isAttendData.value = onComplete
            _isAttend.value = onComplete
            if(point != 0) {
                _attendPoint.value = point!!
                _userPoint.value = userPoint.value?.plus(point)
            }
        }
    }
}