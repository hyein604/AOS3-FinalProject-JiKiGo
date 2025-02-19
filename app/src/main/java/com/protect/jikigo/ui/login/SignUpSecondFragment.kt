package com.protect.jikigo.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentSignUpSecondBinding
import com.protect.jikigo.ui.extensions.setTimer
import com.protect.jikigo.ui.viewModel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpSecondFragment : Fragment() {
    private var _binding: FragmentSignUpSecondBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SignUpViewModel>()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpSecondBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

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
        editTextWatcher()
        onClickAuthBtn()
        onClickSignUpState()
        onClickSignUpBtn()
        onClickAuthCheckBtn()
        //   observeViewModel()
    }

    private fun onClickToolbar() {
        binding.toolbarSignUp.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun editTextWatcher() {
        val regexAuthNumber = "^[0-9]{6}$".toRegex()
        val regexId = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,16}$".toRegex()
        val regexPw = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$])[A-Za-z0-9!@#$]{14,20}$".toRegex()
        val regexNickName = "^[가-힣a-zA-Z0-9]{2,10}$".toRegex()

        with(binding) {
            etSignUpName.editText?.addTextChangedListener { edit ->
                viewModel.name.value = edit.toString()
                viewModel.validateName()
                viewModel.nameError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpName.error = errorMessage
                }
            }
            etSignUpMobile.editText?.addTextChangedListener { edit ->
                viewModel.mobile.value = edit.toString()
                viewModel.validateMobile()
                viewModel.mobileError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpMobile.error = errorMessage
                }
            }
            etSignUpAuthNumber.editText?.addTextChangedListener { edit ->
                viewModel.mobile.value = edit.toString()
                viewModel.validateAuthNumber()
                viewModel.authNumberError.observe(viewLifecycleOwner) { errorMessage ->
                    binding.etSignUpAuthNumber.error = errorMessage
                }
            }
        }
    }

    private fun onClickAuthBtn() {
        with(binding) {
            btnSignUpMobile.setOnClickListener {
                val mobile = etSignUpMobile.editText?.text.toString()
                if (mobile.isEmpty()) {
                    tvErrorMobile.visibility = View.VISIBLE
                    tvErrorMobile.text = getString(R.string.sign_up_error_mobile)
                    tvErrorMobile.setTextColor(ContextCompat.getColor(requireContext(), R.color.negative))
                } else {
                    tvErrorMobile.visibility = View.VISIBLE
                    tvErrorMobile.text = getString(R.string.common_auth_number_check)
                    tvErrorMobile.setTextColor(ContextCompat.getColor(requireContext(), R.color.positive))
                    btnSignUpMobile.setTimer(lifecycleScope, etSignUpMobile, requireContext())
                }
            }
        }
    }


    private fun checkPW(): Boolean {
        val pwMain = binding.etSignUpPw.editText?.text.toString().trim()
        val pwCheck = binding.etSignUpPwCheck.editText?.text.toString().trim()

        val isEmpty = pwCheck.isEmpty()
        val isMatch = pwMain == pwCheck

        if (isEmpty || !isMatch) {
            // 비밀번호가 비어있거나, 일치하지 않는 경우 → 에러 표시
            binding.etSignUpPwCheck.isErrorEnabled = true
            binding.etSignUpPwCheck.error = "비밀번호가 일치하지 않습니다."
            binding.etSignUpPwCheck.errorIconDrawable = null // 에러 아이콘 비활성화
            binding.tvErrorPwCheck.visibility = View.VISIBLE
        } else {
            // 비밀번호가 일치하는 경우 → 에러 제거
            binding.etSignUpPwCheck.isErrorEnabled = false
            binding.etSignUpPwCheck.error = null
            binding.tvErrorPwCheck.visibility = View.GONE
        }

        // endIcon을 다시 활성화하여 토글 버튼 유지
        binding.etSignUpPwCheck.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE


        return !isEmpty && isMatch
    }

    private fun onClickSignUpBtn() {
        // 회원가입 완료
        binding.btnSignUpDone.setOnClickListener {
            val action = SignUpSecondFragmentDirections.actionSignUpSecondToLogin()
            findNavController().navigate(action)
        }
    }

    private fun validateInput(
        textInputLayout: TextInputLayout,
        errorText: TextView,
        regex: Regex
    ): Boolean {
        val inputText = textInputLayout.editText?.text.toString().trim()

        return if (inputText.isEmpty() || !regex.matches(inputText)) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "에러 메시지"
            errorText.visibility = View.VISIBLE
            false
        } else {
            textInputLayout.isErrorEnabled = false
            errorText.visibility = View.GONE
            textInputLayout.error = null
            true
        }
    }

    private fun validateInputPW(
        textInputLayout: TextInputLayout,
        errorText: TextView,
        regex: Regex
    ): Boolean {
        val inputText = textInputLayout.editText?.text.toString().trim()

        return if (inputText.isEmpty() || !regex.matches(inputText)) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = "에러 메시지"
            textInputLayout.errorIconDrawable = null
            errorText.visibility = View.VISIBLE
            false
        } else {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = null
            errorText.visibility = View.GONE
            true
        }.also {
            textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        }
    }

    private fun onClickAuthCheckBtn() {
        with(binding) {
            btnSignUpAuthNumber.setOnClickListener {
                val tempPW = "123456"
                val pw = binding.etSignUpAuthNumber.editText?.text.toString()

                if (pw.isEmpty()) {
                    tvErrorAuthNumber.visibility = View.VISIBLE
                    tvErrorAuthNumber.text = getString(R.string.sign_up_error_auth_number)
                    tvErrorAuthNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.negative))
                } else if (pw == tempPW) {
                    tvErrorAuthNumber.visibility = View.VISIBLE
                    tvErrorAuthNumber.text = getString(R.string.common_auth_number_check_success)
                    tvErrorAuthNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.positive))
                    btnSignUpAuthNumber.isEnabled = false
                } else {
                    tvErrorAuthNumber.visibility = View.VISIBLE
                    tvErrorAuthNumber.text = getString(R.string.common_auth_number_check_failure)
                    tvErrorAuthNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.negative))
                }
            }
        }
    }

    private fun onClickSignUpState() {
        var isCheck1 = false
        var isCheck2 = false
        with(binding) {
            btnSignUpId.setOnClickListener {
                // 아이디 중복검사
                isCheck1 = true
                updateSignUPBtn(isCheck1, isCheck2)
            }
            btnSignUpNickname.setOnClickListener {
                // 닉네임 중복검사
                isCheck2 = true
                updateSignUPBtn(isCheck1, isCheck2)
            }
        }
    }

    private fun updateSignUPBtn(chk1: Boolean, chk2: Boolean) {
        binding.btnSignUpDone.isEnabled = chk1 && chk2
    }
}