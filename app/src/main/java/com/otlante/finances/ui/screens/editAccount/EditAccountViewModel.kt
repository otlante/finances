package com.otlante.finances.ui.screens.editAccount

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
data class EditAccountUiState(
    val account: Account? = null,
    val isLoading: Boolean = false,
    val isReadyToLeft: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null,

    val editedName: String? = null,
    val editedBalance: String? = null,
    val editedCurrency: String? = null,

    val isNameDialogVisible: Boolean = false,
    val isBalanceDialogVisible: Boolean = false,
    val isCurrencySheetVisible: Boolean = false
)

/**
 * ViewModel responsible for managing the account data and exposing
 * its state to the UI via [AccountUiState].
 *
 * @property repository the [ApiRepository] used to load account data
 */
class EditAccountViewModel @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditAccountUiState())
    val uiState: StateFlow<EditAccountUiState> = _uiState

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
        val formattedBalance = Formatter.formatAmount(
            account.balance
        )
        _uiState.update {
            it.copy(
                account = account.copy(balance = formattedBalance),
                isLoading = false,
                isRefreshing = false,
                error = null,
                editedName = account.name,
                editedBalance = formattedBalance,
                editedCurrency = account.currency
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

    fun onNameClick() {
        _uiState.update { it.copy(isNameDialogVisible = true) }
    }

    fun onBalanceClick() {
        _uiState.update { it.copy(isBalanceDialogVisible = true) }
    }

    fun onCurrencyClick() {
        _uiState.update { it.copy(isCurrencySheetVisible = true) }
    }

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(editedName = newName) }
    }

    fun confirmNameEdit() {
        _uiState.update {
            it.copy(
                account = it.account?.copy(name = it.editedName ?: it.account.name),
                editedName = it.account?.name,
                isNameDialogVisible = false
            )
        }
    }

    fun dismissNameEdit() {
        _uiState.update {
            it.copy(
                editedName = it.account?.name,
                isNameDialogVisible = false
            )
        }
    }

    fun onBalanceChanged(newBalance: String) {
        _uiState.update { it.copy(editedBalance = newBalance) }
    }

    fun confirmBalanceEdit() {
        val currentBalance = uiState.value.editedBalance
        val rounded = currentBalance?.replace(",", ".")?.toDoubleOrNull()?.let {
            Formatter.formatAmount(it)
        }
        if (rounded != null) {
            _uiState.update {
                it.copy(
                    account = it.account?.copy(balance = rounded),
                    editedBalance = rounded,
                    isBalanceDialogVisible = false
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    editedBalance = it.account?.balance,
                    isBalanceDialogVisible = false
                )
            }
        }
    }

    fun dismissBalanceEdit() {
        _uiState.update {
            it.copy(
                editedBalance = it.account?.balance,
                isBalanceDialogVisible = false
            )
        }
    }

    fun onCurrencySelected(newCurrency: String) {
        _uiState.update {
            it.copy(
                account = it.account?.copy(currency = newCurrency),
                editedCurrency = newCurrency,
                isCurrencySheetVisible = false
            )
        }
    }

    fun dismissCurrencyEdit() {
        _uiState.update {
            it.copy(
                editedCurrency = it.account?.currency,
                isCurrencySheetVisible = false
            )
        }
    }

    fun cancelChanges() {
        _uiState.update { state ->
            state.copy(
                isReadyToLeft = true,
                isNameDialogVisible = false,
                isBalanceDialogVisible = false,
                isCurrencySheetVisible = false
            )
        }
    }

    fun saveChanges() {
        val name = uiState.value.account?.name ?: return
        val balance = uiState.value.account?.balance?.replace(",", ".") ?: return
        val currency = uiState.value.account?.currency ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.updateAccount(name, balance, currency).fold(
                onSuccess = { account ->
                    _uiState.update {
                        it.copy(isLoading = false, isReadyToLeft = true)
                    }
                },
                onError = { error ->
                    fetchAccountError(error)
                }
            )
        }
    }
}
