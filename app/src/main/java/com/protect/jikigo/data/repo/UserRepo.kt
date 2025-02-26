package com.protect.jikigo.data.repo

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.data.model.UserQR
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.extensions.saveUserId
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Singleton
class UserRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val realTime: FirebaseDatabase,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context,
) {
    suspend fun getUserInfo(id: String): UserInfo? {
        return try {
            val db = FirebaseFirestore.getInstance() // 캐시 먼저 쓰면 값을 못 가져옴..
            val document = db.collection("UserInfo").document(id).get().await()//.get(Source.CACHE).await() // 캐시 우선 사용
                ?: db.collection("UserInfo").document(id).get().await() // 네트워크에서 가져오기

            if (document.exists()) {
                document.toObject(UserInfo::class.java)?.also {
                    Log.d("UserRepo", "UserInfo loaded successfully for id: $id")
                }
            } else {
                Log.e("UserRepo", "UserInfo not found for id: $id")
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepo", "Error fetching user info: ${e.message}", e)
            null
        }
    }


    // 아이디 중복 확인
    suspend fun isIdAvailable(id: String): Boolean {
        return try {
            val snapshot = firestore
                .collection("UserInfo")
                .whereEqualTo("userId", id)
                .get()
                .await()
            snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    // 닉네임 중복 확인
    suspend fun isNickNameAvailable(nickName: String): Boolean {
        return try {
            val snapshot = firestore
                .collection("UserInfo")
                .whereEqualTo("userNickName", nickName)
                .get()
                .await()
            snapshot.isEmpty
        } catch (e: Exception) {
            Log.d("isNickNameAvailable Error isNickNameAvailable: ${e.message}", e.toString())
            false
        }
    }

    suspend fun saveUser() {
        try {
            val user = runCatching { UserApiClient.instance.me().getOrThrow() }.getOrNull()!!
            val kakaoAccount = user.kakaoAccount!!

            // null 체크 후 기본값 설정
            val name = kakaoAccount.name ?: "이름 없음"
            val phone = kakaoAccount.phoneNumber ?: "000-0000-0000"
            val email = kakaoAccount.email ?: "jikigo@jikigo.com"
            val nickname = kakaoAccount.profile?.nickname ?: "닉네임 없음"
            val profileImg = kakaoAccount.profile?.thumbnailImageUrl ?: ""

            // Firestore 문서 ID로 userDocId 사용
            val userInfo = createUserInfo(name, phone, email, nickname, profileImg)
            Log.d("SaveUser", "생성된 UserInfo 객체: $userInfo")

            val userDocId = userInfo.userDocId

            // Firestore 문서 ID로 userDocId 사용하여 저장
            val userDocRef = firestore.collection("UserInfo").document(userDocId)
            Log.d("SaveUser", "Firestore에 저장할 문서 ID: $userDocId")

            val documentSnapshot = userDocRef.get().await()
            if (documentSnapshot.exists()) {
                Log.d("SaveUser", "이미 존재하는 사용자: $userDocId")
            } else {
                userDocRef.set(userInfo).await()
                Log.d("SaveUser", "새 사용자 저장 성공: $userDocId")
            }

            // DataStore에 userId 저장
            context.saveUserId(userInfo.userId)
            Log.d("SaveUser", "DataStore에 저장된 userId: ${context.getUserId()}")

        } catch (e: Exception) {
            Log.e("SaveUser", "Firestore 저장 중 오류 발생: ${e.message}", e)
        }
    }


    // realTimeDB에 qr 고유코드로 문서를 만든다
    fun setUserPaymentData(userQR: UserQR) {
        val document = firestore.collection("UserInfo").document(userQR.userId)
        val realDB = realTime.getReference("UserInfo").child("userQR")  // userQR 노드에 저장

        Log.d("setUserPaymentData", "${userQR.userPoint}")
        Log.d("setUserPaymentData", userQR.userId)
        Log.d("setUserPaymentData", userQR.userQR)

        document.update("userQR", userQR)
            .addOnSuccessListener {
                realDB.child(userQR.userQR).setValue(userQR)  // 사용자별 데이터 저장
                    .addOnSuccessListener {
                        Log.d("Firebase", "Realtime Database에 userQR 저장 성공")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firebase", "Realtime Database 저장 실패", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Firestore 업데이트 실패", e)
            }
    }


    // 결제기록 제거 (테스트용)
    private fun deleteHistory(userQR: UserQR) {
        Log.d("setPaymentHistory", "userQR: ${userQR}")
        val document = firestore.collection("UserInfo").document(userQR.userId)

        // 현재 날짜를 YYYY-MM-DD 형식으로 가져오기
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val paymentHistoryRef =
            document.collection("Calendar")
                .document(currentDate)
                .collection("PaymentHistory")

        paymentHistoryRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    doc.reference.delete()
                }
            }
    }

    // 결제내역
    private fun setPaymentHistory(userQR: UserQR) {
        Log.d("setPaymentHistory", "userQR: ${userQR}")
        val document = firestore.collection("UserInfo").document(userQR.userId)

        // 현재 날짜를 YYYY-MM-DD 형식으로 가져오기
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val paymentHistoryRef =
            document.collection("Calendar")
                .document(currentDate)
                .collection("PaymentHistory")


        // 새 문서를 추가하고 ID를 가져오기
        val newPaymentDocRef = paymentHistoryRef.document() // 랜덤 문서 ID 생성


        val paymentData = hashMapOf(
            "docId" to newPaymentDocRef.id,
            "reason" to "지키고 페이",
            "amount" to userQR.paymentPrice,
            "paymentDate" to userQR.paymentDate,
            "payType" to userQR.payType,
            "payName" to userQR.payName,
        )

        newPaymentDocRef.set(paymentData)
            .addOnSuccessListener {
                Log.d("PaymentHistory", "결제 저장")
            }
            .addOnFailureListener {
                Log.d("PaymentHistory", "결제 실패")
            }

        // subCollection에 오늘 날짜 ex) 2025-02-25 라는 문서이름을 만들고 문서에 값을 넣어주고싶어
    }

    fun getPointError(userQR: UserQR, callback: (String) -> Unit) {
        //  val document = firestore.collection("UserInfo").document(userQR.userId)
        val realDB = realTime.getReference("UserInfo").child("userQR").child(userQR.userQR)

        realDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val qrData = snapshot.getValue(UserQR::class.java)
                    Log.d("TEST", "실시간 업데이트 감지 됨: ${qrData?.userQrError}")
                    val error = qrData?.userQrError.toString()
                    callback(error)
                    // UI 업데이트 또는 알림 처리
                } else {
                    Log.d("TEST", "userQR 데이터 없음")
                    callback("userQR no DATA")
                }
            }

            override fun onCancelled(e: DatabaseError) {
                Log.e("TEST", "데이터 감지 실패: ${e.message}")
                callback("onCancelled")
            }

        })
    }


    // Kakao SDK의 콜백 기반 API를 suspend 함수로 변환
    private suspend fun UserApiClient.me(): Result<User> = suspendCoroutine { continuation ->
        this.me { user, error ->
            when {
                error != null -> continuation.resume(Result.failure(error))
                user != null -> continuation.resume(Result.success(user))
                else -> continuation.resume(Result.failure(IllegalStateException("User and error are both null")))
            }
        }
    }

    private fun createUserInfo(name: String, phoneNumber: String, email: String, nickName: String, profileImg: String) =
        UserInfo(
            userDocId = email,
            userName = name,
            userMobile = phoneNumber,
            userId = email,
            userNickName = nickName,
            userProfileImg = profileImg,
        )

    fun clearRealDB(userQR: UserQR, callback: (Boolean) -> Unit) {
        val realDB = realTime.getReference("UserInfo").child("userQR").child(userQR.userQR)
        val firestoreDoc = firestore.collection("UserInfo").document(userQR.userId)

        realDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val qrData = snapshot.getValue(UserQR::class.java)
                    qrData?.let { userQR ->
                        // Firestore에 저장
                        firestoreDoc.update("userPoint", userQR.userPoint)
                        firestoreDoc.update("userQR", userQR)
                            .addOnSuccessListener {
                                // Realtime DB에서 데이터 삭제
                                realDB.removeValue()
                                    .addOnSuccessListener {
                                        setPaymentHistory(userQR)
                                        callback(true)
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firebase", "Realtime DB 데이터 삭제 실패", e)
                                        callback(false)
                                    }
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firebase", "Firestore 업데이트 실패", e)
                                callback(false)
                            }
                    }
                } else {
                    Log.d("Firebase", "Realtime DB에 데이터가 없습니다")
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "데이터 읽기 실패: ${error.message}")
                callback(false)
            }
        })
    }
}

