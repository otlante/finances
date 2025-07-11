package com.otlante.finances

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.otlante.finances.di.component.ActivityComponent
import com.otlante.finances.ui.composition.LocalViewModelFactory
import com.otlante.finances.ui.nav.RootNavGraph
import com.otlante.finances.ui.screens.splash.SplashViewModel
import com.otlante.finances.ui.theme.FinancesTheme

/**
 * Main activity that hosts the entire application.
 * Configures the splash screen behavior, handles edge-to-edge mode,
 * and sets up the root navigation graph.
 */
class MainActivity : ComponentActivity() {

    private lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        activityComponent = appComponent.activityComponent().create()
        activityComponent.inject(this)

        val splashViewModel: SplashViewModel by viewModels {
            activityComponent.viewModelFactory()
        }
        splashScreen.setKeepOnScreenCondition { !splashViewModel.isReady.value }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalViewModelFactory provides activityComponent.viewModelFactory()) {
                FinancesTheme {
                    RootNavGraph()
                }
            }
        }
    }
}
