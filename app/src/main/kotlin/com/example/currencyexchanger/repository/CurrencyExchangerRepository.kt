package com.example.currencyexchanger.repository

import com.example.currencyexchanger.domain.CurrencyBalance
import com.example.currencyexchanger.domain.CurrencyExchangeRates
import kotlinx.coroutines.flow.Flow

interface CurrencyExchangerRepository{

    suspend fun getCurrencyExchangeRates(): Flow<Result<CurrencyExchangeRates>>

    suspend fun getBalance(currency: String): Flow<CurrencyBalance>

    suspend fun getBalances(): Flow<List<CurrencyBalance>>

    suspend fun upsertBalance(currency: String, amount: Float)

    suspend fun submitTransaction(
        newBalance: CurrencyBalance,
        transactionBalance: CurrencyBalance
    )

    suspend fun getNumberOfTransactions(): Flow<Int>
}
