package com.example.currencyexchanger.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.currencyexchanger.INITIAL_AMOUNT
import com.example.currencyexchanger.INITIAL_CURRENCY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider

class RoomDbInitializer(
    private val userProvider: Provider<CurrencyDao>,
) : RoomDatabase.Callback() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        populateInitialBalance()
    }

    private suspend fun populateInitialBalance() {
        userProvider.get().upsertBalance(CurrencyBalanceModel(INITIAL_CURRENCY, INITIAL_AMOUNT))
    }
}
