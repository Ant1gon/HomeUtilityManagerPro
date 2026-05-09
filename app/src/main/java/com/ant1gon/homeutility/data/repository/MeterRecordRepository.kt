package com.ant1gon.homeutility.data.repository

import com.ant1gon.homeutility.data.dao.MeterRecordDao
import com.ant1gon.homeutility.data.entity.MeterRecordEntity
import com.ant1gon.homeutility.domain.model.MeterRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MeterRecordRepository @Inject constructor(
    private val meterRecordDao: MeterRecordDao
) {

    suspend fun createMeterRecord(
        meterId: Long,
        currentReading: Double,
        previousReading: Double = 0.0,
        dayZoneReading: Double = 0.0,
        nightZoneReading: Double = 0.0,
        month: Int,
        year: Int
    ): Long {
        val delta = maxOf(0.0, currentReading - previousReading)
        val entity = MeterRecordEntity(
            meterId = meterId,
            currentReading = currentReading,
            previousReading = previousReading,
            delta = delta,
            dayZoneReading = dayZoneReading,
            nightZoneReading = nightZoneReading,
            month = month,
            year = year
        )
        return meterRecordDao.insert(entity)
    }

    suspend fun updateMeterRecord(record: MeterRecord) {
        val entity = record.toEntity()
        meterRecordDao.update(entity)
    }

    suspend fun deleteMeterRecord(id: Long) {
        val record = meterRecordDao.getById(id) ?: return
        meterRecordDao.delete(record)
    }

    suspend fun getMeterRecordById(id: Long): MeterRecord? {
        return meterRecordDao.getById(id)?.toDomain()
    }

    fun getMeterRecordsByMeter(meterId: Long): Flow<List<MeterRecord>> {
        return meterRecordDao.getByMeterFlow(meterId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getMeterRecordByMonth(
        meterId: Long,
        month: Int,
        year: Int
    ): MeterRecord? {
        return meterRecordDao.getByMeterAndMonth(meterId, month, year)?.toDomain()
    }

    suspend fun getLatestRecord(meterId: Long): MeterRecord? {
        return meterRecordDao.getLatestRecord(meterId)?.toDomain()
    }

    private fun MeterRecordEntity.toDomain() = MeterRecord(
        id = id,
        meterId = meterId,
        currentReading = currentReading,
        previousReading = previousReading,
        delta = delta,
        dayZoneReading = dayZoneReading,
        nightZoneReading = nightZoneReading,
        month = month,
        year = year,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun MeterRecord.toEntity() = MeterRecordEntity(
        id = id,
        meterId = meterId,
        currentReading = currentReading,
        previousReading = previousReading,
        delta = delta,
        dayZoneReading = dayZoneReading,
        nightZoneReading = nightZoneReading,
        month = month,
        year = year,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
