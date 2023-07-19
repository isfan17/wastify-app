package com.bangkit.wastify.di

import android.content.Context
import com.bangkit.wastify.data.repositories.auth.AuthRepository
import com.bangkit.wastify.data.repositories.auth.AuthRepositoryImpl
import com.bangkit.wastify.data.repositories.identify.IdentifyRepository
import com.bangkit.wastify.data.repositories.identify.IdentifyRepositoryImpl
import com.bangkit.wastify.data.repositories.main.MainRepository
import com.bangkit.wastify.data.repositories.main.MainRepositoryImpl
import com.bangkit.wastify.data.repositories.user.UserRepository
import com.bangkit.wastify.data.repositories.user.UserRepositoryImpl
import com.bangkit.wastify.ml.WastifyModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideMainRepository(impl: MainRepositoryImpl): MainRepository = impl

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    @Singleton
    fun provideIdentifyRepository(impl: IdentifyRepositoryImpl): IdentifyRepository = impl

    @Singleton
    @Provides
    fun provideWastifyModel(@ApplicationContext app: Context) = WastifyModel.newInstance(app)
}