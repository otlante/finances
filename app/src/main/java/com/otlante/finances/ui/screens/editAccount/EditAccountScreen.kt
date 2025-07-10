package com.otlante.finances.ui.screens.editAccount

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.otlante.finances.R
import com.otlante.finances.di.LocalViewModelFactory
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.components.ListItemType
import com.otlante.finances.ui.utils.Formatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    snackBarHostState: SnackbarHostState,
    navController: NavController
) {

    val factory = LocalViewModelFactory.current
    val viewModel: EditAccountViewModel = viewModel(factory = factory)

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
            viewModel.saveChanges()
        }
        EditAccountCallbacksHolder.onCancel = {
            viewModel.cancelChanges()
        }
    }

    MainContent(uiState, viewModel)

    Dialogs(uiState, viewModel)

    if (uiState.isReadyToLeft) {
        navController.popBackStack()
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Dialogs(
    uiState: EditAccountUiState,
    viewModel: EditAccountViewModel,
) {
    if (uiState.isNameDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissNameEdit() },
            title = { Text("Редактировать имя") },
            text = {
                TextField(
                    value = uiState.editedName ?: "",
                    onValueChange = viewModel::onNameChanged,
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmNameEdit() }) {
                    Text("OK")
                }
            }
        )
    }

    if (uiState.isBalanceDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissBalanceEdit() },
            title = { Text("Редактировать баланс") },
            text = {
                TextField(
                    value = uiState.editedBalance ?: "",
                    onValueChange = viewModel::onBalanceChanged,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmBalanceEdit() }) {
                    Text("OK")
                }
            }
        )
    }

    val sheetState = rememberModalBottomSheetState()
    if (uiState.isCurrencySheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissCurrencyEdit() },
            sheetState = sheetState
        ) {
            Formatter.Currency.entries.toTypedArray().forEach { currency ->
                ListItem(
                    emoji = currency.symbol,
                    title = currency.description,
                    onClick = { viewModel.onCurrencySelected(currency.toString()) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun MainContent(
    uiState: EditAccountUiState,
    viewModel: EditAccountViewModel
) {
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
            onClick = { viewModel.onNameClick() }
        )

        HorizontalDivider()

        ListItem(
            type = ListItemType.SUMMARIZE,
            emoji = "\uD83D\uDCB0",
            title = stringResource(R.string.balance),
            showArrow = true,
            rightText = "${uiState.account?.balance ?: ""} ${Formatter.getCurrencySymbol(uiState.editedCurrency)}",
            onClick = { viewModel.onBalanceClick() }
        )

        HorizontalDivider()

        ListItem(
            type = ListItemType.SUMMARIZE,
            title = stringResource(R.string.currency),
            showArrow = true,
            rightText = uiState.account?.currency,
            onClick = { viewModel.onCurrencyClick() }
        )

        HorizontalDivider()
    }
}
