package com.otlante.finances.ui.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun AccountScreen(snackBarHostState: SnackbarHostState, repository: ApiRepository) {
    val viewModel: AccountViewModel = viewModel(
        factory = AccountViewModelFactory(repository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchAccount() },
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.isLoading) CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        else if (uiState.error != null) LaunchedEffect(uiState.error) {
            uiState.error?.let { errorMessage ->
                val result = snackBarHostState.showSnackbar(
                    message = errorMessage.description,
                    actionLabel = "Retry"
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.fetchAccount(initial = true)
                }
            }
        }
        else Column(modifier = Modifier.fillMaxSize()) {
            ListItem(
                type = ListItemType.SUMMARIZE,
                emoji = "\uD83D\uDCB0",
                title = stringResource(R.string.balance),
                showArrow = true,
                rightText = uiState.account?.balance,
                onClick = { }
            )

            HorizontalDivider()

            ListItem(
                type = ListItemType.SUMMARIZE,
                title = stringResource(R.string.currency),
                showArrow = true,
                rightText = uiState.account?.currency,
                onClick = { }
            )

            HorizontalDivider()
        }
    }
}