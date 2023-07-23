package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.db.entities.ResultEntity
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.utils.Helper.byteArrayStringToBitmap
import com.bangkit.wastify.utils.Helper.convertDateStringToMillis

data class FirebaseResult(
    var id: Int? = null,
    val image: String? = null,
    val classifications: List<Classification>? = null,
    val typeId: String? = null,
    val categoryId: String? = null,
    val recyclable: Boolean? = null,
    val createdAt: String? = null,
)

fun List<FirebaseResult>.asEntityModel(): List<ResultEntity> {
    return map {
        ResultEntity(
            id = it.id!!,
            image = byteArrayStringToBitmap(it.image!!),
            classifications = it.classifications!!,
            typeId = it.typeId!!,
            categoryId = it.categoryId!!,
            recyclable = it.recyclable!!,
            createdAtMillis = convertDateStringToMillis(it.createdAt!!)
        )
    }
}