package com.otlante.finances

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.otlante.finances.screens.CheckScreen
import com.otlante.finances.screens.ExpensesScreen
import com.otlante.finances.screens.IncomeScreen
import com.otlante.finances.screens.ItemsOfExpensesScreen
import com.otlante.finances.screens.SettingsScreen

fun NavGraphBuilder.expensesGraph() {
    navigation(startDestination = "expenses/main", route = BottomNavItem.Expenses.route) {
        composable("expenses/main") {
            ExpensesScreen()
        }
    }
}

fun NavGraphBuilder.incomeGraph() {
    navigation(startDestination = "income/main", route = BottomNavItem.Income.route) {
        composable("income/main") {
            IncomeScreen()
        }
    }
}

fun NavGraphBuilder.checkGraph() {
    navigation(startDestination = "check/main", route = BottomNavItem.Check.route) {
        composable("check/main") {
            CheckScreen()
        }
    }
}

fun NavGraphBuilder.itemsOfExpensesGraph() {
    navigation(
        startDestination = "itemsOfExpenses/main",
        route = BottomNavItem.ItemsOfExpenses.route
    ) {
        composable("itemsOfExpenses/main") {
            ItemsOfExpensesScreen()
        }
    }
}

fun NavGraphBuilder.settingsGraph() {
    navigation(startDestination = "settings/main", route = BottomNavItem.Settings.route) {
        composable("settings/main") {
            SettingsScreen()
        }
    }
}
