package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.PurchasedCoupon
import com.protect.jikigo.data.repo.CouponBoxRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CouponBoxViewModel @Inject constructor(
    private val couponBoxRepo: CouponBoxRepo,
): ViewModel() {
    private val _couponList = MutableLiveData<MutableList<PurchasedCoupon>>()
    val couponList: LiveData<MutableList<PurchasedCoupon>> get() = _couponList

    fun loadCouponData(userId: String) {
        couponBoxRepo.loadCouponData(userId) {
            _couponList.value = it
        }
    }

    fun loadUsedCouponData(userId: String) {
        couponBoxRepo.loadUsedCouponData(userId) {
            _couponList.value = it
        }
    }
}