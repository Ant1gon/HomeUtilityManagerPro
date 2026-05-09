package com.ant1gon.homeutility.data.dao

import androidx.room.*
import com.ant1gon.homeutility.data.entity.HouseholdEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HouseholdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(household: HouseholdEntity): Long

    @Update
    suspend fun update(household: HouseholdEntity)

    @Delete
    suspend fun delete(household: HouseholdEntity)

    @Query("SELECT * FROM households WHERE id = :id")
    suspend fun getById(id: Long): HouseholdEntity?

    @Query("SELECT * FROM households ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<HouseholdEntity>>

    @Query("SELECT * FROM households ORDER BY createdAt DESC")
    suspend fun getAll(): List<HouseholdEntity>
}
