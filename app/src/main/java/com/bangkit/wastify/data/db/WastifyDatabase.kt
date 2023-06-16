package com.bangkit.wastify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.data.model.Result

@Database(
    entities = [
        User::class,
        Type::class,
        Category::class,
        Article::class,
        Result::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class WastifyDatabase: RoomDatabase() {
    abstract fun getWastifyDao(): WastifyDao
}