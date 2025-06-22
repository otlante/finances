package com.otlante.finances.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.otlante.finances.ui.screens.MainScreen
import com.otlante.finances.ui.screens.SplashScreen
import com.otlante.finances.ui.screens.SplashViewModel

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
            MainScreen()
        }
    }
}