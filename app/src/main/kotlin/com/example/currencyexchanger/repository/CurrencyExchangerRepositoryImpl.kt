package com.example.currencyexchanger.repository

import com.example.currencyexchanger.db.CurrencyBalanceModel
import com.example.currencyexchanger.db.LocalDataSource
import com.example.currencyexchanger.domain.CurrencyBalance
import com.example.currencyexchanger.domain.CurrencyExchangeRates
import com.example.currencyexchanger.network.RemoteDataSource
import com.example.currencyexchanger.network.model.CurrencyExchangeRatesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyExchangerRepositoryImpl @Inject constructor(
    private val remoteDataSourceImpl: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : CurrencyExchangerRepository {

    override suspend fun getCurrencyExchangeRates() = flow {
        val remoteData = remoteDataSourceImpl.getCurrencyExchangeRates()
        emit(Result.success(remoteData.toDomain()))
    }
        .catch { emit(Result.failure(it)) }

    override suspend fun getBalance(currency: String) =
        localDataSource.getBalance(currency).map { CurrencyBalance(currency, it) }

    override suspend fun getBalances(): Flow<List<CurrencyBalance>> =
        localDataSource.getBalances()
            .map { it.map { model -> model.toDomain() } }

    override suspend fun upsertBalance(currency: String, amount: Float) {
        localDataSource.upsertBalance(CurrencyBalanceModel(currency, amount))
    }

    override suspend fun submitTransaction(
        newBalance: CurrencyBalance,
        transactionBalance: CurrencyBalance
    ) {
        localDataSource.upsertBalance(CurrencyBalanceModel(newBalance.currency, newBalance.amount))

        val transactionAmount =
            localDataSource.getBalance(transactionBalance.currency).firstOrNull()
        val transactionNewAmount =
            transactionAmount?.let { it + transactionBalance.amount } ?: transactionBalance.amount

        localDataSource.upsertBalance(
            CurrencyBalanceModel(
                transactionBalance.currency,
                transactionNewAmount
            )
        )
        localDataSource.addTransaction()
    }

    override suspend fun getNumberOfTransactions() = localDataSource.getTransactionsCount()
}

private fun CurrencyExchangeRatesModel.toDomain() = CurrencyExchangeRates(
    base = base,
    date = date,
    rates = rates.entries.map { CurrencyExchangeRates.Rate(it.key, it.value) },
)

private fun CurrencyBalanceModel.toDomain() = CurrencyBalance(
    currency = currency,
    amount = amount,
)
