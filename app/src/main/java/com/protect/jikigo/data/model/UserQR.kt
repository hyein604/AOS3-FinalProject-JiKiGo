package com.protect.jikigo.data.model

data class UserQR(
    // 사용자 고유 ID
    val userDocId: String = "",
    val userQRUUID: String = "",
    val userQRUse: Boolean = false,
    val userQrDocId: String = ""
)