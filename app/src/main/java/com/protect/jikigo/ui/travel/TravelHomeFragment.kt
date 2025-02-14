package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTravelHomeBinding
import com.protect.jikigo.ui.extensions.statusBarColor

class TravelHomeFragment : Fragment() {
    private var _binding: FragmentTravelHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelHomeBinding.inflate(inflater, container, false)
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
        setStatusBar()
        moveToSearch()
        moveToHotCoupon()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.primary)
    }

    private fun moveToSearch() {
        binding.searchBar.setOnClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelSearch()
            findNavController().navigate(action)
        }
    }

    private fun moveToHotCoupon() {
        binding.tvTravelHomeMore.setOnClickListener {
            val action = TravelFragmentDirections.actionNavigationTravelToTravelHotCoupon()
            findNavController().navigate(action)
        }
    }
}