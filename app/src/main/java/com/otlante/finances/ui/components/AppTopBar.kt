package com.otlante.finances.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

/**
 * Composable function that displays a centered top app bar with an optional
 * navigation icon and customizable action buttons.
 *
 * @param title the text displayed as the title of the app bar
 * @param navigationIconResId optional drawable resource ID for the navigation icon
 * @param onNavigationClick optional callback invoked when the navigation icon is clicked
 * @param actions optional composable lambda to define action icons/buttons in the app bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    @DrawableRes navigationIconResId: Int? = null,
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        modifier = Modifier,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if (navigationIconResId != null && onNavigationClick != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(painterResource(navigationIconResId), contentDescription = "")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = actions
    )
}
