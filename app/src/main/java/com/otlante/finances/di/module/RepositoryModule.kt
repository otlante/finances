package com.otlante.finances.di.module

import com.otlante.finances.data.remote.repository.ApiRepositoryImpl
import com.otlante.finances.domain.repository.ApiRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindApiRepository(
        impl: ApiRepositoryImpl
    ): ApiRepository
}
