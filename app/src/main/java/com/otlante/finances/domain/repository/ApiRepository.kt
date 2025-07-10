package com.otlante.finances.domain.repository

import com.otlante.finances.data.remote.ResultState
import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.entity.Transaction
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository interface that defines methods to fetch financial data
 * such as transactions, accounts, and categories from a data source.
 */
interface ApiRepository {

    val accountFlow: StateFlow<Account?>

    /**
     * Retrieves expense transactions.
     *
     * @return a [ResultState] containing a list of expense [Transaction]s
     */
    suspend fun getExpenseTransactions(): ResultState<List<Transaction>>

    /**
     * Retrieves income transactions.
     *
     * @return a [ResultState] containing a list of income [Transaction]s
     */
    suspend fun getIncomeTransactions(): ResultState<List<Transaction>>

    /**
     * Retrieves the main user account.
     *
     * @return a [ResultState] containing the main [Account]
     */
    suspend fun getMainAccount(): ResultState<Account>

    /**
     * Retrieves all transaction categories.
     *
     * @return a [ResultState] containing a list of [Category] objects
     */
    suspend fun getAllCategories(): ResultState<List<Category>>

    /**
     * Retrieves transaction history between the specified start and end dates.
     *
     * @param startDate start date of the period in ISO format
     * @param endDate end date of the period in ISO format
     * @return a [ResultState] containing a list of [Transaction]s in the specified period
     */
    suspend fun getHistory(
        startDate: String,
        endDate: String
    ): ResultState<List<Transaction>>

    suspend fun updateAccount(
        name: String,
        balance: String,
        currency: String,
    ): ResultState<Account>
}
