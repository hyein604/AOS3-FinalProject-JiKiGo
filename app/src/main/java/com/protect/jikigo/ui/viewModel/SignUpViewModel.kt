package com.protect.jikigo.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.repo.UserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepo: UserRepo
) : ViewModel() {

    val name = MutableLiveData("")
    val mobile = MutableLiveData("")
    val authNumber = MutableLiveData("")
    val id = MutableLiveData("")
    val pw = MutableLiveData("")
    val pwChk = MutableLiveData("")
    val nickName = MutableLiveData("")

    // 에러 메시지 LiveData
    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?> = _nameError

    private val _mobileError = MutableLiveData<String?>()
    val mobileError: LiveData<String?> = _mobileError

    private val _authNumberError = MutableLiveData<String?>()
    val authNumberError: LiveData<String?> = _authNumberError

    private val _idError = MutableLiveData<String?>()
    val idError: LiveData<String?> = _idError

    private val _pwError = MutableLiveData<String?>()
    val pwError: LiveData<String?> = _pwError

    private val _pwChkError = MutableLiveData<String?>()
    val pwChkError: LiveData<String?> = _pwChkError

    private val _nickNameError = MutableLiveData<String?>()
    val nickNameError: LiveData<String?> = _nickNameError

    // 중복 확인
    private val _nicknameResult = MutableLiveData<Pair<Boolean, String>>()
    val nicknameResult: LiveData<Pair<Boolean, String>> get() = _nicknameResult

    private val _idResult = MutableLiveData<Pair<Boolean, String>>()
    val idResult: LiveData<Pair<Boolean, String>> get() = _idResult

    // 버튼 상태
    private val _isIdCheckBtnEnabled = MutableLiveData(false)
    val isIdCheckBtnEnabled: LiveData<Boolean> = _isIdCheckBtnEnabled

    private val _isSignUpBtnEnabled = MutableLiveData(false)
    val isSignUpBtnEnabled: LiveData<Boolean> = _isSignUpBtnEnabled

    private var isIdChecked = false
    private var isNicknameChecked = false
    private var isAuthNumberChecked = false

    fun validateName() {
        val value = name.value.orEmpty()
        _nameError.value = when {
            value.isEmpty() -> "이름을 입력해주세요."
            value.length !in 2..8 -> "이름은 최소2, 최대8자 입니다."
            !value.matches(Regex("^[가-힣a-zA-Z]+$")) -> "이름에는 특수문자나 숫자를 포함할 수 없습니다."
            else -> null
        }
    }

    fun validateMobile() {
        val value = mobile.value.orEmpty()
        _mobileError.value = when {
            value.isEmpty() -> "휴대전화를 입력해주세요."
            !value.matches(Regex("^[0-9]{11}$")) -> "휴대전화 번호는 (\'-\'하이픈 제외) 11자 입니다"
            else -> null
        }
    }

    fun validateAuthNumber() {
        val value = authNumber.value.orEmpty()
        _authNumberError.value = when {
            value.isEmpty() -> "인증번호를 입력해주세요."
            !value.matches(Regex("^[0-9]{6}$")) -> "인증번호는 6자 입니다."
            else -> null
        }
    }

    fun validateId() {
        val value = id.value.orEmpty()
        _idError.value = when {
            value.isEmpty() -> null
            value.length !in 8..16 -> "아이디는 최소 8자, 최대 16자 입니다."
            else -> null
        }
    }

    fun validatePw() {
        val value = pw.value.orEmpty()
        _pwError.value = when {
            value.isEmpty() -> null
            value.length !in 14..20 -> "비밀번호는 최소 14자, 최대20자 입니다"
            else -> null
        }
    }

    fun validatePwChk() {
        val value = pwChk.value.orEmpty()
        _pwChkError.value = when {
            value.isEmpty() -> null
            value != pw.value -> "비밀번호가 일치하지 않습니다."
            else -> null
        }
    }

    fun validateNickName() {
        val value = nickName.value.orEmpty()
        _nickNameError.value = when {
            value.isEmpty() -> null
            value.length !in 2..10 -> "닉네임은 최소2, 최대10자 입니다."
            else -> null
        }
    }

    // db 저장
    fun registerUserInfo(callback: (Boolean, String?) -> Unit) {
        val userInfo = UserInfo(
            userName = name.value.orEmpty(),
            userMobile = mobile.value.orEmpty(),
            userId = id.value.orEmpty(),
            userPw = pw.value.orEmpty(),
            userNickName = nickName.value.orEmpty()

        )
        userRepo.addUserInfo(userInfo, callback)
    }

    // 모든 입력값과 중복 확인 완료 여부에 따라 가입 버튼 활성화 여부 결정
    fun validateSignUpButton() {
        _isSignUpBtnEnabled.value = name.value.orEmpty().isNotEmpty() &&
                id.value.orEmpty().isNotEmpty() &&
                nickName.value.orEmpty().isNotEmpty() &&
                pw.value.orEmpty().length >= 8 &&
                pwChk.value == pw.value &&
                mobile.value.orEmpty().length == 11 &&
                authNumber.value.orEmpty().length == 6 &&
                isIdChecked && isNicknameChecked && isAuthNumberChecked
    }


    // 아이디 중복 확인
    fun checkedId(id: String) {
        viewModelScope.launch {
            try {
                val trimmedId = id.trim().lowercase()
                val available = userRepo.isIdAvailable(trimmedId)

                val msg = if (available) {
                    isIdChecked = true
                    "사용 가능한 아이디입니다."
                } else {
                    isIdChecked = false
                    "이미 사용 중인 아이디입니다."
                }
                _idResult.value = Pair(available, msg)
                validateSignUpButton() // 가입 버튼 상태 업데이트
            } catch (e: Exception) {
                _idResult.value = Pair(false, "아이디 중복 확인 중 오류가 발생했습니다.")
            }
        }
    }


    // 입력 필드 상태에 따라 중복 확인 버튼 활성화 여부 결정
    fun validateIdCheckButton() {
        val value = id.value.orEmpty()
        _isIdCheckBtnEnabled.value = value.length >= 6
        // 아이디 변경 시 중복 확인 다시 필요
        isIdChecked = false
    }
}