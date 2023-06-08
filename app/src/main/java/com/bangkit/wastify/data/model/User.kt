package com.bangkit.wastify.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    val imageUrl: String? = null
): Parcelable
