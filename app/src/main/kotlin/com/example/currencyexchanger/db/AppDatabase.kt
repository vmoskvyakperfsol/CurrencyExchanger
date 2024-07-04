package com.example.currencyexchanger.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = true,
    entities = [CurrencyBalanceModel::class, TransactionModel::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract val currencyDao: CurrencyDao

    abstract val transactionDao: TransactionDao
}
