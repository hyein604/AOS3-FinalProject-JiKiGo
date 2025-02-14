package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.Storage

import com.protect.jikigo.databinding.ItemCouponBoxListBinding

class CouponBoxAdapter(
    private val listener: CouponOnClickListener
): RecyclerView.Adapter<CouponBoxAdapter.CouponBoxViewHolder>() {

    private val items = Storage.coupon

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponBoxViewHolder {
        return CouponBoxViewHolder(
            ItemCouponBoxListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener = { position -> listener.onClickListener(items[position]) }
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CouponBoxViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class CouponBoxViewHolder(
        private val binding: ItemCouponBoxListBinding,
        private val listener: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { 
                listener(adapterPosition)
            }
        }
        fun bind(item: Coupon) {
            binding.apply {
                tvCouponListName.text = item.name
                tvCouponListDDay.text = "D-30"
                tvCouponListClient.text = item.brand
                tvCouponListDate.text = item.date
                Glide.with(root.context)
                    .load(item.image)
                    .into(ivCouponListImage)
            }
        }
    }
}

interface CouponOnClickListener {
    fun onClickListener(item: Coupon)
}