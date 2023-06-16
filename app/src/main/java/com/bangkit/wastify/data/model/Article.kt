package com.bangkit.wastify.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "articles")
@Parcelize
data class Article(
    @PrimaryKey
    var id: String,
    @DrawableRes val image: Int,
    val title: String,
    val source: String,
    val publishedAt: String,
    val description: String
): Parcelable
