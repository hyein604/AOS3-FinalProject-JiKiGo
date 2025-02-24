package com.protect.jikigo.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.Confirm
import com.protect.jikigo.data.repo.ConfirmRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransitConfirmPhotoViewModel @Inject constructor(
    private val confirmRepo: ConfirmRepo,
): ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    private val _downloadUrl = MutableLiveData<String>()
    val downloadUrl: LiveData<String> get() = _downloadUrl

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun saveConfirmUri() {
        _isLoading.value = true
        confirmRepo.uploadConfirmImage(
            imageUri.value!!,
            onSuccess = { url -> _downloadUrl.value = url},
            onFailure =  { exception -> Log.d("confirm", "$exception") }
        )
    }

    fun saveConfirmInfo(item: Confirm) {
        confirmRepo.addConfirmItem(item)
        _isLoading.value = false
    }
}