package com.protect.jikigo.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.R
import com.protect.jikigo.data.NewsItem
import com.protect.jikigo.databinding.ItemNewsBannerBinding
import org.jsoup.Jsoup
import java.util.concurrent.Executors

class NewsBannerAdapter : ListAdapter<NewsItem, NewsBannerAdapter.NewsBannerViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsBannerViewHolder {
        val binding = ItemNewsBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsBannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsBannerViewHolder(private val binding: ItemNewsBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            binding.tvNewsBannerTitle.text = newsItem.title // 뉴스 제목

            // Open Graph에서 뉴스 이미지 가져오기
            fetchNewsImage(newsItem.link) { imageUrl ->
                Glide.with(binding.ivNewsBannerImage.context)
                    .load(imageUrl ?: R.drawable.background_news_no_picture) // 기본 이미지 대체 가능
                    .into(binding.ivNewsBannerImage)
            }

            // 클릭 시 웹 브라우저에서 뉴스 링크 열기
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.link))
                context.startActivity(intent)
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
    }
}

// 리스트 비교를 위한 DiffUtil 설정 (효율적인 업데이트를 위해 사용)
class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.link == newItem.link
    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem == newItem
}
