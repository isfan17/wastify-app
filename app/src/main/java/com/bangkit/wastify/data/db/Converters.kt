package com.bangkit.wastify.data.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(string: String): List<String> {
        return string.split("**")
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString("**")
    }
}