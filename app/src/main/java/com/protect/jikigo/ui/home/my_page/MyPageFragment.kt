package com.protect.jikigo.ui.home.my_page

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.protect.jikigo.LoginActivity
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentMyPageBinding
import com.protect.jikigo.ui.extensions.clearUserId
import com.protect.jikigo.ui.extensions.statusBarColor
import com.protect.jikigo.ui.viewModel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermissionLauncher() // 권한 요청 런처 설정
        setLayout() // UI 초기화
    }

    /**
     * 사용자 활동 권한 요청을 처리하는 런처 설정
     */
    private fun setupPermissionLauncher() {
        requestPermissions = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            val allGranted = result.values.all { it } // 모든 권한이 허용되었는지 확인
            if (allGranted) {
                lifecycleScope.launch { viewModel.readStepsByTimeRange() } // 걸음 수 데이터 가져오기
            } else {
                viewModel.movePermissionSetting(requireContext()) // 권한 설정 화면으로 이동
            }
        }
    }

    /**
     * UI 레이아웃 설정
     */
    private fun setLayout() {
        setStatusBar()
        setupClickListeners()
        observe()
        checkHC()
    }

    /**
     * ViewModel의 LiveData를 관찰하여 UI 업데이트
     */
    private fun observe() {
        // 걸음 수 데이터가 변경될 때 UI 업데이트
        viewModel.totalSteps.observe(viewLifecycleOwner) { steps ->
            binding.tvMyPageWalkCount.text = "$steps 걸음"
            binding.tvMyPageWalkKcal.text = "${steps.toInt() * 0.04} kcal"

            val now = SimpleDateFormat("MM-dd").format(Date())
            binding.tvMyPageWalkDate.text = now
        }

        // 권한 요청 여부를 감지하고 요청 실행
        viewModel.requestPermissions.observe(viewLifecycleOwner) {
            requestPermissions.launch(
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION)
            )
        }
    }

    /**
     * Google Health Connect 설치 여부 확인
     */
    private fun checkHC() {
        viewModel.checkInstallHC(requireContext())
    }

    /**
     * 상태바 색상 설정
     */
    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.white)
    }

    /**
     * 버튼 및 UI 클릭 이벤트 설정
     */
    private fun setupClickListeners() {
        binding.apply {
            btnMyPageProfileEdit.setOnClickListener { navigateTo(R.id.action_myPage_to_profileEdit) }
            viewMyPagePoint.setOnClickListener { navigateTo(R.id.action_myPage_to_pointHistory) }
            viewMyPageCoupon.setOnClickListener { navigateTo(R.id.action_myPage_to_couponBox) }
            toolbarMyPage.setNavigationOnClickListener { findNavController().navigateUp() }
            btnMyPageLogout.setOnClickListener { logout() }
        }
    }

    /**
     * 네비게이션을 통해 화면 이동
     */
    private fun navigateTo(actionId: Int) {
        findNavController().navigate(actionId)
    }

    /**
     * 로그아웃 기능: 사용자 ID 삭제 후 로그인 화면으로 이동
     */
    private fun logout() {
        viewLifecycleOwner.lifecycleScope.launch {
            requireContext().clearUserId()
            requireActivity().finish()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }
}