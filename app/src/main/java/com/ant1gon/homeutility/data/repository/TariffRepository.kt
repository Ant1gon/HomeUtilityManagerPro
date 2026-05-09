package com.ant1gon.homeutility.data.repository

import com.ant1gon.homeutility.data.dao.TariffDao
import com.ant1gon.homeutility.data.entity.TariffEntity
import com.ant1gon.homeutility.domain.model.Tariff
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TariffRepository @Inject constructor(
    private val tariffDao: TariffDao
) {

    suspend fun createTariff(
        householdId: Long,
        meterType: String,
        effectiveDate: LocalDate,
        basePrice: Double,
        tieredThreshold: Double = 0.0,
        tieredPrice: Double = 0.0
    ): Long {
        val entity = TariffEntity(
            householdId = householdId,
            meterType = meterType,
            effectiveDate = effectiveDate,
            basePrice = basePrice,
            tieredThreshold = tieredThreshold,
            tieredPrice = tieredPrice
        )
        return tariffDao.insert(entity)
    }

    suspend fun updateTariff(tariff: Tariff) {
        val entity = tariff.toEntity()
        tariffDao.update(entity)
    }

    suspend fun deleteTariff(id: Long) {
        val tariff = tariffDao.getById(id) ?: return
        tariffDao.delete(tariff)
    }

    suspend fun getTariffById(id: Long): Tariff? {
        return tariffDao.getById(id)?.toDomain()
    }

    fun getTariffsByHousehold(householdId: Long): Flow<List<Tariff>> {
        return tariffDao.getByHouseholdFlow(householdId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getActiveTariff(
        householdId: Long,
        meterType: String,
        date: LocalDate = LocalDate.now()
    ): Tariff? {
        return tariffDao.getActiveTariff(householdId, meterType, date)?.toDomain()
    }

    suspend fun getTariffHistory(
        householdId: Long,
        meterType: String
    ): List<Tariff> {
        return tariffDao.getByHouseholdFlow(householdId).map { entities ->
            entities
                .filter { it.meterType == meterType }
                .map { it.toDomain() }
                .sortedByDescending { it.effectiveDate }
        }.let { flow ->
            var result: List<Tariff> = emptyList()
            flow.collect { result = it }
            result
        }
    }

    private fun TariffEntity.toDomain() = Tariff(
        id = id,
        householdId = householdId,
        meterType = meterType,
        effectiveDate = effectiveDate,
        basePrice = basePrice,
        tieredThreshold = tieredThreshold,
        tieredPrice = tieredPrice,
        version = version,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Tariff.toEntity() = TariffEntity(
        id = id,
        householdId = householdId,
        meterType = meterType,
        effectiveDate = effectiveDate,
        basePrice = basePrice,
        tieredThreshold = tieredThreshold,
        tieredPrice = tieredPrice,
        version = version,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
