package com.bangkit.wastify.data.model

import com.bangkit.wastify.data.db.entities.ResultEntity
import com.bangkit.wastify.utils.Helper.convertDateStringToMillis

data class Result(
    val imageUrl: String,
    val name: String,
    val percentage: Double,
    val typeId: String,
    val categoryId: String,
    val recyclable: Boolean,
    val createdAt: String
)

fun Result.asEntityModel(): ResultEntity {
    return ResultEntity(
        imageUrl = this.imageUrl,
        name = this.name,
        percentage = this.percentage,
        createdAtMillis = convertDateStringToMillis(this.createdAt),
        typeId = this.typeId,
        categoryId = this.categoryId,
        recyclable = this.recyclable,
    )
}