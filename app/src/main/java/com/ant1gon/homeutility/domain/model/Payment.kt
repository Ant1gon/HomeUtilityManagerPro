package com.ant1gon.homeutility.domain.model

import java.time.LocalDateTime

data class Payment(
    val id: Long = 0,
    val householdId: Long,
    val amountPaid: Double,
    val month: Int,
    val year: Int,
    val totalCalculated: Double,
    val balance: Double,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
