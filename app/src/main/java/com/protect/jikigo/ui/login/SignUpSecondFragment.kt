package com.protect.jikigo.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.protect.jikigo.databinding.FragmentSignUpSecondBinding
import com.protect.jikigo.ui.extensions.setTimer
import com.protect.jikigo.ui.extensions.showDialogOkAndCancel
import com.protect.jikigo.ui.extensions.showSnackBar
import com.protect.jikigo.ui.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignUpSecondFragment : Fragment() {
    private var _binding: FragmentSignUpSecondBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SignUpViewModel>()

    // firebase auth
    private lateinit var auth: FirebaseAuth
    private var authId: String? = null
    private var authToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpSecondBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("kr")

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
        onClickToolbar()
        observeViewModel()
        setupListeners()
    }

    private fun onClickToolbar() {
        binding.toolbarSignUp.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        with(binding) {
            etSignUpName.editText?.addTextChangedListener { edit ->
                viewModel.name.value = edit.toString()
                viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpName.error = errorMessage
                }
            }
            etSignUpMobile.editText?.addTextChangedListener { edit ->
                viewModel.mobile.value = edit.toString()
                viewModel.mobileError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpMobile.error = errorMessage
                }
            }
            etSignUpAuthNumber.editText?.addTextChangedListener { edit ->
                viewModel.authNumber.value = edit.toString()
                viewModel.authNumberError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpAuthNumber.error = errorMessage
                }
            }
            etSignUpId.editText?.addTextChangedListener { edit ->
                viewModel.id.value = edit.toString()
                viewModel.idError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpId.error = errorMessage
                }
            }
            etSignUpPw.editText?.addTextChangedListener { edit ->
                viewModel.pw.value = edit.toString()
                viewModel.pwError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpPw.error = errorMessage
                    binding.etSignUpPw.errorIconDrawable = null
                }
            }
            etSignUpPwCheck.editText?.addTextChangedListener { edit ->
                viewModel.pwChk.value = edit.toString()
                viewModel.pwChkError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpPwCheck.error = errorMessage
                    binding.etSignUpPwCheck.errorIconDrawable = null
                }
            }
            etSignUpNickname.editText?.addTextChangedListener { edit ->
                viewModel.nickName.value = edit.toString()
                viewModel.nickNameError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpNickname.error = errorMessage
                }
            }

            // 아이디 중복 확인
            viewModel.idResult.observe(viewLifecycleOwner) { result ->
                val (isAvailable, msg) = result
                Log.d("id", "아이디 사용 가능: $isAvailable, 메시지: $msg")
                requireContext().showSnackBar(binding.root, msg)
                if (!isAvailable) {
                    binding.btnSignUpId.isEnabled = true
                }
            }

            // 닉네임 중복 확인
            viewModel.nicknameResult.observe(viewLifecycleOwner) { result ->
                val (isAvailable, msg) = result
                Log.d("nickname", "닉네임 사용 가능: $isAvailable, 메시지: $msg")
                requireContext().showSnackBar(binding.root, msg)
                if (!isAvailable) {
                    binding.btnSignUpNickname.isEnabled = true
                }
            }

            // 아이디 중복 확인 버튼 상태 관찰
            viewModel.isIdCheckBtnEnabled.observe(viewLifecycleOwner) { isEnabled ->
                binding.btnSignUpId.isEnabled = isEnabled
            }
            // 닉네임 중복 확인 상태
            viewModel.isNickNameCheckBtnEnabled.observe(viewLifecycleOwner) { isEnabled ->
                binding.btnSignUpNickname.isEnabled = isEnabled
            }
            // 전화번호 상태
            viewModel.isMobileBtnEnabled.observe(viewLifecycleOwner) { isEnabled ->
                binding.btnSignUpMobile.isEnabled = isEnabled
            }
            // 인증번호 상태
            viewModel.isAuthNumberBtnEnabled.observe(viewLifecycleOwner) { isEnabled ->
                binding.btnSignUpAuthNumber.isEnabled = isEnabled
            }
            // 가입하기 상태
            viewModel.isSignUpBtnEnabled.observe(viewLifecycleOwner) { isEnabled ->
                binding.btnSignUpDone.isEnabled = isEnabled
            }
        }

        binding.btnSignUpId.setOnClickListener {
            val inputId = binding.etSignUpId.editText?.text.toString()
            viewModel.checkedId(inputId)
            binding.btnSignUpId.isEnabled = false
        }
    }

    // 입력 상태
    private fun setupListeners() {
        binding.etSignUpName.editText?.addTextChangedListener { edit ->
            viewModel.name.value = edit.toString()
            viewModel.validateName()
            viewModel.validateSignUpBtn()
        }

        binding.etSignUpMobile.editText?.addTextChangedListener { edit ->
            viewModel.mobile.value = edit.toString()
            viewModel.validateMobile()
            viewModel.validateSignUpBtn()
            viewModel.validateMobileCheckBtn()
        }

        binding.etSignUpAuthNumber.editText?.addTextChangedListener { edit ->
            viewModel.authNumber.value = edit.toString()
            viewModel.validateAuthNumber()
            viewModel.validateAuthCheckBtn()
            viewModel.validateSignUpBtn()
        }

        binding.etSignUpId.editText?.addTextChangedListener { edit ->
            viewModel.id.value = edit.toString()
            viewModel.validateId()
            viewModel.validateSignUpBtn()
            viewModel.validateIdCheckBtn()
        }

        binding.etSignUpNickname.editText?.addTextChangedListener { edit ->
            viewModel.nickName.value = edit.toString()
            viewModel.validateNickName()
            viewModel.validateSignUpBtn()
            viewModel.validateNickNameCheckBtn()
        }

        binding.etSignUpPw.editText?.addTextChangedListener { edit ->
            viewModel.pw.value = edit.toString()
            viewModel.validatePw()
            viewModel.validateSignUpBtn()
        }

        binding.etSignUpPwCheck.editText?.addTextChangedListener { edit ->
            viewModel.pwChk.value = edit.toString()
            viewModel.validatePwChk()
            viewModel.validateSignUpBtn()
            Log.d("signUP", viewModel.validateSignUpBtn().toString())
        }

        // 아이디 중복 확인 메서드
        binding.btnSignUpId.setOnClickListener {
            val inputId = binding.etSignUpId.editText?.text.toString()
            viewModel.checkedId(inputId)
            binding.btnSignUpId.isEnabled = false
        }

        // 닉네임 중복 확인 메서드
        binding.btnSignUpNickname.setOnClickListener {
            val inputNickName = binding.etSignUpNickname.editText?.text.toString()
            viewModel.checkedNickName(inputNickName)
            binding.btnSignUpNickname.isEnabled = false
        }

        binding.btnSignUpMobile.setOnClickListener {
            val mobile = binding.etSignUpMobile.editText?.text.toString()
            val formattedMobile = formatMobile(mobile)
            if (isValidMobile(formattedMobile)) {
                firebaseAuthNumberSend(formattedMobile)
            } else {
                requireContext().showSnackBar(binding.root, "전화번호를 올바르게 입력하세요.")
            }
        }

        binding.btnSignUpAuthNumber.setOnClickListener {
            val code = binding.etSignUpAuthNumber.editText?.text.toString()
            if (code.isNotEmpty() && authId != null) {
                binding.btnSignUpAuthNumber.isEnabled = false
                authCode(code)
            } else {
                requireContext().showSnackBar(binding.root, "인증 코드를 입력하거나 인증 요청을 다시 진행하세요.")
            }
        }

        // 가입 버튼 메서드
        binding.btnSignUpDone.setOnClickListener {
            binding.btnSignUpDone.isEnabled = false
            val isErrors = listOf(
                viewModel.nameError.value,
                viewModel.mobileError.value,
                viewModel.authNumberError.value,
                viewModel.idError.value,
                viewModel.pwError.value,
                viewModel.pwChkError.value,
                viewModel.nickNameError.value,
            ).any { !it.isNullOrEmpty() }

            if (isErrors) {
                requireContext().showSnackBar(binding.root, "입력한 내용을 확인해주세요.")
                binding.btnSignUpDone.isEnabled = true
            } else {
                viewModel.registerUserInfo { success, docu ->
                    if (success) {
                        Log.d("success", "success")
                        requireContext().showDialogOkAndCancel(
                            title = "회원가입",
                            msg = "회원가입을 완료하였습니다.\n로그인 화면으로 이동합니다.",
                            pos = "확인",
                            nega = "취소"
                        ) { result ->
                            if (result) {
                                val action = SignUpSecondFragmentDirections.actionSignUpSecondToLogin()
                                findNavController().navigate(action)
                            }
                        }
                    } else {
                        Log.d("false", "false")
                        binding.btnSignUpDone.isEnabled = true
                    }
                }
            }
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // 인증 성공 처리
                    Log.d("signUpAuthNumber", "인증 성공 처리")
                    requireContext().showSnackBar(binding.root, "전화번호 인증 성공!")
                    viewModel.authNumberSuccess()
                    viewModel.validateSignUpBtn()
                } else {
                    // 인증 실패 시 버튼 활성화
                    Log.d("signUpAuthNumber", "인증 실패 처리")
                    binding.btnSignUpMobile.isEnabled = true
                    requireContext().showSnackBar(binding.root, "전화번호 인증 실패: ${task.exception?.message}")
                }
            }
    }

    private fun isValidMobile(mobile: String): Boolean {
        val phoneRegex = Regex("^\\+[1-9]\\d{1,14}$")
        return phoneRegex.matches(mobile)
    }

    // 전화번호 E.164 형식 변환
    private fun formatMobile(phoneNumber: String, countryCode: String = "+82"): String {
        // 숫자만 남기기
        val cleanedNumber = phoneNumber.replace(Regex("[^\\d]"), "")
        Log.d("cleanNumber", cleanedNumber)
        return if (cleanedNumber.startsWith("0")) {
            countryCode + cleanedNumber.substring(1)
        } else {
            countryCode + cleanedNumber
        }
    }

    private fun authCode(authCode: String) {
        if (authId != null) {
            val credential = PhoneAuthProvider.getCredential(authId!!, authCode)
            signInWithPhoneAuthCredential(credential)
        } else {
            binding.btnSignUpAuthNumber.isEnabled = true
        }
    }
    

    // firebase 번호 인증 요청
    private fun firebaseAuthNumberSend(mobile: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobile)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(auth: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(auth)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // 인증번호 전송 실패 시
                    binding.btnSignUpMobile.isEnabled = true
                    requireContext().showSnackBar(binding.root, "인증번호 전송 실패")
                    Log.e("PhoneAuth", "Verification failed", e)
                }

                override fun onCodeSent(
                    auth: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // 인증번호 전송 성공
                    authId = auth
                    authToken = token
                    requireContext().showSnackBar(binding.root, "인증번호가 전송되었습니다.")
                    Log.d("PhoneAuth", "Code sent: $authId")
                    binding.btnSignUpMobile.setTimer(lifecycleScope, binding.etSignUpMobile, requireContext())
                    lifecycleScope.launch {
                        delay(120000)
                        binding.btnSignUpMobile.isEnabled = true
                    }
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        binding.btnSignUpMobile.isEnabled = false
    }
}