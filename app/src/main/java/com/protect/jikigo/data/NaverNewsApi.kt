// API 요청을 보낼 인터페이스
package com.protect.jikigo.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverNewsApi {
    @Headers(
        "X-Naver-Client-Id: yXZg8mBN2ukoa_aZntkc",
        "X-Naver-Client-Secret: QhDrY7BUMm"
    )
    @GET("v1/search/news.json")
    fun searchNews(
        @Query("query") query: String,
        @Query("display") display: Int = 30,
        @Query("start") start: Int = 1,
        @Query("sort") sort: String = "date"
    ): Call<NewsResponse>
}
