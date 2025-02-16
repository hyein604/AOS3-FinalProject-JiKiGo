package com.protect.jikigo.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.protect.jikigo.databinding.FragmentSignUpSecondBinding
import kotlin.math.truncate


class SignUpSecondFragment : Fragment() {
    private var _binding: FragmentSignUpSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickToolbar()
        checkInput()
    }

    private fun onClickToolbar() {
        binding.toolbarSignUp.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkInput() {
        val regexName = "^[가-힣]{2,8}$".toRegex()
        val regexMobile = "^[0-9]{11}$".toRegex()
        val regexAuthNumber = "^[0-9]{6}$".toRegex()
        val regexId = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{8,16}$".toRegex()
        val regexPw = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$])[A-Za-z0-9!@#$]{14,20}$".toRegex()
        val regexNickName = "^[가-힣a-zA-Z0-9]{2,10}$".toRegex()

        binding.btnSignUpDone.setOnClickListener {
            var isValid = true
            if (!validateInput(binding.etSignUpName, binding.tvErrorName, regexName)) {
                isValid = false
            }
            if (!validateInput(binding.etSignUpMobile, binding.tvErrorMobile, regexMobile)) {
                isValid = false
            }
            if (!validateInput(binding.etSignUpAuthNumber, binding.tvErrorAuthNumber, regexAuthNumber)) {
                isValid = false
            }
            if (!validateInput(binding.etSignUpId, binding.tvErrorId, regexId)) {
                isValid = false
            }
            if (!validateInput(binding.etSignUpPw, binding.tvErrorPw, regexPw)) {
                isValid = false
            }

            if (!checkPW()) {
                isValid = false
            }
            if (!validateInput(binding.etSignUpNickname, binding.tvErrorNickname, regexNickName)) {
                isValid = false
            }
            if (isValid) {
                // db전송 모든 검사 완료
                findNavController().navigateUp()
            }
        }
    }


    private fun checkPW(): Boolean {
        val pwMain = binding.etSignUpPw.editText?.text.toString().trim()
        val pwCheck = binding.etSignUpPwCheck.editText?.text.toString().trim()

        val isEmpty = pwCheck.isEmpty()
        val isMatch = pwMain == pwCheck

        // UI 처리
        binding.etSignUpPwCheck.isErrorEnabled = true
        binding.etSignUpPwCheck.error = "에러 메시지"

        binding.tvErrorPwCheck.visibility = if (isEmpty || !isMatch) View.VISIBLE else View.GONE

        return !isEmpty && isMatch
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

    private fun setTimerBtn() {

    }
}