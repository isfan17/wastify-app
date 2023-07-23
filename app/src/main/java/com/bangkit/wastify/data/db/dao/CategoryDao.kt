package com.bangkit.wastify.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.wastify.data.db.entities.CategoryEntity
import com.bangkit.wastify.data.model.CategoryAndMethods
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategory(id: String): Flow<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryAndMethods(id: String): Flow<CategoryAndMethods>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Query("DELETE FROM categories")
    suspend fun clearCategories()
}