package com.example.currencyexchanger.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT amount FROM currency_balances WHERE currency = :currency")
    fun getBalance(currency: String): Flow<Float>

    @Query("SELECT * FROM currency_balances")
    fun getBalances(): Flow<List<CurrencyBalanceModel>>

    @Upsert
    suspend fun upsertBalance(balance: CurrencyBalanceModel)
}
