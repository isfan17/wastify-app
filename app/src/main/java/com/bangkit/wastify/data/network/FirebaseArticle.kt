package com.bangkit.wastify.data.network

import com.bangkit.wastify.data.db.entities.ArticleEntity
import com.bangkit.wastify.utils.Helper.convertDateStringToMillis

data class FirebaseArticle(
    var id: String? = null,
    val title: String? = null,
    val image: String? = null,
    val source: String? = null,
    val publishedAt: String? = null,
    val description: String? = null,
)

fun List<FirebaseArticle>.asEntityModel(): List<ArticleEntity> {
    return map {
        ArticleEntity(
            id = it.id!!,
            image = it.image!!,
            title = it.title!!,
            source = it.source!!,
            publishedAtMillis = convertDateStringToMillis(it.publishedAt!!),
            description = it.description!!,
        )
    }
}