package com.otlante.finances.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.otlante.finances.ui.screens.home.HomeScreen
import com.otlante.finances.ui.screens.splash.SplashScreen
import com.otlante.finances.ui.screens.splash.SplashViewModel

/**
 * Composable function that defines the root-level navigation graph of the app.
 *
 * Provides routing between the splash screen and the main application screen.
 * Initializes a [SplashViewModel] for managing splash screen state.
 */
@Composable
fun RootNavGraph() {
    val splashViewModel: SplashViewModel = viewModel()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestination.Splash.route
    ) {
        composable(NavDestination.Splash.route) {
            SplashScreen(
                onReady = { splashViewModel.markReady() },
                onAnimationFinished = {
                    navController.navigate(NavDestination.Main.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(NavDestination.Main.route) {
            HomeScreen()
        }
    }
}
