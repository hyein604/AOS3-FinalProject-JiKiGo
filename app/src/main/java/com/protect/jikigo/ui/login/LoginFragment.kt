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
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.HomeActivity
import com.protect.jikigo.databinding.FragmentLoginBinding
import com.protect.jikigo.ui.extensions.getUserId
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

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            when(loading) {
                false -> {
                    binding.loadingContainer.visibility = View.GONE
                    binding.loginProgress.visibility = View.GONE
                    moveToHome()
                }
                true -> {
                    binding.loadingContainer.visibility = View.VISIBLE
                    binding.loginProgress.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun setLayout() {
        autoLogin()
        onClick()
    }

    private fun autoLogin() {
        lifecycleScope.launch {
            val userId = requireContext().getUserId()
            Log.d("userId", "$userId")

            if (userId != null) {
                moveToHome()
            }
        }
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
                viewModel.kakaoLogin()
            }
        }
    }

    private fun moveToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}