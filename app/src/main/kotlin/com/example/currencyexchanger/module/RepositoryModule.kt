package com.example.currencyexchanger.module

import com.example.currencyexchanger.db.LocalDataSource
import com.example.currencyexchanger.network.RemoteDataSource
import com.example.currencyexchanger.repository.CurrencyExchangerRepository
import com.example.currencyexchanger.repository.CurrencyExchangerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): CurrencyExchangerRepository =
        CurrencyExchangerRepositoryImpl(remoteDataSource, localDataSource)
}
