package com.protect.jikigo.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentRewardBinding
import com.protect.jikigo.ui.extensions.statusBarColor
import java.util.Random


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
        setStatusBarColor()
        onClickListener()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun onClickListener() {
        // 백버튼
        binding.toolbarReward.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        // 랭크 화면으로 이동
        binding.viewRewardWalkRank.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToRanking()
            findNavController().navigate(action)
        }
        // 텀블러 인증 화면으로 이동
        binding.viewRewardTumblr.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToTumblrConfirm()
            findNavController().navigate(action)
        }
        // 전기 이동수단 인증 화면으로 이동
        binding.viewRewardElectricVehicle.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToElectricVehicleConfirm()
            findNavController().navigate(action)
        }
        // 대중교통 인증 화면으로 이동
        binding.viewRewardTransit.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToTransitConfirm()
            findNavController().navigate(action)
        }
        // 마이 페이지 화면으로 이동
        binding.toolbarReward.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_my_page -> {
                    val action = RewardFragmentDirections.actionNavigationRewardToMyPage()
                    findNavController().navigate(action)
                }
            }
            true
        }
        // 출석체크 바텀시트 띄우기
        binding.viewRewardAttend.setOnClickListener {
            val random = Random().nextInt(10) + 1
            val action = RewardFragmentDirections.actionNavigationRewardToAttendBottomSheet(random)
            findNavController().navigate(action)
        }
        binding.btnRewardWalkPoint.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToWalkRewardBottomSheet()
            findNavController().navigate(action)
        }
    }
}