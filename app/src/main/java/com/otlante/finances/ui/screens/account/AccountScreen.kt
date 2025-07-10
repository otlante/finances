package com.otlante.finances.ui.screens.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.otlante.finances.di.LocalViewModelFactory
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.components.ListItemType

/**
 * Composable function that displays the account screen, including
 * balance and currency information with support for pull-to-refresh,
 * loading indicators, and error handling via snackbar.
 *
 * @param snackBarHostState the [SnackbarHostState] used to show error messages and retry prompts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(snackBarHostState: SnackbarHostState) {

    val factory = LocalViewModelFactory.current
    val viewModel: AccountViewModel = viewModel(factory = factory)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    uiState.error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            val result = snackBarHostState.showSnackbar(
                message = errorMessage.description,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.fetchAccount(initial = true)
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.fetchAccount() },
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            else -> AccountContent(uiState)
        }
    }
}

@Composable
private fun AccountContent(uiState: AccountUiState) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        ListItem(
            type = ListItemType.SUMMARIZE,
            emoji = "\uD83D\uDCB0",
            title = stringResource(R.string.balance),
            rightText = uiState.account?.balance,
        )

        HorizontalDivider()

        ListItem(
            type = ListItemType.SUMMARIZE,
            title = stringResource(R.string.currency),
            rightText = uiState.account?.currency,
        )

        HorizontalDivider()
    }
}
