package com.protect.jikigo.ui.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.textfield.TextInputLayout
import com.protect.jikigo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

// textView 속성을 넣어주는 함수
fun TextView.applySpannableStyles(
    startPos: Int,
    lastPos: Int,
    colorResId: Int? = null,
    isBold: Boolean = false,
    isUnderlined: Boolean = false
) {
    val spannableBuilder = SpannableStringBuilder(this.text)

    colorResId?.let {
        val color = ContextCompat.getColor(this.context, it)
        spannableBuilder.setSpan(
            ForegroundColorSpan(color),
            startPos, lastPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    if (isBold) {
        spannableBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            startPos, lastPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    if (isUnderlined) {
        spannableBuilder.setSpan(
            UnderlineSpan(),
            startPos, lastPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    this.text = spannableBuilder
}

// 상태바 색상 변경
fun Context.statusBarColor(colorResId: Int) {
    val window = (this as? Activity)?.window ?: return
    window.statusBarColor = ContextCompat.getColor(this, colorResId)
}

// 포인트의 패턴을 적용
fun TextView.applyNumberFormat(amount: Int) {
    text = amount.convertThreeDigitComma()
}

fun Number.convertThreeDigitComma(): String {
    val decimalFormat = DecimalFormat("#,###P")
    return decimalFormat.format(this)
}

// 토스트 메시지
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Button.setTimer(lifecycleScope: LifecycleCoroutineScope, editText: TextInputLayout, context: Context) {
    this.isEnabled = false
    editText.isEnabled = false
    lifecycleScope.launch(Dispatchers.Main) {
        val totalTime = 180
        for (i in totalTime downTo 0) {
            val minutes = i / 60
            val seconds = i % 60
            this@setTimer.text = String.format("%d:%02d", minutes, seconds)
            delay(1000) // 1초 대기
        }
        this@setTimer.text = context.getString(R.string.common_auth_request)
        this@setTimer.isEnabled = true
        editText.isEnabled = true
    }
}

fun TextView.setTimer(lifecycleScope: LifecycleCoroutineScope, context: Context, imgView: ImageView) {
    imgView.isEnabled = false
    lifecycleScope.launch(Dispatchers.Main) {
        val totalTime = 180
        for (i in totalTime downTo 0) {
            val minutes = i / 60
            val seconds = i % 60
            this@setTimer.text = String.format("%d:%02d", minutes, seconds)
            delay(1000) // 1초 대기
        }
        this@setTimer.text = context.getString(R.string.payment_qr_refresh)
        this@setTimer.isEnabled = true
        imgView.isEnabled = true
    }
}

// 다이얼로그 띄우기
fun Context.showDialog(
    title: String, msg: String, pos: String, nega: String,
    onResult: (Boolean) -> Unit

) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
        .setMessage(msg)
        .setPositiveButton(pos) { dialog, _ ->
            dialog.dismiss()
            onResult(true)
        }
        .setNegativeButton(nega) { dialog, _ ->
            dialog.dismiss()
            onResult(false)
        }
    val alertDialog = builder.create()
    alertDialog.show()
}

