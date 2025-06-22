package com.otlante.finances.ui.screens.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otlante.finances.network.NetworkError
import com.otlante.finances.network.ResultState
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.formatAmount
import com.otlante.finances.ui.nav.NavDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class DatePickerDialogType {
    START_DATE, END_DATE
}

data class HistoryUiState(
    val transactions: List<Transaction> = emptyList(),
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val totalSum: String = "0 ₽",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val openDialog: DatePickerDialogType? = null,
    val error: NetworkError? = null
)

class HistoryViewModel(
    private val repository: ApiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    private val parentRoute: String? = savedStateHandle[NavDestination.History.PARENT_ROUTE_ARG]

    init {
        fetchHistory(initial = true)
    }

    fun onStartDatePickerOpen() = openDialog(DatePickerDialogType.START_DATE)

    fun onEndDatePickerOpen() = openDialog(DatePickerDialogType.END_DATE)

    private fun openDialog(type: DatePickerDialogType) {
        _uiState.update { it.copy(openDialog = type) }
    }

    fun onDatePickerDismiss() {
        _uiState.update { it.copy(openDialog = null) }
    }

    fun onDateSelected(dateInMillis: Long?) {
        if (dateInMillis == null) return onDatePickerDismiss()

        val newDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.of("UTC")).toLocalDate()
        val dialogType = _uiState.value.openDialog

        _uiState.update { state ->
            val updated = when (dialogType) {
                DatePickerDialogType.START_DATE ->
                    if (newDate.isAfter(state.endDate)) state else state.copy(startDate = newDate)

                DatePickerDialogType.END_DATE ->
                    if (newDate.isBefore(state.startDate)) state else state.copy(endDate = newDate)

                null -> state
            }
            updated.copy(openDialog = null)
        }

        if (dialogType != null) {
            fetchHistory(initial = true)
        }
    }

    fun fetchHistory(initial: Boolean = false) {
        viewModelScope.launch {
            setLoadingState(initial)

            val state = _uiState.value
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE

            repository.getHistory(
                startDate = state.startDate.format(formatter),
                endDate = state.endDate.format(formatter)
            ).fold(
                onSuccess = { allTransactions ->
                    val filtered = filterByParentRoute(allTransactions)
                    val total = filtered.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

                    _uiState.update {
                        it.copy(
                            transactions = formatTransactions(filtered),
                            totalSum = formatAmount(total),
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }
                },
                onError = { error ->
                    _uiState.update {
                        it.copy(
                            error = error,
                            isLoading = false,
                            isRefreshing = false
                        )
                    }
                }
            )
        }
    }

    private fun setLoadingState(initial: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = initial,
                isRefreshing = !initial,
                error = null
            )
        }
    }

    private fun filterByParentRoute(transactions: List<Transaction>): List<Transaction> {
        return when (parentRoute) {
            NavDestination.BottomNav.Expenses.route -> transactions.filter { !it.category.isIncome }
            NavDestination.BottomNav.Incomes.route -> transactions.filter { it.category.isIncome }
            else -> transactions
        }
    }

    private fun formatTransactions(transactions: List<Transaction>): List<Transaction> {
        return transactions
            .sortedByDescending { it.transactionDate }
            .map { it.copy(amount = formatAmount(it.amount)) }
    }
}

class HistoryViewModelFactory(
    private val repository: ApiRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}