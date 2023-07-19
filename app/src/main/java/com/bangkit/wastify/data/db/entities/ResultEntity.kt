package com.bangkit.wastify.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.utils.Helper.convertDateMillisToString

@Entity(tableName = "results")
data class ResultEntity(
    @PrimaryKey
    val id: Int = 0,
    val imageUrl: String,
    val name: String,
    val percentage: Double,
    val createdAtMillis: Long,
    val typeId: String,
    val categoryId: String,
    val recyclable: Boolean,
)

fun List<ResultEntity>.asDomainModel(): List<Result> {
    return map {
        Result(
            imageUrl = it.imageUrl,
            name = it.name,
            percentage = it.percentage,
            createdAt = convertDateMillisToString(it.createdAtMillis),
            typeId = it.typeId,
            categoryId = it.categoryId,
            recyclable = it.recyclable,
        )
    }
}