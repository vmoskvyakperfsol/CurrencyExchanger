package com.example.currencyexchanger.network.model

import com.example.currencyexchanger.network.RemoteDataSource
import com.example.currencyexchanger.network.api.APIService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: APIService
): RemoteDataSource {
    override suspend fun getCurrencyExchangeRates() = apiService.getCurrencyExchangeRates()
}
