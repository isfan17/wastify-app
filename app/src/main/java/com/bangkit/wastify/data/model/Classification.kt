package com.bangkit.wastify.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Classification(
    val name: String = "",
    val percentage: Double = 0.0
): Parcelable
