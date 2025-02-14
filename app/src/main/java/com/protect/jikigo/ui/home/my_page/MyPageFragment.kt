package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentMyPageBinding
import com.protect.jikigo.ui.extensions.statusBarColor


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
        setStatusBar()
        moveToEditProfile()
        moveToPointHistory()
        moveToCouponBox()
        onClickToolbar()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun moveToEditProfile() {
        binding.btnMyPageProfileEdit.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageToProfileEdit()
            findNavController().navigate(action)
        }
    }

    private fun moveToPointHistory() {
        binding.viewMyPagePoint.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageToPointHistory()
            findNavController().navigate(action)
        }
    }

    private fun moveToCouponBox() {
        binding.viewMyPageCoupon.setOnClickListener {
            val action = MyPageFragmentDirections.actionMyPageToCouponBox()
            findNavController().navigate(action)
        }
    }

    private fun onClickToolbar() {
        binding.toolbarMyPage.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


}