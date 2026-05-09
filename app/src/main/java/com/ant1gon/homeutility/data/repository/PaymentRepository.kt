package com.ant1gon.homeutility.data.repository

import com.ant1gon.homeutility.data.dao.PaymentDao
import com.ant1gon.homeutility.data.entity.PaymentEntity
import com.ant1gon.homeutility.domain.model.Payment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val paymentDao: PaymentDao
) {

    suspend fun createPayment(
        householdId: Long,
        amountPaid: Double,
        totalCalculated: Double,
        month: Int,
        year: Int
    ): Long {
        val balance = totalCalculated - amountPaid
        val entity = PaymentEntity(
            householdId = householdId,
            amountPaid = amountPaid,
            totalCalculated = totalCalculated,
            balance = balance,
            month = month,
            year = year
        )
        return paymentDao.insert(entity)
    }

    suspend fun updatePayment(payment: Payment) {
        val entity = payment.toEntity()
        paymentDao.update(entity)
    }

    suspend fun deletePayment(id: Long) {
        val payment = paymentDao.getById(id) ?: return
        paymentDao.delete(payment)
    }

    suspend fun getPaymentById(id: Long): Payment? {
        return paymentDao.getById(id)?.toDomain()
    }

    fun getPaymentsByHousehold(householdId: Long): Flow<List<Payment>> {
        return paymentDao.getByHouseholdFlow(householdId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getPaymentByMonth(
        householdId: Long,
        month: Int,
        year: Int
    ): Payment? {
        return paymentDao.getByHouseholdAndMonth(householdId, month, year)?.toDomain()
    }

    private fun PaymentEntity.toDomain() = Payment(
        id = id,
        householdId = householdId,
        amountPaid = amountPaid,
        month = month,
        year = year,
        totalCalculated = totalCalculated,
        balance = balance,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    private fun Payment.toEntity() = PaymentEntity(
        id = id,
        householdId = householdId,
        amountPaid = amountPaid,
        month = month,
        year = year,
        totalCalculated = totalCalculated,
        balance = balance,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
