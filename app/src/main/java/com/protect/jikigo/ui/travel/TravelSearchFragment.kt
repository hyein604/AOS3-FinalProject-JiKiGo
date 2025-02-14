package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTravelSearchBinding
import com.protect.jikigo.ui.extensions.statusBarColor


class TravelSearchFragment : Fragment() {
    private var _binding: FragmentTravelSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelSearchBinding.inflate(inflater, container, false)
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
        setStatusBar()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun moveToTravel() {
        binding.toobarSearch.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}