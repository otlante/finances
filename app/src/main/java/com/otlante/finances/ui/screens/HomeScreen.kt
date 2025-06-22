package com.otlante.finances.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.otlante.finances.R
import com.otlante.finances.ui.components.AppBottomNavBar
import com.otlante.finances.ui.components.AppFAB
import com.otlante.finances.ui.components.AppTopBar
import com.otlante.finances.ui.nav.AppNavGraph
import com.otlante.finances.ui.nav.NavDestination

@Composable
fun MainScreen() {

    val snackBarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val routesWithFab = setOf(
        NavDestination.BottomNav.Expenses.route,
        NavDestination.BottomNav.Incomes.route,
        NavDestination.BottomNav.Check.route
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            when (currentRoute) {
                NavDestination.BottomNav.Expenses.route -> AppTopBar(
                    title = stringResource(R.string.expenses_top_bar_title),
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(
                                NavDestination.History.buildRoute(NavDestination.BottomNav.Expenses.route)
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_history),
                                contentDescription = stringResource(R.string.expenses_top_bar_action_description),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                NavDestination.BottomNav.Incomes.route -> AppTopBar(
                    title = stringResource(R.string.incomes_top_bar_title),
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(
                                NavDestination.History.buildRoute(NavDestination.BottomNav.Incomes.route)
                            )
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_history),
                                contentDescription = stringResource(R.string.expenses_top_bar_action_description),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                NavDestination.BottomNav.Check.route -> AppTopBar(
                    title = stringResource(R.string.account_top_bar_title),
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = stringResource(R.string.account_top_bar_action_description),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )

                NavDestination.BottomNav.Articles.route -> AppTopBar(title = stringResource(R.string.articles_top_bar_title))
                NavDestination.BottomNav.Settings.route -> AppTopBar(title = stringResource(R.string.settings_top_bar_title))

                NavDestination.History.routeWithArgument -> AppTopBar(
                    title = "Моя история",
                    navigationIconResId = R.drawable.ic_back,
                    onNavigationClick = {
                        navController.popBackStack()
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                painterResource(R.drawable.ic_history_timer),
                                contentDescription = "Анализ истории",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            AppBottomNavBar(navController)
        },

        floatingActionButton = {
            if (currentRoute in routesWithFab) {
                AppFAB(onClick = { })
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            AppNavGraph(navController, snackBarHostState)
        }
    }
}