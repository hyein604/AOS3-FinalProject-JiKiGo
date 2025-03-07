package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.protect.jikigo.databinding.ItemNewsAllBannerBinding

class NewsAllBannerAdapter(private val bannerImages: List<Int>) :
    RecyclerView.Adapter<NewsAllBannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(private val binding: ItemNewsAllBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.ivNewsAllBanner.setImageResource(imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemNewsAllBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerImages[position])
    }

    override fun getItemCount(): Int = bannerImages.size
}
