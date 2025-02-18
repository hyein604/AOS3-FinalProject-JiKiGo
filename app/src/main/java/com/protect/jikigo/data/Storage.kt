package com.protect.jikigo.data

import android.os.Parcelable
import com.protect.jikigo.R
import kotlinx.parcelize.Parcelize

object Storage {
    val storeList: List<Store> = getStoreData()
    val notificationList: List<Notification> = getNotificationData()
    val coupon: List<Coupon> = getCouponData()
    val pointHistory: List<PointHistory> = getPointHistoryData()
    val rankingUser: List<RankingUser> = getRankingUserData()
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

    private fun getNotificationData(): List<Notification> {
        return listOf(
            Notification(
                "장세훈, '정세훈' 으로 이름 헷갈려 서러움 기념 이벤트\uD83C\uDF89", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "이번에 저희 팀원이 조장님의 이름을 헷갈렸습니다.\n" +
                        "성함이 장세훈 이신데 팀원분이 정세훈이라고 하셨어요.\n" +
                        "\n" +
                        "충격받으신 조장님이 기념으로 이벤트를 하나 기획 하셨습니다!\n" +
                        "\n" +
                        "무려 전 회원 10만 포인트 증정 이벤트입니다.\n" +
                        "단 사전 이벤트 신청하신 분만 해당됩니다.\n" +
                        "많이많이 참여해주세요~\n" +
                        "\n" +
                        "죄송합니다",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTI4exT4h_yL8_DEigEIJObPdii4lJcZ12JRA&s"
            ),

            Notification(
                "도미노피자 제휴업체 선정", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "여러분의 최애 음식은 무엇인가요?\n" +
                        "저의 최애 음식은 많은데요 그 중 하나는 피자입니다.\n" +
                        "\n" +
                        "도미노피자 제휴업체 맺어놨으니깐 피자 많이 드세요.\n" +
                        "전 진짜 많이 먹을거예요\n" +
                        "행복해여라~",
                "https://cdn.dominos.co.kr/admin/upload/goods/20240326_Td6eyIV8.jpg?RS=350x350&SP=1"
            ),

            Notification(
                "지키GO 환경부 선정 올해의 환경지킴이앱 선정", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "지키고가 환경부픽을 받았습니다\n" +
                        "지원금 달달하군요\n" +
                        "\n" +
                        "환경지킴이앱에 선정된건 모두 이용자분들 덕분입니다.\n" +
                        "저희가 만든 앱 잘 활용해 주셔서 감사해요.\n" +
                        "I love you..."
            ),

            Notification(
                "공지사항 1", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "검색 기능 테스트용 공지사항 1 입니다."
            ),

            Notification(
                "중요공지", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "검색 기능 테스트용 중요공지 입니다."
            ),

            Notification(
                "새로운 업데이트", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "검색 기능 테스트용 새로운 업데이트 입니다."
            )
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

    private fun getRankingUserData(): List<RankingUser> {
        return listOf(
            RankingUser("천안지렁이라이더", "https://cdn.mhnse.com/news/photo/202409/320798_361970_3115.jpg", 18379),
            RankingUser("구황작물", "https://wimg.heraldcorp.com/content/default/2024/04/12/20240412050389_0.jpg", 182098),
            RankingUser("감자무밨니", "https://i.namu.wiki/i/YTjOUCgGyUl7yza1MSfmi_RWJpkY4I2qflSY3NAp529euJDGe5hl1mneeHSTlBhz1LaQ2lwM2LohlguvqRdH7A.webp", 142408),
            RankingUser("돌멩이", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-f-556Uod9bhUGfm-mvScCjEMn5Kx3AhinQ&s", 9865),
            RankingUser("울산안경도둑", "https://image.ajunews.com/content/image/2023/05/15/20230515143901746182.jpg", 24309),
            RankingUser("윈터아마멘터", "https://image.ajunews.com/content/image/2023/05/15/20230515143901746182.jpg", 1093)
        ).sortedByDescending {
            it.walkCount
        }
    }


    private fun getFindAccountText(): List<String> {
        return listOf(
            "아이디 찾기", "비밀번호 찾기"
        )
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

@Parcelize
data class Notification(
    val title: String,
    val date: String,
    val content: String,
    val image: String? = null
) : Parcelable

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
    val image: String? = null
): Parcelable
