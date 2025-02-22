package com.protect.jikigo.data.repo

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.protect.jikigo.data.model.UserInfo
import com.protect.jikigo.ui.extensions.getUserId
import com.protect.jikigo.ui.extensions.saveUserId
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class UserRepo @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context,
) {
    suspend fun getUserInfo(id: String): UserInfo? {
        return try {
            val db = FirebaseFirestore.getInstance()
            val document = db.collection("UserInfo").document(id).get(Source.CACHE).await() // 캐시 우선 사용
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
}