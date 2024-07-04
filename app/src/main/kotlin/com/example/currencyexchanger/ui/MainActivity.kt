package com.example.currencyexchanger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.currencyexchanger.R
import com.example.currencyexchanger.navigation.Destination
import com.example.currencyexchanger.navigation.mainNavigationGraph
import com.example.currencyexchanger.ui.components.ScaffoldWithTopAppBar
import com.example.currencyexchanger.ui.theme.CurrencyExchangerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyExchangerTheme {
                ScaffoldWithTopAppBar(title = stringResource(id = R.string.currency_converter)) {
                    val mainNavController = rememberNavController()
                    NavHost(
                        navController = mainNavController,
                        startDestination = Destination.Home
                    ) {
                        mainNavigationGraph(mainNavController)
                    }
                }
            }
        }
    }
}
