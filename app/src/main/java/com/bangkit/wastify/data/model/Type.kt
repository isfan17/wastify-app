package com.bangkit.wastify.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bangkit.wastify.data.db.Converters

@Entity(tableName = "types")
data class Type(
    @PrimaryKey
    var id: String,
    val icon: String,
    val image: String,
    val name: String,
    val description: String,
    @TypeConverters(Converters::class)
    val categories: List<String>
)
