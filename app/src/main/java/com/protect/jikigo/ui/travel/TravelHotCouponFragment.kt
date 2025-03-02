package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.data.model.Coupon
import com.protect.jikigo.data.repo.CouponRepo
import com.protect.jikigo.databinding.FragmentTravelHotCouponBinding
import com.protect.jikigo.ui.adapter.CouponAdaptor
import com.protect.jikigo.ui.adapter.TravelCouponOnClickListener
import com.protect.jikigo.ui.extensions.statusBarColor
import com.protect.jikigo.ui.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TravelHotCouponFragment : Fragment(), TravelCouponOnClickListener {
    private var _binding: FragmentTravelHotCouponBinding? = null
    private val binding get() = _binding!!

    private var coupon : List<Coupon> = listOf()
    lateinit var adaptor: CouponAdaptor

    @Inject
    lateinit var couponRepo : CouponRepo

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
        setStatusBarColor()
        onClickToolbar()
        setRecyclerView()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun onClickToolbar() {
        binding.toolbarTravelHotCoupon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setRecyclerView(){

        startShimmer()
        lifecycleScope.launch {
            coupon = couponRepo.getAllCouponSortedBySales()
            adaptor = CouponAdaptor(coupon, this@TravelHotCouponFragment)
            binding.rvTravelHorCoupon.adapter = adaptor

            if (!isAdded || view == null) return@launch

            stopShimmer()
        }
    }

    private fun startShimmer(){
        binding.rvTravelHorCoupon.visibility = View.GONE
        binding.shimmerTravelHotCoupon.visibility = View.VISIBLE
        binding.shimmerTravelHotCoupon.startShimmer()
    }

    private fun stopShimmer() {
        if (!isAdded || view == null) return

        binding.shimmerTravelHotCoupon.stopShimmer()
        binding.shimmerTravelHotCoupon.visibility = View.GONE
        binding.rvTravelHorCoupon.visibility = View.VISIBLE
    }

    override fun onClickListener(item: Coupon) {
        val action = TravelHotCouponFragmentDirections.actionTravelHotCouponToTravelCouponDetail(item)
        findNavController().navigate(action)

    }
}