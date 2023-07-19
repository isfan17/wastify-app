package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.db.entities.TypeEntity

data class FirebaseType(
    var id: String? = null,
    val name: String? = null,
    val icon: String? = null,
    val image: String? = null,
    val description: String? = null,
)

fun List<FirebaseType>.asEntityModel(): List<TypeEntity> {
    return map {
        TypeEntity(
            id = it.id!!,
            name = it.name!!,
            icon = it.icon!!,
            image = it.image!!,
            description = it.description!!,
        )
    }
}
