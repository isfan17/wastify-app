package com.bangkit.wastify.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.bangkit.wastify.data.db.WasteDatabase
import com.bangkit.wastify.data.repositories.auth.AuthRepository
import com.bangkit.wastify.data.repositories.auth.AuthRepositoryImpl
import com.bangkit.wastify.data.repositories.waste.WasteRepository
import com.bangkit.wastify.data.repositories.waste.WasteRepositoryImpl
import com.bangkit.wastify.utils.Constants.KEY_FIRST_TIME_TOGGLE
import com.bangkit.wastify.utils.Constants.SHARED_PREFERENCES_NAME
import com.bangkit.wastify.utils.Constants.WASTE_DATABASE_NAME
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideWasteRepository(impl: WasteRepositoryImpl): WasteRepository = impl

    @Singleton
    @Provides
    fun provideWasteDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        WasteDatabase::class.java,
        WASTE_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideWasteDao(db: WasteDatabase) = db.getWasteDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}