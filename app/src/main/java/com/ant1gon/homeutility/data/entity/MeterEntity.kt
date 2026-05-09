package com.ant1gon.homeutility.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "meters",
    foreignKeys = [
        ForeignKey(
            entity = HouseholdEntity::class,
            parentColumns = ["id"],
            childColumns = ["householdId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TariffEntity::class,
            parentColumns = ["id"],
            childColumns = ["tariffId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class MeterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val householdId: Long,
    val tariffId: Long?,
    val customName: String,
    val meterType: String, // "Electricity", "Water", "Gas", "Heating"
    val location: String = "",
    val electricityZones: Int = 1, // 1, 2, or 3 zones for electricity meters
    val waterType: String = "", // "Hot" or "Cold" for water meters
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
