package com.protect.jikigo.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.data.NewsItem
import com.protect.jikigo.databinding.ItemNewsBannerBinding


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
            binding.tvNewsBannerTitle.text = newsItem.title

            Glide.with(binding.ivNewsBannerImage.context)
                .load(newsItem.imageUrl)
                .into(binding.ivNewsBannerImage)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.link))
                context.startActivity(intent)
            }
        }
    }
}

// 리스트 비교를 위한 DiffUtil 설정 (효율적인 업데이트를 위해 사용)
class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.link == newItem.link
    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem == newItem
}
