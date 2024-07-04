package com.example.currencyexchanger.module

import com.example.currencyexchanger.repository.CurrencyExchangerRepository
import com.example.currencyexchanger.usecase.GetBalanceUseCase
import com.example.currencyexchanger.usecase.GetCurrencyExchangeRatesUseCase
import com.example.currencyexchanger.usecase.SubmitTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideGetCurrencyExchangeRatesUseCase(
        repository: CurrencyExchangerRepository
    ): GetCurrencyExchangeRatesUseCase =
        GetCurrencyExchangeRatesUseCase(repository)

    @Provides
    fun provideGetBalanceUseCase(
        repository: CurrencyExchangerRepository
    ): GetBalanceUseCase = GetBalanceUseCase(repository)

    @Provides
    fun provideSubmitTransactionUseCase(
        repository: CurrencyExchangerRepository
    ): SubmitTransactionUseCase = SubmitTransactionUseCase(repository)

}
