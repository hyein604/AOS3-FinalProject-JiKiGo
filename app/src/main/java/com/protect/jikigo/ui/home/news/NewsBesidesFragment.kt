package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentNewsBesidesBinding
import com.protect.jikigo.ui.adapter.NewsBannerAdapter
import com.protect.jikigo.ui.adapter.OnBannerItemClickListener
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.protect.jikigo.data.RetrofitClient
import com.protect.jikigo.data.NewsResponse
import com.protect.jikigo.ui.adapter.NewsAdapter
import com.protect.jikigo.ui.home.noti.NotificationFragmentDirections
import com.protect.jikigo.utils.cleanHtml

class NewsBesidesFragment : Fragment() {
    private var _binding: FragmentNewsBesidesBinding? = null
    private val binding get() = _binding!! // ViewBinding 사용

    private var category: String? = null
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString(ARG_CATEGORY) // 프래그먼트에 전달된 카테고리 가져오기
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBesidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsAdapter() // ✅ 여기서 초기화
        setupRecyclerView()
        setupHomeBannerUI()

        category?.let { fetchNews(it) }
    }


    // 뉴스 목록 RecyclerView 설정
    private fun setupRecyclerView() {
        binding.rvNewsBesidesLatestNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    // 뉴스 API 호출 및 데이터 로드
    private fun fetchNews(query: String) {
        val call = RetrofitClient.instance.searchNews(query)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    response.body()?.items?.let { newsList ->
                        val cleanedNewsList = newsList.map { news ->
                            news.copy(
                                title = news.title.cleanHtml(), // HTML 태그 제거
                                description = news.description.cleanHtml()
                            )
                        }
                        newsAdapter.submitList(cleanedNewsList) // 데이터를 어댑터에 전달
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

    // 배너 어댑터 설정
    private val bannerAdapter: NewsBannerAdapter by lazy {
        NewsBannerAdapter(object : OnBannerItemClickListener {
            override fun onItemClick(banner: Int) {
                // 배너 클릭 이벤트 처리
            }
        })
    }

    // 배너 UI 설정
    private fun setupHomeBannerUI() {
        with(binding) {
            vpNewsBesidesHotTopic.adapter = bannerAdapter
            vpNewsBesidesHotTopic.isUserInputEnabled = true // 스와이프 가능

            val bannerImages = listOf(
                R.drawable.img_today_news_home_tmp_1,
                R.drawable.img_today_news_home_tmp_2,
                R.drawable.img_today_news_home_tmp_3
            )
            bannerAdapter.submitList(bannerImages)

            // TabLayout(인디케이터)과 ViewPager2(배너) 연결
            TabLayoutMediator(indicatorNewsBesidesHotTopic, vpNewsBesidesHotTopic) { _, _ -> }
                .attach()
        }
    }

    companion object {
        private const val ARG_CATEGORY = "category"

        // 카테고리를 받아 프래그먼트 인스턴스를 생성하는 함수
        fun newInstance(category: String) = NewsBesidesFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_CATEGORY, category)
            }
        }
    }
}