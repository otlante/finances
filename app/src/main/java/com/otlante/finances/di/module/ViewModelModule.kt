package com.otlante.finances.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.otlante.finances.di.factory.ViewModelFactory
import com.otlante.finances.di.key.ViewModelKey
import com.otlante.finances.ui.screens.addOrEditTrans.SharedRefreshIncomeViewModel
import com.otlante.finances.ui.screens.account.AccountViewModel
import com.otlante.finances.ui.screens.articles.ArticlesViewModel
import com.otlante.finances.ui.screens.editAccount.EditAccountViewModel
import com.otlante.finances.ui.screens.expenses.ExpensesViewModel
import com.otlante.finances.ui.screens.income.IncomeViewModel
import com.otlante.finances.ui.screens.settings.SettingsViewModel
import com.otlante.finances.ui.screens.splash.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticlesViewModel::class)
    fun bindArticlesViewModel(articlesViewModel: ArticlesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    fun bindEditAccountViewModel(editAccountViewModel: EditAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    fun bindExpensesViewModel(expensesViewModel: ExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    fun bindIncomeViewModel(incomeViewModel: IncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SharedRefreshIncomeViewModel::class)
    fun bindSharedRefreshIncomeViewModel(sharedRefreshIncomeViewModel: SharedRefreshIncomeViewModel): ViewModel
}
