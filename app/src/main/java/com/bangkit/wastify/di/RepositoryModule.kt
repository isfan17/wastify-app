package com.bangkit.wastify.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bangkit.wastify.data.db.WastifyDao
import com.bangkit.wastify.data.db.WastifyDatabase
import com.bangkit.wastify.data.repositories.auth.AuthRepository
import com.bangkit.wastify.data.repositories.auth.AuthRepositoryImpl
import com.bangkit.wastify.data.repositories.main.MainRepository
import com.bangkit.wastify.data.repositories.main.MainRepositoryImpl
import com.bangkit.wastify.ml.WastifyModel
import com.bangkit.wastify.utils.Constants
import com.bangkit.wastify.utils.DummyDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
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

    @Singleton
    @Provides
    fun provideWastifyDatabase(
        @ApplicationContext app: Context,
        provider: Provider<WastifyDao>
    ) = Room.databaseBuilder(app, WastifyDatabase::class.java, Constants.WASTIFY_DATABASE_NAME)
        .addCallback(object : RoomDatabase.Callback() {
            private val applicationScope = CoroutineScope(SupervisorJob())

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                applicationScope.launch(Dispatchers.IO) {
                    populateDatabase()
                }
            }

            private suspend fun populateDatabase() {
                val categories = DummyDataSource.getCategories()
                val articles = DummyDataSource.getArticles()
                val types = DummyDataSource.getTypes()
                provider.get().insertCategories(categories)
                provider.get().insertArticles(articles)
                provider.get().insertTypes(types)
            }
        })
        .build()

    @Singleton
    @Provides
    fun provideWastifyDao(db: WastifyDatabase) = db.getWastifyDao()

    @Singleton
    @Provides
    fun provideWastifyModel(@ApplicationContext app: Context) = WastifyModel.newInstance(app)
}