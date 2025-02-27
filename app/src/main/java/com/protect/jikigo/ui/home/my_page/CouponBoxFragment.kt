package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.protect.jikigo.R
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.model.PurchasedCoupon
import com.protect.jikigo.databinding.FragmentCouponBoxBinding
import com.protect.jikigo.ui.adapter.CouponBoxAdapter
import com.protect.jikigo.ui.adapter.CouponOnClickListener
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.extensions.statusBarColor
import com.protect.jikigo.ui.viewModel.CouponBoxViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CouponBoxFragment : Fragment(), CouponOnClickListener {
    private var _binding: FragmentCouponBoxBinding? = null
    private val binding get() = _binding!!
    private val adapter: CouponBoxAdapter by lazy { CouponBoxAdapter(this) }
    private lateinit var userId: String
    private val viewModel: CouponBoxViewModel by viewModels()

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
        observe()
        checkData()
        tabLayout()
    }

    private fun onClickToolbar() {
        binding.toolbarCouponBox.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkData() {
        lifecycleScope.launch {
            userId = requireContext().getUserId() ?: ""
            viewModel.loadCouponData(userId)
        }
    }

    private fun observe() {
        binding.apply {
            viewModel.couponList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    /*
    리사이클러 뷰
     */

    private fun recycler() {
        binding.recyclerCouponBox.adapter = adapter
    }

    override fun onClickListener(item: PurchasedCoupon) {
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
                    when(tab?.position) {
                        0 -> {
                            viewModel.loadCouponData(userId)
                        }
                        1 -> {
                            viewModel.loadUsedCouponData(userId)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when(tab?.position) {
                        0 -> {
                            viewModel.loadCouponData(userId)
                        }
                        1 -> {
                            viewModel.loadUsedCouponData(userId)
                        }
                    }
                }
            })
        }
    }

}