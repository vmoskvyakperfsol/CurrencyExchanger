package com.example.currencyexchanger.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT COUNT(id) FROM currency_transactions")
    fun getNumberOfTransactions(): Flow<Int>

    @Insert
    suspend fun addTransaction(transactionModel: TransactionModel)
}
