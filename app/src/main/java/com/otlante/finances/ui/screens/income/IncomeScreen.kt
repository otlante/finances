package com.otlante.finances.ui.screens.income

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.otlante.finances.R
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.components.ListItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(snackBarHostState: SnackbarHostState, repository: ApiRepository) {
    val viewModel: IncomeViewModel = viewModel(
        factory = IncomeViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchIncome() },
    ) {
        if (uiState.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else if (uiState.error != null) LaunchedEffect(uiState.error) {
            uiState.error?.let { errorMessage ->
                val result = snackBarHostState.showSnackbar(
                    message = errorMessage.description,
                    actionLabel = "Retry"
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.fetchIncome(initial = true)
                }
            }
        }
        else LazyColumn(modifier = Modifier.fillMaxSize()) {
            stickyHeader {
                ListItem(
                    type = ListItemType.SUMMARIZE,
                    title = stringResource(R.string.total),
                    rightText = uiState.totalAmount,
                )
                HorizontalDivider()
            }
            items(items = uiState.transactions, key = { item -> item.id }) { transaction ->
                ListItem(
                    emoji = transaction.category.emoji,
                    title = transaction.category.name,
                    subtitle = transaction.comment,
                    rightText = transaction.amount,
                    showArrow = true,
                    onClick = { }
                )
                HorizontalDivider()
            }
        }
    }
}

