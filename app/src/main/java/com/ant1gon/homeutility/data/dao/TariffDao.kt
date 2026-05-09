package com.ant1gon.homeutility.data.dao

import androidx.room.*
import com.ant1gon.homeutility.data.entity.TariffEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TariffDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tariff: TariffEntity): Long

    @Update
    suspend fun update(tariff: TariffEntity)

    @Delete
    suspend fun delete(tariff: TariffEntity)

    @Query("SELECT * FROM tariffs WHERE id = :id")
    suspend fun getById(id: Long): TariffEntity?

    @Query("SELECT * FROM tariffs WHERE householdId = :householdId ORDER BY effectiveDate DESC")
    fun getByHouseholdFlow(householdId: Long): Flow<List<TariffEntity>>

    @Query("SELECT * FROM tariffs WHERE householdId = :householdId AND meterType = :meterType AND effectiveDate <= :date ORDER BY effectiveDate DESC LIMIT 1")
    suspend fun getActiveTariff(
        householdId: Long,
        meterType: String,
        date: LocalDate
    ): TariffEntity?
}
