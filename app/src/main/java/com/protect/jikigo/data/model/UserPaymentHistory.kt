package com.protect.jikigo.data.model

data class UserPaymentHistory(
    val docId: String = "",
    val reason: String = "",
    val payName: String = "",
    val amount: Int = 0,
    val paymentDate: String = "",// = getCurrentFormattedDate(), // 현재 날짜를 String으로 저장,
    val payType: String = "" // 사용 또는 적립
)