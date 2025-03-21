package com.protect.jikigo.ui.payment

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.protect.jikigo.R
import com.protect.jikigo.data.model.UserQR
import com.protect.jikigo.databinding.FragmentPaymentQrBinding
import com.protect.jikigo.ui.activity.HomeActivity
import com.protect.jikigo.utils.applyNumberFormat
import com.protect.jikigo.utils.getUserId
import com.protect.jikigo.utils.setTimerCallBack
import com.protect.jikigo.utils.showDialog
import com.protect.jikigo.utils.showDialogOkAndCancel
import com.protect.jikigo.utils.statusBarColor
import com.protect.jikigo.ui.viewModel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class PaymentQRFragment : Fragment() {
    private var _binding: FragmentPaymentQrBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PaymentViewModel by viewModels()
    private var timerJob: Job? = null
    private lateinit var userId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserId()
        setLayout()
    }

    private fun setLayout() {
        setStatusBarColor()
        onClickBack()
        onClickToolbar()
        showDialog()
        setQRCode()
        getUserPoint()
        moveToTravel()
        getUserPointError()
        setTimer()
    }

    // 뒤로가기
    private fun onClickBack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setClearRealDB()
                findNavController().popBackStack()
            }
        })
    }

    private fun initUserId() {
        viewLifecycleOwner.lifecycleScope.launch {
            userId = requireContext().getUserId()!!
            Log.d("initUserId", userId)
        }
    }

    private fun getUserPoint() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getUserPoint(requireContext().getUserId()!!)
            viewModel.userPoint.observe(viewLifecycleOwner) {
                binding.tvPaymentPoint.applyNumberFormat(it ?: 0)
            }
        }
    }

    private fun getUserPointError() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userQR.observe(viewLifecycleOwner) {
                viewModel.getPointError(it)
            }
            viewModel.userPointError.observe(viewLifecycleOwner) {
                if (it == "포인트가 부족합니다.") {
                    requireContext().showDialog(
                        title = "결제 확인",
                        msg = it,
                        pos = "확인",
                    ) { result ->
                        if (result) {
                            // 확인
                            setClearRealDB()
                            setQRCode()
                        }
                    }
                } else if (it == "해당 QR은 현장결제 전용입니다.") {
                    requireContext().showDialog(
                        title = "결제 확인",
                        msg = it,
                        pos = "확인",
                    ) { result ->
                        if (result) {
                            // 확인
                            setClearRealDB()
                            setQRCode()
                        }
                    }
                } else if (it == "이미 사용 된 QR코드 입니다.") {
                    requireContext().showDialog(
                        title = "결제 확인",
                        msg = it,
                        pos = "확인",
                    ) { result ->
                        if (result) {
                            // 확인
                            setClearRealDB()
                            setQRCode()
                        }
                    }
                } else if (it == "정상적으로 결제되었습니다.") {
                    requireContext().showDialog(
                        title = "결제 확인",
                        msg = it,
                        pos = "확인",
                    ) { result ->
                        if (result) {
                            // 확인
                            //  setPaymentHistoryDB()
                            setClearRealDB()
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    // 결제 됐을 때 realDB내려야함 결제 완료 다이얼로그 확인 눌렀을 때
    private fun setClearRealDB() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userQR.observe(viewLifecycleOwner) {
                viewModel.clearDB(it)
            }
        }
    }


    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.payment_background)
    }

    private fun onClickToolbar() {
        binding.toolbarPayment.setNavigationOnClickListener {
            setClearRealDB()
            findNavController().navigateUp()
        }
    }

    private fun showDialog() {
        binding.ivQrHelp.setOnClickListener {
            val action = PaymentQRFragmentDirections.actionPaymentQRToPaymentQRDialog()
            findNavController().navigate(action)
        }
    }

    private fun moveToTravel() {
        binding.viewQrUse.setOnClickListener {
            requireContext().showDialogOkAndCancel(
                title = "사용처",
                msg = "사용처 화면으로 이동합니다",
                pos = "이동",
                nega = "취소"
            ) { result ->
                if (result) {
                    findNavController().popBackStack(findNavController().graph.startDestinationId, false)
                    val homeActivity = requireActivity() as HomeActivity
                    homeActivity.selectedTravelNavigation()
                    setClearRealDB()
                }
            }
        }
    }

    private fun setTimer() {
        binding.ivQrRefresh.setOnClickListener {
            setQRCode()
        }
    }

    private fun setQRCode() {
        lifecycleScope.launch {
            timerJob?.cancelAndJoin()

            timerJob = binding.tvQrTime.setTimerCallBack(
                lifecycleScope,
                requireContext(),
                binding.ivQrRefresh,
                binding.ivPaymentQr,
            )
            {
                setClearRealDB()
            }
            // data: UUID, userDocID, userPoint
            val transactionId = UUID.randomUUID().toString() // 고유 트랜잭션 ID 생성

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.userPoint.observe(viewLifecycleOwner) {
                    Log.d("userPoint", "$it")
                    val userData = UserQR(
                        userId = userId,
                        userPoint = it,
                        userQR = transactionId,
                        userQrError = "",
                    )
                    viewModel.setUserPaymentData(userData)
                    val qrJson = Gson().toJson(userData)

                    binding.ivPaymentQr.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val width = binding.ivPaymentQr.measuredWidth
                            val height = binding.ivPaymentQr.measuredHeight

                            if (width > 0 && height > 0) {
                                val img = createQRCode(qrJson, width, height)
                                Glide.with(requireContext())
                                    .load(img)
                                    .into(binding.ivPaymentQr)
                                // 리스너 제거 (메모리 누수 방지)
                                binding.ivPaymentQr.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        }
                    })
                }
            }
        }
    }

    private fun createQRCode(data: String, width: Int, height: Int): Bitmap? {
        val writer = QRCodeWriter()
        return try {
            val bitMatrix: BitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}