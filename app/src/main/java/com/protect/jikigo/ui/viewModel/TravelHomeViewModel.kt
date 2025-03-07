package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.R
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.data.repo.CouponRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelHomeViewModel @Inject constructor(
    private val couponRepo: CouponRepo
) : ViewModel() {

    private val _hotCoupons = MutableLiveData<List<Coupon>>()
    val hotCoupons: LiveData<List<Coupon>> get() = _hotCoupons

    private val _bannerImages = MutableLiveData<List<Int>>()
    val bannerImages: LiveData<List<Int>> get() = _bannerImages

    init {
        fetchHotCoupons()
        loadBannerImages()
    }

    private fun fetchHotCoupons() {
        viewModelScope.launch {
            val coupons = couponRepo.getAllCouponSortedBySales().take(4)
            _hotCoupons.value = coupons
        }
    }

    private fun loadBannerImages() {
        _bannerImages.value = listOf(
            R.drawable.img_travel_banner1,
            R.drawable.img_travel_banner2,
            R.drawable.img_travel_banner3
        )
    }
}
