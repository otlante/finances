package com.otlante.finances

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.otlante.finances.screens.SplashScreen
import com.otlante.finances.ui.theme.FinancesTheme

class MainActivity : ComponentActivity() {

    private fun setStatusBarColor(window: Window, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(color)
                view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }
        } else {
            window.statusBarColor = color
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinancesTheme {
                setStatusBarColor(window, MaterialTheme.colorScheme.onSecondaryContainer.toArgb())
                MainNavigation()
            }
        }
    }
}

val items = listOf(
    BottomNavItem.Expenses,
    BottomNavItem.Income,
    BottomNavItem.Check,
    BottomNavItem.ItemsOfExpenses,
    BottomNavItem.Settings
)

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "splash") {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute?.startsWith(item.route) == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = item.icon,
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute?.startsWith(BottomNavItem.Expenses.route) == true ||
                currentRoute?.startsWith(BottomNavItem.Income.route) == true
            ) {
                FloatingActionButton(
                    onClick = { },
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add))
                }
            }
        }
    ) {
        Log.i("MainActivity", "MainNavigation: $it")
        NavHost(
            navController = navController,
            startDestination = SplashScreen.ROUTE,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
        ) {
            composable(SplashScreen.ROUTE) {
                SplashScreen(navController)
            }
            expensesGraph()
            incomeGraph()
            checkGraph()
            itemsOfExpensesGraph()
            settingsGraph()
        }
    }
}