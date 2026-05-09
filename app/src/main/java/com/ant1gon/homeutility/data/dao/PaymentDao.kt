package com.ant1gon.homeutility.data.dao

import androidx.room.*
import com.ant1gon.homeutility.data.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(payment: PaymentEntity): Long

    @Update
    suspend fun update(payment: PaymentEntity)

    @Delete
    suspend fun delete(payment: PaymentEntity)

    @Query("SELECT * FROM payments WHERE id = :id")
    suspend fun getById(id: Long): PaymentEntity?

    @Query("SELECT * FROM payments WHERE householdId = :householdId ORDER BY year DESC, month DESC")
    fun getByHouseholdFlow(householdId: Long): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE householdId = :householdId AND month = :month AND year = :year LIMIT 1")
    suspend fun getByHouseholdAndMonth(
        householdId: Long,
        month: Int,
        year: Int
    ): PaymentEntity?
}
