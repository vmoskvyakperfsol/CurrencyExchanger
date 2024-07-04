package com.example.currencyexchanger.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.currencyexchanger.R
import com.example.currencyexchanger.domain.CurrencyBalance
import com.example.currencyexchanger.domain.CurrencyExchangeRates
import com.example.currencyexchanger.ui.components.ConverterField
import com.example.currencyexchanger.ui.components.ConverterType
import com.example.currencyexchanger.ui.theme.CurrencyExchangerTheme
import com.example.currencyexchanger.ui.theme.balanceBackgroundColor

@Composable
fun Home(
    state: HomeUIState,
    onAction: (HomeAction) -> Unit,
    onTransitionSuccess: (String) -> Unit
) {
    state.transitionDetail?.let {
        onTransitionSuccess(
            stringResource(
                id = R.string.transition_success_message,
                "${it.sellAmount.amount} ${it.sellAmount.currency}",
                "${it.receivedAmount.amount} ${it.receivedAmount.currency}",
                it.commissionFee.toString()
            )
        )
        state.onDialogTransitionDetailShown()
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        if (state.shouldShowLoading) {
            // implement loading, error states
        } else {
            TopImage()
            ConverterField(
                type = ConverterType.SELL,
                amount = state.sellAmount,
                rates = state.myBalances
                    .map {
                        CurrencyExchangeRates.Rate(
                            it.currency,
                            it.amount
                        )
                    }, //todo: not so right, because amount and rate it's different purposes
                currency = state.sellCurrency,
                onAmountChanged = { amount ->
                    onAction(HomeAction.SellAmountChanged(amount))
                },
                onCurrencyChanged = { rate ->
                    onAction(HomeAction.SellCurrencyChanged(rate))
                }
            )
            HorizontalDivider()
            ConverterField(
                type = ConverterType.RECEIVE,
                amount = state.receiveAmount,
                rates = state.currencyExchangeRates,
                onAmountChanged = { _ ->
                },
                onCurrencyChanged = { rate ->
                    onAction(HomeAction.ReceiveCurrencyChanged(rate))
                },
                currency = state.receiveCurrency,
            )
            ConvertButton(state.isSubmitEnabled) {
                onAction(HomeAction.SubmitTransaction)
            }
            MyBalances(balances = state.myBalances)
        }
    }
}

@Composable
fun MyBalances(
    balances: List<CurrencyBalance>
) {
    Text(
        text = stringResource(id = R.string.my_balances),
        modifier = Modifier.padding(vertical = 8.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(balanceBackgroundColor)
    ) {
        balances.forEach { balance ->
            Row {
                Text(
                    text = balance.currency,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )
                Text(
                    text = balance.amount.toString(),
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ConvertButton(isEnabled: Boolean, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        content = { Text(text = stringResource(id = R.string.submit)) },
        enabled = isEnabled,
        onClick = { onClick() })
}

@Composable
private fun TopImage() {
    Box(
        modifier = Modifier
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.world),
            contentDescription = "",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CurrencyExchangerTheme {
        Box(modifier = Modifier.background(Color.LightGray)) {
            Home(
                HomeUIState(
                    shouldShowLoading = false,
                    shouldShowError = false,
                    myBalances = listOf(
                        CurrencyBalance("EUR", 1000f),
                        CurrencyBalance("AED", 25.4f)
                    ),
                    currencyExchangeRates = listOf(
                        CurrencyExchangeRates.Rate("EUR", 1f),
                        CurrencyExchangeRates.Rate("AED", 1f)
                    ),
                    sellAmount = "1000",
                    receiveAmount = "42.5",
                    sellCurrency = CurrencyExchangeRates.Rate("EUR", 1f),
                    receiveCurrency = CurrencyExchangeRates.Rate("AED", 1f),
                    isSubmitEnabled = true,
                    transitionDetail = null,
                    onDialogTransitionDetailShown = {}
                ),
                onAction = { },
                onTransitionSuccess = {}
            )
        }
    }
}
