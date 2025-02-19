package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.data.Coupon
import com.protect.jikigo.data.Storage
import com.protect.jikigo.databinding.FragmentTravelHotCouponBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.toast

class TravelHotCouponFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelHotCouponBinding? = null
    private val binding get() = _binding!!

    private var coupon : List<Coupon> = listOf()
    lateinit var adaptor: CouponAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelHotCouponBinding.inflate(inflater, container, false)
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
        onClickToolbar()
        setRecyclerView()
    }

    private fun onClickToolbar() {
        binding.toolbarTravelHotCoupon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setRecyclerView(){
        coupon = Storage.coupon.sortedByDescending { it.salesCount } ?: emptyList()
        adaptor = CouponAdaptor(coupon, this)
        binding.rvTravelHorCoupon.adapter = adaptor
    }

    override fun onClickListener(item: Coupon) {
        requireContext().toast(item.name)
    }
}