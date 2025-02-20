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
                        val filteredNews = withContext(Dispatchers.IO) {
                            newsList.map { newsItem ->
                                async { newsItem.copy(
                                    imageUrl = fetchNewsImageAsync(newsItem.link),
                                    title = newsItem.title.cleanHtml(),
                                    description = newsItem.description.cleanHtml()) }
                            }.awaitAll().filter { it.imageUrl != null }
                        }
                        _newsList.postValue(filteredNews)
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
