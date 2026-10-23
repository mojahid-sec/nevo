package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun MyApplicationTheme(
    theme: NivoTheme = NivoTheme.CRIMSON,
    content: @Composable () -> Unit
) {
    val nivoColors = remember(theme) { getThemeColors(theme) }

    val colorScheme = remember(nivoColors) {
        if (nivoColors.isLight) {
            lightColorScheme(
                primary = nivoColors.primaryAccent,
                onPrimary = Color.White,
                primaryContainer = nivoColors.secondaryAccent,
                onPrimaryContainer = Color.White,
                secondary = nivoColors.accent,
                onSecondary = Color.White,
                secondaryContainer = nivoColors.cardElevated,
                onSecondaryContainer = nivoColors.textPrimary,
                background = nivoColors.background,
                onBackground = nivoColors.textPrimary,
                surface = nivoColors.card,
                onSurface = nivoColors.textPrimary,
                surfaceVariant = nivoColors.cardElevated,
                onSurfaceVariant = nivoColors.textSecondary,
                outline = nivoColors.outline,
                error = nivoColors.primaryAccent,
                onError = Color.White
            )
        } else {
            darkColorScheme(
                primary = nivoColors.primaryAccent,
                onPrimary = nivoColors.textPrimary,
                primaryContainer = nivoColors.secondaryAccent,
                onPrimaryContainer = nivoColors.textPrimary,
                secondary = nivoColors.accent,
                onSecondary = nivoColors.textPrimary,
                secondaryContainer = nivoColors.cardElevated,
                onSecondaryContainer = nivoColors.textPrimary,
                background = nivoColors.background,
                onBackground = nivoColors.textPrimary,
                surface = nivoColors.card,
                onSurface = nivoColors.textPrimary,
                surfaceVariant = nivoColors.cardElevated,
                onSurfaceVariant = nivoColors.textSecondary,
                outline = nivoColors.outline,
                error = nivoColors.primaryAccent,
                onError = nivoColors.textPrimary
            )
        }
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = nivoColors.barBackground.toArgb()
                window.navigationBarColor = nivoColors.barBackground.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = nivoColors.isLight
                insetsController.isAppearanceLightNavigationBars = nivoColors.isLight
            }
        }
    }

    CompositionLocalProvider(
        LocalNivoColors provides nivoColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
