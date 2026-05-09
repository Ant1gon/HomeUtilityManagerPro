package com.ant1gon.homeutility.di

import com.ant1gon.homeutility.data.dao.HouseholdDao
import com.ant1gon.homeutility.data.dao.MeterDao
import com.ant1gon.homeutility.data.repository.HouseholdRepository
import com.ant1gon.homeutility.data.repository.MeterRepository
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
}
