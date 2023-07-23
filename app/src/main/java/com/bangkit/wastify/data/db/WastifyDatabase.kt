package com.bangkit.wastify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bangkit.wastify.data.db.dao.ArticleDao
import com.bangkit.wastify.data.db.dao.CategoryDao
import com.bangkit.wastify.data.db.dao.DisposalMethodDao
import com.bangkit.wastify.data.db.dao.ResultDao
import com.bangkit.wastify.data.db.dao.TypeDao
import com.bangkit.wastify.data.db.dao.UserDao
import com.bangkit.wastify.data.db.entities.ArticleEntity
import com.bangkit.wastify.data.db.entities.CategoryEntity
import com.bangkit.wastify.data.db.entities.DisposalMethodEntity
import com.bangkit.wastify.data.db.entities.ResultEntity
import com.bangkit.wastify.data.db.entities.TypeEntity
import com.bangkit.wastify.data.db.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TypeEntity::class,
        CategoryEntity::class,
        ArticleEntity::class,
        DisposalMethodEntity::class,
        ResultEntity::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class WastifyDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getTypeDao(): TypeDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getDisposalMethodDao(): DisposalMethodDao
    abstract fun getArticleDao(): ArticleDao
    abstract fun getResultDao(): ResultDao
}