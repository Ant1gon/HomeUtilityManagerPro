package com.ant1gon.homeutility.data.dao

import androidx.room.*
import com.ant1gon.homeutility.data.entity.MeterRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeterRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: MeterRecordEntity): Long

    @Update
    suspend fun update(record: MeterRecordEntity)

    @Delete
    suspend fun delete(record: MeterRecordEntity)

    @Query("SELECT * FROM meter_records WHERE id = :id")
    suspend fun getById(id: Long): MeterRecordEntity?

    @Query("SELECT * FROM meter_records WHERE meterId = :meterId ORDER BY year DESC, month DESC")
    fun getByMeterFlow(meterId: Long): Flow<List<MeterRecordEntity>>

    @Query("SELECT * FROM meter_records WHERE meterId = :meterId AND month = :month AND year = :year LIMIT 1")
    suspend fun getByMeterAndMonth(
        meterId: Long,
        month: Int,
        year: Int
    ): MeterRecordEntity?

    @Query("SELECT * FROM meter_records WHERE meterId = :meterId ORDER BY year DESC, month DESC LIMIT 1")
    suspend fun getLatestRecord(meterId: Long): MeterRecordEntity?
}
