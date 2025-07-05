package com.otlante.finances.data.remote

import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.entity.Transaction
import com.otlante.finances.domain.entity.UpdateAccountRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service interface defining endpoints
 * for accessing accounts, transactions, and categories.
 */
interface ApiService {
    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") id: Int): Account

    @GET("accounts")
    suspend fun getAccounts(): List<Account>

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsForPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<Transaction>

    @GET("categories")
    suspend fun getAllCategories(): List<Category>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") id: Int,
        @Body request: UpdateAccountRequest
    ): Account
}
