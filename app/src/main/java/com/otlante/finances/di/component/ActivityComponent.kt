package com.otlante.finances.di.component

import androidx.lifecycle.ViewModelProvider
import com.otlante.finances.MainActivity
import com.otlante.finances.di.module.ViewModelModule
import com.otlante.finances.di.scope.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(activity: MainActivity)

    fun viewModelFactory(): ViewModelProvider.Factory
}
