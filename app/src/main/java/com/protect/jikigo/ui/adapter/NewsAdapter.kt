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
            binding.tvNewsBesidesTitle.text = newsItem.title
            binding.tvNewsBesidesContent.text = newsItem.description
            binding.tvNewsBesidesSource.text = formatDate(newsItem.pubDate)

            // Open Graph에서 이미지 가져오기
            fetchNewsImage(newsItem.link) { imageUrl ->
                Glide.with(binding.ivNewsBesidesThumbnail.context)
                    .load(imageUrl ?: R.drawable.img_news_all_banner_2) // 기본 이미지 대체 가능
                    .into(binding.ivNewsBesidesThumbnail)
            }
        }

        private fun fetchNewsImage(url: String, callback: (String?) -> Unit) {
            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                try {
                    val doc = Jsoup.connect(url).get()
                    val imageUrl = doc.select("meta[property=og:image]").attr("content")
                    val finalImageUrl = imageUrl.ifEmpty { null }

                    // UI 스레드에서 실행하도록 변경
                    binding.root.post {
                        callback(finalImageUrl)
                    }
                } catch (e: Exception) {
                    binding.root.post {
                        callback(null)
                    }
                }
            }
        }


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

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem == newItem
    }
}
