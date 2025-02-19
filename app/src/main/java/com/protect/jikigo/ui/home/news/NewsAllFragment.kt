package com.protect.jikigo.ui.home.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.protect.jikigo.databinding.FragmentNewsAllBinding
import android.util.Log
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.protect.jikigo.data.RetrofitClient
import com.protect.jikigo.data.NewsResponse
import com.protect.jikigo.R
import com.protect.jikigo.data.NewsItem
import com.protect.jikigo.ui.adapter.NewsAllBannerAdapter
import com.protect.jikigo.utils.cleanHtml
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executors

class NewsAllFragment : Fragment() {
    private var _binding: FragmentNewsAllBinding? = null
    private val binding get() = _binding!!

    private val bannerImages = listOf(
        R.drawable.img_news_all_banner_1,
        R.drawable.img_news_all_banner_2,
        R.drawable.img_news_all_banner_3
    )

    // 배너 자동 넘기기에 필요한 요소들
    private val handler = Handler(Looper.getMainLooper())
    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            binding?.let {
                val nextItem = (it.vpNewsAllBanner.currentItem + 1) % bannerImages.size
                Log.d("adapter", "currentItem Index : ${it.vpNewsAllBanner.currentItem}")
                it.vpNewsAllBanner.setCurrentItem(nextItem, true)
            }
        }
    }

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
        handler.removeCallbacks(autoSlideRunnable)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뉴스 검색 실행
        fetchNews("환경오염")
        // 띠 배너
        setupHomeBannerUI()

    }

    // 웹사이트의 Open Graph 태그에서 이미지 URL 가져오기
    private fun fetchNewsImage(url: String, callback: (String?) -> Unit) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                val doc = Jsoup.connect(url).get()
                val imageUrl = doc.select("meta[property=og:image]").attr("content")
                val finalImageUrl = if (imageUrl.startsWith("http://")) {
                    // HTTP 프로토콜이 있을 경우 HTTPS로 변경
                    imageUrl.replace("http://", "https://")
                } else {
                    imageUrl
                }

                // UI 스레드에서 실행하도록 변경
                binding.root.post {
                    callback(finalImageUrl.ifEmpty { null })
                }
            } catch (e: Exception) {
                binding.root.post {
                    callback(null)
                }
            }
        }
    }

    // 뉴스 날짜 포맷을 변경하는 함수
    private fun formatDate(pubDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            val outputFormat = SimpleDateFormat("yyyy/M/d HH:mm", Locale.ENGLISH)
            val date = inputFormat.parse(pubDate)
            date?.let { outputFormat.format(it) } ?: pubDate
        } catch (e: Exception) {
            pubDate // 변환 실패 시 원본 반환
        }
    }

    // 뉴스 링크를 열기 위한 함수
    private fun openNewsLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun fetchNews(query: String) {
        val call = RetrofitClient.instance.searchNews(query)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.items?.let { newsList ->
                        val filteredNews = mutableListOf<NewsItem>()

                        newsList.forEach { newsItem ->
                            fetchNewsImage(newsItem.link) { imageUrl ->
                                if (imageUrl != null) {
                                    filteredNews.add(newsItem.copy(imageUrl = imageUrl))
                                }
                                if (filteredNews.size == 3) {
                                    updateUI(filteredNews)
                                }
                            }
                        }
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

    private fun updateUI(newsList: List<NewsItem>) {
        if (newsList.size >= 3) {
            // 첫 번째 뉴스
            binding.tvNewsAllFirstTitle.text = newsList[0].title.cleanHtml()
            binding.tvNewsAllFirstDate.text = formatDate(newsList[0].pubDate)
            Glide.with(binding.ivContentNewsAllFirstImage.context)
                .load(newsList[0].imageUrl)
                .into(binding.ivContentNewsAllFirstImage)
            binding.ivContentNewsAllFirstImage.setOnClickListener {
                openNewsLink(newsList[0].link)
            }

            // 두 번째 뉴스
            binding.tvNewsAllSecondTitle.text = newsList[1].title.cleanHtml()
            binding.tvNewsAllSecondDate.text = formatDate(newsList[1].pubDate)
            Glide.with(binding.ivContentNewsAllSecondImage.context)
                .load(newsList[1].imageUrl)
                .into(binding.ivContentNewsAllSecondImage)
            binding.ivContentNewsAllSecondImage.setOnClickListener {
                openNewsLink(newsList[1].link)
            }

            // 세 번째 뉴스
            binding.tvNewsAllThirdTitle.text = newsList[2].title.cleanHtml()
            binding.tvNewsAllThirdDate.text = formatDate(newsList[2].pubDate)
            Glide.with(binding.ivContentNewsAllThirdImage.context)
                .load(newsList[2].imageUrl)
                .into(binding.ivContentNewsAllThirdImage)
            binding.ivContentNewsAllThirdImage.setOnClickListener {
                openNewsLink(newsList[2].link)
            }
        }
    }

    // 배너화면 설정
    private fun setupHomeBannerUI() {
        val bannerAdapter = NewsAllBannerAdapter(bannerImages)
        binding.vpNewsAllBanner.adapter = bannerAdapter
        // 자동 슬라이드 시작
        handler.postDelayed(autoSlideRunnable, 1500)

        // 사용자가 직접 터치하면 자동 슬라이드 멈추기
        binding.vpNewsAllBanner.registerOnPageChangeCallback(object :
            androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING) {
                    handler.removeCallbacks(autoSlideRunnable) // 터치 시 자동 슬라이드 멈춤
                } else if (state == androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE) {
                    handler.postDelayed(autoSlideRunnable, 1500) // 다시 자동 슬라이드 시작
                }
            }
        })
    }

    // 메모리 누수 방지를 위해 onDestroy에서 handler 해제
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(autoSlideRunnable)
    }
}