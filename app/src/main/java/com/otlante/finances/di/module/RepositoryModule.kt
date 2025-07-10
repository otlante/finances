package com.otlante.finances.di.module

import com.otlante.finances.data.remote.repository.ApiRepositoryImpl
import com.otlante.finances.domain.repository.ApiRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindApiRepository(
        impl: ApiRepositoryImpl
    ): ApiRepository
}
