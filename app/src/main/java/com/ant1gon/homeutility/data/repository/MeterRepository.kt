package com.ant1gon.homeutility.data.repository

import com.ant1gon.homeutility.data.dao.MeterDao
import com.ant1gon.homeutility.data.entity.MeterEntity
import com.ant1gon.homeutility.domain.model.Meter
import com.ant1gon.homeutility.domain.model.MeterType
import com.ant1gon.homeutility.domain.model.WaterType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MeterRepository @Inject constructor(
    private val meterDao: MeterDao
) {

    suspend fun createMeter(
        householdId: Long,
        customName: String,
        meterType: MeterType,
        location: String = "",
        electricityZones: Int = 1,
        waterType: WaterType? = null,
        tariffId: Long? = null
    ): Long {
        val entity = MeterEntity(
            householdId = householdId,
            tariffId = tariffId,
            customName = customName,
            meterType = meterType.name,
            location = location,
            electricityZones = electricityZones,
            waterType = waterType?.name ?: ""
        )
        return meterDao.insert(entity)
    }

    suspend fun updateMeter(meter: Meter) {
        val entity = meter.toEntity()
        meterDao.update(entity)
    }

    suspend fun deleteMeter(id: Long) {
        val meter = meterDao.getById(id) ?: return
        meterDao.delete(meter)
    }

    suspend fun getMeterById(id: Long): Meter? {
        return meterDao.getById(id)?.toDomain()
    }

    fun getMetersByHousehold(householdId: Long): Flow<List<Meter>> {
        return meterDao.getByHouseholdFlow(householdId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun MeterEntity.toDomain() = Meter(
        id = id,
        householdId = householdId,
        tariffId = tariffId,
        customName = customName,
        meterType = MeterType.valueOf(meterType),
        location = location,
        electricityZones = electricityZones,
        waterType = if (waterType.isNotEmpty()) WaterType.valueOf(waterType) else null,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Meter.toEntity() = MeterEntity(
        id = id,
        householdId = householdId,
        tariffId = tariffId,
        customName = customName,
        meterType = meterType.name,
        location = location,
        electricityZones = electricityZones,
        waterType = waterType?.name ?: "",
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
