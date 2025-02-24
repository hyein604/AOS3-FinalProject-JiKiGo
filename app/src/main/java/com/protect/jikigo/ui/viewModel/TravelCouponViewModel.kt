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
class TravelCouponViewModel @Inject constructor(
    private val couponRepo: CouponRepo
) : ViewModel() {

    private val _selectedBrand = MutableLiveData<String>("전체보기")
    val selectedBrand: LiveData<String> get() = _selectedBrand

    private val _filteredCoupons = MutableLiveData<List<Coupon>>()
    val filteredCoupons: LiveData<List<Coupon>> get() = _filteredCoupons

    private val _brandList = MutableLiveData<List<String>>()
    val brandList: LiveData<List<String>> get() = _brandList

    private var category: String = "숙박"

    fun setCategory(index: Int) {
        category = when (index) {
            1 -> "숙박"
            2 -> "레저/티켓"
            3 -> "공연/전시"
            4 -> "여행용품"
            else -> "숙박"
        }
        fetchCoupons()
    }

    private fun fetchCoupons() {
        viewModelScope.launch {
            val coupons = couponRepo.getCouponByCategory(category)
            _brandList.value = coupons.map { it.couponBrand }.toSet().sorted()
            _filteredCoupons.value = coupons
        }
    }

    fun selectBrand(brand: String) {
        _selectedBrand.value = brand
        applyFiltersAndSorting("추천순")
    }

    fun applyFiltersAndSorting(sortOption: String) {
        viewModelScope.launch {
            val filteredCoupons = if (_selectedBrand.value == "전체보기") {
                couponRepo.getCouponByCategoryAndSort(category, sortOption)
            } else {
                couponRepo.getCouponByBrandAndCategoryAndSort(_selectedBrand.value!!, category, sortOption)
            }
            _filteredCoupons.value = filteredCoupons
        }
    }
}
