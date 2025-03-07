package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.protect.jikigo.R
import com.protect.jikigo.data.model.UserPaymentHistory
import com.protect.jikigo.databinding.ItemPointHistoryListBinding
import com.protect.jikigo.utils.applyMinusNumberFormat
import com.protect.jikigo.utils.applyNumberFormat
import com.protect.jikigo.utils.applySpannableStyles

class PointHistoryAdapter: RecyclerView.Adapter<PointHistoryAdapter.PointHistoryViewHolder>() {

    private var items = mutableListOf<UserPaymentHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHistoryViewHolder {
        return PointHistoryViewHolder(ItemPointHistoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(item: MutableList<UserPaymentHistory>) {
        items.clear()
        items.addAll(item)
        notifyDataSetChanged()
    }

    class PointHistoryViewHolder(
        private val binding: ItemPointHistoryListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserPaymentHistory) {
            binding.apply {
                when(item.payType) {
                    "사용" -> {
                        tvPointHistoryListPoint.applyMinusNumberFormat(item.amount)
                    }

                    else -> {
                        tvPointHistoryListPoint.applyNumberFormat(item.amount)
                        tvPointHistoryListPoint.applySpannableStyles(
                            0,
                            item.amount.toString().length + 1,
                            R.color.positive
                        )
                    }
                }
                tvPointHistoryListPointDescription.text = item.reason
                tvPointHistoryListPointDate.text = item.paymentDate.substring(startIndex = 11, endIndex = 16)
            }
        }
    }
}