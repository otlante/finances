package com.otlante.finances.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.otlante.finances.R
import com.otlante.finances.formatAmount
import com.otlante.finances.model.ExpenseItem

@Composable
fun ExpensesScreen() {

    val mockExpenses = listOf(
        ExpenseItem(emoji = "🏡", title = "Аренда квартиры", amount = 100_000.0, onClick = {}),
        ExpenseItem(emoji = "👗", title = "Одежда", amount = 100_000.0, onClick = {}),
        ExpenseItem(
            emoji = "🐶",
            title = "На собачку",
            subtitle = "Джек",
            amount = 100_000.0,
            onClick = {}),
        ExpenseItem(
            emoji = "🐶",
            title = "На собачку",
            subtitle = "Энни",
            amount = 100_000.0,
            onClick = {}),
        ExpenseItem(emoji = "РК", title = "Ремонт квартиры", amount = 100_000.0, onClick = {}),
        ExpenseItem(emoji = "🍭", title = "Продукты", amount = 100_000.0, onClick = {}),
        ExpenseItem(emoji = "🏋️", title = "Спортзал", amount = 100_000.0, onClick = {}),
        ExpenseItem(emoji = "💊", title = "Медицина", amount = 100_000.0, onClick = {}),
    )

    val amountSpent = mockExpenses.sumOf { it.amount }

    Column {
        ScreenHeader(
            titleText = stringResource(R.string.expenses_today),
            iconImageId = R.drawable.ic_history,
            iconContentDescription = stringResource(R.string.history)
        )

        ListItem(
            type = ListItemType.SUMMARIZE,
            title = stringResource(R.string.total),
            rightText = formatAmount(amountSpent),
        )

        HorizontalDivider()

        LazyColumn {
            items(mockExpenses) { item ->
                ListItem(
                    emoji = item.emoji,
                    title = item.title,
                    subtitle = item.subtitle,
                    rightText = formatAmount(item.amount),
                    showArrow = true,
                    onClick = item.onClick
                )
                HorizontalDivider()
            }
        }
    }
}
