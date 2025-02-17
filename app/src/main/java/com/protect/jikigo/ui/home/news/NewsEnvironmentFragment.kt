package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.databinding.FragmentNewsEnvironmentBinding


class NewsEnvironmentFragment : Fragment() {
    private var _binding: FragmentNewsEnvironmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsEnvironmentBinding.inflate(inflater, container, false)
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
        binding.toolbarNewsEnvironment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewPagerWithTabs() {
        val adapter = NewsEnvironmentPagerAdapter(this)
        binding.vpNewsEnvironment.adapter = adapter
        binding.vpNewsEnvironment.offscreenPageLimit = NewsEnvironmentType.values().size

        TabLayoutMediator(binding.tabNewsEnvironment, binding.vpNewsEnvironment) { tab, position ->
            tab.text = NewsEnvironmentType.values()[position].getTodayNewsEnvironmentTabTitle()
        }.attach()
    }
}

class NewsEnvironmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = NewsEnvironmentType.values().size

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            NewsEnvironmentAllFragment() // "전체" 탭
        } else {
            NewsEnvironmentAirFragment() // 나머지 탭
        }
    }
}