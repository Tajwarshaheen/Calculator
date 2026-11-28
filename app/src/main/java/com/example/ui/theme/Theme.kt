package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

enum class ThemeMode {
    DARK,
    LIGHT,
    SYSTEM
}

@Immutable
data class TajuExtendedColors(
    val isDark: Boolean,
    val glassSurface: Color,
    val glassSurfaceElevated: Color,
    val glassBorder: Color,
    val goldAccent: Color,
    val goldBright: Color,
    val goldBorder: Color,
    val keyDigitSurface: Color,
    val keyDigitBorder: Color,
    val keyOperatorSurface: Color,
    val keyOperatorBorder: Color,
    val keyFunctionSurface: Color,
    val keyActionSurface: Color,
    val keyActionText: Color,
    val mutedText: Color,
    val errorSurface: Color,
    val errorText: Color
)

private val DarkExtendedColors = TajuExtendedColors(
    isDark = true,
    glassSurface = TajuNavySurface.copy(alpha = 0.88f),
    glassSurfaceElevated = TajuNavyElevated.copy(alpha = 0.92f),
    glassBorder = TajuGlassBorderDark,
    goldAccent = TajuGoldPrimary,
    goldBright = TajuGoldBright,
    goldBorder = TajuGoldBorderDark,
    keyDigitSurface = TajuNavyKey,
    keyDigitBorder = Color(0x26FFFFFF),
    keyOperatorSurface = TajuTealSecondary,
    keyOperatorBorder = Color(0x4D4CA8CE),
    keyFunctionSurface = Color(0xFF0D2036),
    keyActionSurface = Color(0xFF1A2A42),
    keyActionText = Color(0xFFE7C582),
    mutedText = TajuSlateMuted,
    errorSurface = TajuErrorSurfaceDark,
    errorText = TajuErrorDark
)

private val LightExtendedColors = TajuExtendedColors(
    isDark = false,
    glassSurface = TajuLightSurface.copy(alpha = 0.94f),
    glassSurfaceElevated = TajuLightElevated.copy(alpha = 0.96f),
    glassBorder = TajuLightBorder,
    goldAccent = TajuLightGold,
    goldBright = TajuLightGoldBright,
    goldBorder = TajuLightGoldBorder,
    keyDigitSurface = TajuLightKey,
    keyDigitBorder = Color(0x1807111F),
    keyOperatorSurface = TajuTealSecondary,
    keyOperatorBorder = Color(0x33013146),
    keyFunctionSurface = TajuLightKeyAlt,
    keyActionSurface = Color(0xFFF6EFE0),
    keyActionText = Color(0xFF8C6621),
    mutedText = TajuLightMuted,
    errorSurface = TajuErrorSurfaceLight,
    errorText = TajuErrorLight
)

val LocalTajuColors = staticCompositionLocalOf { DarkExtendedColors }

private val TajuDarkColorScheme = darkColorScheme(
    primary = TajuGoldPrimary,
    onPrimary = TajuMidnightBg,
    primaryContainer = TajuTealSecondary,
    onPrimaryContainer = TajuWhiteText,
    secondary = TajuTealSecondary,
    onSecondary = TajuWhiteText,
    secondaryContainer = TajuNavyElevated,
    onSecondaryContainer = TajuWhiteText,
    tertiary = TajuGoldBright,
    onTertiary = TajuMidnightBg,
    background = TajuMidnightBg,
    onBackground = TajuWhiteText,
    surface = TajuNavySurface,
    onSurface = TajuWhiteText,
    surfaceVariant = TajuNavyElevated,
    onSurfaceVariant = TajuSlateMuted,
    outline = TajuGlassBorderDark,
    error = TajuErrorDark,
    onError = TajuMidnightBg
)

private val TajuLightColorScheme = lightColorScheme(
    primary = TajuLightGold,
    onPrimary = TajuWhiteText,
    primaryContainer = Color(0xFFF6E9CC),
    onPrimaryContainer = TajuLightText,
    secondary = TajuTealSecondary,
    onSecondary = TajuWhiteText,
    secondaryContainer = TajuLightElevated,
    onSecondaryContainer = TajuLightText,
    tertiary = TajuTealElevated,
    onTertiary = TajuWhiteText,
    background = TajuLightBg,
    onBackground = TajuLightText,
    surface = TajuLightSurface,
    onSurface = TajuLightText,
    surfaceVariant = TajuLightElevated,
    onSurfaceVariant = TajuLightMuted,
    outline = TajuLightBorder,
    error = TajuErrorLight,
    onError = TajuWhiteText
)

private val TajuShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun TajuCalculatorTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (useDarkTheme) TajuDarkColorScheme else TajuLightColorScheme
    val extendedColors = if (useDarkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !useDarkTheme
                insetsController.isAppearanceLightNavigationBars = !useDarkTheme
            }
        }
    }

    CompositionLocalProvider(LocalTajuColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = TajuTypography,
            shapes = TajuShapes,
            content = content
        )
    }
}
