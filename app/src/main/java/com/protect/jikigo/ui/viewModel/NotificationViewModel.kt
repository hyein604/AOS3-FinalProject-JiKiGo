package com.protect.jikigo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.Notification
import com.protect.jikigo.data.repo.NotificationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepo
) : ViewModel() {

    private val _notificationList = MutableLiveData<List<Notification>>(emptyList())
    val notificationList: LiveData<List<Notification>> get() = _notificationList

    // 검색된 리스트
    private val _filteredList = MutableLiveData<List<Notification>>(emptyList())
    val filteredList: LiveData<List<Notification>> get() = _filteredList

    // 검색 결과 UI 상태 저장
    private val _isSearchResultVisible = MutableLiveData(false)
    val isSearchResultVisible: LiveData<Boolean> get() = _isSearchResultVisible

    private val _isViewAllVisible = MutableLiveData(false)
    val isViewAllVisible: LiveData<Boolean> get() = _isViewAllVisible

    // 검색 결과 개수
    private val _searchResultCount = MutableLiveData(0)
    val searchResultCount: LiveData<Int> get() = _searchResultCount

    init {
        loadNotifications() // 초기 데이터 불러오기
    }

    // 공지사항 데이터 불러오기
    private fun loadNotifications() {
        viewModelScope.launch {
            _notificationList.value = repository.getMySavedPosts()
            Log.d("notification","뷰모델에서 리스트 : ${_notificationList.value}")
            _filteredList.value = _notificationList.value // 기본적으로 필터된 리스트도 같은 데이터로 설정
        }
    }

    // 검색 수행
    fun performSearch(query: String) {
        _filteredList.value = if (query.isNotEmpty()) {
            _notificationList.value?.filter { it.title.contains(query, ignoreCase = true) } ?: emptyList()
        } else {
            _notificationList.value
        }

        _searchResultCount.value = _filteredList.value?.size ?: 0 // 검색된 개수 저장

        _isSearchResultVisible.value = _filteredList.value!!.isNotEmpty()
        _isViewAllVisible.value = _filteredList.value!!.isNotEmpty()
    }


    // 검색 초기화
    fun resetSearch() {
        _filteredList.value = _notificationList.value
        _searchResultCount.value = _notificationList.value?.size ?: 0

        _isSearchResultVisible.value = false
        _isViewAllVisible.value = false
    }
}
