package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.databinding.ItemTravelBannerBinding

class TravelBannerAdapter (
    private val images: List<Int>,
    private val onBannerClickListener: (Int) -> Unit
) : RecyclerView.Adapter<TravelBannerAdapter.BannerViewHolder>() {

    private val infiniteImages = listOf(images.last()) + images + listOf(images.first())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemTravelBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val actualPosition = position % images.size
        Glide.with(holder.itemView.context)
            .load(infiniteImages[actualPosition])
            .into(holder.binding.ivTravelBanner)

        holder.itemView.setOnClickListener {
            onBannerClickListener(actualPosition)
        }
    }

    override fun getItemCount(): Int = infiniteImages.size

    class BannerViewHolder(val binding: ItemTravelBannerBinding) : RecyclerView.ViewHolder(binding.root)
}