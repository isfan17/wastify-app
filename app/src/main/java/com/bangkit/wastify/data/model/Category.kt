package com.bangkit.wastify.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bangkit.wastify.data.db.Converters

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String,
    val icon: String,
    val image: String,
    val name: String,
    val description: String,
    @TypeConverters(Converters::class)
    val disposalMethods: List<String>
)