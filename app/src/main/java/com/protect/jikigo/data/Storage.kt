package com.protect.jikigo.data

import android.os.Parcelable
import com.protect.jikigo.R
import kotlinx.parcelize.Parcelize

object Storage {
    val storeList: List<Store> = getStoreData()
    val notiList: List<String> = getNotiData()
    val coupon: List<Coupon> = getCouponData()
    val pointHistory: List<PointHistory> = getPointHistoryData()
    val rankingUser: List<RankingUser> = getRankingUserData()

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

    private fun getCouponData(): List<Coupon> {
        return listOf(
            Coupon("여행용품", "세훈님 드라이브 쿠폰", "100,000원", "지키고", "2025-03-02 까지", R.drawable.img_bus),
            Coupon("레저/티켓", "상원님 게임 쿠폰", "50,000원", "메이플스토리", "2025-03-02 까지", R.drawable.img_earth),
            Coupon("숙박", "혜인님 야근 쿠폰", "200,000원", "지키고", "2025-02-18 까지", R.drawable.img_calendar),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰", "500,000원", "지키고", "2025-03-02 까지", R.drawable.img_store),
        )
    }

    private fun getPointHistoryData(): List<PointHistory> {
        return listOf(
            PointHistory("-10500P", "커피 쿠폰 구매"),
            PointHistory("-200000P", "야근 쿠폰 구매"),
            PointHistory("-500000P", "맛집 탐방 쿠폰 구매"),
        )
    }

    private fun getRankingUserData(): List<RankingUser> {
        return listOf(
            RankingUser("천안지렁이라이더", "https://cdn.mhnse.com/news/photo/202409/320798_361970_3115.jpg", 18379),
            RankingUser("구황작물", "https://wimg.heraldcorp.com/content/default/2024/04/12/20240412050389_0.jpg",182098),
            RankingUser("감자무밨니", "https://i.namu.wiki/i/YTjOUCgGyUl7yza1MSfmi_RWJpkY4I2qflSY3NAp529euJDGe5hl1mneeHSTlBhz1LaQ2lwM2LohlguvqRdH7A.webp",142408),
            RankingUser("돌멩이", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-f-556Uod9bhUGfm-mvScCjEMn5Kx3AhinQ&s", 9865),
            RankingUser("울산안경도둑", "https://image.ajunews.com/content/image/2023/05/15/20230515143901746182.jpg", 24309),
            RankingUser("윈터아마멘터", "https://image.ajunews.com/content/image/2023/05/15/20230515143901746182.jpg", 1093)
        ).sortedByDescending { it.walkCount }
    }
}

data class RankingUser(
    val name: String,
    val profilePicture: String,
    val walkCount: Int,
)

data class Store(
    val thumbNailImg: Int,
    val title: String,
    val address: String,
    val number: String
)


data class Notification(
    val title: String,
    val date: String
)

@Parcelize
data class Coupon(
    val category: String,
    val name: String,
    val price: String,
    val brand: String,
    val date: String,
    val image: Int,
) : Parcelable

data class PointHistory(
    val point: String,
    val description: String,
)

