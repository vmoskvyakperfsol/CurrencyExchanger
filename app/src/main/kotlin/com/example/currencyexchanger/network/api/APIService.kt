package com.example.currencyexchanger.network.api

import com.example.currencyexchanger.network.model.CurrencyExchangeRatesModel
import retrofit2.http.GET

interface APIService {

    @GET("tasks/api/currency-exchange-rates")
    suspend fun getCurrencyExchangeRates(): CurrencyExchangeRatesModel
}
