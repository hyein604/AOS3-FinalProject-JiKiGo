package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTravelCouponDetailBinding
import com.protect.jikigo.ui.extensions.statusBarColor


class TravelCouponDetailFragment : Fragment() {
    private var _binding: FragmentTravelCouponDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelCouponDetailBinding.inflate(inflater, container, false)
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
        showDialog()
        onClickToolbar()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun onClickToolbar() {
        binding.toolbarTravelCouponDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {
        binding.btnTravelCouponDetailBuy.setOnClickListener {
            val action = TravelCouponDetailFragmentDirections.actionTravelCouponDetailToTravelPaymentBottomSheet()
            findNavController().navigate(action)
        }
    }
}