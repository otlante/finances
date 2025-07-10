package com.otlante.finances.di.module

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.otlante.finances.domain.repository.ApiRepository
import com.otlante.finances.ui.screens.account.AccountViewModel
import com.otlante.finances.ui.screens.articles.ArticlesViewModel
import com.otlante.finances.ui.screens.editAccount.EditAccountViewModel
import com.otlante.finances.ui.screens.expenses.ExpensesViewModel
import com.otlante.finances.ui.screens.history.HistoryViewModel
import com.otlante.finances.ui.screens.income.IncomeViewModel
import com.otlante.finances.ui.screens.settings.SettingsViewModel
import com.otlante.finances.ui.screens.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticlesViewModel::class)
    abstract fun bindArticlesViewModel(articlesViewModel: ArticlesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    abstract fun bindEditAccountViewModel(editAccountViewModel: EditAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    abstract fun bindExpensesAccountViewModel(expensesViewModel: ExpensesViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(HistoryViewModel::class)
//    abstract fun bindHistoryViewModelFactory(factory: HistoryViewModel.Factory): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    abstract fun bindIncomeAccountViewModel(incomeViewModel: IncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsAccountViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashAccountViewModel(splashViewModel: SplashViewModel): ViewModel
}
