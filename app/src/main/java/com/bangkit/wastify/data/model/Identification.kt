package com.bangkit.wastify.data.model

import android.graphics.Bitmap

data class Identification (
    val image: Bitmap,
    val result: String,
    val percentage: Double,
    val typeId: String,
    val categoryId: String,
    val recyclable: Boolean
)