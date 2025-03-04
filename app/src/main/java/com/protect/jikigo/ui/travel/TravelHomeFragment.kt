package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.R
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.databinding.FragmentTravelHomeBinding
import com.protect.jikigo.ui.adapter.CouponHorizontalAdapter
import com.protect.jikigo.ui.adapter.TravelBannerAdapter
import com.protect.jikigo.ui.adapter.TravelCouponHorizontalOnClickListener
import com.protect.jikigo.utils.applySpannableStyles
import com.protect.jikigo.utils.statusBarColor
import com.protect.jikigo.ui.viewModel.TravelHomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TravelHomeFragment : Fragment(), TravelCouponHorizontalOnClickListener {
    private var _binding: FragmentTravelHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TravelHomeViewModel by viewModels()

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
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        setupObservers()
    }

    private fun setLayout() {
        setStatusBar()
        moveToSearch()
        moveToHotCoupon()
        setText()
    }

    private fun setupObservers() {
        binding.shimmerTravelHomeCoupon.startShimmer()
        binding.shimmerTravelHomeCoupon.visibility = View.VISIBLE
        binding.rvHotCouponList.visibility = View.GONE

        viewModel.hotCoupons.observe(viewLifecycleOwner) { coupons ->
            binding.rvHotCouponList.adapter = CouponHorizontalAdapter(coupons, this)

            if (!isAdded || view == null) return@observe

            stopShimmer()
        }

        viewModel.bannerImages.observe(viewLifecycleOwner) { images ->
            setBannerSlider(images)
        }
    }

    private fun stopShimmer() {
        if (!isAdded || view == null) return

        binding.shimmerTravelHomeCoupon.stopShimmer()
        binding.shimmerTravelHomeCoupon.visibility = View.GONE
        binding.rvHotCouponList.visibility = View.VISIBLE
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
        binding.ivTravelHomeMore.setOnClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelHotCoupon()
            findNavController().navigate(action)
        }
    }

    private fun setBannerSlider(bannerImages: List<Int>) {
        val adapter = TravelBannerAdapter(bannerImages) { position ->
            val actualPosition = (position - 1 + bannerImages.size) % bannerImages.size
            val action = TravelFragmentDirections.actionNavigationTravelToTravelBanner(actualPosition)
            findNavController().navigate(action)
        }

        binding.vpTravelBanner.adapter = adapter

        TabLayoutMediator(binding.indicatorTravelBanner, binding.vpTravelBanner) { tab, position ->
            tab.view.visibility = if (position == 0 || position == bannerImages.size + 1) View.GONE else View.VISIBLE
        }.attach()

        startAutoSlide()
        binding.vpTravelBanner.setCurrentItem(1, false)

        binding.vpTravelBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val itemCount = binding.vpTravelBanner.adapter?.itemCount ?: return

                binding.vpTravelBanner.postDelayed({
                    when (position) {
                        0 -> binding.vpTravelBanner.setCurrentItem(itemCount - 2, false)
                        itemCount - 1 -> binding.vpTravelBanner.setCurrentItem(1, false)
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
                    binding.vpTravelBanner.setCurrentItem(0, true)
                } else {
                    binding.vpTravelBanner.setCurrentItem(nextPage, true)
                }
                handler.postDelayed(this, 3000)
            }
        }

        handler.postDelayed(slideRunnable, 3000)
    }

    private fun setText() {
        binding.tvTravelHomeHotCoupon.applySpannableStyles(0, 3, R.color.primary)
    }

    override fun onClickListener(item: Coupon) {
        val action = TravelFragmentDirections.actionNavigationTravelToTravelCouponDetail(item)
        findNavController().navigate(action)
    }
}
