package com.protect.jikigo.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentPaymentQrBinding
import com.protect.jikigo.ui.extensions.statusBarColor

class PaymentQRFragment : Fragment() {
    private var _binding: FragmentPaymentQrBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentQrBinding.inflate(inflater, container, false)
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
        showDialog()
        moveToTravel()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.payment_background)
    }

    private fun onClickToolbar() {
        binding.toolbarPayment.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {
        binding.ivQrHelp.setOnClickListener {
            val action = PaymentQRFragmentDirections.actionPaymentQRToPaymentQRDialog()
            findNavController().navigate(action)
        }
    }

    private fun moveToTravel() {
        binding.viewQrUse.setOnClickListener {
            val action = PaymentQRFragmentDirections.actionPaymentQRToNavigationTravel()
            findNavController().navigate(action)
        }
    }

}