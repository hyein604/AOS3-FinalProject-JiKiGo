package com.protect.jikigo.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.databinding.FragmentElectricVehicleConfirmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ElectricVehicleConfirmFragment : Fragment() {
    private var _binding: FragmentElectricVehicleConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentElectricVehicleConfirmBinding.inflate(inflater, container, false)
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
        onClickListener()
    }

    private fun onClickListener() {
        binding.apply {
            toolbarElectricVehicleConfirm.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnElectricVehicleConfirm.setOnClickListener {
                val action = ElectricVehicleConfirmFragmentDirections.actionElectricVehicleConfirmToElectricVehicleConfirmPhoto()
                findNavController().navigate(action)
            }
        }
    }

}