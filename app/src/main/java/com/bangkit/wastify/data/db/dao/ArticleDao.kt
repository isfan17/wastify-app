package com.bangkit.wastify.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bangkit.wastify.data.db.entities.ArticleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles ORDER BY published_at ASC")
    fun getArticles(): Flow<List<ArticleEntity>>

    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticle(id: String): Flow<ArticleEntity>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1")
    fun getSavedArticles(): Flow<List<ArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Update
    suspend fun updateArticle(article: ArticleEntity)

    @Query("DELETE FROM articles")
    suspend fun clearArticles()

    @Query("SELECT * FROM articles WHERE id = :id")
    suspend fun checkArticle(id: String): ArticleEntity?
}