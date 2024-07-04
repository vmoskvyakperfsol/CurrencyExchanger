package com.example.currencyexchanger.usecase

import com.example.currencyexchanger.domain.CurrencyBalance
import com.example.currencyexchanger.repository.CurrencyExchangerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: CurrencyExchangerRepository
) {
    suspend operator fun invoke(): Flow<List<CurrencyBalance>> = repository.getBalances()
}
