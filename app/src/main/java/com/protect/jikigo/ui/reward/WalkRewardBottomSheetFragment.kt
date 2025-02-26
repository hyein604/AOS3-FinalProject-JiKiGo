package com.protect.jikigo.ui.reward

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.protect.jikigo.databinding.FragmentWalkRewardBottomSheetBinding
import com.protect.jikigo.ui.viewModel.WalkViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        // ViewModel에서 값 가져와 UI 업데이트
        walkViewModel.totalSteps.observe(viewLifecycleOwner) { stepsCount ->
            steps = stepsCount.toIntOrNull() ?: 0
            updateSteps(steps)
        }

        walkViewModel.currentGoal.observe(viewLifecycleOwner) { updateUI() }
        walkViewModel.currentReward.observe(viewLifecycleOwner) { updateUI() }

        binding.btnWalkRewardBottomSheetReward.setOnClickListener {
            if (steps >= (walkViewModel.currentGoal.value ?: 0)) {
                if (walkViewModel.currentGoal.value == 150) {
                    // 마지막 보상 버튼 클릭 시 "오늘 보상을 모두 받았어요"로 변경
                    isFinalRewardClaimed = true
                    updateUI()
                } else {
                    walkViewModel.moveToNextGoal()
                }
            }
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

        if (goal == 150 && isFinalRewardClaimed) {
            // 마지막 단계 보상까지 받으면 버튼 비활성화 & 텍스트 변경
            binding.btnWalkRewardBottomSheetReward.text = "오늘 보상을 모두 받았어요"
            binding.btnWalkRewardBottomSheetReward.isEnabled = false
            binding.btnWalkRewardBottomSheetReward.setBackgroundColor(Color.parseColor("#D2D2D2"))
            binding.btnWalkRewardBottomSheetReward.setTextColor(Color.parseColor("#707070"))
        } else {
            // 보상 버튼 상태 업데이트
            binding.btnWalkRewardBottomSheetReward.text = "${reward}P"
            binding.btnWalkRewardBottomSheetReward.isEnabled = isEnabled

            if (isEnabled) {
                binding.btnWalkRewardBottomSheetReward.setBackgroundColor(Color.parseColor("#7A8FFF"))
                binding.btnWalkRewardBottomSheetReward.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                binding.btnWalkRewardBottomSheetReward.setBackgroundColor(Color.parseColor("#D2D2D2"))
                binding.btnWalkRewardBottomSheetReward.setTextColor(Color.parseColor("#707070"))
            }
        }
    }
}
