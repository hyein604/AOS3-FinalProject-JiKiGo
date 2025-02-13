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