package com.ant1gon.homeutility.data.dao

import androidx.room.*
import com.ant1gon.homeutility.data.entity.MeterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meter: MeterEntity): Long

    @Update
    suspend fun update(meter: MeterEntity)

    @Delete
    suspend fun delete(meter: MeterEntity)

    @Query("SELECT * FROM meters WHERE id = :id")
    suspend fun getById(id: Long): MeterEntity?

    @Query("SELECT * FROM meters WHERE householdId = :householdId ORDER BY createdAt DESC")
    fun getByHouseholdFlow(householdId: Long): Flow<List<MeterEntity>>

    @Query("SELECT * FROM meters WHERE householdId = :householdId ORDER BY createdAt DESC")
    suspend fun getByHousehold(householdId: Long): List<MeterEntity>
}
