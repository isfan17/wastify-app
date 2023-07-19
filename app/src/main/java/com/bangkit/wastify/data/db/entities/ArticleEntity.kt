package com.bangkit.wastify.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.utils.Helper.convertDateMillisToString

@Entity(tableName = "articles")
data class ArticleEntity (
    @PrimaryKey
    val id: String,
    val image: String,
    val title: String,
    val source: String,
    @ColumnInfo(name = "published_at")
    val publishedAtMillis: Long,
    val description: String,
    var isBookmarked: Boolean = false,
)

fun List<ArticleEntity>.asDomainModel(): List<Article> {
    return map {
        Article(
            id = it.id,
            image = it.image,
            title = it.title,
            source = it.source,
            publishedAt = convertDateMillisToString(it.publishedAtMillis),
            description = it.description,
            isBookmarked = it.isBookmarked,
        )
    }
}

fun ArticleEntity.asDomainModel(): Article {
    return Article(
        id = this.id,
        image = this.image,
        title = this.title,
        source = this.source,
        publishedAt = convertDateMillisToString(this.publishedAtMillis),
        description = this.description,
        isBookmarked = this.isBookmarked,
    )
}