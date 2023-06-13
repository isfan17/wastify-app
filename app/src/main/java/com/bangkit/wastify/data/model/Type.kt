package com.bangkit.wastify.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "types")
@Parcelize
data class Type(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @DrawableRes val icon: Int,
    @DrawableRes val image: Int,
    val name: String,
    val description: String
): Parcelable
