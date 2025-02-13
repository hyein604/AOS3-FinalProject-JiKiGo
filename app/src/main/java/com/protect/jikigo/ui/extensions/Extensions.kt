package com.protect.jikigo.ui.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.text.DecimalFormat

// textView 속성을 넣어주는 함수
fun TextView.spannable(underLine: Int, colorLine: Int, boldLine: Int, color: Int) {
    val spannableBuilder = SpannableStringBuilder(this.text)

    spannableBuilder.setSpan(
        UnderlineSpan(),
        0, underLine, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // 색상 적용
    spannableBuilder.setSpan(
        ForegroundColorSpan(color),
        0, colorLine, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Bold 적용
    spannableBuilder.setSpan(
        StyleSpan(Typeface.BOLD),
        0, boldLine,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )


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