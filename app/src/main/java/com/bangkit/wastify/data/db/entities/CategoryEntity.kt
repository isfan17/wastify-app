package com.bangkit.wastify.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bangkit.wastify.data.model.Category

@Entity(tableName = "Categories")
data class CategoryEntity (
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String,
    val image: String,
    val description: String,
    val typeId: String,
)

fun List<CategoryEntity>.asDomainModel(): List<Category> {
    return map {
        Category(
            id = it.id,
            icon = it.icon,
            image = it.image,
            name = it.name,
            description = it.description,
        )
    }
}