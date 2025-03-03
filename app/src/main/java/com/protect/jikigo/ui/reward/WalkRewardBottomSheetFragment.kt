package com.protect.jikigo.ui.reward

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.protect.jikigo.databinding.FragmentWalkRewardBottomSheetBinding
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.ui.viewModel.WalkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WalkRewardBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentWalkRewardBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val walkViewModel: WalkViewModel by activityViewModels()

    private var steps = 0
    private var isFinalRewardClaimed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkRewardBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launch {
            if (walkViewModel.healthConnectClient.value == null) {
                walkViewModel.checkInstallHC(requireContext())
            }
            walkViewModel.readStepsByTimeRange()
        }


        // 저장된 마지막 보상 여부 불러오기
        isFinalRewardClaimed = loadFinalRewardClaimed()
        updateUI()

        walkViewModel.totalSteps.observe(viewLifecycleOwner) { stepsCount ->
            steps = stepsCount.toIntOrNull() ?: 0
            updateSteps(steps)
        }

        walkViewModel.currentGoal.observe(viewLifecycleOwner) { updateUI() }
        walkViewModel.currentReward.observe(viewLifecycleOwner) { updateUI() }

        binding.btnWalkRewardBottomSheetReward.setOnClickListener {
            val previousReward = walkViewModel.currentReward.value ?: 0
            saveRewardPoint(previousReward)

            if (steps >= (walkViewModel.currentGoal.value ?: 0)) {
                if (walkViewModel.currentGoal.value == 20000) {
                    isFinalRewardClaimed = true
                    saveFinalRewardClaimed(true) // 마지막 보상 여부 저장
                    updateUI()
                } else {
                    walkViewModel.moveToNextGoal()
                }
            }
        }
    }

    // 마지막 보상 여부를 SharedPreferences에 저장
    private fun saveFinalRewardClaimed(claimed: Boolean) {
        val sharedPreferences = requireContext().getSharedPreferences("walk_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("final_reward_claimed", claimed).apply()
    }

    // 마지막 보상 여부를 SharedPreferences에서 불러오기
    private fun loadFinalRewardClaimed(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("walk_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("final_reward_claimed", false)
    }

    private fun saveRewardPoint(reward: Int) {
        lifecycleScope.launch {
            val userId = requireContext().getUserId() ?: ""
            Log.d("ttest", "프래그먼트 userId: $userId, reward: $reward")
            walkViewModel.setRankingRewardPoint(userId, reward)
        }
    }

    private fun updateSteps(newSteps: Int) {
        steps = newSteps
        binding.tvWalkRewardBottomSheetStepsCount.text = "$steps"
        val progress = (steps.toFloat() / (walkViewModel.currentGoal.value ?: 5) * 100).toInt()
        binding.progressBarWalkRewardBottomSheet.setProgress(progress)
        updateUI()
    }

    private fun updateUI() {
        binding.tvWalkRewardBottomSheetGoal.text = "/${walkViewModel.currentGoal.value}"

        val goal = walkViewModel.currentGoal.value ?: 5
        val reward = walkViewModel.currentReward.value ?: 0
        val isEnabled = steps >= goal

        if (goal == 20000 && isFinalRewardClaimed) {
            // 마지막 단계 보상까지 받으면 버튼 비활성화 & 텍스트 변경
            binding.btnWalkRewardBottomSheetReward.text = "오늘 보상을 모두 받았어요"
            binding.btnWalkRewardBottomSheetReward.isEnabled = false
            binding.btnWalkRewardBottomSheetReward.setBackgroundColor(Color.parseColor("#D2D2D2"))
            binding.btnWalkRewardBottomSheetReward.setTextColor(Color.parseColor("#707070"))
            binding.tvWalkRewardBottomSheetCongrats.visibility = View.VISIBLE
            binding.tvWalkRewardBottomSheetCheering.visibility = View.GONE

        } else {
            // 보상 버튼 상태 업데이트
            binding.btnWalkRewardBottomSheetReward.text = "${reward}P"
            binding.btnWalkRewardBottomSheetReward.isEnabled = isEnabled

            if (isEnabled) {
                binding.btnWalkRewardBottomSheetReward.setBackgroundColor(Color.parseColor("#7A8FFF"))
                binding.btnWalkRewardBottomSheetReward.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvWalkRewardBottomSheetCongrats.visibility = View.VISIBLE
                binding.tvWalkRewardBottomSheetCheering.visibility = View.GONE
            } else {
                binding.btnWalkRewardBottomSheetReward.setBackgroundColor(Color.parseColor("#D2D2D2"))
                binding.btnWalkRewardBottomSheetReward.setTextColor(Color.parseColor("#707070"))
                binding.tvWalkRewardBottomSheetCongrats.visibility = View.GONE
                binding.tvWalkRewardBottomSheetCheering.visibility = View.VISIBLE
            }
        }
    }
}
