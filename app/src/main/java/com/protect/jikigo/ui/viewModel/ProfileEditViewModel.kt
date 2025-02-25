package com.protect.jikigo.ui.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.repo.MyPageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val myPageRepo: MyPageRepo
): ViewModel() {
    private val _profile = MutableLiveData<UserInfo>()
    val profile: LiveData<UserInfo> get() = _profile

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    private val _changeImage = MutableLiveData<Boolean>()
    val changeImage: LiveData<Boolean> get() = _changeImage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val documentId = MutableLiveData<String>()

    fun loadProfile(userid: String) {
        myPageRepo.getProfile(userid) { profile, id ->
            _profile.value = profile!!
            documentId.value = id!!
        }
    }

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    fun checkChange() {
        _changeImage.value = true
        _imageUri.value = null
    }

    fun saveProfile(userid: String, userNickName: String) {
        _isLoading.value = true
        if(imageUri.value != null) {
            myPageRepo.uploadProfileImage(
                userid, imageUri.value!!,
                onSuccess = {
                    val updates = mapOf(
                        "userProfileImg" to it,
                        "userNickName" to userNickName
                    )

                    myPageRepo.updateUserInfo(documentId.value!!, updates) {
                        if(!it) {
                            saveProfile(userid, userNickName)
                        }
                        _isLoading.value = false
                    }
                },
                onFailure = {
                    Log.d("profileEdit", "$it")
                }
            )
        }

        if(imageUri.value == null && changeImage.value == true)  {
            val updates = mapOf(
                "userProfileImg" to "https://www.studiopeople.kr/common/img/default_profile.png",
                "userNickName" to userNickName
            )
            myPageRepo.updateUserInfo(documentId.value!!, updates) {
                if(!it) {
                    saveProfile(userid, userNickName)
                }
                _isLoading.value = false
            }
        }

        if(imageUri.value == null && changeImage.value == false) {
            val updates = mapOf(
                "userNickName" to userNickName
            )

            myPageRepo.updateUserInfo(documentId.value!!, updates) {
                if(!it) {
                    saveProfile(userid, userNickName)
                }
                _isLoading.value = false
            }
        }
    }
}