package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.repo.DeleteIdRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteIdViewModel @Inject constructor(
    private val deleteIdRepo: DeleteIdRepo
): ViewModel() {
    // 체크상태
    private val _isAgreementChecked = MutableLiveData<Boolean>(false)
    val isAgreementChecked: LiveData<Boolean> get() = _isAgreementChecked

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun agreementChecked(boolean: Boolean) {
        _isAgreementChecked.value = boolean
    }

    fun startLoading() {
        _isLoading.value = true
    }

    fun deleteId(userId: String) {
        deleteIdRepo.deleteId(userId) {
            _isLoading.value = false
        }
    }


}