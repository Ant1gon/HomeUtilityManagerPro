package com.ant1gon.homeutility.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Tariff(
    val id: Long = 0,
    val householdId: Long,
    val meterType: String,
    val effectiveDate: LocalDate,
    val basePrice: Double,
    val tieredThreshold: Double = 0.0,
    val tieredPrice: Double = 0.0,
    val version: Int = 1,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
