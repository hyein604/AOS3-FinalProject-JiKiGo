package com.protect.jikigo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.data.RankingUser
import com.protect.jikigo.databinding.RowRankingBinding

class RankingAdapter(private val rankingList: List<RankingUser>) : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(private val binding: RowRankingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rankingUser: RankingUser, position: Int) {
            binding.tvRankingPosition.text = (position + 1).toString()
            binding.tvRankingUsername.text = rankingUser.name
            binding.tvRankingSteps.text = String.format("%,d", rankingUser.walkCount)

            Glide.with(binding.root)
                .load(rankingUser.profilePicture)
                .into(binding.ivRankingProfile)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val binding = RowRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(rankingList[position], position)
    }

    override fun getItemCount(): Int = rankingList.size
}
