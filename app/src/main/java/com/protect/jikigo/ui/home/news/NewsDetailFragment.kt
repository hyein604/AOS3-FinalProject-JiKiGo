package com.protect.jikigo.ui.home.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.protect.jikigo.databinding.FragmentNewsDetailBinding
import com.protect.jikigo.data.NewsItem
import java.text.SimpleDateFormat
import java.util.Locale
import com.protect.jikigo.R

class NewsDetailFragment : Fragment() {
    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    private var newsItem: NewsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newsItem = it.getParcelable("newsItem")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsItem?.let { news ->
            // 제목 설정
            binding.tvNewsDetailTitle.text = news.title

            // 기자 정보 및 날짜 설정
            val formattedDate = formatDate(news.pubDate)
            binding.tvNewsDetailReporter.text = "멋사뉴스  |  김혜인 기자  |  $formattedDate"

            // 뉴스 이미지 설정
            Glide.with(requireContext())
                .load(news.image ?: R.drawable.img_news_all_banner_2) // 기본 이미지 설정
                .into(binding.ivNewsDetailNewsImage)

            // 기사 내용 설정
            binding.tvNewsDetailContent.text = news.description
        }
    }

    // 날짜 포맷 변경
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
