package com.otlante.finances.di.component

import android.content.Context
import com.otlante.finances.di.module.ApiModule
import com.otlante.finances.di.module.RepositoryModule
import com.otlante.finances.domain.repository.ApiRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        RepositoryModule::class,
    ]
)
interface AppComponent {

    fun apiRepository(): ApiRepository

    fun activityComponent(): ActivityComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }
}
