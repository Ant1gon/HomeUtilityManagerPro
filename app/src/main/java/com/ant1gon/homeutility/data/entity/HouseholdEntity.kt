package com.ant1gon.homeutility.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "households")
data class HouseholdEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val address: String = "",
    val ownershipType: String, // "Owned" or "Rented"
    val rentCost: Double = 0.0, // Only active if ownershipType is "Rented"
    val balance: Double = 0.0, // Cumulative balance (overpayment or debt)
    val maintenanceFeeMontly: Double = 0.0, // OSBB/Maintenance fee
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
