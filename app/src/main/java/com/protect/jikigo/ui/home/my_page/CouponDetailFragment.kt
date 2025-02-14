package com.protect.jikigo.ui.home.my_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
        requireActivity().statusBarColor(R.color.primary)
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
        binding.tvCouponDetailName.text = args.couponArg.name
        binding.tvCouponDetailClient.text = args.couponArg.brand
        binding.tvCouponDetailDate.text = args.couponArg.date
    }
}