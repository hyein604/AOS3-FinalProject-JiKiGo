package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.databinding.FragmentTravelPaymentCompleteBinding

class TravelPaymentCompleteFragment : Fragment() {
    private var _binding: FragmentTravelPaymentCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelPaymentCompleteBinding.inflate(inflater, container, false)
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
        moveToTravel()
    }

    private fun moveToTravel() {
        binding.btnTravelPaymentCompleteGoToCoupon.setOnClickListener {
            val action = TravelPaymentCompleteFragmentDirections.actionTravelPaymentCompleteToCouponBox()
            findNavController().navigate(action)
        }
        binding.btnTravelPaymentCompleteContinueShopping.setOnClickListener {
            val action = TravelPaymentCompleteFragmentDirections.actionTravelPaymentCompleteToNavigationTravel()
            findNavController().navigate(action)
        }
    }
}