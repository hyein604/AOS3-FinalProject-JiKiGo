package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.protect.jikigo.databinding.RowLatestNewsBinding
import com.protect.jikigo.data.NewsItem

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
            binding.tvNewsBesidesSource.text = "${newsItem.pubDate}"
//            binding.tvNewsBesidesSource.text = "${newsItem.pubDate} · ${newsItem.pubDate}"

//            // 썸네일 이미지 로드 (Glide 사용 예제)
//            Glide.with(binding.ivNewsBesidesThumbnail.context)
//                .load(newsItem.imageUrl)
//                .into(binding.ivNewsBesidesThumbnail)
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.link == newItem.link
        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem == newItem
    }
}
