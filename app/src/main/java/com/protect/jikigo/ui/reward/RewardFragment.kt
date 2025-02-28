package com.protect.jikigo.ui.reward

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentRewardBinding
import com.protect.jikigo.ui.extensions.convertThreeDigitComma
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.extensions.statusBarColor
import com.protect.jikigo.ui.viewModel.RewardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Random

@AndroidEntryPoint
class RewardFragment : Fragment() {
    private var _binding: FragmentRewardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RewardViewModel by viewModels()

    private lateinit var userId : String

    private val random = Random().nextInt(4) + 1

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
        observe()
        checkData()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.white)
    }

    // 시작 시 확인할 데이터
    private fun checkData() {
        viewModel.loading(true)
        lifecycleScope.launch {
            userId = requireContext().getUserId() ?: ""
            viewModel.loadAttend(userId)
            viewModel.loadProfile(userId)
        }
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
            lifecycleScope.launch {
                if(viewModel.isAttendData.value == false) {
                    viewModel.loading(true)
                    viewModel.todayAttend(userId, random)
                }
                else {
                    val action = RewardFragmentDirections.actionNavigationRewardToPointHistory()
                    findNavController().navigate(action)
                }
            }
        }
        // 포인트 내역으로 이동
        binding.tvRewardPoint.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToPointHistory()
            findNavController().navigate(action)
        }
        // QR로 이동
        binding.ivRewardBarcode.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToPaymentQR()
            findNavController().navigate(action)
        }
        binding.viewRewardWalk.setOnClickListener {
            val action = RewardFragmentDirections.actionNavigationRewardToWalkRewardBottomSheet()
            findNavController().navigate(action)
        }
    }

    private fun observe() {
        binding.apply {
            viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
                when(loading) {
                    false -> {
                        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        binding.layoutRewardLoading.visibility = View.GONE
                    }
                    true -> {
                        requireActivity().window.setFlags(
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        )
                        binding.layoutRewardLoading.visibility = View.VISIBLE
                    }
                }
            }

            viewModel.profile.observe(viewLifecycleOwner) { profile ->
                tvRewardNickname.text = profile.userNickName
            }

            viewModel.userPoint.observe(viewLifecycleOwner) {
                tvRewardPoint.text = it.convertThreeDigitComma()
            }

            viewModel.isAttend.observe(viewLifecycleOwner) {
                if(it) {
                    viewModel.loading(false)
                    val action = RewardFragmentDirections.actionNavigationRewardToAttendBottomSheet(random)
                    findNavController().navigate(action)
                    viewModel.doneAttend()
                }
                else {
                    viewModel.loading(false)
                }
            }

            viewModel.attendPoint.observe(viewLifecycleOwner) {
                setupSpannableString(it)
            }
        }
    }

    private fun setupSpannableString(point: Int) {
        binding.apply {
            val fullText = "출석 포인트 ${point}P "
            val spannableString = SpannableString(fullText)

            val startIndex = 6  // 12번째 문자 (인덱스는 0부터 시작)
            val endIndex = 10    // 18번째 문자 + 1 (마지막 인덱스는 포함되지 않음)

            spannableString.setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.primary)),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            tvRewardAttend.text = spannableString
            tvRewardAttendDescription.isVisible = true
        }
    }
}