package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.PurchasedCoupon
import com.protect.jikigo.data.repo.CouponBoxRepo
import com.protect.jikigo.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CouponBoxViewModel @Inject constructor(
    private val couponBoxRepo: CouponBoxRepo,
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _couponList = MutableLiveData<MutableList<PurchasedCoupon>>()
    val couponList: LiveData<MutableList<PurchasedCoupon>> get() = _couponList

    fun loadCouponData(userId: String) {
        couponBoxRepo.loadCouponData(userId) {
            _couponList.value = it
            _isLoading.value = false
        }
    }

    fun startLoading() {
        _isLoading.value = true
    }

    fun loadUsedCouponData(userId: String) {
        couponBoxRepo.loadUsedCouponData(userId) {
            _couponList.value = it
            _isLoading.value = false
        }
    }

    suspend fun updateCouponsExpiry(userId: String) {
        couponBoxRepo.updateCouponsExpiry(userId)
    }
}