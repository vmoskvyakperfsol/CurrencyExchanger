package com.example.currencyexchanger.db

import kotlinx.coroutines.flow.Flow

class LocalDataSourceImpl(
    private val currencyDao: CurrencyDao,
    private val transactionDao: TransactionDao
) : LocalDataSource {
    override suspend fun getBalance(currency: String) =
        currencyDao.getBalance(currency)

    override suspend fun getBalances(): Flow<List<CurrencyBalanceModel>> = currencyDao.getBalances()

    override suspend fun upsertBalance(currencyBalanceModel: CurrencyBalanceModel) {
        currencyDao.upsertBalance(currencyBalanceModel)
    }

    override suspend fun addTransaction() {
        transactionDao.addTransaction(
            TransactionModel(
                time = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getTransactionsCount(): Flow<Int> =
        transactionDao.getNumberOfTransactions()
}
