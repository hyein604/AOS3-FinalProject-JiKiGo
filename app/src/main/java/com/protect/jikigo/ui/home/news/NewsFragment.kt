package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.databinding.FragmentNewsBinding
import com.protect.jikigo.R

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickToolbar()
        setupBigCategory()
    }

    private fun onClickToolbar() {
        binding.toolbarNews.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupBigCategory() {
        val bigCategoryTitle = arguments?.let { NewsFragmentArgs.fromBundle(it).categoryTitle }
        binding.tvNewsBigCategoryTitle.text = bigCategoryTitle

        val imageRes = when (bigCategoryTitle) {
            "여행" -> R.drawable.img_travel
            "건강" -> R.drawable.img_health
            else -> R.drawable.img_environment // 기본값
        }
        binding.ivNewsEnvorionmentBigCategoryIcon.setImageResource(imageRes)

        // bigCategoryTitle 값에 따라 뉴스 검색어 설정
        val categories = when (bigCategoryTitle) {
            "여행" -> listOf("국내 여행", "추천 국내 여행지", "추천 국내 숙소", "추천 국내 맛집", "지역 축제 행사")
            "건강" -> listOf("건강 상식", "건강 운동", "식품 건강 정보")
            else -> listOf("환경 오염", "대기오염 대기환경", "해양 오염 강 오염", "생태계 오염", "환경 오염 정책")
        }

        // ViewPager와 TabLayout 설정 (categories를 전달)
        setupViewPagerWithTabs(categories)
    }

    private fun setupViewPagerWithTabs(categories: List<String>) {
        val bigCategoryTitle = arguments?.let { NewsFragmentArgs.fromBundle(it).categoryTitle }
        val smallCategoryTitle = NewsType.fromCategoryTitle(bigCategoryTitle) // bigCategoryTitle에 맞는 카테고리 가져오기

        val adapter = NewsEnvironmentPagerAdapter(this, categories)
        binding.vpNews.adapter = adapter
        // 뷰페이저가 한 번에 여러 개의 페이지를 캐싱하도록 offscreenPageLimit을 설정하면 더 안정적으로 동작할 수 있다.
        binding.vpNews.offscreenPageLimit = NewsType.values().size

        // 스와이프 기능 비활성화
        binding.vpNews.isUserInputEnabled = false

        TabLayoutMediator(binding.tabNews, binding.vpNews) { tab, position ->
            tab.text = smallCategoryTitle[position].getTodayNewsEnvironmentTabTitle()
        }.attach()
    }
}

class NewsEnvironmentPagerAdapter(fragment: Fragment, private val categories: List<String>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            NewsAllFragment.newInstance(categories[position]) // "전체" 또는 첫 번째 탭
        } else {
            NewsBesidesFragment.newInstance(categories[position])
        }
    }
}