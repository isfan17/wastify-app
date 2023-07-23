package com.bangkit.wastify.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.wastify.data.db.entities.TypeEntity
import com.bangkit.wastify.data.model.TypeAndCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDao {

    @Query("SELECT * FROM types")
    fun getTypes(): Flow<List<TypeEntity>>

    @Query("SELECT * FROM types WHERE id = :id")
    fun getType(id: String): Flow<TypeEntity>

    @Query("SELECT * FROM types WHERE id = :id")
    fun getTypeAndCategories(id: String): Flow<TypeAndCategories>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypes(types: List<TypeEntity>)

    @Query("DELETE FROM types")
    suspend fun clearTypes()
}