package com.protect.jikigo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.databinding.FragmentSignUpFirstBinding


class SignUpFirstFragment : Fragment() {
    private var _binding: FragmentSignUpFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
    }

    // 클릭 메서드
    private fun onClick() {
        binding.btnSignUpConfirm.setOnClickListener {
            val action = SignUpFirstFragmentDirections.actionSignUpFirstToSignUpSecond()
            findNavController().navigate(action)
        }
        binding.toolbarSignUp.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun onClickMore() {
        binding.tvSignUpServiceMore.setOnClickListener {

        }
    }
}