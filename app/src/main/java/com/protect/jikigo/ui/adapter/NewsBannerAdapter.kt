package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.protect.jikigo.databinding.ItemNewsBannerBinding

class NewsBannerAdapter(
    private val listener: OnBannerItemClickListener,
) : RecyclerView.Adapter<NewsBannerAdapter.NewsBannerViewHolder>() {
    private val items = mutableListOf<Int>() // Int 리스트로 변경

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsBannerViewHolder {
        return NewsBannerViewHolder(
            ItemNewsBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) { listener.onItemClick(items[it]) }
    }

    override fun onBindViewHolder(holder: NewsBannerViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun submitList(bannerItems: List<Int>) { // Int 리스트 받도록 수정
        items.clear()
        items.addAll(bannerItems)
        notifyDataSetChanged()
    }

    class NewsBannerViewHolder(
        private val binding: ItemNewsBannerBinding,
        onItemClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(imageResId: Int) {
            binding.ivNewsBannerImage.setImageResource(imageResId) // 이미지 설정
        }
    }
}

interface OnBannerItemClickListener {
    fun onItemClick(banner: Int)
}
