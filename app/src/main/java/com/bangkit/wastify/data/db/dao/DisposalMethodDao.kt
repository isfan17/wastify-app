package com.bangkit.wastify.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.wastify.data.db.entities.DisposalMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DisposalMethodDao {

    @Query("SELECT * FROM disposal_methods")
    fun getDisposalMethods(): Flow<List<DisposalMethodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDisposalMethods(methods: List<DisposalMethodEntity>)

    @Query("DELETE FROM disposal_methods")
    suspend fun clearDisposalMethods()
}