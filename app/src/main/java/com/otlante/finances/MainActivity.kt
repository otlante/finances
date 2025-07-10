package com.otlante.finances

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otlante.finances.di.LocalViewModelFactory
import com.otlante.finances.ui.nav.RootNavGraph
import com.otlante.finances.ui.screens.income.IncomeViewModel
import com.otlante.finances.ui.screens.splash.SplashViewModel
import com.otlante.finances.ui.theme.FinancesTheme
import javax.inject.Inject

/**
 * Main activity that hosts the entire application.
 * Configures the splash screen behavior, handles edge-to-edge mode,
 * and sets up the root navigation graph.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalViewModelFactory provides appComponent.viewModelFactory()) {
                FinancesTheme {
                    RootNavGraph()
                }
            }
        }
    }
}
