package com.example.currencyexchanger.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyExchangeRatesModel(
    @field:Json(name = "base")
    val base: String,

    @field:Json(name = "date")
    val date: String,

    @field:Json(name = "rates")
    val rates: Map<String, Float>,
)
