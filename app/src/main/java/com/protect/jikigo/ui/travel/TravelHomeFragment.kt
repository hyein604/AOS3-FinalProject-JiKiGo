package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.Storage
import com.protect.jikigo.databinding.FragmentTravelHomeBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.statusBarColor

class TravelHomeFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
    }

    private fun setLayout() {
        setStatusBar()
        moveToSearch()
        moveToHotCoupon()
        setTextWithColor(binding.tvTravelHomeHotCoupon, "HOT 추천쿠폰", "HOT", R.color.primary)
        setRecyclerView()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.primary)
    }

    private fun moveToSearch() {
        binding.searchBar.setOnClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelSearch()
            findNavController().navigate(action)
        }
        binding.searchBar.setOnMenuItemClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelSearch()
            findNavController().navigate(action)
            true
        }
    }

    private fun moveToHotCoupon() {
        binding.tvTravelHomeMore.setOnClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelHotCoupon()
            findNavController().navigate(action)
        }
    }

    private fun setRecyclerView(){
        val coupon = Storage.coupon.sortedByDescending { it.salesCount }.take(4)
        val adapter = CouponAdaptor(coupon, this)
        binding.rvHotCouponList.adapter = adapter
    }

    private fun setTextWithColor(textView: TextView, text: String, target: String, colorResId: Int) {
        val spannableString = SpannableString(text).apply {
            val start = text.indexOf(target)
            val end = start + target.length
            setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), colorResId)),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.text = spannableString
    }

    override fun onClickListener(item: Coupon) {
        val action = TravelFragmentDirections.actionNavigationTravelToTravelCouponDetail(item)
        findNavController().navigate(action)
    }
}