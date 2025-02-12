package com.protect.jikigo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewsDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsEnvironmentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewsDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewsEnvironmentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
//package com.protect.jikigo
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.lifecycleScope
//import androidx.viewpager2.adapter.FragmentStateAdapter
//import androidx.viewpager2.widget.ViewPager2
//import com.google.android.material.tabs.TabLayoutMediator
//import com.protect.jikigo.databinding.FragmentNewsEnvironmentBinding
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//class NewsEnvironmentFragment : Fragment() {
//    private var _binding: FragmentNewsEnvironmentBinding? = null
//    private val binding get() = _binding!!
//
//    private var todayNewsCategoryType: NewsEnvironmentType? = null
//    private var currentPosition: Int = 0  // 현재 탭의 포지션을 저장
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // 전달받은 TodayNewsType을 초기화
//        todayNewsCategoryType = NewsEnvironmentType.valueOf(
//            arguments?.getString(KEY_TODAY_NEWS_TYPE) ?: NewsEnvironmentType.ALL.name
//        )
//
//        // 이전에 저장된 탭 상태가 있다면 상태값 가져오기
//        savedInstanceState?.let {
//            currentPosition = it.getInt(KEY_CURRENT_POSITION, 0)
//        }
//    }
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentNewsEnvironmentBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupListeners()
//        setupTabs()
//        setupViewPager()
//    }
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    // 리스너 함수 모음
//    private fun setupListeners() {
//        with(binding) {
//            toolbarNewsEnvorionment.setNavigationOnClickListener {
//                // findNavController().navigateUp()
//            }
//        }
//    }
//
//
//    // 탭설정
//    private fun setupTabs() {
//        // TodayNewsType의 항목을 탭으로 추가
//        NewsEnvironmentType.entries.forEach { type ->
//            binding.tabNewsEnvorionment.addTab(binding.tabNewsEnvorionment.newTab().setText(type.getTodayNewsTabTitle()))
//        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            delay(DELAY_TIME) // 초기 탭 선택 시 자연스러운 애니메이션을 위해 딜레이를 준다.
//            // 뷰가 파괴되지 않았는지 확인
//            if (_binding != null) {
//                binding.tabNewsEnvorionment.selectTab(binding.tabNewsEnvorionment.getTabAt(currentPosition), true)
//            }
//        }
//    }
//
//
//    // 뷰페이저 설정
//    private fun setupViewPager() {
//        val adapter = NewsEnvironmentPagerAdapter(this)
//        binding.vpNewsEnvorionment.adapter = adapter
//
//        // TabLayout과 ViewPager2 연결
//        TabLayoutMediator(binding.tabNewsEnvorionment, binding.vpNewsEnvorionment) { tab, position ->
//            tab.text = NewsEnvironmentType.entries[position].getTodayNewsTabTitle()
//        }.attach()
//
//        // 탭 페이지가 바뀔 때마다 currentPosition을 업데이트
//        binding.vpNewsEnvorionment.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                currentPosition = position
//            }
//        })
//    }
//
//
//    companion object {
//        private const val KEY_TODAY_NEWS_TYPE = "today_news_type"
//        private const val KEY_CURRENT_POSITION = "current_position"
//        private const val DELAY_TIME = 100L
//
//        fun newInstance(type: NewsEnvironmentType): NewsEnvironmentFragment {
//            return NewsEnvironmentFragment().apply {
//                arguments = Bundle().apply {
//                    putString(KEY_TODAY_NEWS_TYPE, type.name)
//                }
//            }
//        }
//    }
//}
//
//
//// ViewPager2의 어댑터
//class NewsEnvironmentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
//
//    override fun getItemCount(): Int = NewsEnvironmentType.entries.size
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            NewsEnvironmentType.ALL.ordinal -> NewsEnvironmentAllFragment()
//            NewsEnvironmentType.AIR.ordinal -> NewsEnvironmentAirFragment()
//            else -> {
//                NewsEnvironmentAllFragment()
//            }
//        }
//    }
//}