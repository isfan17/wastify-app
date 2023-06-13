package com.bangkit.wastify.data.model

import android.graphics.Bitmap

data class Classification (
    val image: Bitmap,
    val result: String,
    val percentage: Double,
    val typeId: Int,
    val categoryId: Int,
    val recyclable: Boolean
)