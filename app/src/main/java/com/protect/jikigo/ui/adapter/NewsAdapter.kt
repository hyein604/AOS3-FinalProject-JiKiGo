package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.protect.jikigo.databinding.RowLatestNewsBinding
import com.protect.jikigo.data.NewsItem
import java.text.SimpleDateFormat
import java.util.Locale
import com.protect.jikigo.R
import org.jsoup.Jsoup
import java.util.concurrent.Executors

class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = RowLatestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsViewHolder(private val binding: RowLatestNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            binding.tvNewsBesidesTitle.text = newsItem.title // 뉴스 제목
            binding.tvNewsBesidesContent.text = newsItem.description // 뉴스 내용
            binding.tvNewsBesidesSource.text = formatDate(newsItem.pubDate) // 날짜 포맷 변경

            // Open Graph에서 뉴스 이미지 가져오기
            fetchNewsImage(newsItem.link) { imageUrl ->
                Glide.with(binding.ivNewsBesidesThumbnail.context)
                    .load(imageUrl ?: R.drawable.img_news_all_banner_2) // 기본 이미지 대체 가능
                    .into(binding.ivNewsBesidesThumbnail)
            }
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
    }

    // 리스트 비교를 위한 DiffUtil 설정 (효율적인 업데이트를 위해 사용)
    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem == newItem
    }
}