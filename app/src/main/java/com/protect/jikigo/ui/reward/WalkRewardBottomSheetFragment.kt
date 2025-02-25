package com.protect.jikigo.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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

    private var currentGoal = 1000
    private var currentReward = 10
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

        // ViewModel의 걸음 수 관찰
        walkViewModel.totalSteps.observe(viewLifecycleOwner) { stepsCount ->
            steps = stepsCount.toIntOrNull() ?: 0
            updateSteps(steps)
        }

        updateUI()

        binding.btnWalkRewardBottomSheetReward.setOnClickListener {
            if (steps >= currentGoal) {
                moveToNextGoal()
            }
        }
    }

    fun updateSteps(newSteps: Int) {
        steps = newSteps
        binding.tvWalkRewardBottomSheetStepsCount.text = "$steps"
        val progress = (steps.toFloat() / currentGoal * 100).toInt()
        binding.progressBarWalkRewardBottomSheet.setProgress(progress)
        binding.btnWalkRewardBottomSheetReward.isEnabled = steps >= currentGoal
    }

    private fun moveToNextGoal() {
        when (currentGoal) {
            1000 -> {
                currentGoal = 10000
                currentReward = 20
            }
            10000 -> {
                currentGoal = 20000
                currentReward = 30
            }
            20000 -> {
                binding.btnWalkRewardBottomSheetReward.isEnabled = false
                return
            }
        }
        updateUI()
    }

    private fun updateUI() {
        binding.tvWalkRewardBottomSheetGoal.text = "/$currentGoal"
        binding.btnWalkRewardBottomSheetReward.text = "${currentReward}P"
        binding.btnWalkRewardBottomSheetReward.isEnabled = steps >= currentGoal
    }
}
