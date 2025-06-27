package com.otlante.finances.ui.screens.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otlante.finances.ui.utils.Formatter
import com.otlante.finances.data.remote.NetworkError
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for the Income screen.
 *
 * @property transactions List of income transactions.
 * @property totalAmount Formatted total sum of income transactions.
 * @property isLoading True if the initial loading is in progress.
 * @property isRefreshing True if a refresh operation is in progress.
 * @property error NetworkError if any error occurred during data fetch.
 */
data class IncomeUiState(
    val transactions: List<Transaction> = emptyList(),
    val totalAmount: String = "0",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null
)

/**
 * ViewModel responsible for fetching and managing income transactions data.
 *
 * Uses an [ApiRepository] to fetch income transactions asynchronously.
 * Updates [IncomeUiState] accordingly to reflect loading, success, and error states.
 *
 * @property repository The repository interface to access income transaction data.
 */
class IncomeViewModel(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeUiState())
    val uiState: StateFlow<IncomeUiState> = _uiState

    init {
        fetchIncome(initial = true)
    }

    /**
     * Fetches the income transactions from the repository.
     *
     * @param initial If true, sets the loading indicator for initial loading, otherwise sets refresh state.
     */
    fun fetchIncome(initial: Boolean = false) {
        viewModelScope.launch {
            setLoadingState(initial)

            repository.getIncomeTransactions().fold(
                onSuccess = ::fetchIncomeSuccess,
                onError = ::fetchIncomeError
            )
        }
    }

    private fun fetchIncomeError(error: NetworkError) {
        _uiState.update {
            it.copy(
                error = error,
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    private fun fetchIncomeSuccess(transactions: List<Transaction>) {
        val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        val formattedTransactions = transactions.map {
            it.copy(amount = Formatter.formatAmount(it.amount))
        }

        _uiState.update {
            it.copy(
                transactions = formattedTransactions,
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


/**
 * Factory for creating instances of [IncomeViewModel].
 *
 * @property repository The repository to be passed to the ViewModel.
 */
class IncomeViewModelFactory(
    private val repository: ApiRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
