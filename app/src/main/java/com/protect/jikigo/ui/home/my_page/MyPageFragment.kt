package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentMyPageBinding


class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
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
        moveToEditProfile()
        moveToPointHistory()
        moveToCouponBox()
    }

    private fun moveToEditProfile() {
        binding.btnMyPageProfileEdit.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageToProfileEditFragment()
            findNavController().navigate(action)
        }
    }

    private fun moveToPointHistory() {
        binding.ivMyPagePoint.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageToPointHistoryFragment()
            findNavController().navigate(action)
        }
    }

    private fun moveToCouponBox() {
        binding.viewMyPagePoint.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageToCouponBoxFragment()
            findNavController().navigate(action)
        }
    }


}