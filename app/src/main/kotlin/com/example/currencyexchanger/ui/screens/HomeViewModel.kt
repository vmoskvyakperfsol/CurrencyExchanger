package com.example.currencyexchanger.ui.screens

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchanger.domain.CurrencyBalance
import com.example.currencyexchanger.domain.CurrencyExchangeRates
import com.example.currencyexchanger.usecase.GetBalanceUseCase
import com.example.currencyexchanger.usecase.GetCurrencyExchangeRatesUseCase
import com.example.currencyexchanger.usecase.SubmitTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrencyExchangeRatesUseCase: GetCurrencyExchangeRatesUseCase,
    private val getBalanceUseCase: GetBalanceUseCase,
    private val submitTransactionUseCase: SubmitTransactionUseCase,
) : ViewModel() {

    private var shouldShowLoading: Boolean = false
        set(value) {
            field = value
            updateState()
        }

    private var shouldShowError: Boolean = false
        set(value) {
            field = value
            updateState()
        }

    private var myBalances: List<CurrencyBalance> = emptyList()
        set(value) {
            field = value
            updateState()
        }

    private var currencyExchangeRates: List<CurrencyExchangeRates.Rate> = emptyList()
        set(value) {
            field = value
            updateState()
        }

    private var sellAmount: String = ""
        set(value) {
            field = value
            updateState()
        }

    private var receiveAmount: String = ""
        set(value) {
            field = value
            updateState()
        }

    private var sellCurrency: CurrencyExchangeRates.Rate = CurrencyExchangeRates.Rate("", 0f)
        set(value) {
            field = value
            updateState()
        }

    private var receiveCurrency: CurrencyExchangeRates.Rate = CurrencyExchangeRates.Rate("", 0f)
        set(value) {
            field = value
            updateState()
        }

    private var isSubmitEnabled: Boolean = false
        set(value) {
            field = value
            updateState()
        }

    private var transitionDetail: TransitionDetail? = null
        set(value) {
            field = value
            updateState()
        }

    private val state
        get() = HomeUIState(
            shouldShowLoading = shouldShowLoading,
            shouldShowError = shouldShowError,
            myBalances = myBalances,
            currencyExchangeRates = currencyExchangeRates,
            sellAmount = sellAmount,
            receiveAmount = receiveAmount,
            sellCurrency = sellCurrency,
            receiveCurrency = receiveCurrency,
            isSubmitEnabled = isSubmitEnabled,
            transitionDetail = transitionDetail,
            onDialogTransitionDetailShown = {
                transitionDetail = null
            },
        )

    private val _homeUIState = MutableStateFlow(state)
    val homeUIState = _homeUIState.asStateFlow()

    private var selectedBalanceAmount: CurrencyBalance = CurrencyBalance("", 0f)

    init {
        loadCurrencyExchangeRatesAndCurrentBalance()
        startUpdateExchangeRates()
    }

    private fun startUpdateExchangeRates() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                getCurrencyExchangeRatesUseCase.invoke()
                    .collect { result ->
                        if (result.isSuccess) {
                            result.getOrNull()?.let {
                                updateExchangeRates(it)
                                Log.d("CurrencyExchanger",
                                    "exchange rates are updated. But please go to back end developers," +
                                            "be nice and ask them to get rid of this"
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun loadCurrencyExchangeRatesAndCurrentBalance() {
        viewModelScope.launch {
            showLoading()
            getCurrencyExchangeRatesUseCase.invoke()
                .collect { result ->
                    if (result.isSuccess) {
                        result.getOrNull()?.let {
                            updateExchangeRates(it)
                            loadMyBalances(it)
                        } ?: showError()
                    } else {
                        showError()
                    }
                }
        }
    }

    private suspend fun loadMyBalances(exchangeRates: CurrencyExchangeRates) {
        viewModelScope.launch {
            getBalanceUseCase.invoke()
                .collect {
                    if (it.isEmpty()) {
                        showError()
                        return@collect
                    }
                    val baseCurrencyRate =
                        it.firstOrNull { rate -> rate.currency == exchangeRates.base }
                    baseCurrencyRate?.let { currencyRate ->
                        val baseRate =
                            exchangeRates.rates.firstOrNull { baseRate -> baseRate.currency == baseCurrencyRate.currency }
                        sellCurrency =
                            CurrencyExchangeRates.Rate(currencyRate.currency, baseRate?.rate ?: 1f)
                        receiveCurrency = exchangeRates.rates[0]

                        selectedBalanceAmount = currencyRate
                        onSellAmountChanged(selectedBalanceAmount.amount.toString())
                    }

                    myBalances = it
                    shouldShowLoading = false
                }
        }
    }

    private fun updateExchangeRates(exchangeRates: CurrencyExchangeRates) {
        currencyExchangeRates = exchangeRates.rates
    }

    private fun showLoading() {
        shouldShowLoading = true
        shouldShowError = false
    }

    private fun hideLoading() {
        shouldShowLoading = false
    }

    private fun showError() {
        shouldShowError = true
        hideLoading()
    }

    private fun updateState() {
        _homeUIState.update { state }
    }

    private fun updateReceiveAmount() {
        sellAmount.toFloatOrNull()?.let {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING

            viewModelScope.launch {
                getBalanceUseCase.invoke().collect { balance ->
                    balance.firstOrNull { it.currency == sellCurrency.currency }?.let {
                        selectedBalanceAmount = it
                        currencyExchangeRates.firstOrNull { rate -> rate.currency == it.currency }
                            ?.let { rates ->
                                val amount =
                                    (sellAmount.toFloat() / rates.rate) * receiveCurrency.rate
                                receiveAmount = df.format(amount)
                            }
                    }
                }
            }
        }
    }

    private fun onSellAmountChanged(amount: String) {
        sellAmount = amount
        sellAmount.toFloatOrNull()?.let {
            isSubmitEnabled = selectedBalanceAmount.amount - it >= 0
        }
        updateReceiveAmount()
    }

    private fun onSellCurrencyChanged(currency: CurrencyExchangeRates.Rate) {
        sellCurrency = currency
        viewModelScope.launch {
            getBalanceUseCase.invoke().collect { balance ->
                balance.firstOrNull { it.currency == sellCurrency.currency }?.let {
                    sellAmount = it.amount.toString()
                    updateReceiveAmount()
                }
            }
        }
    }

    private fun onReceiveCurrencyChanged(currencyExchangeRates: CurrencyExchangeRates.Rate) {
        receiveCurrency = currencyExchangeRates
        updateReceiveAmount()
    }

    private fun onSubmitTransaction() {
        viewModelScope.launch {
            transitionDetail = submitTransactionUseCase.invoke(
                sellAmount = sellAmount.toFloat(),
                newBalance = CurrencyBalance(
                    selectedBalanceAmount.currency,
                    selectedBalanceAmount.amount - sellAmount.toFloat()
                ),
                transactionBalance = CurrencyBalance(
                    receiveCurrency.currency,
                    receiveAmount.toFloat()
                )
            )
        }
    }

    fun onAction(action: HomeAction) {
        when(action) {
            is HomeAction.ReceiveCurrencyChanged -> {
                onReceiveCurrencyChanged(action.rate)
            }
            is HomeAction.SellAmountChanged -> onSellAmountChanged(action.amount)
            is HomeAction.SellCurrencyChanged -> onSellCurrencyChanged(action.rate)
            HomeAction.SubmitTransaction -> onSubmitTransaction()
        }
    }
}

@Stable
data class HomeUIState(
    val shouldShowLoading: Boolean,
    val shouldShowError: Boolean,
    val myBalances: List<CurrencyBalance>,
    val currencyExchangeRates: List<CurrencyExchangeRates.Rate>,
    val sellAmount: String,
    val receiveAmount: String,
    val sellCurrency: CurrencyExchangeRates.Rate,
    val receiveCurrency: CurrencyExchangeRates.Rate,
    val isSubmitEnabled: Boolean,
    val transitionDetail: TransitionDetail?,
    val onDialogTransitionDetailShown: () -> Unit,
)

sealed interface HomeAction {
    data class SellAmountChanged(val amount: String): HomeAction
    data class SellCurrencyChanged(val rate: CurrencyExchangeRates.Rate): HomeAction
    data class ReceiveCurrencyChanged(val rate: CurrencyExchangeRates.Rate): HomeAction
    data object SubmitTransaction: HomeAction
}

data class TransitionDetail(
    val sellAmount: CurrencyBalance,
    val receivedAmount: CurrencyBalance,
    val commissionFee: Float
)
