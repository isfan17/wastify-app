package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.utils.Helper

data class FirebaseType(
    var id: String? = null,
    val namet: String? = null,
    val icont: String? = null,
    val imgt: String? = null,
    val desct: String? = null,
    val cat: HashMap<String, String>? = null
)

fun List<FirebaseType>.asDomainModel(): List<Type> {
    return map {
        Type(
            id = it.id!!,
            name = it.namet!!,
            icon = it.icont!!,
            image = it.imgt!!,
            description = it.desct!!,
            categories = Helper.hashMapToListOfString(it.cat!!)
        )
    }
}
