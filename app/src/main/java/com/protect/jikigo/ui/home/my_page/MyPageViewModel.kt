package com.protect.jikigo.ui.home.my_page


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(

): ViewModel() {
    val totalSteps = MutableLiveData<String>()
}