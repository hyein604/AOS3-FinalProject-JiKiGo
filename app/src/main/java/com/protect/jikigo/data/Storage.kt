package com.protect.jikigo.data

import com.protect.jikigo.R

object Storage {
    val storeList: List<Store> = getStoreData()
    val notiList: List<String> = getNotiData()

    private fun getStoreData(): List<Store> {
        return listOf(
            Store(R.drawable.img_qr, "장세훈", "천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안", "000-111-111111"),
            Store(R.drawable.img_qr, "박상원", "동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해", "000-222-222222"),
            Store(R.drawable.img_qr, "김태경", "서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울", "000-333-333333"),
            Store(R.drawable.img_qr, "김혜인", "울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산", "000-444-444444"),
            Store(R.drawable.img_qr, "정지석", "천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안", "000-555-555555"),
        )
    }

    private fun getNotiData(): List<String> {
        return listOf(
            "도미노피자 제휴업체 선정",
            "지키GO 환경부 선정 올해의 환경지킴이앱 선정",
            "장세훈, \'정세훈\'으로 이름 헷갈려 서러움 기념 이벤트🎉"
        )

    }
}

data class Store(
    val thumbNailImg: Int,
    val title: String,
    val address: String,
    val number: String
)

