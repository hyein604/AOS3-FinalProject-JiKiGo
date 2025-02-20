package com.protect.jikigo.ui.home.my_page

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileEditViewModel: ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }
}