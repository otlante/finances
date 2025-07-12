package com.otlante.finances.ui.screens.addOrEditTrans

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SharedRefreshIncomeViewModel @Inject constructor() : ViewModel() {

    private val _refreshIncomesSignal = MutableStateFlow(false)
    private val _refreshExpensesSignal = MutableStateFlow(false)
    val refreshIncomesSignal: StateFlow<Boolean> = _refreshIncomesSignal
    val refreshExpensesSignal: StateFlow<Boolean> = _refreshExpensesSignal

    fun sendRefreshIncomesSignal() {
        _refreshIncomesSignal.value = true
    }

    fun resetRefreshIncomesSignal() {
        _refreshIncomesSignal.value = false
    }

    fun sendRefreshExpensesSignal() {
        _refreshExpensesSignal.value = true
    }

    fun resetRefreshExpensesSignal() {
        _refreshExpensesSignal.value = false
    }
}