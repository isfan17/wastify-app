package com.bangkit.wastify.data.db.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.network.FirebaseResult
import com.bangkit.wastify.utils.Helper.bitmapToByteArrayString
import com.bangkit.wastify.utils.Helper.convertDateMillisToString

@Entity(tableName = "results")
data class ResultEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val image: Bitmap,
    val classifications: List<Classification>,
    val typeId: String,
    val categoryId: String,
    val recyclable: Boolean,
    val createdAtMillis: Long,
)

fun List<ResultEntity>.asDomainModel(): List<Result> {
    return map {
        Result(
            id = it.id,
            image = it.image,
            classifications = it.classifications,
            typeId = it.typeId,
            categoryId = it.categoryId,
            recyclable = it.recyclable,
            createdAt = convertDateMillisToString(it.createdAtMillis)
        )
    }
}

fun List<ResultEntity>.asNetworkModel(): List<FirebaseResult> {
    return map {
        FirebaseResult(
            id = it.id,
            image = bitmapToByteArrayString(it.image),
            classifications = it.classifications,
            typeId = it.typeId,
            categoryId = it.categoryId,
            recyclable = it.recyclable,
            createdAt = convertDateMillisToString(it.createdAtMillis)
        )
    }
}