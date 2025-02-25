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
import androidx.lifecycle.lifecycleScope
import com.protect.jikigo.databinding.FragmentRankingBinding
import com.protect.jikigo.ui.rank.dialog.RankingHelpDialog
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.R
import com.protect.jikigo.ui.extensions.statusBarColor
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.protect.jikigo.ui.adapter.RankingAdapter
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.viewModel.MyPageViewModel
import com.protect.jikigo.ui.viewModel.RankingViewModel
import com.protect.jikigo.ui.viewModel.WalkViewModel
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
    private val walkViewModel: WalkViewModel by activityViewModels()
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
        setLayout()  // 화면 레이아웃 설정
        observeRankingData()  // 랭킹 데이터 관찰
        observeStepCount()  // 걸음 수 데이터 관찰
        startTimer()  // 타이머 시작
        getUserInfo()  // 사용자 정보 불러오기
    }

    // 걸음 수 데이터를 관찰하고 UI에 반영
    private fun observeStepCount() {
        // 오늘의 걸음
        walkViewModel.totalSteps.observe(viewLifecycleOwner) { steps ->
            binding.tvRankingWeeklyStepsCount.text = "$steps"
        }

        // 주간 걸음과 이번주 심은 나무 수
        walkViewModel.weeklySteps.observe(viewLifecycleOwner) { steps ->
            binding.tvRankingMyProfileWalkCount.text = "$steps"

            // 걸음 수에 기반한 나무 심기 수 계산
            val plantedTrees = steps.toInt() * 0.00001628 // 1000보당 0.01628그루
            val plantedTreesFormatted = String.format("%.2f", plantedTrees)
            binding.tvRankingMonthlyTreesCount.text = plantedTreesFormatted
        }

        lifecycleScope.launch {
            if (walkViewModel.healthConnectClient.value == null) {
                walkViewModel.checkInstallHC(requireContext())
            }
            walkViewModel.readStepsByTimeRange()
            walkViewModel.updateWeeklySteps()
        }
    }

    // 사용자 정보를 불러와 화면에 표시
    private fun getUserInfo() {
        lifecycleScope.launch {
            val userId = requireContext().getUserId() ?: ""
            rankingViewModel.getUserInfo(userId)
        }

        rankingViewModel.item.observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let {
                binding.tvRankingMyProfileName.text = it.userNickName
                Glide.with(this)
                    .load(it.userProfileImg)
                    .into(binding.ivRankingMyProfile)
            }
        }
    }

    // 타이머를 시작하여 매 초마다 남은 시간을 갱신
    private fun startTimer() {
        handler.post(updateTimerRunnable)
    }

    // 매 초마다 실행되어 남은 시간을 갱신
    private val updateTimerRunnable = object : Runnable {
        override fun run() {
            val remainingTime = getRemainingTimeUntilNextMonday()
            binding.tvRankingRemainingTime.text = "남은시간 $remainingTime"
            handler.postDelayed(this, 1000) // 1초마다 실행
        }
    }

    // 다음 월요일까지 남은 시간을 구함
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

    // 레이아웃 구성 함수
    private fun setLayout() {
        setStatusBarColor()
        onClickToolbar()
        onClickHelp()
        setupRecyclerView()
    }

    // RecyclerView 초기화
    private fun setupRecyclerView() {
        binding.rvRanking.layoutManager = LinearLayoutManager(requireContext())
    }

    // 랭킹 데이터 관찰
    private fun observeRankingData() {
        rankingViewModel.rankingList.observe(viewLifecycleOwner) { rankingList ->
            binding.rvRanking.adapter = RankingAdapter(rankingList)
        }
    }

    // 도움말 버튼 클릭 시 도움말 다이얼로그 표시
    private fun onClickHelp() {
        binding.ivRankingHelp.setOnClickListener {
            val dialog = RankingHelpDialog()
            dialog.show(childFragmentManager, "RankingHelpDialog")
        }
    }

    // 상태바 색상 설정
    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.primary)
    }

    // 툴바 뒤로가기 버튼 클릭 시 이전 화면으로 이동
    private fun onClickToolbar() {
        binding.toolbarRanking.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}