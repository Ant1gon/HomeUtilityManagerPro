package com.ant1gon.homeutility.di

import android.content.Context
import com.ant1gon.homeutility.data.database.UtilityDatabase
import com.ant1gon.homeutility.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideUtilityDatabase(
        @ApplicationContext context: Context
    ): UtilityDatabase {
        return UtilityDatabase.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideHouseholdDao(database: UtilityDatabase): HouseholdDao {
        return database.householdDao()
    }

    @Singleton
    @Provides
    fun provideMeterDao(database: UtilityDatabase): MeterDao {
        return database.meterDao()
    }

    @Singleton
    @Provides
    fun provideTariffDao(database: UtilityDatabase): TariffDao {
        return database.tariffDao()
    }

    @Singleton
    @Provides
    fun provideMeterRecordDao(database: UtilityDatabase): MeterRecordDao {
        return database.meterRecordDao()
    }

    @Singleton
    @Provides
    fun providePaymentDao(database: UtilityDatabase): PaymentDao {
        return database.paymentDao()
    }
}
