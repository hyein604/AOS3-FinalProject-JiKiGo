package com.protect.jikigo.ui.viewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class MyPageViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _totalSteps = MutableLiveData<String>()
    val totalSteps: LiveData<String> = _totalSteps

    private val _healthConnectClient = MutableLiveData<HealthConnectClient?>()
    val healthConnectClient: LiveData<HealthConnectClient?> = _healthConnectClient

    private val _requestPermissions = MutableLiveData<Boolean>()
    val requestPermissions: LiveData<Boolean> = _requestPermissions

    // Health Connect에서 걸음 수 데이터를 읽고/쓰기 위한 권한 설정
    private val permission = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

    /**
     * Google Health Connect 앱 설치 여부 확인 및 초기화
     */
    fun checkInstallHC(context: Context) {
        val providerPackageName = "com.google.android.apps.healthdata"
        val availabilityStatus = HealthConnectClient.getSdkStatus(context, providerPackageName)

        // SDK가 사용 불가능한 경우 사용자에게 안내 및 앱 설치 유도
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE ||
            availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            Toast.makeText(context, "헬스 커넥트 앱을 설치 또는 업데이트 해주세요", Toast.LENGTH_SHORT).show()
            openPlayStoreForHealthConnect(context)
            return
        }

        // Health Connect 클라이언트 생성 후 권한 요청
        _healthConnectClient.value = HealthConnectClient.getOrCreate(context)
        _requestPermissions.value = true
    }

    /**
     * Google Play 스토어에서 Health Connect 앱 설치 화면 열기
     */
    fun openPlayStoreForHealthConnect(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")
            setPackage("com.android.vending")
        }
        context.startActivity(intent)
    }

    /**
     * 권한 설정 화면으로 이동 (사용자가 직접 권한 활성화하도록 유도)
     */
    fun movePermissionSetting(context: Context) {
        val packageName = "com.google.android.apps.healthdata"
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent)
            Toast.makeText(context, "앱 권한 → '걸음 수'를 활성화 해주세요.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "설정 화면을 여는 데 실패했습니다. 직접 앱 권한을 확인해주세요.", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Health Connect에서 오늘 하루 동안의 걸음 수 데이터를 읽어와 업데이트
     */
    suspend fun readStepsByTimeRange() {
        val healthClient = _healthConnectClient.value ?: return
        val endTime = LocalDateTime.now()
        val startTime = LocalDateTime.of(endTime.toLocalDate(), LocalTime.MIDNIGHT)

        try {
            // 주어진 시간 범위 내의 걸음 수 데이터를 조회
            val response = healthClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            // 첫 번째 기록의 걸음 수를 LiveData에 반영 (데이터가 없으면 "0")
            _totalSteps.postValue(response.records.firstOrNull()?.count?.toString() ?: "0")
        } catch (e: Exception) {
            Log.e("Total Steps", "Error fetching step count", e)
            _totalSteps.postValue("0")
        }
    }
}
