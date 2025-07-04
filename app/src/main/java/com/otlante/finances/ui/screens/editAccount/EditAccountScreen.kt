package com.otlante.finances.ui.screens.editAccount

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.otlante.finances.R
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.components.ListItemType
import com.otlante.finances.ui.screens.account.AccountUiState
import com.otlante.finances.ui.screens.account.AccountViewModel
import com.otlante.finances.ui.screens.account.AccountViewModelFactory


@Composable
fun EditAccountScreen(
    snackBarHostState: SnackbarHostState,
    repository: ApiRepository,
    navController: NavController
) {
    val viewModel: EditAccountViewModel = viewModel(
        factory = EditAccountViewModelFactory(repository)
    )

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

    LaunchedEffect(Unit) {
        EditAccountCallbacksHolder.onConfirm = {
            Log.i("TAG", "EditAccountScreen: ABOBA1")
            navController.popBackStack()
        }
        EditAccountCallbacksHolder.onCancel = {
            Log.i("TAG", "EditAccountScreen: ABOBA2")
            navController.popBackStack()
        }
    }

    EditAccountScreenContent(uiState)
}

@Composable
fun EditAccountScreenContent(uiState: EditAccountUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ListItem(
            type = ListItemType.SUMMARIZE,
            emoji = "\uD83C\uDFF7\uFE0F",
            title = stringResource(R.string.account_name),
            showArrow = true,
            rightText = uiState.account?.name,
            onClick = { }
        )

        HorizontalDivider()

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
