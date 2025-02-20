package com.protect.jikigo.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.protect.jikigo.HomeActivity
import com.protect.jikigo.data.KakaoAuthClient
import com.protect.jikigo.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val kakaoAuthClient = KakaoAuthClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        onClick()
    }

    // 각 요소 클릭 메서드
    private fun onClick() {
        with(binding) {
            btnLogin.setOnClickListener {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
            tvLoginSignUp.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginToSignUpFirst()
                findNavController().navigate(action)
            }
            tvLoginFindAccount.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginToFindAccount()
                findNavController().navigate(action)
            }
            btnLoginKakao.setOnClickListener {
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Log.d("KAKAO", "error != null")
                        Log.d("KAKAO", "$error")

                    } else if (token != null) {
                        Log.d("KAKAO", "token != null")
                        getKakaoUserInfo()
                    }
                }
                //attach 프로퍼티 -> requireContext()) fragment lifecycle | activtiy this
                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                    UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                        if (error != null) {

                            // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                            // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                return@loginWithKakaoTalk
                            }


                            // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                        } else if (token != null) {
                            Log.i("KAKAO", "카카오톡으로 로그인 성공 ${token.accessToken}")
                            getKakaoUserInfo()
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                }
            }
        }
    }

    private fun getKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("KAKAO", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.d("KAKAO id: ", "${user.id}")
                Log.d("KAKAO email: ", "${user.kakaoAccount?.email}")
                Log.d("KAKAO phone: ", "${user.kakaoAccount?.phoneNumber}")
                Log.d("KAKAO nickname: ", "${user.kakaoAccount?.profile?.nickname}")
                Log.d("KAKAO thumb: ", "${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }

        }
    }
}