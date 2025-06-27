package com.otlante.finances.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.otlante.finances.ui.nav.NavDestination

/**
 * Composable function that renders a bottom navigation bar
 * with predefined destinations.
 *
 * Highlights the currently selected destination and handles
 * navigation actions when items are clicked.
 *
 * @param navController the [NavHostController] managing app navigation state
 */
@Composable
fun AppBottomNavBar(navController: NavHostController) {
    val destinations = listOf(
        NavDestination.BottomNav.Expenses,
        NavDestination.BottomNav.Incomes,
        NavDestination.BottomNav.Check,
        NavDestination.BottomNav.Articles,
        NavDestination.BottomNav.Settings
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        destinations.forEach { destination ->
            val isSelected = if (currentRoute == NavDestination.History.routeWithArgument) {
                navBackStackEntry?.arguments?.getString(NavDestination.History.PARENT_ROUTE_ARG) == destination.route
            } else {
                currentRoute == destination.route
            }

            GetNavigationBarItem(destination, isSelected, navController)
        }
    }
}

@Composable
private fun RowScope.GetNavigationBarItem(
    destination: NavDestination.BottomNav,
    isSelected: Boolean,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { Text(text = stringResource(id = destination.label)) },
        icon = {
            Icon(
                painterResource(destination.icon),
                contentDescription = stringResource(destination.label)
            )
        },
        selected = isSelected,
        onClick = {
            if (isSelected) {
                navController.popBackStack(destination.route, inclusive = false)
            } else {
                navController.navigate(destination.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    )
}
