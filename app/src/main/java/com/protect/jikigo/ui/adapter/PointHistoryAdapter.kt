package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.protect.jikigo.data.PointHistory
import com.protect.jikigo.data.Storage
import com.protect.jikigo.data.model.UserPaymentHistory
import com.protect.jikigo.databinding.ItemPointHistoryListBinding

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
                tvPointHistoryListPoint.text = if(item.payType == "사용") {
                    "-${item.amount}P"
                }
                else {
                    "+${item.amount}P"
                }
                tvPointHistoryListPointDescription.text = item.reason
            }
        }
    }
}