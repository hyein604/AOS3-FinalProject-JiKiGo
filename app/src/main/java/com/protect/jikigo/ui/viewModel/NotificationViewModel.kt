package com.protect.jikigo.ui.viewModel

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

    private val _notificationListNotificationFragment = MutableLiveData<List<Notification>>(emptyList())
    val notificationListNotificationFragment: LiveData<List<Notification>> get() = _notificationListNotificationFragment

    private val _notificationListHomeFragment = MutableLiveData<List<Notification>>(emptyList())
    val notificationListHomeFragment: LiveData<List<Notification>> get() = _notificationListHomeFragment


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

    private val _hasSearched = MutableLiveData(false)
    val hasSearched: LiveData<Boolean> get() = _hasSearched

    init {
        loadNotifications() // 초기 데이터 불러오기
    }

    // 공지사항 데이터 불러오기
    private fun loadNotifications() {
        viewModelScope.launch {
            val notifications = repository.getMySavedPosts()

            _notificationListHomeFragment.value = notifications

            _notificationListNotificationFragment.value = notifications.sortedByDescending { it.important } // 중요 공지 최상단
            _filteredList.value = _notificationListNotificationFragment.value
        }
    }
    // 검색 수행
    fun performSearch(query: String) {
        if (query.isEmpty()) {
            _filteredList.value = _notificationListNotificationFragment.value
            _searchResultCount.value = _notificationListNotificationFragment.value?.size ?: 0
            _isSearchResultVisible.value = false
            _isViewAllVisible.value = false
            _hasSearched.value = false // 검색하지 않은 상태
            return
        }

        _hasSearched.value = true // 검색 수행됨

        _filteredList.value = _notificationListHomeFragment.value?.filter {
            it.title.contains(query, ignoreCase = true)
        }?.sortedByDescending { it.important } ?: emptyList()

        val resultCount = _filteredList.value?.size ?: 0
        _searchResultCount.value = resultCount

        _isSearchResultVisible.value = resultCount > 0
        _isViewAllVisible.value = resultCount > 0
    }


    // 검색 초기화
    fun resetSearch() {
        _filteredList.value = _notificationListNotificationFragment.value
        _searchResultCount.value = _notificationListNotificationFragment.value?.size ?: 0

        _isSearchResultVisible.value = false
        _isViewAllVisible.value = false
    }
}
