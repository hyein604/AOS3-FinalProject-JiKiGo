package com.protect.jikigo.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentHomeBinding
import com.protect.jikigo.databinding.FragmentRewardBinding
import com.protect.jikigo.ui.home.HomeFragmentDirections
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.protect.jikigo.ui.extensions.statusBarColor


class RewardFragment : Fragment() {
    private var _binding: FragmentRewardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRewardBinding.inflate(inflater, container, false)
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
        moveToRanking()
        setStatusBarColor()
        onClickToolbar()
        moveToRank()
        moveToTumbler()
        moveToTransit()
        moveToElectricVehicle()
        showBottomDialog()
    }

    private fun moveToRanking() {
        binding.btnRewardWalkRankMore.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToRanking()
            findNavController().navigate(action)
        }
    }
    
    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun onClickToolbar() {
        binding.toolbarReward.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun moveToRank() {
        binding.viewRewardWalkRank.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToRanking()
            findNavController().navigate(action)
        }
    }

    private fun moveToTumbler() {
        binding.viewRewardTumblr.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToTumblrConfirm()
            findNavController().navigate(action)
        }
    }

    private fun moveToElectricVehicle() {
        binding.viewRewardElectricVehicle.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToElectricVehicleConfirm()
            findNavController().navigate(action)
        }
    }

    private fun moveToTransit() {
        binding.viewRewardTransit.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToTransitConfirm()
            findNavController().navigate(action)
        }
    }

    private fun showBottomDialog() {
        binding.viewRewardAttend.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToAttendBottomSheet()
            findNavController().navigate(action)
        }
    }
}