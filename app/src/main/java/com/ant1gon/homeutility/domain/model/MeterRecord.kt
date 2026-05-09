package com.ant1gon.homeutility.domain.model

import java.time.LocalDateTime

data class MeterRecord(
    val id: Long = 0,
    val meterId: Long,
    val currentReading: Double,
    val previousReading: Double = 0.0,
    val delta: Double = 0.0,
    val dayZoneReading: Double = 0.0,
    val nightZoneReading: Double = 0.0,
    val month: Int,
    val year: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
