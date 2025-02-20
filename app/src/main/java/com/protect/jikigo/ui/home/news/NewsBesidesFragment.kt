package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.databinding.FragmentNewsBesidesBinding
import com.protect.jikigo.ui.adapter.NewsBannerAdapter
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.protect.jikigo.data.NewsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.protect.jikigo.data.RetrofitClient
import com.protect.jikigo.data.NewsResponse
import com.protect.jikigo.ui.adapter.NewsAdapter
import com.protect.jikigo.ui.viewModel.NewsViewModel
import com.protect.jikigo.utils.cleanHtml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsBesidesFragment : Fragment() {
    private var _binding: FragmentNewsBesidesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels()

    private var newsCall: Call<NewsResponse>? = null
    private var category: String? = null

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsBannerAdapter: NewsBannerAdapter

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
        newsCall?.cancel() // API 요청 취소
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsAdapter()
        newsBannerAdapter = NewsBannerAdapter()
        setupRecyclerView()
        setupHomeBannerUI()

        // ViewModel 관찰하여 UI 업데이트
        viewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            newsAdapter.submitList(newsList as MutableList<NewsItem>?)

            // 이미지가 있는 뉴스 아이템만 필터링 후 랜덤으로 3개 선택
            val imageNewsList = newsList.filter { it.imageUrl != null }.take(3)
            newsBannerAdapter.submitList(imageNewsList as MutableList<NewsItem>?)

        }

        category?.let { viewModel.fetchNews(it) }
    }


    // 뉴스 목록 RecyclerView 설정
    private fun setupRecyclerView() {
        binding.rvNewsBesidesLatestNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }



    // 배너 UI 설정
    private fun setupHomeBannerUI() {
        with(binding) {
            vpNewsBesidesHotTopic.adapter = newsBannerAdapter
            vpNewsBesidesHotTopic.isUserInputEnabled = true // 스와이프 가능

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