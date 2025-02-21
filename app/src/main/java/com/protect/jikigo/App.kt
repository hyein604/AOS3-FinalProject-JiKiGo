package com.protect.jikigo

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kakao")

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase 초기화
        FirebaseApp.initializeApp(this)
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        Log.d("KAKAO", "${Utility.getKeyHash(this)}")
    }

    companion object {
        private val USER_ID = stringPreferencesKey("KAKAO")
        suspend fun setUserId(context: Context, userId: String) {
            context.dataStore.edit { preferences ->
                preferences[USER_ID] = userId
            }
        }

        fun getUserId(context: Context): Flow<String?> {
            return context.dataStore.data.map { preferences ->
                preferences[USER_ID]
            }
        }
    }
}