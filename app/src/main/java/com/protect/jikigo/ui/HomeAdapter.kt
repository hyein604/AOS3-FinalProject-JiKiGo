package com.protect.jikigo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.protect.jikigo.data.Store
import com.protect.jikigo.databinding.ItemHomeStoreBinding

class HomeAdapter(
    private val items: List<Store>,
    private val listener: HomeStoreItemClickListener
) : RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder.from(parent, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(items[position])
    }
}


class HomeViewHolder(
    private val binding: ItemHomeStoreBinding,
    private val listener: HomeStoreItemClickListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Store) {
        itemView.setOnClickListener {
            listener.onClickStore(item)
        }
        with(binding) {
            Glide.with(root.context)
                .load(item.thumbNailImg) // 이미지 URL
                .into(ivRvThumbNail); // 이미지가 로드될 ImageView

            tvRvTitle.text = item.title
            tvRvNumber.text = item.number
            tvRvAddress.text = item.address
        }

    }

    companion object {
        fun from(parent: ViewGroup, listener: HomeStoreItemClickListener): HomeViewHolder {
            return HomeViewHolder(
                ItemHomeStoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), listener
            )
        }
    }
}