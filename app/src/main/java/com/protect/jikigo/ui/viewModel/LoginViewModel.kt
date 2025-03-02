package com.protect.jikigo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.data.NidProfileMap
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

    private val _naverLoginResult = MutableLiveData<Boolean>()
    val naverLoginResult: LiveData<Boolean> get() = _naverLoginResult

    private val _naverUserInfo = MutableLiveData<NidProfileMap?>()
    val naverUserInfo: LiveData<NidProfileMap?> get() = _naverUserInfo

    private val _naverOAuthCallback = MutableLiveData<OAuthLoginCallback>()
    val naverOAuthCallback: LiveData<OAuthLoginCallback> get() = _naverOAuthCallback

    private val _isLoading = MutableLiveData<Boolean>()
    var isLoading = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    var isSuccess = _isSuccess

    fun kakaoLogin() = viewModelScope.launch {
        _isLoading.postValue(true)
        when (loginRepo.kakaoLogin()) {
            true -> {
                saveUserInformation()
                _isLoading.postValue(true)
            }

            else -> {
                _isLoading.postValue(false)
                _isSuccess.postValue(false)
                Log.d("LoginViewModel", "카카오 로그인 실패")
            }
        }
    }

    private fun saveUserInformation() = viewModelScope.launch {
        _isLoading.postValue(true)
        when (userRepo.saveKakaoUser()) {
            true -> {
                _isSuccess.postValue(true)
                Log.d("LoginViewModel", "로그인 정보 저장 성공")
            }

            else -> {
                _isSuccess.postValue(false)
                Log.d("LoginViewModel", "로그인 정보 저장 실패")
            }
        }
    }


    fun naverLogin(profileMap: NidProfileMap?) {
        viewModelScope.launch {
            if (profileMap != null) {
                userRepo.saveNaverUser(profileMap)
                _isLoading.postValue(false)
                _isSuccess.postValue(true)
            }

        }
    }

    fun getNaverUserInfo() {
        viewModelScope.launch {
            runCatching {
                _isLoading.postValue(true)
                loginRepo.getUserInfo()
            }.onSuccess {
                naverLogin(it)
            }.onFailure {
                Log.e("LoginViewModel", it.message.toString())
            }
        }
    }

    val oAuthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            // 로그인 성공시 수행 코드
            getNaverUserInfo()
            Log.d("naver", "네이버 로그인 성공")
            Log.d("naver", "${NaverIdLoginSDK.getAccessToken()}")
            Log.d("naver", "${NaverIdLoginSDK.getRefreshToken()}")
            Log.d("naver", "${NaverIdLoginSDK.getExpiresAt()}")
            Log.d("naver", "${NaverIdLoginSDK.getTokenType()}")
            Log.d("naver", "${NaverIdLoginSDK.getState()}")
        }

        override fun onFailure(httpStatus: Int, message: String) {
            Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
            Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
        }

        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }
}