package com.protect.jikigo.ui.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.protect.jikigo.ui.extensions.DataStoreKeys.USER_ID
import com.protect.jikigo.ui.extensions.DataStoreKeys.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/*
* 사용자 정보 저장을 위한 데이터스토어 정의
* 사용방법: suspend함수이므로 suspend함수 또는 launch{} 내부에서 호출해야함
* context의 확장함수로 정의했기때문에 Fragment에서는 requireContext()쓰삼
* activity에서는 this(생략) 쓰삼*/
object DataStoreKeys {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kakao")
    val USER_ID = stringPreferencesKey("KAKAO")
}

// userId 저장
suspend fun Context.saveUserId(userId: String) {
    dataStore.edit { preferences ->
        preferences[USER_ID] = userId
    }
}

// userId 호출
suspend fun Context.getUserId(): String? {
    return dataStore.data.map { preferences ->
        preferences[USER_ID]?.takeIf { it.isNotEmpty() }
    }.first()
}

// userId 제거
suspend fun Context.clearUserId() {
    dataStore.edit { preferences ->
        preferences.clear()
    }
}