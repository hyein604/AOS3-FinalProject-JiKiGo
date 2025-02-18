package com.protect.jikigo.ui.travel

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentTravelPaymentBottomSheetBinding
import com.protect.jikigo.ui.extensions.applyNumberFormat
import com.protect.jikigo.ui.extensions.applySpannableStyles


class TravelPaymentBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentTravelPaymentBottomSheetBinding? = null
    private val binding get() = _binding!!

    // 보유 포인트
    private val userPoints = 11000
    // 상품 가격
    private val productPrice = 10000

    // 체크상태
    private var isAgreementChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelPaymentBottomSheetBinding.inflate(inflater, container, false)
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
        // 보유 포인트와 상품 가격을 비교하여 처리
        if (userPoints < productPrice) {
            // 포인트 부족한 경우
            showInsufficientPoints()
        } else {
            // 포인트가 충분한 경우
            showSufficientPoints()
        }

        onClickBuyBtn()
        applyAgreementLink()
    }

    private fun showInsufficientPoints() {
        val requiredPoints = productPrice - userPoints

        // 포인트 부족 시 필요한 포인트를 표시
        binding.tvTravelPaymentSheetRequiredPointLabel.isVisible = true
        binding.tvTravelPaymentSheetRequiredPoint.isVisible = true
        binding.tvTravelPaymentSheetRequiredPoint.applyNumberFormat(requiredPoints)

        // 사용 포인트, 남은 포인트 비활성화
        binding.tvTravelPaymentSheetUsedPointLabel.isVisible = false
        binding.tvTravelPaymentSheetRemainingPointLabel.isVisible = false
        binding.tvTravelPaymentSheetUsedPoint.isVisible = false
        binding.tvTravelPaymentSheetRemainingPoint.isVisible = false

        // 보유 포인트와 필요한 포인트 표시
        binding.tvTravelPaymentSheetInsufficientPoint.applyNumberFormat(userPoints)

        // 결제 버튼 비활성화
        binding.btnTravelPaymentComplete.isEnabled = false
        binding.btnTravelPaymentComplete.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_10))

        // 약관 관련 UI 숨김
        binding.ivbTravelPaymentAgree.isVisible = false
        binding.tvTravelPaymentAgreeContent.isVisible = false
    }

    private fun showSufficientPoints() {
        val usedPoints = -productPrice
        val remainingPoints = userPoints - productPrice

        // 보유 포인트, 사용 포인트, 남은 포인트 표시
        binding.tvTravelPaymentSheetInsufficientPoint.applyNumberFormat(userPoints)
        binding.tvTravelPaymentSheetUsedPoint.applyNumberFormat(usedPoints)
        binding.tvTravelPaymentSheetRemainingPoint.applyNumberFormat(remainingPoints)

        // 약관 동의 체크박스와 약관 내용 보이기
        binding.ivbTravelPaymentAgree.isVisible = true
        binding.tvTravelPaymentAgreeContent.isVisible = true

        // 결제 버튼 활성화
        binding.btnTravelPaymentComplete.isEnabled = false  // 기본적으로 비활성화
        binding.btnTravelPaymentComplete.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray_10))

        isAgreementChecked = false
        binding.ivbTravelPaymentAgree.setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray_10))

        // 약관 동의 체크박스 클릭 리스너
        binding.ivbTravelPaymentAgree.setOnClickListener {
            toggleAgreementCheck()
        }

        // 약관 동의 텍스트 클릭 리스너
        binding.tvTravelPaymentAgreeContent.setOnClickListener {
            toggleAgreementCheck()
        }
    }

    // 약관 동의 상태 변경 함수
    private fun toggleAgreementCheck() {
        // 체크 상태 변경
        isAgreementChecked = !isAgreementChecked

        // 이미지 버튼 색상 변경
        val checkColor = if (isAgreementChecked) {
            R.color.primary
        } else {
            R.color.gray_10
        }

        // 이미지 버튼 색상 변경
        binding.ivbTravelPaymentAgree.setColorFilter(ContextCompat.getColor(requireContext(), checkColor))

        // 결제 버튼 활성화 여부 변경
        binding.btnTravelPaymentComplete.isEnabled = isAgreementChecked

        // 결제 버튼 색상 변경
        val buttonColor = if (isAgreementChecked) {
            R.color.primary
        } else {
            R.color.gray_10
        }
        binding.btnTravelPaymentComplete.setBackgroundColor(ContextCompat.getColor(requireContext(), buttonColor))
    }

    private fun applyAgreementLink() {
        val fullText = "쿠폰 정보를 확인 하였으며, 지키GO 서비스 이용약관에 동의합니다."
        val start = fullText.indexOf("지키GO 서비스 이용약관")
        val end = start + "지키GO 서비스 이용약관".length

        val spannable = SpannableStringBuilder(fullText)

        binding.tvTravelPaymentAgreeContent.applySpannableStyles(
            startPos = start,
            lastPos = end,
            colorResId = R.color.black,
            isBold = true,
            isUnderlined = true
        )

        // 클릭 이벤트 추가
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // 약관 화면 이동
                // 클릭 시 처리할 이벤트
                Toast.makeText(context, "이용약관 클릭", Toast.LENGTH_SHORT).show()
            }

            // 클릭한 텍스트의 색상 변경 (파란색 대신 검정색으로)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ContextCompat.getColor(context!!, R.color.black)
                ds.isUnderlineText = true
                ds.bgColor = ContextCompat.getColor(context!!, R.color.white )
            }
        }

        // '지키GO 서비스 이용약관' 부분에 클릭 이벤트 적용
        spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 텍스트를 TextView에 설정
        binding.tvTravelPaymentAgreeContent.text = spannable

        // LinkMovementMethod로 클릭 이벤트를 처리할 수 있도록 설정
        binding.tvTravelPaymentAgreeContent.movementMethod = LinkMovementMethod.getInstance()

        binding.tvTravelPaymentAgreeContent.setOnClickListener {
            if (!binding.tvTravelPaymentAgreeContent.hasSelection()) { // 약관이 선택되지 않은 경우만 체크박스 변경
                toggleAgreementCheck()
            }
        }
    }

    private fun onClickBuyBtn() {
        binding.btnTravelPaymentComplete.setOnClickListener {
            val action = TravelPaymentBottomSheetFragmentDirections.actionTravelPaymentBottomSheetToTravelPaymentComplete()
            findNavController().navigate(action)
        }
    }

}