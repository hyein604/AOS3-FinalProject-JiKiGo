package com.protect.jikigo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.PurchasedCoupon
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TravelPaymentBottomSheetViewModel @Inject constructor(
    private val userRepo: UserRepo
) : ViewModel() {
    private val _item = MutableLiveData<UserInfo?>()
    val item: LiveData<UserInfo?> get() = _item

    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            try {
                val userInfo = userRepo.getUserInfo(userId)
                if (userInfo != null) {
                    _item.value = userInfo //
                } else {
                    Log.e("HomeViewModel", "User info not found for id: $userId")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching user info", e)
            }
        }
    }

    fun setPurchasedCoupon(userId : String, purchasedCoupon : PurchasedCoupon) {
        viewModelScope.launch {
            try {
                userRepo.setPurchasedCoupon(userId, purchasedCoupon)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching user info", e)
            }
        }
    }

    fun updateUserPoints(userId: String, usedPoint: Int) {
        viewModelScope.launch {
            try {
                userRepo.updateUserPoint(userId, usedPoint)
            } catch (e: Exception) {
                Log.e("ViewModel", "Error updating points: ${e.message}")
            }
        }
    }
}