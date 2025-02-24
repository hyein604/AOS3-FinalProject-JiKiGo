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

import com.protect.jikigo.databinding.ItemCouponBoxListBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

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
                showDaysUntilExpiration(2025,3,2)
                tvCouponListClient.text = item.brand
                tvCouponListDate.text = item.date
                Glide.with(root.context)
                    .load(item.image)
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
    fun onClickListener(item: Coupon)
}