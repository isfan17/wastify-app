package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.db.entities.CategoryEntity
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.utils.Helper

data class FirebaseCategory(
    var id: String? = null,
    val name: String? = null,
    val icon: String? = null,
    val image: String? = null,
    val description: String? = null,
    val type_id: String? = null,
)

fun List<FirebaseCategory>.asEntityModel(): List<CategoryEntity> {
    return map {
        CategoryEntity(
            id = it.id!!,
            name = it.name!!,
            icon = it.icon!!,
            image = it.image!!,
            description = it.description!!,
            typeId = it.type_id!!,
        )
    }
}
