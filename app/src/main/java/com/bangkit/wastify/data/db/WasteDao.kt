package com.bangkit.wastify.data.db

import androidx.room.*
import com.bangkit.wastify.data.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface WasteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Query("DELETE FROM categories")
    suspend fun clearCategories()

    @Query("SELECT * FROM categories")
    fun getCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: Int): Flow<Category>
}