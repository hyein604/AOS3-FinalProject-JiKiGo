package com.protect.jikigo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        moveToMyPage()
        moveToNews()
        moveToNotification()
        moveToQR()
    }

    private fun setStatusBarColor() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.primary)
    }

    private fun moveToNotification() {
        binding.tvHomeNoticeMore.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNotification()
            findNavController().navigate(action)
        }
    }

    private fun moveToNews() {
        binding.ivHomeEnvironment.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToNewsEnvironment()
            findNavController().navigate(action)
        }
    }

    private fun moveToQR() {
        binding.ivHomeQr.setOnClickListener {
            val action = HomeFragmentDirections.actionNavigationHomeToPaymentQR()
            findNavController().navigate(action)
        }
    }

    private fun moveToMyPage() {
        binding.toolbarHome.setOnMenuItemClickListener { menu ->
            if (menu.itemId == R.id.menu_my_page) {
                val action = HomeFragmentDirections.actionNavigationHomeToMyPage()
                findNavController().navigate(action)
                true
            } else {
                false
            }
        }
    }
}