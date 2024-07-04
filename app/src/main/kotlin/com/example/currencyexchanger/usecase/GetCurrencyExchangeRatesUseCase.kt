package com.example.currencyexchanger.usecase

import com.example.currencyexchanger.domain.CurrencyExchangeRates
import com.example.currencyexchanger.repository.CurrencyExchangerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrencyExchangeRatesUseCase @Inject constructor(
    private val repository: CurrencyExchangerRepository
) {
    suspend operator fun invoke(): Flow<Result<CurrencyExchangeRates>> =
        repository.getCurrencyExchangeRates()
}
