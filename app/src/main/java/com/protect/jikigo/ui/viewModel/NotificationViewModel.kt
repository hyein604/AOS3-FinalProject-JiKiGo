package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.protect.jikigo.data.model.Notification
import com.protect.jikigo.data.repo.NotificationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepo
) : ViewModel() {
    // 공지사항 리스트
    private val notificationList = repository.getMySavedPosts()

    // 검색된 리스트
    private val _filteredList = MutableLiveData(notificationList)
    val filteredList: LiveData<List<Notification>> get() = _filteredList

    // 검색 결과 UI 상태 저장
    private val _isSearchResultVisible = MutableLiveData(false)
    val isSearchResultVisible: LiveData<Boolean> get() = _isSearchResultVisible

    private val _isViewAllVisible = MutableLiveData(false)
    val isViewAllVisible: LiveData<Boolean> get() = _isViewAllVisible

    // 검색 수행
    fun performSearch(query: String) {
        _filteredList.value = if (query.isNotEmpty()) {
            notificationList.filter { it.title.contains(query, ignoreCase = true) }
        } else {
            notificationList
        }

        _isSearchResultVisible.value = _filteredList.value!!.isNotEmpty()
        _isViewAllVisible.value = _filteredList.value!!.isNotEmpty()
    }

    // 검색 초기화
    fun resetSearch() {
        _filteredList.value = notificationList
        _isSearchResultVisible.value = false
        _isViewAllVisible.value = false
    }
}
