package com.example.currencyexchanger.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    data object Home: Destination()

    @Serializable
    data class CurrencyConvertedDialog(
        val message: String,
    ): Destination()
}
