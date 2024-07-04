package com.example.currencyexchanger.network

import com.example.currencyexchanger.network.model.CurrencyExchangeRatesModel

interface RemoteDataSource {
    suspend fun getCurrencyExchangeRates(): CurrencyExchangeRatesModel
}
