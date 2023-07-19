package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.db.entities.ResultEntity
import com.bangkit.wastify.utils.Helper.convertDateStringToMillis
import com.bangkit.wastify.data.model.Result

data class FirebaseResult (
    var imageUrl: String? = null,
    val name: String? = null,
    val percentage: Double? = null,
    val typeId: String? = null,
    val categoryId: String? = null,
    val recyclable: Boolean? = null,
    val createdAt: String? = null
)

fun List<FirebaseResult>.asEntityModel(): List<ResultEntity> {
    return map {
        ResultEntity(
            imageUrl = it.imageUrl!!,
            name = it.name!!,
            percentage = it.percentage!!,
            typeId = it.typeId!!,
            categoryId = it.categoryId!!,
            recyclable = it.recyclable!!,
            createdAtMillis = convertDateStringToMillis(it.createdAt!!)
        )
    }
}

fun List<FirebaseResult>.asDomainModel(): List<Result> {
    return map {
        Result(
            imageUrl = it.imageUrl!!,
            name = it.name!!,
            percentage = it.percentage!!,
            typeId = it.typeId!!,
            categoryId = it.categoryId!!,
            recyclable = it.recyclable!!,
            createdAt = it.createdAt!!,
        )
    }
}