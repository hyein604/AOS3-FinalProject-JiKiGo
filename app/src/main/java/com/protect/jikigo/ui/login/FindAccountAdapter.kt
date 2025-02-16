package com.protect.jikigo.ui.login

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FindAccountAdapter(fragment: Fragment, private val tab: List<String>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return tab.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FindIdFragment.newInstance()
            else -> FindPwFragment.newInstance()
        }
    }
}