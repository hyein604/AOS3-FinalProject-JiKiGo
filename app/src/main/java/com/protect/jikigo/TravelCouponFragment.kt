package com.protect.jikigo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.protect.jikigo.databinding.FragmentTravelCouponBinding

class TravelCouponFragment : Fragment() {
    private lateinit var fragmentTravelCouponBinding: FragmentTravelCouponBinding
    private lateinit var homeActivity: HomeActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTravelCouponBinding = FragmentTravelCouponBinding.inflate(inflater)
        homeActivity = activity as HomeActivity

        fragmentTravelCouponBinding.apply {
            val sortContainer = sortContainer
            val tvSort = tvSort

            sortContainer.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), it)
                popupMenu.menuInflater.inflate(R.menu.menu_sort_options, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { item ->
                    tvSort.text = item.title
                    true
                }
                popupMenu.show()
            }
        }

        return fragmentTravelCouponBinding.root
    }
}