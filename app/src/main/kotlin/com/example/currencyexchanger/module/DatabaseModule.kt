package com.example.currencyexchanger.module

import android.content.Context
import androidx.room.Room
import com.example.currencyexchanger.db.LocalDataSourceImpl
import com.example.currencyexchanger.R
import com.example.currencyexchanger.db.AppDatabase
import com.example.currencyexchanger.db.CurrencyDao
import com.example.currencyexchanger.db.LocalDataSource
import com.example.currencyexchanger.db.RoomDbInitializer
import com.example.currencyexchanger.db.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        userProvider: Provider<CurrencyDao>,
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "${context.getString(R.string.app_name)}.db"
        ).addCallback(
            RoomDbInitializer(userProvider = userProvider)
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(db: AppDatabase): CurrencyDao = db.currencyDao

    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase): TransactionDao = db.transactionDao

    @Provides
    fun provideLocalDataSource(
        currencyDao: CurrencyDao,
        transactionDao: TransactionDao
    ): LocalDataSource = LocalDataSourceImpl(
        currencyDao, transactionDao
    )
}
