package com.otlante.finances

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: @Composable () -> Unit
) {

    object Expenses : BottomNavItem(
        "expenses",
        "Расходы",
        {
            Icon(
                painter = painterResource(R.drawable.ic_expenses),
                contentDescription = "Расходы"
            )
        }
    )

    object Income : BottomNavItem(
        "income",
        "Доходы",
        {
            Icon(
                painter = painterResource(R.drawable.ic_income),
                contentDescription = "Доходы"
            )
        }
    )

    object Check : BottomNavItem(
        "check",
        "Счет",
        {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = "Счет"
            )
        }
    )

    object ItemsOfExpenses : BottomNavItem(
        "itemsOfExpenses",
        "Статьи",
        {
            Icon(
                painter = painterResource(R.drawable.ic_items_of_expenses),
                contentDescription = "Статьи"
            )
        }
    )

    object Settings : BottomNavItem(
        "settings",
        "Настройки",
        {
            Icon(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Настройки"
            )
        }
    )
}