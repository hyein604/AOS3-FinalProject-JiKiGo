package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentNewsEnvironmentAirBinding
import com.protect.jikigo.ui.adapter.NewsBannerAdapter
import com.protect.jikigo.ui.adapter.OnBannerItemClickListener

class NewsEnvironmentAirFragment : Fragment() {

    private var _binding: FragmentNewsEnvironmentAirBinding? = null
    private val binding get() = _binding!!

    private val bannerAdapter: NewsBannerAdapter by lazy {
        NewsBannerAdapter(object : OnBannerItemClickListener {
            override fun onItemClick(banner: Int) {
                // 배너 클릭 이벤트 처리
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsEnvironmentAirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHomeBannerUI()
    }

    // 배너화면 설정
    private fun setupHomeBannerUI() {
        with(binding) {
            vpNewsEnvironmentAirHotTopic.adapter = bannerAdapter

            // 배너 이미지 추가 (drawable 폴더에 있는 이미지 3개)
            val bannerImages = listOf(
                R.drawable.img_today_news_home_tmp_1, // 첫 번째 이미지
                R.drawable.img_today_news_home_tmp_2, // 두 번째 이미지
                R.drawable.img_today_news_home_tmp_3  // 세 번째 이미지
            )
            bannerAdapter.submitList(bannerImages)

            // TabLayout(인디케이터)과 ViewPager2(배너) 연결
            TabLayoutMediator(indicatorNewsEnvironmentAirHotTopic, vpNewsEnvironmentAirHotTopic) { _, _ -> }
                .attach()
        }
    }
}
