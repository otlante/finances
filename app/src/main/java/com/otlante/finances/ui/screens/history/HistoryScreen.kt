package com.otlante.finances.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.components.DatePickerModal
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.components.ListItemType
import com.otlante.finances.ui.utils.Formatter
import java.time.LocalDate
import java.time.ZoneId

/**
 * Screen composable displaying the user's transaction history filtered by date range.
 * Shows a header with start/end dates and total sum, a list of transactions,
 * handles loading and error states with a snackbar, and displays a date picker dialog.
 *
 * @param snackBarHostState the SnackbarHostState to show messages
 * @param repository the repository to fetch transaction data
 * @param navBackStackEntry the NavBackStackEntry for saved state handle
 */
@Composable
fun HistoryScreen(
    snackBarHostState: SnackbarHostState,
    repository: ApiRepository,
    navBackStackEntry: NavBackStackEntry
) {
    val viewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(
            repository = repository,
            savedStateHandle = navBackStackEntry.savedStateHandle
        )
    )

    val uiState by viewModel.uiState.collectAsState()
    val account by viewModel.accountFlow.collectAsState()

    uiState.error?.let { errorMessage ->
        LaunchedEffect(uiState.error) {
            val result = snackBarHostState.showSnackbar(
                message = errorMessage.description,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.fetchHistory(initial = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HistoryHeader(
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            totalSum = uiState.totalSum,
            account = account,
            onStartDateClick = viewModel::onStartDatePickerOpen,
            onEndDateClick = viewModel::onEndDatePickerOpen
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                else -> HistoryList(uiState.transactions, account)
            }
        }
    }

    uiState.openDialog?.let { dialogType ->
        val dialogDate = when (dialogType) {
            DatePickerDialogType.START_DATE -> uiState.startDate
            DatePickerDialogType.END_DATE -> uiState.endDate
        }

        DatePickerModal(
            selectedDate = dialogDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli(),
            onDateSelected = viewModel::onDateSelected,
            onDismiss = viewModel::onDatePickerDismiss
        )
    }
}

@Composable
private fun HistoryHeader(
    startDate: LocalDate,
    endDate: LocalDate,
    totalSum: String,
    account: Account?,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
        ListItem(
            title = "Начало",
            type = ListItemType.SUMMARIZE,
            rightText = Formatter.formatHeaderDate(startDate),
            onClick = onStartDateClick
        )
        HorizontalDivider()
        ListItem(
            title = "Конец",
            type = ListItemType.SUMMARIZE,
            rightText = Formatter.formatHeaderDate(endDate),
            onClick = onEndDateClick
        )
        HorizontalDivider()
        ListItem(
            title = "Сумма",
            type = ListItemType.SUMMARIZE,
            rightText = "$totalSum ${
                Formatter.getCurrencySymbol(
                    account?.currency
                )
            }",
        )
        HorizontalDivider()
    }
}

@Composable
private fun HistoryList(transactions: List<Transaction>, account: Account?) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = transactions,
            key = { it.id }
        ) { transaction ->
            ListItem(
                emoji = transaction.category.emoji,
                title = transaction.category.name,
                subtitle = transaction.comment,
                rightText = "${transaction.amount} ${
                    Formatter.getCurrencySymbol(
                        account?.currency
                    )
                }",
                rightComment = Formatter.formatTransactionDate(Formatter.formatDate(transaction.transactionDate)),
                showArrow = true,
                onClick = { }
            )
            HorizontalDivider()
        }
    }
}
