package com.otlante.finances.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.otlante.finances.di.scope.ActivityScope
import com.otlante.finances.ui.screens.addOrEditTrans.AddOrEditTransViewModel
import com.otlante.finances.ui.screens.history.HistoryViewModel
import javax.inject.Inject
import javax.inject.Provider

@ActivityScope
class ViewModelFactory @Inject constructor(
    private val viewModelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>,
    private val historyViewModelFactoryProvider: Provider<HistoryViewModel.Factory>,
    private val addOrEditTransViewModelFactoryProvider: Provider<AddOrEditTransViewModel.Factory>,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        var creator: Provider<out ViewModel>? = viewModelProviders[modelClass]
        if (creator == null) {
            for ((key, value) in viewModelProviders) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        if (creator != null) {
            return creator.get() as T
        }

        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            return historyViewModelFactoryProvider.get().create(savedStateHandle) as T
        }

        if (modelClass.isAssignableFrom(AddOrEditTransViewModel::class.java)) {
            val savedStateHandle = extras.createSavedStateHandle()
            return addOrEditTransViewModelFactoryProvider.get().create(savedStateHandle) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}
