package com.otlante.finances.domain.repository

import com.otlante.finances.network.ResultState
import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.entity.Transaction

interface ApiRepository {
    suspend fun getExpenseTransactions(): ResultState<List<Transaction>>
    suspend fun getIncomeTransactions(): ResultState<List<Transaction>>
    suspend fun getMainAccount(): ResultState<Account>
    suspend fun getAllCategories(): ResultState<List<Category>>
    suspend fun getHistory(
        startDate: String,
        endDate: String
    ): ResultState<List<Transaction>>
}