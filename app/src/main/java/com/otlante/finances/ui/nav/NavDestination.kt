package com.otlante.finances.ui.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.otlante.finances.R

/**
 * Sealed class representing the navigation destinations of the app.
 *
 * @property route the route string used for navigation
 */
sealed class NavDestination(val route: String) {

    data object Splash : NavDestination("splash")

    data object Main : NavDestination("main")

    data object EditAccount : NavDestination("editAccount")

    data object History : NavDestination("history/{parentRoute}") {
        const val PARENT_ROUTE_ARG = "parentRoute"
        val routeWithArgument = "history/{$PARENT_ROUTE_ARG}"
        val arguments = listOf(
            navArgument(PARENT_ROUTE_ARG) { type = NavType.StringType }
        )

        fun buildRoute(parentRoute: String) = "history/$parentRoute"
    }

    sealed class BottomNav(
        route: String,
        @DrawableRes val icon: Int,
        @StringRes val label: Int
    ) : NavDestination(route) {

        data object Expenses : BottomNav(
            route = "expenses",
            icon = R.drawable.ic_expenses,
            label = R.string.bottom_nav_expenses
        )

        data object Incomes : BottomNav(
            route = "income",
            icon = R.drawable.ic_income,
            label = R.string.bottom_nav_incomes
        )

        data object Account : BottomNav(
            route = "check",
            icon = R.drawable.ic_check,
            label = R.string.bottom_nav_check
        )

        data object Articles : BottomNav(
            route = "articles",
            icon = R.drawable.ic_articles,
            label = R.string.bottom_nav_articles
        )

        data object Settings : BottomNav(
            route = "settings",
            icon = R.drawable.ic_settings,
            label = R.string.bottom_nav_settings
        )
    }
}
