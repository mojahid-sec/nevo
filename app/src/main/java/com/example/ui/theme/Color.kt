package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * CompositionLocal providing current active NivoColors.
 */
val LocalNivoColors = staticCompositionLocalOf { CrimsonColors }

/**
 * Dynamic theme properties that adapt instantly to whichever of the 10 themes is selected.
 */
val PureBlack: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.barBackground

val DarkBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.background

val CardBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.card

val CardElevated: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.cardElevated

val PrimaryRed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.primaryAccent

val DarkRed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.secondaryAccent

val AccentRed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.accent

val PrimaryText: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.textPrimary

val SecondaryText: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.textSecondary

val SubtleGrey: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.subtle

val DividerGrey: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.outline

val OutlineGrey: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.outline

val SurfaceVariantDark: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalNivoColors.current.cardElevated
