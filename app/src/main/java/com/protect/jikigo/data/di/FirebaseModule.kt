package com.protect.jikigo.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.protect.jikigo.data.repo.CouponRepo
import com.protect.jikigo.data.repo.StoreRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage =
        FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFireDatabase(): FirebaseDatabase =
        FirebaseDatabase.getInstance()

    @Provides
    @Singleton
    fun provideCouponRepo(firestore: FirebaseFirestore): CouponRepo {
        return CouponRepo(firestore)
    }

    @Provides
    @Singleton
    fun provideStoreRepo(firestore: FirebaseFirestore): StoreRepo {
        return StoreRepo(firestore)
    }


}