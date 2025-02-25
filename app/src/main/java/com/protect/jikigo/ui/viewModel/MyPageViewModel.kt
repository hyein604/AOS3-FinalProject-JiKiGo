package com.protect.jikigo.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.repo.MyPageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val myPageRepo: MyPageRepo
): ViewModel() {
    val totalSteps = MutableLiveData<String>()

    private val _profile = MutableLiveData<UserInfo>()
    val profile: LiveData<UserInfo> get() = _profile

    fun loadProfile(userid: String) {
       myPageRepo.getProfile(userid) { profile ->
           _profile.value = profile!!
       }
    }
}