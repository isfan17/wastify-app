package com.bangkit.wastify.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class Result(
    @PrimaryKey
    val imageUrl: String,
    val name: String,
    val percentage: Double,
    val typeId: String,
    val categoryId: String,
    val recyclable: Boolean,
    val createdAt: String
)