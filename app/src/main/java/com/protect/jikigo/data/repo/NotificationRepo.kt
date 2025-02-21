package com.protect.jikigo.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.protect.jikigo.data.model.Notification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
){
    fun getMySavedPosts(): List<Notification> {
        // 테스트용 더미 데이터 생성
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
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTI4exT4h_yL8_DEigEIJObPdii4lJcZ12JRA&s",
                false
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
                "https://cdn.dominos.co.kr/admin/upload/goods/20240326_Td6eyIV8.jpg?RS=350x350&SP=1",
                false
            ),

            Notification(
                "지키GO 환경부 선정 올해의 환경지킴이앱 선정", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "지키고가 환경부픽을 받았습니다\n" +
                        "지원금 달달하군요\n" +
                        "\n" +
                        "환경지킴이앱에 선정된건 모두 이용자분들 덕분입니다.\n" +
                        "저희가 만든 앱 잘 활용해 주셔서 감사해요.\n" +
                        "I love you...",
                null,
                false
            ),

            Notification(
                "공지사항 1", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "검색 기능 테스트용 공지사항 1 입니다.",
                null,
                false
            ),

            Notification(
                "중요공지", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "검색 기능 테스트용 중요공지 입니다.",
                null,
                false
            ),

            Notification(
                "새로운 업데이트", "2025.2.7",
                "안녕하세요 지키GO 운영자 입니다.\n" +
                        "검색 기능 테스트용 새로운 업데이트 입니다.",
                null,
                false
            )
        )
    }
}