package com.bangkit.wastify.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bangkit.wastify.data.model.Type

@Entity(tableName = "types")
data class TypeEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String,
    val image: String,
    val description: String,
)

fun List<TypeEntity>.asDomainModel(): List<Type> {
    return map {
        Type(
            id = it.id,
            name = it.name,
            icon = it.icon,
            image = it.image,
            description = it.description,
        )
    }
}