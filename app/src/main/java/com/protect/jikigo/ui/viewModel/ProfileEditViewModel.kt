package com.protect.jikigo.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.repo.MyPageRepo
import javax.inject.Inject

class ProfileEditViewModel@Inject constructor(
    private val myPageRepo: MyPageRepo
): ViewModel() {
    private val _profile = MutableLiveData<UserInfo>()
    val profile: LiveData<UserInfo> get() = _profile

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    private val changeImage = MutableLiveData<Boolean>(false)

    private val image = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadProfile(userid: String) {
        myPageRepo.getProfile(userid) { profile ->
            _profile.value = profile!!
        }
    }

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun saveProfile(userid: String, userName: String) {
        _isLoading.value = true
        if(imageUri.value != null) {
            myPageRepo.uploadProfileImage(
                userid, imageUri.value!!,
                onSuccess = {
                    image.value = it
                    changeImage.value = true
                },
                onFailure = {
                    Log.d("profileEdit", "$it")
                }
            )

            val updates = mapOf(
                "userProfileImg" to image.value!!,
                "userName" to userName
            )

            myPageRepo.updateUserInfo(userid, updates) {
                if(!it) {
                    saveProfile(userid, userName)
                }
                _isLoading.value = false
            }
        }

        if(imageUri.value == null && changeImage.value == true)  {
            val updates = mapOf(
                "userProfileImg" to "https://www.studiopeople.kr/common/img/default_profile.png",
                "userName" to userName
            )
            myPageRepo.updateUserInfo(userid, updates) {
                if(!it) {
                    saveProfile(userid, userName)
                }
                _isLoading.value = false
            }
        }

        if(imageUri.value == null && changeImage.value == false) {
            val updates = mapOf(
                "userName" to userName
            )

            myPageRepo.updateUserInfo(userid, updates) {
                if(!it) {
                    saveProfile(userid, userName)
                }
                _isLoading.value = false
            }
        }
    }
}