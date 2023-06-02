package com.bangkit.wastify.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    var id: String? = null,
    @DrawableRes val image: Int,
    val title: String,
    val source: String,
    val publishedAt: String,
    val description: String
): Parcelable
