package com.ant1gon.homeutility.di

import com.ant1gon.homeutility.data.dao.*
import com.ant1gon.homeutility.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideHouseholdRepository(
        householdDao: HouseholdDao
    ): HouseholdRepository {
        return HouseholdRepository(householdDao)
    }

    @Singleton
    @Provides
    fun provideMeterRepository(
        meterDao: MeterDao
    ): MeterRepository {
        return MeterRepository(meterDao)
    }

    @Singleton
    @Provides
    fun provideTariffRepository(
        tariffDao: TariffDao
    ): TariffRepository {
        return TariffRepository(tariffDao)
    }

    @Singleton
    @Provides
    fun provideMeterRecordRepository(
        meterRecordDao: MeterRecordDao
    ): MeterRecordRepository {
        return MeterRecordRepository(meterRecordDao)
    }

    @Singleton
    @Provides
    fun providePaymentRepository(
        paymentDao: PaymentDao
    ): PaymentRepository {
        return PaymentRepository(paymentDao)
    }
}
