package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.R
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
                showDaysUntilExpiration(item.purchasedCouponValidDays.substring(0, 4).toInt(),item.purchasedCouponValidDays.substring(5, 7).toInt(),item.purchasedCouponValidDays.substring(8, 10).toInt())
                tvCouponListClient.text = item.purchasedCouponBrand
                Glide.with(root.context)
                    .load(item.purchasedCouponImage)
                    .into(ivCouponListImage)
                when (item.purchasedCouponStatus) {
                    0 -> {
                        tvCouponListDate.text = "${item.purchasedCouponValidDays} 까지"
                        viewCouponBlur.isVisible = false
                        tvCouponListDDay.setBackgroundColor(root.context.getColor(R.color.primary))
                    }
                    1 -> {
                        tvCouponListDate.text = "${item.purchasedCouponUsedDate}"
                        viewCouponBlur.isVisible = true
                        tvCouponListDDay.setBackgroundColor(root.context.getColor(R.color.gray_10))
                        tvCouponListDDay.text = "사용 완료"
                    }
                    2 -> {
                        tvCouponListDate.text = "${item.purchasedCouponValidDays}"
                        viewCouponBlur.isVisible = true
                        tvCouponListDDay.setBackgroundColor(root.context.getColor(R.color.gray_10))
                        tvCouponListDDay.text = "만료"
                    }
                }
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
            val dDayText = if (daysLeft > 0) {
                "D-$daysLeft"
            } else {
                "사용 완료"
            }

            binding.tvCouponListDDay.text = dDayText
        }

    }
}

interface CouponOnClickListener {
    fun onClickListener(item: PurchasedCoupon)
}