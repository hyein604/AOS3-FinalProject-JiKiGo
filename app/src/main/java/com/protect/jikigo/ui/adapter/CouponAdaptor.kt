package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.databinding.ItemCouponBinding
import com.protect.jikigo.ui.extensions.applyNumberFormat

class CouponAdaptor (
    private val items: List<Coupon>,
    private val listener: TravelCouponOnClickListener
) : RecyclerView.Adapter<CouponAdaptor.CouponViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        return CouponViewHolder(
            ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener = { position -> listener.onClickListener(items[position]) }
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class CouponViewHolder(
        private val binding: ItemCouponBinding,
        private val listener: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener(adapterPosition)
            }
        }
        fun bind(item: Coupon) {
            binding.apply {
                tvCouponName.text = item.name
                tvCouponBrand.text = item.brand
                tvCouponPrice.applyNumberFormat(item.price)
                Glide.with(root.context)
                    .load(item.image)
                    .into(ivCouponDetailThumnail)
            }
        }
    }
}
interface TravelCouponOnClickListener {
    fun onClickListener(item: Coupon)
}