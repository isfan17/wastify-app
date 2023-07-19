package com.bangkit.wastify.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.bangkit.wastify.data.db.entities.CategoryEntity
import com.bangkit.wastify.data.db.entities.DisposalMethodEntity

data class CategoryAndMethods(
    @Embedded
    val category: CategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val methods: List<DisposalMethodEntity>
)
