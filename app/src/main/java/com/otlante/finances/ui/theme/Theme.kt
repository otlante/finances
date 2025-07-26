package com.otlante.finances.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.luminance

private fun generateColorScheme(
    primary: Color,
    isDark: Boolean
) = if (isDark) {
    darkColorScheme(
        primary = primary,
        onPrimary = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        primaryContainer = primary.copy(alpha = 0.2f),
        onPrimaryContainer = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        secondary = primary.copy(alpha = 0.7f),
        onSecondary = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        secondaryContainer = primary.copy(alpha = 0.1f),
        onSecondaryContainer = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        surface = Color(0xFF121212),
        onSurface = Color.White,
        surfaceVariant = Color(0xFF1E1E1E),
        onSurfaceVariant = Color.White.copy(alpha = 0.7f),
        background = Color(0xFF000000),
        onBackground = Color.White,
        error = Color(0xFFCF6679),
        onError = Color.Black,
        errorContainer = Color(0xFFCF6679).copy(alpha = 0.2f),
        onErrorContainer = Color.White,
        outline = Color.White.copy(alpha = 0.12f),
        outlineVariant = Color.White.copy(alpha = 0.08f),
        scrim = Color.Black.copy(alpha = 0.32f),
        inverseSurface = Color.White,
        inverseOnSurface = Color.Black,
        inversePrimary = primary.copy(alpha = 0.8f),
        surfaceTint = primary,
        surfaceBright = Color(0xFF1E1E1E),
        surfaceDim = Color(0xFF121212)
    )
} else {
    lightColorScheme(
        primary = primary,
        onPrimary = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        primaryContainer = primary.copy(alpha = 0.2f),
        onPrimaryContainer = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        secondary = primary.copy(alpha = 0.7f),
        onSecondary = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        secondaryContainer = primary.copy(alpha = 0.1f),
        onSecondaryContainer = if (primary.luminance() > 0.5f) Color.Black else Color.White,
        surface = Color(0xFFFFFBFE),
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFFE7E0EC),
        onSurfaceVariant = Color(0xFF49454F),
        background = Color(0xFFFFFBFE),
        onBackground = Color(0xFF1C1B1F),
        error = Color(0xFFBA1A1A),
        onError = Color.White,
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        outline = Color(0xFF79747E),
        outlineVariant = Color(0xFFCAC4D0),
        scrim = Color.Black.copy(alpha = 0.32f),
        inverseSurface = Color(0xFF313033),
        inverseOnSurface = Color(0xFFF4EFF4),
        inversePrimary = primary.copy(alpha = 0.8f),
        surfaceTint = primary,
        surfaceBright = Color(0xFFFFFBFE),
        surfaceDim = Color(0xFFF3EDF7)
    )
}

@Composable
fun FinancesTheme(
    isDarkTheme: Boolean,
    primaryColor: Color,
    content: @Composable () -> Unit
) {
    val colorScheme = generateColorScheme(primaryColor, isDarkTheme)
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
