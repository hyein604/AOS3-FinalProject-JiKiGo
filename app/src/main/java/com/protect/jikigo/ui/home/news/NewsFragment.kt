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

import android.widget.ImageView
import androidx.core.view.updateLayoutParams



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
        setupViewPagerWithTabs()
        setupBigCategory()
    }

    private fun onClickToolbar() {
        binding.toolbarNews.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupBigCategory() {
        // Safe Args를 사용하여 전달받은 categoryTitle 적용
        val bigCategoryTitle = arguments?.let { NewsFragmentArgs.fromBundle(it).categoryTitle }
        binding.tvNewsBigCategoryTitle.text = bigCategoryTitle

        // 카테고리에 따라 아이콘 변경
        val imageRes = when (bigCategoryTitle) {
            "여행" -> R.drawable.img_travel
            "건강" -> R.drawable.img_health
            else -> R.drawable.img_environment // 기본값
        }
        binding.ivNewsEnvorionmentBigCategoryIcon.setImageResource(imageRes)

    }



    private fun setupViewPagerWithTabs() {
        val adapter = NewsEnvironmentPagerAdapter(this)
        binding.vpNews.adapter = adapter
        // 뷰페이저가 한 번에 여러 개의 페이지를 캐싱하도록 offscreenPageLimit을 설정하면 더 안정적으로 동작할 수 있다.
        binding.vpNews.offscreenPageLimit = NewsType.values().size

        // 스와이프 기능 비활성화
        binding.vpNews.isUserInputEnabled = false

        TabLayoutMediator(binding.tabNews, binding.vpNews) { tab, position ->
            tab.text = NewsType.values()[position].getTodayNewsEnvironmentTabTitle()
        }.attach()
    }
}

class NewsEnvironmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val categories = listOf(
        "전체", "대기오염 대기환경", "해양 오염 강 오염", "생태계 오염", "환경 오염 정책"
    )

    override fun getItemCount(): Int = NewsType.values().size

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            NewsAllFragment() // "전체" 탭
        } else {
            NewsBesidesFragment.newInstance(categories[position])
        }
    }
}