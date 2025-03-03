// API 요청을 보낼 인터페이스
package com.protect.jikigo.data

import com.protect.jikigo.BuildConfig
import com.protect.jikigo.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverNewsApi {
    @Headers(
        BuildConfig.NAVER_CLIENT_NEWS_ID,
        BuildConfig.NAVER_CLIENT_NEWS_SECRET,
    )
    @GET("v1/search/news.json")
    suspend fun searchNews(
        @Query("query") query: String,
        @Query("display") display: Int = 20,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "sim"
    ): Response<NewsResponse>
}
