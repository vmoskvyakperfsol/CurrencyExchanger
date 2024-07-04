package com.example.currencyexchanger.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_transactions")
data class TransactionModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var time: Long
)
