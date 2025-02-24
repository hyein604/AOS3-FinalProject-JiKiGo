package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _bannerImages = MutableLiveData<List<String>>()
    val bannerImages: LiveData<List<String>> get() = _bannerImages

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
            "https://lh3.googleusercontent.com/proxy/iA2VKsfQq-DYS_Axoz4zkjw_fnijbMtOMCc7ZxFXgV5RYyQ9wZS_JFZZ2ypzdyI3ZUJQpbE8a7yFHQAXR-lycYe2HhneOZdWOGJIU_Ysc5m-0H99kDVKCSLnNMeFng",
            "https://media.triple.guide/triple-cms/c_limit,f_auto,h_1024,w_1024/506341c9-1d0e-4f41-9081-40f6c9fbb4ec.jpeg",
            "https://i.namu.wiki/i/xN8ho4RHhhOntPkg6lUzKqkUmIvwARA0KzMjv8xZm9hP-T64alryJs4APV255xFBnoL74ea0RwWurso8PvnCSw.webp"
        )
    }
}
