package com.otlante.finances.network

import com.otlante.finances.domain.entity.Account
import com.otlante.finances.domain.entity.Category
import com.otlante.finances.domain.entity.Transaction
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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
}