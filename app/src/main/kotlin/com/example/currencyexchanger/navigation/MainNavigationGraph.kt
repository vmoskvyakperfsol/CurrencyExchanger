package com.example.currencyexchanger.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import com.example.currencyexchanger.ui.screens.CurrencyConvertedDialog
import com.example.currencyexchanger.ui.screens.Home
import com.example.currencyexchanger.ui.screens.HomeViewModel

fun NavGraphBuilder.mainNavigationGraph(
    mainNavController: NavController
) {
    composable<Destination.Home> {
        val viewModel: HomeViewModel = hiltViewModel()
        val state by viewModel.homeUIState.collectAsState()

        Home(state,
            onAction = viewModel::onAction,
            onTransitionSuccess = { message ->
                mainNavController.navigate(Destination.CurrencyConvertedDialog(message))
            }
        )
    }

    dialog<Destination.CurrencyConvertedDialog> {
        val args = it.toRoute<Destination.CurrencyConvertedDialog>()

        CurrencyConvertedDialog(args.message) {
            mainNavController.popBackStack()
        }
    }
}
