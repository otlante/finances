package com.otlante.finances.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.NetworkError
import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.utils.Formatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state holder for the account screen.
 *
 * @property account the current [Account] data, or null if not loaded
 * @property isLoading true if the initial load is in progress
 * @property isRefreshing true if a pull-to-refresh is in progress
 * @property error optional [NetworkError] describing the failure
 */
data class AccountUiState(
    val account: Account? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null
)

/**
 * ViewModel responsible for managing the account data and exposing
 * its state to the UI via [AccountUiState].
 *
 * @property repository the [ApiRepository] used to load account data
 */
class AccountViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    val accountFlow = repository.accountFlow
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        fetchAccount(initial = true)
    }

    /**
     * Fetches account data from the repository and updates the UI state.
     *
     * @param initial true if called during first load, false if called from refresh
     */
    fun fetchAccount(initial: Boolean = false) {
        viewModelScope.launch {
            updateLoadingState(initial)

            repository.getMainAccount().fold(
                onSuccess = ::fetchAccountSuccess,
                onError = ::fetchAccountError
            )
        }
    }

    private fun fetchAccountSuccess(account: Account) {
        _uiState.update {
            it.copy(
                account = account.copy(balance = Formatter.formatAmount(account.balance)),
                isLoading = false,
                isRefreshing = false,
                error = null
            )
        }
    }

    private fun fetchAccountError(error: NetworkError) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                error = error
            )
        }
    }

    private fun updateLoadingState(initial: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = initial,
                isRefreshing = !initial,
                error = null
            )
        }
    }
}
