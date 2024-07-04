package com.example.currencyexchanger.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ConverterTextField(
    modifier: Modifier = Modifier,
    text: String,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val textFieldColors = TextFieldDefaults.colors().copy(
        disabledTextColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent
    )
    TextField(
        colors = textFieldColors,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        value = text,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier,
        trailingIcon = trailingIcon,
        onValueChange = onValueChange,
        readOnly = readOnly,
    )
}
