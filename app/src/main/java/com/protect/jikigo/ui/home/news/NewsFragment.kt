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
    }

    private fun onClickToolbar() {
        binding.toolbarNews.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewPagerWithTabs() {
        val adapter = NewsEnvironmentPagerAdapter(this)
        binding.vpNews.adapter = adapter
        // 뷰페이저가 한 번에 여러 개의 페이지를 캐싱하도록 offscreenPageLimit을 설정하면 더 안정적으로 동작할 수 있다.
        binding.vpNews.offscreenPageLimit = NewsType.values().size

        TabLayoutMediator(binding.tabNews, binding.vpNews) { tab, position ->
            tab.text = NewsType.values()[position].getTodayNewsEnvironmentTabTitle()
        }.attach()
    }
}

class NewsEnvironmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NewsType.values().size

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            NewsAllFragment() // "전체" 탭
        } else {
            NewsBesidesFragment() // 나머지 탭
        }
    }
}