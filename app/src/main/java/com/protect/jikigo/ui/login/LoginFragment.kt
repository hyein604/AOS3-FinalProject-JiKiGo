package com.protect.jikigo.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.HomeActivity
import com.protect.jikigo.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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
        }
    }
}