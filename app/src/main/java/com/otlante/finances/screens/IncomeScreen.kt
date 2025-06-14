package com.otlante.finances.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.otlante.finances.R
import com.otlante.finances.formatAmount
import com.otlante.finances.model.IncomeItem

@Composable
fun IncomeScreen() {

    val mockIncomes = listOf(
        IncomeItem(title = "Зарплата", amount = 500_000.0, onClick = {}),
        IncomeItem(title = "Подработка", amount = 100_000.0, onClick = {}),
    )

    val amountIncome = mockIncomes.sumOf { it.amount }

    Column {
        ScreenHeader(
            titleText = stringResource(R.string.income_today),
            iconImageId = R.drawable.ic_history,
            iconContentDescription = stringResource(R.string.history)
        )

        ListItem(
            type = ListItemType.SUMMARIZE,
            title = stringResource(R.string.total),
            rightText = formatAmount(amountIncome),
        )

        HorizontalDivider()

        LazyColumn {
            items(mockIncomes) { item ->
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