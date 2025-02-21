package com.protect.jikigo.data

import android.os.Parcelable
import com.protect.jikigo.R
import kotlinx.parcelize.Parcelize

object Storage {
    val storeList: List<Store> = getStoreData()
    val coupon: List<Coupon> = getCouponData()
    val pointHistory: List<PointHistory> = getPointHistoryData()
    // val rankingUser: List<RankingUser> = getRankingUserData()
    val findAccount: List<String> = getFindAccountText()

    private fun getStoreData(): List<Store> {
        return listOf(
            Store(R.drawable.img_qr, "장세훈", "천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안", "000-111-111111"),
            Store(R.drawable.img_qr, "박상원", "동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해 동해", "000-222-222222"),
            Store(R.drawable.img_qr, "김태경", "서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울 서울", "000-333-333333"),
            Store(R.drawable.img_qr, "김혜인", "울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산 울산", "000-444-444444"),
            Store(R.drawable.img_qr, "정지석", "천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안 천안", "000-555-555555"),
        )
    }

    private fun getCouponData(): List<Coupon> {
        return listOf(
            Coupon("여행용품", "세훈님 드라이브 쿠폰1", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 100),
            Coupon("여행용품", "상원님 게임 쿠폰2", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 99),
            Coupon("숙박", "혜인님 야근 쿠폰3", 20000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 98),
            Coupon("레저/티켓", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 97),
            Coupon("여행용품", "세훈님 드라이브 쿠폰1", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 1),
            Coupon("레저/티켓", "상원님 게임 쿠폰5", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 5),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰7", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 7),
            Coupon("숙박", "세훈님 드라이브 쿠폰9", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 9),
            Coupon("레저/티켓", "상원님 게임 쿠폰8", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 8),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 4),
            Coupon("여행용품", "세훈님 드라이브 쿠폰1", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 1),
            Coupon("여행용품", "상원님 게임 쿠폰2", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 2),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("레저/티켓", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 4),
            Coupon("여행용품", "세훈님 드라이브 쿠폰1", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 1),
            Coupon("레저/티켓", "상원님 게임 쿠폰5", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 5),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰7", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 7),
            Coupon("숙박", "세훈님 드라이브 쿠폰9", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 9),
            Coupon("레저/티켓", "상원님 게임 쿠폰8", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 8),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 4),
            Coupon("여행용품", "세훈님 드라이브 쿠폰1", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 1),
            Coupon("여행용품", "상원님 게임 쿠폰2", 40000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 2),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("레저/티켓", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 4),
            Coupon("여행용품", "세훈님 드라이브 쿠폰1", 300000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 1),
            Coupon("레저/티켓", "상원님 게임 쿠폰5", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 5),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰7", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 7),
            Coupon("숙박", "세훈님 드라이브 쿠폰9", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 9),
            Coupon("레저/티켓", "상원님 게임 쿠폰8", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 8),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 4),
            Coupon("공연/전시", "세훈님 드라이브 쿠폰1", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 1),
            Coupon("여행용품", "상원님 게임 쿠폰2", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 2),
            Coupon("공연/전시", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("레저/티켓", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 4),
            Coupon("여행용품", "세훈님 드라이브 쿠폰1", 200000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 1),
            Coupon("레저/티켓", "상원님 게임 쿠폰5", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 5),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰7", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 7),
            Coupon("숙박", "세훈님 드라이브 쿠폰9", 100000, "지키고", "2025-03-02 까지", R.drawable.img_bus, 9),
            Coupon("레저/티켓", "상원님 게임 쿠폰8", 50000, "메이플스토리", "2025-03-02 까지", R.drawable.img_earth, 8),
            Coupon("숙박", "혜인님 야근 쿠폰3", 200000, "지키고", "2025-02-18 까지", R.drawable.img_calendar, 3),
            Coupon("공연/전시", "지석님 맛집 탐방 쿠폰4", 500000, "지키고", "2025-03-02 까지", R.drawable.img_store, 4),
        )
    }

    private fun getPointHistoryData(): List<PointHistory> {
        return listOf(
            PointHistory("-10500P", "커피 쿠폰 구매"),
            PointHistory("-200000P", "야근 쿠폰 구매"),
            PointHistory("-500000P", "맛집 탐방 쿠폰 구매"),
        )
    }

//    private fun getRankingUserData(): List<RankingUser> {
//        return listOf(
//            RankingUser("천안지렁이라이더", "https://cdn.mhnse.com/news/photo/202409/320798_361970_3115.jpg", 18379),
//            RankingUser("구황작물", "https://wimg.heraldcorp.com/content/default/2024/04/12/20240412050389_0.jpg", 182098),
//            RankingUser("감자무밨니", "https://i.namu.wiki/i/YTjOUCgGyUl7yza1MSfmi_RWJpkY4I2qflSY3NAp529euJDGe5hl1mneeHSTlBhz1LaQ2lwM2LohlguvqRdH7A.webp", 142408),
//            RankingUser("돌멩이", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-f-556Uod9bhUGfm-mvScCjEMn5Kx3AhinQ&s", 9865),
//            RankingUser("울산안경도둑", "https://image.ajunews.com/content/image/2023/05/15/20230515143901746182.jpg", 24309),
//            RankingUser("윈터아마멘터", "https://image.ajunews.com/content/image/2023/05/15/20230515143901746182.jpg", 1093)
//        ).sortedByDescending {
//            it.walkCount
//        }
//    }


    private fun getFindAccountText(): List<String> {
        return listOf(
            "아이디 찾기", "비밀번호 찾기"
        )
    }
}



data class Store(
    val thumbNailImg: Int,
    val title: String,
    val address: String,
    val number: String
)

@Parcelize
data class Coupon(
    val category: String,
    val name: String,
    val price: Int,
    val brand: String,
    val date: String,
    val image: Int,
    val salesCount: Int
) : Parcelable

data class PointHistory(
    val point: String,
    val description: String,
)

data class NewsResponse(
    val items: List<NewsItem>
)

@Parcelize
data class NewsItem(
    val title: String,
    val originallink: String,
    val link: String,
    val description: String,
    val pubDate: String,
    var imageUrl: String? = null
): Parcelable
