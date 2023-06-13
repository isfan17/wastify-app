package com.bangkit.wastify.data.db

import androidx.room.*
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface WastifyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypes(articles: List<Type>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteUser()

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM articles")
    fun getArticles(): Flow<List<Article>>

    @Query("SELECT * FROM types")
    fun getTypes(): Flow<List<Type>>

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<User>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: Int): Flow<Category>

    @Query("SELECT * FROM types WHERE id = :id")
    fun getTypeById(id: Int): Flow<Type>
}