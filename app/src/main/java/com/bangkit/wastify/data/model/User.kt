package com.bangkit.wastify.data.model

import android.os.Parcelable
import com.bangkit.wastify.data.db.entities.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String,
    val name: String,
    val email: String,
    var imageUrl: String? = null,
): Parcelable

fun User.asEntityModel(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        imageUrl = this.imageUrl,
    )
}