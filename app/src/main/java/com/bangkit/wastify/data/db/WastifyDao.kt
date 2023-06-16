package com.bangkit.wastify.data.db

import androidx.room.*
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.model.User
import kotlinx.coroutines.flow.Flow
import com.bangkit.wastify.data.model.Result

@Dao
interface WastifyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypes(articles: List<Type>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(articles: List<Result>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteUser()

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM types")
    fun getTypes(): Flow<List<Type>>
    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<Category>>
    @Query("SELECT * FROM articles")
    fun getArticles(): Flow<List<Article>>
    @Query("SELECT * FROM results")
    fun getResults(): Flow<List<Result>>

    @Query("DELETE FROM types")
    suspend fun clearTypes()
    @Query("DELETE FROM categories")
    suspend fun clearCategories()
    @Query("DELETE FROM articles")
    suspend fun clearArticles()
    @Query("DELETE FROM results")
    suspend fun clearResults()

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<User>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: String): Flow<Category>
    @Query("SELECT * FROM types WHERE id = :id")
    fun getTypeById(id: String): Flow<Type>
    @Query("SELECT * FROM articles WHERE id = :id")
    fun getArticleById(id: String): Flow<Article>
}