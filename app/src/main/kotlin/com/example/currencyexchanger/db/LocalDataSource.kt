package com.example.currencyexchanger.db

import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getBalance(currency: String): Flow<Float>

    suspend fun getBalances(): Flow<List<CurrencyBalanceModel>>

    suspend fun upsertBalance(currencyBalanceModel: CurrencyBalanceModel)

    suspend fun addTransaction()

    suspend fun getTransactionsCount(): Flow<Int>
}
