package com.protect.jikigo.ui.home.my_page

import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.protect.jikigo.R
import com.protect.jikigo.databinding.FragmentCouponDetailBinding
import com.protect.jikigo.utils.statusBarColor

class CouponDetailFragment : Fragment() {
    private var _binding: FragmentCouponDetailBinding? = null
    private val binding get() = _binding!!
    private val args: CouponDetailFragmentArgs by navArgs()
    private val WHITE: Int = 0xFFFFFFFF.toInt()
    private val BLACK: Int = 0xFF000000.toInt()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCouponDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBar()
        setLayout()
    }

    private fun setStatusBar() {
        requireActivity().statusBarColor(R.color.white)
    }

    private fun setLayout() {
        onClickToolbar()
        setText()
    }

    private fun onClickToolbar() {
        binding.toolbarCouponDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setText() {
        binding.apply {
            tvCouponDetailName.text = args.couponArg.purchasedCouponName
            tvCouponDetailClient.text = args.couponArg.purchasedCouponBrand
            when(args.couponArg.purchasedCouponStatus) {
                0 -> {
                    tvCouponDetailDate.text = "유효 기간 : ${args.couponArg.purchasedCouponValidDays}"
                }
                1 -> {
                    tvCouponDetailDate.text = "${args.couponArg.purchasedCouponUsedDate} 사용 완료"
                    viewCouponDetailBlur.isVisible = true
                    ivCouponDetailBarcode.isVisible = false
                }
                2 -> {
                    tvCouponDetailDate.text = "${args.couponArg.purchasedCouponValidDays} 만료됨"
                    viewCouponDetailBlur.isVisible = true
                    ivCouponDetailBarcode.isVisible = false
                }
            }
            Glide.with(requireContext())
                .load(args.couponArg.purchasedCouponImage)
                .into(ivCouponDetailImage)
            val barcode = createBarcode(args.couponArg.purchasedCouponBarCode)
            Glide.with(requireContext())
                .load(barcode)
                .into(ivCouponDetailBarcode)
        }
    }

    private fun createBarcode(code: String) : Bitmap {
        val widthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 390f,
            resources.displayMetrics
        )
        val heightPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 111f,
            resources.displayMetrics
        )
        val format: BarcodeFormat = BarcodeFormat.CODE_128
        val matrix: BitMatrix = MultiFormatWriter().encode(code, format, widthPx.toInt(), heightPx.toInt())
        val bitmap = createBitmap(matrix)
        return bitmap
    }

    private fun createBitmap(matrix: BitMatrix): Bitmap {
        val width = matrix.width
        val height = matrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (matrix.get(x, y)) BLACK else WHITE)
            }
        }
        return bitmap
    }
}