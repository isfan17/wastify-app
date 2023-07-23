package com.bangkit.wastify.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import com.bangkit.wastify.data.db.entities.ResultEntity
import com.bangkit.wastify.utils.Helper.convertDateStringToMillis
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result (
    var id: Int? = null,
    val image: Bitmap,
    val classifications: List<Classification>,
    val typeId: String,
    val categoryId: String,
    val recyclable: Boolean,
    val createdAt: String,
): Parcelable

fun Result.asEntityModel(): ResultEntity {
    return ResultEntity(
        image = this.image,
        classifications = this.classifications,
        typeId = this.typeId,
        categoryId = this.categoryId,
        recyclable = this.recyclable,
        createdAtMillis = convertDateStringToMillis(this.createdAt),
    )
}