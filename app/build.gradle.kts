import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.protect.jikigo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.protect.jikigo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders += mapOf(
            "KAKAO_NATIVE_APP_KEY" to properties.getProperty("KAKAO_NATIVE_APP_KEY"),
            "NAVER_CLIENT_ID" to properties.getProperty("NAVER_CLIENT_ID"),
            "NAVER_CLIENT_SECRET" to properties.getProperty("NAVER_CLIENT_SECRET"),
            "NAVER_CLIENT_NEWS_ID" to properties.getProperty("NAVER_CLIENT_NEWS_ID"),
            "NAVER_CLIENT_NEWS_SECRET" to properties.getProperty("NAVER_CLIENT_NEWS_SECRET"),
        )

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            "\"${properties.getProperty("KAKAO_NATIVE_APP_KEY")}\""
        )

        // 네이버 관련 BuildConfig 추가
        buildConfigField(
            "String",
            "NAVER_CLIENT_ID",
            "\"${properties.getProperty("NAVER_CLIENT_ID")}\""
        )

        buildConfigField(
            "String",
            "NAVER_CLIENT_SECRET",
            "\"${properties.getProperty("NAVER_CLIENT_SECRET")}\""
        )

        buildConfigField(
            "String",
            "NAVER_CLIENT_NEWS_ID",
            "\"${properties.getProperty("NAVER_CLIENT_NEWS_ID")}\""
        )

        buildConfigField(
            "String",
            "NAVER_CLIENT_NEWS_SECRET",
            "\"${properties.getProperty("NAVER_CLIENT_NEWS_SECRET")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.navigation:navigation-fragment:2.6.0")
    implementation("androidx.navigation:navigation-ui:2.6.0")
    // 뷰페이저 점 인디케이터
    implementation("me.relex:circleindicator:2.1.6")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Retrofit 및 OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Jsoup
    implementation("org.jsoup:jsoup:1.15.3")

    // QR코드
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore:25.1.1")
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-appcheck-safetynet:16.1.2")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation("com.google.firebase:firebase-database")

    // hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // kakao
    implementation("com.kakao.sdk:v2-all:2.20.1") // 전체 모듈 설치, 2.11.0 버전부터 지원

    // data store
    implementation("androidx.datastore:datastore-preferences:1.1.2")
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.1.2")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.1.2")
    implementation("androidx.datastore:datastore-preferences-core:1.1.2")

    // health
    implementation("androidx.health.connect:connect-client:1.1.0-alpha10")

    // naver
    implementation("com.navercorp.nid:oauth:5.10.0") // jdk 11
  //  implementation(files("libs/oauth-5.10.0.aar"))
  
    implementation ("androidx.work:work-runtime-ktx:2.9.0")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    // splash
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")

}


kapt {
    correctErrorTypes = true
}