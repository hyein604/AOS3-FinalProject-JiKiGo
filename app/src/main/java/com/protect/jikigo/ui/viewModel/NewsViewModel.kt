package com.protect.jikigo.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.NewsItem
import com.protect.jikigo.data.RetrofitClient
import com.protect.jikigo.utils.cleanHtml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class NewsViewModel : ViewModel() {

    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> get() = _newsList

    fun fetchNews(query: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) { RetrofitClient.instance.searchNews(query) }
                if (response.isSuccessful) {
                    response.body()?.items?.let { newsList ->
                        // title과 description 먼저 보여주기
                        val initialNewsList = newsList.map { newsItem ->
                            newsItem.copy(
                                title = newsItem.title.cleanHtml(),
                                description = newsItem.description.cleanHtml(),
                                imageUrl = null // 초기에는 이미지 없음
                            )
                        }
                        _newsList.postValue(initialNewsList)

                        // 개별적으로 imageUrl 크롤링 후 업데이트
                        initialNewsList.forEachIndexed { index, newsItem ->
                            viewModelScope.launch(Dispatchers.IO) {
                                val imageUrl = fetchNewsImageAsync(newsItem.link)
                                if (imageUrl != null) {
                                    val updatedNewsItem = newsItem.copy(imageUrl = imageUrl)

                                    withContext(Dispatchers.Main) {
                                        // 기존 리스트에서 해당 아이템만 교체하여 업데이트
                                        _newsList.value = _newsList.value?.toMutableList()?.apply {
                                            set(index, updatedNewsItem)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.e("News", "API 호출 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("News", "네트워크 오류: ${e.message}")
            }
        }
    }

    private suspend fun fetchNewsImageAsync(url: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val doc = Jsoup.connect(url).get()
                val imageUrl = doc.select("meta[property=og:image]").attr("content")
                imageUrl.takeIf { it.startsWith("http") }?.replace("http://", "https://")
            } catch (e: Exception) {
                null
            }
        }
    }
}
