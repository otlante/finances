package com.otlante.finances.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.NetworkError
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.utils.Formatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represents the UI state of the Expenses screen.
 *
 * @property transactions list of expense transactions to display
 * @property totalAmount formatted total amount of expenses
 * @property isLoading true if initial data loading is in progress
 * @property isRefreshing true if pull-to-refresh is active
 * @property error an optional [NetworkError] describing failure
 */
data class ExpensesUiState(
    val transactions: List<Transaction> = emptyList(),
    val totalAmount: String = "0",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null
)

/**
 * ViewModel responsible for loading and managing the expenses data,
 * handling error states, formatting transaction amounts, and managing UI state.
 *
 * @property repository the [ApiRepository] used to fetch transactions
 */
class ExpensesViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    val accountFlow = repository.accountFlow
    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState

    init {
        fetchExpenses(initial = true)
    }

    /**
     * Loads expense transactions from the repository and updates the UI state.
     * Handles both initial load and pull-to-refresh scenarios.
     *
     * @param initial true if this is the first load, false for pull-to-refresh
     */
    fun fetchExpenses(initial: Boolean = false) {
        viewModelScope.launch {
            setLoadingState(initial)

            repository.getExpenseTransactions().fold(
                onSuccess = ::fetchExpensesSuccess,
                onError = ::fetchExpensesError
            )
        }
    }

    private fun fetchExpensesError(error: NetworkError) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                error = error
            )
        }
    }

    private fun fetchExpensesSuccess(transactions: List<Transaction>) {
        val formatted = transactions.map { it.copy(amount = Formatter.formatAmount(it.amount)) }
        val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

        _uiState.update {
            it.copy(
                transactions = formatted,
                totalAmount = Formatter.formatAmount(total),
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
}
