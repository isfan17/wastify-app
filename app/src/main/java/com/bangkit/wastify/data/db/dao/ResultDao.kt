package com.bangkit.wastify.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.wastify.data.db.entities.ResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDao {


    @Query("SELECT * FROM results")
    fun getResults(): Flow<List<ResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(articles: List<ResultEntity>)

    @Query("DELETE FROM results")
    suspend fun clearResults()
}