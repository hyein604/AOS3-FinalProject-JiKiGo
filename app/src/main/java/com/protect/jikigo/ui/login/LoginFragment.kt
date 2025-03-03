package com.protect.jikigo.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.navercorp.nid.NaverIdLoginSDK
import com.protect.jikigo.ui.activity.HomeActivity
import com.protect.jikigo.databinding.FragmentLoginBinding
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.utils.showSnackBar
import com.protect.jikigo.ui.viewModel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

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
      //  autoLogin()
        observeViewModel()
        onClick()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            when (loading) {
                false -> {
                    binding.loadingContainer.visibility = View.GONE
                    binding.loginProgress.visibility = View.GONE
                }

                true -> {
                    binding.loadingContainer.visibility = View.VISIBLE
                    binding.loginProgress.visibility = View.VISIBLE
                }
            }
        }
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            when (isSuccess) {
                false -> {
                    requireContext().showSnackBar(binding.root, "로그인 실패")
                }

                true -> {
                    moveToHome()
                }
            }
        }
    }

    private fun autoLogin() {
        lifecycleScope.launch {
            val userId = requireContext().getUserId()
            Log.d("LoginFragment", "userId: $userId")

            if (!userId.isNullOrEmpty()) {
                moveToHome()
            } else {
                Log.d("LoginFragment", "userId: null")
            }
        }
    }

    // 각 요소 클릭 메서드
    private fun onClick() {
        with(binding) {
            btnLoginKakao.setOnClickListener {
                viewModel.kakaoLogin()
            }
            btnLoginNaver2.setOnClickListener {
                NaverIdLoginSDK.authenticate(requireContext(), viewModel.oAuthLoginCallback)
            }
        }
    }


    private fun moveToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}