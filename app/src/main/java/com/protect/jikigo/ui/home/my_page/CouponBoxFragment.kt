package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentCouponBoxBinding
import com.protect.jikigo.databinding.FragmentPointHistoryBinding
import com.protect.jikigo.ui.adapter.CouponBoxAdapter
import com.protect.jikigo.ui.adapter.PointHistoryAdapter
import com.protect.jikigo.ui.extensions.statusBarColor


class CouponBoxFragment : Fragment() {
    private var _binding: FragmentCouponBoxBinding? = null
    private val binding get() = _binding!!
    private val adapter: CouponBoxAdapter by lazy { CouponBoxAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponBoxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBar()
        setLayout()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.primary)
    }

    private fun setLayout() {
        onClickToolbar()
        recycler()
    }

    private fun onClickToolbar() {
        binding.toolbarCouponBox.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun recycler() {
        binding.recyclerCouponBox.adapter = adapter
    }

}