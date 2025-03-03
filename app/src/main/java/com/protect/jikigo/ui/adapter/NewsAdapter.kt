package com.protect.jikigo.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.protect.jikigo.data.model.NewsItem
import com.protect.jikigo.databinding.RowLatestNewsBinding
import java.text.SimpleDateFormat
import java.util.Locale
import org.jsoup.Jsoup

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

            when(newsItem.imageUrl){
                // 이미지가 없을경우
                null -> binding.ivNewsBesidesThumbnailLoading.visibility = View.GONE

                // 이미지가 있을경우
                else -> {
                    binding.ivNewsBesidesThumbnailLoading.visibility = View.GONE
                    Glide.with(binding.ivNewsBesidesThumbnail.context)
                        .load(newsItem.imageUrl)
                        .into(binding.ivNewsBesidesThumbnail)
                    binding.ivNewsBesidesThumbnail.visibility = View.VISIBLE
                }

            }
            if (newsItem.imageUrl != null) {
                // 로딩중일경
                // 이미지가 있을 경우
                Glide.with(binding.ivNewsBesidesThumbnail.context)
                    .load(newsItem.imageUrl)
                    .into(binding.ivNewsBesidesThumbnail)
                binding.ivNewsBesidesThumbnail.visibility = View.VISIBLE
            } else {
                // 이미지가 없을 경우
                binding.ivNewsBesidesThumbnail.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.link))
                context.startActivity(intent)
            }
        }


        private fun formatDate(pubDate: String): String {
            return try {
                val inputFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                val outputFormat = SimpleDateFormat("yyyy/M/d HH:mm", Locale.ENGLISH)
                val date = inputFormat.parse(pubDate)
                date?.let { outputFormat.format(it) } ?: pubDate
            } catch (e: Exception) {
                pubDate
            }
        }
    }

    private fun fetchNewsImageSync(url: String): String? {
        return try {
            val doc = Jsoup.connect(url).get()

            // 다양한 메타 태그에서 이미지 URL 가져오기
            var imageUrl = listOf(
                "meta[property=og:image]",
                "meta[name=twitter:image]",
                "meta[name=thumbnail]"
            ).mapNotNull { selector ->
                doc.select(selector).attr("content").takeIf { it.isNotEmpty() }
            }.firstOrNull() ?: ""

            // 본문 내 <img> 태그에서 이미지 가져오기
            if (imageUrl.isEmpty()) {
                imageUrl = doc.select("article img, figure img, div img, span img").firstOrNull()?.attr("src") ?: ""
            }

            // 상대 경로는 절대 경로로 변환
            if (imageUrl.startsWith("/")) {
                val baseUri = Uri.parse(url).scheme + "://" + Uri.parse(url).host
                imageUrl = baseUri + imageUrl
            }

            // HTTP → HTTPS 자동 변환
            imageUrl.takeIf { it.isNotEmpty() }?.replace("http://", "https://")
        } catch (e: Exception) {

            // 이미지 못 불러왔으면 null 반환
            null
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem == newItem
    }
}
