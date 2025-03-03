package com.protect.jikigo.ui.reward

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
import com.protect.jikigo.databinding.FragmentTumblrConfirmBinding
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.extensions.setTimerCallBack
import com.protect.jikigo.ui.extensions.showDialog
import com.protect.jikigo.ui.extensions.statusBarColor
import com.protect.jikigo.ui.viewModel.TumblerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.util.UUID

@AndroidEntryPoint
class TumblrConfirmFragment : Fragment() {
    private var _binding: FragmentTumblrConfirmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TumblerViewModel by viewModels()
    private lateinit var userId: String
    private var timerJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTumblrConfirmBinding.inflate(inflater, container, false)
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
        initUserId()

        onClickBack()
        onClickToolbar()

        setQRCode()
        getUserPoint()

        getUserPointError()
        setTimer()

        binding.cardTumblrConfirm.setBackgroundResource(R.drawable.card_reward_shape)
    }

    private fun getUserPoint() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getUserPoint(requireContext().getUserId()!!)
        }
    }

    private fun initUserId() {
        viewLifecycleOwner.lifecycleScope.launch {
            userId = requireContext().getUserId()!!
        }
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

    private fun onClickToolbar() {
        binding.toolbarTumblrConfirm.setNavigationOnClickListener {
            setClearRealDB()
            findNavController().navigateUp()
        }
    }

    // 텀블러 인증 때 realDB내려야함 결제 완료 다이얼로그 확인 눌렀을 때
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
                binding.ivTumblrConfirmQr,
            ) {
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
                        payName = "텀블러 인증(적립)",
                        userQrError = "",
                    )
                    viewModel.setUserPaymentData(userData)
                    val qrJson = Gson().toJson(userData)

                    binding.ivTumblrConfirmQr.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            val width = binding.ivTumblrConfirmQr.measuredWidth
                            val height = binding.ivTumblrConfirmQr.measuredHeight

                            if (width > 0 && height > 0) {
                                val img = createQRCode(qrJson, width, height)
                                Glide.with(requireContext())
                                    .load(img)
                                    .into(binding.ivTumblrConfirmQr)
                                // 리스너 제거 (메모리 누수 방지)
                                binding.ivTumblrConfirmQr.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            }
                        }
                    })
                }
            }
        }
    }

    private fun getUserPointError() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userQR.observe(viewLifecycleOwner) {
                viewModel.getPointError(it)
            }
            viewModel.userPointError.observe(viewLifecycleOwner) {
                if (it == "해당 QR은 텀블러 인증 전용 입니다.") {
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
                } else if (it == "텀블러 인증 완료") {
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