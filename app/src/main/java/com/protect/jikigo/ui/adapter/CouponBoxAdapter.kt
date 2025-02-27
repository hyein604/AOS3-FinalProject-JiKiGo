package com.protect.jikigo.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.R
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.Storage
import com.protect.jikigo.data.model.PurchasedCoupon

import com.protect.jikigo.databinding.ItemCouponBoxListBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class CouponBoxAdapter(
    private val listener: CouponOnClickListener
): RecyclerView.Adapter<CouponBoxAdapter.CouponBoxViewHolder>() {

    private val items = mutableListOf<PurchasedCoupon>()

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

    fun submitList(item: MutableList<PurchasedCoupon>) {
        items.clear()
        items.addAll(item)
        notifyDataSetChanged()
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
        fun bind(item: PurchasedCoupon) {
            binding.apply {
                tvCouponListName.text = item.purchasedCouponName
                showDaysUntilExpiration(item.purchasedCouponValidDays.substring(0, 5).toInt(),item.purchasedCouponValidDays.substring(6, 8).toInt(),item.purchasedCouponValidDays.substring(9, 11).toInt())
                tvCouponListClient.text = item.purchasedCouponBrand
                tvCouponListDate.text = "${item.purchasedCouponValidDays} 까지"
                Glide.with(root.context)
                    .load(item.purchasedCouponImage)
                    .into(ivCouponListImage)
            }
        }

        private fun showDaysUntilExpiration(expirationYear: Int, expirationMonth: Int, expirationDay: Int) {
            val today = Calendar.getInstance()
            val expirationDate = Calendar.getInstance().apply {
                set(expirationYear, expirationMonth - 1, expirationDay) // month는 0부터 시작
            }

            val diffInMillis = expirationDate.timeInMillis - today.timeInMillis
            val daysLeft = TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()

            // D-day 형식으로 표시
            val dDayText = if (daysLeft >= 0) {
                "D-$daysLeft"
            } else {
                binding.viewCouponBlur.isVisible = true
                binding.tvCouponListDDay.setBackgroundColor(Color.LTGRAY)
                "사용 완료"
            }

            binding.tvCouponListDDay.text = dDayText
        }

    }
}

interface CouponOnClickListener {
    fun onClickListener(item: PurchasedCoupon)
}