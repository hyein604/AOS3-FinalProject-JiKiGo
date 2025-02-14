package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.protect.jikigo.data.PointHistory
import com.protect.jikigo.data.Storage
import com.protect.jikigo.databinding.ItemPointHistoryListBinding

class PointHistoryAdapter: RecyclerView.Adapter<PointHistoryAdapter.PointHistoryViewHolder>() {

    private val items = Storage.pointHistory

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointHistoryViewHolder {
        return PointHistoryViewHolder(ItemPointHistoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PointHistoryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class PointHistoryViewHolder(
        private val binding: ItemPointHistoryListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PointHistory) {
            binding.apply {
                tvPointHistoryListPoint.text = item.point
                tvPointHistoryListPointDescription.text = item.description
            }
        }
    }
}