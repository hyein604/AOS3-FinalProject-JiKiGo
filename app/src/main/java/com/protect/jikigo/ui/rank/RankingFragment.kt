package com.protect.jikigo.ui.rank

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.protect.jikigo.databinding.FragmentRankingBinding
import com.protect.jikigo.ui.rank.dialog.RankingHelpDialog
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.ui.extensions.statusBarColor
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.protect.jikigo.ui.adapter.RankingAdapter
import com.protect.jikigo.ui.extensions.applyNumberFormat
import com.protect.jikigo.ui.extensions.applySpannableStyles
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.viewModel.MyPageViewModel
import com.protect.jikigo.ui.viewModel.RankingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
@AndroidEntryPoint
class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!
    private val rankingViewModel: RankingViewModel by viewModels()
    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacks(updateTimerRunnable)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLayout()
        observeRankingData()
        observeStepCount() // 걸음 수 데이터 관찰
        startTimer()
        getUserInfo()
    }


    private fun observeStepCount() {
        myPageViewModel.totalSteps.observe(viewLifecycleOwner) { steps ->
            binding.tvRankingWeeklyStepsCount.text = "$steps"
            Log.d("test1","뷰모델에서 불러온 걸음 수 : $steps")
        }

        lifecycleScope.launch {
            if (myPageViewModel.healthConnectClient.value == null) {
                myPageViewModel.checkInstallHC(requireContext())
            }
            myPageViewModel.readStepsByTimeRange()
        }
    }

    private fun getUserInfo() {
        lifecycleScope.launch {
            val userId = requireContext().getUserId() ?: ""
            rankingViewModel.getUserInfo(userId)
        }

        rankingViewModel.item.observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let {
                binding.tvRankingMyProfileName.text = it.userName
                Glide.with(this)
                    .load(it.userProfileImg)
                    .into(binding.ivRankingMyProfile)
            }
        }
    }

    private fun startTimer() {
        handler.post(updateTimerRunnable)
    }

    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            val remainingTime = getRemainingTimeUntilNextMonday()
            binding.tvRankingRemainingTime.text = "남은시간 $remainingTime"
            handler.postDelayed(this, 1000)
        }
    }

    private fun getRemainingTimeUntilNextMonday(): String {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val remainingMillis = calendar.timeInMillis - System.currentTimeMillis()
        val hours = remainingMillis / (1000 * 60 * 60)
        val minutes = (remainingMillis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (remainingMillis % (1000 * 60)) / 1000

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun setLayout() {
        setStatusBarColor()
        onClickToolbar()
        onClickHelp()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeRankingData() {
        rankingViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            binding.rvRanking.adapter = RankingAdapter(rankingList)
        }
    }

    private fun onClickHelp() {
        binding.ivRankingHelp.setOnClickListener {
            val dialog = RankingHelpDialog()
            dialog.show(childFragmentManager, "RankingHelpDialog")
        }
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.primary)
    }

    private fun onClickToolbar() {
        binding.toolbarRanking.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
