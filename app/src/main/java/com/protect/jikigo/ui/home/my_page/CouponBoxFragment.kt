package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.protect.jikigo.R
import com.protect.jikigo.data.model.PurchasedCoupon
import com.protect.jikigo.databinding.FragmentCouponBoxBinding
import com.protect.jikigo.ui.adapter.CouponBoxAdapter
import com.protect.jikigo.ui.adapter.CouponOnClickListener
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.utils.statusBarColor
import com.protect.jikigo.ui.viewModel.CouponBoxViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CouponBoxFragment : Fragment(), CouponOnClickListener {
    private var _binding: FragmentCouponBoxBinding? = null
    private val binding get() = _binding!!
    private val adapter: CouponBoxAdapter by lazy { CouponBoxAdapter(this) }
    private lateinit var userId: String
    private val viewModel: CouponBoxViewModel by viewModels()
    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            currentPosition = it.getInt(KEY_CURRENT_POSITION, 0)
        }
    }

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
        viewModel.startLoading()
        lifecycleScope.launch {
            userId = requireContext().getUserId() ?: ""
            viewModel.updateCouponsExpiry(userId)
            when(currentPosition) {
                0 -> {
                    binding.tabCouponBox.post {
                        binding.tabCouponBox.setScrollPosition(currentPosition, 0f, false) // 스크롤 즉시 이동
                        binding.tabCouponBox.getTabAt(currentPosition)?.select() // 탭 선택
                    }
                    viewModel.loadCouponData(userId)
                }
                1 -> {
                    binding.tabCouponBox.post {
                        binding.tabCouponBox.setScrollPosition(currentPosition, 0f, false) // 스크롤 즉시 이동
                        binding.tabCouponBox.getTabAt(currentPosition)?.select() // 탭 선택
                    }
                    viewModel.loadUsedCouponData(userId)
                }
            }
        }
    }

    private fun observe() {
        binding.apply {
            viewModel.couponList.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) {
                when (it) {
                    true -> {
                        requireActivity().window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        layoutCouponBoxLoading.isVisible = true
                    }

                    false -> {
                        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        layoutCouponBoxLoading.isVisible = false
                    }
                }
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
                            viewModel.startLoading()
                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(DELAY_TIME) // 초기 탭 선택 시 자연스러운 애니메이션을 위해 딜레이를 준다.
                                // 뷰가 파괴되지 않았는지 확인
                                if (_binding != null) {
                                    currentPosition = 0
                                    viewModel.loadCouponData(userId)
                                }
                            }
                        }
                        1 -> {
                            viewModel.startLoading()
                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(DELAY_TIME) // 초기 탭 선택 시 자연스러운 애니메이션을 위해 딜레이를 준다.
                                // 뷰가 파괴되지 않았는지 확인
                                if (_binding != null) {
                                    currentPosition = 1
                                    viewModel.loadUsedCouponData(userId)
                                }
                            }
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when(tab?.position) {
                        0 -> {
                            viewModel.startLoading()
                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(DELAY_TIME) // 초기 탭 선택 시 자연스러운 애니메이션을 위해 딜레이를 준다.
                                // 뷰가 파괴되지 않았는지 확인
                                if (_binding != null) {
                                    currentPosition = 0
                                    viewModel.loadCouponData(userId)
                                }
                            }
                        }
                        1 -> {
                            viewModel.startLoading()
                            viewLifecycleOwner.lifecycleScope.launch {
                                delay(DELAY_TIME) // 초기 탭 선택 시 자연스러운 애니메이션을 위해 딜레이를 준다.
                                // 뷰가 파괴되지 않았는지 확인
                                if (_binding != null) {
                                    currentPosition = 1
                                    viewModel.loadUsedCouponData(userId)
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    companion object {
        private const val KEY_CURRENT_POSITION = "current_position"
        private const val DELAY_TIME = 100L
    }

}