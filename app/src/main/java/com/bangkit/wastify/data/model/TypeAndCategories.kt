package com.bangkit.wastify.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.bangkit.wastify.data.db.entities.CategoryEntity
import com.bangkit.wastify.data.db.entities.TypeEntity

data class TypeAndCategories(
    @Embedded
    val type: TypeEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "typeId"
    )
    val categories: List<CategoryEntity>
)
