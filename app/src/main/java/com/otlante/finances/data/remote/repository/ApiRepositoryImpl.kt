package com.otlante.finances.data.remote.repository

import android.util.Log
import com.otlante.finances.data.remote.ApiService
import com.otlante.finances.data.remote.NetworkError
import com.otlante.finances.data.remote.NoConnectionException
import com.otlante.finances.data.remote.ResultState
import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.entity.UpdateAccountRequest
import com.otlante.finances.domain.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepositoryImpl @Inject constructor(
    private val api: ApiService
) : ApiRepository {

    init {
        Log.d("ApiRepositoryImpl", "REPO CREATED: ")
    }
    private val _accountFlow = MutableStateFlow<Account?>(null)
    override val accountFlow: StateFlow<Account?> get() = _accountFlow.asStateFlow()

    private suspend fun resolveCurrentAccountId(): Int {
        val accounts = api.getAccounts()
        val firstAccountId = accounts.firstOrNull()?.id
        check(firstAccountId != null) { "No accounts found for the user." }
        return firstAccountId
    }

    companion object {
        private const val SERVER_ERROR_START_CODE = 500
    }

    override suspend fun getHistory(
        startDate: String,
        endDate: String
    ): ResultState<List<Transaction>> {
        return safeNetworkCall {
            val accountId = resolveCurrentAccountId()
            api.getTransactionsForPeriod(accountId, startDate, endDate)
        }
    }

    override suspend fun updateAccount(
        name: String,
        balance: String,
        currency: String
    ): ResultState<Account> {
        return safeNetworkCall {
            val accountId = resolveCurrentAccountId()
            val request = UpdateAccountRequest(name, balance, currency)
            val res = api.updateAccount(accountId, request)
            _accountFlow.value = res
            res
        }
    }

    override suspend fun getExpenseTransactions(): ResultState<List<Transaction>> {
        return when (val result = getHistoryForCurrentMonth()) {
            is ResultState.Success -> {
                val expenses = result.data.filter { !it.category.isIncome }
                if (expenses.isNotEmpty()) {
                    _accountFlow.value = expenses[0].account
                }
                ResultState.Success(expenses)
            }

            is ResultState.Error -> result
        }
    }

    override suspend fun getIncomeTransactions(): ResultState<List<Transaction>> {
        return when (val result = getHistoryForCurrentMonth()) {
            is ResultState.Success -> {
                val incomes = result.data.filter { it.category.isIncome }
                if (incomes.isNotEmpty()) {
                    _accountFlow.value = incomes[0].account
                }
                ResultState.Success(incomes)
            }

            is ResultState.Error -> result
        }
    }

    override suspend fun getMainAccount(): ResultState<Account> {
        return safeNetworkCall {
            val accountId = resolveCurrentAccountId()
            val res = api.getAccountById(accountId)
            _accountFlow.value = res
            res
        }
    }

    override suspend fun getAllCategories(): ResultState<List<Category>> {
        return safeNetworkCall {
            api.getAllCategories()
        }
    }

    private suspend fun getHistoryForCurrentMonth(): ResultState<List<Transaction>> {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return getHistory(startOfMonth.format(formatter), today.format(formatter))
    }

    private suspend inline fun <T> safeNetworkCall(crossinline apiCall: suspend () -> T): ResultState<T> =
        withContext(Dispatchers.IO) {
            try {
                ResultState.Success(apiCall.invoke())
            } catch (e: Exception) {
                ResultState.Error(mapToNetworkError(e))
            }
        }


    private fun mapToNetworkError(e: Exception): NetworkError = when (e) {
        is NoConnectionException -> NetworkError.NoInternetError
        is HttpException -> {
            if (e.code() >= SERVER_ERROR_START_CODE) {
                NetworkError.ServerError
            } else {
                NetworkError.UnknownError(e)
            }
        }

        else -> NetworkError.UnknownError(e)
    }
}
