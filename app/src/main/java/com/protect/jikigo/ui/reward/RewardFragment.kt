package com.protect.jikigo.ui.reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentHomeBinding
import com.protect.jikigo.databinding.FragmentRewardBinding
import com.protect.jikigo.ui.home.HomeFragmentDirections

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
    }
    private fun moveToRanking() {
        binding.btnRewardWalkRankMore.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToRanking()
            findNavController().navigate(action)
        }
    }
}