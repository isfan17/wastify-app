package com.bangkit.wastify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.wastify.data.model.Category

@Database(
    entities = [Category::class],
    version = 1
)
abstract class WasteDatabase: RoomDatabase() {
    abstract fun getWasteDao(): WasteDao
}