package com.bangkit.wastify.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disposal_methods")
data class DisposalMethodEntity(
    @PrimaryKey
    val id: String,
    val method: String,
    val categoryId: String,
)