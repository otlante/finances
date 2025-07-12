package com.otlante.finances.ui.screens.expenses

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.otlante.finances.R
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.components.ListItemType
import com.otlante.finances.ui.composition.LocalViewModelFactory
import com.otlante.finances.ui.nav.NavDestination
import com.otlante.finances.ui.nav.TransactionMode
import com.otlante.finances.ui.screens.addOrEditTrans.SharedRefreshIncomeViewModel
import com.otlante.finances.ui.utils.Formatter

/**
 * Composable function that displays the expenses screen, showing a list
 * of expenses with total summary and pull-to-refresh support. Handles
 * loading and error states using a [SnackbarHostState].
 *
 * @param snackBarHostState the [SnackbarHostState] to display error messages and retry actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(snackBarHostState: SnackbarHostState, navController: NavController) {

    val activity = LocalActivity.current as ViewModelStoreOwner
    val sharedViewModel: SharedRefreshIncomeViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = LocalViewModelFactory.current
    )

    val factory = LocalViewModelFactory.current
    val viewModel: ExpensesViewModel = viewModel(factory = factory)

    val refresh by sharedViewModel.refreshExpensesSignal.collectAsState()
    LaunchedEffect(refresh) {
        if (refresh) {
            viewModel.fetchExpenses()
            sharedViewModel.resetRefreshExpensesSignal()
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val account by viewModel.accountFlow.collectAsState()

    uiState.error?.let { errorMessage ->
        LaunchedEffect(uiState.error) {
            val result = snackBarHostState.showSnackbar(
                message = errorMessage.description,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.fetchExpenses(initial = true)
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchExpenses() },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    stickyHeader {
                        ListItem(
                            type = ListItemType.SUMMARIZE,
                            title = stringResource(R.string.total),
                            rightText = "${uiState.totalAmount} ${
                                Formatter.getCurrencySymbol(
                                    account?.currency
                                )
                            }",
                        )
                        HorizontalDivider()
                    }
                    items(items = uiState.transactions, key = { item -> item.id }) { transaction ->
                        ListItem(
                            emoji = transaction.category.emoji,
                            title = transaction.category.name,
                            subtitle = transaction.comment,
                            rightText = "${transaction.amount} ${
                                Formatter.getCurrencySymbol(
                                    account?.currency
                                )
                            }",
                            showArrow = true,
                            onClick = {
                                navController.navigate(
                                    NavDestination.AddOrEditTrans.buildRoute(
                                        TransactionMode.EDIT_EXPENSE, transaction.id
                                    )
                                )
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
