package com.otlante.finances.ui.screens.history

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otlante.finances.ui.utils.Formatter
import com.otlante.finances.data.remote.NetworkError
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.nav.NavDestination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Types of date picker dialogs.
 */
enum class DatePickerDialogType {
    START_DATE, END_DATE
}

/**
 * UI state for the history screen showing transactions.
 *
 * @property transactions List of transactions to display.
 * @property startDate Selected start date of the period.
 * @property endDate Selected end date of the period.
 * @property totalSum Formatted total sum of displayed transactions.
 * @property isLoading Indicates if data is loading.
 * @property isRefreshing Indicates if a refresh is in progress.
 * @property openDialog The currently open date picker dialog type, or null if none.
 * @property error Network error if any occurred.
 */
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

/**
 * ViewModel responsible for managing the history screen logic.
 *
 * @param repository Repository to fetch data from.
 * @param savedStateHandle Saved state handle to retrieve navigation arguments.
 */
class HistoryViewModel @AssistedInject constructor(
    private val repository: ApiRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): HistoryViewModel
    }

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
                onSuccess = ::fetchHistorySuccess,
                onError = ::fetchHistoryError
            )
        }
    }

    private fun fetchHistoryError(error: NetworkError) {
        _uiState.update {
            it.copy(
                error = error,
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    private fun fetchHistorySuccess(allTransactions: List<Transaction>) {
        val filtered = filterByParentRoute(allTransactions)
        val total = filtered.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

        _uiState.update {
            it.copy(
                transactions = formatTransactions(filtered),
                totalSum = Formatter.formatAmount(total),
                isLoading = false,
                isRefreshing = false,
                error = null
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
            .map { it.copy(amount = Formatter.formatAmount(it.amount)) }
    }
}

/**
 * Factory for creating [HistoryViewModel] instances with parameters.
 *
 * @property repository Repository for data access.
 * @property savedStateHandle Saved state handle for navigation arguments.
 */

//class HistoryViewModelFactory @Inject constructor(
//    private val repository: ApiRepository,
//    owner: SavedStateRegistryOwner,
//    defaultArgs: Bundle? = null
//) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle
//    ): T {
//        return HistoryViewModel(repository, handle) as T
//    }
//}
//class HistoryViewModelFactory(
//    private val repository: ApiRepository,
//    private val savedStateHandle: SavedStateHandle
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return HistoryViewModel(repository, savedStateHandle) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
