package com.bangkit.wastify.di

import android.content.Context
import androidx.room.Room
import com.bangkit.wastify.data.db.WastifyDatabase
import com.bangkit.wastify.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Singleton
    @Provides
    fun provideWastifyDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        WastifyDatabase::class.java,
        Constants.WASTIFY_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: WastifyDatabase) = db.getUserDao()

    @Singleton
    @Provides
    fun provideTypeDao(db: WastifyDatabase) = db.getTypeDao()

    @Singleton
    @Provides
    fun provideCategoryDao(db: WastifyDatabase) = db.getCategoryDao()

    @Singleton
    @Provides
    fun provideArticleDao(db: WastifyDatabase) = db.getArticleDao()

    @Singleton
    @Provides
    fun provideDisposalMethodDao(db: WastifyDatabase) = db.getDisposalMethodDao()

    @Singleton
    @Provides
    fun provideResultDao(db: WastifyDatabase) = db.getResultDao()
}