package com.otlante.finances.ui.screens.income

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

data class IncomeUiState(
    val transactions: List<Transaction> = emptyList(),
    val totalAmount: String = "0",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null
)

class IncomeViewModel(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncomeUiState())
    val uiState: StateFlow<IncomeUiState> = _uiState

    init {
        fetchIncome(initial = true)
    }

    fun fetchIncome(initial: Boolean = false) {
        viewModelScope.launch {
            setLoadingState(initial)

            repository.getIncomeTransactions().fold(
                onSuccess = { transactions ->
                    val total = transactions.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                    val formattedTransactions = transactions.map {
                        it.copy(amount = formatAmount(it.amount))
                    }

                    _uiState.update {
                        it.copy(
                            transactions = formattedTransactions,
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
}

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