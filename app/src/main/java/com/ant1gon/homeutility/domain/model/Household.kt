package com.ant1gon.homeutility.domain.model

import java.time.LocalDateTime

data class Household(
    val id: Long = 0,
    val name: String,
    val address: String = "",
    val ownershipType: OwnershipType,
    val rentCost: Double = 0.0,
    val balance: Double = 0.0,
    val maintenanceFeeMontly: Double = 0.0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class OwnershipType {
    OWNED, RENTED
}
