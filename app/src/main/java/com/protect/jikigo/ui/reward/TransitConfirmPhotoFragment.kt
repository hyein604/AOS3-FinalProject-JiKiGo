package com.protect.jikigo.ui.reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentElectricVehicleConfirmPhotoBinding
import com.protect.jikigo.databinding.FragmentTransitConfirmPhotoBinding


class TransitConfirmPhotoFragment : Fragment() {
    private var _binding: FragmentTransitConfirmPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransitConfirmPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}