package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.protect.jikigo.databinding.FragmentNewsAllBinding
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.protect.jikigo.data.RetrofitClient
import com.protect.jikigo.data.NewsResponse
import com.protect.jikigo.R
import com.protect.jikigo.ui.adapter.NewsAllBannerAdapter

class NewsAllFragment : Fragment() {
    private var _binding: FragmentNewsAllBinding? = null
    private val binding get() = _binding!!

    private val bannerImages = listOf(
        R.drawable.img_news_all_banner_1,
        R.drawable.img_news_all_banner_2,
        R.drawable.img_news_all_banner_3
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뉴스 검색 실행
        fetchNews("환경오염")
        // 띠 배너
        setupHomeBannerUI()

    }

    private fun fetchNews(query: String) {
        val call = RetrofitClient.instance.searchNews(query)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.items?.forEach { news ->
                        Log.d("News", "${query}, 제목: ${news.title}, 링크: ${news.link}")
                    }
                } else {
                    Log.e("News", "API 호출 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("News", "네트워크 오류: ${t.message}")
            }
        })
    }

    // 배너화면 설정
    private fun setupHomeBannerUI() {
        val bannerAdapter = NewsAllBannerAdapter(bannerImages)
        binding.vpNewsAllBanner.adapter = bannerAdapter
    }

}