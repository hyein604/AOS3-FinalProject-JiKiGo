package com.protect.jikigo.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTosBinding
import com.protect.jikigo.ui.travel.TravelPaymentBottomSheetFragment


class TosFragment : Fragment() {
    private var _binding: FragmentTosBinding? = null
    private val binding get() = _binding!!
    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
            findNavController().navigate(R.id.action_travelCouponDetail_to_travelPaymentBottomSheet)
        }
    }

    private fun setLayout() {
        moveToWebSite()
    }

    private fun moveToWebSite() {
        binding.webViewTos.settings.javaScriptEnabled = true
        binding.webViewTos.webViewClient = WebViewClient()
        binding.webViewTos.loadUrl(args.urlMethod.url)
        Log.d("TEST", args.urlMethod.url)
    }

}