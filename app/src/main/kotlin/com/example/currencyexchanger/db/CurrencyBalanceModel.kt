package com.example.currencyexchanger.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_balances")
data class CurrencyBalanceModel(
    @PrimaryKey val currency: String,
    var amount: Float
)
