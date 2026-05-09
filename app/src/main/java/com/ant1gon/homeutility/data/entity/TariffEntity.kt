package com.ant1gon.homeutility.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "tariffs",
    foreignKeys = [
        ForeignKey(
            entity = HouseholdEntity::class,
            parentColumns = ["id"],
            childColumns = ["householdId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TariffEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val householdId: Long,
    val meterType: String, // "Electricity", "Water", "Gas", "Heating"
    val effectiveDate: LocalDate,
    val basePrice: Double, // Price per unit
    val tieredThreshold: Double = 0.0, // Volume limit (e.g., 100 kWh)
    val tieredPrice: Double = 0.0, // Increased price for units exceeding threshold
    val version: Int = 1, // For versioning tariffs
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
