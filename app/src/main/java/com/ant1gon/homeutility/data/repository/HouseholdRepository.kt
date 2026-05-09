package com.ant1gon.homeutility.data.repository

import com.ant1gon.homeutility.data.dao.HouseholdDao
import com.ant1gon.homeutility.data.entity.HouseholdEntity
import com.ant1gon.homeutility.domain.model.Household
import com.ant1gon.homeutility.domain.model.OwnershipType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HouseholdRepository @Inject constructor(
    private val householdDao: HouseholdDao
) {

    suspend fun createHousehold(
        name: String,
        address: String = "",
        ownershipType: OwnershipType,
        rentCost: Double = 0.0,
        maintenanceFeeMontly: Double = 0.0
    ): Long {
        val entity = HouseholdEntity(
            name = name,
            address = address,
            ownershipType = ownershipType.name,
            rentCost = if (ownershipType == OwnershipType.RENTED) rentCost else 0.0,
            maintenanceFeeMontly = maintenanceFeeMontly
        )
        return householdDao.insert(entity)
    }

    suspend fun updateHousehold(household: Household) {
        val entity = household.toEntity()
        householdDao.update(entity)
    }

    suspend fun deleteHousehold(id: Long) {
        val household = householdDao.getById(id) ?: return
        householdDao.delete(household)
    }

    suspend fun getHouseholdById(id: Long): Household? {
        return householdDao.getById(id)?.toDomain()
    }

    fun getAllHouseholds(): Flow<List<Household>> {
        return householdDao.getAllFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun updateBalance(householdId: Long, newBalance: Double) {
        val household = householdDao.getById(householdId) ?: return
        householdDao.update(household.copy(balance = newBalance))
    }

    private fun HouseholdEntity.toDomain() = Household(
        id = id,
        name = name,
        address = address,
        ownershipType = OwnershipType.valueOf(ownershipType),
        rentCost = rentCost,
        balance = balance,
        maintenanceFeeMontly = maintenanceFeeMontly,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Household.toEntity() = HouseholdEntity(
        id = id,
        name = name,
        address = address,
        ownershipType = ownershipType.name,
        rentCost = rentCost,
        balance = balance,
        maintenanceFeeMontly = maintenanceFeeMontly,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
