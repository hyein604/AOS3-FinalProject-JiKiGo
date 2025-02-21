package com.protect.jikigo.ui.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.protect.jikigo.data.model.Notification
import com.protect.jikigo.databinding.RowNotificationBinding
import com.protect.jikigo.R

class NotificationAdapter(
    private val items: List<Notification>,
    private val onItemClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(private val binding: RowNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notification) {
            binding.tvNotificationTitle.text = item.title
            binding.tvNotificationDate.text = item.date

            // 중요 공지 스타일 적용
            if (item.important) {
                binding.tvNotificationTitle.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
                binding.tvNotificationTitle.setTypeface(null, Typeface.BOLD)
            }

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            RowNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}