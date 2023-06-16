package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.utils.Helper

data class FirebaseCategory(
    var id: String? = null,
    val name: String? = null,
    val icon: String? = null,
    val image: String? = null,
    val desc: String? = null,
    val dispm: HashMap<String, String>? = null
)

fun List<FirebaseCategory>.asDomainModel(): List<Category> {
    return map {
        Category(
            id = it.id!!,
            name = it.name!!,
            icon = it.icon!!,
            image = it.image!!,
            description = it.desc!!,
            disposalMethods = Helper.hashMapToListOfString(it.dispm!!)
        )
    }
}
