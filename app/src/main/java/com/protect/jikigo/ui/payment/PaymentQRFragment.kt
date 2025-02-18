package com.protect.jikigo.ui.payment

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentPaymentQrBinding
import com.protect.jikigo.ui.extensions.applyNumberFormat
import com.protect.jikigo.ui.extensions.setTimer
import com.protect.jikigo.ui.extensions.showDialog
import com.protect.jikigo.ui.extensions.statusBarColor
import java.util.UUID

class PaymentQRFragment : Fragment() {
    private var _binding: FragmentPaymentQrBinding? = null
    private val binding get() = _binding!!
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
        setLayout()
    }

    private fun setLayout() {
        setStatusBarColor()
        onClickToolbar()
        showDialog()
        setUserPoint()
        moveToTravel()
        setTimer()
    }

    private fun setStatusBarColor() {
        requireActivity().statusBarColor(R.color.payment_background)
    }

    private fun onClickToolbar() {
        binding.toolbarPayment.setNavigationOnClickListener {
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
            requireContext().showDialog(
                title = "사용처",
                msg = "사용처 화면으로 이동합니다",
                pos = "이동",
                nega = "취소"
            ) { result ->
                if (result) {
                    val action = PaymentQRFragmentDirections.actionPaymentQRToNavigationTravel()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun setUserPoint() {
        binding.tvPaymentPoint.applyNumberFormat(3456)
    }

    private fun setTimer() {
        binding.ivQrRefresh.setOnClickListener {
            setQRCode()
        }
    }

    private fun setQRCode() {
        binding.tvQrTime.setTimer(lifecycleScope, requireContext(), binding.ivQrRefresh)
        // data: UUID, userDocID, userPoint
        val transactionId = UUID.randomUUID().toString() // 고유 트랜잭션 ID 생성


        val userData = mapOf(
            "transactionId" to transactionId,
            "userDocId" to "zzangse",
            "userPoint" to 3456
        )
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