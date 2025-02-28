package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTravelBannerBinding
import com.protect.jikigo.ui.extensions.statusBarColor


class TravelBannerFragment : Fragment() {
    private var _binding: FragmentTravelBannerBinding? = null
    private val binding get() = _binding!!
    private val args : TravelBannerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelBannerBinding.inflate(inflater, container, false)
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
        loadBannerData(args.positionArgs)
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun onClickToolbar() {
        binding.toolbarTravelBanner.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadBannerData(position : Int){
        val bannerTitle = listOf(
            "MCY PARK",
            "경주월드",
            "동궁과 월지"
        )

        binding.toolbarTravelBanner.title = bannerTitle[position]
    }

}