package com.ant1gon.homeutility.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "meter_records",
    foreignKeys = [
        ForeignKey(
            entity = MeterEntity::class,
            parentColumns = ["id"],
            childColumns = ["meterId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MeterRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val meterId: Long,
    val currentReading: Double, // Current meter value
    val previousReading: Double = 0.0, // Previous month reading
    val delta: Double = 0.0, // Consumption (currentReading - previousReading)
    val dayZoneReading: Double = 0.0, // For dual-zone electricity
    val nightZoneReading: Double = 0.0, // For dual-zone electricity
    val month: Int, // 1-12
    val year: Int,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
