package com.ant1gon.homeutility.domain.model

import java.time.LocalDateTime

data class Meter(
    val id: Long = 0,
    val householdId: Long,
    val tariffId: Long?,
    val customName: String,
    val meterType: MeterType,
    val location: String = "",
    val electricityZones: Int = 1,
    val waterType: WaterType? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class MeterType {
    ELECTRICITY, WATER, GAS, HEATING
}

enum class WaterType {
    HOT, COLD
}
