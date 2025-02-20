package com.protect.jikigo.ui.home.my_page


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MyPageViewModel(): ViewModel() {
    val count = MutableLiveData<Long>()
}