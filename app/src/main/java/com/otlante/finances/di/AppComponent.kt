package com.otlante.finances.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.otlante.finances.MainActivity
import com.otlante.finances.di.module.ApiModule
import com.otlante.finances.di.module.RepositoryModule
import com.otlante.finances.di.module.ViewModelModule
import com.otlante.finances.domain.repository.ApiRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
    ]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun apiRepository(): ApiRepository

    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }
}