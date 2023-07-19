package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.db.entities.DisposalMethodEntity

data class FirebaseDisposalMethod(
    var id: String? = null,
    val method: String? = null,
    val category_id: String? = null,
)

fun List<FirebaseDisposalMethod>.asEntityModel(): List<DisposalMethodEntity> {
    return map {
        DisposalMethodEntity(
            id = it.id!!,
            method = it.method!!,
            categoryId = it.category_id!!,
        )
    }
}