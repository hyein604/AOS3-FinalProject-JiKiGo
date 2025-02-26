package com.protect.jikigo.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.databinding.FragmentTransitConfirmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransitConfirmFragment : Fragment() {
    private var _binding: FragmentTransitConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransitConfirmBinding.inflate(inflater, container, false)
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
            toolbarTransitConfirm.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            btnTransitConfirm.setOnClickListener {
                val action = TransitConfirmFragmentDirections.actionTransitConfirmToTransitConfirmPhoto()
                findNavController().navigate(action)
            }
        }
    }
}