package com.otlante.finances.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.otlante.finances.ui.composition.LocalViewModelFactory
import com.otlante.finances.ui.screens.home.HomeScreen
import com.otlante.finances.ui.screens.splash.SplashScreen
import com.otlante.finances.ui.screens.splash.SplashViewModel
import com.otlante.finances.ui.screens.pinCode.PinCodeEntryScreen
import com.otlante.finances.ui.screens.pinCode.PinCodeEntryViewModel

/**
 * Composable function that defines the root-level navigation graph of the app.
 *
 * Provides routing between the splash screen and the main application screen.
 * Initializes a [SplashViewModel] for managing splash screen state.
 */
@Composable
fun RootNavGraph() {

    val factory = LocalViewModelFactory.current
    val splashViewModel: SplashViewModel = viewModel(factory = factory)
    val pinCodeEntryViewModel: PinCodeEntryViewModel = viewModel(factory = factory)

    val navController = rememberNavController()
    val isPinSet by pinCodeEntryViewModel.isPinSet.collectAsState()

    NavHost(
        navController = navController,
        startDestination = NavDestination.Splash.route
    ) {
        composable(NavDestination.Splash.route) {
            SplashScreen(
                onReady = { splashViewModel.markReady() },
                onAnimationFinished = {
                    if (isPinSet) {
                        navController.navigate(NavDestination.PinCodeEntry.route) {
                            popUpTo(NavDestination.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(NavDestination.Main.route) {
                            popUpTo(NavDestination.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(NavDestination.Main.route) {
            HomeScreen()
        }
        
        composable(NavDestination.PinCodeEntry.route) {
            PinCodeEntryScreen(
                onPinCorrect = {
                    navController.navigate(NavDestination.Main.route) {
                        popUpTo(NavDestination.PinCodeEntry.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
