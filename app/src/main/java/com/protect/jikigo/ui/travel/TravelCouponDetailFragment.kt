package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTravelCouponDetailBinding
import com.protect.jikigo.ui.extensions.applyNumberFormat
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.extensions.statusBarColor
import com.protect.jikigo.ui.viewModel.TravelCouponDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TravelCouponDetailFragment : Fragment() {
    private var _binding: FragmentTravelCouponDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel : TravelCouponDetailViewModel by viewModels()
    private val args: TravelCouponDetailFragmentArgs by navArgs()

    private var userPoint : Int = 0

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
        getUserInfo()
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

    private fun getUserInfo() {
        lifecycleScope.launch {
            val userId = requireContext().getUserId() ?: ""
            viewModel.getUserInfo(userId)
        }

        viewModel.item.observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let {
                userPoint = it.userPoint ?: 0
            }
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
        binding.tvTravelCouponDetailInfoContent.text = args.couponArg.couponInfo.replace("\\n", "\n")
    }

    private fun moveToBottomSheet() {
        binding.btnTravelCouponDetailBuy.setOnClickListener {
            val action = TravelCouponDetailFragmentDirections.actionTravelCouponDetailToTravelPaymentBottomSheet(args.couponArg, userPoint)
            findNavController().navigate(action)
        }
    }
}