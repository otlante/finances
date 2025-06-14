package com.otlante.finances.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.otlante.finances.R
import com.otlante.finances.formatAmount

@Composable
fun CheckScreen() {
    Column {
        ScreenHeader(
            titleText = stringResource(R.string.my_check),
            iconImageId = R.drawable.ic_edit,
            iconContentDescription = stringResource(R.string.change)
        )

        ListItem(
            type = ListItemType.SUMMARIZE,
            emoji = "\uD83D\uDCB0",
            title = stringResource(R.string.balance),
            showArrow = true,
            rightText = formatAmount(-670000.0),
        )

        HorizontalDivider()

        ListItem(
            type = ListItemType.SUMMARIZE,
            title = stringResource(R.string.currency),
            showArrow = true,
            rightText = "₽",
        )

        HorizontalDivider()
    }
}