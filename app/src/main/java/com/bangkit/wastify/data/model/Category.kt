package com.bangkit.wastify.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var id: String? = null,
    @DrawableRes val icon: Int,
    @DrawableRes val image: Int,
    val name: String,
    val description: String
): Parcelable
