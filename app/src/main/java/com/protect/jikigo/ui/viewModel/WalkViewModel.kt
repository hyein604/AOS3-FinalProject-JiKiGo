package com.protect.jikigo.ui.viewModel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.repo.WalkRewardBottomSheetRepo
import com.protect.jikigo.utils.getUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class WalkViewModel @Inject constructor(
    application: Application,
    private val firestore: FirebaseFirestore,
    private val walkRewardBottomSheetRepo: WalkRewardBottomSheetRepo
) : AndroidViewModel(application) {

    private val sharedPreferences: SharedPreferences =
        application.getSharedPreferences("walk_prefs", Context.MODE_PRIVATE)

    // LiveData 변수
    private val _totalSteps = MutableLiveData<String>()
    val totalSteps: LiveData<String> = _totalSteps

    private val _weeklySteps = MutableLiveData<Int>()
    val weeklySteps: LiveData<Int> = _weeklySteps

    private val _healthConnectClient = MutableLiveData<HealthConnectClient?>()
    val healthConnectClient: LiveData<HealthConnectClient?> = _healthConnectClient

    private val _requestPermissions = MutableLiveData<Boolean>()
    val requestPermissions: LiveData<Boolean> = _requestPermissions

    private val _currentGoal = MutableLiveData(loadGoal())
    val currentGoal: LiveData<Int> get() = _currentGoal

    private val _currentReward = MutableLiveData(loadReward())
    val currentReward: LiveData<Int> get() = _currentReward

    // Health Connect 권한 설정
    private val permission = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

    init {
        // 하루가 지났다면 걸음수 보상 단계 초기화
        if (shouldResetDaily()) {
            resetDailyProgress()
        }

        viewModelScope.launch {
            checkAndInitHealthClient(getApplication<Application>().applicationContext)
            updateWeeklySteps()
        }
    }

    /** 주간 걸음 수 업데이트 */
    internal suspend fun updateWeeklySteps() {
        val healthClient = _healthConnectClient.value ?: return
        val endTime = LocalDateTime.now()

        val today = Calendar.getInstance()
        val dayOfWeek = today.get(Calendar.DAY_OF_WEEK)
        val daysSinceMonday = if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - Calendar.MONDAY
        val startTime = endTime.minusDays(daysSinceMonday.toLong()).toLocalDate().atStartOfDay()

        try {
            val response = healthClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )

            val weeklySteps = response.records.sumOf { it.count.toInt() }
            _weeklySteps.postValue(weeklySteps)
            updateWeeklyStepsInFirestore(weeklySteps)
        } catch (e: Exception) {
            Log.e("Weekly Steps", "Error fetching weekly step count", e)
        }
    }

    /** Firestore에 주간 걸음 수 저장 */
    private suspend fun updateWeeklyStepsInFirestore(weeklySteps: Int) {
        val userId = getApplication<Application>().applicationContext.getUserId() ?: return

        firestore.collection("UserInfo")
            .document(userId)
            .update("userStepWeekly", weeklySteps)
            .addOnSuccessListener {
                Log.d("Firestore", "Weekly steps updated: $weeklySteps")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating weekly steps", e)
            }
    }

    /** Health Connect 클라이언트 초기화 및 권한 확인 */
    private fun checkAndInitHealthClient(context: Context) {
        checkInstallHC(context)
        _healthConnectClient.observeForever {
            if (it != null) {
                viewModelScope.launch {
                    readStepsByTimeRange()
                }
            }
        }
    }

    /** Google Health Connect 앱 설치 확인 및 클라이언트 초기화 */
    fun checkInstallHC(context: Context) {
        val providerPackageName = "com.google.android.apps.healthdata"
        val availabilityStatus = HealthConnectClient.getSdkStatus(context, providerPackageName)

        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE ||
            availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
        ) {
            Toast.makeText(context, "헬스 커넥트 앱을 설치 또는 업데이트 해주세요", Toast.LENGTH_SHORT).show()
            openPlayStoreForHealthConnect(context)
            return
        }

        val healthClient = HealthConnectClient.getOrCreate(context)
        _healthConnectClient.value = healthClient

        viewModelScope.launch {
            val grantedPermissions = healthClient.permissionController.getGrantedPermissions()
            if (!grantedPermissions.containsAll(permission)) {
                _requestPermissions.value = true
                movePermissionSetting(context)
            } else {
                readStepsByTimeRange()
            }
        }
    }

    /** Google Play 스토어에서 Health Connect 앱 설치 화면 열기 */
    fun openPlayStoreForHealthConnect(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")
            setPackage("com.android.vending")
        }
        context.startActivity(intent)
    }

    /** 사용자를 권한 설정 화면으로 이동 */
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

    /** 오늘 하루 걸음 수 가져오기 */
    suspend fun readStepsByTimeRange() {
        if (_healthConnectClient.value == null) {
            checkInstallHC(getApplication<Application>().applicationContext)
            return
        }

        val healthClient = _healthConnectClient.value ?: return
        val endTime = LocalDateTime.now()
        val startTime = LocalDateTime.of(endTime.toLocalDate(), LocalTime.MIDNIGHT)

        try {
            val response = healthClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
                )
            )
            _totalSteps.postValue(response.records.firstOrNull()?.count?.toString() ?: "0")
        } catch (e: Exception) {
            Log.e("Total Steps", "Error fetching step count", e)
            _totalSteps.postValue("0")
        }
    }

    /** 목표 및 보상 관리 */
    fun moveToNextGoal() {
        when (_currentGoal.value) {
            5000 -> setGoalAndReward(10000, 20)
            10000 -> setGoalAndReward(20000, 30)
        }
    }

    private fun setGoalAndReward(goal: Int, reward: Int) {
        _currentGoal.value = goal
        _currentReward.value = reward
        saveGoal(goal)
        saveReward(reward)
    }

    /** 하루가 지나면 데이터 초기화 */
    private fun shouldResetDaily(): Boolean {
        val lastResetTime = sharedPreferences.getLong("last_reset_time", 0L)
        val currentTime = System.currentTimeMillis()

        val lastResetCalendar = Calendar.getInstance().apply { timeInMillis = lastResetTime }
        val currentCalendar = Calendar.getInstance().apply { timeInMillis = currentTime }

        return lastResetTime == 0L ||
                lastResetCalendar.get(Calendar.DAY_OF_YEAR) != currentCalendar.get(Calendar.DAY_OF_YEAR)
    }

    /** 하루가 지나면 걸음 목표 및 보상을 초기화 */
    private fun resetDailyProgress() {
        sharedPreferences.edit()
            .putInt("current_goal", 5000)
            .putInt("current_reward", 10)
            .putBoolean("final_reward_claimed", false)
            .putLong("last_reset_time", System.currentTimeMillis())
            .apply()

        _currentGoal.postValue(5000)
        _currentReward.postValue(10)
    }

    /** 목표 걸음 수를 SharedPreferences에 저장 */
    private fun saveGoal(goal: Int) {
        sharedPreferences.edit().putInt("current_goal", goal).apply()
    }

    /** 보상을 SharedPreferences에 저장 */
    private fun saveReward(reward: Int) {
        sharedPreferences.edit().putInt("current_reward", reward).apply()
    }

    /** SharedPreferences에서 목표 걸음 수 불러오기 (기본값: 5000) */
    private fun loadGoal(): Int {
        return sharedPreferences.getInt("current_goal", 5000)
    }

    /** SharedPreferences에서 보상 불러오기 (기본값: 10) */
    private fun loadReward(): Int {
        return sharedPreferences.getInt("current_reward", 10)
    }


    /** 보상 지급 */
    fun setRankingRewardPoint(userId: String, reward: Int) {
        viewModelScope.launch {
            walkRewardBottomSheetRepo.setWalkRewardBottomSheetHistory(userId, reward)
        }
    }
}