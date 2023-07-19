package com.bangkit.wastify.data.model

import android.os.Parcelable
import com.bangkit.wastify.data.db.entities.ArticleEntity
import com.bangkit.wastify.utils.Helper.convertDateStringToMillis
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    var id: String,
    val image: String,
    val title: String,
    val source: String,
    val publishedAt: String,
    val description: String,
    var isBookmarked: Boolean,
): Parcelable

fun Article.asEntityModel(): ArticleEntity {
    return ArticleEntity(
        id = this.id,
        image = this.image,
        title = this.title,
        source = this.source,
        publishedAtMillis = convertDateStringToMillis(this.publishedAt),
        description = this.description,
        isBookmarked = this.isBookmarked,
    )
}