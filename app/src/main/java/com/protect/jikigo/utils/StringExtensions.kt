package com.protect.jikigo.utils

import android.os.Build
import android.text.Html
import org.jsoup.Jsoup

// 확장 함수
fun String.cleanHtml(): String {
    // 1. HTML 태그 제거
    val noHtml = Jsoup.parse(this).text()

    // 2. HTML 엔터티 디코딩 (&quot; -> ")
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(noHtml, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(noHtml).toString()
    }
}
