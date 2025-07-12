package com.otlante.finances.ui.screens.addOrEditTrans

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.otlante.finances.domain.entity.Account
import com.otlante.finances.ui.components.DatePickerModal
import com.otlante.finances.ui.components.ListItem
import com.otlante.finances.ui.composition.LocalViewModelFactory
import com.otlante.finances.ui.screens.addOrEditTrans.SharedRefreshIncomeViewModel
import com.otlante.finances.ui.utils.Formatter
import java.time.ZoneId
import java.util.Calendar

@Composable
fun AddOrEditIncomeOrExpenseScreen(
    snackBarHostState: SnackbarHostState,
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
) {

    val factory = LocalViewModelFactory.current
    val viewModel: AddOrEditTransViewModel = viewModel(
        viewModelStoreOwner = navBackStackEntry,
        factory = factory,
    )

    val activity = LocalActivity.current as ViewModelStoreOwner
    val sharedViewModel: SharedRefreshIncomeViewModel = viewModel(
        viewModelStoreOwner = activity,
        factory = LocalViewModelFactory.current
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val account by viewModel.accountFlow.collectAsState()

    uiState.error?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            val result = snackBarHostState.showSnackbar(
                message = errorMessage.description,
                actionLabel = "Retry"
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.fetchAccount(initial = true)
            }
        }
    }

    LaunchedEffect(Unit) {
        AddOrEditTransCallbacksHolder.onConfirm = {
            viewModel.saveChanges()
        }
        AddOrEditTransCallbacksHolder.onCancel = {
            viewModel.cancelChanges()
        }
    }

    LaunchedEffect(uiState.isReadyToLeft) {
        if (uiState.isReadyToLeft) {
            if (uiState.isIncomeTransaction) {
                sharedViewModel.sendRefreshIncomesSignal()
            } else {
                sharedViewModel.sendRefreshExpensesSignal()
            }
            navController.popBackStack()
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }

    MainContent(uiState, account, viewModel)

    Dialogs(uiState, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialogs(
    uiState: AddOrEditIncomeOrExpenseUiState,
    viewModel: AddOrEditTransViewModel,
) {
    when (uiState.activeDialog) {
        ActiveDialog.CATEGORY -> CategoriesDialog(uiState, viewModel)
        ActiveDialog.SUM -> SumDialog(uiState, viewModel)
        ActiveDialog.DATE -> DateDialog(uiState, viewModel)
        ActiveDialog.TIME -> DialWithDialog(
            {
                viewModel.onTimeConfirm(it.hour, it.minute)
            },
            {
                viewModel.onTimeDismiss()
            }
        )

        ActiveDialog.NAME -> NameDialog(uiState, viewModel)
        ActiveDialog.NONE -> {}
    }
}

@Composable
fun NameDialog(uiState: AddOrEditIncomeOrExpenseUiState, viewModel: AddOrEditTransViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.onNameDialogDismiss() },
        title = { Text("Редактировать имя") },
        text = {
            TextField(
                value = uiState.editedName,
                onValueChange = viewModel::onNameChanged,
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { viewModel.onNameDialogConfirm() }) {
                Text("OK")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Отмена")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}

@Composable
fun DateDialog(uiState: AddOrEditIncomeOrExpenseUiState, viewModel: AddOrEditTransViewModel) {
    DatePickerModal(
        selectedDate = uiState.editedDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli(),
        onDateSelected = viewModel::onDateSelected,
        onDismiss = viewModel::onDatePickerDismiss
    )
}

@Composable
fun SumDialog(uiState: AddOrEditIncomeOrExpenseUiState, viewModel: AddOrEditTransViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.onSumEditDismiss() },
        title = { Text("Сумма") },
        text = {
            TextField(
                value = uiState.editedSum,
                onValueChange = viewModel::onSumChanged,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { viewModel.confirmSumEdit() }) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun MainContent(
    uiState: AddOrEditIncomeOrExpenseUiState,
    account: Account?,
    viewModel: AddOrEditTransViewModel,
) {
    Column {
        ListItem(
            title = "Счёт",
            rightText = uiState.account?.name ?: ""
        )
        HorizontalDivider()
        ListItem(
            title = "Статья",
            rightText = uiState.editedCategory?.name ?: "",
            showArrow = true,
            onClick = { viewModel.onCategoryClick() }
        )
        HorizontalDivider()
        ListItem(
            title = "Сумма",
            rightText = "${uiState.checkedSum} ${Formatter.getCurrencySymbol(account?.currency)}",
            showArrow = true,
            onClick = { viewModel.onSumClick() }
        )
        HorizontalDivider()
        ListItem(
            title = "Дата",
            rightText = Formatter.formatHeaderDate(uiState.editedDate),
            showArrow = true,
            onClick = { viewModel.onDateClick() }
        )
        HorizontalDivider()
        ListItem(
            title = "Время",
            rightText = Formatter.formatHeaderTime(uiState.editedTime),
            showArrow = true,
            onClick = { viewModel.onTimeClick() }
        )
        HorizontalDivider()
        ListItem(
            title = uiState.editedName,
            showArrow = true,
            onClick = { viewModel.onNameClick() }
        )
        HorizontalDivider()

        if (uiState.isEditableTransaction) {
            Button(
                { viewModel.deleteTransaction() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Удалить транзакцию", color = Color.White)
            }
        }
    }
}

@Composable
fun CategoriesDialog(
    uiState: AddOrEditIncomeOrExpenseUiState,
    viewModel: AddOrEditTransViewModel,
) {
    AlertDialog(
        onDismissRequest = { viewModel.onCategorySelectDismiss() },
        title = { Text("Выберите категорию") },
        text = {
            Column {
                uiState.incomeCategories?.forEach { category ->
                    Text(
                        text = category.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onCategorySelect(category) }
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    HorizontalDivider()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { viewModel.onCategorySelectDismiss() }) {
                Text("Закрыть")
            }
        }
    )
}

