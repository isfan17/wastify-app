package com.bangkit.wastify.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bangkit.wastify.data.model.User

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey
    var id: String,
    val name: String,
    val email: String,
    var imageUrl: String? = null,
)

fun UserEntity.asDomainModel(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        imageUrl = this.imageUrl,
    )
}