package com.otlante.finances.ui.screens.addOrEditTrans

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otlante.finances.data.remote.NetworkError
import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.nav.NavDestination
import com.otlante.finances.ui.nav.TransactionMode
import com.otlante.finances.ui.utils.Formatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

enum class ActiveDialog {
    CATEGORY, SUM, DATE, TIME, NAME, NONE
}

data class AddOrEditIncomeOrExpenseUiState(
    val account: Account? = null,
    val incomeCategories: List<Category>? = null,
    val isLoading: Boolean = false,
    val isReadyToLeft: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: NetworkError? = null,
    val isEditableTransaction: Boolean = false,
    val isIncomeTransaction: Boolean = false,

    val editedCategory: Category? = null,
    val editedSum: String = "0",
    val checkedSum: String = "0",
    val editedDate: LocalDate = LocalDate.now(),
    val editedTime: LocalTime = LocalTime.now(),
    val editedName: String = "Название",
    val checkedName: String = "Название",

    val activeDialog: ActiveDialog = ActiveDialog.NONE,
)

class AddOrEditTransViewModel @AssistedInject constructor(
    private val repository: ApiRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): AddOrEditTransViewModel
    }

    val accountFlow = repository.accountFlow
    private val _uiState = MutableStateFlow(AddOrEditIncomeOrExpenseUiState())
    val uiState: StateFlow<AddOrEditIncomeOrExpenseUiState> = _uiState

    private val mode: String? = savedStateHandle[NavDestination.AddOrEditTrans.MODE_ARG]
    private val id: Int? = savedStateHandle[NavDestination.AddOrEditTrans.ID_ARG]

    init {
        when (mode) {
            TransactionMode.EDIT_EXPENSE.name -> {
                _uiState.update {
                    it.copy(
                        isEditableTransaction = true,
                        isIncomeTransaction = false
                    )
                }
                fetchTransaction(initial = true, id = id ?: 0)
                fetchCategories(initial = true, isIncome = false)
            }

            TransactionMode.EDIT_INCOME.name -> {
                _uiState.update {
                    it.copy(
                        isEditableTransaction = true,
                        isIncomeTransaction = true
                    )
                }
                fetchTransaction(initial = true, id = id ?: 0)
                fetchCategories(initial = true, isIncome = true)
            }

            TransactionMode.ADD_EXPENSE.name -> {
                _uiState.update {
                    it.copy(
                        isEditableTransaction = false,
                        isIncomeTransaction = false
                    )
                }
                fetchAccount(initial = true)
                fetchCategories(initial = true, isIncome = false, needToUpdateCategory = true)
            }

            TransactionMode.ADD_INCOME.name -> {
                _uiState.update {
                    it.copy(
                        isEditableTransaction = false,
                        isIncomeTransaction = true
                    )
                }
                fetchAccount(initial = true)
                fetchCategories(initial = true, isIncome = true, needToUpdateCategory = true)
            }
        }
    }

    fun fetchTransaction(initial: Boolean, id: Int) {
        viewModelScope.launch {
            updateLoadingState(initial)

            repository.getTransactionById(id).fold(
                onSuccess = ::fetchTransactionSuccess,
                onError = ::fetchError
            )
        }
    }

    private fun fetchTransactionSuccess(transaction: Transaction) {
        val dateTime = Formatter.formatDate(transaction.transactionDate)
        _uiState.update {
            it.copy(
                account = transaction.account,
                editedCategory = transaction.category,
                editedSum = transaction.amount,
                checkedSum = transaction.amount,
                editedDate = dateTime.toLocalDate(),
                editedTime = dateTime.toLocalTime(),
                editedName = transaction.comment ?: "",
                checkedName = transaction.comment ?: "",
                isLoading = false,
                isRefreshing = false,
                error = null,
            )
        }
    }

    fun fetchCategories(
        initial: Boolean = false,
        isIncome: Boolean,
        needToUpdateCategory: Boolean = false
    ) {
        viewModelScope.launch {
            updateLoadingState(initial)

            repository.getAllCategories().fold(
                onSuccess = { fetchCategoriesSuccess(it, isIncome, needToUpdateCategory) },
                onError = ::fetchError
            )
        }
    }

    private fun fetchCategoriesSuccess(
        list: List<Category>,
        isIncome: Boolean,
        needToUpdateCategory: Boolean
    ) {
        val incomeCategories = list.filter { it.isIncome == isIncome }
        if (needToUpdateCategory) {
            _uiState.update {
                it.copy(
                    incomeCategories = incomeCategories,
                    isLoading = false,
                    isRefreshing = false,
                    error = null,
                    editedCategory = incomeCategories.firstOrNull()
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    incomeCategories = incomeCategories,
                    isLoading = false,
                    isRefreshing = false,
                    error = null,
                )
            }
        }
    }

    private fun fetchError(error: NetworkError) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isRefreshing = false,
                error = error
            )
        }
    }

    fun fetchAccount(initial: Boolean = false) {
        viewModelScope.launch {
            updateLoadingState(initial)

            repository.getMainAccount().fold(
                onSuccess = ::fetchAccountSuccess,
                onError = ::fetchError
            )
        }
    }

    private fun fetchAccountSuccess(account: Account) {
        _uiState.update {
            it.copy(
                account = account,
                isLoading = false,
                isRefreshing = false,
                error = null,
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

    fun onCategoryClick() {
        _uiState.update { it.copy(activeDialog = ActiveDialog.CATEGORY) }
    }

    fun onSumClick() {
        _uiState.update { it.copy(activeDialog = ActiveDialog.SUM) }
    }

    fun onDateClick() {
        _uiState.update { it.copy(activeDialog = ActiveDialog.DATE) }
    }

    fun onTimeClick() {
        _uiState.update { it.copy(activeDialog = ActiveDialog.TIME) }
    }

    fun onNameClick() {
        _uiState.update { it.copy(activeDialog = ActiveDialog.NAME) }
    }

    fun onCategorySelect(category: Category) {
        _uiState.update {
            it.copy(
                activeDialog = ActiveDialog.NONE,
                editedCategory = category
            )
        }
    }

    fun onCategorySelectDismiss() {
        _uiState.update {
            it.copy(
                activeDialog = ActiveDialog.NONE,
            )
        }
    }

    fun onSumChanged(newSum: String) {
        _uiState.update { it.copy(editedSum = newSum) }
    }

    fun confirmSumEdit() {
        val rounded = _uiState.value.editedSum.replace(",", ".").toDoubleOrNull()?.let {
            Formatter.formatAmount(it)
        }
        if (rounded != null) {
            _uiState.update {
                it.copy(
                    editedSum = rounded,
                    checkedSum = rounded,
                    activeDialog = ActiveDialog.NONE,
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    editedSum = it.checkedSum,
                    activeDialog = ActiveDialog.NONE,
                )
            }
        }
    }

    fun onSumEditDismiss() {
        _uiState.update {
            it.copy(
                editedSum = it.checkedSum,
                activeDialog = ActiveDialog.NONE,
            )
        }
    }

    fun onDatePickerDismiss() {
        _uiState.update { it.copy(activeDialog = ActiveDialog.NONE) }
    }

    fun onDateSelected(dateInMillis: Long?) {
        if (dateInMillis == null) return onDatePickerDismiss()

        val newDate = Instant.ofEpochMilli(dateInMillis).atZone(ZoneId.of("UTC")).toLocalDate()

        _uiState.update {
            it.copy(
                editedDate = newDate,
                activeDialog = ActiveDialog.NONE,
            )
        }
    }

    fun onTimeDismiss() {
        _uiState.update { it.copy(activeDialog = ActiveDialog.NONE) }
    }

    fun onTimeConfirm(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                activeDialog = ActiveDialog.NONE,
                editedTime = LocalTime.of(hour, minute)
            )
        }
    }

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(editedName = newName) }
    }

    fun onNameDialogConfirm() {
        _uiState.update {
            it.copy(
                checkedName = it.editedName,
                activeDialog = ActiveDialog.NONE,
            )
        }
    }

    fun onNameDialogDismiss() {
        _uiState.update {
            it.copy(
                editedName = it.checkedName,
                activeDialog = ActiveDialog.NONE,
            )
        }
    }

    fun cancelChanges() {
        _uiState.update { state ->
            state.copy(
                isReadyToLeft = true,
                activeDialog = ActiveDialog.NONE,
            )
        }
    }

    fun deleteTransaction() {
        if (id == null) {
            fetchError(NetworkError.UnknownError())
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository
                .deleteTransaction(id).fold(
                    onSuccess = { account ->
                        _uiState.update {
                            it.copy(isLoading = false, isReadyToLeft = true)
                        }
                    },
                    onError = { error ->
                        fetchError(error)
                    }
                )
        }
    }

    fun saveChanges() {
        val accountId = _uiState.value.account?.id
        val categoryId = _uiState.value.editedCategory?.id
        val amount = _uiState.value.checkedSum.replace(',', '.')
        val editedDate = _uiState.value.editedDate
        val editedTime = _uiState.value.editedTime
        val comment = _uiState.value.checkedName

        if (accountId == null || categoryId == null) {
            fetchError(NetworkError.UnknownError())
            return
        }

        val transactionDate = Formatter.formatToIsoUtc(
            editedDate,
            editedTime
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (mode == TransactionMode.EDIT_EXPENSE.name || mode == TransactionMode.EDIT_INCOME.name) {
                repository.updateTransaction(
                    transactionId = id ?: 0,
                    accountId = accountId,
                    categoryId = categoryId,
                    amount = amount,
                    transactionDate = transactionDate,
                    comment = comment
                ).fold(
                    onSuccess = { account ->
                        _uiState.update {
                            it.copy(isLoading = false, isReadyToLeft = true)
                        }
                    },
                    onError = { error ->
                        fetchError(error)
                    }
                )
            } else {
                repository.addTransaction(
                    accountId = accountId,
                    categoryId = categoryId,
                    amount = amount,
                    transactionDate = transactionDate,
                    comment = comment
                ).fold(
                    onSuccess = { account ->
                        _uiState.update {
                            it.copy(isLoading = false, isReadyToLeft = true)
                        }
                    },
                    onError = { error ->
                        fetchError(error)
                    }
                )
            }
        }
    }
}
