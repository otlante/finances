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
            SettingsScreen()
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
    }
}
