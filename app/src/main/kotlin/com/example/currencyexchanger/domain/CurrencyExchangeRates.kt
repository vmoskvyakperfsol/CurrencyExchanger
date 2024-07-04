package com.example.currencyexchanger.domain

data class CurrencyExchangeRates(
    val base: String,
    val date: String,
    val rates: List<Rate>
) {
    data class Rate(
        val currency: String,
        val rate: Float,
    )
}
