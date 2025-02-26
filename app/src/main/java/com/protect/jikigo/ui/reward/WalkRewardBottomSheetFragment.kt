package com.protect.jikigo.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.protect.jikigo.databinding.FragmentWalkRewardBottomSheetBinding
import com.protect.jikigo.ui.viewModel.WalkViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class WalkRewardBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentWalkRewardBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val walkViewModel: WalkViewModel by activityViewModels()


    private var steps = 0

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
            if (steps >= walkViewModel.currentGoal.value!!) {
                walkViewModel.moveToNextGoal()
            }
        }
    }

    fun updateSteps(newSteps: Int) {
        steps = newSteps
        binding.tvWalkRewardBottomSheetStepsCount.text = "$steps"
        val progress = (steps.toFloat() / (walkViewModel.currentGoal.value ?: 5) * 100).toInt()
        binding.progressBarWalkRewardBottomSheet.setProgress(progress)
        binding.btnWalkRewardBottomSheetReward.isEnabled = steps >= (walkViewModel.currentGoal.value ?: 5)
    }

    private fun updateUI() {
        binding.tvWalkRewardBottomSheetGoal.text = "/${walkViewModel.currentGoal.value}"
        binding.btnWalkRewardBottomSheetReward.text = "${walkViewModel.currentReward.value}P"
        binding.btnWalkRewardBottomSheetReward.isEnabled = steps >= (walkViewModel.currentGoal.value ?: 5)
    }
}
