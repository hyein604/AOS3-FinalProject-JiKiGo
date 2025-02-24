package com.protect.jikigo.ui.payment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.UserQR
import com.protect.jikigo.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val userRepo: UserRepo
) : ViewModel() {
    private val _userPoint = MutableLiveData<Int>()
    val userPoint: LiveData<Int> = _userPoint

    private val _userPointError = MutableLiveData<String>()
    val userPointError: LiveData<String> = _userPointError

    private val _userQRClear = MutableLiveData<Boolean>()
    val userQrClear: LiveData<Boolean> = _userQRClear

    fun getUserPoint(userId: String) {
        viewModelScope.launch {
            val user = userRepo.getUserInfo(userId)
            _userPoint.postValue(user?.userPoint)
        }
    }

    fun setUserPaymentData(userQR: UserQR) {
        viewModelScope.launch {
            userRepo.setUserPaymentData(userQR)
        }
    }

    fun getPointError() {
        viewModelScope.launch {
            userRepo.getPointError {
                _userPointError.postValue(it)
            }
        }
    }

    fun clearDB(userId: String) {
        viewModelScope.launch {
            userRepo.clearRealDB(userId) {
                _userQRClear.postValue(it)
            }
        }
    }
}