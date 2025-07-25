package com.otlante.finances.ui.screens.home

import android.R.attr.duration
import android.content.Context
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.otlante.finances.R
import com.otlante.finances.ui.components.AppBottomNavBar
import com.otlante.finances.ui.components.AppFAB
import com.otlante.finances.ui.components.AppTopBar
import com.otlante.finances.ui.nav.AppNavGraph
import com.otlante.finances.ui.nav.NavDestination
import com.otlante.finances.ui.nav.TransactionMode
import com.otlante.finances.ui.screens.addOrEditTrans.AddOrEditTransCallbacksHolder
import com.otlante.finances.ui.screens.editAccount.EditAccountCallbacksHolder
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import com.otlante.finances.data.remote.repository.VibrationSettingsRepository
import com.otlante.finances.ui.screens.settings.VibrationMode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Root composable for the home screen containing scaffold layout with top bar, bottom navigation,
 * and floating action button based on current navigation route.
 *
 * It manages navigation state, snackbar host state, and conditionally shows FAB on selected routes.
 */
@Composable
fun HomeScreen() {

    val snackBarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current.applicationContext
    val vibrationSettingsRepository = remember { VibrationSettingsRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = context.getSystemService(VibratorManager::class.java)
        manager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Vibrator::class.java)
    }
    val vibratorManager: VibratorManager? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        } else {
            null
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = getTopBarForRoute(currentRoute, navController),
        bottomBar = {
            AppBottomNavBar(navController, onTabSelected = { _ ->
                coroutineScope.launch {
                    val enabled = vibrationSettingsRepository.isVibrationEnabled.first()
                    if (enabled) {
                        val mode = vibrationSettingsRepository.vibrationMode.first()
                        when (mode) {
                            VibrationMode.SHORT -> simpleVibrate(vibrator, 20L)
                            VibrationMode.MEDIUM -> simpleVibrate(vibrator, 50L)
                            VibrationMode.LONG -> simpleVibrate(vibrator, 100L)
                            VibrationMode.PRIMITIVE_LOW_TICK -> {
                                vibrateWithEffect(
                                    vibrator,
                                    vibratorManager,
                                    VibrationEffect.Composition.PRIMITIVE_LOW_TICK
                                )
                            }

                            VibrationMode.PRIMITIVE_SPIN -> {
                                vibrateWithEffect(
                                    vibrator,
                                    vibratorManager,
                                    VibrationEffect.Composition.PRIMITIVE_SPIN
                                )
                            }

                            VibrationMode.PRIMITIVE_THUD -> {
                                vibrateWithEffect(
                                    vibrator,
                                    vibratorManager,
                                    VibrationEffect.Composition.PRIMITIVE_THUD
                                )
                            }
                        }
                    }
                }
            })
        },

        floatingActionButton = {
            if (currentRoute == NavDestination.BottomNav.Incomes.route) {
                AppFAB {
                    navController.navigate(
                        NavDestination.AddOrEditTrans.buildRoute(TransactionMode.ADD_INCOME)
                    )
                }
            } else if (currentRoute == NavDestination.BottomNav.Expenses.route) {
                AppFAB {
                    navController.navigate(
                        NavDestination.AddOrEditTrans.buildRoute(TransactionMode.ADD_EXPENSE)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            AppNavGraph(navController, snackBarHostState)
        }
    }
}

private fun simpleVibrate(
    vibrator: Vibrator?,
    duration: Long
) {
    vibrator?.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
}

private fun vibrateWithEffect(
    vibrator: Vibrator?,
    vibratorManager: VibratorManager?,
    effectId: Int
) {
    if (isPrimitiveSupported(
            effectId,
            vibratorManager
        ) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    ) {
        vibratorManager?.vibrate(
            CombinedVibration.createParallel(
                VibrationEffect.startComposition()
                    .addPrimitive(effectId)
                    .compose()
            )
        )
    } else {
        simpleVibrate(vibrator, 100L)
    }
}

private fun isPrimitiveSupported(effectId: Int, vibratorManager: VibratorManager?): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        vibratorManager?.defaultVibrator?.areAllPrimitivesSupported(effectId) ?: false
    } else {
        false
    }
}

/**
 * Returns a composable lambda that renders the top app bar according to the current navigation route.
 *
 * @param currentRoute the current navigation route string
 * @param navController the NavHostController to perform navigation actions
 * @return a composable lambda rendering the appropriate top app bar for the route
 */
@Composable
private fun getTopBarForRoute(
    currentRoute: String?,
    navController: NavHostController
): @Composable () -> Unit = {
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

        NavDestination.BottomNav.Account.route -> AppTopBar(
            title = stringResource(R.string.account_top_bar_title),
            actions = {
                IconButton(onClick = {
                    navController.navigate(
                        NavDestination.EditAccount.route
                    )
                }) {
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

        NavDestination.EditAccount.route -> AppTopBar(
            title = "Редактировать аккаунт",
            navigationIconResId = R.drawable.ic_cross,
            onNavigationClick = {
                EditAccountCallbacksHolder.onCancel?.invoke()
            },
            actions = {
                IconButton(onClick = {
                    EditAccountCallbacksHolder.onConfirm?.invoke()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_confirm),
                        contentDescription = "Подтвердить",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )

        NavDestination.AddOrEditTrans.routeWithArgument -> AppTopBar(
            title = "Мои доходы",
            navigationIconResId = R.drawable.ic_cross,
            onNavigationClick = {
                AddOrEditTransCallbacksHolder.onCancel?.invoke()
            },
            actions = {
                IconButton(onClick = {
                    AddOrEditTransCallbacksHolder.onConfirm?.invoke()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_confirm),
                        contentDescription = "Подтвердить",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}
