package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.protect.jikigo.databinding.FragmentTravelPaymentBottomSheetBinding


class TravelPaymentBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentTravelPaymentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelPaymentBottomSheetBinding.inflate(inflater, container, false)
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
        onClickBuyBtn()
    }

    private fun onClickBuyBtn() {
        binding.btnTravelPaymentComplete.setOnClickListener {
            val action = TravelPaymentBottomSheetFragmentDirections.actionTravelPaymentBottomSheetToTravelPaymentComplete()
            findNavController().navigate(action)
        }
    }

}