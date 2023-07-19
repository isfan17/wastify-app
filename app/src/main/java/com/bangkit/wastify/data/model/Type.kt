package com.bangkit.wastify.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Type(
    val id: String,
    val icon: String,
    val image: String,
    val name: String,
    val description: String,
): Parcelable
