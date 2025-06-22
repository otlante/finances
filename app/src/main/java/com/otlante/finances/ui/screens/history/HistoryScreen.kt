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
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.formatDate
import com.otlante.finances.formatHeaderDate
import com.otlante.finances.formatTransactionDate
import com.otlante.finances.ui.components.DatePickerModal
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.components.ListItemType
import java.time.LocalDate
import java.time.ZoneId

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

    Column(modifier = Modifier.fillMaxSize()) {
        HistoryHeader(
            startDate = uiState.startDate,
            endDate = uiState.endDate,
            totalSum = uiState.totalSum,
            onStartDateClick = viewModel::onStartDatePickerOpen,
            onEndDateClick = viewModel::onEndDatePickerOpen
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    val error = uiState.error
                    LaunchedEffect(error) {
                        val result = snackBarHostState.showSnackbar(
                            message = error?.description.orEmpty(),
                            actionLabel = "Повтор"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.fetchHistory(initial = true)
                        }
                    }
                }

                else -> HistoryList(uiState.transactions)
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
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    Column(modifier = Modifier.background(MaterialTheme.colorScheme.secondary)) {
        ListItem(
            title = "Начало",
            type = ListItemType.SUMMARIZE,
            rightText = formatHeaderDate(startDate),
            onClick = onStartDateClick
        )
        HorizontalDivider()
        ListItem(
            title = "Конец",
            type = ListItemType.SUMMARIZE,
            rightText = formatHeaderDate(endDate),
            onClick = onEndDateClick
        )
        HorizontalDivider()
        ListItem(
            title = "Сумма",
            type = ListItemType.SUMMARIZE,
            rightText = totalSum,
        )
        HorizontalDivider()
    }
}

@Composable
private fun HistoryList(transactions: List<Transaction>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            items = transactions,
            key = { it.id }
        ) { transaction ->
            ListItem(
                emoji = transaction.category.emoji,
                title = transaction.category.name,
                subtitle = transaction.comment,
                rightText = transaction.amount,
                rightComment = formatTransactionDate(formatDate(transaction.transactionDate)),
                showArrow = true,
                onClick = { }
            )
            HorizontalDivider()
        }
    }
}