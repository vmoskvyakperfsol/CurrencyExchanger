package com.example.currencyexchanger.ui.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.currencyexchanger.R
import com.example.currencyexchanger.domain.CurrencyExchangeRates
import com.example.currencyexchanger.ui.theme.receiveColor
import com.example.currencyexchanger.ui.theme.sellColor

enum class ConverterType { SELL, RECEIVE }

@Composable
fun ConverterField(
    type: ConverterType,
    amount: String,
    currency: CurrencyExchangeRates.Rate,
    rates: List<CurrencyExchangeRates.Rate>,
    onAmountChanged: (amount: String) -> Unit,
    onCurrencyChanged: (CurrencyExchangeRates.Rate) -> Unit,
) {
    val convertText = if (type == ConverterType.SELL) stringResource(id = R.string.sell)
    else stringResource(id = R.string.receive)

    val convertIcon =
        ImageVector.vectorResource(
            id = if (type == ConverterType.SELL) R.drawable.ic_sell
            else R.drawable.ic_receive
        )
    val convertIconTint =
        if (type == ConverterType.SELL) sellColor
        else receiveColor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = convertIcon,
            tint = convertIconTint,
            contentDescription = "",
            modifier = Modifier.fillMaxHeight()
        )
        Text(
            text = convertText, modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )
        ConverterTextField(
            text = amount,
            onValueChange = {
                onAmountChanged(it)
            },
            modifier = Modifier.weight(1f)
        )
        ExposedDropdownMenu(
            modifier = Modifier.weight(1f),
            options = rates,
        ) {
            onCurrencyChanged(it)
        }
    }
}
