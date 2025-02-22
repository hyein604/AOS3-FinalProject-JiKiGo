package com.protect.jikigo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.repo.LoginRepo
import com.protect.jikigo.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginRepo,
    private val userRepo: UserRepo,
) : ViewModel() {
    private val _kakaoLoginResult = MutableLiveData<Boolean>()
    val kakaoLoginResult: LiveData<Boolean> get() = _kakaoLoginResult

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading = _isLoading

    fun kakaoLogin() = viewModelScope.launch {
        runCatching {
            loginRepo.kakaoLogin()
        }.onSuccess { result ->
            _kakaoLoginResult.postValue(result)
            saveUserInfomation()
        }.onFailure {
            Log.d("LoginViewModel", "카카오 로그인 실패")
        }
    }

    private fun saveUserInfomation() = viewModelScope.launch {
        runCatching {
            _isLoading.postValue(true)
            userRepo.saveUser()
        }.onSuccess {
            _isLoading.postValue(false)
            Log.d("LoginViewModel", "저장 성공")
        }.onFailure {
            Log.d("LoginViewModel", "로그인 정보 저장 실패 $it")
        }
    }
}