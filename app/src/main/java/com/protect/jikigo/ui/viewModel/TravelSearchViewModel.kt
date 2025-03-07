package com.protect.jikigo.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.data.repo.CouponRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelSearchViewModel @Inject constructor(
    private val couponRepo: CouponRepo,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _couponList = MutableLiveData<List<Coupon>>()
    val couponList: LiveData<List<Coupon>> get() = _couponList

    private val _filteredCoupons = MutableLiveData<List<Coupon>>()
    val filteredCoupons: LiveData<List<Coupon>> get() = _filteredCoupons

    private val _recentSearches = MutableLiveData<List<String>>()
    val recentSearches: LiveData<List<String>> get() = _recentSearches

    private val sharedPreference by lazy {
        context.getSharedPreferences("recent_search", Context.MODE_PRIVATE)
    }

    init {
        fetchAllCoupons()
        loadRecentSearches()
    }

    private fun fetchAllCoupons() {
        viewModelScope.launch {
            _couponList.value = couponRepo.getAllCoupon()
        }
    }

    fun filterCoupons(query: String) {
        val allCoupons = _couponList.value ?: emptyList()
        _filteredCoupons.value = allCoupons.filter {
            it.couponName.contains(query, ignoreCase = true) || it.couponBrand.contains(query, ignoreCase = true)
        }
    }

    // 검색어 추가
    fun addRecentSearch(query: String) {
        val searches = getRecentSearches().toMutableList()

        // 중복 방지: 이미 리스트에 있는 검색어는 제거하고 맨 앞에 추가
        searches.remove(query)
        searches.add(0, query)

        // 최근 검색어가 10개를 넘으면 마지막 검색어 제거
        if (searches.size > 10) {
            searches.removeAt(searches.size - 1)
        }

        // 최근 검색어 리스트를 SharedPreferences에 문자열 형태로 저장
        sharedPreference.edit().putString("chips", searches.joinToString(",")).apply()

        // LiveData에 반영
        _recentSearches.value = searches
    }

    // 검색어 삭제
    fun removeRecentSearch(query: String) {
        val searches = getRecentSearches().toMutableList()
        searches.remove(query)
        sharedPreference.edit().putString("chips", searches.joinToString(",")).apply()
        _recentSearches.value = searches
    }

    // 최근 검색어 리스트 가져오기
    private fun getRecentSearches(): List<String> {
        val saved = sharedPreference.getString("chips", "")
        return if (!saved.isNullOrEmpty()) saved.split(",") else emptyList()
    }

    private fun loadRecentSearches() {
        _recentSearches.value = getRecentSearches()
    }

}
