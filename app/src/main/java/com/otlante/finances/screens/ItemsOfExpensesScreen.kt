package com.otlante.finances.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.otlante.finances.R
import com.otlante.finances.model.ItemOfExpensesItem

@Composable
fun ItemsOfExpensesScreen() {

    val mockItemsOfExpenses = listOf(
        ItemOfExpensesItem(emoji = "🏡", title = "Аренда квартиры"),
        ItemOfExpensesItem(emoji = "👗", title = "Одежда"),
        ItemOfExpensesItem(emoji = "🐶", title = "На собачку"),
        ItemOfExpensesItem(emoji = "🐶", title = "На собачку"),
        ItemOfExpensesItem(emoji = "РК", title = "Ремонт квартиры"),
        ItemOfExpensesItem(emoji = "🍭", title = "Продукты"),
        ItemOfExpensesItem(emoji = "🏋️", title = "Спортзал"),
        ItemOfExpensesItem(emoji = "💊", title = "Медицина"),
    )

    val query by remember { mutableStateOf("") }

    Column {
        ScreenHeader(
            titleText = stringResource(R.string.my_expense_items),
        )

        TextField(
            value = query,
            onValueChange = {},
            placeholder = { Text(stringResource(R.string.find_expense_item)) },
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search)
                    )
                }
            },
            shape = RoundedCornerShape(0),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn {
            items(mockItemsOfExpenses) { item ->
                ListItem(
                    emoji = item.emoji,
                    title = item.title,
                )

                HorizontalDivider()
            }
        }
    }
}
