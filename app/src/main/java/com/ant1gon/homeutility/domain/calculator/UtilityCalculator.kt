package com.ant1gon.homeutility.domain.calculator

import com.ant1gon.homeutility.domain.model.MeterType
import kotlin.math.max

object UtilityCalculator {

    /**
     * Calculate electricity cost with dual-zone support
     * Formula: (Δ Day × Tariff) + (Δ Night × Tariff × 0.5)
     */
    fun calculateElectricityCost(
        dayZoneDelta: Double,
        nightZoneDelta: Double,
        tariffPerUnit: Double,
        tieredThreshold: Double = 0.0,
        tieredPrice: Double = 0.0
    ): Double {
        val totalConsumption = dayZoneDelta + nightZoneDelta

        return if (tieredThreshold > 0 && totalConsumption > tieredThreshold) {
            val baseCost = tieredThreshold * tariffPerUnit
            val exceededUnits = totalConsumption - tieredThreshold
            val nightZoneCost = nightZoneDelta * tariffPerUnit * 0.5
            baseCost + (exceededUnits * tieredPrice) + nightZoneCost
        } else {
            (dayZoneDelta * tariffPerUnit) + (nightZoneDelta * tariffPerUnit * 0.5)
        }
    }

    /**
     * Calculate water and sewage cost
     * Formula: (Δ Hot × Price_hot) + (Δ Cold × Price_cold) + (Δ Total Water × Price_sewage) + Fixed Fees
     */
    fun calculateWaterCost(
        hotWaterDelta: Double,
        coldWaterDelta: Double,
        hotWaterPrice: Double,
        coldWaterPrice: Double,
        sewagePrice: Double,
        fixedFees: Double = 0.0,
        tieredThreshold: Double = 0.0,
        tieredPrice: Double = 0.0
    ): Double {
        val totalWaterDelta = hotWaterDelta + coldWaterDelta

        val hotCost = if (tieredThreshold > 0 && hotWaterDelta > tieredThreshold) {
            (tieredThreshold * hotWaterPrice) + ((hotWaterDelta - tieredThreshold) * tieredPrice)
        } else {
            hotWaterDelta * hotWaterPrice
        }

        val coldCost = if (tieredThreshold > 0 && coldWaterDelta > tieredThreshold) {
            (tieredThreshold * coldWaterPrice) + ((coldWaterDelta - tieredThreshold) * tieredPrice)
        } else {
            coldWaterDelta * coldWaterPrice
        }

        val sewageCost = totalWaterDelta * sewagePrice

        return hotCost + coldCost + sewageCost + fixedFees
    }

    /**
     * Calculate gas cost with tiered pricing
     */
    fun calculateGasCost(
        delta: Double,
        basePrice: Double,
        tieredThreshold: Double = 0.0,
        tieredPrice: Double = 0.0,
        fixedFees: Double = 0.0
    ): Double {
        val baseCost = if (tieredThreshold > 0 && delta > tieredThreshold) {
            (tieredThreshold * basePrice) + ((delta - tieredThreshold) * tieredPrice)
        } else {
            delta * basePrice
        }
        return baseCost + fixedFees
    }

    /**
     * Calculate heating cost (usually flat rate per month)
     */
    fun calculateHeatingCost(fixedCost: Double): Double = fixedCost

    /**
     * Calculate delta (consumption)
     */
    fun calculateDelta(current: Double, previous: Double): Double {
        return max(0.0, current - previous)
    }

    /**
     * Calculate monthly balance
     * Balance = TotalCalculated - AmountPaid
     */
    fun calculateMonthlyBalance(totalCalculated: Double, amountPaid: Double): Double {
        return totalCalculated - amountPaid
    }

    /**
     * Update household balance
     * Balance carries over from month to month
     */
    fun updateHouseholdBalance(
        previousBalance: Double,
        monthlyBalance: Double
    ): Double {
        return previousBalance + monthlyBalance
    }
}
