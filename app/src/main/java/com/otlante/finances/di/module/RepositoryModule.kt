package com.otlante.finances.di.module

import android.content.Context
import com.otlante.finances.data.remote.repository.ApiRepositoryImpl
import com.otlante.finances.data.remote.repository.PinCodeRepository
import com.otlante.finances.data.remote.SettingsDataStore
import com.otlante.finances.domain.repository.ApiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindApiRepository(
        impl: ApiRepositoryImpl
    ): ApiRepository

    companion object {
        @Provides
        @Singleton
        fun provideSettingsDataStore(context: Context): SettingsDataStore = SettingsDataStore(context)
        
        @Provides
        @Singleton
        fun providePinCodeRepository(context: Context): PinCodeRepository = PinCodeRepository(context)
    }
}
