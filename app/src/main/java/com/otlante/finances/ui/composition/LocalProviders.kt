package com.otlante.finances.ui.composition

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider

val LocalViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("No AssistedViewModelFactoryHolder provided")
}
