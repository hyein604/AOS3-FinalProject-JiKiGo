package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.R
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.Storage
import com.protect.jikigo.databinding.FragmentTravelHomeBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelBannerAdapter
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.applySpannableStyles
import com.protect.jikigo.ui.extensions.statusBarColor

class TravelHomeFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelHomeBinding? = null
    private val binding get() = _binding!!

    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelHomeBinding.inflate(inflater, container, false)
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
        moveToSearch()
        setBannerSlider()
        moveToHotCoupon()
        setText()
        setRecyclerView()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.primary)
    }

    private fun moveToSearch() {
        binding.searchBar.setOnClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelSearch()
            findNavController().navigate(action)
        }
        binding.searchBar.setOnMenuItemClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelSearch()
            findNavController().navigate(action)
            true
        }
    }

    private fun moveToHotCoupon() {
        binding.tvTravelHomeMore.setOnClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelHotCoupon()
            findNavController().navigate(action)
        }
    }

    private fun setBannerSlider(){
        val bannerImages = listOf(
            "https://lh3.googleusercontent.com/proxy/iA2VKsfQq-DYS_Axoz4zkjw_fnijbMtOMCc7ZxFXgV5RYyQ9wZS_JFZZ2ypzdyI3ZUJQpbE8a7yFHQAXR-lycYe2HhneOZdWOGJIU_Ysc5m-0H99kDVKCSLnNMeFng",
            "https://media.triple.guide/triple-cms/c_limit,f_auto,h_1024,w_1024/506341c9-1d0e-4f41-9081-40f6c9fbb4ec.jpeg",
            "https://i.namu.wiki/i/xN8ho4RHhhOntPkg6lUzKqkUmIvwARA0KzMjv8xZm9hP-T64alryJs4APV255xFBnoL74ea0RwWurso8PvnCSw.webp"
        )

        val adaptor = TravelBannerAdapter(bannerImages) { position ->
            val actualPosition =  (position - 1 + bannerImages.size) % bannerImages.size
            when (position) {
                0 -> {
                    Toast.makeText(requireContext(), "$actualPosition 번째 배너", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    Toast.makeText(requireContext(), "$actualPosition 번째 배너", Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    Toast.makeText(requireContext(), "$actualPosition 번째 배너", Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }
        binding.vpTravelBanner.adapter = adaptor

        TabLayoutMediator(binding.indicatorTravelBanner, binding.vpTravelBanner) { tab, position ->
            // 실제 데이터 개수 (더미 제외)
            val itemCount = bannerImages.size

            if (position == 0 || position == itemCount + 1) {
                // 첫 번째와 마지막(더미 데이터) 인디케이터 숨기기
                tab.view.visibility = View.GONE
            } else {
                tab.view.visibility = View.VISIBLE
            }
        }.attach()

        startAutoSlide()

        binding.vpTravelBanner.setCurrentItem(1, false)

        binding.vpTravelBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val itemCount = binding.vpTravelBanner.adapter?.itemCount ?: return

                binding.vpTravelBanner.postDelayed({
                    when (position) {
                        0 -> binding.vpTravelBanner.setCurrentItem(itemCount - 2, false) // 첫 번째 더미 → 마지막 아이템
                        itemCount - 1 -> binding.vpTravelBanner.setCurrentItem(1, false) // 마지막 더미 → 첫 번째 아이템
                    }
                }, 300)
            }
        })
    }

    private fun startAutoSlide() {
        val itemCount = binding.vpTravelBanner.adapter?.itemCount ?: return

        val slideRunnable = object : Runnable {
            override fun run() {
                val nextPage = binding.vpTravelBanner.currentItem + 1
                if (nextPage == itemCount) {
                    // 마지막 더미에 도달하면 첫 번째 실제 아이템으로 이동
                    binding.vpTravelBanner.setCurrentItem(0, true)
                } else {
                    // 다음 페이지로 이동
                    binding.vpTravelBanner.setCurrentItem(nextPage, true)
                }
                handler.postDelayed(this, 3000)
            }
        }

        // 슬라이드 시작
        handler.postDelayed(slideRunnable, 3000)
    }

    private fun setRecyclerView(){
        val coupon = Storage.coupon.sortedByDescending { it.salesCount }.take(4)
        val adapter = CouponAdaptor(coupon, this)
        binding.rvHotCouponList.adapter = adapter
    }

    private fun setText() {
        binding.tvTravelHomeHotCoupon.applySpannableStyles(
            0, 3, R.color.primary
        )
    }

    override fun onClickListener(item: Coupon) {
        val action = TravelFragmentDirections.actionNavigationTravelToTravelCouponDetail()
        findNavController().navigate(action)
    }
}