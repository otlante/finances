package com.otlante.finances

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.otlante.finances.ui.nav.RootNavGraph
import com.otlante.finances.ui.screens.splash.SplashViewModel
import com.otlante.finances.ui.theme.FinancesTheme

/**
 * Main activity that hosts the entire application.
 * Configures the splash screen behavior, handles edge-to-edge mode,
 * and sets up the root navigation graph.
 */
class MainActivity : ComponentActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { !viewModel.isReady.value }
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.remove()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            FinancesTheme {
                RootNavGraph()
            }
        }
    }
}
