package com.ant1gon.homeutility.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ant1gon.homeutility.data.converter.LocalDateTimeConverter
import com.ant1gon.homeutility.data.dao.*
import com.ant1gon.homeutility.data.entity.*

@Database(
    entities = [
        HouseholdEntity::class,
        MeterEntity::class,
        TariffEntity::class,
        MeterRecordEntity::class,
        PaymentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class UtilityDatabase : RoomDatabase() {
    abstract fun householdDao(): HouseholdDao
    abstract fun meterDao(): MeterDao
    abstract fun tariffDao(): TariffDao
    abstract fun meterRecordDao(): MeterRecordDao
    abstract fun paymentDao(): PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: UtilityDatabase? = null

        fun getDatabase(context: Context): UtilityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UtilityDatabase::class.java,
                    "utility_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
