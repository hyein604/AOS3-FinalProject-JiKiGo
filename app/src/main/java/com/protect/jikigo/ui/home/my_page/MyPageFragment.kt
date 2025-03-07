package com.protect.jikigo.ui.home.my_page



import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.protect.jikigo.ui.activity.LoginActivity
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentMyPageBinding
import com.protect.jikigo.utils.clearUserId
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.utils.statusBarColor
import com.protect.jikigo.ui.viewModel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var userId : String

    private lateinit var requestPermissions: ActivityResultLauncher<Set<String>>
    private lateinit var healthConnectClient: HealthConnectClient

    private val permission =
        setOf(
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class)
        )


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
        setLayout()
    }

    private fun setLayout() {
        setStatusBar()
        onClickListener()
        observe()
        checkData()
    }
    // 옵저버
    private fun observe() {
        binding.apply {
            viewModel.totalSteps.observe(viewLifecycleOwner) {step ->
                tvMyPageWalkCount.text = "${step} 걸음"

                val stepKcal = step.toInt() * 0.04
                val stepKcalFormatted = String.format("%.2f", stepKcal)
                tvMyPageWalkKcal.text = "${stepKcalFormatted}kcal"

                val date = Date(System.currentTimeMillis())
                val simpleDateFormat = SimpleDateFormat("MM-dd")
                val now = simpleDateFormat.format(date)
                tvMyPageWalkDate.text = "$now"
            }

            viewModel.profile.observe(viewLifecycleOwner) { profile ->
                tvMyPageProfileName.text = "${profile.userNickName}"
                Glide.with(requireContext())
                    .load(profile.userProfileImg)
                    .circleCrop()
                    .into(ivMyPageProfileImage)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
                when(loading) {
                    false -> {
                        binding.layoutMyPageLoading.visibility = View.GONE
                    }
                    true -> {
                        binding.layoutMyPageLoading.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
    // 시작 시 확인할 데이터
    private fun checkData() {
        viewModel.loading(true)
        lifecycleScope.launch {
            checkInstallHC()
            userId = requireContext().getUserId() ?: ""
            viewModel.loadProfile(userId)
        }
    }
    // 시스템 상태바 색 변경
    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun onClickListener() {
        binding.apply {
            // 프로필 수정
            btnMyPageProfileEdit.setOnClickListener {
                val action = MyPageFragmentDirections.actionMyPageToProfileEdit()
                findNavController().navigate(action)
            }
            // 포인트 내역
            viewMyPagePoint.setOnClickListener {
                val action = MyPageFragmentDirections.actionMyPageToPointHistory()
                findNavController().navigate(action)
            }
            // 쿠폰함
            viewMyPageCoupon.setOnClickListener {
                val action = MyPageFragmentDirections.actionMyPageToCouponBox()
                findNavController().navigate(action)
            }
            // 백 버튼
            toolbarMyPage.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            // 회원 탈퇴 다이얼로그
            btnMyPageDeleteAccount.setOnClickListener {
                val dialog = DeleteIdDialog()
                dialog.show(childFragmentManager, "DeleteIdDialog")
            }
            // 로그아웃
            binding.btnMyPageLogout.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    requireContext().clearUserId()
                    requireActivity().finish()
                    NaverIdLoginSDK.logout()
                    UserApiClient.instance.logout { error->
                        if (error != null) {
                            Log.d("MyPageFragment", "카카오로그아웃 실패")
                        } else {
                            Log.d("MyPageFragment", "카카오로그아웃 성공")
                        }
                    }
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }



    /*
    걸음 수
     */

    // 권한 확인
    private fun movePermissionSetting(context: Context) {
        val packageName = "com.google.android.apps.healthdata"
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)

        if (intent != null) {
            context.startActivity(intent)
            Toast.makeText(context, "앱 권한 → '걸음 수'를 활성화 해주세요.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "설정 화면을 여는 데 실패했습니다. 직접 앱 권한을 확인해주세요.", Toast.LENGTH_LONG).show()
        }
    }



    //헬스 커넥트 플레이스토어 이동
    private fun openPlayStoreForHealthConnect(){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")
            setPackage("com.android.vending")
        }
        startActivity(intent)
    }

    //헬스커넥트 설치, 버전, 권한 여부 확인
    private fun checkInstallHC() {
        val providerPackageName = "com.google.android.apps.healthdata"
        val availabilityStatus = HealthConnectClient.getSdkStatus(requireContext(), providerPackageName)

        // 헬스 커넥트 앱이 없거나 업데이트가 필요할 때 Play Store 이동
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE ||
            availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            Toast.makeText(requireContext(), "헬스 커넥트 앱을 설치 또는 업데이트 해주세요", Toast.LENGTH_SHORT).show()
            openPlayStoreForHealthConnect()
            return
        }

        healthConnectClient = HealthConnectClient.getOrCreate(requireContext())

        // 권한(permission) 확인
        val requestPermissionActivityContract = PermissionController.createRequestPermissionResultContract()
        requestPermissions = registerForActivityResult(requestPermissionActivityContract) { granted ->
            if (granted.containsAll(permission)) {
                // 권한이 부여된 경우
                Log.d("PermissionO", "$healthConnectClient")
                lifecycleScope.launch {
                    readStepsByTimeRange()
                }
            } else {
                // 권한이 거부된 경우 처리
                Log.d("PermissionX", "$healthConnectClient")

                // "다시 묻지 않음"을 선택했는지 확인
                val shouldShowRationale = permission.any {
                    shouldShowRequestPermissionRationale(it)
                }

                if (shouldShowRationale) {
                    // 권한을 다시 요청
                    requestPermissions.launch(permission)
                } else {
                    // "다시 묻지 않음"을 선택했을 경우 -> 설정 화면으로 이동
                    movePermissionSetting(requireContext())
                }
            }
        }

        // 권한 요청 실행
        requestPermissions.launch(permission)
    }

    // 걸음 수 불러오기
    private suspend fun readStepsByTimeRange() {
        val endTime = LocalDateTime.now()
        val startTime = LocalDateTime.of(endTime.toLocalDate(), LocalTime.MIDNIGHT)

        try {
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            viewModel.totalSteps.value = response.records[0].count.toString()

            // records가 비어있는지 확인하고, 로그 출력
            if (response.records.isEmpty()) {
                viewModel.totalSteps.value = "0"
            } else {
                viewModel.totalSteps.value = response.records[0].count.toString()
            }

        }
        catch (e: IndexOutOfBoundsException) {
            viewModel.totalSteps.value = "0"
        }
        catch (e: Exception) {
            Log.v("Total Steps", "실행 안됨 : $e")
        }
    }

}