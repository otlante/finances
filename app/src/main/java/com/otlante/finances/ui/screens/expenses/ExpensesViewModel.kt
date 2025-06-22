package com.otlante.finances.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.otlante.finances.network.NetworkError
import com.otlante.finances.network.ResultState
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.formatAmount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExpensesUiState(
    val transactions: List<Transaction> = emptyList(),
    val totalAmount: String = "0 ₽",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null
)

class ExpensesViewModel(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState

    init {
        fetchExpenses(initial = true)
    }

    fun fetchExpenses(initial: Boolean = false) {
        viewModelScope.launch {
            setLoadingState(initial)

            repository.getExpenseTransactions().fold(
                onSuccess = { transactions ->
                    val formatted = transactions.map { it.copy(amount = formatAmount(it.amount)) }
                    val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

                    _uiState.update {
                        it.copy(
                            transactions = formatted,
                            totalAmount = formatAmount(total),
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        )
                    }
                },
                onError = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = error
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
}

class ExpensesViewModelFactory(
    private val repository: ApiRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpensesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}