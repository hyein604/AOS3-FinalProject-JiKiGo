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

    private val _userQR = MutableLiveData<UserQR>()
    val userQR: LiveData<UserQR> = _userQR

    fun getUserPoint(userId: String) {
        viewModelScope.launch {
            val user = userRepo.getUserInfo(userId)
            _userPoint.postValue(user?.userPoint)
        }
    }

    fun setUserPaymentData(userQR: UserQR) {
        viewModelScope.launch {
            _userQR.postValue(userQR)
            userRepo.setUserPaymentData(userQR)
        }
    }

    fun getPointError(userQR: UserQR) {
        viewModelScope.launch {
            userRepo.getPointError(userQR) {
                _userPointError.postValue(it)
            }
        }
    }

    fun clearDB(userQR: UserQR) {
        viewModelScope.launch {
            userRepo.clearRealDB(userQR) {
                _userQRClear.postValue(it)
            }
        }
    }
}