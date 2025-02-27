package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentCouponDetailBinding
import com.protect.jikigo.ui.extensions.statusBarColor

class CouponDetailFragment : Fragment() {
    private var _binding: FragmentCouponDetailBinding? = null
    private val binding get() = _binding!!
    private val args: CouponDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBar()
        setLayout()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun setLayout() {
        onClickToolbar()
        setText()
    }

    private fun onClickToolbar() {
        binding.toolbarCouponDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setText() {
        binding.apply {
            tvCouponDetailName.text = args.couponArg.purchasedCouponName
            tvCouponDetailClient.text = args.couponArg.purchasedCouponBrand
            if(!args.couponArg.purchasedCouponUsed) {
                tvCouponDetailDate.text = "유효 기간 : ${args.couponArg.purchasedCouponValidDays}"
            }
            else if(args.couponArg.purchasedCouponIsExpiry){
                tvCouponDetailDate.text = "${args.couponArg.purchasedCouponValidDays} 만료됨"
                viewCouponDetailBlur.isVisible = true
                ivCouponDetailBarcode.isVisible = false
            }
            else {
                tvCouponDetailDate.text = "${args.couponArg.purchasedCouponUsedDate} 사용 완료"
                viewCouponDetailBlur.isVisible = true
                ivCouponDetailBarcode.isVisible = false
            }
            Glide.with(requireContext())
                .load(args.couponArg.purchasedCouponImage)
                .into(ivCouponDetailImage)
        }
    }
}