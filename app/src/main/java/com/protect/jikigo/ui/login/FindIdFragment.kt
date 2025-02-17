package com.protect.jikigo.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentFindIdBinding
import com.protect.jikigo.ui.extensions.setTimer


class FindIdFragment : Fragment() {
    private var _binding: FragmentFindIdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindIdBinding.inflate(inflater, container, false)
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
        checkInput()
        onClickAuthBtn()
        editTextWatcher()
        onClickAuthCheckBtn()
    }

    private fun checkInput() {
        val regexName = "^[가-힣]{2,8}$".toRegex()
        val regexMobile = "^[0-9]{11}$".toRegex()
        val regexAuthNumber = "^[0-9]{6}$".toRegex()

        binding.btnFindId.setOnClickListener {
            var isValid = true
            if (!validateInput(binding.etFindIdName, binding.tvErrorName, regexName)) {
                isValid = false
            }
            if (!validateInput(binding.etFindPwMobile, binding.tvErrorMobile, regexMobile)) {
                isValid = false
            }
            if (!validateInput(binding.etFindIdAuthNumber, binding.tvErrorAuthNumber, regexAuthNumber)) {
                isValid = false
            }

            if (isValid) {
                // db전송 모든 검사 완료
                // id 보여주기
                binding.tvFindId.text = "찾은 아이디!"
                binding.tvFindId.visibility = View.VISIBLE
            }
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


    private fun editTextWatcher() {
        with(binding) {
            etFindIdName.editText?.addTextChangedListener(object : TextWatcher {
                // 변경 전
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 변경 중
                    if (s != null) {
                        if (s.isEmpty()) {
                            tvErrorName.visibility = View.VISIBLE
                            tvErrorName.text = getString(R.string.sign_up_error_name)
                            tvErrorName.setTextColor(ContextCompat.getColor(requireContext(), R.color.negative))
                            etFindPwMobile.isEnabled = false
                            etFindIdAuthNumber.isEnabled = false
                        } else {
                            tvErrorName.visibility = View.GONE
                            etFindPwMobile.isEnabled = true
                            etFindIdAuthNumber.isEnabled = true
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // 입력이 완료된 후
                    btnFindIdAuthNumber.isEnabled = true
                }
            })
        }
    }

    private fun onClickAuthBtn() {
        with(binding) {

            btnFindIdAuthNumber.setOnClickListener {
                val mobile = etFindPwMobile.editText?.text.toString()
                if (mobile.isEmpty()) {
                    tvErrorMobile.visibility = View.VISIBLE
                    tvErrorMobile.text = getString(R.string.sign_up_error_mobile)
                    tvErrorMobile.setTextColor(ContextCompat.getColor(requireContext(), R.color.negative))
                } else {
                    tvErrorMobile.visibility = View.VISIBLE
                    tvErrorMobile.text = getString(R.string.common_auth_number_check)
                    tvErrorMobile.setTextColor(ContextCompat.getColor(requireContext(), R.color.positive))
                    btnFindIdAuthNumber.setTimer(lifecycleScope, etFindPwMobile, requireContext())
                }
            }
        }
    }


    private fun onClickAuthCheckBtn() {
        with(binding) {
            btnFindIdAuthNumberCheck.setOnClickListener {
                val tempPW = "123456"
                val pw = binding.etFindIdAuthNumber.editText?.text.toString()

                if (pw.isEmpty()) {
                    tvErrorAuthNumber.visibility = View.VISIBLE
                    tvErrorAuthNumber.text = getString(R.string.sign_up_error_auth_number)
                    tvErrorAuthNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.negative))
                } else if (pw == tempPW) {
                    tvErrorAuthNumber.visibility = View.VISIBLE
                    tvErrorAuthNumber.text = getString(R.string.common_auth_number_check_success)
                    tvErrorAuthNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.positive))
                    btnFindIdAuthNumberCheck.isEnabled = false
                } else {
                    tvErrorAuthNumber.visibility = View.VISIBLE
                    tvErrorAuthNumber.text = getString(R.string.common_auth_number_check_failure)
                    tvErrorAuthNumber.setTextColor(ContextCompat.getColor(requireContext(), R.color.negative))
                }
            }
        }
    }

    companion object {
        fun newInstance(): FindIdFragment {
            return FindIdFragment().apply {

            }
        }
    }
}