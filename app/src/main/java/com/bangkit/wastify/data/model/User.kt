package com.bangkit.wastify.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Entity(tableName = "users")
@Parcelize
data class User(
    @PrimaryKey
    var id: String,
    val name: String,
    val email: String,
    var imageUrl: String? = null
): Parcelable
