package com.example.currencyexchanger.usecase

import com.example.currencyexchanger.COMMISSION
import com.example.currencyexchanger.NUMBER_OF_FREE_CURRENCY_CHANGES
import com.example.currencyexchanger.domain.CurrencyBalance
import com.example.currencyexchanger.repository.CurrencyExchangerRepository
import com.example.currencyexchanger.ui.screens.TransitionDetail
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SubmitTransactionUseCase @Inject constructor(
    private val repository: CurrencyExchangerRepository
) {
    suspend fun invoke(
        sellAmount: Float,
        newBalance: CurrencyBalance,
        transactionBalance: CurrencyBalance
    ): TransitionDetail {
        val commissionFee = repository.getNumberOfTransactions().firstOrNull()?.let { number ->
            if (number >= NUMBER_OF_FREE_CURRENCY_CHANGES) {
                COMMISSION
            } else {
                0f
            }
        } ?: 0f

        repository.submitTransaction(
            newBalance.copy(amount = newBalance.amount - commissionFee),
            transactionBalance
        )
        return TransitionDetail(
            sellAmount = CurrencyBalance(newBalance.currency, sellAmount),
            receivedAmount = CurrencyBalance(transactionBalance.currency, transactionBalance.amount),
            commissionFee = commissionFee,
        )
    }
}
