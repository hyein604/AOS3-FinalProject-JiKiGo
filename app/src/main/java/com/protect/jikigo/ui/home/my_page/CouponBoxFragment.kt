package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.protect.jikigo.R
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.databinding.FragmentCouponBoxBinding
import com.protect.jikigo.ui.adapter.CouponBoxAdapter
import com.protect.jikigo.ui.adapter.CouponOnClickListener
import com.protect.jikigo.ui.extensions.statusBarColor


class CouponBoxFragment : Fragment(), CouponOnClickListener {
    private var _binding: FragmentCouponBoxBinding? = null
    private val binding get() = _binding!!
    private val adapter: CouponBoxAdapter by lazy { CouponBoxAdapter(this) }

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
        requireActivity().statusBarColor(R.color.white)
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

    /*
    리사이클러 뷰
     */

    private fun recycler() {
        binding.recyclerCouponBox.adapter = adapter
    }

    override fun onClickListener(item: Coupon) {
        val action = CouponBoxFragmentDirections.actionCouponBoxFragmentToCouponDetailFragment(item)
        findNavController().navigate(action)
    }

    /*
    탭
     */

    private fun tabLayout() {
        binding.apply {
            tabCouponBox.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    TODO("Not yet implemented")
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

}