package com.otlante.finances.ui.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.otlante.finances.ui.screens.account.AccountScreen
import com.otlante.finances.ui.screens.addOrEditTrans.AddOrEditIncomeOrExpenseScreen
import com.otlante.finances.ui.screens.articles.ArticlesScreen
import com.otlante.finances.ui.screens.editAccount.EditAccountScreen
import com.otlante.finances.ui.screens.expenses.ExpensesScreen
import com.otlante.finances.ui.screens.history.HistoryScreen
import com.otlante.finances.ui.screens.income.IncomeScreen
import com.otlante.finances.ui.screens.settings.SettingsScreen
import com.otlante.finances.ui.screens.settings.VibrationSettingsScreen
import com.otlante.finances.ui.screens.settings.PrimaryColorPickerScreen
import com.otlante.finances.ui.screens.settings.SyncSettingsScreen
import com.otlante.finances.ui.screens.settings.AboutScreen
import com.otlante.finances.ui.screens.pinCode.PinCodeSetupScreen
import com.otlante.finances.ui.screens.pinCode.PinCodeEntryScreen
import com.otlante.finances.ui.screens.pinCode.PinCodeDisableScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otlante.finances.ui.screens.settings.SettingsViewModel
import com.otlante.finances.ui.composition.LocalViewModelFactory

/**
 * Composable function that defines the navigation graph for the app.
 *
 * Configures available destinations and binds each route to its corresponding
 * screen composable.
 *
 * @param navController the [NavHostController] managing app navigation
 * @param snackBarHostState the [SnackbarHostState] used to show snackbars across screens
 */

enum class TransactionMode {
    ADD_INCOME, ADD_EXPENSE, EDIT_INCOME, EDIT_EXPENSE
}

@Composable
fun AppNavGraph(navController: NavHostController, snackBarHostState: SnackbarHostState) {
    NavHost(
        navController = navController,
        startDestination = NavDestination.BottomNav.Expenses.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        modifier = Modifier
    ) {
        composable(route = NavDestination.BottomNav.Expenses.route) {
            ExpensesScreen(snackBarHostState = snackBarHostState, navController = navController)
        }
        composable(route = NavDestination.BottomNav.Incomes.route) {
            IncomeScreen(snackBarHostState = snackBarHostState, navController = navController)
        }
        composable(route = NavDestination.BottomNav.Account.route) {
            AccountScreen(snackBarHostState = snackBarHostState)
        }
        composable(route = NavDestination.BottomNav.Articles.route) {
            ArticlesScreen(snackBarHostState = snackBarHostState)
        }
        composable(route = NavDestination.BottomNav.Settings.route) {
            SettingsScreen(
                onVibrationClick = { navController.navigate(NavDestination.VibrationSettings.route) },
                onPrimaryColorClick = { navController.navigate(NavDestination.PrimaryColorPicker.route) },
                onPinCodeClick = { navController.navigate(NavDestination.PinCodeSetup.route) },
                onPinCodeDisableClick = { navController.navigate(NavDestination.PinCodeDisable.route) },
                onSyncClick = { navController.navigate(NavDestination.SyncSettings.route) },
                onAboutClick = { navController.navigate(NavDestination.About.route) }
            )
        }
        composable(route = NavDestination.VibrationSettings.route) {
            VibrationSettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(route = NavDestination.SyncSettings.route) {
            SyncSettingsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(route = NavDestination.About.route) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }
        composable(
            route = NavDestination.History.routeWithArgument,
            arguments = NavDestination.History.arguments
        ) {
            HistoryScreen(
                snackBarHostState = snackBarHostState,
                navBackStackEntry = it,
            )
        }
        composable(route = NavDestination.EditAccount.route) {
            EditAccountScreen(
                snackBarHostState = snackBarHostState,
                navController = navController
            )
        }
        composable(
            route = NavDestination.AddOrEditTrans.routeWithArgument,
            arguments = NavDestination.AddOrEditTrans.arguments
        ) {
            AddOrEditIncomeOrExpenseScreen(
                snackBarHostState = snackBarHostState,
                navController = navController,
                navBackStackEntry = it
            )
        }
        composable(route = NavDestination.PrimaryColorPicker.route) {
            val factory = LocalViewModelFactory.current
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
            PrimaryColorPickerScreen(
                onColorClick = {
                    settingsViewModel.setPrimaryColor(it)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = NavDestination.PinCodeSetup.route) {
            PinCodeSetupScreen(
                onBack = { navController.popBackStack() },
                onPinSet = { navController.popBackStack() }
            )
        }

        composable(route = NavDestination.PinCodeEntry.route) {
            PinCodeEntryScreen(
                onPinCorrect = {
                    navController.navigate(NavDestination.Main.route) {
                        popUpTo(NavDestination.PinCodeEntry.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = NavDestination.PinCodeDisable.route) {
            PinCodeDisableScreen(
                onBack = { navController.popBackStack() },
                onPinDisabled = { navController.popBackStack() }
            )
        }
    }
}
