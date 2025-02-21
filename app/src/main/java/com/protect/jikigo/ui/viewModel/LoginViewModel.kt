package com.protect.jikigo.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo
) : ViewModel() {

    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> get() = _name

    private val _mobile = MutableLiveData<String?>()
    val mobile: LiveData<String?> get() = _mobile

    private val _nickName = MutableLiveData<String?>()
    val nickName: LiveData<String?> get() = _nickName

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    private val _profileImg = MutableLiveData<String?>()
    val profileImg: LiveData<String?> get() = _profileImg

    /**
     * 카카오 로그인 (자동 로그인 지원)
     */
    fun kakao(context: Context, onLoginComplete: (Boolean) -> Unit) {
        // 기존 토큰이 유효하면 자동 로그인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e("LoginViewModel", "토큰 조회 실패: ${error.message}")
                startKakaoLogin(context, onLoginComplete) // 로그인 진행
            } else {
                Log.d("LoginViewModel", "유효한 토큰 확인됨 → 자동 로그인 진행")

                // 기존 저장된 액세스 토큰 가져오기
                val token = com.kakao.sdk.auth.TokenManagerProvider.instance.manager.getToken()
                if (token != null) {
                    getKakaoUserInfo(token.accessToken, onLoginComplete)
                } else {
                    Log.e("LoginViewModel", "액세스 토큰이 없음. 로그인 필요.")
                    startKakaoLogin(context, onLoginComplete)
                }
            }
        }
    }

    /**
     * 카카오 로그인 실행 (토큰이 없을 때)
     */
    private fun startKakaoLogin(context: Context, onLoginComplete: (Boolean) -> Unit) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d("LoginViewModel", "카카오 로그인 실패: ${error.message}")
                onLoginComplete(false)
            } else if (token != null) {
                getKakaoUserInfo(token.accessToken, onLoginComplete)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    getKakaoUserInfo(token.toString(), onLoginComplete)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    /**
     * 카카오 사용자 정보 가져오기 및 DB 저장
     */
    private fun getKakaoUserInfo(token: String, onLoginComplete: (Boolean) -> Unit) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("LoginViewModel", "카카오 사용자 정보 가져오기 실패: ${error.message}")
                onLoginComplete(false)
            } else if (user != null) {
                val userInfo = UserInfo(
                    userName = user.kakaoAccount?.name!!,
                    userId = user.kakaoAccount?.email ?: user.id.toString(),
                    userMobile = user.kakaoAccount?.phoneNumber?.replace("+82 ", "0")?.replace("-", "")?.replace(" ", "")!!,
                    userNickName = user.kakaoAccount?.profile?.nickname!!,
                    userProfileImg = user.kakaoAccount?.profile?.thumbnailImageUrl!!,
                    kakaoToken = token
                )

                // Firestore에서 기존 사용자 확인 후 처리
                userRepo.addUserInfo(userInfo) { success, message ->
                    when (message) {
                        "EXISTING_USER" -> {
                            Log.d("LoginViewModel", "기존 사용자 로그인 완료")
                            onLoginComplete(true) // 기존 사용자 즉시 로그인
                        }

                        else -> {
                            if (success) {
                                Log.d("LoginViewModel", "새 사용자 저장 성공")
                                onLoginComplete(true)
                            } else {
                                Log.e("LoginViewModel", "사용자 저장 실패: $message")
                                onLoginComplete(false)
                            }
                        }
                    }
                }
            }
        }
    }
}