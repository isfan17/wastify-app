package com.bangkit.wastify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.model.User

@Database(
    entities = [
        User::class,
        Category::class,
        Article::class,
        Type::class
    ],
    version = 1
)
abstract class WastifyDatabase: RoomDatabase() {
    abstract fun getWastifyDao(): WastifyDao
}