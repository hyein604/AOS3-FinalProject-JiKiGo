package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTravelCouponDetailBinding
import com.protect.jikigo.ui.extensions.applyNumberFormat
import com.protect.jikigo.ui.extensions.statusBarColor


class TravelCouponDetailFragment : Fragment() {
    private var _binding: FragmentTravelCouponDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TravelCouponDetailFragmentArgs by navArgs()

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
        onClickToolbar()
        moveToBottomSheet()
        setContent()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun onClickToolbar() {
        binding.toolbarTravelCouponDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setContent(){
        Glide.with(binding.root.context)
            .load(args.couponArg.couponImg)
            .placeholder(R.drawable.background_gray5)
            .into(binding.ivCouponDetailThumnail)
        binding.tvTravelCouponDetailBrand.text = args.couponArg.couponBrand
        binding.tvTravelCouponDetailName.text = args.couponArg.couponName
        binding.tvTravelCouponDetatilPrice.applyNumberFormat(args.couponArg.couponPrice)
        binding.tvTravelCouponDetailWhere.text = "사용처 : ${args.couponArg.couponBrand}"
        binding.tvTravelCouponDetailInfoContent.text = args.couponArg.couponInfo
    }

    private fun moveToBottomSheet() {
        binding.btnTravelCouponDetailBuy.setOnClickListener {
            val action = TravelCouponDetailFragmentDirections.actionTravelCouponDetailToTravelPaymentBottomSheet()
            findNavController().navigate(action)
        }
    }
}