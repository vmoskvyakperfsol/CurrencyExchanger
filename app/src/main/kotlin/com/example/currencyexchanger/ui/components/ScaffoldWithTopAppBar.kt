package com.example.currencyexchanger.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.currencyexchanger.ui.theme.backgroundGradientEnd
import com.example.currencyexchanger.ui.theme.backgroundGradientStart
import com.example.currencyexchanger.ui.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithTopAppBar(
    title: String,
    content: @Composable () -> Unit
) {
    Scaffold(
        contentColor = textColor,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                title = {
                    Text(
                        title,
                        maxLines = 1,
                        color = textColor
                    )
                },
            )
        },
    ) { innerPadding ->
        val gradientLinear =
            Brush.linearGradient(listOf(backgroundGradientStart, backgroundGradientEnd))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientLinear)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
            ,
        ) {
            content()
        }
    }
}
