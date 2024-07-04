package com.example.currencyexchanger.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.currencyexchanger.R

@Composable
fun CurrencyConvertedDialog(
    message: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onConfirm() },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(stringResource(id = R.string.done))
            }
        },
        title = {
            Text(stringResource(id = R.string.currency_converted))
        },
        text = {
            Text(text = message)
        }
    )
}
