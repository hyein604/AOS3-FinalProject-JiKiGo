package com.protect.jikigo.ui.rank.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.protect.jikigo.R

class RankingBannerAdapter(fragment: Fragment) :
    RecyclerView.Adapter<RankingBannerAdapter.ViewHolder>() {

    private val pages = listOf(R.layout.item_ranking_dialog_page_1, R.layout.item_ranking_dialog_page_2)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int = pages.size

    override fun getItemViewType(position: Int): Int = pages[position]

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}